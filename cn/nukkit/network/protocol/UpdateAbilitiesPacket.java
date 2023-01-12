/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.types.AbilityLayer;
import cn.nukkit.network.protocol.types.PlayerAbility;
import cn.nukkit.utils.BinaryStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public class UpdateAbilitiesPacket
extends DataPacket {
    protected static final PlayerAbility[] VALID_FLAGS = new PlayerAbility[]{PlayerAbility.BUILD, PlayerAbility.MINE, PlayerAbility.DOORS_AND_SWITCHES, PlayerAbility.OPEN_CONTAINERS, PlayerAbility.ATTACK_PLAYERS, PlayerAbility.ATTACK_MOBS, PlayerAbility.OPERATOR_COMMANDS, PlayerAbility.TELEPORT, PlayerAbility.INVULNERABLE, PlayerAbility.FLYING, PlayerAbility.MAY_FLY, PlayerAbility.INSTABUILD, PlayerAbility.LIGHTNING, PlayerAbility.FLY_SPEED, PlayerAbility.WALK_SPEED, PlayerAbility.MUTED, PlayerAbility.WORLD_BUILDER, PlayerAbility.NO_CLIP};
    private static final EnumMap<PlayerAbility, Integer> i = new EnumMap(PlayerAbility.class);
    private long f;
    private PlayerPermission e;
    private CommandPermission g;
    private final List<AbilityLayer> h = new ObjectArrayList<AbilityLayer>();

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putLLong(this.f);
        this.putUnsignedVarInt(this.e.ordinal());
        this.putUnsignedVarInt(this.g.ordinal());
        this.putArray(this.h, this::a);
    }

    private void a(BinaryStream binaryStream, AbilityLayer abilityLayer) {
        binaryStream.putLShort(abilityLayer.getLayerType().ordinal());
        binaryStream.putLInt(UpdateAbilitiesPacket.a(abilityLayer.getAbilitiesSet()));
        binaryStream.putLInt(UpdateAbilitiesPacket.a(abilityLayer.getAbilityValues()));
        binaryStream.putLFloat(abilityLayer.getFlySpeed());
        binaryStream.putLFloat(abilityLayer.getWalkSpeed());
    }

    private static int a(Set<PlayerAbility> set) {
        int n = 0;
        for (PlayerAbility playerAbility : set) {
            n |= i.getOrDefault((Object)playerAbility, 0).intValue();
        }
        return n;
    }

    @Override
    public byte pid() {
        return -69;
    }

    public String toString() {
        return "UpdateAbilitiesPacket(entityId=" + this.getEntityId() + ", playerPermission=" + (Object)((Object)this.getPlayerPermission()) + ", commandPermission=" + (Object)((Object)this.getCommandPermission()) + ", abilityLayers=" + this.getAbilityLayers() + ")";
    }

    public long getEntityId() {
        return this.f;
    }

    public PlayerPermission getPlayerPermission() {
        return this.e;
    }

    public CommandPermission getCommandPermission() {
        return this.g;
    }

    public List<AbilityLayer> getAbilityLayers() {
        return this.h;
    }

    public void setEntityId(long l) {
        this.f = l;
    }

    public void setPlayerPermission(PlayerPermission playerPermission) {
        this.e = playerPermission;
    }

    public void setCommandPermission(CommandPermission commandPermission) {
        this.g = commandPermission;
    }

    static {
        for (int k = 0; k < VALID_FLAGS.length; ++k) {
            i.put(VALID_FLAGS[k], 1 << k);
        }
    }

    public static enum CommandPermission {
        NORMAL,
        OPERATOR,
        HOST,
        AUTOMATION,
        ADMIN;

    }

    public static enum PlayerPermission {
        VISITOR,
        MEMBER,
        OPERATOR,
        CUSTOM;

    }
}

