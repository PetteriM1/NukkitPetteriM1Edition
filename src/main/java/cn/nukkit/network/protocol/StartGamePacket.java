package cn.nukkit.network.protocol;

import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.utils.Utils;
import lombok.ToString;

@ToString
public class StartGamePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.START_GAME_PACKET;

    public static final int GAME_PUBLISH_SETTING_NO_MULTI_PLAY = 0;
    public static final int GAME_PUBLISH_SETTING_INVITE_ONLY = 1;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_ONLY = 2;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_OF_FRIENDS = 3;
    public static final int GAME_PUBLISH_SETTING_PUBLIC = 4;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    public String version;

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
    public int dayCycleStopTime = -1;
    public boolean eduMode = false;
    public int eduEditionOffer = 0;
    public boolean hasEduFeaturesEnabled = false;
    public float rainLevel;
    public float lightningLevel;
    public boolean hasConfirmedPlatformLockedContent = false;
    public boolean multiplayerGame = true;
    public boolean broadcastToLAN = true;
    public boolean broadcastToXboxLive = true;
    public int xblBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
    public int platformBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
    public boolean commandsEnabled;
    public boolean isTexturePacksRequired = false;
    public GameRules gameRules;
    public boolean bonusChest = false;
    public boolean hasStartWithMapEnabled = false;
    public boolean trustPlayers = false;
    public int permissionLevel = 1;
    public int gamePublish = 4;
    public int serverChunkTickRange = 4;
    public boolean broadcastToPlatform;
    public int platformBroadcastMode = 4;
    public boolean xblBroadcastIntentOld = true;
    public boolean hasLockedBehaviorPack = false;
    public boolean hasLockedResourcePack = false;
    public boolean isFromLockedWorldTemplate = false;
    public boolean isUsingMsaGamertagsOnly = false;
    public boolean isFromWorldTemplate = false;
    public boolean isWorldTemplateOptionLocked = false;
    public boolean isOnlySpawningV1Villagers = false;
    public String vanillaVersion = Utils.getVersionByProtocol(protocol);
    public String levelId = "";
    public String worldName;
    public String premiumWorldTemplateId = "";
    public boolean isTrial = false;
    public boolean isMovementServerAuthoritative;
    public long currentTick;
    public int enchantmentSeed;
    public String multiplayerCorrelationId = "";

    @Override
    public void decode() {
    }

    @Override
    public void encode() {
        this.reset();
        this.putEntityUniqueId(this.entityUniqueId);
        this.putEntityRuntimeId(this.entityRuntimeId);
        this.putVarInt(this.playerGamemode);
        this.putVector3f(this.x, this.y, this.z);
        this.putLFloat(this.yaw);
        this.putLFloat(this.pitch);
        this.putVarInt(this.seed);
        if (protocol >= 407) {
            this.putLShort(0x00); // SpawnBiomeType - Default
            this.putString(protocol >= ProtocolInfo.v1_16_100 ? "plains" : ""); // UserDefinedBiomeName
        }
        this.putVarInt(this.dimension);
        this.putVarInt(this.generator);
        this.putVarInt(this.worldGamemode);
        this.putVarInt(this.difficulty);
        this.putBlockVector3(this.spawnX, this.spawnY, this.spawnZ);
        this.putBoolean(this.hasAchievementsDisabled);
        this.putVarInt(this.dayCycleStopTime);
        if (protocol >= 388) {
            this.putVarInt(this.eduEditionOffer);
        } else {
            this.putBoolean(this.eduMode);
        }
        if (protocol > 224) {
            this.putBoolean(this.hasEduFeaturesEnabled);
            if (protocol >= 407) {
                this.putString(""); // Education Edition Product ID
            }
        }
        this.putLFloat(this.rainLevel);
        this.putLFloat(this.lightningLevel);
        if (protocol >= 332) {
            this.putBoolean(this.hasConfirmedPlatformLockedContent);
        }
        this.putBoolean(this.multiplayerGame);
        this.putBoolean(this.broadcastToLAN);
        if (protocol >= 332) {
            this.putVarInt(this.xblBroadcastIntent);
            this.putVarInt(this.platformBroadcastIntent);
        } else {
            this.putBoolean(this.broadcastToXboxLive);
        }
        this.putBoolean(this.commandsEnabled);
        this.putBoolean(this.isTexturePacksRequired);
        this.putGameRules(protocol, gameRules);
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.putLInt(0); // Experiment count
            this.putBoolean(false); // Were experiments previously toggled
        }
        this.putBoolean(this.bonusChest);
        if (protocol > 201) {
            this.putBoolean(this.hasStartWithMapEnabled);
        }
        if (protocol < 332) {
            this.putBoolean(this.trustPlayers);
        }
        this.putVarInt(this.permissionLevel);
        if (protocol < 332) {
            this.putVarInt(this.gamePublish);
        }
        if (protocol >= 201) {
            this.putLInt(this.serverChunkTickRange);
        }
        if (protocol >= 223 && protocol < 332) {
            this.putBoolean(this.broadcastToPlatform);
            this.putVarInt(this.platformBroadcastMode);
            this.putBoolean(this.xblBroadcastIntentOld);
        }
        if (protocol > 224) {
            this.putBoolean(this.hasLockedBehaviorPack);
            this.putBoolean(this.hasLockedResourcePack);
            this.putBoolean(this.isFromLockedWorldTemplate);
        }
        if (protocol >= 291) {
            this.putBoolean(this.isUsingMsaGamertagsOnly);
            if (protocol >= 313) {
                this.putBoolean(this.isFromWorldTemplate);
                this.putBoolean(this.isWorldTemplateOptionLocked);
                if (protocol >= 361) {
                    this.putBoolean(this.isOnlySpawningV1Villagers);
                    if (protocol >= 388) {
                        this.putString(this.vanillaVersion);
                    }
                }
            }
            if (protocol >= 407) {
                this.putLInt(protocol >= ProtocolInfo.v1_16_100 ? 16 : 0); // Limited world width
                this.putLInt(protocol >= ProtocolInfo.v1_16_100 ? 16 : 0); // Limited world height
                this.putBoolean(false); // Nether type
                if (protocol >= ProtocolInfo.v1_17_30) { // EduSharedUriResource
                    this.putString(""); // buttonName
                    this.putString(""); // linkUri
                }
                this.putBoolean(false); // Experimental Gameplay
            }
        }
        this.putString(this.levelId);
        this.putString(this.worldName);
        this.putString(this.premiumWorldTemplateId);
        this.putBoolean(this.isTrial);
        if (protocol >= 388) {
            if (protocol >= ProtocolInfo.v1_16_100) {
                if (protocol >= ProtocolInfo.v1_16_210) {
                    this.putUnsignedVarInt(this.isMovementServerAuthoritative ? 1 : 0); // 2 - rewind
                    this.putVarInt(0); // RewindHistorySize
                    this.putBoolean(false); // isServerAuthoritativeBlockBreaking
                } else {
                    this.putVarInt(this.isMovementServerAuthoritative ? 1 : 0); // 2 - rewind
                }
            } else {
                this.putBoolean(this.isMovementServerAuthoritative);
            }
        }
        this.putLLong(this.currentTick);
        this.putVarInt(this.enchantmentSeed);
        if (protocol > 274) {
            if (protocol >= ProtocolInfo.v1_16_100) {
                this.putUnsignedVarInt(0); // Custom blocks
            } else {
                this.put(GlobalBlockPalette.getCompiledTable(this.protocol));
            }
            if (protocol >= 361) {
                this.put(RuntimeItems.getMapping(protocol).getItemPalette());
            }
            this.putString(this.multiplayerCorrelationId);
            if (protocol == 354 && version != null && version.startsWith("1.11.4")) {
                this.putBoolean(this.isOnlySpawningV1Villagers);
            } else if (protocol >= 407) {
                this.putBoolean(false); // isInventoryServerAuthoritative
                if (protocol >= ProtocolInfo.v1_16_230_50) {
                    this.putString(""); // serverEngine
                    if (protocol >= ProtocolInfo.v1_18_0) {
                        this.putLLong(0L); // BlockRegistryChecksum
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private static class ItemData {
        private String name;
        private int id;
    }
}
