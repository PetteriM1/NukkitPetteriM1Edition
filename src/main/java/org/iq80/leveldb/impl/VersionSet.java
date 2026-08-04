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
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.*;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.ReadOptions;
import org.iq80.leveldb.env.Env;
import org.iq80.leveldb.env.File;
import org.iq80.leveldb.env.SequentialFile;
import org.iq80.leveldb.iterator.InternalIterator;
import org.iq80.leveldb.iterator.MergingIterator;
import org.iq80.leveldb.table.UserComparator;
import org.iq80.leveldb.util.SafeListBuilder;
import org.iq80.leveldb.util.Slice;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.iq80.leveldb.impl.DbConstants.NUM_LEVELS;
import static org.iq80.leveldb.impl.LogMonitors.throwExceptionMonitor;

public class VersionSet {

    private final AtomicLong nextFileNumber = new AtomicLong(2);
    private long manifestFileNumber = 1;
    private Version current;
    private long lastSequence;
    private long logNumber;
    private long prevLogNumber;

    private final Map<Version, Object> activeVersions = new MapMaker().weakKeys().makeMap();
    private final Options options;
    private final File databaseDir;
    private final TableCache tableCache;
    private final InternalKeyComparator internalKeyComparator;
    private final Env env;

    private LogWriter descriptorLog;
    private final Map<Integer, InternalKey> compactPointers = new TreeMap<>();

    public VersionSet(Options options, File databaseDir, TableCache tableCache, InternalKeyComparator internalKeyComparator, Env env)
            throws IOException {
        this.options = options;
        this.databaseDir = databaseDir;
        this.tableCache = tableCache;
        this.internalKeyComparator = internalKeyComparator;
        this.env = env;
        appendVersion(new Version(this));

        initializeIfNeeded();
    }

    private void initializeIfNeeded()
            throws IOException {
        File currentFile = databaseDir.child(Filename.currentFileName());

        if (!currentFile.exists()) {
            VersionEdit edit = new VersionEdit();
            edit.setComparatorName(internalKeyComparator.name());
            edit.setLogNumber(prevLogNumber);
            edit.setNextFileNumber(nextFileNumber.get());
            edit.setLastSequenceNumber(lastSequence);

            LogWriter log = Logs.createLogWriter(databaseDir.child(Filename.descriptorFileName(manifestFileNumber)), manifestFileNumber, env);
            try {
                writeSnapshot(log);
                log.addRecord(edit.encode(), false);
            } finally {
                log.close();
            }

            Filename.setCurrentFile(databaseDir, log.getFileNumber(), env);
        }
    }

    public void release()
            throws IOException {
        if (descriptorLog != null) {
            descriptorLog.close();
            descriptorLog = null;
        }

        Version t = current;
        if (t != null) {
            current = null;
            t.release();
        }

        if (Nukkit.DEBUG > 1) {
            Set<Version> versions = activeVersions.keySet();
            if (!versions.isEmpty()) {
                options.logger().log("DB closed with %s open snapshots. This could mean your application has a resource leak.", versions.size());
            }
        }
    }

    private void appendVersion(Version version) {
        Objects.requireNonNull(version);
        checkArgument(version != current, "version is the current version");
        Version previous = current;
        current = version; //version already retained, create with retained = 1
        activeVersions.put(version, new Object());
        if (previous != null) {
            previous.release();
        }
    }

    public void removeVersion(Version version) {
        Objects.requireNonNull(version);
        checkArgument(version != current, "version is the current version");
        boolean removed = activeVersions.remove(version) != null;
        assert removed : "Expected the version to still be in the active set";
    }

    public InternalKeyComparator getInternalKeyComparator() {
        return internalKeyComparator;
    }

    public TableCache getTableCache() {
        return tableCache;
    }

    public Version getCurrent() {
        return current;
    }

    public long getManifestFileNumber() {
        return manifestFileNumber;
    }

    public long getNextFileNumber() {
        return nextFileNumber.getAndIncrement();
    }

    public long getLogNumber() {
        return logNumber;
    }

    public long getPrevLogNumber() {
        return prevLogNumber;
    }

