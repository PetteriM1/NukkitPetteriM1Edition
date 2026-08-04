/*
 * Copyright (C) 2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iq80.leveldb.impl;

import cn.nukkit.Nukkit;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.io.Closer;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongIterator;
import org.iq80.leveldb.*;
import org.iq80.leveldb.env.*;
import org.iq80.leveldb.impl.Filename.FileInfo;
import org.iq80.leveldb.impl.Filename.FileType;
import org.iq80.leveldb.impl.WriteBatchImpl.Handler;
import org.iq80.leveldb.iterator.*;
import org.iq80.leveldb.table.*;
import org.iq80.leveldb.util.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.iq80.leveldb.impl.DbConstants.*;
import static org.iq80.leveldb.impl.SequenceNumber.MAX_SEQUENCE_NUMBER;
import static org.iq80.leveldb.impl.ValueType.DELETION;
import static org.iq80.leveldb.impl.ValueType.VALUE;
import static org.iq80.leveldb.util.SizeOf.SIZE_OF_INT;
import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;
import static org.iq80.leveldb.util.Slices.readLengthPrefixedBytes;
import static org.iq80.leveldb.util.Slices.writeLengthPrefixedBytes;

@SuppressWarnings("AccessingNonPublicFieldOfAnotherObject")
public class DbImpl
        implements DB {
    private final Options options;
    private final boolean ownsLogger;
    private final File databaseDir;
    private final TableCache tableCache;
    private final DbLock dbLock;
    private final VersionSet versions;

    private final AtomicBoolean shuttingDown = new AtomicBoolean();
    private final ReentrantLock mutex = new ReentrantLock();
    private final Condition backgroundCondition = mutex.newCondition();

    private final LongArrayList pendingOutputs = new LongArrayList(); // todo
    private final Deque<WriteBatchInternal> writers = new LinkedList<>();
    private final SnapshotList snapshots = new SnapshotList(mutex);
    private final WriteBatchImpl tmpBatch = new WriteBatchImpl();
    private final Env env;

    private LogWriter log;

    private MemTable memTable;
    private volatile MemTable immutableMemTable;

    private final InternalKeyComparator internalKeyComparator;

    private volatile Throwable backgroundException;
    private final ExecutorService compactionExecutor;
    private Future<?> backgroundCompaction;

    private ManualCompaction manualCompaction;

    private final CompactionStats[] stats = new CompactionStats[DbConstants.NUM_LEVELS];

    public DbImpl(Options rawOptions, String dbname, Env env)
            throws IOException {
        this.env = env;
        Objects.requireNonNull(rawOptions);
        Objects.requireNonNull(dbname);
        final File databaseDir = env.toFile(dbname);
        this.options = sanitizeOptions(databaseDir, rawOptions);
        this.ownsLogger = this.options.logger() != rawOptions.logger();

        if (this.options.compressionType() == CompressionType.SNAPPY && !Snappy.available()) {
            // Disable snappy if it's not available.
            this.options.compressionType(CompressionType.NONE);
        }

        this.databaseDir = databaseDir;

        if (this.options.filterPolicy() != null) {
            checkArgument(this.options.filterPolicy() instanceof FilterPolicy, "Filter policy must implement Java interface FilterPolicy");
            this.options.filterPolicy(InternalFilterPolicy.convert(this.options.filterPolicy()));
        }

        //use custom comparator if set
        DBComparator comparator = options.comparator();
        UserComparator userComparator;
        if (comparator != null) {
            userComparator = new CustomUserComparator(comparator);
        } else {
            userComparator = new BytewiseComparator();
        }
        internalKeyComparator = new InternalKeyComparator(userComparator);
        immutableMemTable = null;

        ThreadFactory compactionThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("leveldb-" + databaseDir.getName() + "-%s")
                .setUncaughtExceptionHandler((t, e) -> {
                    mutex.lock();
                    try {
                        if (backgroundException == null) {
                            backgroundException = e;
                        }
                        if (Nukkit.DEBUG > 1) options.logger().log("Unexpected exception occurred %s", e);
                    } finally {
                        mutex.unlock();
                    }
                })
                .build();
        compactionExecutor = Executors.newSingleThreadExecutor(compactionThreadFactory);

        // Reserve ten files or so for other uses and give the rest to TableCache.
        int tableCacheSize = options.maxOpenFiles() - DbConstants.NUM_NON_TABLE_CACHE_FILES;
        tableCache = new TableCache(databaseDir, tableCacheSize, new InternalUserComparator(internalKeyComparator), options, env);

        // create the version set

        // create the database dir if it does not already exist
        databaseDir.mkdirs();
        checkArgument(databaseDir.exists(), "Database directory '%s' does not exist and could not be created", databaseDir);
        checkArgument(databaseDir.isDirectory(), "Database directory '%s' is not a directory", databaseDir);

        for (int i = 0; i < DbConstants.NUM_LEVELS; i++) {
            stats[i] = new CompactionStats();
        }

        mutex.lock();
        Closer c = Closer.create();
        boolean success = false;
        try {
            // lock the database dir
            this.dbLock = env.tryLock(databaseDir.child(Filename.lockFileName()));
            c.register(dbLock::release);
            //<editor-fold desc="Recover">
            // verify the "current" file
            File currentFile = databaseDir.child(Filename.currentFileName());
            if (!currentFile.canRead()) {
                checkArgument(options.createIfMissing(), "Database '%s' does not exist and the create if missing option is disabled", databaseDir);
                /** @see VersionSet#initializeIfNeeded() newDB() **/
            } else {
                checkArgument(!options.errorIfExists(), "Database '%s' exists and the error if exists option is enabled", databaseDir);
            }

            this.versions = new VersionSet(options, databaseDir, tableCache, internalKeyComparator, env);
            c.register(versions::release);
            // load  (and recover) current version
            boolean saveManifest = versions.recover();

            // Recover from all newer log files than the ones named in the
            // descriptor (new log files may have been added by the previous
            // incarnation without registering them in the descriptor).
            //
            // Note that PrevLogNumber() is no longer used, but we pay
            // attention to it in case we are recovering a database
            // produced by an older version of leveldb.
            long minLogNumber = versions.getLogNumber();
            long previousLogNumber = versions.getPrevLogNumber();
            final Set<Long> expected = versions.getLiveFiles().stream().map(FileMetaData::getNumber).collect(Collectors.toSet());
            List<File> filenames = databaseDir.listFiles();

            LongArrayList logs = new LongArrayList();
            for (File filename : filenames) {
                FileInfo fileInfo = Filename.parseFileName(filename);
                if (fileInfo != null) {
                    expected.remove(fileInfo.getFileNumber());
                    if (fileInfo.getFileType() == FileType.LOG &&
                            ((fileInfo.getFileNumber() >= minLogNumber) || (fileInfo.getFileNumber() == previousLogNumber))) {
                        logs.add(fileInfo.getFileNumber());
                    }
                }
            }

            checkArgument(expected.isEmpty(), "%s missing files", expected.size());

            // Recover in the order in which the logs were generated
            VersionEdit edit = new VersionEdit();
            Collections.sort(logs);
            for (LongIterator iterator = logs.longIterator(); iterator.hasNext(); ) {
                long fileNumber = iterator.nextLong();
                RecoverResult result = recoverLogFile(fileNumber, !iterator.hasNext(), edit);
                saveManifest |= result.saveManifest;

                // The previous incarnation may not have written any MANIFEST
                // records after allocating this log number.  So we manually
                // update the file number allocation counter in VersionSet.
                this.versions.markFileNumberUsed(fileNumber);

                if (versions.getLastSequence() < result.maxSequence) {
                    versions.setLastSequence(result.maxSequence);
                }
            }
            //</editor-fold>

            // open transaction log
            if (memTable == null) {
                long logFileNumber = versions.getNextFileNumber();
                this.log = Logs.createLogWriter(databaseDir.child(Filename.logFileName(logFileNumber)), logFileNumber, env);
                c.register(log);
                edit.setLogNumber(log.getFileNumber());
                memTable = new MemTable(internalKeyComparator);
            }

            if (saveManifest) {
                edit.setPreviousLogNumber(0);
                edit.setLogNumber(log.getFileNumber());
                // apply recovered edits
                versions.logAndApply(edit, mutex);
            }

            // cleanup unused files
            deleteObsoleteFiles();

            // schedule compactions
            maybeScheduleCompaction();
            success = true;
        } catch (Throwable e) {
            throw c.rethrow(e);
        } finally {
            if (!success) {
                if (ownsLogger) { //only close logger if created internally
                    c.register(this.options.logger());
                }
                c.close();
            }
            mutex.unlock();
        }
    }

    // Fix user-supplied options to be reasonable
    private static <T extends Comparable<T>> T clipToRange(T in, T min, T max) {
        if (in.compareTo(min) < 0) {
            return min;
        }
        if (in.compareTo(max) > 0) {
            return max;
        }
        return in;
    }

    /**
     * Ensure we do not use external values as is. Ensure value are in correct ranges
     * and a copy of external Options instance is used.
     */
    private Options sanitizeOptions(File databaseDir, Options src) throws IOException {
        final Options result = Options.fromOptions(src);
        result.maxOpenFiles(clipToRange(src.maxOpenFiles(), 64 + NUM_NON_TABLE_CACHE_FILES, 50000));
        result.writeBufferSize(clipToRange(src.writeBufferSize(), 64 << 10, 1 << 30));
        result.maxFileSize(clipToRange(src.maxFileSize(), 1 << 20, 1 << 30));
        result.blockSize(clipToRange(src.blockSize(), 1 << 10, 4 << 20));
        if (result.logger() == null && databaseDir != null && (databaseDir.isDirectory() || databaseDir.mkdirs())) {
            File file = databaseDir.child(Filename.infoLogFileName());
            file.renameTo(databaseDir.child(Filename.oldInfoLogFileName()));
            result.logger(env.newLogger(file));
        }
        if (result.logger() == null) {
            result.logger(new NoOpLogger());
        }
        return result;
    }

    /**
     * Wait for all background activity to finish and invalidate all cache.
     * Only used to test that all file handles are closed correctly.
     */
    void invalidateAllCaches() {
        mutex.lock();
        try {
            while (backgroundCompaction != null && backgroundException == null) {
                backgroundCondition.awaitUninterruptibly();
            }
            tableCache.invalidateAll();
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void close() {
        if (shuttingDown.getAndSet(true)) {
            return;
        }

        mutex.lock();
        try {
            while (backgroundCompaction != null) {
                backgroundCondition.awaitUninterruptibly();
            }
        } finally {
            mutex.unlock();
        }

        compactionExecutor.shutdown();
        try {
            compactionExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            versions.release();
        } catch (IOException ignored) {
        }
        try {
            log.close();
        } catch (IOException ignored) {
        }
        tableCache.close();
        if (ownsLogger) {
            Closeables.closeQuietly(options.logger());
        }
        dbLock.release();
    }

    @Override
    public String getProperty(String name) {
        if (!name.startsWith("leveldb.")) {
            return null;
        }
        String key = name.substring("leveldb.".length());
        mutex.lock();
        try {
            Matcher matcher;
            matcher = Pattern.compile("num-files-at-level(\\d+)")
                    .matcher(key);
            if (matcher.matches()) {
                final int level = Integer.parseInt(matcher.group(1));
                return String.valueOf(versions.numberOfFilesInLevel(level));
            }
            matcher = Pattern.compile("stats")
                    .matcher(key);
            if (matcher.matches()) {
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("                               Compactions\n");
                stringBuilder.append("Level  Files Size(MB) Time(sec) Read(MB) Write(MB)\n");
                stringBuilder.append("--------------------------------------------------\n");
                for (int level = 0; level < DbConstants.NUM_LEVELS; level++) {
                    int files = versions.numberOfFilesInLevel(level);
                    if (stats[level].micros > 0 || files > 0) {
                        stringBuilder.append(String.format(
                                "%3d %8d %8.0f %9.0f %8.0f %9.0f%n",
                                level,
                                files,
                                versions.numberOfBytesInLevel(level) / 1048576.0,
                                stats[level].micros / 1e6,
                                stats[level].bytesRead / 1048576.0,
                                stats[level].bytesWritten / 1048576.0));
                    }
                }
                return stringBuilder.toString();
            } else if ("sstables".equals(key)) {
                return versions.getCurrent().toString();
            } else if ("approximate-memory-usage".equals(key)) {
                long sizeTotal = tableCache.getApproximateMemoryUsage();
                if (memTable != null) {
                    sizeTotal += memTable.approximateMemoryUsage();
                }
                if (immutableMemTable != null) {
                    sizeTotal += immutableMemTable.approximateMemoryUsage();
                }
                return Long.toUnsignedString(sizeTotal);
            }
        } finally {
            mutex.unlock();
        }
        return null;
    }

    private void deleteObsoleteFiles() {
        checkState(mutex.isHeldByCurrentThread());
        if (backgroundException != null) {
            return;
        }
        // Make a set of all of the live files
        LongArrayList live = new LongArrayList(this.pendingOutputs);
        for (FileMetaData fileMetaData : versions.getLiveFiles()) {
            live.add(fileMetaData.getNumber());
        }

        final List<File> filesToDelete = new ArrayList<>();
        for (File file : databaseDir.listFiles()) {
            FileInfo fileInfo = Filename.parseFileName(file);
            if (fileInfo == null) {
                continue;
            }
            long number = fileInfo.getFileNumber();
            boolean keep = true;
            switch (fileInfo.getFileType()) {
                case LOG:
                    keep = ((number >= versions.getLogNumber()) ||
                            (number == versions.getPrevLogNumber()));
                    break;
                case DESCRIPTOR:
                    // Keep my manifest file, and any newer incarnations'
                    // (in case there is a race that allows other incarnations)
                    keep = (number >= versions.getManifestFileNumber());
                    break;
                case TABLE:
                    keep = live.contains(number);
                    break;
                case TEMP:
                    // Any temp files that are currently being written to must
                    // be recorded in pending_outputs_, which is inserted into "live"
                    keep = live.contains(number);
                    break;
                case CURRENT:
                case DB_LOCK:
                case INFO_LOG:
                    keep = true;
                    break;
            }

            if (!keep) {
                if (fileInfo.getFileType() == FileType.TABLE) {
                    tableCache.evict(number);
                }
                if (Nukkit.DEBUG > 1) options.logger().log("Delete type=%s #%s",
                        fileInfo.getFileType(),
                        number);
                filesToDelete.add(file);
            }
        }
        // While deleting all files unblock other threads. All files being deleted
        // have unique names which will not collide with newly created files and
        // are therefore safe to delete while allowing other threads to proceed.
        mutex.unlock();
        try {
            filesToDelete.forEach(File::delete);
        } finally {
            mutex.lock();
        }
    }

    private void maybeScheduleCompaction() {
        checkState(mutex.isHeldByCurrentThread());

        if (backgroundCompaction != null) {
            // Already scheduled
        } else if (shuttingDown.get()) {
            // DB is being shutdown; no more background compactions
        } else if (backgroundException != null) {
            // Already got an error; no more changes
        } else if (immutableMemTable == null &&
                manualCompaction == null &&
                !versions.needsCompaction()) {
            // No work to be done
        } else {
            backgroundCompaction = compactionExecutor.submit(this::backgroundCall);
        }
    }

    private void checkBackgroundException() {
        Throwable e = backgroundException;
        if (e != null) {
            throw new BackgroundProcessingException(e);
        }
    }

    private void backgroundCall() {
        mutex.lock();
        try {
            checkState(backgroundCompaction != null, "Compaction was not correctly scheduled");

            try {
                if (!shuttingDown.get() && backgroundException == null) {
                    backgroundCompaction();
                }
            } finally {
                backgroundCompaction = null;
            }
            // Previous compaction may have produced too many files in a level,
            // so reschedule another compaction if needed.
            maybeScheduleCompaction();
        } catch (DatabaseShutdownException ignored) {
        } catch (Throwable throwable) {
            recordBackgroundError(throwable);
        } finally {
            try {
                backgroundCondition.signalAll();
            } finally {
                mutex.unlock();
            }
        }
    }

    private void backgroundCompaction()
            throws IOException {
        checkState(mutex.isHeldByCurrentThread());

        if (immutableMemTable != null) {
            compactMemTable();
            return;
        }

        Compaction compaction;
        InternalKey manualEnd = null;
        boolean isManual = manualCompaction != null;
        if (isManual) {
            ManualCompaction m = this.manualCompaction;
            compaction = versions.compactRange(m.level, m.begin, m.end);
            m.done = compaction == null;
            if (compaction != null) {
                manualEnd = compaction.input(0, compaction.getLevelInputs().size() - 1).getLargest();
            }
            if (Nukkit.DEBUG > 1) options.logger().log(
                    "Manual compaction at level-%s from %s .. %s; will stop at %s",
                    m.level,
                    (m.begin != null ? m.begin.toString() : "(begin)"),
                    (m.end != null ? m.end.toString() : "(end)"),
                    (m.done ? "(end)" : manualEnd)
            );
        } else {
            compaction = versions.pickCompaction();
        }

        if (compaction == null) {
            // no compaction
        } else if (!isManual && compaction.isTrivialMove()) {
            // Move file to next level
            checkState(compaction.getLevelInputs().size() == 1);
            FileMetaData fileMetaData = compaction.getLevelInputs().get(0);
            compaction.getEdit().deleteFile(compaction.getLevel(), fileMetaData.getNumber());
            compaction.getEdit().addFile(compaction.getLevel() + 1, fileMetaData);
            versions.logAndApply(compaction.getEdit(), mutex);
            if (Nukkit.DEBUG > 1) options.logger().log("Moved #%s to level-%s %s bytes: %s",
                    fileMetaData.getNumber(),
                    compaction.getLevel() + 1,
                    fileMetaData.getFileSize(),
                    versions.levelSummary());
        } else {
            CompactionState compactionState = new CompactionState(compaction);
            try {
                doCompactionWork(compactionState);
            } catch (Exception e) {
                if (Nukkit.DEBUG > 1) options.logger().log(
                        "Compaction error: %s", e.getMessage());
                recordBackgroundError(e);
            } finally {
                cleanupCompaction(compactionState);
                compaction.close(); //release resources
                deleteObsoleteFiles();
            }
        }
        if (compaction != null) {
            compaction.close();
        }

        // manual compaction complete
        if (isManual) {
            ManualCompaction m = manualCompaction;
            if (backgroundException != null) {
                m.done = true;
            }
            if (!m.done) {
                m.begin = manualEnd;
            }
            manualCompaction = null;
        }
    }

    private void recordBackgroundError(Throwable e) {
        checkState(mutex.isHeldByCurrentThread());
        Throwable backgroundException = this.backgroundException;
        if (backgroundException == null) {
            this.backgroundException = e;
            backgroundCondition.signalAll();
        }
        Throwables.throwIfInstanceOf(e, Error.class);
    }

    private void cleanupCompaction(CompactionState compactionState) throws IOException {
        checkState(mutex.isHeldByCurrentThread());

        if (compactionState.builder != null) {
            compactionState.builder.abandon();
            compactionState.builder = null;
        }
        if (compactionState.outfile != null) {
            //an error as occurred but we need to release the resources!
            compactionState.outfile.force();
            compactionState.outfile.close();
            compactionState.outfile = null;
        }

        for (FileMetaData output : compactionState.outputs) {
            pendingOutputs.remove(output.getNumber());
        }
    }

    private static class RecoverResult {
        long maxSequence;
        boolean saveManifest;

        public RecoverResult(long maxSequence, boolean saveManifest) {
            this.maxSequence = maxSequence;
            this.saveManifest = saveManifest;
        }
    }

    private RecoverResult recoverLogFile(long fileNumber, boolean lastLog, VersionEdit edit)
            throws IOException {
        checkState(mutex.isHeldByCurrentThread());
        File file = databaseDir.child(Filename.logFileName(fileNumber));
        try (SequentialFile in = env.newSequentialFile(file)) {
            LogMonitor logMonitor = LogMonitors.logMonitor(options.logger());

            // We intentionally make LogReader do checksumming even if
            // paranoidChecks==false so that corruptions cause entire commits
            // to be skipped instead of propagating bad information (like overly
            // large sequence numbers).
            LogReader logReader = new LogReader(in, logMonitor, true, 0);

            if (Nukkit.DEBUG > 1) options.logger().log("Recovering log #%s",
                    fileNumber);

            // Read all the records and add to a memtable
            long maxSequence = 0;
            int compactions = 0;
            boolean saveManifest = false;
            MemTable mem = null;
            for (Slice record = logReader.readRecord(); record != null; record = logReader.readRecord()) {
                SliceInput sliceInput = record.input();
                // read header
                if (sliceInput.available() < 12) {
                    logMonitor.corruption(sliceInput.available(), "log record too small");
                    continue;
                }
                long sequenceBegin = sliceInput.readLong();
                int updateSize = sliceInput.readInt();

                // read entries
                try (WriteBatchImpl writeBatch = readWriteBatch(sliceInput, updateSize)) {
                    // apply entries to memTable
                    if (mem == null) {
                        mem = new MemTable(internalKeyComparator);
                    }
                    writeBatch.forEach(new InsertIntoHandler(mem, sequenceBegin));
                } catch (Exception e) {
                    if (!options.paranoidChecks()) {
                        if (Nukkit.DEBUG > 1) options.logger().log("Ignoring error %s", e);
                    }
                    Throwables.propagateIfPossible(e, IOException.class);
                    throw new IOException(e);
                }

                // update the maxSequence
                long lastSequence = sequenceBegin + updateSize - 1;
                if (lastSequence > maxSequence) {
                    maxSequence = lastSequence;
                }

                // flush mem table if necessary
                if (mem.approximateMemoryUsage() > options.writeBufferSize()) {
                    compactions++;
                    saveManifest = true;
                    writeLevel0Table(mem, edit, null);
                    mem = null;
                }
            }

            // See if we should keep reusing the last log file.
            if (options.reuseLogs() && lastLog && compactions == 0) {
                Preconditions.checkState(this.log == null);
                Preconditions.checkState(this.memTable == null);
                long originalSize = file.length();
                final WritableFile writableFile = env.newAppendableFile(file);
                if (Nukkit.DEBUG > 1) options.logger().log("Reusing old log %s", file);
                this.log = Logs.createLogWriter(fileNumber, writableFile, originalSize);
                if (mem != null) {
                    this.memTable = mem;
                    mem = null;
                } else {
                    // mem can be NULL if lognum exists but was empty.
                    this.memTable = new MemTable(internalKeyComparator);
                }
            }

            // flush mem table
            if (mem != null && !mem.isEmpty()) {
                saveManifest = true;
                writeLevel0Table(mem, edit, null);
            }

            return new RecoverResult(maxSequence, saveManifest);
        }
    }

    @Override
    public byte[] get(byte[] key)
            throws DBException {
        return get(key, Defaults.DEFAULT_READ_OPTIONS);
    }

    @Override
    public byte[] get(byte[] key, ReadOptions options)
            throws DBException {
        LookupKey lookupKey;
        LookupResult lookupResult;
        mutex.lock();
        try {
            long lastSequence = options.snapshot() != null ?
                    snapshots.getSequenceFrom(options.snapshot()) : versions.getLastSequence();
            lookupKey = new LookupKey(Slices.wrappedBuffer(key), lastSequence);

            // First look in the memtable, then in the immutable memtable (if any).
            final MemTable memTable = this.memTable;
            final MemTable immutableMemTable = this.immutableMemTable;
            final Version current = versions.getCurrent();
            current.retain();
            ReadStats readStats = null;
            mutex.unlock();
            try {
                lookupResult = memTable.get(lookupKey);
                if (lookupResult == null && immutableMemTable != null) {
                    lookupResult = immutableMemTable.get(lookupKey);
                }

                if (lookupResult == null) {
                    // Not in memTables; try live files in level order
                    readStats = new ReadStats();
                    lookupResult = current.get(options, lookupKey, readStats);
                }

                // schedule compaction if necessary
            } finally {
                mutex.lock();
                if (false && readStats != null && current.updateStats(readStats)) { // We rather take slightly slower seeks than constantly attempting to compact
                    maybeScheduleCompaction();
                }
                current.release();
            }
        } finally {
            mutex.unlock();
        }

        if (lookupResult != null) {
            Slice value = lookupResult.getValue();
            if (value != null) {
                return value.getBytes();
            }
        }
        return null;
    }

    @Override
    public void put(byte[] key, byte[] value)
            throws DBException {
        put(key, value, Defaults.DEFAULT_WRITE_OPTIONS);
    }

    @Override
    public Snapshot put(byte[] key, byte[] value, WriteOptions options)
            throws DBException {
        try (WriteBatchImpl writeBatch = new WriteBatchImpl()) {
            return writeInternal(writeBatch.put(key, value), options);
        }
    }

    @Override
    public void delete(byte[] key)
            throws DBException {
        delete(key, Defaults.DEFAULT_WRITE_OPTIONS);
    }

    @Override
    public Snapshot delete(byte[] key, WriteOptions options)
            throws DBException {
        try (WriteBatchImpl writeBatch = new WriteBatchImpl()) {
            return writeInternal(writeBatch.delete(key), options);
        }
    }

    @Override
    public void write(WriteBatch updates)
            throws DBException {
        writeInternal((WriteBatchImpl) updates, Defaults.DEFAULT_WRITE_OPTIONS);
    }

    @Override
    public Snapshot write(WriteBatch updates, WriteOptions options)
            throws DBException {
        return writeInternal((WriteBatchImpl) updates, options);
    }

    public Snapshot writeInternal(WriteBatchImpl myBatch, WriteOptions options)
            throws DBException {
        checkBackgroundException();
        final WriteBatchInternal w = new WriteBatchInternal(myBatch, options.sync(), mutex.newCondition());
        mutex.lock();
        try {
            writers.offerLast(w);
            while (!w.done && writers.peekFirst() != w) {
                w.backgroundCondition.awaitUninterruptibly();
            }
            if (w.done) {
                w.checkExceptions();
                return options.snapshot() ? snapshots.newSnapshot(versions.getLastSequence()) : null;
            }
            ValueHolder<WriteBatchInternal> lastWriterVh = new ValueHolder<>(w);
            Throwable error = null;
            try {
                multipleWriteGroup(myBatch, options, lastWriterVh);
            } catch (Exception e) {
                //all writers must be notified of this exception
                error = e;
            }

            WriteBatchInternal lastWrite = lastWriterVh.getValue();
            while (true) {
                WriteBatchInternal ready = writers.peekFirst();
                writers.pollFirst();
                if (ready != w) {
                    ready.error = error;
                    ready.done = true;
                    ready.signal();
                }
                if (ready == lastWrite) {
                    break;
                }
            }

            // Notify new head of write queue
            if (!writers.isEmpty()) {
                writers.peekFirst().signal();
            }
            checkBackgroundException();
            if (error != null) {
                Throwables.propagateIfPossible(error, DBException.class);
                throw new DBException(error);
            }
            return options.snapshot() ? snapshots.newSnapshot(versions.getLastSequence()) : null;
        } finally {
            mutex.unlock();
        }
    }

    private void multipleWriteGroup(WriteBatchImpl myBatch, WriteOptions options, ValueHolder<WriteBatchInternal> lastWriter) {
        long sequenceEnd;
        WriteBatchImpl updates;
        // May temporarily unlock and wait.
        makeRoomForWrite(myBatch == null);
        if (myBatch != null) {
            updates = buildBatchGroup(lastWriter);

            // Get sequence numbers for this change set
            long sequenceBegin = versions.getLastSequence() + 1;
            sequenceEnd = sequenceBegin + updates.size() - 1;

            // Add to log and apply to memtable.  We can release the lock
            // during this phase since "w" is currently responsible for logging
            // and protects against concurrent loggers and concurrent writes
            // into mem_.
            // log and memtable are modified by makeRoomForWrite
            mutex.unlock();
            try {
                // Log write
                Slice record = writeWriteBatch(updates, sequenceBegin);
                log.addRecord(record, options.sync());
                // Update memtable
                //this.memTable is modified by makeRoomForWrite
                updates.forEach(new InsertIntoHandler(this.memTable, sequenceBegin));
            } catch (Exception e) {
                // The state of the log file is indeterminate: the log record we
                // just added may or may not show up when the DB is re-opened.
                // So we force the DB into a mode where all future writes fail.
                mutex.lock();
                try {
                    //we need to be inside lock to record exception
                    recordBackgroundError(e);
                } finally {
                    mutex.unlock();
                }
            } finally {
                mutex.lock();
            }
            if (updates == tmpBatch) {
                tmpBatch.clear();
            }
            // Reserve this sequence in the version set
            versions.setLastSequence(sequenceEnd);
        }
    }

    /**
     * REQUIRES: Writer list must be non-empty
     * REQUIRES: First writer must have a non-NULL batch
     */
    private WriteBatchImpl buildBatchGroup(ValueHolder<WriteBatchInternal> lastWriter) {
        checkArgument(!writers.isEmpty(), "A least one writer is required");
        final WriteBatchInternal first = writers.peekFirst();
        WriteBatchImpl result = first.batch;
        checkArgument(result != null, "Batch must be non null");

        int sizeInit;
        sizeInit = first.batch.getApproximateSize();
        /*
         * Allow the group to grow up to a maximum size, but if the
         * original write is small, limit the growth so we do not slow
         * down the small write too much.
         */
        int maxSize = 1 << 20;
        if (sizeInit <= (128 << 10)) {
            maxSize = sizeInit + (128 << 10);
        }

        int size = 0;
        lastWriter.setValue(first);
        for (WriteBatchInternal w : writers) {
            if (w.sync && !lastWriter.getValue().sync) {
                // Do not include a sync write into a batch handled by a non-sync write.
                break;
            }

            if (w.batch != null) {
                size += w.batch.getApproximateSize();
                if (size > maxSize) {
                    // Do not make batch too big
                    break;
                }

                // Append to result
                if (result == first.batch) {
                    // Switch to temporary batch instead of disturbing caller's batch
                    result = tmpBatch;
                    checkState(result.size() == 0, "Temp batch should be clean");
                    result.append(first.batch);
                } else if (first.batch != w.batch) {
                    result.append(w.batch);
                }
            }
            lastWriter.setValue(w);
        }
        return result;
    }

    @Override
    public WriteBatch createWriteBatch() {
        checkBackgroundException();
        return new WriteBatchImpl();
    }

    @Override
    public DBIteratorAdapter iterator() {
        return iterator(Defaults.DEFAULT_READ_OPTIONS);
    }

    @Override
    public DBIteratorAdapter iterator(ReadOptions options) {
        mutex.lock();
        try {
            InternalIterator rawIterator = internalIterator(options);

            // filter out any entries not visible in our snapshot
            long snapshot = getSnapshot(options);
            SnapshotSeekingIterator snapshotIterator = new SnapshotSeekingIterator(rawIterator, snapshot, internalKeyComparator.getUserComparator(), new RecordBytesListener());
            return new DBIteratorAdapter(snapshotIterator);
        } finally {
            mutex.unlock();
        }
    }

    InternalIterator internalIterator(ReadOptions options) {
        mutex.lock();
        try (SafeListBuilder<InternalIterator> builder = SafeListBuilder.builder()) {
            // merge together the memTable, immutableMemTable, and tables in version set
            builder.add(memTable.iterator());
            if (immutableMemTable != null) {
                builder.add(immutableMemTable.iterator());
            }
            Version current = versions.getCurrent();
            builder.addAll(current.getLevelIterators(options));
            current.retain();
            return new DbIterator(new MergingIterator(builder.build(), internalKeyComparator), () -> {
                mutex.lock();
                try {
                    current.release();
                } finally {
                    mutex.unlock();
                }
            });
        } catch (IOException e) {
            throw new DBException(e);
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Record a sample of bytes read at the specified internal key.
     * Samples are taken approximately once every config::READ_BYTES_PERIOD
     * bytes.
     */
    void recordReadSample(InternalKey key) {
        mutex.lock();
        try {
            if (versions.getCurrent().recordReadSample(key)) {
                maybeScheduleCompaction();
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public Snapshot getSnapshot() {
        checkBackgroundException();
        mutex.lock();
        try {
            return snapshots.newSnapshot(versions.getLastSequence());
        } finally {
            mutex.unlock();
        }
    }

    private long getSnapshot(ReadOptions options) {
        long snapshot;
        if (options.snapshot() != null) {
            snapshot = snapshots.getSequenceFrom(options.snapshot());
        } else {
            snapshot = versions.getLastSequence();
        }
        return snapshot;
    }

    private void makeRoomForWrite(boolean force) {
        checkState(mutex.isHeldByCurrentThread());
        checkState(!writers.isEmpty());

        boolean allowDelay = !force;

        while (true) {
            checkBackgroundException();
            if (allowDelay && versions.numberOfFilesInLevel(0) > L0_SLOWDOWN_WRITES_TRIGGER) {
                // We are getting close to hitting a hard limit on the number of
                // L0 files.  Rather than delaying a single write by several
                // seconds when we hit the hard limit, start delaying each
                // individual write by 1ms to reduce latency variance.  Also,
                // this delay hands over some CPU to the compaction thread in
                // case it is sharing the same core as the writer.
                try {
                    mutex.unlock();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new DBException(e);
                } finally {
                    mutex.lock();
                }

                // Do not delay a single write more than once
                allowDelay = false;
            } else if (!force && memTable.approximateMemoryUsage() <= options.writeBufferSize()) {
                // There is room in current memtable
                break;
            } else if (immutableMemTable != null) {
                // We have filled up the current memtable, but the previous
                // one is still being compacted, so we wait.
                if (Nukkit.DEBUG > 1) options.logger().log("Current memtable full; waiting...");
                backgroundCondition.awaitUninterruptibly();
            } else if (versions.numberOfFilesInLevel(0) >= L0_STOP_WRITES_TRIGGER) {
                // There are too many level-0 files.
                if (Nukkit.DEBUG > 1) options.logger().log("Too many L0 files; waiting...");
                backgroundCondition.awaitUninterruptibly();
            } else {
                // Attempt to switch to a new memtable and trigger compaction of old
                checkState(versions.getPrevLogNumber() == 0);

                // close the existing log
                try {
                    log.close();
                } catch (IOException e) {
                    throw new DBException("Unable to close log file " + log, e);
                }

                // open a new log
                long logNumber = versions.getNextFileNumber();
                try {
                    this.log = Logs.createLogWriter(databaseDir.child(Filename.logFileName(logNumber)), logNumber, env);
                } catch (IOException e) {
                    throw new DBException("Unable to open new log file " +
                            databaseDir.child(Filename.logFileName(logNumber)).getPath(), e);
                }

                // create a new mem table
                immutableMemTable = memTable;
                memTable = new MemTable(internalKeyComparator);

                // Do not force another compaction there is space available
                force = false;

                maybeScheduleCompaction();
            }
        }
    }

    private void compactMemTable()
            throws IOException {
        checkState(mutex.isHeldByCurrentThread());
        checkState(immutableMemTable != null);

        try {
            // Save the contents of the memtable as a new Table
            VersionEdit edit = new VersionEdit();
            Version base = versions.getCurrent();
            base.retain();
            writeLevel0Table(immutableMemTable, edit, base);
            base.release();

            if (shuttingDown.get()) {
                throw new DatabaseShutdownException("Database shutdown during memtable compaction");
            }

            // Replace immutable memtable with the generated Table
            edit.setPreviousLogNumber(0);
            edit.setLogNumber(log.getFileNumber());  // Earlier logs no longer needed
            versions.logAndApply(edit, mutex);

            immutableMemTable = null;
            deleteObsoleteFiles();
        } finally {
            backgroundCondition.signalAll();
        }
    }

    private void writeLevel0Table(MemTable mem, VersionEdit edit, Version base)
            throws IOException {
        final long startMicros = env.nowMicros();
        checkState(mutex.isHeldByCurrentThread());

        // skip empty mem table
        if (mem.isEmpty()) {
            return;
        }

        // write the memtable to a new sstable
        long fileNumber = versions.getNextFileNumber();
        pendingOutputs.add(fileNumber);
        if (Nukkit.DEBUG > 1) options.logger().log("Level-0 table #%s: started",
                fileNumber);

        mutex.unlock();
        FileMetaData meta;
        try {
            meta = buildTable(mem, fileNumber);
        } finally {
            mutex.lock();
        }
        if (Nukkit.DEBUG > 1) options.logger().log("Level-0 table #%s: %s bytes",
                meta.getNumber(),
                meta.getFileSize());
        pendingOutputs.remove(fileNumber);

        // Note that if file size is zero, the file has been deleted and
        // should not be added to the manifest.
        int level = 0;
        if (meta.getFileSize() > 0) {
            Slice minUserKey = meta.getSmallest().getUserKey();
            Slice maxUserKey = meta.getLargest().getUserKey();
            if (base != null) {
                level = base.pickLevelForMemTableOutput(minUserKey, maxUserKey);
            }
            edit.addFile(level, meta);
        }
        this.stats[level].add(env.nowMicros() - startMicros, 0, meta.getFileSize());
    }

    private FileMetaData buildTable(MemTable data, long fileNumber)
            throws IOException {
        File file = databaseDir.child(Filename.tableFileName(fileNumber));
        try {
            InternalKey smallest = null;
            InternalKey largest = null;
            try (WritableFile writableFile = env.newWritableFile(file)) {
                TableBuilder tableBuilder = new TableBuilder(options, writableFile, new InternalUserComparator(internalKeyComparator));

                try (InternalIterator it = data.iterator()) {
                    for (boolean valid = it.seekToFirst(); valid; valid = it.next()) {
                        // update keys
                        InternalKey key = it.key();
                        if (smallest == null) {
                            smallest = key;
                        }
                        largest = key;

                        tableBuilder.add(key.encode(), it.value());
                    }
                }

                tableBuilder.finish();
                writableFile.force();
            }

            if (smallest == null) {
                //empty iterator
                file.delete();
                return new FileMetaData(fileNumber, 0, null, null);
            }
            FileMetaData fileMetaData = new FileMetaData(fileNumber, file.length(), smallest, largest);

            // verify table can be opened
            tableCache.newIterator(fileMetaData, Defaults.DEFAULT_READ_OPTIONS).close();

            return fileMetaData;
        } catch (IOException e) {
            file.delete();
            throw e;
        }
    }

    private void doCompactionWork(CompactionState compactionState)
            throws IOException {
        final long startMicros = env.nowMicros();
        long immMicros = 0;  // Micros spent doing imm_ compactions
        if (Nukkit.DEBUG > 1) options.logger().log("Compacting %s@%s + %s@%s files",
                compactionState.compaction.input(0).size(),
                compactionState.compaction.getLevel(),
                compactionState.compaction.input(1).size(),
                compactionState.compaction.getLevel() + 1);

        checkState(mutex.isHeldByCurrentThread());
        checkArgument(versions.numberOfBytesInLevel(compactionState.getCompaction().getLevel()) > 0);
        checkArgument(compactionState.builder == null);
        checkArgument(compactionState.outfile == null);

        compactionState.smallestSnapshot = snapshots.isEmpty() ? versions.getLastSequence() : snapshots.getOldest();

        // Release mutex while we're actually doing the compaction work
        final MergingIterator mergingIterator = versions.makeInputIterator(compactionState.compaction);
        mutex.unlock();
        try (MergingIterator iterator = mergingIterator) {
            Slice currentUserKey = null;
            boolean hasCurrentUserKey = false;

            long lastSequenceForKey = MAX_SEQUENCE_NUMBER;
            for (boolean valid = iterator.seekToFirst(); valid && !shuttingDown.get(); valid = iterator.next()) {
                // always give priority to compacting the current mem table
                if (immutableMemTable != null) {
                    long immStart = env.nowMicros();
                    mutex.lock();
                    try {
                        compactMemTable();
                    } finally {
                        mutex.unlock();
                    }
                    immMicros += (env.nowMicros() - immStart);
                }
                InternalKey key = iterator.key();
                if (compactionState.compaction.shouldStopBefore(key) && compactionState.builder != null) {
                    finishCompactionOutputFile(compactionState);
                }

                // Handle key/value, add to state, etc.
                boolean drop = false;
                // todo if key doesn't parse (it is corrupted),
                if (false /*!ParseInternalKey(key, &ikey)*/) {
                    // do not hide error keys
                    currentUserKey = null;
                    hasCurrentUserKey = false;
                    lastSequenceForKey = MAX_SEQUENCE_NUMBER;
                } else {
                    if (!hasCurrentUserKey || internalKeyComparator.getUserComparator().compare(key.getUserKey(), currentUserKey) != 0) {
                        // First occurrence of this user key
                        currentUserKey = key.getUserKey();
                        hasCurrentUserKey = true;
                        lastSequenceForKey = MAX_SEQUENCE_NUMBER;
                    }

                    if (lastSequenceForKey <= compactionState.smallestSnapshot) {
                        // Hidden by an newer entry for same user key
                        drop = true; // (A)
                    } else if (key.getValueType() == DELETION &&
                            key.getSequenceNumber() <= compactionState.smallestSnapshot &&
                            compactionState.compaction.isBaseLevelForKey(key.getUserKey())) {
                        // For this user key:
                        // (1) there is no data in higher levels
                        // (2) data in lower levels will have larger sequence numbers
                        // (3) data in layers that are being compacted here and have
                        //     smaller sequence numbers will be dropped in the next
                        //     few iterations of this loop (by rule (A) above).
                        // Therefore this deletion marker is obsolete and can be dropped.
                        drop = true;
                    }

                    lastSequenceForKey = key.getSequenceNumber();
                }

                if (!drop) {
                    // Open output file if necessary
                    if (compactionState.builder == null) {
                        openCompactionOutputFile(compactionState);
                    }
                    if (compactionState.builder.getEntryCount() == 0) {
                        compactionState.currentSmallest = key;
                    }
                    compactionState.currentLargest = key;
                    compactionState.builder.add(key.encode(), iterator.value());

                    // Close output file if it is big enough
                    if (compactionState.builder.getFileSize() >=
                            compactionState.compaction.getMaxOutputFileSize()) {
                        finishCompactionOutputFile(compactionState);
                    }
                }
            }

            if (shuttingDown.get()) {
                throw new DatabaseShutdownException("DB shutdown during compaction");
            }
            if (compactionState.builder != null) {
                finishCompactionOutputFile(compactionState);
            }
        } finally {
            long micros = env.nowMicros() - startMicros - immMicros;
            long bytesRead = 0;
            for (int which = 0; which < 2; which++) {
                for (int i = 0; i < compactionState.compaction.input(which).size(); i++) {
                    bytesRead += compactionState.compaction.input(which, i).getFileSize();
                }
            }
            long bytesWritten = 0;
            for (int i = 0; i < compactionState.outputs.size(); i++) {
                bytesWritten += compactionState.outputs.get(i).getFileSize();
            }
            mutex.lock();
            this.stats[compactionState.compaction.getLevel() + 1].add(micros, bytesRead, bytesWritten);
        }
        installCompactionResults(compactionState);
        if (Nukkit.DEBUG > 1) options.logger().log(
                "compacted to: %s", versions.levelSummary());
    }

    private void openCompactionOutputFile(CompactionState compactionState)
            throws IOException {
        Objects.requireNonNull(compactionState);
        checkArgument(compactionState.builder == null, "compactionState builder is not null");

        long fileNumber;
        mutex.lock();
        try {
            fileNumber = versions.getNextFileNumber();
            pendingOutputs.add(fileNumber);
            compactionState.currentFileNumber = fileNumber;
            compactionState.currentFileSize = 0;
            compactionState.currentSmallest = null;
            compactionState.currentLargest = null;
        } finally {
            mutex.unlock();
        }
        File file = databaseDir.child(Filename.tableFileName(fileNumber));
        compactionState.outfile = env.newWritableFile(file);
        compactionState.builder = new TableBuilder(options, compactionState.outfile, new InternalUserComparator(internalKeyComparator));
    }

    private void finishCompactionOutputFile(CompactionState compactionState)
            throws IOException {
        Objects.requireNonNull(compactionState);
        checkArgument(compactionState.outfile != null);
        checkArgument(compactionState.builder != null);

        long outputNumber = compactionState.currentFileNumber;
        checkArgument(outputNumber != 0);

        long currentEntries = compactionState.builder.getEntryCount();
        long currentBytes;
        try {
            compactionState.builder.finish();
            currentBytes = compactionState.builder.getFileSize();
        } finally {
            compactionState.builder = null;
        }
        compactionState.currentFileSize = currentBytes;
        compactionState.totalBytes += currentBytes;

        FileMetaData currentFileMetaData = new FileMetaData(compactionState.currentFileNumber,
                compactionState.currentFileSize,
                compactionState.currentSmallest,
                compactionState.currentLargest);
        compactionState.outputs.add(currentFileMetaData);

        compactionState.outfile.force();
        compactionState.outfile.close();
        compactionState.outfile = null;

        if (currentEntries > 0) {
            // Verify that the table is usable
            tableCache.newIterator(outputNumber, Defaults.DEFAULT_READ_OPTIONS).close();
            if (Nukkit.DEBUG > 1) options.logger().log(
                    "Generated table #%s@%s: %s keys, %s bytes",
                    outputNumber,
                    compactionState.compaction.getLevel(),
                    currentEntries,
                    currentBytes);
        }
    }

    private void installCompactionResults(CompactionState compact)
            throws IOException {
        checkState(mutex.isHeldByCurrentThread());
        if (Nukkit.DEBUG > 1) options.logger().log("Compacted %s@%s + %s@%s files => %s bytes",
                compact.compaction.input(0).size(),
                compact.compaction.getLevel(),
                compact.compaction.input(1).size(),
                compact.compaction.getLevel() + 1,
                compact.totalBytes);

        // Add compaction outputs
        compact.compaction.addInputDeletions(compact.compaction.getEdit());
        int level = compact.compaction.getLevel();
        for (FileMetaData output : compact.outputs) {
            compact.compaction.getEdit().addFile(level + 1, output);
            pendingOutputs.remove(output.getNumber());
        }

        versions.logAndApply(compact.compaction.getEdit(), mutex);
    }

    int numberOfFilesInLevel(int level) {
        mutex.lock();
        Version v;
        try {
            v = versions.getCurrent();
        } finally {
            mutex.unlock();
        }
        return v.numberOfFilesInLevel(level);
    }

    @Override
    public long[] getApproximateSizes(Range... ranges) {
        Objects.requireNonNull(ranges);
        long[] sizes = new long[ranges.length];
        for (int i = 0; i < ranges.length; i++) {
            Range range = ranges[i];
            sizes[i] = getApproximateSizes(range);
        }
        return sizes;
    }

    public long getApproximateSizes(Range range) {
        mutex.lock();
        Version v;
        try {
            v = versions.getCurrent();
            v.retain();
            try {
                InternalKey startKey = new InternalKey(Slices.wrappedBuffer(range.start()), MAX_SEQUENCE_NUMBER, VALUE);
                InternalKey limitKey = new InternalKey(Slices.wrappedBuffer(range.limit()), MAX_SEQUENCE_NUMBER, VALUE);
                long startOffset = v.getApproximateOffsetOf(startKey);
                long limitOffset = v.getApproximateOffsetOf(limitKey);
                return (limitOffset >= startOffset ? limitOffset - startOffset : 0);
            } finally {
                v.release();
            }
        } finally {
            mutex.unlock();
        }
    }

    public long getMaxNextLevelOverlappingBytes() {
        mutex.lock();
        try {
            return versions.getMaxNextLevelOverlappingBytes();
        } finally {
            mutex.unlock();
        }
    }

    private static class CompactionState {
        private final Compaction compaction;

        private final List<FileMetaData> outputs = new ArrayList<>();

        private long smallestSnapshot;

        // State kept for output being generated
        private WritableFile outfile;
        private TableBuilder builder;

        // Current file being generated
        private long currentFileNumber;
        private long currentFileSize;
        private InternalKey currentSmallest;
        private InternalKey currentLargest;

        private long totalBytes;

        private CompactionState(Compaction compaction) {
            this.compaction = compaction;
        }

        public Compaction getCompaction() {
            return compaction;
        }
    }

    private static class ManualCompaction {
        private final int level;
        private InternalKey begin;
        private final InternalKey end;
        private boolean done;

        private ManualCompaction(int level, InternalKey begin, InternalKey end) {
            this.level = level;
            this.begin = begin;
            this.end = end;
        }
    }

    // Per level compaction stats.  stats[level] stores the stats for
    // compactions that produced data for the specified "level".
    private static class CompactionStats {
        long micros;
        long bytesRead;
        long bytesWritten;

        CompactionStats() {
            this.micros = 0;
            this.bytesRead = 0;
            this.bytesWritten = 0;
        }

        public void add(long micros, long bytesRead, long bytesWritten) {
            this.micros += micros;
            this.bytesRead += bytesRead;
            this.bytesWritten += bytesWritten;
        }
    }

    private WriteBatchImpl readWriteBatch(SliceInput record, int updateSize)
            throws IOException {
        WriteBatchImpl writeBatch = new WriteBatchImpl();
        int entries = 0;
        while (record.isReadable()) {
            entries++;
            ValueType valueType = ValueType.getValueTypeByPersistentId(record.readByte());
            if (valueType == VALUE) {
                Slice key = readLengthPrefixedBytes(record);
                Slice value = readLengthPrefixedBytes(record);
                writeBatch.put(key, value);
            } else if (valueType == DELETION) {
                Slice key = readLengthPrefixedBytes(record);
                writeBatch.delete(key);
            } else {
                throw new IllegalStateException("Unexpected value type " + valueType);
            }
        }

        if (entries != updateSize) {
            throw new IOException(String.format("Expected %d entries in log record but found %s entries", updateSize, entries));
        }

        return writeBatch;
    }

    static Slice writeWriteBatch(WriteBatchImpl updates, long sequenceBegin) {
        Slice record = Slices.allocate(SIZE_OF_LONG + SIZE_OF_INT + updates.getApproximateSize());
        final SliceOutput sliceOutput = record.output();
        sliceOutput.writeLong(sequenceBegin);
        sliceOutput.writeInt(updates.size());
        updates.forEach(new Handler() {
            @Override
            public void put(Slice key, Slice value) {
                sliceOutput.writeByte(VALUE.getPersistentId());
                writeLengthPrefixedBytes(sliceOutput, key);
                writeLengthPrefixedBytes(sliceOutput, value);
            }

            @Override
            public void delete(Slice key) {
                sliceOutput.writeByte(DELETION.getPersistentId());
                writeLengthPrefixedBytes(sliceOutput, key);
            }
        });
        return record.slice(0, sliceOutput.size());
    }

    public static class DatabaseShutdownException
            extends DBException {
        public DatabaseShutdownException() {
        }

        public DatabaseShutdownException(String message) {
            super(message);
        }
    }

    public static class BackgroundProcessingException
            extends DBException {
        public BackgroundProcessingException(Throwable cause) {
            super(cause);
        }
    }

    private final Object suspensionMutex = new Object();
    private int suspensionCounter;

    @Override
    public void suspendCompactions()
            throws InterruptedException {
        compactionExecutor.execute(() -> {
            try {
                synchronized (suspensionMutex) {
                    suspensionCounter++;
                    suspensionMutex.notifyAll();
                    while (suspensionCounter > 0 && !compactionExecutor.isShutdown()) {
                        suspensionMutex.wait(500);
                    }
                }
            } catch (InterruptedException e) {
            }
        });
        synchronized (suspensionMutex) {
            while (suspensionCounter < 1) {
                suspensionMutex.wait();
            }
        }
    }

    @Override
    public void resumeCompactions() {
        synchronized (suspensionMutex) {
            suspensionCounter--;
            suspensionMutex.notifyAll();
        }
    }

    @Override
    public void compactRange(byte[] begin, byte[] end)
            throws DBException {
        final Slice smallestUserKey = begin == null ? null : new Slice(begin, 0, begin.length);
        final Slice largestUserKey = end == null ? null : new Slice(end, 0, end.length);
        int maxLevelWithFiles = 1;
        mutex.lock();
        try {
            Version base = versions.getCurrent();
            for (int level = 1; level < DbConstants.NUM_LEVELS; level++) {
                if (base.overlapInLevel(level, smallestUserKey, largestUserKey)) {
                    maxLevelWithFiles = level;
                }
            }
        } finally {
            mutex.unlock();
        }
        testCompactMemTable(); // TODO: Skip if memtable does not overlap
        for (int level = 0; level < maxLevelWithFiles; level++) {
            testCompactRange(level, smallestUserKey, largestUserKey);
        }
    }

    void testCompactRange(int level, Slice begin, Slice end) throws DBException {
        checkArgument(level >= 0);
        checkArgument(level + 1 < DbConstants.NUM_LEVELS);

        final InternalKey beginStorage = begin == null ? null : new InternalKey(begin, SequenceNumber.MAX_SEQUENCE_NUMBER, VALUE);
        final InternalKey endStorage = end == null ? null : new InternalKey(end, 0, DELETION);
        ManualCompaction manual = new ManualCompaction(level, beginStorage, endStorage);
        mutex.lock();
        try {
            while (!manual.done && !shuttingDown.get() && backgroundException == null) {
                if (manualCompaction == null) {  // Idle
                    manualCompaction = manual;
                    maybeScheduleCompaction();
                } else {  // Running either my compaction or another compaction.
                    backgroundCondition.awaitUninterruptibly();
                }
            }
            if (manualCompaction == manual) {
                // Cancel my manual compaction since we aborted early for some reason.
                manualCompaction = null;
            }
        } finally {
            mutex.unlock();
        }
    }

    void testCompactMemTable() throws DBException {
        // NULL batch means just wait for earlier writes to be done
        writeInternal(null, Defaults.DEFAULT_WRITE_OPTIONS);
        // Wait until the compaction completes
        mutex.lock();

        try {
            while (immutableMemTable != null && backgroundException == null) {
                backgroundCondition.awaitUninterruptibly();
            }
            if (immutableMemTable != null) {
                if (backgroundException != null) {
                    throw new DBException(backgroundException);
                }
            }
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Wait for all background activity to finish; only usable in controlled environment.
     */
    void waitForBackgroundCompactationToFinish() {
        mutex.lock();
        try {
            while (backgroundCompaction != null && !shuttingDown.get() && backgroundException == null) {
                backgroundCondition.awaitUninterruptibly();
            }
        } finally {
            mutex.unlock();
        }
    }

    public static boolean destroyDB(File dbname, Env env) throws IOException {
        // Ignore error in case directory does not exist
        if (!dbname.exists()) {
            return true;
        }
        List<File> filenames = dbname.listFiles();

        boolean res = true;
        File lockFile = dbname.child(Filename.lockFileName());
        DbLock lock = env.tryLock(lockFile);
        try {
            for (File filename : filenames) {
                FileInfo fileInfo = Filename.parseFileName(filename);
                if (fileInfo != null && fileInfo.getFileType() != FileType.DB_LOCK) {  // Lock file will be deleted at end
                    res &= filename.delete();
                }
            }
        } finally {
            try {
                lock.release(); // Ignore error since state is already gone
            } catch (Exception ignore) {
            }
        }
        lockFile.delete();
        dbname.delete();  // Ignore error in case dir contains other files
        return res;
    }

    public class RecordBytesListener
            implements SnapshotSeekingIterator.IRecordBytesListener {
        private final SplittableRandom r;
        private int bytesReadUntilSampling;

        RecordBytesListener() {
            this.r = new SplittableRandom();
            this.bytesReadUntilSampling = getRandomCompactionPeriod(r);
        }

        @Override
        public void record(InternalKey internalKey, int bytes) {
            bytesReadUntilSampling -= bytes;
            while (bytesReadUntilSampling < 0) {
                bytesReadUntilSampling += getRandomCompactionPeriod(r);
                DbImpl.this.recordReadSample(internalKey);
            }
        }

        /**
         * Picks the number of bytes that can be read until a compaction is scheduled.
         *
         * @param r
         */
        private int getRandomCompactionPeriod(SplittableRandom r) {
            return r.nextInt(2 * DbConstants.READ_BYTES_PERIOD);
        }
    }

    private class WriteBatchInternal {
        private final WriteBatchImpl batch;
        private final boolean sync;
        private final Condition backgroundCondition;
        boolean done = false;
        public Throwable error;

        WriteBatchInternal(WriteBatchImpl batch, boolean sync, Condition backgroundCondition) {
            this.batch = batch;
            this.sync = sync;
            this.backgroundCondition = backgroundCondition;
        }

        void signal() {
            backgroundCondition.signal();
        }

        void checkExceptions() {
            checkBackgroundException();
            if (error instanceof Error) {
                throw (Error) error;
            }
            if (error != null) {
                throw new DBException(error);
            }
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" + databaseDir + "}";
    }
}
