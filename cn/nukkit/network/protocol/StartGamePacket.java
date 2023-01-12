/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.utils.Utils;
import java.io.IOException;
import java.util.UUID;

public class StartGamePacket
extends DataPacket {
    public static final byte NETWORK_ID = 11;
    public static final int GAME_PUBLISH_SETTING_NO_MULTI_PLAY = 0;
    public static final int GAME_PUBLISH_SETTING_INVITE_ONLY = 1;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_ONLY = 2;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_OF_FRIENDS = 3;
    public static final int GAME_PUBLISH_SETTING_PUBLIC = 4;
    public String version;
    public boolean eduMode;
    public boolean broadcastToXboxLive = true;
    public int gamePublish = 4;
    public boolean broadcastToPlatform;
    public int platformBroadcastMode = 4;
    public boolean xblBroadcastIntentOld = true;
    public boolean trustingPlayers;
    public boolean forceNoServerAuthBlockBreaking;
    public long entityUniqueId;
    public long entityRuntimeId;
    public int playerGamemode;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float pitch;
    public int seed;
    public byte dimension;
    public int generator = 1;
    public int worldGamemode;
    public int difficulty;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public boolean hasAchievementsDisabled = true;
    public boolean worldEditor;
    public int dayCycleStopTime = -1;
    public int eduEditionOffer = 0;
    public boolean hasEduFeaturesEnabled;
    public float rainLevel;
    public float lightningLevel;
    public boolean hasConfirmedPlatformLockedContent;
    public boolean multiplayerGame = true;
    public boolean broadcastToLAN = true;
    public int xblBroadcastIntent = 4;
    public int platformBroadcastIntent = 4;
    public boolean commandsEnabled;
    public boolean isTexturePacksRequired;
    public GameRules gameRules;
    public boolean bonusChest;
    public boolean hasStartWithMapEnabled;
    public int permissionLevel = 1;
    public int serverChunkTickRange = 4;
    public boolean hasLockedBehaviorPack;
    public boolean hasLockedResourcePack;
    public boolean isFromLockedWorldTemplate;
    public boolean isUsingMsaGamertagsOnly;
    public boolean isFromWorldTemplate;
    public boolean isWorldTemplateOptionLocked;
    public boolean isOnlySpawningV1Villagers;
    public String vanillaVersion = "1.19.50";
    public String levelId = "";
    public String worldName;
    public String premiumWorldTemplateId = "";
    public boolean isTrial;
    public boolean isMovementServerAuthoritative;
    public boolean isInventoryServerAuthoritative;
    public long currentTick;
    public int enchantmentSeed;
    public String multiplayerCorrelationId = "";
    public boolean isDisablingPersonas;
    public boolean isDisablingCustomSkins;
    public boolean clientSideGenerationEnabled;
    public byte chatRestrictionLevel;
    public boolean disablePlayerInteractions;

    @Override
    public byte pid() {
        return 11;
    }

    @Override
    public void decode() {
        this.b();
    }

    @Override
    public void encode() {
        block36: {
            block38: {
                block37: {
                    this.reset();
                    this.putEntityUniqueId(this.entityUniqueId);
                    this.putEntityRuntimeId(this.entityRuntimeId);
                    this.putVarInt(this.playerGamemode);
                    this.putVector3f(this.x, this.y, this.z);
                    this.putLFloat(this.yaw);
                    this.putLFloat(this.pitch);
                    if (this.protocol >= 503) {
                        this.putLLong(this.seed);
                    } else {
                        this.putVarInt(this.seed);
                    }
                    if (this.protocol >= 407) {
                        this.putLShort(0);
                        this.putString(this.protocol >= 419 ? "plains" : "");
                    }
                    this.putVarInt(this.dimension);
                    this.putVarInt(this.generator);
                    this.putVarInt(this.worldGamemode);
                    this.putVarInt(this.difficulty);
                    this.putBlockVector3(this.spawnX, this.spawnY, this.spawnZ);
                    this.putBoolean(this.hasAchievementsDisabled);
                    if (this.protocol >= 534) {
                        this.putBoolean(this.worldEditor);
                    }
                    this.putVarInt(this.dayCycleStopTime);
                    if (this.protocol >= 388) {
                        this.putVarInt(this.eduEditionOffer);
                    } else {
                        this.putBoolean(this.eduMode);
                    }
                    if (this.protocol > 224) {
                        this.putBoolean(this.hasEduFeaturesEnabled);
                        if (this.protocol >= 407) {
                            this.putString("");
                        }
                    }
                    this.putLFloat(this.rainLevel);
                    this.putLFloat(this.lightningLevel);
                    if (this.protocol >= 332) {
                        this.putBoolean(this.hasConfirmedPlatformLockedContent);
                    }
                    this.putBoolean(this.multiplayerGame);
                    this.putBoolean(this.broadcastToLAN);
                    if (this.protocol >= 332) {
                        this.putVarInt(this.xblBroadcastIntent);
                        this.putVarInt(this.platformBroadcastIntent);
                    } else {
                        this.putBoolean(this.broadcastToXboxLive);
                    }
                    this.putBoolean(this.commandsEnabled);
                    this.putBoolean(this.isTexturePacksRequired);
                    this.putGameRules(this.protocol, this.gameRules);
                    if (this.protocol >= 419) {
                        this.putLInt(0);
                        this.putBoolean(false);
                    }
                    this.putBoolean(this.bonusChest);
                    if (this.protocol > 201) {
                        this.putBoolean(this.hasStartWithMapEnabled);
                    }
                    if (this.protocol < 332) {
                        this.putBoolean(this.trustingPlayers);
                    }
                    this.putVarInt(this.permissionLevel);
                    if (this.protocol < 332) {
                        this.putVarInt(this.gamePublish);
                    }
                    if (this.protocol >= 201) {
                        this.putLInt(this.serverChunkTickRange);
                    }
                    if (this.protocol >= 223 && this.protocol < 332) {
                        this.putBoolean(this.broadcastToPlatform);
                        this.putVarInt(this.platformBroadcastMode);
                        this.putBoolean(this.xblBroadcastIntentOld);
                    }
                    if (this.protocol > 224) {
                        this.putBoolean(this.hasLockedBehaviorPack);
                        this.putBoolean(this.hasLockedResourcePack);
                        this.putBoolean(this.isFromLockedWorldTemplate);
                    }
                    if (this.protocol >= 291) {
                        this.putBoolean(this.isUsingMsaGamertagsOnly);
                        if (this.protocol >= 313) {
                            this.putBoolean(this.isFromWorldTemplate);
                            this.putBoolean(this.isWorldTemplateOptionLocked);
                            if (this.protocol >= 361) {
                                this.putBoolean(this.isOnlySpawningV1Villagers);
                                if (this.protocol >= 388) {
                                    if (this.protocol >= 544) {
                                        this.putBoolean(this.isDisablingPersonas);
                                        this.putBoolean(this.isDisablingCustomSkins);
                                    }
                                    this.putString(Utils.getVersionByProtocol(this.protocol));
                                }
                            }
                        }
                        if (this.protocol >= 407) {
                            this.putLInt(this.protocol >= 419 ? 16 : 0);
                            this.putLInt(this.protocol >= 419 ? 16 : 0);
                            this.putBoolean(false);
                            if (this.protocol >= 465) {
                                this.putString("");
                                this.putString("");
                            }
                            this.putBoolean(false);
                            if (this.protocol >= 544) {
                                this.putByte(this.chatRestrictionLevel);
                                this.putBoolean(this.disablePlayerInteractions);
                            }
                        }
                    }
                    this.putString(this.levelId);
                    this.putString(this.worldName);
                    this.putString(this.premiumWorldTemplateId);
                    this.putBoolean(this.isTrial);
                    if (this.protocol >= 388) {
                        if (this.protocol >= 419) {
                            if (this.protocol >= 428) {
                                this.putVarInt(this.isMovementServerAuthoritative ? 1 : 0);
                                this.putVarInt(0);
                                this.putBoolean(!this.forceNoServerAuthBlockBreaking && this.protocol >= 440);
                            } else {
                                this.putVarInt(this.isMovementServerAuthoritative ? 1 : 0);
                            }
                        } else {
                            this.putBoolean(this.isMovementServerAuthoritative);
                        }
                    }
                    this.putLLong(this.currentTick);
                    this.putVarInt(this.enchantmentSeed);
                    if (this.protocol <= 274) break block36;
                    if (this.protocol >= 419) {
                        this.putUnsignedVarInt(0L);
                    } else {
                        this.put(GlobalBlockPalette.getCompiledTable(this.protocol));
                    }
                    if (this.protocol >= 361) {
                        this.put(RuntimeItems.getMapping(this.protocol).getItemPalette());
                    }
                    this.putString(this.multiplayerCorrelationId);
                    if (this.protocol != 354 || this.version == null || !this.version.startsWith("1.11.4")) break block37;
                    this.putBoolean(this.isOnlySpawningV1Villagers);
                    break block36;
                }
                if (this.protocol < 407) break block36;
                this.putBoolean(false);
                if (this.protocol < 433) break block36;
                this.putString("");
                if (this.protocol < 474) break block36;
                if (this.protocol >= 524) break block38;
                this.putLLong(0L);
                break block36;
            }
            try {
                this.put(NBTIO.writeNetwork(new CompoundTag("")));
            }
            catch (IOException iOException) {
                throw new RuntimeException(iOException);
            }
            this.putLLong(0L);
            this.putUUID(new UUID(0L, 0L));
            if (this.protocol < 544) break block36;
            this.putBoolean(this.clientSideGenerationEnabled);
        }
    }

    public String toString() {
        return "StartGamePacket(version=" + this.version + ", eduMode=" + this.eduMode + ", broadcastToXboxLive=" + this.broadcastToXboxLive + ", gamePublish=" + this.gamePublish + ", broadcastToPlatform=" + this.broadcastToPlatform + ", platformBroadcastMode=" + this.platformBroadcastMode + ", xblBroadcastIntentOld=" + this.xblBroadcastIntentOld + ", trustingPlayers=" + this.trustingPlayers + ", forceNoServerAuthBlockBreaking=" + this.forceNoServerAuthBlockBreaking + ", entityUniqueId=" + this.entityUniqueId + ", entityRuntimeId=" + this.entityRuntimeId + ", playerGamemode=" + this.playerGamemode + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + ", yaw=" + this.yaw + ", pitch=" + this.pitch + ", seed=" + this.seed + ", dimension=" + this.dimension + ", generator=" + this.generator + ", worldGamemode=" + this.worldGamemode + ", difficulty=" + this.difficulty + ", spawnX=" + this.spawnX + ", spawnY=" + this.spawnY + ", spawnZ=" + this.spawnZ + ", hasAchievementsDisabled=" + this.hasAchievementsDisabled + ", worldEditor=" + this.worldEditor + ", dayCycleStopTime=" + this.dayCycleStopTime + ", eduEditionOffer=" + this.eduEditionOffer + ", hasEduFeaturesEnabled=" + this.hasEduFeaturesEnabled + ", rainLevel=" + this.rainLevel + ", lightningLevel=" + this.lightningLevel + ", hasConfirmedPlatformLockedContent=" + this.hasConfirmedPlatformLockedContent + ", multiplayerGame=" + this.multiplayerGame + ", broadcastToLAN=" + this.broadcastToLAN + ", xblBroadcastIntent=" + this.xblBroadcastIntent + ", platformBroadcastIntent=" + this.platformBroadcastIntent + ", commandsEnabled=" + this.commandsEnabled + ", isTexturePacksRequired=" + this.isTexturePacksRequired + ", gameRules=" + this.gameRules + ", bonusChest=" + this.bonusChest + ", hasStartWithMapEnabled=" + this.hasStartWithMapEnabled + ", permissionLevel=" + this.permissionLevel + ", serverChunkTickRange=" + this.serverChunkTickRange + ", hasLockedBehaviorPack=" + this.hasLockedBehaviorPack + ", hasLockedResourcePack=" + this.hasLockedResourcePack + ", isFromLockedWorldTemplate=" + this.isFromLockedWorldTemplate + ", isUsingMsaGamertagsOnly=" + this.isUsingMsaGamertagsOnly + ", isFromWorldTemplate=" + this.isFromWorldTemplate + ", isWorldTemplateOptionLocked=" + this.isWorldTemplateOptionLocked + ", isOnlySpawningV1Villagers=" + this.isOnlySpawningV1Villagers + ", vanillaVersion=" + this.vanillaVersion + ", levelId=" + this.levelId + ", worldName=" + this.worldName + ", premiumWorldTemplateId=" + this.premiumWorldTemplateId + ", isTrial=" + this.isTrial + ", isMovementServerAuthoritative=" + this.isMovementServerAuthoritative + ", isInventoryServerAuthoritative=" + this.isInventoryServerAuthoritative + ", currentTick=" + this.currentTick + ", enchantmentSeed=" + this.enchantmentSeed + ", multiplayerCorrelationId=" + this.multiplayerCorrelationId + ", isDisablingPersonas=" + this.isDisablingPersonas + ", isDisablingCustomSkins=" + this.isDisablingCustomSkins + ", clientSideGenerationEnabled=" + this.clientSideGenerationEnabled + ", chatRestrictionLevel=" + this.chatRestrictionLevel + ", disablePlayerInteractions=" + this.disablePlayerInteractions + ")";
    }

    private static IOException a(IOException iOException) {
        return iOException;
    }
}