    public MergingIterator makeInputIterator(Compaction c) throws IOException {
        ReadOptions rOptions = new ReadOptions();
        rOptions.verifyChecksums(this.options.paranoidChecks());
        rOptions.fillCache(false);

        // Level-0 files have to be merged together.  For other levels,
        // we will make a concatenating iterator per level.
        // TODO(opt): use concatenating iterator for level-0 if there is no overlap
        try (SafeListBuilder<InternalIterator> list = SafeListBuilder.builder()) {
            for (int which = 0; which < 2; which++) {
                List<FileMetaData> files = c.input(which);
                if (!files.isEmpty()) {
                    if (c.getLevel() + which == 0) {
                        try (SafeListBuilder<InternalIterator> builder = SafeListBuilder.builder()) {
                            for (FileMetaData file : files) {
                                builder.add(tableCache.newIterator(file, rOptions));
                            }
                            list.add(new MergingIterator(builder.build(), internalKeyComparator));
                        }
                    } else {
                        // Create concatenating iterator for the files from this level
                        list.add(Level.createLevelConcatIterator(tableCache, files, internalKeyComparator, rOptions));
                    }
                }
            }
            return new MergingIterator(list.build(), internalKeyComparator);
        }
    }

    public boolean overlapInLevel(int level, Slice smallestUserKey, Slice largestUserKey) {
        return current.overlapInLevel(level, smallestUserKey, largestUserKey);
    }

    public int numberOfFilesInLevel(int level) {
        return current.numberOfFilesInLevel(level);
    }

    public long numberOfBytesInLevel(int level) {
        return current.numberOfFilesInLevel(level);
    }

    public long getLastSequence() {
        return lastSequence;
    }

    public void setLastSequence(long newLastSequence) {
        checkArgument(newLastSequence >= lastSequence, "Expected newLastSequence to be greater than or equal to current lastSequence");
        this.lastSequence = newLastSequence;
    }

    public void logAndApply(VersionEdit edit, ReentrantLock mutex)
            throws IOException {
        if (edit.getLogNumber() != null) {
            checkArgument(edit.getLogNumber() >= logNumber);
            checkArgument(edit.getLogNumber() < nextFileNumber.get());
        } else {
            edit.setLogNumber(logNumber);
        }

        if (edit.getPreviousLogNumber() == null) {
            edit.setPreviousLogNumber(prevLogNumber);
        }

        edit.setNextFileNumber(nextFileNumber.get());
        edit.setLastSequenceNumber(lastSequence);

        Version version = new Version(this);
        try (Builder builder = new Builder(this, current)) {
            builder.apply(edit);
            builder.saveTo(version);
        }

        finalizeVersion(version);

        boolean createdNewManifest = false;
        final long mFileNumber = manifestFileNumber;
        try {
            // Initialize new descriptor log file if necessary by creating
            // a temporary file that contains a snapshot of the current version.
            if (descriptorLog == null) {
                // No reason to unlock mutex here since we only hit this path in the
                // first call to logAndApply (when opening the database).
                edit.setNextFileNumber(nextFileNumber.get());
                descriptorLog = Logs.createLogWriter(databaseDir.child(Filename.descriptorFileName(mFileNumber)), mFileNumber, env);
                writeSnapshot(descriptorLog);
                createdNewManifest = true;
            }
            // Unlock during expensive MANIFEST log write
            mutex.unlock();
            try {
                // Write new record to MANIFEST log
                Slice record = edit.encode();
                descriptorLog.addRecord(record, true);

                // If we just created a new descriptor file, install it by writing a
                // new CURRENT file that points to it.
                if (createdNewManifest) {
                    Filename.setCurrentFile(databaseDir, mFileNumber, env);
                }
            } finally {
                mutex.lock();
            }
        } catch (IOException e) {
            if (Nukkit.DEBUG > 1) options.logger().log("MANIFEST write: %s", e);
            // New manifest file was not installed, so clean up state and delete the file
            if (createdNewManifest) {
                descriptorLog.close();
                // todo add delete method to LogWriter
                databaseDir.child(Filename.logFileName(mFileNumber)).delete();
                descriptorLog = null;
            }
            throw e;
        }

        // Install the new version
        appendVersion(version);
        logNumber = edit.getLogNumber();
        prevLogNumber = edit.getPreviousLogNumber();
    }

