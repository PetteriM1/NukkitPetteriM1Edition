package cn.nukkit.item.custom;

public interface CustomItem {

    default String getIdentifier() {
        return this.getItemDefinition().getIdentifier();
    }

    ItemDefinition getItemDefinition();
}
