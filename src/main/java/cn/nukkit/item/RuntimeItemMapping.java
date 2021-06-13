package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.item.customitem.ItemCustom;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.BinaryStream;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

import java.util.Collection;
import java.util.HashMap;

public class RuntimeItemMapping {

    private final int protocolId;

    private final Collection<RuntimeItems.Entry> entries;
    private final HashMap<Integer, Class<? extends Item>> customItems = new HashMap<>();

    private final Int2IntMap legacyNetworkMap;
    private final Int2IntMap networkLegacyMap;

    private byte[] itemDataPalette;

    public RuntimeItemMapping(int protocolId,
                              Collection<RuntimeItems.Entry> entries,
                              byte[] itemDataPalette,
                              Int2IntMap legacyNetworkMap,
                              Int2IntMap networkLegacyMap) {
        this.protocolId = protocolId;
        this.entries = entries;
        this.itemDataPalette = itemDataPalette;
        this.legacyNetworkMap = legacyNetworkMap;
        this.networkLegacyMap = networkLegacyMap;
        this.legacyNetworkMap.defaultReturnValue(-1);
        this.networkLegacyMap.defaultReturnValue(-1);
    }

    public void registeredCustomItem(int id, Class<? extends ItemCustom> classz) {
        if (!Server.getInstance().enableCustomItems) {
            return;
        }
        this.customItems.put(id, classz);
        Item.list[id] = classz;
        this.refreshItemPalette();
    }

    public void refreshItemPalette() {
        BinaryStream paletteBuffer = new BinaryStream();
        paletteBuffer.putUnsignedVarInt(entries.size() + customItems.size());

        for (RuntimeItems.Entry entry : entries) {
            paletteBuffer.putString(entry.name);
            paletteBuffer.putLShort(entry.id);

            if (protocolId >= ProtocolInfo.v1_16_100) {
                paletteBuffer.putBoolean(false); // Component item
            }
        }

        if (Server.getInstance().enableCustomItems && !customItems.isEmpty() && protocolId >= ProtocolInfo.v1_16_100) {
            for(Integer id : customItems.keySet()) {
                Item item = Item.get(id);
                if (item instanceof ItemCustom) {
                    ItemCustom itemCustom = (ItemCustom) item;
                    paletteBuffer.putString("CustomItem:" + itemCustom.getName());
                    paletteBuffer.putLShort(id);
                    paletteBuffer.putBoolean(true); // Component item
                }
            }
        }

        this.itemDataPalette = paletteBuffer.getBuffer();
    }

    public HashMap<Integer, Class<? extends Item>> getCustomItems() {
        return new HashMap<>(customItems);
    }

    public int getNetworkFullId(Item item) {
        int fullId = RuntimeItems.getFullId(item.getId(), item.hasMeta() ? item.getDamage() : -1);
        int networkId = this.legacyNetworkMap.get(fullId);
        if (networkId == -1) {
            networkId = this.legacyNetworkMap.get(RuntimeItems.getFullId(item.getId(), 0));
        }
        if (networkId == -1) {
            throw new IllegalArgumentException("Unknown item mapping " + item);
        }

        return networkId;
    }

    public int getLegacyFullId(int networkId) {
        int fullId = networkLegacyMap.get(networkId);
        if (fullId == -1) {
            throw new IllegalArgumentException("Unknown network mapping: " + networkId);
        }
        return fullId;
    }

    public int getProtocolId() {
        return this.protocolId;
    }

    public byte[] getItemDataPalette() {
        return this.itemDataPalette;
    }
}