    private void writeSnapshot(LogWriter log)
            throws IOException {
        // Save metadata
        VersionEdit edit = new VersionEdit();
        edit.setComparatorName(internalKeyComparator.name());

        // Save compaction pointers
        edit.setCompactPointers(compactPointers);

        // Save files
        edit.addFiles(current.getFiles());

        Slice record = edit.encode();
        log.addRecord(record, false);
    }

    /**
     * @return {@code true} if manifest should be saved, {@code false} otherwise
     */
    public boolean recover()
            throws IOException {
        // Read "CURRENT" file, which contains a pointer to the current manifest file
        final String descriptorName = Filename.getCurrentFile(databaseDir, env);

        // open file channel
        final File descriptorFile = databaseDir.child(descriptorName);
        try (SequentialFile in = env.newSequentialFile(descriptorFile)) {
            // read log edit log
            Long nextFileNumber = null;
            Long lastSequence = null;
            Long logNumber = null;
            Long prevLogNumber = null;
            Builder builder = new Builder(this, current);

            LogReader reader = new LogReader(in, throwExceptionMonitor(), true, 0);
            for (Slice record = reader.readRecord(); record != null; record = reader.readRecord()) {
                // read version edit
                VersionEdit edit = new VersionEdit(record);

                // verify comparator
                String editComparator = edit.getComparatorName();
                String userComparator = internalKeyComparator.name();
                checkArgument(editComparator == null || editComparator.equals(userComparator),
                        "Expected user comparator %s to match existing database comparator ", userComparator, editComparator);

                // apply edit
                builder.apply(edit);

                // save edit values for verification below
                logNumber = coalesce(edit.getLogNumber(), logNumber);
                prevLogNumber = coalesce(edit.getPreviousLogNumber(), prevLogNumber);
                nextFileNumber = coalesce(edit.getNextFileNumber(), nextFileNumber);
                lastSequence = coalesce(edit.getLastSequenceNumber(), lastSequence);
            }

            List<String> problems = new ArrayList<>();
            if (nextFileNumber == null) {
                problems.add("Descriptor does not contain a meta-nextfile entry");
            }
            if (logNumber == null) {
                problems.add("Descriptor does not contain a meta-lognumber entry");
            }
            if (lastSequence == null) {
                problems.add("Descriptor does not contain a last-sequence-number entry");
            }
            if (!problems.isEmpty()) {
                throw new DBException("Corruption: \n\t" + Joiner.on("\n\t").join(problems));
            }

            if (prevLogNumber == null) {
                prevLogNumber = 0L;
            }
            markFileNumberUsed(prevLogNumber);
            markFileNumberUsed(logNumber);

            Version newVersion = new Version(this);
            builder.saveTo(newVersion);
            builder.close();

            // Install recovered version
            finalizeVersion(newVersion);

            appendVersion(newVersion);
            manifestFileNumber = nextFileNumber;
            this.nextFileNumber.set(nextFileNumber + 1);
            this.lastSequence = lastSequence;
            this.logNumber = logNumber;
            this.prevLogNumber = prevLogNumber;
            // No need to save manifest
            return !reuseManifest(descriptorFile);
        }
    }

    void markFileNumberUsed(long number) {
        long current;
        while ((current = nextFileNumber.get()) <= number) {
            if (nextFileNumber.compareAndSet(current, number + 1)) {
                break;
            }
        }
    }

    private boolean reuseManifest(File currentFile) {
        if (!options.reuseLogs()) {
            return false;
        }
        Filename.FileInfo fileInfo = Filename.parseFileName(currentFile);
        if (fileInfo == null ||
                fileInfo.getFileType() != Filename.FileType.DESCRIPTOR ||
                // Make new compacted MANIFEST if old one is too big
                currentFile.length() >= targetFileSize()) {
            return false;
        }
        Preconditions.checkState(descriptorLog == null, "descriptor log should be null");
        try {
            descriptorLog = LogWriter.createWriter(fileInfo.getFileNumber(), env.newAppendableFile(currentFile));
        } catch (Exception e) {
            assert descriptorLog == null;
            if (Nukkit.DEBUG > 1) options.logger().log("Reuse MANIFEST: %s", e);
            return false;
        }

        if (Nukkit.DEBUG > 1) options.logger().log("Reusing MANIFEST %s", currentFile);
        this.manifestFileNumber = fileInfo.getFileNumber();
        return true;
    }

