package cn.nukkit.network.protocol;

import cn.nukkit.block.custom.CustomBlockDefinition;
import cn.nukkit.block.custom.CustomBlockManager;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GlobalBlockPalette;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.types.ExperimentData;
import cn.nukkit.utils.Binary;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.ToString;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@ToString
public class StartGamePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.START_GAME_PACKET;

    public static final int GAME_PUBLISH_SETTING_NO_MULTI_PLAY = 0;
    public static final int GAME_PUBLISH_SETTING_INVITE_ONLY = 1;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_ONLY = 2;
    public static final int GAME_PUBLISH_SETTING_FRIENDS_OF_FRIENDS = 3;
    public static final int GAME_PUBLISH_SETTING_PUBLIC = 4;

    private static final byte[] EMPTY_COMPOUND_TAG;
    private static final byte[] EMPTY_UUID;

    static {
        try {
            EMPTY_COMPOUND_TAG = NBTIO.writeNetwork(new CompoundTag(""));
            EMPTY_UUID = Binary.writeUUID(new UUID(0, 0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // PM1E only
    public boolean forceNoServerAuthBlockBreaking;
    public Collection<CustomBlockDefinition> blockDefinitions = CustomBlockManager.get().getBlockDefinitions();
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
    public int editorWorldType;
    public int dayCycleStopTime = -1; // -1 = not stopped, any positive value = stopped
    public int eduEditionOffer = 0;
    public boolean hasEduFeaturesEnabled;
    public float rainLevel;
    public float lightningLevel;
    public boolean hasConfirmedPlatformLockedContent;
    public boolean multiplayerGame = true;
    public boolean broadcastToLAN = true;
    public int xblBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
    public int platformBroadcastIntent = GAME_PUBLISH_SETTING_PUBLIC;
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
    public String vanillaVersion = "*";
    public String levelId = ""; // base64 string, usually the same as world folder name in vanilla
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
    public boolean emoteChatMuted;
    public boolean hardcore;
    public final List<ExperimentData> experiments = new ObjectArrayList<>(1);

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
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
        /* Level settings start */
        if (protocol >= ProtocolInfo.v1_18_30) {
            this.putLLong(this.seed);
        } else {
            this.putVarInt(this.seed);
        }
        /* Spawn settings start */
        if (protocol >= 407) {
            this.putLShort(0x00); // SpawnBiomeType - Default
            this.putString(protocol >= ProtocolInfo.v1_16_100 ? "plains" : ""); // UserDefinedBiomeName
        }
        this.putVarInt(this.dimension);
        /* Spawn settings end */
        this.putVarInt(this.generator);
        this.putVarInt(this.worldGamemode);
        if (protocol >= ProtocolInfo.v1_20_80) {
            this.putBoolean(this.hardcore);
        }
        this.putVarInt(this.difficulty);
        this.putBlockVector3(protocol, this.spawnX, this.spawnY, this.spawnZ);
        this.putBoolean(this.hasAchievementsDisabled);
        if (protocol >= ProtocolInfo.v1_19_10) {
            if (protocol >= ProtocolInfo.v1_21_100) { // This actually changed earlier but doesn't matter here
                this.putVarInt(this.editorWorldType);
            } else {
                this.putBoolean(this.editorWorldType != 0);
            }
            if (protocol >= ProtocolInfo.v1_19_80) {
                this.putBoolean(false); // isCreatedInEditor
                this.putBoolean(false); // isExportedFromEditor
            }
        }
        this.putVarInt(this.dayCycleStopTime);
        if (protocol >= 388) {
            if (protocol >= ProtocolInfo.v1_26_40) {
                this.putUnsignedVarInt(this.eduEditionOffer);
            } else {
                this.putVarInt(this.eduEditionOffer);
            }
        } else {
            this.putBoolean(false); // eduMode
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
            this.putBoolean(true); // broadcastToXboxLive
        }
        this.putBoolean(this.commandsEnabled);
        this.putBoolean(this.isTexturePacksRequired);
        this.putGameRules(protocol, gameRules, protocol < ProtocolInfo.v1_26_40);
        if (protocol >= ProtocolInfo.v1_16_100) {
            this.putExperiments(this.experiments);
        }
        this.putBoolean(this.bonusChest);
        if (protocol > 201) {
            this.putBoolean(this.hasStartWithMapEnabled);
        }
        if (protocol < 332) {
            this.putBoolean(false); // trustingPlayers
        }
        if (protocol >= ProtocolInfo.v1_26_40) {
            this.putByte((byte) this.permissionLevel);
        } else {
            this.putVarInt(this.permissionLevel);
        }
        if (protocol < 332) {
            this.putVarInt(4); // gamePublish
        }
        if (protocol >= 201) {
            this.putLInt(this.serverChunkTickRange);
        }
        if (protocol >= 223 && protocol < 332) {
            this.putBoolean(false); // broadcastToPlatform
            this.putVarInt(4); // platformBroadcastMode
            this.putBoolean(true); // xblBroadcastIntentOld
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
                        if (protocol >= ProtocolInfo.v1_19_20) {
                            this.putBoolean(this.isDisablingPersonas);
                            this.putBoolean(this.isDisablingCustomSkins);
                            if (protocol >= ProtocolInfo.v1_19_60) {
                                this.putBoolean(this.emoteChatMuted);
                            }
                        }
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
                if (protocol >= ProtocolInfo.v1_19_20) {
                    this.putByte(this.chatRestrictionLevel);
                    this.putBoolean(this.disablePlayerInteractions);
                    if (protocol >= ProtocolInfo.v1_21_0 && protocol < ProtocolInfo.v1_26_0) {
                        this.putString(""); // ServerId
                        this.putString(""); // WorldId
                        this.putString(""); // ScenarioId
                        if (protocol >= ProtocolInfo.v1_21_90) {
                            this.putString(""); // OwnerId
                        }
                    }
                }
                if (protocol >= ProtocolInfo.v1_26_30) {
                    this.putVarInt(0); // ServerEditorConnectionPolicy
                    this.putBoolean(false); // AllowAnonymousBlockDropsInEditorWorlds
                }
            }
        }
        /* Level settings end */
        this.putString(this.levelId);
        this.putString(this.worldName);
        this.putString(this.premiumWorldTemplateId);
        this.putBoolean(this.isTrial);
        if (protocol >= 388) {
            if (protocol >= ProtocolInfo.v1_16_100) {
                if (protocol >= ProtocolInfo.v1_16_210) {
                    if (protocol < ProtocolInfo.v1_21_90) {
                        this.putVarInt(this.isMovementServerAuthoritative ? 1 : 0); // 2 - rewind
                    }
                    this.putVarInt(0); // RewindHistorySize
                    this.putBoolean(!this.forceNoServerAuthBlockBreaking && protocol >= ProtocolInfo.v1_17_0); // isServerAuthoritativeBlockBreaking
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
                if (this.blockDefinitions != null && !this.blockDefinitions.isEmpty()) {
                    this.putUnsignedVarInt(this.blockDefinitions.size());
                    for (CustomBlockDefinition definition : this.blockDefinitions) {
                        this.putString(definition.getIdentifier());
                        this.putNbtTag(definition.getNetworkData());
                    }
                } else {
                    this.putUnsignedVarInt(0); // No custom blocks
                }
            } else {
                this.put(GlobalBlockPalette.getCompiledTable(this.protocol));
            }
            if (protocol >= 361 && protocol < ProtocolInfo.v1_21_60) {
                this.put(RuntimeItems.getMapping(protocol).getItemPalette());
            }
            this.putString(this.multiplayerCorrelationId);
            if (protocol == 354 && vanillaVersion != null && vanillaVersion.startsWith("1.11.4")) {
                this.putBoolean(this.isOnlySpawningV1Villagers);
            } else if (protocol >= 407) {
                this.putBoolean(false); // isInventoryServerAuthoritative
                if (protocol >= ProtocolInfo.v1_16_230_50) {
                    this.putString(""); // serverEngine
                    if (protocol >= ProtocolInfo.v1_18_0_20) {
                        if (protocol < ProtocolInfo.v1_19_0_29) {
                            this.putLLong(0); // blockRegistryChecksum
                        } else {
                            this.put(EMPTY_COMPOUND_TAG); // playerPropertyData
                            this.putLLong(0); // blockRegistryChecksum
                            this.put(EMPTY_UUID); // worldTemplateId
                            if (protocol >= ProtocolInfo.v1_19_20) {
                                this.putBoolean(this.clientSideGenerationEnabled);
                                if (protocol >= ProtocolInfo.v1_19_80) {
                                    this.putBoolean(false); // blockIdsAreHashed
                                    if (protocol >= ProtocolInfo.v1_20_0_23) {
                                        if (protocol >= ProtocolInfo.v1_21_100 && protocol < ProtocolInfo.v1_21_130_28) {
                                            this.putBoolean(false); // mTickDeathSystemsEnabled
                                        }
                                        /* NetworkPermissions start */
                                        this.putBoolean(true); // isServerAuthSounds
                                        /* NetworkPermissions end */
                                        if (protocol >= ProtocolInfo.v1_26_30 && protocol < ProtocolInfo.v1_26_40) {
                                            this.putBoolean(false); // LoggingChat
                                        }
                                        if (protocol >= ProtocolInfo.v1_26_0) {
                                            this.putBoolean(false); // no server join info
                                            /* ServerTelemetryData start */
                                            this.putString("");
                                            this.putString("");
                                            this.putString("");
                                            this.putString("");
                                            /* ServerTelemetryData end */
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
