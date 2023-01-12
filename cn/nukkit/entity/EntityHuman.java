/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHumanType;
import cn.nukkit.entity.data.IntPositionEntityData;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.SetEntityLinkPacket;
import cn.nukkit.utils.PersonaPiece;
import cn.nukkit.utils.PersonaPieceTint;
import cn.nukkit.utils.SerializedImage;
import cn.nukkit.utils.SkinAnimation;
import cn.nukkit.utils.Utils;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EntityHuman
extends EntityHumanType {
    public static final int DATA_PLAYER_FLAG_SLEEP = 1;
    public static final int DATA_PLAYER_FLAG_DEAD = 2;
    public static final int DATA_PLAYER_FLAGS = 26;
    public static final int DATA_PLAYER_BUTTON_TEXT = 40;
    protected UUID uuid;
    protected byte[] rawUUID;
    protected Skin skin;

    @Override
    public float getWidth() {
        return 0.59f;
    }

    @Override
    public float getLength() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return this.isSwimming() || this.isGliding() ? 0.6f : 1.8f;
    }

    @Override
    protected double getStepHeight() {
        return 0.6f;
    }

    @Override
    public float getEyeHeight() {
        return this.isSwimming() || this.isGliding() ? 0.42f : 1.62f;
    }

    @Override
    protected float getBaseOffset() {
        return 1.62f;
    }

    @Override
    public int getNetworkId() {
        return -1;
    }

    public EntityHuman(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    public Skin getSkin() {
        return this.skin;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public byte[] getRawUniqueId() {
        return this.rawUUID;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    @Override
    protected void initEntity() {
        this.setDataFlag(26, 1, false, false);
        this.setDataFlag(0, 49, true, false);
        this.setDataProperty(new IntPositionEntityData(28, 0, 0, 0), false);
        if (!(this instanceof Player)) {
            if (this.namedTag.contains("NameTag")) {
                this.setNameTag(this.namedTag.getString("NameTag"));
            }
            if (this.namedTag.contains("Skin") && this.namedTag.get("Skin") instanceof CompoundTag) {
                Object object;
                CompoundTag compoundTag = this.namedTag.getCompound("Skin");
                if (!compoundTag.contains("Transparent")) {
                    compoundTag.putBoolean("Transparent", false);
                }
                Skin skin = new Skin();
                if (compoundTag.contains("ModelId")) {
                    skin.setSkinId(compoundTag.getString("ModelId"));
                }
                if (compoundTag.contains("PlayFabId")) {
                    skin.setPlayFabId(compoundTag.getString("PlayFabId"));
                }
                if (compoundTag.contains("Data")) {
                    object = compoundTag.getByteArray("Data");
                    if (compoundTag.contains("SkinImageWidth") && compoundTag.contains("SkinImageHeight")) {
                        skin.setSkinData(new SerializedImage(compoundTag.getInt("SkinImageWidth"), compoundTag.getInt("SkinImageHeight"), (byte[])object));
                    } else {
                        skin.setSkinData((byte[])object);
                    }
                }
                if (compoundTag.contains("CapeId")) {
                    skin.setCapeId(compoundTag.getString("CapeId"));
                }
                if (compoundTag.contains("CapeData")) {
                    object = compoundTag.getByteArray("CapeData");
                    if (compoundTag.contains("CapeImageWidth") && compoundTag.contains("CapeImageHeight")) {
                        skin.setCapeData(new SerializedImage(compoundTag.getInt("CapeImageWidth"), compoundTag.getInt("CapeImageHeight"), (byte[])object));
                    } else {
                        skin.setCapeData((byte[])object);
                    }
                }
                if (compoundTag.contains("GeometryName")) {
                    skin.setGeometryName(compoundTag.getString("GeometryName"));
                }
                if (compoundTag.contains("SkinResourcePatch")) {
                    skin.setSkinResourcePatch(new String(compoundTag.getByteArray("SkinResourcePatch"), StandardCharsets.UTF_8));
                }
                if (compoundTag.contains("GeometryData")) {
                    skin.setGeometryData(new String(compoundTag.getByteArray("GeometryData"), StandardCharsets.UTF_8));
                }
                if (compoundTag.contains("SkinAnimationData")) {
                    skin.setAnimationData(new String(compoundTag.getByteArray("SkinAnimationData"), StandardCharsets.UTF_8));
                } else if (compoundTag.contains("AnimationData")) {
                    skin.setAnimationData(new String(compoundTag.getByteArray("AnimationData"), StandardCharsets.UTF_8));
                }
                if (compoundTag.contains("PremiumSkin")) {
                    skin.setPremium(compoundTag.getBoolean("PremiumSkin"));
                }
                if (compoundTag.contains("PersonaSkin")) {
                    skin.setPersona(compoundTag.getBoolean("PersonaSkin"));
                }
                if (compoundTag.contains("CapeOnClassicSkin")) {
                    skin.setCapeOnClassic(compoundTag.getBoolean("CapeOnClassicSkin"));
                }
                if (compoundTag.contains("AnimatedImageData")) {
                    object = compoundTag.getList("AnimatedImageData", CompoundTag.class).getAll().iterator();
                    while (object.hasNext()) {
                        Iterator iterator = (CompoundTag)object.next();
                        skin.getAnimations().add(new SkinAnimation(new SerializedImage(((CompoundTag)((Object)iterator)).getInt("ImageWidth"), ((CompoundTag)((Object)iterator)).getInt("ImageHeight"), ((CompoundTag)((Object)iterator)).getByteArray("Image")), ((CompoundTag)((Object)iterator)).getInt("Type"), ((CompoundTag)((Object)iterator)).getFloat("Frames"), ((CompoundTag)((Object)iterator)).getInt("AnimationExpression")));
                    }
                }
                if (compoundTag.contains("ArmSize")) {
                    skin.setArmSize(compoundTag.getString("ArmSize"));
                }
                if (compoundTag.contains("SkinColor")) {
                    skin.setSkinColor(compoundTag.getString("SkinColor"));
                }
                if (compoundTag.contains("PersonaPieces")) {
                    object = compoundTag.getList("PersonaPieces", CompoundTag.class);
                    for (CompoundTag compoundTag2 : ((ListTag)object).getAll()) {
                        skin.getPersonaPieces().add(new PersonaPiece(compoundTag2.getString("PieceId"), compoundTag2.getString("PieceType"), compoundTag2.getString("PackId"), compoundTag2.getBoolean("IsDefault"), compoundTag2.getString("ProductId")));
                    }
                }
                if (compoundTag.contains("PieceTintColors")) {
                    object = compoundTag.getList("PieceTintColors", CompoundTag.class);
                    for (CompoundTag compoundTag2 : ((ListTag)object).getAll()) {
                        skin.getTintColors().add(new PersonaPieceTint(compoundTag2.getString("PieceType"), compoundTag2.getList("Colors", StringTag.class).getAll().stream().map(stringTag -> stringTag.data).collect(Collectors.toList())));
                    }
                }
                if (compoundTag.contains("IsTrustedSkin")) {
                    skin.setTrusted(compoundTag.getBoolean("IsTrustedSkin"));
                }
                this.setSkin(skin);
            }
            this.uuid = Utils.dataToUUID(String.valueOf(this.getId()).getBytes(StandardCharsets.UTF_8), this.skin.getSkinData().data, this.getNameTag().getBytes(StandardCharsets.UTF_8));
        }
        super.initEntity();
    }

    @Override
    public String getName() {
        return this.getNameTag();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        if (this.skin != null) {
            Object object;
            ListTag<CompoundTag> listTag;
            ListTag<CompoundTag> listTag2;
            Object object2;
            CompoundTag compoundTag = new CompoundTag().putByteArray("Data", this.getSkin().getSkinData().data).putInt("SkinImageWidth", this.getSkin().getSkinData().width).putInt("SkinImageHeight", this.getSkin().getSkinData().height).putString("ModelId", this.skin.getSkinId()).putString("CapeId", this.getSkin().getCapeId()).putByteArray("CapeData", this.getSkin().getCapeData().data).putInt("CapeImageWidth", this.getSkin().getCapeData().width).putInt("CapeImageHeight", this.getSkin().getCapeData().height).putByteArray("SkinResourcePatch", this.getSkin().getSkinResourcePatch().getBytes(StandardCharsets.UTF_8)).putByteArray("GeometryData", this.skin.getGeometryData().getBytes(StandardCharsets.UTF_8)).putByteArray("SkinAnimationData", this.getSkin().getAnimationData().getBytes(StandardCharsets.UTF_8)).putBoolean("PremiumSkin", this.getSkin().isPremium()).putBoolean("PersonaSkin", this.getSkin().isPersona()).putBoolean("CapeOnClassicSkin", this.getSkin().isCapeOnClassic()).putString("ArmSize", this.getSkin().getArmSize()).putString("SkinColor", this.getSkin().getSkinColor()).putBoolean("IsTrustedSkin", this.getSkin().isTrusted());
            List<SkinAnimation> list = this.getSkin().getAnimations();
            if (!list.isEmpty()) {
                object2 = new ListTag("AnimatedImageData");
                listTag2 = list.iterator();
                while (listTag2.hasNext()) {
                    listTag = (SkinAnimation)listTag2.next();
                    ((ListTag)object2).add(new CompoundTag().putFloat("Frames", ((SkinAnimation)((Object)listTag)).frames).putInt("Type", ((SkinAnimation)((Object)listTag)).type).putInt("ImageWidth", ((SkinAnimation)((Object)listTag)).image.width).putInt("ImageHeight", ((SkinAnimation)((Object)listTag)).image.height).putInt("AnimationExpression", ((SkinAnimation)((Object)listTag)).expression).putByteArray("Image", ((SkinAnimation)((Object)listTag)).image.data));
                }
                compoundTag.putList((ListTag<? extends Tag>)object2);
            }
            if (!(object2 = this.getSkin().getPersonaPieces()).isEmpty()) {
                listTag2 = new ListTag<CompoundTag>("PersonaPieces");
                listTag = object2.iterator();
                while (listTag.hasNext()) {
                    object = (PersonaPiece)listTag.next();
                    listTag2.add(new CompoundTag().putString("PieceId", ((PersonaPiece)object).id).putString("PieceType", ((PersonaPiece)object).type).putString("PackId", ((PersonaPiece)object).packId).putBoolean("IsDefault", ((PersonaPiece)object).isDefault).putString("ProductId", ((PersonaPiece)object).productId));
                }
            }
            if (!(listTag2 = this.getSkin().getTintColors()).isEmpty()) {
                listTag = new ListTag<CompoundTag>("PieceTintColors");
                object = listTag2.iterator();
                while (object.hasNext()) {
                    PersonaPieceTint personaPieceTint = (PersonaPieceTint)object.next();
                    ListTag listTag3 = new ListTag("Colors");
                    listTag3.setAll(personaPieceTint.colors.stream().map(string -> new StringTag("", (String)string)).collect(Collectors.toList()));
                    listTag.add(new CompoundTag().putString("PieceType", personaPieceTint.pieceType).putList(listTag3));
                }
            }
            if (!this.getSkin().getPlayFabId().isEmpty()) {
                compoundTag.putString("PlayFabId", this.getSkin().getPlayFabId());
            }
            this.namedTag.putCompound("Skin", compoundTag);
        }
    }

    @Override
    public void addMovement(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.level.addPlayerMovement(this, d2, d3, d4, d5, d6, d7);
    }

    @Override
    public void spawnTo(Player player) {
        block7: {
            if (this == player || this.hasSpawned.containsKey(player.getLoaderId())) break block7;
            this.hasSpawned.put(player.getLoaderId(), player);
            if (!this.skin.isValid(null, this.server.doNotLimitSkinGeometry)) {
                throw new IllegalStateException(this.getClass().getSimpleName() + " must have a valid skin set");
            }
            if (this instanceof Player) {
                this.server.updatePlayerListData(this.uuid, this.getId(), ((Player)this).getDisplayName(), this.skin, ((Player)this).getLoginChainData().getXUID(), new Player[]{player});
            } else {
                this.server.updatePlayerListData(this.uuid, this.getId(), this.getName(), this.skin, new Player[]{player});
            }
            AddPlayerPacket addPlayerPacket = new AddPlayerPacket();
            addPlayerPacket.uuid = this.uuid;
            addPlayerPacket.username = this.getName();
            addPlayerPacket.entityUniqueId = this.getId();
            addPlayerPacket.entityRuntimeId = this.getId();
            addPlayerPacket.x = (float)this.x;
            addPlayerPacket.y = (float)this.y;
            addPlayerPacket.z = (float)this.z;
            addPlayerPacket.speedX = (float)this.motionX;
            addPlayerPacket.speedY = (float)this.motionY;
            addPlayerPacket.speedZ = (float)this.motionZ;
            addPlayerPacket.yaw = (float)this.yaw;
            addPlayerPacket.pitch = (float)this.pitch;
            addPlayerPacket.item = this.getInventory().getItemInHand();
            addPlayerPacket.metadata = this.dataProperties.clone();
            player.dataPacket(addPlayerPacket);
            if (this instanceof Player) {
                this.inventory.sendArmorContents(player);
            } else {
                this.inventory.sendArmorContentsIfNotAr(player);
            }
            this.offhandInventory.sendContents(player);
            if (this.riding != null) {
                SetEntityLinkPacket setEntityLinkPacket = new SetEntityLinkPacket();
                setEntityLinkPacket.vehicleUniqueId = this.riding.getId();
                setEntityLinkPacket.riderUniqueId = this.getId();
                setEntityLinkPacket.type = 1;
                setEntityLinkPacket.immediate = 1;
                player.dataPacket(setEntityLinkPacket);
            }
            if (!(this instanceof Player)) {
                this.server.removePlayerListData(this.uuid, player);
            }
        }
    }

    @Override
    public void close() {
        if (!this.closed) {
            if (this.inventory != null && (!(this instanceof Player) || ((Player)this).loggedIn)) {
                for (Player player : this.inventory.getViewers()) {
                    player.removeWindow(this.inventory);
                }
            }
            super.close();
        }
    }

    @Override
    protected void onBlock(Entity entity, boolean bl, float f2) {
        block1: {
            block0: {
                super.onBlock(entity, bl, f2);
                Item item = this.getOffhandInventory().getItem(0);
                if (item.getId() != 513) break block0;
                this.getOffhandInventory().setItem(0, this.damageArmor(item, entity, f2, true, null));
                break block1;
            }
            Item item = this.getInventory().getItemInHand();
            if (item.getId() != 513) break block1;
            this.getInventory().setItemInHand(this.damageArmor(item, entity, f2, true, null));
        }
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

