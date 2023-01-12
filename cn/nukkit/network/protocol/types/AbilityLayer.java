/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol.types;

import cn.nukkit.network.protocol.types.PlayerAbility;
import java.util.EnumSet;
import java.util.Set;

public class AbilityLayer {
    private Type d;
    private final Set<PlayerAbility> e = EnumSet.noneOf(PlayerAbility.class);
    private final Set<PlayerAbility> a = EnumSet.noneOf(PlayerAbility.class);
    private float c;
    private float b;

    public Type getLayerType() {
        return this.d;
    }

    public Set<PlayerAbility> getAbilitiesSet() {
        return this.e;
    }

    public Set<PlayerAbility> getAbilityValues() {
        return this.a;
    }

    public float getFlySpeed() {
        return this.c;
    }

    public float getWalkSpeed() {
        return this.b;
    }

    public void setLayerType(Type type) {
        this.d = type;
    }

    public void setFlySpeed(float f2) {
        this.c = f2;
    }

    public void setWalkSpeed(float f2) {
        this.b = f2;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof AbilityLayer)) {
            return false;
        }
        AbilityLayer abilityLayer = (AbilityLayer)object;
        if (!abilityLayer.canEqual(this)) {
            return false;
        }
        Type type = this.getLayerType();
        Type type2 = abilityLayer.getLayerType();
        if (type == null ? type2 != null : !((Object)((Object)type)).equals((Object)type2)) {
            return false;
        }
        Set<PlayerAbility> set = this.getAbilitiesSet();
        Set<PlayerAbility> set2 = abilityLayer.getAbilitiesSet();
        if (set == null ? set2 != null : !((Object)set).equals(set2)) {
            return false;
        }
        Set<PlayerAbility> set3 = this.getAbilityValues();
        Set<PlayerAbility> set4 = abilityLayer.getAbilityValues();
        if (set3 == null ? set4 != null : !((Object)set3).equals(set4)) {
            return false;
        }
        if (Float.compare(this.getFlySpeed(), abilityLayer.getFlySpeed()) != 0) {
            return false;
        }
        return Float.compare(this.getWalkSpeed(), abilityLayer.getWalkSpeed()) == 0;
    }

    protected boolean canEqual(Object object) {
        return object instanceof AbilityLayer;
    }

    public int hashCode() {
        int n = 59;
        int n2 = 1;
        Type type = this.getLayerType();
        n2 = n2 * 59 + (type == null ? 43 : ((Object)((Object)type)).hashCode());
        Set<PlayerAbility> set = this.getAbilitiesSet();
        n2 = n2 * 59 + (set == null ? 43 : ((Object)set).hashCode());
        Set<PlayerAbility> set2 = this.getAbilityValues();
        n2 = n2 * 59 + (set2 == null ? 43 : ((Object)set2).hashCode());
        n2 = n2 * 59 + Float.floatToIntBits(this.getFlySpeed());
        n2 = n2 * 59 + Float.floatToIntBits(this.getWalkSpeed());
        return n2;
    }

    public String toString() {
        return "AbilityLayer(layerType=" + (Object)((Object)this.getLayerType()) + ", abilitiesSet=" + this.getAbilitiesSet() + ", abilityValues=" + this.getAbilityValues() + ", flySpeed=" + this.getFlySpeed() + ", walkSpeed=" + this.getWalkSpeed() + ")";
    }

    public static enum Type {
        CACHE,
        BASE,
        SPECTATOR,
        COMMANDS,
        EDITOR;

    }
}

