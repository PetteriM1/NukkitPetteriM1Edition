package cn.nukkit.item.custom;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.RuntimeItemMapping;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.ItemComponentPacket;
import cn.nukkit.network.protocol.ProtocolInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;

/**
 * Handles custom item registry.
 * <p>
 * See <a href="https://github.com/PetteriM1/CustomItemExample">CustomItemExample</a> for example usage
 */
public class CustomItemManager {

    /**
     * Lowest allowed Nukkit save id for custom items
     */
    public static final int LOWEST_CUSTOM_ITEM_ID = 5000;

    private static final CustomItemManager INSTANCE = new CustomItemManager();
    private final Map<String, ItemDefinition> itemDefinitions = new HashMap<>();
    private final Int2ObjectMap<ItemDefinition> legacyDefinitions = new Int2ObjectOpenHashMap<>();
    private volatile boolean closed;
    private BatchPacket cachedPacket2168;
    private BatchPacket cachedPacket1001;
    private BatchPacket cachedPacket975;
    private BatchPacket cachedPacket924;
    private BatchPacket cachedPacket897;
    private BatchPacket cachedPacket21120;
    private BatchPacket cachedPacket21110;
    private BatchPacket cachedPacket21100;
    private BatchPacket cachedPacket2190;
    private BatchPacket cachedPacket2180;
    private BatchPacket cachedPacket2170;
    private BatchPacket cachedPacket2160;
    private BatchPacket cachedPacketCustomOnly;
    private BatchPacket cachedPacketOld;
    private BatchPacket cachedPacketOldNoSnappy;
    private CustomItemManager() {

    }

    /**
     * Get CustomItemManager instance
     */
    public static CustomItemManager get() {
        return INSTANCE;
    }

    public BatchPacket getCachedPacket(int protocol) {
        if (protocol < ProtocolInfo.v1_16_100) {
            throw new UnsupportedOperationException("Unsupported protocol");
        }

        if (protocol >= ProtocolInfo.v1_26_40) {
            if (this.cachedPacket2168 == null) {
                this.cachedPacket2168 = createPacket(ProtocolInfo.v1_26_40);
            }
            return this.cachedPacket2168;
        } else if (protocol >= ProtocolInfo.v1_26_30) {
            if (this.cachedPacket1001 == null) {
                this.cachedPacket1001 = createPacket(ProtocolInfo.v1_26_30);
            }
            return this.cachedPacket1001;
        } else if (protocol >= ProtocolInfo.v1_26_20_26) {
            if (this.cachedPacket975 == null) {
                this.cachedPacket975 = createPacket(ProtocolInfo.v1_26_20_26);
            }
            return this.cachedPacket975;
        } else if (protocol >= ProtocolInfo.v1_26_0) {
            if (this.cachedPacket924 == null) {
                this.cachedPacket924 = createPacket(ProtocolInfo.v1_26_0);
            }
            return this.cachedPacket924;
        } else if (protocol >= ProtocolInfo.v1_21_130_28) {
            if (this.cachedPacket897 == null) {
                this.cachedPacket897 = createPacket(ProtocolInfo.v1_21_130_28);
            }
            return this.cachedPacket897;
        } else if (protocol >= ProtocolInfo.v1_21_120) {
            if (this.cachedPacket21120 == null) {
                this.cachedPacket21120 = createPacket(ProtocolInfo.v1_21_120);
            }
            return this.cachedPacket21120;
        } else if (protocol >= ProtocolInfo.v1_21_110) {
            if (this.cachedPacket21110 == null) {
                this.cachedPacket21110 = createPacket(ProtocolInfo.v1_21_110);
            }
            return this.cachedPacket21110;
        } else if (protocol >= ProtocolInfo.v1_21_100) {
            if (this.cachedPacket21100 == null) {
                this.cachedPacket21100 = createPacket(ProtocolInfo.v1_21_100);
            }
            return this.cachedPacket21100;
        } else if (protocol >= ProtocolInfo.v1_21_90) {
            if (this.cachedPacket2190 == null) {
                this.cachedPacket2190 = createPacket(ProtocolInfo.v1_21_90);
            }
            return this.cachedPacket2190;
        } else if (protocol >= ProtocolInfo.v1_21_80) {
            if (this.cachedPacket2180 == null) {
                this.cachedPacket2180 = createPacket(ProtocolInfo.v1_21_80);
            }
            return this.cachedPacket2180;
        } else if (protocol >= ProtocolInfo.v1_21_70_24) {
            if (this.cachedPacket2170 == null) {
                this.cachedPacket2170 = createPacket(ProtocolInfo.v1_21_70_24);
            }
            return this.cachedPacket2170;
        } else if (protocol >= ProtocolInfo.v1_21_60) {
            if (this.cachedPacket2160 == null) {
                this.cachedPacket2160 = createPacket(ProtocolInfo.v1_21_60);
            }
            return this.cachedPacket2160;
        } else if (protocol >= ProtocolInfo.v1_20_60) {
            if (this.cachedPacketCustomOnly == null) {
                ItemComponentPacket pk = new ItemComponentPacket();
                pk.protocol = ProtocolInfo.v1_20_60;
                pk.itemDefinitions = new ArrayList<>(this.itemDefinitions.size());
                this.itemDefinitions.values().forEach((def) -> pk.itemDefinitions.add(new ItemComponentPacket.ItemDefinition(def.getIdentifier(), -1, true, 0, def.getNetworkData(pk.protocol))));
                pk.tryEncode();
                this.cachedPacketCustomOnly = pk.compress(Deflater.BEST_COMPRESSION);
            }
            return this.cachedPacketCustomOnly;
        } else if (protocol >= ProtocolInfo.v1_19_30_23 || !Server.getInstance().useSnappy) {
            if (this.cachedPacketOld == null) {
                ItemComponentPacket pk = new ItemComponentPacket();
                pk.protocol = ProtocolInfo.v1_19_30_23;
                pk.itemDefinitions = new ArrayList<>(this.itemDefinitions.size());
                this.itemDefinitions.values().forEach((def) -> pk.itemDefinitions.add(new ItemComponentPacket.ItemDefinition(def.getIdentifier(), -1, true, 0, def.getNetworkData(pk.protocol))));
                pk.tryEncode();
                this.cachedPacketOld = pk.compress(Deflater.BEST_COMPRESSION);
            }
            return this.cachedPacketOld;
        } else {
            if (this.cachedPacketOldNoSnappy == null) {
                ItemComponentPacket pk = new ItemComponentPacket();
                pk.protocol = ProtocolInfo.v1_19_30_23 - 1;
                pk.itemDefinitions = new ArrayList<>(this.itemDefinitions.size());
                this.itemDefinitions.values().forEach((def) -> pk.itemDefinitions.add(new ItemComponentPacket.ItemDefinition(def.getIdentifier(), -1, true, 0, def.getNetworkData(pk.protocol))));
                pk.tryEncode();
                this.cachedPacketOldNoSnappy = pk.compress(Deflater.BEST_COMPRESSION);
            }
            return this.cachedPacketOldNoSnappy;
        }
    }

