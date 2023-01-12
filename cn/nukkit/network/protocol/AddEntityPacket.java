/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.custom.EntityDefinition;
import cn.nukkit.entity.custom.EntityManager;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.types.EntityLink;
import cn.nukkit.utils.Binary;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;

public class AddEntityPacket
extends DataPacket {
    public static final byte NETWORK_ID = 13;
    public static final ImmutableMap<Integer, String> LEGACY_IDS = ImmutableMap.builder().put(51, "minecraft:npc").put(63, "minecraft:player").put(48, "minecraft:wither_skeleton").put(47, "minecraft:husk").put(46, "minecraft:stray").put(45, "minecraft:witch").put(44, "minecraft:zombie_villager").put(43, "minecraft:blaze").put(42, "minecraft:magma_cube").put(41, "minecraft:ghast").put(40, "minecraft:cave_spider").put(39, "minecraft:silverfish").put(38, "minecraft:enderman").put(37, "minecraft:slime").put(36, "minecraft:zombie_pigman").put(35, "minecraft:spider").put(34, "minecraft:skeleton").put(33, "minecraft:creeper").put(32, "minecraft:zombie").put(26, "minecraft:skeleton_horse").put(25, "minecraft:mule").put(24, "minecraft:donkey").put(31, "minecraft:dolphin").put(111, "minecraft:tropicalfish").put(14, "minecraft:wolf").put(17, "minecraft:squid").put(110, "minecraft:drowned").put(13, "minecraft:sheep").put(16, "minecraft:mooshroom").put(113, "minecraft:panda").put(109, "minecraft:salmon").put(12, "minecraft:pig").put(15, "minecraft:villager").put(112, "minecraft:cod").put(108, "minecraft:pufferfish").put(11, "minecraft:cow").put(10, "minecraft:chicken").put(107, "minecraft:balloon").put(29, "minecraft:llama").put(20, "minecraft:iron_golem").put(18, "minecraft:rabbit").put(21, "minecraft:snow_golem").put(19, "minecraft:bat").put(22, "minecraft:ocelot").put(23, "minecraft:horse").put(75, "minecraft:cat").put(28, "minecraft:polar_bear").put(27, "minecraft:zombie_horse").put(74, "minecraft:turtle").put(30, "minecraft:parrot").put(49, "minecraft:guardian").put(50, "minecraft:elder_guardian").put(57, "minecraft:vindicator").put(52, "minecraft:wither").put(53, "minecraft:ender_dragon").put(54, "minecraft:shulker").put(55, "minecraft:endermite").put(84, "minecraft:minecart").put(96, "minecraft:hopper_minecart").put(97, "minecraft:tnt_minecart").put(98, "minecraft:chest_minecart").put(100, "minecraft:command_block_minecart").put(61, "minecraft:armor_stand").put(64, "minecraft:item").put(65, "minecraft:tnt").put(66, "minecraft:falling_block").put(68, "minecraft:xp_bottle").put(69, "minecraft:xp_orb").put(70, "minecraft:eye_of_ender_signal").put(71, "minecraft:ender_crystal").put(76, "minecraft:shulker_bullet").put(77, "minecraft:fishing_hook").put(79, "minecraft:dragon_fireball").put(80, "minecraft:arrow").put(81, "minecraft:snowball").put(82, "minecraft:egg").put(83, "minecraft:painting").put(73, "minecraft:thrown_trident").put(85, "minecraft:fireball").put(86, "minecraft:splash_potion").put(87, "minecraft:ender_pearl").put(88, "minecraft:leash_knot").put(89, "minecraft:wither_skull").put(91, "minecraft:wither_skull_dangerous").put(90, "minecraft:boat").put(93, "minecraft:lightning_bolt").put(94, "minecraft:small_fireball").put(102, "minecraft:llama_spit").put(95, "minecraft:area_effect_cloud").put(101, "minecraft:lingering_potion").put(72, "minecraft:fireworks_rocket").put(103, "minecraft:evocation_fang").put(104, "minecraft:evocation_illager").put(105, "minecraft:vex").put(56, "minecraft:agent").put(106, "minecraft:ice_bomb").put(58, "minecraft:phantom").put(62, "minecraft:tripod_camera").put(114, "minecraft:pillager").put(118, "minecraft:wandering_trader").put(59, "minecraft:ravager").put(115, "minecraft:villager_v2").put(116, "minecraft:zombie_villager_v2").put(121, "minecraft:fox").put(122, "minecraft:bee").put(123, "minecraft:piglin").put(124, "minecraft:hoglin").put(125, "minecraft:strider").put(126, "minecraft:zoglin").put(127, "minecraft:piglin_brute").put(128, "minecraft:goat").put(129, "minecraft:glow_squid").put(130, "minecraft:axolotl").put(131, "minecraft:warden").put(132, "minecraft:frog").put(133, "minecraft:tadpole").put(134, "minecraft:allay").put(218, "minecraft:chest_boat").build();
    private static final ImmutableMap<Integer, String> e = ImmutableMap.builder().put(123, "minecraft:zombie_pigman").put(124, "minecraft:pig").put(125, "minecraft:pig").put(126, "minecraft:pig").put(127, "minecraft:zombie_pigman").build();
    public long entityUniqueId;
    public long entityRuntimeId;
    public int type;
    public String id;
    public float x;
    public float y;
    public float z;
    public float speedX = 0.0f;
    public float speedY = 0.0f;
    public float speedZ = 0.0f;
    public float yaw;
    public float pitch;
    public float headYaw;
    public float bodyYaw = -1.0f;
    public EntityMetadata metadata = new EntityMetadata();
    public Attribute[] attributes = new Attribute[0];
    public EntityLink[] links = new EntityLink[0];

    @Override
    public byte pid() {
        return 13;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        if (this.protocol < 313) {
            this.putUnsignedVarInt(this.type);
        } else {
            this.putString(this.c());
        }
        this.putVector3f(this.x, this.y, this.z);
        this.putVector3f(this.speedX, this.speedY, this.speedZ);
        this.putLFloat(this.pitch);
        this.putLFloat(this.yaw);
        if (this.protocol >= 274) {
            this.putLFloat(this.headYaw);
            if (this.protocol >= 534) {
                this.putLFloat(this.bodyYaw == -1.0f ? this.yaw : this.bodyYaw);
            }
        }
        this.putAttributeList(this.attributes);
        this.put(Binary.writeMetadata(this.protocol, this.metadata));
        if (this.protocol >= 557) {
            this.putUnsignedVarInt(0L);
            this.putUnsignedVarInt(0L);
        }
        this.putUnsignedVarInt(this.links.length);
        for (EntityLink entityLink : this.links) {
            this.putEntityLink(this.protocol, entityLink);
        }
    }

    private String c() {
        EntityDefinition entityDefinition;
        String string;
        if (this.id != null) {
            return this.id;
        }
        if (this.type == 218 && this.protocol < 524) {
            return "minecraft:boat";
        }
        if (this.type == 134 && this.protocol < 524) {
            return "minecraft:bat";
        }
        if (this.type == 131 && this.protocol < 524) {
            return "minecraft:iron_golem";
        }
        if (this.type == 133 && this.protocol < 524) {
            return "minecraft:salmon";
        }
        if (this.type == 132 && this.protocol < 524) {
            return "minecraft:rabbit";
        }
        if (this.type == 128 && this.protocol < 440) {
            return "minecraft:sheep";
        }
        if (this.type == 130 && this.protocol < 440) {
            return "minecraft:tropicalfish";
        }
        if (this.type == 129 && this.protocol < 440) {
            return "minecraft:squid";
        }
        if (this.protocol < 407) {
            if (this.type == 122 && this.protocol < 389) {
                return "minecraft:bat";
            }
            if (this.type == 121 && this.protocol < 388) {
                return "minecraft:wolf";
            }
            string = e.get(this.type);
            if (string != null) {
                return string;
            }
        }
        if ((string = LEGACY_IDS.get(this.type)) == null && (entityDefinition = EntityManager.get().getDefinition(this.type)) != null) {
            string = entityDefinition.getIdentifier();
        }
        if (string == null) {
            throw new IllegalStateException("Unknown entity with network id " + this.type);
        }
        return string;
    }

    public String toString() {
        return "AddEntityPacket(entityUniqueId=" + this.entityUniqueId + ", entityRuntimeId=" + this.entityRuntimeId + ", type=" + this.type + ", id=" + this.id + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", speedX=" + this.speedX + ", speedY=" + this.speedY + ", speedZ=" + this.speedZ + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ", headYaw=" + this.headYaw + ", bodyYaw=" + this.bodyYaw + ", metadata=" + this.metadata + ", attributes=" + Arrays.deepToString(this.attributes) + ", links=" + Arrays.deepToString(this.links) + ")";
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