    private void finalizeVersion(Version version) {
        // Precomputed best level for next compaction
        int bestLevel = -1;
        double bestScore = -1;

        for (int level = 0; level < version.numberOfLevels() - 1; level++) {
            double score;
            if (level == 0) {
                // We treat level-0 specially by bounding the number of files
                // instead of number of bytes for two reasons:
                //
                // (1) With larger write-buffer sizes, it is nice not to do too
                // many level-0 compactions.
                //
                // (2) The files in level-0 are merged on every read and
                // therefore we wish to avoid too many files when the individual
                // file size is small (perhaps because of a small write-buffer
                // setting, or very high compression ratios, or lots of
                // overwrites/deletions).
                score = 1.0 * version.numberOfFilesInLevel(level) / DbConstants.L0_COMPACTION_TRIGGER;
            } else {
                // Compute the ratio of current size to size limit.
                long levelBytes = 0;
                for (FileMetaData fileMetaData : version.getFiles(level)) {
                    levelBytes += fileMetaData.getFileSize();
                }
                score = 1.0 * levelBytes / maxBytesForLevel(level);
            }

            if (score > bestScore) {
                bestLevel = level;
                bestScore = score;
            }
        }

        version.setCompactionLevel(bestLevel);
        version.setCompactionScore(bestScore);
    }

