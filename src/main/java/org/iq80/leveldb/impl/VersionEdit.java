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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.iq80.leveldb.util.DynamicSliceOutput;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.SliceInput;
import org.iq80.leveldb.util.VariableLengthQuantity;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class VersionEdit {
    private String comparatorName;
    private Long logNumber;
    private Long nextFileNumber;
    private Long previousLogNumber;
    private Long lastSequenceNumber;
    private final Map<Integer, InternalKey> compactPointers = new TreeMap<>();
    private final Multimap<Integer, FileMetaData> newFiles = ArrayListMultimap.create();
    private final Multimap<Integer, Long> deletedFiles = ArrayListMultimap.create();

    public VersionEdit() {
    }

    public VersionEdit(Slice slice) {
        SliceInput sliceInput = slice.input();
        while (sliceInput.isReadable()) {
            int i = VariableLengthQuantity.readVariableLengthInt(sliceInput);
            VersionEditTag tag = VersionEditTag.getValueTypeByPersistentId(i);
            tag.readValue(sliceInput, this);
        }
    }

    public void setCompactPointers(Map<Integer, InternalKey> compactPointers) {
        this.compactPointers.putAll(compactPointers);
    }

    public void setComparatorName(String comparatorName) {
        this.comparatorName = comparatorName;
    }

    public void setLastSequenceNumber(long lastSequenceNumber) {
        this.lastSequenceNumber = lastSequenceNumber;
    }

    public void setLogNumber(long logNumber) {
        this.logNumber = logNumber;
    }

    public void setNextFileNumber(long nextFileNumber) {
        this.nextFileNumber = nextFileNumber;
    }

    public void setPreviousLogNumber(long previousLogNumber) {
        this.previousLogNumber = previousLogNumber;
    }

    public Map<Integer, InternalKey> getCompactPointers() {
        return Collections.unmodifiableMap(compactPointers);
    }

    public String getComparatorName() {
        return comparatorName;
    }

    public Multimap<Integer, Long> getDeletedFiles() {
        return ImmutableMultimap.copyOf(deletedFiles);
    }

    public Long getLastSequenceNumber() {
        return lastSequenceNumber;
    }

    public Long getLogNumber() {
        return logNumber;
    }

    public Multimap<Integer, FileMetaData> getNewFiles() {
        return ImmutableMultimap.copyOf(newFiles);
    }

    public Long getNextFileNumber() {
        return nextFileNumber;
    }

    public Long getPreviousLogNumber() {
        return previousLogNumber;
    }

    // Add the specified file at the specified level.
    // REQUIRES: This version has not been saved (see VersionSet::SaveTo)
    // REQUIRES: "smallest" and "largest" are smallest and largest keys in file
    public void addFile(int level, long fileNumber,
                        long fileSize,
                        InternalKey smallest,
                        InternalKey largest) {
        FileMetaData fileMetaData = new FileMetaData(fileNumber, fileSize, smallest, largest);
        addFile(level, fileMetaData);
    }

    public void addFile(int level, FileMetaData fileMetaData) {
        newFiles.put(level, fileMetaData);
    }

    public void addFiles(Multimap<Integer, FileMetaData> files) {
        newFiles.putAll(files);
    }

    // Delete the specified "file" from the specified "level".
    public void deleteFile(int level, long fileNumber) {
        deletedFiles.put(level, fileNumber);
    }

    public Slice encode() {
        DynamicSliceOutput dynamicSliceOutput = new DynamicSliceOutput(4096);
        for (VersionEditTag versionEditTag : VersionEditTag.values()) {
            versionEditTag.writeValue(dynamicSliceOutput, this);
        }
        return dynamicSliceOutput.slice();
    }

    public void setCompactPointer(int level, InternalKey key) {
        compactPointers.put(level, key);
    }

    @Override
    public String toString() {
        return "VersionEdit" +
                "{comparatorName='" + comparatorName + '\'' +
                ", logNumber=" + logNumber +
                ", previousLogNumber=" + previousLogNumber +
                ", lastSequenceNumber=" + lastSequenceNumber +
                ", compactPointers=" + compactPointers +
                ", newFiles=" + newFiles +
                ", deletedFiles=" + deletedFiles +
                '}';
    }
}
