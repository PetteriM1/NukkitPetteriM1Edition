/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.AdvancedSourceInterface;
import cn.nukkit.network.CompressionProvider;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.AddItemEntityPacket;
import cn.nukkit.network.protocol.AddPaintingPacket;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.AdventureSettingsPacket;
import cn.nukkit.network.protocol.AnimatePacket;
import cn.nukkit.network.protocol.AnvilDamagePacket;
import cn.nukkit.network.protocol.AvailableCommandsPacket;
import cn.nukkit.network.protocol.AvailableEntityIdentifiersPacket;
import cn.nukkit.network.protocol.BatchPacket;
import cn.nukkit.network.protocol.BiomeDefinitionListPacket;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.network.protocol.BlockPickRequestPacket;
import cn.nukkit.network.protocol.BookEditPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.ChangeDimensionPacket;
import cn.nukkit.network.protocol.ChunkRadiusUpdatedPacket;
import cn.nukkit.network.protocol.ClientCacheStatusPacket;
import cn.nukkit.network.protocol.ClientboundMapItemDataPacket;
import cn.nukkit.network.protocol.CodeBuilderPacket;
import cn.nukkit.network.protocol.CommandRequestPacket;
import cn.nukkit.network.protocol.CompletedUsingItemPacket;
import cn.nukkit.network.protocol.ContainerClosePacket;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.ContainerSetDataPacket;
import cn.nukkit.network.protocol.CraftingDataPacket;
import cn.nukkit.network.protocol.CraftingEventPacket;
import cn.nukkit.network.protocol.CreativeContentPacket;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.DeathInfoPacket;
import cn.nukkit.network.protocol.DebugInfoPacket;
import cn.nukkit.network.protocol.DimensionDataPacket;
import cn.nukkit.network.protocol.DisconnectPacket;
import cn.nukkit.network.protocol.EmoteListPacket;
import cn.nukkit.network.protocol.EmotePacket;
import cn.nukkit.network.protocol.EntityEventPacket;
import cn.nukkit.network.protocol.EntityFallPacket;
import cn.nukkit.network.protocol.FilterTextPacket;
import cn.nukkit.network.protocol.GameRulesChangedPacket;
import cn.nukkit.network.protocol.HurtArmorPacket;
import cn.nukkit.network.protocol.InteractPacket;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import cn.nukkit.network.protocol.ItemComponentPacket;
import cn.nukkit.network.protocol.ItemFrameDropItemPacket;
import cn.nukkit.network.protocol.LecternUpdatePacket;
import cn.nukkit.network.protocol.LevelChunkPacket;
import cn.nukkit.network.protocol.LevelEventGenericPacket;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import cn.nukkit.network.protocol.LevelSoundEventPacketV1;
import cn.nukkit.network.protocol.LoginPacket;
import cn.nukkit.network.protocol.MapCreateLockedCopyPacket;
import cn.nukkit.network.protocol.MapInfoRequestPacket;
import cn.nukkit.network.protocol.MobArmorEquipmentPacket;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import cn.nukkit.network.protocol.ModalFormRequestPacket;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.MoveEntityDeltaPacket;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.NetworkChunkPublisherUpdatePacket;
import cn.nukkit.network.protocol.NetworkSettingsPacket;
import cn.nukkit.network.protocol.NetworkStackLatencyPacket;
import cn.nukkit.network.protocol.OnScreenTextureAnimationPacket;
import cn.nukkit.network.protocol.PacketViolationWarningPacket;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.network.protocol.PlayStatusPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.PlayerArmorDamagePacket;
import cn.nukkit.network.protocol.PlayerAuthInputPacket;
import cn.nukkit.network.protocol.PlayerEnchantOptionsPacket;
import cn.nukkit.network.protocol.PlayerHotbarPacket;
import cn.nukkit.network.protocol.PlayerInputPacket;
import cn.nukkit.network.protocol.PlayerListPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.RequestAbilityPacket;
import cn.nukkit.network.protocol.RequestChunkRadiusPacket;
import cn.nukkit.network.protocol.RequestNetworkSettingsPacket;
import cn.nukkit.network.protocol.ResourcePackChunkDataPacket;
import cn.nukkit.network.protocol.ResourcePackChunkRequestPacket;
import cn.nukkit.network.protocol.ResourcePackClientResponsePacket;
import cn.nukkit.network.protocol.ResourcePackDataInfoPacket;
import cn.nukkit.network.protocol.ResourcePackStackPacket;
import cn.nukkit.network.protocol.ResourcePacksInfoPacket;
import cn.nukkit.network.protocol.RespawnPacket;
import cn.nukkit.network.protocol.RiderJumpPacket;
import cn.nukkit.network.protocol.ScriptCustomEventPacket;
import cn.nukkit.network.protocol.ServerSettingsRequestPacket;
import cn.nukkit.network.protocol.ServerSettingsResponsePacket;
import cn.nukkit.network.protocol.SetCommandsEnabledPacket;
import cn.nukkit.network.protocol.SetDifficultyPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import cn.nukkit.network.protocol.SetHealthPacket;
import cn.nukkit.network.protocol.SetLocalPlayerAsInitializedPacket;
import cn.nukkit.network.protocol.SetPlayerGameTypePacket;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.network.protocol.SetTimePacket;
import cn.nukkit.network.protocol.SetTitlePacket;
import cn.nukkit.network.protocol.ShowCreditsPacket;
import cn.nukkit.network.protocol.SpawnExperienceOrbPacket;
import cn.nukkit.network.protocol.SpawnParticleEffectPacket;
import cn.nukkit.network.protocol.StartGamePacket;
import cn.nukkit.network.protocol.TakeItemEntityPacket;
import cn.nukkit.network.protocol.TextPacket;
import cn.nukkit.network.protocol.ToastRequestPacket;
import cn.nukkit.network.protocol.TransferPacket;
import cn.nukkit.network.protocol.UpdateAbilitiesPacket;
import cn.nukkit.network.protocol.UpdateAdventureSettingsPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import cn.nukkit.network.protocol.UpdatePlayerGameTypePacket;
import cn.nukkit.network.protocol.UpdateSoftEnumPacket;
import cn.nukkit.network.protocol.UpdateTradePacket;
import cn.nukkit.network.protocol.VideoStreamConnectPacket;
import cn.nukkit.utils.BinaryStream;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.VarInt;
import cn.nukkit.utils.Zlib;
import com.nukkitx.network.util.Preconditions;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Network {
    private static final Logger b = LogManager.getLogger(Network.class);
    public static final byte CHANNEL_NONE = 0;
    public static final byte CHANNEL_PRIORITY = 1;
    public static final byte CHANNEL_WORLD_CHUNKS = 2;
    public static final byte CHANNEL_MOVEMENT = 3;
    public static final byte CHANNEL_BLOCKS = 4;
    public static final byte CHANNEL_WORLD_EVENTS = 5;
    public static final byte CHANNEL_ENTITY_SPAWNING = 6;
    public static final byte CHANNEL_TEXT = 7;
    public static final byte CHANNEL_END = 31;
    private Class<? extends DataPacket>[] h = new Class[256];
    private final Server i;
    private final Set<SourceInterface> c = new HashSet<SourceInterface>();
    private final Set<AdvancedSourceInterface> d = new HashSet<AdvancedSourceInterface>();
    private double e = 0.0;
    private double g = 0.0;
    private String f;
    private String a;

    public Network(Server server) {
        this.a();
        this.i = server;
    }

    public void addStatistics(double d2, double d3) {
        this.e += d2;
        this.g += d3;
    }

    public double getUpload() {
        return this.e;
    }

    public double getDownload() {
        return this.g;
    }

    public void resetStatistics() {
        this.e = 0.0;
        this.g = 0.0;
    }

    public Set<SourceInterface> getInterfaces() {
        return this.c;
    }

    public void processInterfaces() {
        for (SourceInterface sourceInterface : this.c) {
            try {
                sourceInterface.process();
            }
            catch (Exception exception) {
                sourceInterface.emergencyShutdown();
                this.unregisterInterface(sourceInterface);
                b.fatal(this.i.getLanguage().translateString("nukkit.server.networkError", new String[]{sourceInterface.getClass().getName(), Utils.getExceptionMessage(exception)}), (Throwable)exception);
            }
        }
    }

    public void registerInterface(SourceInterface sourceInterface) {
        this.c.add(sourceInterface);
        if (sourceInterface instanceof AdvancedSourceInterface) {
            this.d.add((AdvancedSourceInterface)sourceInterface);
            ((AdvancedSourceInterface)sourceInterface).setNetwork(this);
        }
        sourceInterface.setName(this.f + "!@#" + this.a);
    }

    public void unregisterInterface(SourceInterface sourceInterface) {
        block0: {
            this.c.remove(sourceInterface);
            if (!(sourceInterface instanceof AdvancedSourceInterface)) break block0;
            this.d.remove(sourceInterface);
        }
    }

    public void setName(String string) {
        this.f = string;
        this.updateName();
    }

    public String getName() {
        return this.f;
    }

    public String getSubName() {
        return this.a;
    }

    public void setSubName(String string) {
        this.a = string;
    }

    public void updateName() {
        for (SourceInterface sourceInterface : this.c) {
            sourceInterface.setName(this.f + "!@#" + this.a);
        }
    }

    public void registerPacket(byte by, Class<? extends DataPacket> clazz) {
        this.h[by & 0xFF] = clazz;
    }

    public Server getServer() {
        return this.i;
    }

    public void processBatch(BatchPacket batchPacket, Player player) {
        ObjectArrayList<DataPacket> objectArrayList = new ObjectArrayList<DataPacket>();
        try {
            this.processBatch(batchPacket.payload, objectArrayList, player.getNetworkSession().getCompression(), player.protocol, player.raknetProtocol);
            this.processPackets(player, objectArrayList);
        }
        catch (Exception exception) {
            b.error("Error whilst decoding batch packet from " + player.getName(), (Throwable)exception);
            player.close("", "Error whilst decoding batch packet");
        }
    }

    public void processBatch(byte[] byArray, Collection<DataPacket> collection, CompressionProvider compressionProvider, int n, int n2) throws Exception {
        byte[] byArray2 = compressionProvider.decompress(byArray);
        BinaryStream binaryStream = new BinaryStream(byArray2);
        int n3 = 0;
        while (!binaryStream.feof()) {
            int n4;
            if (++n3 >= 800) {
                throw new ProtocolException("Too big batch packet (count >= 800)");
            }
            byte[] byArray3 = binaryStream.getByteArray();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray3);
            switch (n2) {
                case 7: {
                    n4 = byteArrayInputStream.read();
                    break;
                }
                case 8: {
                    n4 = byteArrayInputStream.read();
                    byteArrayInputStream.skip(2L);
                    break;
                }
                default: {
                    n4 = (int)VarInt.readUnsignedVarInt(byteArrayInputStream) & 0x3FF;
                }
            }
            DataPacket dataPacket = this.getPacket(n4);
            if (dataPacket == null) {
                if (Nukkit.DEBUG <= 1) continue;
                b.debug("Received unknown packet with ID: {}", (Object)Integer.toHexString(n4));
                continue;
            }
            dataPacket.setBuffer(byArray3, byArray3.length - byteArrayInputStream.available());
            dataPacket.protocol = n;
            try {
                if (n2 > 8) {
                    dataPacket.decode();
                } else {
                    dataPacket.setBuffer(byArray3, 3);
                    dataPacket.decode();
                }
            }
            catch (Exception exception) {
                throw new IllegalStateException("Unable to decode " + dataPacket.getClass().getSimpleName(), exception);
            }
            collection.add(dataPacket);
        }
    }

    public void processPackets(Player player, List<DataPacket> list) {
        if (list.isEmpty()) {
            return;
        }
        list.forEach(player::handleDataPacket);
    }

    public DataPacket getPacket(byte by) {
        return this.getPacket(by & 0xFF);
    }

    public DataPacket getPacket(int n) {
        Preconditions.checkElementIndex(n, this.h.length, "packetId");
        Class<? extends DataPacket> clazz = this.h[n];
        if (clazz != null) {
            try {
                return clazz.newInstance();
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
            }
        }
        return null;
    }

    public void sendPacket(InetSocketAddress inetSocketAddress, ByteBuf byteBuf) {
        for (AdvancedSourceInterface advancedSourceInterface : this.d) {
            advancedSourceInterface.sendRawPacket(inetSocketAddress, byteBuf);
        }
    }

    public void blockAddress(InetAddress inetAddress) {
        for (AdvancedSourceInterface advancedSourceInterface : this.d) {
            advancedSourceInterface.blockAddress(inetAddress);
        }
    }

    public void blockAddress(InetAddress inetAddress, int n) {
        for (AdvancedSourceInterface advancedSourceInterface : this.d) {
            advancedSourceInterface.blockAddress(inetAddress, n);
        }
    }

    public void unblockAddress(InetAddress inetAddress) {
        for (AdvancedSourceInterface advancedSourceInterface : this.d) {
            advancedSourceInterface.unblockAddress(inetAddress);
        }
    }

    private void a() {
        this.h = new Class[256];
        this.registerPacket((byte)13, AddEntityPacket.class);
        this.registerPacket((byte)15, AddItemEntityPacket.class);
        this.registerPacket((byte)22, AddPaintingPacket.class);
        this.registerPacket((byte)12, AddPlayerPacket.class);
        this.registerPacket((byte)55, AdventureSettingsPacket.class);
        this.registerPacket((byte)44, AnimatePacket.class);
        this.registerPacket((byte)-115, AnvilDamagePacket.class);
        this.registerPacket((byte)76, AvailableCommandsPacket.class);
        this.registerPacket((byte)-1, BatchPacket.class);
        this.registerPacket((byte)56, BlockEntityDataPacket.class);
        this.registerPacket((byte)26, BlockEventPacket.class);
        this.registerPacket((byte)34, BlockPickRequestPacket.class);
        this.registerPacket((byte)97, BookEditPacket.class);
        this.registerPacket((byte)74, BossEventPacket.class);
        this.registerPacket((byte)61, ChangeDimensionPacket.class);
        this.registerPacket((byte)70, ChunkRadiusUpdatedPacket.class);
        this.registerPacket((byte)67, ClientboundMapItemDataPacket.class);
        this.registerPacket((byte)77, CommandRequestPacket.class);
        this.registerPacket((byte)47, ContainerClosePacket.class);
        this.registerPacket((byte)46, ContainerOpenPacket.class);
        this.registerPacket((byte)51, ContainerSetDataPacket.class);
        this.registerPacket((byte)52, CraftingDataPacket.class);
        this.registerPacket((byte)53, CraftingEventPacket.class);
        this.registerPacket((byte)5, DisconnectPacket.class);
        this.registerPacket((byte)27, EntityEventPacket.class);
        this.registerPacket((byte)37, EntityFallPacket.class);
        this.registerPacket((byte)58, LevelChunkPacket.class);
        this.registerPacket((byte)72, GameRulesChangedPacket.class);
        this.registerPacket((byte)38, HurtArmorPacket.class);
        this.registerPacket((byte)33, InteractPacket.class);
        this.registerPacket((byte)49, InventoryContentPacket.class);
        this.registerPacket((byte)50, InventorySlotPacket.class);
        this.registerPacket((byte)30, InventoryTransactionPacket.class);
        this.registerPacket((byte)71, ItemFrameDropItemPacket.class);
        this.registerPacket((byte)25, LevelEventPacket.class);
        this.registerPacket((byte)24, LevelSoundEventPacketV1.class);
        this.registerPacket((byte)1, LoginPacket.class);
        this.registerPacket((byte)68, MapInfoRequestPacket.class);
        this.registerPacket((byte)32, MobArmorEquipmentPacket.class);
        this.registerPacket((byte)31, MobEquipmentPacket.class);
        this.registerPacket((byte)100, ModalFormRequestPacket.class);
        this.registerPacket((byte)101, ModalFormResponsePacket.class);
        this.registerPacket((byte)18, MoveEntityAbsolutePacket.class);
        this.registerPacket((byte)19, MovePlayerPacket.class);
        this.registerPacket((byte)36, PlayerActionPacket.class);
        this.registerPacket((byte)57, PlayerInputPacket.class);
        this.registerPacket((byte)63, PlayerListPacket.class);
        this.registerPacket((byte)48, PlayerHotbarPacket.class);
        this.registerPacket((byte)86, PlaySoundPacket.class);
        this.registerPacket((byte)2, PlayStatusPacket.class);
        this.registerPacket((byte)14, RemoveEntityPacket.class);
        this.registerPacket((byte)69, RequestChunkRadiusPacket.class);
        this.registerPacket((byte)6, ResourcePacksInfoPacket.class);
        this.registerPacket((byte)7, ResourcePackStackPacket.class);
        this.registerPacket((byte)8, ResourcePackClientResponsePacket.class);
        this.registerPacket((byte)82, ResourcePackDataInfoPacket.class);
        this.registerPacket((byte)83, ResourcePackChunkDataPacket.class);
        this.registerPacket((byte)84, ResourcePackChunkRequestPacket.class);
        this.registerPacket((byte)93, PlayerSkinPacket.class);
        this.registerPacket((byte)45, RespawnPacket.class);
        this.registerPacket((byte)20, RiderJumpPacket.class);
        this.registerPacket((byte)59, SetCommandsEnabledPacket.class);
        this.registerPacket((byte)60, SetDifficultyPacket.class);
        this.registerPacket((byte)39, SetEntityDataPacket.class);
        this.registerPacket((byte)41, SetEntityLinkPacket.class);
        this.registerPacket((byte)40, SetEntityMotionPacket.class);
        this.registerPacket((byte)42, SetHealthPacket.class);
        this.registerPacket((byte)62, SetPlayerGameTypePacket.class);
        this.registerPacket((byte)43, SetSpawnPositionPacket.class);
        this.registerPacket((byte)88, SetTitlePacket.class);
        this.registerPacket((byte)10, SetTimePacket.class);
        this.registerPacket((byte)102, ServerSettingsRequestPacket.class);
        this.registerPacket((byte)103, ServerSettingsResponsePacket.class);
        this.registerPacket((byte)75, ShowCreditsPacket.class);
        this.registerPacket((byte)66, SpawnExperienceOrbPacket.class);
        this.registerPacket((byte)11, StartGamePacket.class);
        this.registerPacket((byte)17, TakeItemEntityPacket.class);
        this.registerPacket((byte)9, TextPacket.class);
        this.registerPacket((byte)85, TransferPacket.class);
        this.registerPacket((byte)29, UpdateAttributesPacket.class);
        this.registerPacket((byte)21, UpdateBlockPacket.class);
        this.registerPacket((byte)80, UpdateTradePacket.class);
        this.registerPacket((byte)111, MoveEntityDeltaPacket.class);
        this.registerPacket((byte)113, SetLocalPlayerAsInitializedPacket.class);
        this.registerPacket((byte)115, NetworkStackLatencyPacket.class);
        this.registerPacket((byte)114, UpdateSoftEnumPacket.class);
        this.registerPacket((byte)121, NetworkChunkPublisherUpdatePacket.class);
        this.registerPacket((byte)119, AvailableEntityIdentifiersPacket.class);
        this.registerPacket((byte)120, LevelSoundEventPacket.class);
        this.registerPacket((byte)117, ScriptCustomEventPacket.class);
        this.registerPacket((byte)118, SpawnParticleEffectPacket.class);
        this.registerPacket((byte)122, BiomeDefinitionListPacket.class);
        this.registerPacket((byte)123, LevelSoundEventPacket.class);
        this.registerPacket((byte)124, LevelEventGenericPacket.class);
        this.registerPacket((byte)125, LecternUpdatePacket.class);
        this.registerPacket((byte)126, VideoStreamConnectPacket.class);
        this.registerPacket((byte)-127, ClientCacheStatusPacket.class);
        this.registerPacket((byte)-125, MapCreateLockedCopyPacket.class);
        this.registerPacket((byte)-126, OnScreenTextureAnimationPacket.class);
        this.registerPacket((byte)-114, CompletedUsingItemPacket.class);
        this.registerPacket((byte)-113, NetworkSettingsPacket.class);
        this.registerPacket((byte)-106, CodeBuilderPacket.class);
        this.registerPacket((byte)-112, PlayerAuthInputPacket.class);
        this.registerPacket((byte)-111, CreativeContentPacket.class);
        this.registerPacket((byte)-101, DebugInfoPacket.class);
        this.registerPacket((byte)-104, EmoteListPacket.class);
        this.registerPacket((byte)-100, PacketViolationWarningPacket.class);
        this.registerPacket((byte)-107, PlayerArmorDamagePacket.class);
        this.registerPacket((byte)-110, PlayerEnchantOptionsPacket.class);
        this.registerPacket((byte)-105, UpdatePlayerGameTypePacket.class);
        this.registerPacket((byte)-69, UpdateAbilitiesPacket.class);
        this.registerPacket((byte)-72, RequestAbilityPacket.class);
        this.registerPacket((byte)-68, UpdateAdventureSettingsPacket.class);
        this.registerPacket((byte)-118, EmotePacket.class);
        this.registerPacket((byte)-94, ItemComponentPacket.class);
        this.registerPacket((byte)-93, FilterTextPacket.class);
        this.registerPacket((byte)-76, DimensionDataPacket.class);
        this.registerPacket((byte)-70, ToastRequestPacket.class);
        this.registerPacket((byte)-67, DeathInfoPacket.class);
        this.registerPacket((byte)-63, RequestNetworkSettingsPacket.class);
    }

    public static byte[] inflateRaw(byte[] byArray) throws IOException, DataFormatException {
        return Zlib.inflateRaw(byArray, -1);
    }

    public static byte[] deflateRaw(byte[] byArray, int n) throws IOException {
        try {
            return Zlib.deflateRaw(byArray, n);
        }
        catch (Exception exception) {
            throw new IOException(exception);
        }
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

