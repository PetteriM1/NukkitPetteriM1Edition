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
package org.iq80.leveldb;

import java.io.Closeable;
import java.util.Map;

/**
 * A DB is a persistent ordered map from keys to values.
 * A DB is safe for concurrent access from multiple threads without
 * any external synchronization.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public interface DB
        extends Iterable<Map.Entry<byte[], byte[]>>, Closeable {
    /**
     * Compact the underlying storage for the key range [begin, end].
     * In particular, deleted and overwritten versions are discarded,
     * and the data is rearranged to reduce the cost of operations
     * needed to access the data.  This operation should typically only
     * be invoked by users who understand the underlying implementation.
     * <p>
     * <p>
     * {@code begin == null} is treated as before all keys in the database.
     * {@code end == null} is treated as a key after all keys in the database.
     * <p>
     * Therefore the call to {@code db.compactRange(null, null);} will compact
     * the entire database.
     *
     * @param begin if null then compaction start from the first key
     * @param end   if null then compaction ends at the last key
     */
    void compactRange(byte[] begin, byte[] end)
            throws DBException;

    WriteBatch createWriteBatch();

    void delete(byte[] key)
            throws DBException;

    /**
     * Remove the database entry (if any) for "key".
     *
     * @return null if {@link WriteOptions#snapshot()}==false otherwise returns a snapshot
     * of the DB after this operation.
     * @throws DBException on any write failure. It is not an error if "key"
     *                     did not exist in the database.
     */
    Snapshot delete(byte[] key, WriteOptions options)
            throws DBException;

    /**
     * Same as calling {@link DB#get(byte[], ReadOptions)} with default options
     */
    byte[] get(byte[] key)
            throws DBException;

    /**
     * If the database contains an entry for "key" return its corresponding
     * value.
     * <p>
     * If there is no entry for "key" return null.
     *
     * @param key     key to search for
     * @param options read option
     * @return key value or {@code null}
     * @throws DBException if error occurred in accessing db sate
     */
    byte[] get(byte[] key, ReadOptions options)
            throws DBException;

    /**
     * For each i in [0,n-1], store in "sizes[i]", the approximate
     * file system space used by keys in "[range[i].start .. range[i].limit)".
     * <p>
     * Note that the returned sizes measure file system space usage, so
     * if the user data compresses by a factor of ten, the returned
     * sizes will be one-tenth the size of the corresponding user data size.
     * <p>
     * The results may not include the sizes of recently written data.
     *
     * @param ranges each range to test for
     * @return array with size result of each range
     */
    long[] getApproximateSizes(Range... ranges);

    /**
     * DB implementations can export properties about their state
     * via this method.  If "property" is a valid property understood by this
     * DB implementation, fills "*value" with its current value and returns
     * true.  Otherwise returns false.
     * <p>
     * <p>
     * Valid property names include:
     * <p>
     * <ul>
     * <li>"leveldb.num-files-at-level<N>" - return the number of files at level <N>,
     * where <N> is an ASCII representation of a level number (e.g. "0").</li>
     * <li>"leveldb.stats" - returns a multi-line string that describes statistics
     * about the internal operation of the DB.</li>
     * <li>"leveldb.sstables" - returns a multi-line string that describes all
     * of the sstables that make up the db contents.</li>
     * <li>"leveldb.approximate-memory-usage" - returns the approximate number of
     * bytes of memory in use by the DB.</li>
     * </ul>
     *
     * @param name property name
     * @return property value, {@code null} if property does not exist
     */
    String getProperty(String name);

    /**
     * Return a handle to the current DB state.  Iterators created with
     * this handle will all observe a stable snapshot of the current DB
     * state.  The caller must call {@link Snapshot#close()} when the
     * snapshot is no longer needed.
     *
     * @return current db state Snapshot handle
     */
    Snapshot getSnapshot();

    /**
     * Same as calling {@link DB#iterator(ReadOptions)} with default options
     */
    @Override
    DBIterator iterator();

    /**
     * Return a iterator over the contents of the database.
     * The result of {@link DB#iterator(ReadOptions)} will automatically position
     * itsel to first entry if seek method is not called.
     * <p>
     * It is preferable to call one of the Seek methods on the iterator before
     * using it.
     * <p>
     * Caller should call {@link DBIterator#close()} when it is no longer needed.
     * The returned iterator should be closed before this db is deleted.
     *
     * @param options iterator read options
     * @return new iterator content of the database
     */
    DBIterator iterator(ReadOptions options);

    void put(byte[] key, byte[] value)
            throws DBException;

    /**
     * Set the database entry for "key" to "value".
     * <p>
     * Note: consider setting options.sync = true.
     *
     * @param key   entry key
     * @param value entry valye
     * @return null if options.isSnapshot()==false otherwise returns a snapshot
     * of the DB after this operation.
     * @throws DBException on any write failure
     */
    Snapshot put(byte[] key, byte[] value, WriteOptions options)
            throws DBException;

    /**
     * Resumes the background compaction threads.
     */
    void resumeCompactions();

    /**
     * Suspends any background compaction threads.  This methods
     * returns once the background compactions are suspended.
     */
    void suspendCompactions()
            throws InterruptedException;

    void write(WriteBatch updates)
            throws DBException;

    /**
     * Apply the specified updates to the database.
     * <p>
     * Note: consider setting options.sync = true.
     *
     * @return null if {@link WriteOptions#snapshot()}==false otherwise returns a snapshot
     * of the DB after this operation.
     * @throws DBException on any write failure
     */
    Snapshot write(WriteBatch updates, WriteOptions options)
            throws DBException;
}
