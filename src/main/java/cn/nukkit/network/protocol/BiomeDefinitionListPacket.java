package cn.nukkit.network.protocol;

import cn.nukkit.Nukkit;
import cn.nukkit.utils.Utils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.io.ByteStreams;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lombok.ToString;
import lombok.Value;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.zip.Deflater;

@ToString(exclude = "tag")
public class BiomeDefinitionListPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BIOME_DEFINITION_LIST_PACKET;

    private static final BatchPacket CACHED_PACKET_843;
    private static final BatchPacket CACHED_PACKET_827;
    private static final BatchPacket CACHED_PACKET_800;
    private static BatchPacket CACHED_PACKET_554; // 554 data and supports Snappy compression
    private static BatchPacket CACHED_PACKET_486;
    private static BatchPacket CACHED_PACKET_407; // 361 data but Zlib raw compressed
    private static BatchPacket CACHED_PACKET_361;

    private byte[] tag;
    private LinkedHashMap<String, BiomeDefinitionData> biomeDefinitions;

    static {
        // Preload definitions for all recent versions for now
        Gson gson = new GsonBuilder().registerTypeAdapter(Color.class, new ColorTypeAdapter()).create();
        Type type = new TypeToken<LinkedHashMap<String, BiomeDefinitionData>>() {
        }.getType();

        LinkedHashMap<String, BiomeDefinitionData> definitions800 = gson.fromJson(Utils.loadJsonResource("stripped_biome_definitions.json"), type);

        BiomeDefinitionListPacket pk800 = new BiomeDefinitionListPacket();
        pk800.protocol = ProtocolInfo.v1_21_80;
        pk800.biomeDefinitions = definitions800;
        pk800.tryEncode();
        CACHED_PACKET_800 = pk800.compress(Deflater.BEST_COMPRESSION);

        BiomeDefinitionListPacket pk827 = new BiomeDefinitionListPacket();
        pk827.protocol = ProtocolInfo.v1_21_100;
        pk827.biomeDefinitions = definitions800;
        pk827.tryEncode();
        CACHED_PACKET_827 = pk827.compress(Deflater.BEST_COMPRESSION);

        BiomeDefinitionListPacket pk843 = new BiomeDefinitionListPacket();
        pk843.protocol = ProtocolInfo.v1_21_110;
        pk843.biomeDefinitions = definitions800;
        pk843.tryEncode();
        CACHED_PACKET_843 = pk843.compress(Deflater.BEST_COMPRESSION);
    }

    @Value
    private static class BiomeDefinitionData {

        public float temperature;
        public float downfall;
        public float redSporeDensity;
        public float blueSporeDensity;
        public float ashDensity;
        public float whiteAshDensity;
        public float depth;
        public float scale;
        public Color mapWaterColor;
        public boolean rain;

        @JsonCreator
        public BiomeDefinitionData(float temperature, float downfall, float redSporeDensity,
                                   float blueSporeDensity, float ashDensity, float whiteAshDensity, float depth,
                                   float scale, Color mapWaterColor, boolean rain) {
            this.temperature = temperature;
            this.downfall = downfall;
            this.redSporeDensity = redSporeDensity;
            this.blueSporeDensity = blueSporeDensity;
            this.ashDensity = ashDensity;
            this.whiteAshDensity = whiteAshDensity;
            this.depth = depth;
            this.scale = scale;
            this.mapWaterColor = mapWaterColor;
            this.rain = rain;
        }
    }

    @SuppressWarnings({"NullableProblems", "SuspiciousMethodCalls"})
    private static class SequencedHashSet<E> implements List<E> {

        private final Object2IntMap<E> map = new Object2IntLinkedOpenHashMap<>();
        private final Int2ObjectMap<E> inverse = new Int2ObjectLinkedOpenHashMap<>();
        private int index = 0;

        @Override
        public boolean add(E e) {
            if (!this.map.containsKey(e)) {
                int index = this.index++;
                this.map.put(e, index);
                this.inverse.put(index, e);
                return true;
            }
            return false;
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            for (E e : c) {
                this.add(e);
            }
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        public int addAndGetIndex(E e) {
            if (!this.map.containsKey(e)) {
                int index = this.index++;
                this.map.put(e, index);
                this.inverse.put(index, e);
                return index;
            }
            return this.map.getInt(e);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean contains(Object o) {
            return map.containsKey(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return map.keySet().containsAll(c);
        }

        public E get(int index) {
            return this.inverse.get(index);
        }

        @Override
        public int indexOf(Object o) {
            return map.getInt(o);
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public Iterator<E> iterator() {
            return map.keySet().iterator();
        }

        @Override
        public int lastIndexOf(Object o) {
            return map.getInt(o);
        }

        @Override
        public ListIterator<E> listIterator() {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object[] toArray() {
            return map.keySet().toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return map.keySet().toArray(a);
        }

        @Override
        public String toString() {
            return map.keySet().toString();
        }
    }

    private static class ColorTypeAdapter extends TypeAdapter<Color> {

        @Override
        public Color read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }

            int r = 0, g = 0, b = 0, a = 255;
            in.beginObject();
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "r":
                        r = in.nextInt();
                        break;
                    case "g":
                        g = in.nextInt();
                        break;
                    case "b":
                        b = in.nextInt();
                        break;
                    case "a":
                        a = in.nextInt();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return new Color(r, g, b, a);
        }

        @Override
        public void write(JsonWriter out, Color color) {
        }
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        if (protocol < ProtocolInfo.v1_21_80 && this.tag == null) {
            throw new RuntimeException("tag == null, use getCachedPacket!");
        }

        if (protocol >= ProtocolInfo.v1_21_80 && this.biomeDefinitions == null) {
            throw new RuntimeException("biomeDefinitions == null, use getCachedPacket!");
        }

        this.reset();

        if (protocol >= ProtocolInfo.v1_21_80) {
            SequencedHashSet<String> strings = new SequencedHashSet<>();

            this.putUnsignedVarInt(this.biomeDefinitions.size());
            for (Map.Entry<String, BiomeDefinitionData> entry : this.biomeDefinitions.entrySet()) {
                String name = entry.getKey();

                // Vanilla biomes must have "minecraft" prefix since 1.21.100
                if (protocol >= ProtocolInfo.v1_21_100) {
                    name = "minecraft:" + name;
                }

                this.putLShort(strings.addAndGetIndex(name));

                BiomeDefinitionData definition = entry.getValue();
                if (protocol >= ProtocolInfo.v1_21_100) {
                    this.putLShort(-1); // Vanilla biomes don't contain ID field
                } else {
                    this.putBoolean(false); // Optional ID
                }

                this.putLFloat(definition.getTemperature());
                this.putLFloat(definition.getDownfall());
                if (protocol >= ProtocolInfo.v1_21_110) {
                    this.putLFloat(0); // mFoliageSnow - 0 for old behavior
                } else {
                    this.putLFloat(definition.getRedSporeDensity());
                    this.putLFloat(definition.getBlueSporeDensity());
                    this.putLFloat(definition.getAshDensity());
                    this.putLFloat(definition.getWhiteAshDensity());
                }
                this.putLFloat(definition.getDepth());
                this.putLFloat(definition.getScale());
                this.putLInt(definition.getMapWaterColor().getRGB());
                this.putBoolean(definition.isRain());
                this.putBoolean(false); // Optional Tags
                this.putBoolean(false); // Optional ChunkGenData
            }

            this.putUnsignedVarInt(strings.size());
            for (String str : strings) {
                this.putString(str);
            }
        } else {
            this.put(this.tag);
        }
    }

    public static BatchPacket getCachedPacket(int protocol) {
        if (protocol < ProtocolInfo.v1_12_0) {
            throw new UnsupportedOperationException("Unsupported protocol");
        }

        if (protocol >= ProtocolInfo.v1_21_110) {
            return CACHED_PACKET_843;
        } else if (protocol >= ProtocolInfo.v1_21_100) {
            return CACHED_PACKET_827;
        } else if (protocol >= ProtocolInfo.v1_21_80) {
            return CACHED_PACKET_800;
        } else if (protocol >= ProtocolInfo.v1_19_30_23) {
            if (CACHED_PACKET_554 == null) {
                BiomeDefinitionListPacket pk554 = new BiomeDefinitionListPacket();
                pk554.protocol = ProtocolInfo.v1_19_30_23;
                try {
                    pk554.tag = ByteStreams.toByteArray(Objects.requireNonNull(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_554.dat")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                pk554.tryEncode();
                CACHED_PACKET_554 = pk554.compress(Deflater.BEST_COMPRESSION);
            }
            return CACHED_PACKET_554;
        } else if (protocol >= ProtocolInfo.v1_18_10) {
            if (CACHED_PACKET_486 == null) {
                BiomeDefinitionListPacket pk486 = new BiomeDefinitionListPacket();
                pk486.protocol = ProtocolInfo.v1_18_10;
                try {
                    pk486.tag = ByteStreams.toByteArray(Objects.requireNonNull(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_486.dat")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                pk486.tryEncode();
                CACHED_PACKET_486 = pk486.compress(Deflater.BEST_COMPRESSION);
            }
            return CACHED_PACKET_486;
        } else if (protocol >= ProtocolInfo.v1_16_0) {
            if (CACHED_PACKET_407 == null) {
                BiomeDefinitionListPacket pk407 = new BiomeDefinitionListPacket();
                pk407.protocol = ProtocolInfo.v1_16_0;
                try {
                    pk407.tag = ByteStreams.toByteArray(Objects.requireNonNull(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_361.dat")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                pk407.tryEncode();
                CACHED_PACKET_407 = pk407.compress(Deflater.BEST_COMPRESSION);
            }
            return CACHED_PACKET_407;
        } else {
            if (CACHED_PACKET_361 == null) {
                BiomeDefinitionListPacket pk361 = new BiomeDefinitionListPacket();
                pk361.protocol = ProtocolInfo.v1_12_0;
                try {
                    pk361.tag = ByteStreams.toByteArray(Objects.requireNonNull(Nukkit.class.getClassLoader().getResourceAsStream("biome_definitions_361.dat")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                pk361.tryEncode();
                CACHED_PACKET_361 = pk361.compress(Deflater.BEST_COMPRESSION);
            }
            return CACHED_PACKET_361;
        }
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
