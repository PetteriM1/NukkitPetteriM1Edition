package cn.nukkit.item;

import cn.nukkit.Server;
import cn.nukkit.utils.BinaryStream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@UtilityClass
public class RuntimeItems {

    private static final Gson GSON = new Gson();
    private static final Type ENTRY_TYPE = new TypeToken<ArrayList<Entry>>(){}.getType();

    public static final byte[] ITEM_DATA_PALETTE;
    public static final byte[] ITEM_DATA_PALETTE_361;

    private static final Int2IntMap LEGACY_NETWORK_MAP = new Int2IntOpenHashMap();
    private static final Int2IntMap NETWORK_LEGACY_MAP = new Int2IntOpenHashMap();

    static {
        Server.getInstance().getLogger().debug("Loading runtime items...");
        // 361 - 1.12+
        InputStream stream361 = Server.class.getClassLoader().getResourceAsStream("runtime_item_ids_361.json");
        if (stream361 == null) throw new AssertionError("Unable to load runtime_item_ids_361.json");
        Collection<Entry> entries361 = GSON.fromJson(new InputStreamReader(stream361, StandardCharsets.UTF_8), ENTRY_TYPE);
        BinaryStream paletteBuffer361 = new BinaryStream();
        paletteBuffer361.putUnsignedVarInt(entries361.size());
        for (Entry entry : entries361) {
            paletteBuffer361.putString(entry.name);
            paletteBuffer361.putLShort(entry.id);
        }
        ITEM_DATA_PALETTE_361 = paletteBuffer361.getBuffer();
        // 419 - 1.16.100+
        InputStream stream419 = Server.class.getClassLoader().getResourceAsStream("runtime_item_ids_419.json");
        if (stream419 == null) throw new AssertionError("Unable to load runtime_item_ids_419.json");
        Collection<Entry> entries419 = GSON.fromJson(new InputStreamReader(stream419, StandardCharsets.UTF_8), ENTRY_TYPE);
        BinaryStream paletteBuffer419 = new BinaryStream();
        paletteBuffer419.putUnsignedVarInt(entries419.size());
        LEGACY_NETWORK_MAP.defaultReturnValue(-1);
        NETWORK_LEGACY_MAP.defaultReturnValue(-1);
        for (Entry entry : entries419) {
            paletteBuffer419.putString(entry.name);
            paletteBuffer419.putLShort(entry.id);
            paletteBuffer419.putBoolean(false); // Component item
        }
        ITEM_DATA_PALETTE = paletteBuffer419.getBuffer();
    }

    @ToString
    @RequiredArgsConstructor
    static class Entry {
        String name;
        int id;
    }
}
