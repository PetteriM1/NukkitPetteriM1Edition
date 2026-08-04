package cn.nukkit.level.persistence;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;

public interface PersistentDataContainer {
    String STORAGE_TAG = "persistent_storage";

    void setStorage(CompoundTag storage);

    default CompoundTag getReadStorage() {
        return this.getStorage();
    }

    CompoundTag getStorage();

    default boolean isEmpty() {
        return this.getReadStorage().isEmpty();
    }

    default void clearStorage() {
        this.setStorage(new CompoundTag());
    }

    default <T> T get(String key, PersistentDataType<T> type) {
        Tag tag = this.getReadStorage().get(key);
        if (tag != null && type.validate(tag)) {
            return type.deserialize(tag);
        }
        if (tag != null) {
            this.remove(key);
        }
        return null;
    }

    default boolean has(String key, PersistentDataType<?> type) {
        Tag tag = this.getReadStorage().get(key);
        return tag != null && type.validate(tag);
    }

    default void remove(String key) {
        if (this.getReadStorage().contains(key)) {
            this.getStorage().remove(key);
            this.write();
        }
    }

    default <T> void set(String key, PersistentDataType<T> type, T value) {
        if (value == null) {
            this.remove(key);
        } else {
            this.getStorage().put(key, type.serialize(value));
            this.write();
        }
    }

    default void write() {
    }
}
