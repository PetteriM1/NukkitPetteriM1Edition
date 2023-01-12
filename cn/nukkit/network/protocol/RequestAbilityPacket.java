/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.UpdateAbilitiesPacket;
import cn.nukkit.network.protocol.types.PlayerAbility;

public class RequestAbilityPacket
extends DataPacket {
    protected static final PlayerAbility[] ABILITIES = UpdateAbilitiesPacket.VALID_FLAGS;
    protected static final AbilityType[] ABILITY_TYPES = AbilityType.values();
    private PlayerAbility h;
    private AbilityType g;
    private boolean e;
    private float f;

    @Override
    public void decode() {
        this.setAbility(ABILITIES[this.getVarInt()]);
        this.setType(ABILITY_TYPES[this.getByte()]);
        this.setBoolValue(this.getBoolean());
        this.setFloatValue(this.getLFloat());
    }

    @Override
    public void encode() {
        this.a();
    }

    @Override
    public byte pid() {
        return -72;
    }

    public String toString() {
        return "RequestAbilityPacket(ability=" + (Object)((Object)this.getAbility()) + ", type=" + (Object)((Object)this.getType()) + ", boolValue=" + this.isBoolValue() + ", floatValue=" + this.getFloatValue() + ")";
    }

    public PlayerAbility getAbility() {
        return this.h;
    }

    public AbilityType getType() {
        return this.g;
    }

    public boolean isBoolValue() {
        return this.e;
    }

    public float getFloatValue() {
        return this.f;
    }

    public void setAbility(PlayerAbility playerAbility) {
        this.h = playerAbility;
    }

    public void setType(AbilityType abilityType) {
        this.g = abilityType;
    }

    public void setBoolValue(boolean bl) {
        this.e = bl;
    }

    public void setFloatValue(float f2) {
        this.f = f2;
    }

    public static enum AbilityType {
        NONE,
        BOOLEAN,
        FLOAT;

    }
}

