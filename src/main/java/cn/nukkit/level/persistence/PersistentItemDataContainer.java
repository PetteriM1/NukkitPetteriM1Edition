package cn.nukkit.level.persistence;

public interface PersistentItemDataContainer extends PersistentDataContainer {

    boolean convertsToBlock();

    void setConvertsToBlock(boolean value);
}