    private BatchPacket createPacket(int protocol) {
        ItemComponentPacket pk = new ItemComponentPacket();
        pk.protocol = protocol;
        Collection<ItemComponentPacket.ItemDefinition> vanillaItems = RuntimeItems.getMapping(protocol).getVanillaItemDefinitions();
        pk.itemDefinitions = new ArrayList<>(vanillaItems.size() + this.itemDefinitions.size());
        pk.itemDefinitions.addAll(vanillaItems);
        this.itemDefinitions.values().forEach((def) -> pk.itemDefinitions.add(new ItemComponentPacket.ItemDefinition(def.getIdentifier(), def.getLegacyId(), true, 1, def.getNetworkData(protocol))));
        pk.tryEncode();
        return pk.compress(Deflater.BEST_COMPRESSION);
    }

    @SuppressWarnings("unused")
    public void registerDefinition(ItemDefinition definition) {
        if (this.closed) {
            throw new IllegalStateException("Item registry was already closed");
        }

        if (definition.getLegacyId() < LOWEST_CUSTOM_ITEM_ID) {
            throw new IllegalArgumentException("Custom item ID can not be lower than " + LOWEST_CUSTOM_ITEM_ID);
        }

        if (definition.getLegacyId() > 65534) {
            throw new IllegalArgumentException("Custom item ID can not be higher than 65534");
        }

        if (this.legacyDefinitions.containsKey(definition.getLegacyId())) {
            throw new IllegalArgumentException("Custom item " + definition.getIdentifier() + " cannot be registered because legacy ID " +
                    definition.getLegacyId() + " is already used by " + this.getDefinition(definition.getLegacyId()).getIdentifier());
        }

        if (this.itemDefinitions.containsKey(definition.getIdentifier())) {
            throw new IllegalArgumentException("Custom item " + definition.getIdentifier() + " was already registered");
        }

        this.itemDefinitions.put(definition.getIdentifier(), definition);
        this.legacyDefinitions.put(definition.getLegacyId(), definition);

        Item.list[definition.getLegacyId()] = definition.getImplementation();

        for (RuntimeItemMapping mapping : RuntimeItems.VALUES) {
            mapping.registerItem(definition.getIdentifier(), definition.getLegacyId(), definition.getLegacyId(), 0);
        }

        if (definition.getCreativeCategory() != null && definition.getCreativeCategory() != ItemDefinition.CreativeCategory.NONE) {
            Item.addCustomCreativeItem(definition);
        }
    }

    public ItemDefinition getDefinition(String identifier) {
        return this.itemDefinitions.get(identifier);
    }

    public ItemDefinition getDefinition(int legacyId) {
        return this.legacyDefinitions.get(legacyId);
    }

    public int getLegacyId(String identifier) {
        ItemDefinition definition = this.itemDefinitions.get(identifier);
        if (definition == null) {
            return -1;
        }
        return definition.getLegacyId();
    }

    public boolean hasCustomItems() {
        return !this.itemDefinitions.isEmpty();
    }

    /**
     * Internal: close registry to prepare for data generation
     */
    public boolean closeRegistry() {
        if (this.closed) {
            throw new IllegalStateException("Item registry was already closed");
        }

        this.closed = true;

        this.getCachedPacket(ProtocolInfo.CURRENT_PROTOCOL);

        return this.hasCustomItems();
    }
}
