/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.UpdateAbilitiesPacket;
import cn.nukkit.network.protocol.UpdateAdventureSettingsPacket;
import cn.nukkit.network.protocol.types.AbilityLayer;
import cn.nukkit.network.protocol.types.PlayerAbility;
import java.util.EnumMap;
import java.util.Map;

public class AdventureSettings
implements Cloneable {
    public static final int PERMISSION_NORMAL = 0;
    public static final int PERMISSION_OPERATOR = 1;
    public static final int PERMISSION_HOST = 2;
    public static final int PERMISSION_AUTOMATION = 3;
    public static final int PERMISSION_ADMIN = 4;
    private final Map<Type, Boolean> a = new EnumMap<Type, Boolean>(Type.class);
    private Player b;

    public AdventureSettings(Player player) {
        this.b = player;
    }

    public AdventureSettings clone(Player player) {
        try {
            AdventureSettings adventureSettings = (AdventureSettings)super.clone();
            adventureSettings.b = player;
            return adventureSettings;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public AdventureSettings set(Type type, boolean bl) {
        this.a.put(type, bl);
        return this;
    }

    public boolean get(Type type) {
        Boolean bl = this.a.get((Object)type);
        return bl == null ? type.getDefaultValue() : bl.booleanValue();
    }

    public void update() {
        this.a(true);
    }

    void a(boolean bl) {
        if (this.b.protocol >= 553) {
            UpdateAbilitiesPacket updateAbilitiesPacket = new UpdateAbilitiesPacket();
            updateAbilitiesPacket.setEntityId(this.b.getId());
            updateAbilitiesPacket.setCommandPermission(this.b.isOp() && this.b.showAdmin() ? UpdateAbilitiesPacket.CommandPermission.OPERATOR : UpdateAbilitiesPacket.CommandPermission.NORMAL);
            updateAbilitiesPacket.setPlayerPermission(this.b.isOp() && this.b.showAdmin() && !this.b.isSpectator() ? UpdateAbilitiesPacket.PlayerPermission.OPERATOR : UpdateAbilitiesPacket.PlayerPermission.MEMBER);
            AbilityLayer abilityLayer = new AbilityLayer();
            abilityLayer.setLayerType(AbilityLayer.Type.BASE);
            abilityLayer.getAbilitiesSet().addAll(PlayerAbility.VALUES);
            for (Type type : Type.values()) {
                if (!type.isAbility() || !this.get(type)) continue;
                abilityLayer.getAbilityValues().add(type.getAbility());
            }
            abilityLayer.getAbilityValues().add(PlayerAbility.WALK_SPEED);
            abilityLayer.getAbilityValues().add(PlayerAbility.FLY_SPEED);
            if (this.b.isCreative()) {
                abilityLayer.getAbilityValues().add(PlayerAbility.INSTABUILD);
            }
            if (this.b.isOp()) {
                abilityLayer.getAbilityValues().add(PlayerAbility.OPERATOR_COMMANDS);
            }
            abilityLayer.setWalkSpeed(0.1f);
            abilityLayer.setFlySpeed(0.05f);
            updateAbilitiesPacket.getAbilityLayers().add(abilityLayer);
            UpdateAdventureSettingsPacket updateAdventureSettingsPacket = new UpdateAdventureSettingsPacket();
            updateAdventureSettingsPacket.setAutoJump(this.get(Type.AUTO_JUMP));
            updateAdventureSettingsPacket.setImmutableWorld(this.get(Type.WORLD_IMMUTABLE));
            updateAdventureSettingsPacket.setNoMvP(this.get(Type.NO_MVP));
            updateAdventureSettingsPacket.setNoPvM(this.get(Type.NO_PVM));
            updateAdventureSettingsPacket.setShowNameTags(this.get(Type.SHOW_NAME_TAGS));
            this.b.dataPacket(updateAbilitiesPacket);
            this.b.dataPacket(updateAdventureSettingsPacket);
        } else {
            AdventureSettingsPacket adventureSettingsPacket = new AdventureSettingsPacket();
            for (Type type : Type.values()) {
                if (type.getId() <= 0) continue;
                adventureSettingsPacket.setFlag(type.getId(), this.get(type));
            }
            adventureSettingsPacket.commandPermission = this.b.isOp() && this.b.showAdmin() ? 1L : 0L;
            adventureSettingsPacket.playerPermission = this.b.isOp() && this.b.showAdmin() && !this.b.isSpectator() ? 2L : 1L;
            adventureSettingsPacket.entityUniqueId = this.b.getId();
            this.b.dataPacket(adventureSettingsPacket);
        }
        if (bl) {
            this.b.resetInAirTicks();
        }
    }

    public static enum Type {
        WORLD_IMMUTABLE(1, null, false),
        NO_PVM(2, null, false),
        NO_MVP(4, PlayerAbility.INVULNERABLE, false),
        SHOW_NAME_TAGS(16, null, false),
        AUTO_JUMP(32, null, true),
        ALLOW_FLIGHT(64, PlayerAbility.MAY_FLY, false),
        NO_CLIP(128, PlayerAbility.NO_CLIP, false),
        WORLD_BUILDER(256, PlayerAbility.WORLD_BUILDER, false),
        FLYING(512, PlayerAbility.FLYING, false),
        MUTED(1024, PlayerAbility.MUTED, false),
        MINE(65537, PlayerAbility.MINE, true),
        DOORS_AND_SWITCHED(65538, PlayerAbility.DOORS_AND_SWITCHES, true),
        OPEN_CONTAINERS(65540, PlayerAbility.OPEN_CONTAINERS, true),
        ATTACK_PLAYERS(65544, PlayerAbility.ATTACK_PLAYERS, true),
        ATTACK_MOBS(65552, PlayerAbility.ATTACK_MOBS, true),
        OPERATOR(65568, PlayerAbility.OPERATOR_COMMANDS, false),
        TELEPORT(65664, PlayerAbility.TELEPORT, false),
        BUILD(65792, PlayerAbility.BUILD, true),
        BUILD_AND_MINE(0, null, true),
        DEFAULT_LEVEL_PERMISSIONS(66048, null, false);

        private final int c;
        private final PlayerAbility b;
        private final boolean a;

        private Type(int n2, PlayerAbility playerAbility, boolean bl) {
            this.c = n2;
            this.b = playerAbility;
            this.a = bl;
        }

        public int getId() {
            return this.c;
        }

        public boolean getDefaultValue() {
            return this.a;
        }

        public PlayerAbility getAbility() {
            return this.b;
        }

        public boolean isAbility() {
            return this.b != null;
        }
    }
}

