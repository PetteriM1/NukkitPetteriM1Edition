package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.entity.item.EntityArmorStand;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.MobEquipmentPacket;

public class EntityEquipmentInventory extends BaseInventory {

    private final EntityArmorStand entityLiving;

    private static final int MAINHAND = 0;
    private static final int OFFHAND = 1;

    public EntityEquipmentInventory(EntityArmorStand entity) {
        super(entity, InventoryType.ENTITY_EQUIPMENT);
        this.entityLiving = entity;
    }

    @Override
    public InventoryHolder getHolder() {
        return this.holder;
    }

    public Item getItemInHand() {
        return this.getItem(MAINHAND);
    }

    @Override
    public String getName() {
        return "Entity Equipment";
    }

    public Item getOffHandItem() {
        return this.getItem(OFFHAND);
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public void sendContents(Player target) {
        this.sendSlot(MAINHAND, target);
        this.sendSlot(OFFHAND, target);
    }

    @Override
    public void sendContents(Player... target) {
        for (Player player : target) {
            this.sendContents(player);
        }
    }

    @Override
    public void sendSlot(int index, Player... players) {
        for (Player player : players) {
            this.sendSlot(index, player);
        }
    }

    @Override
    public void sendSlot(int index, Player player) {
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.eid = this.entityLiving.getId();
        mobEquipmentPacket.inventorySlot = mobEquipmentPacket.hotbarSlot = index;
        mobEquipmentPacket.item = this.getItem(index);
        player.dataPacket(mobEquipmentPacket);
    }

    public boolean setItemInHand(Item item, boolean send) {
        return this.setItem(MAINHAND, item, send);
    }

    public boolean setOffhandItem(Item item, boolean send) {
        return this.setItem(OFFHAND, item, send);
    }
}