    private static Long coalesce(Long... values) {
        for (Long value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public List<FileMetaData> getLiveFiles() {
        ImmutableList.Builder<FileMetaData> builder = ImmutableList.builder();
        for (Version activeVersion : activeVersions.keySet()) {
            builder.addAll(activeVersion.getFiles().values());
        }
        return builder.build();
    }

    public long targetFileSize() {
        return options.maxFileSize();
    }

    /**
     * Maximum bytes of overlaps in grandparent (i.e., level+2) before we
     * stop building a single file in a level->level+1 compaction.
     */
    public long maxGrandParentOverlapBytes() {
        return 10L * targetFileSize();
    }

    /**
     * Maximum number of bytes in all compacted files.  We avoid expanding
     * the lower level file set of a compaction if it would make the
     * total compaction cover more than this many bytes.
     */
    public long expandedCompactionByteSizeLimit() {
        return 25L * targetFileSize();
    }

    private double maxBytesForLevel(int level) {
        // Note: the result for level zero is not really used since we set
        // the level-0 compaction threshold based on number of files.

        // Result for both level-0 and level-1
        double result = 1.048576E7; // 10 * 1048576.0
        while (level > 1) {
            result *= 10;
            level--;
        }
        return result;
    }

    public long maxFileSizeForLevel() {
        return targetFileSize();
    }

    public long totalFileSize(List<FileMetaData> files) {
        long sum = 0;
        for (FileMetaData file : files) {
            sum += file.getFileSize();
        }
        return sum;
    }

    public boolean needsCompaction() {
        return current.getCompactionScore() >= 1 || current.getFileToCompact() != null;
    }

    public Compaction compactRange(int level, InternalKey begin, InternalKey end) {
        List<FileMetaData> levelInputs = getOverlappingInputs(level, begin, end);
        if (levelInputs.isEmpty()) {
            return null;
        }

        return setupOtherInputs(level, levelInputs);
    }

    public Compaction pickCompaction() {
        // We prefer compactions triggered by too much data in a level over
        // the compactions triggered by seeks.
        boolean sizeCompaction = (current.getCompactionScore() >= 1);
        boolean seekCompaction = (current.getFileToCompact() != null);

        int level;
        List<FileMetaData> levelInputs;
        if (sizeCompaction) {
            level = current.getCompactionLevel();
            checkState(level >= 0);
            checkState(level + 1 < NUM_LEVELS);

            // Pick the first file that comes after compact_pointer_[level]
            levelInputs = new ArrayList<>();
            for (FileMetaData fileMetaData : current.getFiles(level)) {
                if (!compactPointers.containsKey(level) ||
                        internalKeyComparator.compare(fileMetaData.getLargest(), compactPointers.get(level)) > 0) {
                    levelInputs.add(fileMetaData);
                    break;
                }
            }
            if (levelInputs.isEmpty()) {
                // Wrap-around to the beginning of the key space
                levelInputs.add(current.getFiles(level).get(0));
            }
        } else if (seekCompaction) {
            level = current.getFileToCompactLevel();
            levelInputs = ImmutableList.of(current.getFileToCompact());
        } else {
            return null;
        }

        // Files in level 0 may overlap each other, so pick up all overlapping ones
        if (level == 0) {
            Entry<InternalKey, InternalKey> range = getRange(levelInputs);
            // Note that the next call will discard the file we placed in
            // c->inputs_[0] earlier and replace it with an overlapping set
            // which will include the picked file.
            levelInputs = getOverlappingInputs(0, range.getKey(), range.getValue());

            checkState(!levelInputs.isEmpty());
        }

        return setupOtherInputs(level, levelInputs);
    }

    /**
     * find the largest key in a vector of files.
     *
     * @return {@link InternalKey} if {@code files} is no empty, {@code null} otherwise
     */
    private static InternalKey findLargestKey(InternalKeyComparator internalKeyComparator, List<FileMetaData> files) {
        if (files.isEmpty()) {
            return null;
        }
        InternalKey largestKey = files.get(0).getLargest();
        for (FileMetaData file : files) {
            if (internalKeyComparator.compare(file.getLargest(), largestKey) > 0) {
                largestKey = file.getLargest();
            }
        }
        return largestKey;
    }

    /**
     * find minimum file b2=(l2, u2) in level file for which l2 > u1 and userKey(l2) = userKey(u1)
     */
    private static FileMetaData findSmallestBoundaryFile(InternalKeyComparator internalKeyComparator, List<FileMetaData> levelFiles,
                                                         InternalKey largestKey) {
        UserComparator userComparator = internalKeyComparator.getUserComparator();
        FileMetaData smallestBoundaryFile = null;
        for (FileMetaData f : levelFiles) {
            if (internalKeyComparator.compare(f.getSmallest(), largestKey) > 0 &&
                    userComparator.compare(f.getSmallest().getUserKey(), largestKey.getUserKey()) == 0) {
                if (smallestBoundaryFile == null ||
                        internalKeyComparator.compare(f.getSmallest(), smallestBoundaryFile.getSmallest()) < 0) {
                    smallestBoundaryFile = f;
                }
            }
        }
        return smallestBoundaryFile;
    }

    /**
     * Extracts the largest file b1 from {@code compactionFiles} and then searches for a
     * b2 in {@code levelFiles} for which userKey(u1) = userKey(l2). If it finds such a
     * file b2 (known as a boundary file), adds it to {@code compactionFiles} and then
     * searches again using this new upper bound.
     * <p>
     * If there are two blocks, b1=(l1, u1) and b2=(l2, u2) and
     * userKey(u1) = userKey(l2), and if we compact b1 but not b2 then a
     * subsequent get operation will yield an incorrect result because it will
     * return the record from b2 in level i rather than from b1 because it searches
     * level by level for records matching the supplied user key.
     *
     * @param internalKeyComparator internal key comparator
     * @param levelFiles            List of files to search for boundary files.
     * @param compactionFiles       in/out List of files to extend by adding boundary files.
     */
    static void addBoundaryInputs(InternalKeyComparator internalKeyComparator, List<FileMetaData> levelFiles,
                                  List<FileMetaData> compactionFiles) {
        InternalKey largestKey = findLargestKey(internalKeyComparator, compactionFiles);
        if (largestKey == null) {
            return;
        }

        while (true) {
            FileMetaData smallestBoundaryFile =
                    findSmallestBoundaryFile(internalKeyComparator, levelFiles, largestKey);

            // if a boundary file was found advance largestKey, otherwise we're done
            if (smallestBoundaryFile != null) {
                compactionFiles.add(smallestBoundaryFile);
                largestKey = smallestBoundaryFile.getLargest();
            } else {
                break;
            }
        }
    }

    private Compaction setupOtherInputs(int level, List<FileMetaData> levelInputs) {
        addBoundaryInputs(internalKeyComparator, current.getFiles(level), levelInputs);
        Entry<InternalKey, InternalKey> range = getRange(levelInputs);
        InternalKey smallest = range.getKey();
        InternalKey largest = range.getValue();

        List<FileMetaData> levelUpInputs = getOverlappingInputs(level + 1, smallest, largest);
        addBoundaryInputs(internalKeyComparator, current.getFiles(level + 1), levelUpInputs);

        // Get entire range covered by compaction
        range = getRange(levelInputs, levelUpInputs);
        InternalKey allStart = range.getKey();
        InternalKey allLimit = range.getValue();

        // See if we can grow the number of inputs in "level" without
        // changing the number of "level+1" files we pick up.
        if (!levelUpInputs.isEmpty()) {
            List<FileMetaData> expanded0 = getOverlappingInputs(level, allStart, allLimit);
            addBoundaryInputs(internalKeyComparator, current.getFiles(level), expanded0);
            long levelInputSize = totalFileSize(levelInputs);
            long levelUpInputSize = totalFileSize(levelUpInputs);
            long expanded0Size = totalFileSize(expanded0);

            if (expanded0.size() > levelInputs.size()
                    && levelUpInputSize + expanded0Size < expandedCompactionByteSizeLimit()) {
                range = getRange(expanded0);
                InternalKey newStart = range.getKey();
                InternalKey newLimit = range.getValue();

                List<FileMetaData> expanded1 = getOverlappingInputs(level + 1, newStart, newLimit);
                addBoundaryInputs(internalKeyComparator, current.getFiles(level + 1), expanded1);
                if (expanded1.size() == levelUpInputs.size()) {
                    if (Nukkit.DEBUG > 1) options.logger().log(
                            "Expanding@%s %s+%s (%s+%s bytes) to %s+%s (%s+%s bytes)",
                            level,
                            levelInputs.size(),
                            levelUpInputs.size(),
                            levelInputSize, levelUpInputSize,
                            expanded0.size(),
                            expanded1.size(),
                            expanded0Size, levelUpInputSize);
                    largest = newLimit;
                    levelInputs = expanded0;
                    levelUpInputs = expanded1;

                    range = getRange(levelInputs, levelUpInputs);
                    allStart = range.getKey();
                    allLimit = range.getValue();
                }
            }
        }

        // Compute the set of grandparent files that overlap this compaction
        // (parent == level+1; grandparent == level+2)
        final List<FileMetaData> grandparents;
        if (level + 2 < NUM_LEVELS) {
            grandparents = getOverlappingInputs(level + 2, allStart, allLimit);
        } else {
            grandparents = Collections.emptyList();
        }

        Compaction compaction = new Compaction(current, level, maxFileSizeForLevel(), levelInputs, levelUpInputs, grandparents);

        // Update the place where we will do the next compaction for this level.
        // We update this immediately instead of waiting for the VersionEdit
        // to be applied so that if the compaction fails, we will try a different
        // key range next time.
        compactPointers.put(level, largest);
        compaction.getEdit().setCompactPointer(level, largest);

        return compaction;
    }

    List<FileMetaData> getOverlappingInputs(int level, InternalKey begin, InternalKey end) {
        Preconditions.checkArgument(level >= 0 && level <= DbConstants.NUM_LEVELS, "Invalid level value %s", level);
        List<FileMetaData> inputs = new ArrayList<>();
        Slice userBegin = begin == null ? null : begin.getUserKey();
        Slice userEnd = end == null ? null : end.getUserKey();
        UserComparator userComparator = internalKeyComparator.getUserComparator();
        List<FileMetaData> filesInLevel = current.getFiles(level);
        for (int i = 0; i < filesInLevel.size(); i++) {
            FileMetaData fileMetaData = filesInLevel.get(i);
            Slice fileStart = fileMetaData.getSmallest().getUserKey();
            Slice fileLimit = fileMetaData.getLargest().getUserKey();
            if (begin != null && userComparator.compare(fileLimit, userBegin) < 0) {
                // "files1" is completely before specified range; skip it
            } else if (end != null && userComparator.compare(fileStart, userEnd) > 0) {
                // "files1" is completely after specified range; skip it
            } else {
                inputs.add(fileMetaData);
                if (level == 0) {
                    // Level-0 files may overlap each other.  So check if the newly
                    // added file has expanded the range.  If so, restart search.
                    if (begin != null && userComparator.compare(fileStart, userBegin) < 0) {
                        userBegin = fileStart;
                        inputs.clear();
                        i = -1;
                    } else if (end != null && userComparator.compare(fileLimit, userEnd) > 0) {
                        userEnd = fileLimit;
                        inputs.clear();
                        i = -1;
                    }
                }
            }
        }
        return inputs;
    }

    private Entry<InternalKey, InternalKey> getRange(List<FileMetaData>... inputLists) {
        InternalKey smallest = null;
        InternalKey largest = null;
        for (List<FileMetaData> inputList : inputLists) {
            for (FileMetaData fileMetaData : inputList) {
                if (smallest == null) {
                    smallest = fileMetaData.getSmallest();
                    largest = fileMetaData.getLargest();
                } else {
                    if (internalKeyComparator.compare(fileMetaData.getSmallest(), smallest) < 0) {
                        smallest = fileMetaData.getSmallest();
                    }
                    if (internalKeyComparator.compare(fileMetaData.getLargest(), largest) > 0) {
                        largest = fileMetaData.getLargest();
                    }
                }
            }
        }
        return Maps.immutableEntry(smallest, largest);
    }

    public long getMaxNextLevelOverlappingBytes() {
        long result = 0;
        for (int level = 1; level < NUM_LEVELS - 1; level++) {
            for (FileMetaData fileMetaData : current.getFiles(level)) {
                List<FileMetaData> overlaps = getOverlappingInputs(level + 1, fileMetaData.getSmallest(), fileMetaData.getLargest());
                long totalSize = 0;
                for (FileMetaData overlap : overlaps) {
                    totalSize += overlap.getFileSize();
                }
                result = Math.max(result, totalSize);
            }
        }
        return result;
    }

    public CharSequence levelSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("files[ ");
        for (int level = 0; level < NUM_LEVELS; level++) {
            sb.append(" ");
            sb.append(current.getFiles(level).size());
        }
        sb.append(" ]");
        return sb;
    }

    /**
     * A helper class so we can efficiently apply a whole sequence
     * of edits to a particular state without creating intermediate
     * Versions that contain full copies of the intermediate state.
     */
    private static class Builder
            implements AutoCloseable {
        private final VersionSet versionSet;
        private final Version baseVersion;
        private final List<LevelState> levels;

        private Builder(VersionSet versionSet, Version baseVersion) {
            this.versionSet = versionSet;
            this.baseVersion = baseVersion;
            baseVersion.retain();

            levels = new ArrayList<>(baseVersion.numberOfLevels());
            for (int i = 0; i < baseVersion.numberOfLevels(); i++) {
                levels.add(new LevelState(versionSet.internalKeyComparator));
            }
        }

        /**
         * Apply the specified edit to the current state.
         */
        public void apply(VersionEdit edit) {
            // Update compaction pointers
            for (Entry<Integer, InternalKey> entry : edit.getCompactPointers().entrySet()) {
                Integer level = entry.getKey();
                InternalKey internalKey = entry.getValue();
                versionSet.compactPointers.put(level, internalKey);
            }

            // Delete files
            for (Entry<Integer, Long> entry : edit.getDeletedFiles().entries()) {
                int level = entry.getKey();
                long fileNumber = entry.getValue();
                levels.get(level).deletedFiles.add(fileNumber);
            }

            // Add new files
            for (Entry<Integer, FileMetaData> entry : edit.getNewFiles().entries()) {
                int level = entry.getKey();
                FileMetaData fileMetaData = entry.getValue();

                // We arrange to automatically compact this file after
                // a certain number of seeks.  Let's assume:
                //   (1) One seek costs 10ms
                //   (2) Writing or reading 1MB costs 10ms (100MB/s)
                //   (3) A compaction of 1MB does 25MB of IO:
                //         1MB read from this level
                //         10-12MB read from next level (boundaries may be misaligned)
                //         10-12MB written to next level
                // This implies that 25 seeks cost the same as the compaction
                // of 1MB of data.  I.e., one seek costs approximately the
                // same as the compaction of 40KB of data.  We are a little
                // conservative and allow approximately one seek for every 16KB
                // of data before triggering a compaction.
                int allowedSeeks = (int) (fileMetaData.getFileSize() >> 14);
                if (allowedSeeks < 100) {
                    allowedSeeks = 100;
                }
                fileMetaData.setAllowedSeeks(allowedSeeks);

                LevelState getLevel = levels.get(level);
                getLevel.deletedFiles.remove(fileMetaData.getNumber());
                getLevel.addedFiles.add(fileMetaData);
            }
        }

        /**
         * Saves the current state in specified version.
         */
        public void saveTo(Version version)
                throws IOException {
            FileMetaDataBySmallestKey cmp = new FileMetaDataBySmallestKey(versionSet.internalKeyComparator);
            for (int level = 0; level < baseVersion.numberOfLevels(); level++) {
                // Merge the set of added files with the set of pre-existing files.
                // Drop any deleted files.  Store the result in *v.

                Collection<FileMetaData> baseFiles = baseVersion.getFiles(level);
                if (baseFiles == null) {
                    baseFiles = Collections.emptyList();
                }
                SortedSet<FileMetaData> addedFiles = levels.get(level).addedFiles;
                if (addedFiles == null) {
                    addedFiles = ImmutableSortedSet.of();
                }

                // files must be added in sorted order so assertion check in maybeAddFile works
                ArrayList<FileMetaData> sortedFiles = new ArrayList<>(baseFiles.size() + addedFiles.size());
                sortedFiles.addAll(baseFiles);
                sortedFiles.addAll(addedFiles);
                sortedFiles.sort(cmp);

                for (FileMetaData fileMetaData : sortedFiles) {
                    maybeAddFile(version, level, fileMetaData);
                }

                //#ifndef NDEBUG  todo
                // Make sure there is no overlap in levels > 0
                version.assertNoOverlappingFiles(level);
                //#endif
            }
        }

        private void maybeAddFile(Version version, int level, FileMetaData fileMetaData)
                throws IOException {
            if (levels.get(level).deletedFiles.contains(fileMetaData.getNumber())) {
                // File is deleted: do nothing
            } else {
                List<FileMetaData> files = version.getFiles(level);
                if (level > 0 && !files.isEmpty()) {
                    // Must not overlap
                    boolean filesOverlap = versionSet.internalKeyComparator.compare(files.get(files.size() - 1).getLargest(), fileMetaData.getSmallest()) >= 0;
                    if (filesOverlap) {
                        // A memory compaction, while this compaction was running, resulted in a a database state that is
                        // incompatible with the compaction.  This is rare and expensive to detect while the compaction is
                        // running, so we catch here simply discard the work.
                        throw new IOException(String.format("Compaction is obsolete: Overlapping files %s and %s in level %s",
                                files.get(files.size() - 1).getNumber(),
                                fileMetaData.getNumber(), level));
                    }
                }
                version.addFile(level, fileMetaData);
            }
        }

        @Override
        public void close() {
            baseVersion.release();
        }

        private static class FileMetaDataBySmallestKey
                implements Comparator<FileMetaData> {
            private final InternalKeyComparator internalKeyComparator;

            private FileMetaDataBySmallestKey(InternalKeyComparator internalKeyComparator) {
                this.internalKeyComparator = internalKeyComparator;
            }

            @Override
            public int compare(FileMetaData f1, FileMetaData f2) {
                return ComparisonChain
                        .start()
                        .compare(f1.getSmallest(), f2.getSmallest(), internalKeyComparator)
                        .compare(f1.getNumber(), f2.getNumber())
                        .result();
            }
        }

        private static class LevelState {
            private final SortedSet<FileMetaData> addedFiles;
            private final LongOpenHashSet deletedFiles = new LongOpenHashSet();

            public LevelState(InternalKeyComparator internalKeyComparator) {
                addedFiles = new TreeSet<>(new FileMetaDataBySmallestKey(internalKeyComparator));
            }

            @Override
            public String toString() {
                return "LevelState" +
                        "{addedFiles=" + addedFiles +
                        ", deletedFiles=" + deletedFiles +
                        '}';
            }
        }
    }
}
