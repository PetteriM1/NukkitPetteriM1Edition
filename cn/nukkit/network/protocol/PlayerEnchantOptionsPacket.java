/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;

public class PlayerEnchantOptionsPacket
extends DataPacket {
    public static final byte NETWORK_ID = -110;
    public final List<EnchantOptionData> options = new ArrayList<EnchantOptionData>();

    @Override
    public byte pid() {
        return -110;
    }

    @Override
    public void decode() {
        int n = (int)this.getUnsignedVarInt();
        if (n > 1000) {
            throw new RuntimeException("EnchantOptions too big: " + n);
        }
        for (int k = 0; k < n; ++k) {
            Object object;
            Object object2;
            int n2 = this.getVarInt();
            int n3 = this.getInt();
            int n4 = (int)this.getUnsignedVarInt();
            if (n4 > 1000) {
                throw new RuntimeException("Enchantment list too big: " + n4);
            }
            ObjectArrayList<EnchantData> objectArrayList = new ObjectArrayList<EnchantData>();
            for (int i2 = 0; i2 < n4; ++i2) {
                object2 = new EnchantData(this.getByte(), this.getByte());
                objectArrayList.add(object2);
            }
            n4 = (int)this.getUnsignedVarInt();
            if (n4 > 1000) {
                throw new RuntimeException("Enchantment list too big: " + n4);
            }
            ObjectArrayList<EnchantData> objectArrayList2 = new ObjectArrayList<EnchantData>();
            for (int i3 = 0; i3 < n4; ++i3) {
                object = new EnchantData(this.getByte(), this.getByte());
                objectArrayList2.add(object);
            }
            n4 = (int)this.getUnsignedVarInt();
            if (n4 > 1000) {
                throw new RuntimeException("Enchantment list too big: " + n4);
            }
            object2 = new ObjectArrayList();
            for (int i4 = 0; i4 < n4; ++i4) {
                EnchantData enchantData = new EnchantData(this.getByte(), this.getByte());
                object2.add(enchantData);
            }
            object = this.getString();
            int n5 = (int)this.getUnsignedVarInt();
            this.options.add(new EnchantOptionData(n2, n3, objectArrayList, objectArrayList2, (List<EnchantData>)object2, (String)object, n5));
        }
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.options.size());
        for (EnchantOptionData enchantOptionData : this.options) {
            this.putVarInt(enchantOptionData.getMinLevel());
            this.putInt(enchantOptionData.getPrimarySlot());
            this.putUnsignedVarInt(enchantOptionData.getEnchants0().size());
            for (EnchantData enchantData : enchantOptionData.getEnchants0()) {
                this.putByte((byte)enchantData.getType());
                this.putByte((byte)enchantData.getLevel());
            }
            this.putUnsignedVarInt(enchantOptionData.getEnchants1().size());
            for (EnchantData enchantData : enchantOptionData.getEnchants1()) {
                this.putByte((byte)enchantData.getType());
                this.putByte((byte)enchantData.getLevel());
            }
            this.putUnsignedVarInt(enchantOptionData.getEnchants2().size());
            for (EnchantData enchantData : enchantOptionData.getEnchants2()) {
                this.putByte((byte)enchantData.getType());
                this.putByte((byte)enchantData.getLevel());
            }
            this.putString(enchantOptionData.getEnchantName());
            this.putUnsignedVarInt(enchantOptionData.getEnchantNetId());
        }
    }

    public String toString() {
        return "PlayerEnchantOptionsPacket(options=" + this.options + ")";
    }

    private static RuntimeException a(RuntimeException runtimeException) {
        return runtimeException;
    }

    public final class EnchantData {
        private final int b;
        private final int a;

        public EnchantData(int n, int n2) {
            this.b = n;
            this.a = n2;
        }

        public int getType() {
            return this.b;
        }

        public int getLevel() {
            return this.a;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof EnchantData)) {
                return false;
            }
            EnchantData enchantData = (EnchantData)object;
            if (this.getType() != enchantData.getType()) {
                return false;
            }
            return this.getLevel() == enchantData.getLevel();
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            n2 = n2 * 59 + this.getType();
            n2 = n2 * 59 + this.getLevel();
            return n2;
        }

        public String toString() {
            return "PlayerEnchantOptionsPacket.EnchantData(type=" + this.getType() + ", level=" + this.getLevel() + ")";
        }
    }

    public final class EnchantOptionData {
        private final int b;
        private final int g;
        private final List<EnchantData> f;
        private final List<EnchantData> c;
        private final List<EnchantData> d;
        private final String e;
        private final int a;

        public EnchantOptionData(int n, int n2, List<EnchantData> list, List<EnchantData> list2, List<EnchantData> list3, String string, int n3) {
            this.b = n;
            this.g = n2;
            this.f = list;
            this.c = list2;
            this.d = list3;
            this.e = string;
            this.a = n3;
        }

        public int getMinLevel() {
            return this.b;
        }

        public int getPrimarySlot() {
            return this.g;
        }

        public List<EnchantData> getEnchants0() {
            return this.f;
        }

        public List<EnchantData> getEnchants1() {
            return this.c;
        }

        public List<EnchantData> getEnchants2() {
            return this.d;
        }

        public String getEnchantName() {
            return this.e;
        }

        public int getEnchantNetId() {
            return this.a;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof EnchantOptionData)) {
                return false;
            }
            EnchantOptionData enchantOptionData = (EnchantOptionData)object;
            if (this.getMinLevel() != enchantOptionData.getMinLevel()) {
                return false;
            }
            if (this.getPrimarySlot() != enchantOptionData.getPrimarySlot()) {
                return false;
            }
            List<EnchantData> list = this.getEnchants0();
            List<EnchantData> list2 = enchantOptionData.getEnchants0();
            if (list == null ? list2 != null : !((Object)list).equals(list2)) {
                return false;
            }
            List<EnchantData> list3 = this.getEnchants1();
            List<EnchantData> list4 = enchantOptionData.getEnchants1();
            if (list3 == null ? list4 != null : !((Object)list3).equals(list4)) {
                return false;
            }
            List<EnchantData> list5 = this.getEnchants2();
            List<EnchantData> list6 = enchantOptionData.getEnchants2();
            if (list5 == null ? list6 != null : !((Object)list5).equals(list6)) {
                return false;
            }
            String string = this.getEnchantName();
            String string2 = enchantOptionData.getEnchantName();
            if (string == null ? string2 != null : !string.equals(string2)) {
                return false;
            }
            return this.getEnchantNetId() == enchantOptionData.getEnchantNetId();
        }

        public int hashCode() {
            int n = 59;
            int n2 = 1;
            n2 = n2 * 59 + this.getMinLevel();
            n2 = n2 * 59 + this.getPrimarySlot();
            List<EnchantData> list = this.getEnchants0();
            n2 = n2 * 59 + (list == null ? 43 : ((Object)list).hashCode());
            List<EnchantData> list2 = this.getEnchants1();
            n2 = n2 * 59 + (list2 == null ? 43 : ((Object)list2).hashCode());
            List<EnchantData> list3 = this.getEnchants2();
            n2 = n2 * 59 + (list3 == null ? 43 : ((Object)list3).hashCode());
            String string = this.getEnchantName();
            n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
            n2 = n2 * 59 + this.getEnchantNetId();
            return n2;
        }

        public String toString() {
            return "PlayerEnchantOptionsPacket.EnchantOptionData(minLevel=" + this.getMinLevel() + ", primarySlot=" + this.getPrimarySlot() + ", enchants0=" + this.getEnchants0() + ", enchants1=" + this.getEnchants1() + ", enchants2=" + this.getEnchants2() + ", enchantName=" + this.getEnchantName() + ", enchantNetId=" + this.getEnchantNetId() + ")";
        }
    }
}

