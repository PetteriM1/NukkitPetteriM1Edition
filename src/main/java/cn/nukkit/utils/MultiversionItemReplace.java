package cn.nukkit.utils;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ProtocolInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author lt_name
 */
public class MultiversionItemReplace {

    private static final Int2ObjectOpenHashMap<Node> NODE_MAP  = new Int2ObjectOpenHashMap<>();

    static {
        for (Map map : new Config(Config.YAML).loadFromStream(Server.class.getClassLoader().getResourceAsStream("multiversion_item_replace.json")).getMapList("items")) {
            try {
                int originalId = Utils.toInt(map.get("originalId"));
                NODE_MAP.put(originalId,
                        new Node(Utils.toInt(map.get("protocolId")),
                                originalId,
                                Utils.toInt(map.get("newId"))));
            } catch (Exception e) {
                MainLogger.getLogger().logException(e);
            }
        }
    }

    public static int getReplaceId(int originalId, int protocolId) {
        if (NODE_MAP.containsKey(originalId)) {
            Node node = NODE_MAP.get(originalId);
            if (protocolId < node.getProtocolId()) {
                return node.getNewId();
            }
        }
        return 0;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Node {

        private int protocolId = ProtocolInfo.CURRENT_PROTOCOL; //Need to be replaced when less than this agreement
        private int originalId = Item.INFO_UPDATE;
        private int newId = Item.INFO_UPDATE;

    }

}
