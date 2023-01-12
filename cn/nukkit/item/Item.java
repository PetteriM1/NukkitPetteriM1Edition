/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item;

import cn.nukkit.Nukkit;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Fuel;
import cn.nukkit.item.ItemApple;
import cn.nukkit.item.ItemAppleGold;
import cn.nukkit.item.ItemAppleGoldEnchanted;
import cn.nukkit.item.ItemArmorStand;
import cn.nukkit.item.ItemArrow;
import cn.nukkit.item.ItemAxeDiamond;
import cn.nukkit.item.ItemAxeGold;
import cn.nukkit.item.ItemAxeIron;
import cn.nukkit.item.ItemAxeNetherite;
import cn.nukkit.item.ItemAxeStone;
import cn.nukkit.item.ItemAxeWood;
import cn.nukkit.item.ItemBanner;
import cn.nukkit.item.ItemBannerPattern;
import cn.nukkit.item.ItemBed;
import cn.nukkit.item.ItemBeefRaw;
import cn.nukkit.item.ItemBeetroot;
import cn.nukkit.item.ItemBeetrootSoup;
import cn.nukkit.item.ItemBlazePowder;
import cn.nukkit.item.ItemBlazeRod;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemBoat;
import cn.nukkit.item.ItemBone;
import cn.nukkit.item.ItemBook;
import cn.nukkit.item.ItemBookAndQuill;
import cn.nukkit.item.ItemBookEnchanted;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.item.ItemBootsChain;
import cn.nukkit.item.ItemBootsDiamond;
import cn.nukkit.item.ItemBootsGold;
import cn.nukkit.item.ItemBootsIron;
import cn.nukkit.item.ItemBootsLeather;
import cn.nukkit.item.ItemBootsNetherite;
import cn.nukkit.item.ItemBow;
import cn.nukkit.item.ItemBowl;
import cn.nukkit.item.ItemBread;
import cn.nukkit.item.ItemBrewingStand;
import cn.nukkit.item.ItemBrick;
import cn.nukkit.item.ItemBucket;
import cn.nukkit.item.ItemCactus;
import cn.nukkit.item.ItemCake;
import cn.nukkit.item.ItemCampfire;
import cn.nukkit.item.ItemCarrot;
import cn.nukkit.item.ItemCarrotGolden;
import cn.nukkit.item.ItemCarrotOnAStick;
import cn.nukkit.item.ItemCauldron;
import cn.nukkit.item.ItemChestBoatAcacia;
import cn.nukkit.item.ItemChestBoatBirch;
import cn.nukkit.item.ItemChestBoatDarkOak;
import cn.nukkit.item.ItemChestBoatJungle;
import cn.nukkit.item.ItemChestBoatMangrove;
import cn.nukkit.item.ItemChestBoatOak;
import cn.nukkit.item.ItemChestBoatSpruce;
import cn.nukkit.item.ItemChestplateChain;
import cn.nukkit.item.ItemChestplateDiamond;
import cn.nukkit.item.ItemChestplateGold;
import cn.nukkit.item.ItemChestplateIron;
import cn.nukkit.item.ItemChestplateLeather;
import cn.nukkit.item.ItemChestplateNetherite;
import cn.nukkit.item.ItemChickenCooked;
import cn.nukkit.item.ItemChickenRaw;
import cn.nukkit.item.ItemChorusFruit;
import cn.nukkit.item.ItemChorusFruitPopped;
import cn.nukkit.item.ItemClay;
import cn.nukkit.item.ItemClock;
import cn.nukkit.item.ItemClownfish;
import cn.nukkit.item.ItemCoal;
import cn.nukkit.item.ItemCompass;
import cn.nukkit.item.ItemCookie;
import cn.nukkit.item.ItemCrossbow;
import cn.nukkit.item.ItemDiamond;
import cn.nukkit.item.ItemDiscFragment5;
import cn.nukkit.item.ItemDoorAcacia;
import cn.nukkit.item.ItemDoorBirch;
import cn.nukkit.item.ItemDoorDarkOak;
import cn.nukkit.item.ItemDoorIron;
import cn.nukkit.item.ItemDoorJungle;
import cn.nukkit.item.ItemDoorSpruce;
import cn.nukkit.item.ItemDoorWood;
import cn.nukkit.item.ItemDragonBreath;
import cn.nukkit.item.ItemDriedKelp;
import cn.nukkit.item.ItemDye;
import cn.nukkit.item.ItemEgg;
import cn.nukkit.item.ItemElytra;
import cn.nukkit.item.ItemEmerald;
import cn.nukkit.item.ItemEmptyMap;
import cn.nukkit.item.ItemEndCrystal;
import cn.nukkit.item.ItemEnderEye;
import cn.nukkit.item.ItemEnderPearl;
import cn.nukkit.item.ItemExpBottle;
import cn.nukkit.item.ItemFeather;
import cn.nukkit.item.ItemFireCharge;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.item.ItemFireworkStar;
import cn.nukkit.item.ItemFish;
import cn.nukkit.item.ItemFishCooked;
import cn.nukkit.item.ItemFishingRod;
import cn.nukkit.item.ItemFlint;
import cn.nukkit.item.ItemFlintSteel;
import cn.nukkit.item.ItemFlowerPot;
import cn.nukkit.item.ItemGhastTear;
import cn.nukkit.item.ItemGlassBottle;
import cn.nukkit.item.ItemGlowstoneDust;
import cn.nukkit.item.ItemGunpowder;
import cn.nukkit.item.ItemHeartOfTheSea;
import cn.nukkit.item.ItemHelmetChain;
import cn.nukkit.item.ItemHelmetDiamond;
import cn.nukkit.item.ItemHelmetGold;
import cn.nukkit.item.ItemHelmetIron;
import cn.nukkit.item.ItemHelmetLeather;
import cn.nukkit.item.ItemHelmetNetherite;
import cn.nukkit.item.ItemHoeDiamond;
import cn.nukkit.item.ItemHoeGold;
import cn.nukkit.item.ItemHoeIron;
import cn.nukkit.item.ItemHoeNetherite;
import cn.nukkit.item.ItemHoeStone;
import cn.nukkit.item.ItemHoeWood;
import cn.nukkit.item.ItemHoneyBottle;
import cn.nukkit.item.ItemHoneycomb;
import cn.nukkit.item.ItemHopper;
import cn.nukkit.item.ItemHorseArmorDiamond;
import cn.nukkit.item.ItemHorseArmorGold;
import cn.nukkit.item.ItemHorseArmorIron;
import cn.nukkit.item.ItemHorseArmorLeather;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemIngotGold;
import cn.nukkit.item.ItemIngotIron;
import cn.nukkit.item.ItemIngotNetherite;
import cn.nukkit.item.ItemItemFrame;
import cn.nukkit.item.ItemKelp;
import cn.nukkit.item.ItemLadder;
import cn.nukkit.item.ItemLead;
import cn.nukkit.item.ItemLeather;
import cn.nukkit.item.ItemLeggingsChain;
import cn.nukkit.item.ItemLeggingsDiamond;
import cn.nukkit.item.ItemLeggingsGold;
import cn.nukkit.item.ItemLeggingsIron;
import cn.nukkit.item.ItemLeggingsLeather;
import cn.nukkit.item.ItemLeggingsNetherite;
import cn.nukkit.item.ItemLodestoneCompass;
import cn.nukkit.item.ItemMagmaCream;
import cn.nukkit.item.ItemMap;
import cn.nukkit.item.ItemMelon;
import cn.nukkit.item.ItemMelonGlistering;
import cn.nukkit.item.ItemMinecart;
import cn.nukkit.item.ItemMinecartChest;
import cn.nukkit.item.ItemMinecartHopper;
import cn.nukkit.item.ItemMinecartTNT;
import cn.nukkit.item.ItemMushroomStew;
import cn.nukkit.item.ItemMuttonCooked;
import cn.nukkit.item.ItemMuttonRaw;
import cn.nukkit.item.ItemNameTag;
import cn.nukkit.item.ItemNautilusShell;
import cn.nukkit.item.ItemNetherBrick;
import cn.nukkit.item.ItemNetherStar;
import cn.nukkit.item.ItemNetherWart;
import cn.nukkit.item.ItemNuggetGold;
import cn.nukkit.item.ItemNuggetIron;
import cn.nukkit.item.ItemPainting;
import cn.nukkit.item.ItemPaper;
import cn.nukkit.item.ItemPhantomMembrane;
import cn.nukkit.item.ItemPickaxeDiamond;
import cn.nukkit.item.ItemPickaxeGold;
import cn.nukkit.item.ItemPickaxeIron;
import cn.nukkit.item.ItemPickaxeNetherite;
import cn.nukkit.item.ItemPickaxeStone;
import cn.nukkit.item.ItemPickaxeWood;
import cn.nukkit.item.ItemPorkchopCooked;
import cn.nukkit.item.ItemPorkchopRaw;
import cn.nukkit.item.ItemPotato;
import cn.nukkit.item.ItemPotatoBaked;
import cn.nukkit.item.ItemPotatoPoisonous;
import cn.nukkit.item.ItemPotion;
import cn.nukkit.item.ItemPotionLingering;
import cn.nukkit.item.ItemPotionSplash;
import cn.nukkit.item.ItemPrismarineCrystals;
import cn.nukkit.item.ItemPrismarineShard;
import cn.nukkit.item.ItemPufferfish;
import cn.nukkit.item.ItemPumpkinPie;
import cn.nukkit.item.ItemQuartz;
import cn.nukkit.item.ItemRabbitCooked;
import cn.nukkit.item.ItemRabbitFoot;
import cn.nukkit.item.ItemRabbitHide;
import cn.nukkit.item.ItemRabbitRaw;
import cn.nukkit.item.ItemRabbitStew;
import cn.nukkit.item.ItemRail;
import cn.nukkit.item.ItemRecord11;
import cn.nukkit.item.ItemRecord13;
import cn.nukkit.item.ItemRecord5;
import cn.nukkit.item.ItemRecordBlocks;
import cn.nukkit.item.ItemRecordCat;
import cn.nukkit.item.ItemRecordChirp;
import cn.nukkit.item.ItemRecordFar;
import cn.nukkit.item.ItemRecordMall;
import cn.nukkit.item.ItemRecordMellohi;
import cn.nukkit.item.ItemRecordOtherside;
import cn.nukkit.item.ItemRecordPigstep;
import cn.nukkit.item.ItemRecordStal;
import cn.nukkit.item.ItemRecordStrad;
import cn.nukkit.item.ItemRecordWait;
import cn.nukkit.item.ItemRecordWard;
import cn.nukkit.item.ItemRedstone;
import cn.nukkit.item.ItemRedstoneComparator;
import cn.nukkit.item.ItemRedstoneRepeater;
import cn.nukkit.item.ItemRottenFlesh;
import cn.nukkit.item.ItemSaddle;
import cn.nukkit.item.ItemSalmon;
import cn.nukkit.item.ItemSalmonCooked;
import cn.nukkit.item.ItemScrapNetherite;
import cn.nukkit.item.ItemScute;
import cn.nukkit.item.ItemSeedsBeetroot;
import cn.nukkit.item.ItemSeedsMelon;
import cn.nukkit.item.ItemSeedsPumpkin;
import cn.nukkit.item.ItemSeedsWheat;
import cn.nukkit.item.ItemShears;
import cn.nukkit.item.ItemShield;
import cn.nukkit.item.ItemShovelDiamond;
import cn.nukkit.item.ItemShovelGold;
import cn.nukkit.item.ItemShovelIron;
import cn.nukkit.item.ItemShovelNetherite;
import cn.nukkit.item.ItemShovelStone;
import cn.nukkit.item.ItemShovelWood;
import cn.nukkit.item.ItemShulkerShell;
import cn.nukkit.item.ItemSign;
import cn.nukkit.item.ItemSignAcacia;
import cn.nukkit.item.ItemSignBirch;
import cn.nukkit.item.ItemSignDarkOak;
import cn.nukkit.item.ItemSignJungle;
import cn.nukkit.item.ItemSignSpruce;
import cn.nukkit.item.ItemSkull;
import cn.nukkit.item.ItemSlimeball;
import cn.nukkit.item.ItemSnowball;
import cn.nukkit.item.ItemSpawnEgg;
import cn.nukkit.item.ItemSpiderEye;
import cn.nukkit.item.ItemSpiderEyeFermented;
import cn.nukkit.item.ItemSpyglass;
import cn.nukkit.item.ItemSteak;
import cn.nukkit.item.ItemStick;
import cn.nukkit.item.ItemString;
import cn.nukkit.item.ItemSugar;
import cn.nukkit.item.ItemSugarcane;
import cn.nukkit.item.ItemSuspiciousStew;
import cn.nukkit.item.ItemSweetBerries;
import cn.nukkit.item.ItemSwordDiamond;
import cn.nukkit.item.ItemSwordGold;
import cn.nukkit.item.ItemSwordIron;
import cn.nukkit.item.ItemSwordNetherite;
import cn.nukkit.item.ItemSwordStone;
import cn.nukkit.item.ItemSwordWood;
import cn.nukkit.item.ItemTotem;
import cn.nukkit.item.ItemTrident;
import cn.nukkit.item.ItemTurtleShell;
import cn.nukkit.item.ItemWarpedFungusOnAStick;
import cn.nukkit.item.ItemWheat;
import cn.nukkit.item.RuntimeItemMapping;
import cn.nukkit.item.RuntimeItems;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.nbt.tag.StringTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.ProtocolInfo;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.MainLogger;
import cn.nukkit.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Item
implements Cloneable,
BlockID,
ItemID,
ProtocolInfo {
    protected static final String UNKNOWN_STR = "Unknown";
    public static Class[] list = null;
    protected Block block = null;
    protected final int id;
    protected int meta;
    protected boolean hasMeta = true;
    private byte[] i = new byte[0];
    private CompoundTag w = null;
    public int count;
    protected String name;
    private static final List<Item> v = new ObjectArrayList<Item>();
    private static final List<Item> n = new ObjectArrayList<Item>();
    private static final List<Item> l = new ObjectArrayList<Item>();
    private static final List<Item> k = new ObjectArrayList<Item>();
    private static final List<Item> r = new ObjectArrayList<Item>();
    private static final List<Item> q = new ObjectArrayList<Item>();
    private static final List<Item> p = new ObjectArrayList<Item>();
    private static final List<Item> o = new ObjectArrayList<Item>();
    private static final List<Item> t = new ObjectArrayList<Item>();
    private static final List<Item> s = new ObjectArrayList<Item>();
    private static final List<Item> m = new ObjectArrayList<Item>();
    private static final List<Item> b = new ObjectArrayList<Item>();
    private static final List<Item> u = new ObjectArrayList<Item>();
    private static final List<Item> d = new ObjectArrayList<Item>();
    private static final List<Item> h = new ObjectArrayList<Item>();
    private static final List<Item> f = new ObjectArrayList<Item>();
    private static final List<Item> c = new ObjectArrayList<Item>();
    private static final List<Item> a = new ObjectArrayList<Item>();
    private static final List<Item> e = new ObjectArrayList<Item>();
    private static final List<Item> j = new ObjectArrayList<Item>();
    private static final int g = 560;

    public Item(int n) {
        this(n, 0, 1, "Unknown");
    }

    public Item(int n, Integer n2) {
        this(n, n2, 1, "Unknown");
    }

    public Item(int n, Integer n2, int n3) {
        this(n, n2, n3, "Unknown");
    }

    public Item(int n, Integer n2, int n3, String string) {
        this.id = n;
        if (n2 != null && n2 >= 0) {
            this.meta = n2 & 0xFFFF;
        } else {
            this.hasMeta = false;
        }
        this.count = n3;
        this.name = string;
    }

    public boolean hasMeta() {
        return this.hasMeta;
    }

    public boolean canBeActivated() {
        return false;
    }

    public static void init() {
        if (list == null) {
            list = new Class[65535];
            Item.list[65] = ItemLadder.class;
            Item.list[66] = ItemRail.class;
            Item.list[81] = ItemCactus.class;
            Item.list[256] = ItemShovelIron.class;
            Item.list[257] = ItemPickaxeIron.class;
            Item.list[258] = ItemAxeIron.class;
            Item.list[259] = ItemFlintSteel.class;
            Item.list[260] = ItemApple.class;
            Item.list[261] = ItemBow.class;
            Item.list[262] = ItemArrow.class;
            Item.list[263] = ItemCoal.class;
            Item.list[264] = ItemDiamond.class;
            Item.list[265] = ItemIngotIron.class;
            Item.list[266] = ItemIngotGold.class;
            Item.list[267] = ItemSwordIron.class;
            Item.list[268] = ItemSwordWood.class;
            Item.list[269] = ItemShovelWood.class;
            Item.list[270] = ItemPickaxeWood.class;
            Item.list[271] = ItemAxeWood.class;
            Item.list[272] = ItemSwordStone.class;
            Item.list[273] = ItemShovelStone.class;
            Item.list[274] = ItemPickaxeStone.class;
            Item.list[275] = ItemAxeStone.class;
            Item.list[276] = ItemSwordDiamond.class;
            Item.list[277] = ItemShovelDiamond.class;
            Item.list[278] = ItemPickaxeDiamond.class;
            Item.list[279] = ItemAxeDiamond.class;
            Item.list[280] = ItemStick.class;
            Item.list[281] = ItemBowl.class;
            Item.list[282] = ItemMushroomStew.class;
            Item.list[283] = ItemSwordGold.class;
            Item.list[284] = ItemShovelGold.class;
            Item.list[285] = ItemPickaxeGold.class;
            Item.list[286] = ItemAxeGold.class;
            Item.list[287] = ItemString.class;
            Item.list[288] = ItemFeather.class;
            Item.list[289] = ItemGunpowder.class;
            Item.list[290] = ItemHoeWood.class;
            Item.list[291] = ItemHoeStone.class;
            Item.list[292] = ItemHoeIron.class;
            Item.list[293] = ItemHoeDiamond.class;
            Item.list[294] = ItemHoeGold.class;
            Item.list[295] = ItemSeedsWheat.class;
            Item.list[296] = ItemWheat.class;
            Item.list[297] = ItemBread.class;
            Item.list[298] = ItemHelmetLeather.class;
            Item.list[299] = ItemChestplateLeather.class;
            Item.list[300] = ItemLeggingsLeather.class;
            Item.list[301] = ItemBootsLeather.class;
            Item.list[302] = ItemHelmetChain.class;
            Item.list[303] = ItemChestplateChain.class;
            Item.list[304] = ItemLeggingsChain.class;
            Item.list[305] = ItemBootsChain.class;
            Item.list[306] = ItemHelmetIron.class;
            Item.list[307] = ItemChestplateIron.class;
            Item.list[308] = ItemLeggingsIron.class;
            Item.list[309] = ItemBootsIron.class;
            Item.list[310] = ItemHelmetDiamond.class;
            Item.list[311] = ItemChestplateDiamond.class;
            Item.list[312] = ItemLeggingsDiamond.class;
            Item.list[313] = ItemBootsDiamond.class;
            Item.list[314] = ItemHelmetGold.class;
            Item.list[315] = ItemChestplateGold.class;
            Item.list[316] = ItemLeggingsGold.class;
            Item.list[317] = ItemBootsGold.class;
            Item.list[318] = ItemFlint.class;
            Item.list[319] = ItemPorkchopRaw.class;
            Item.list[320] = ItemPorkchopCooked.class;
            Item.list[321] = ItemPainting.class;
            Item.list[322] = ItemAppleGold.class;
            Item.list[323] = ItemSign.class;
            Item.list[324] = ItemDoorWood.class;
            Item.list[325] = ItemBucket.class;
            Item.list[328] = ItemMinecart.class;
            Item.list[329] = ItemSaddle.class;
            Item.list[330] = ItemDoorIron.class;
            Item.list[331] = ItemRedstone.class;
            Item.list[332] = ItemSnowball.class;
            Item.list[333] = ItemBoat.class;
            Item.list[334] = ItemLeather.class;
            Item.list[335] = ItemKelp.class;
            Item.list[336] = ItemBrick.class;
            Item.list[337] = ItemClay.class;
            Item.list[338] = ItemSugarcane.class;
            Item.list[339] = ItemPaper.class;
            Item.list[340] = ItemBook.class;
            Item.list[341] = ItemSlimeball.class;
            Item.list[342] = ItemMinecartChest.class;
            Item.list[344] = ItemEgg.class;
            Item.list[345] = ItemCompass.class;
            Item.list[346] = ItemFishingRod.class;
            Item.list[347] = ItemClock.class;
            Item.list[348] = ItemGlowstoneDust.class;
            Item.list[349] = ItemFish.class;
            Item.list[350] = ItemFishCooked.class;
            Item.list[351] = ItemDye.class;
            Item.list[352] = ItemBone.class;
            Item.list[353] = ItemSugar.class;
            Item.list[354] = ItemCake.class;
            Item.list[355] = ItemBed.class;
            Item.list[356] = ItemRedstoneRepeater.class;
            Item.list[357] = ItemCookie.class;
            Item.list[358] = ItemMap.class;
            Item.list[359] = ItemShears.class;
            Item.list[360] = ItemMelon.class;
            Item.list[361] = ItemSeedsPumpkin.class;
            Item.list[362] = ItemSeedsMelon.class;
            Item.list[363] = ItemBeefRaw.class;
            Item.list[364] = ItemSteak.class;
            Item.list[365] = ItemChickenRaw.class;
            Item.list[366] = ItemChickenCooked.class;
            Item.list[367] = ItemRottenFlesh.class;
            Item.list[368] = ItemEnderPearl.class;
            Item.list[369] = ItemBlazeRod.class;
            Item.list[370] = ItemGhastTear.class;
            Item.list[371] = ItemNuggetGold.class;
            Item.list[372] = ItemNetherWart.class;
            Item.list[373] = ItemPotion.class;
            Item.list[374] = ItemGlassBottle.class;
            Item.list[375] = ItemSpiderEye.class;
            Item.list[376] = ItemSpiderEyeFermented.class;
            Item.list[377] = ItemBlazePowder.class;
            Item.list[378] = ItemMagmaCream.class;
            Item.list[379] = ItemBrewingStand.class;
            Item.list[380] = ItemCauldron.class;
            Item.list[381] = ItemEnderEye.class;
            Item.list[382] = ItemMelonGlistering.class;
            Item.list[383] = ItemSpawnEgg.class;
            Item.list[384] = ItemExpBottle.class;
            Item.list[385] = ItemFireCharge.class;
            Item.list[386] = ItemBookAndQuill.class;
            Item.list[387] = ItemBookWritten.class;
            Item.list[388] = ItemEmerald.class;
            Item.list[389] = ItemItemFrame.class;
            Item.list[390] = ItemFlowerPot.class;
            Item.list[391] = ItemCarrot.class;
            Item.list[392] = ItemPotato.class;
            Item.list[393] = ItemPotatoBaked.class;
            Item.list[394] = ItemPotatoPoisonous.class;
            Item.list[395] = ItemEmptyMap.class;
            Item.list[396] = ItemCarrotGolden.class;
            Item.list[397] = ItemSkull.class;
            Item.list[398] = ItemCarrotOnAStick.class;
            Item.list[399] = ItemNetherStar.class;
            Item.list[400] = ItemPumpkinPie.class;
            Item.list[401] = ItemFirework.class;
            Item.list[402] = ItemFireworkStar.class;
            Item.list[403] = ItemBookEnchanted.class;
            Item.list[404] = ItemRedstoneComparator.class;
            Item.list[405] = ItemNetherBrick.class;
            Item.list[406] = ItemQuartz.class;
            Item.list[407] = ItemMinecartTNT.class;
            Item.list[408] = ItemMinecartHopper.class;
            Item.list[409] = ItemPrismarineShard.class;
            Item.list[410] = ItemHopper.class;
            Item.list[411] = ItemRabbitRaw.class;
            Item.list[412] = ItemRabbitCooked.class;
            Item.list[413] = ItemRabbitStew.class;
            Item.list[414] = ItemRabbitFoot.class;
            Item.list[415] = ItemRabbitHide.class;
            Item.list[416] = ItemHorseArmorLeather.class;
            Item.list[417] = ItemHorseArmorIron.class;
            Item.list[418] = ItemHorseArmorGold.class;
            Item.list[419] = ItemHorseArmorDiamond.class;
            Item.list[420] = ItemLead.class;
            Item.list[421] = ItemNameTag.class;
            Item.list[422] = ItemPrismarineCrystals.class;
            Item.list[423] = ItemMuttonRaw.class;
            Item.list[424] = ItemMuttonCooked.class;
            Item.list[425] = ItemArmorStand.class;
            Item.list[426] = ItemEndCrystal.class;
            Item.list[427] = ItemDoorSpruce.class;
            Item.list[428] = ItemDoorBirch.class;
            Item.list[429] = ItemDoorJungle.class;
            Item.list[430] = ItemDoorAcacia.class;
            Item.list[431] = ItemDoorDarkOak.class;
            Item.list[432] = ItemChorusFruit.class;
            Item.list[433] = ItemChorusFruitPopped.class;
            Item.list[434] = ItemBannerPattern.class;
            Item.list[437] = ItemDragonBreath.class;
            Item.list[438] = ItemPotionSplash.class;
            Item.list[441] = ItemPotionLingering.class;
            Item.list[444] = ItemElytra.class;
            Item.list[445] = ItemShulkerShell.class;
            Item.list[446] = ItemBanner.class;
            Item.list[450] = ItemTotem.class;
            Item.list[452] = ItemNuggetIron.class;
            Item.list[455] = ItemTrident.class;
            Item.list[457] = ItemBeetroot.class;
            Item.list[458] = ItemSeedsBeetroot.class;
            Item.list[459] = ItemBeetrootSoup.class;
            Item.list[460] = ItemSalmon.class;
            Item.list[461] = ItemClownfish.class;
            Item.list[462] = ItemPufferfish.class;
            Item.list[463] = ItemSalmonCooked.class;
            Item.list[464] = ItemDriedKelp.class;
            Item.list[465] = ItemNautilusShell.class;
            Item.list[466] = ItemAppleGoldEnchanted.class;
            Item.list[467] = ItemHeartOfTheSea.class;
            Item.list[468] = ItemScute.class;
            Item.list[469] = ItemTurtleShell.class;
            Item.list[470] = ItemPhantomMembrane.class;
            Item.list[471] = ItemCrossbow.class;
            Item.list[472] = ItemSignSpruce.class;
            Item.list[473] = ItemSignBirch.class;
            Item.list[474] = ItemSignJungle.class;
            Item.list[475] = ItemSignAcacia.class;
            Item.list[476] = ItemSignDarkOak.class;
            Item.list[477] = ItemSweetBerries.class;
            Item.list[510] = ItemRecord11.class;
            Item.list[501] = ItemRecordCat.class;
            Item.list[500] = ItemRecord13.class;
            Item.list[502] = ItemRecordBlocks.class;
            Item.list[503] = ItemRecordChirp.class;
            Item.list[504] = ItemRecordFar.class;
            Item.list[509] = ItemRecordWard.class;
            Item.list[505] = ItemRecordMall.class;
            Item.list[506] = ItemRecordMellohi.class;
            Item.list[507] = ItemRecordStal.class;
            Item.list[508] = ItemRecordStrad.class;
            Item.list[511] = ItemRecordWait.class;
            Item.list[513] = ItemShield.class;
            Item.list[636] = ItemRecord5.class;
            Item.list[637] = ItemDiscFragment5.class;
            Item.list[638] = ItemChestBoatOak.class;
            Item.list[639] = ItemChestBoatBirch.class;
            Item.list[640] = ItemChestBoatJungle.class;
            Item.list[641] = ItemChestBoatSpruce.class;
            Item.list[642] = ItemChestBoatAcacia.class;
            Item.list[643] = ItemChestBoatDarkOak.class;
            Item.list[644] = ItemChestBoatMangrove.class;
            Item.list[720] = ItemCampfire.class;
            Item.list[734] = ItemSuspiciousStew.class;
            Item.list[736] = ItemHoneycomb.class;
            Item.list[737] = ItemHoneyBottle.class;
            Item.list[741] = ItemLodestoneCompass.class;
            Item.list[742] = ItemIngotNetherite.class;
            Item.list[743] = ItemSwordNetherite.class;
            Item.list[744] = ItemShovelNetherite.class;
            Item.list[745] = ItemPickaxeNetherite.class;
            Item.list[746] = ItemAxeNetherite.class;
            Item.list[747] = ItemHoeNetherite.class;
            Item.list[748] = ItemHelmetNetherite.class;
            Item.list[749] = ItemChestplateNetherite.class;
            Item.list[750] = ItemLeggingsNetherite.class;
            Item.list[751] = ItemBootsNetherite.class;
            Item.list[752] = ItemScrapNetherite.class;
            Item.list[757] = ItemWarpedFungusOnAStick.class;
            Item.list[759] = ItemRecordPigstep.class;
            Item.list[772] = ItemSpyglass.class;
            Item.list[773] = ItemRecordOtherside.class;
            for (int k = 0; k < 256; ++k) {
                if (Block.list[k] == null) continue;
                Item.list[k] = Block.list[k];
            }
        }
        Item.clearCreativeItems();
    }

    public static void initCreativeItems() {
        Server.getInstance().getLogger().debug("Loading creative items...");
        Item.a(137);
        Item.a(274);
        Item.a(291);
        Item.a(313);
        Item.a(332);
        Item.a(340);
        Item.a(354);
        Item.a(389);
        for (Map map : new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("creativeitems407.json")).getMapList("items")) {
            try {
                Item item = Item.fromJson(map);
                Item item2 = Item.get(item.getId(), item.getDamage(), item.getCount());
                item2.setCompoundTag(item.getCompoundTag());
                Item.addCreativeItem(407, item2);
            }
            catch (Exception exception) {
                MainLogger.getLogger().logException(exception);
            }
        }
        Item.a(440, 440, s);
        Item.a(448, 448, m);
        Item.a(465, 465, b);
        Item.a(471, 471, u);
        Item.a(475, 475, d);
        Item.a(486, 486, h);
        Item.a(503, 503, f);
        Item.a(527, 527, c);
        Item.a(527, 534, a);
        Item.a(544, 544, e);
        Item.a(560, 560, j);
    }

    private static void a(int n) {
        for (Map map : new Config(2).loadFromStream(Server.class.getClassLoader().getResourceAsStream("creativeitems" + n + ".json")).getMapList("items")) {
            try {
                Item.addCreativeItem(n, Item.fromJson(map));
            }
            catch (Exception exception) {
                MainLogger.getLogger().logException(exception);
            }
        }
    }

    private static void a(int n, int n2, List<Item> list) {
        JsonArray jsonArray;
        if (Nukkit.DEBUG > 1 && !list.isEmpty()) {
            Server.getInstance().getLogger().warning("registerCreativeItemsNew: creativeItems is not empty! Is wrong list used?");
        }
        try {
            InputStream inputStream = Server.class.getClassLoader().getResourceAsStream("creativeitems" + n + ".json");
            Object object = null;
            try {
                jsonArray = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject().getAsJsonArray("items");
            }
            catch (Throwable throwable) {
                object = throwable;
                throw throwable;
            }
            finally {
                if (inputStream != null) {
                    if (object != null) {
                        try {
                            inputStream.close();
                        }
                        catch (Throwable throwable) {
                            ((Throwable)object).addSuppressed(throwable);
                        }
                    } else {
                        inputStream.close();
                    }
                }
            }
        }
        catch (Exception exception) {
            throw new AssertionError("Error loading required block states!", exception);
        }
        for (Object object : jsonArray) {
            Item item = RuntimeItems.getMapping(n).parseCreativeItem(((JsonElement)object).getAsJsonObject(), true, n2);
            if (item == null || item.getName().equals("Unknown")) continue;
            list.add(item.clone());
        }
    }

    public static void clearCreativeItems() {
        v.clear();
        n.clear();
        l.clear();
        k.clear();
        r.clear();
        q.clear();
        p.clear();
        o.clear();
        t.clear();
        s.clear();
        m.clear();
        b.clear();
        u.clear();
        d.clear();
        h.clear();
        f.clear();
        c.clear();
        a.clear();
        e.clear();
        j.clear();
    }

    public static ArrayList<Item> getCreativeItems() {
        Server.mvw("Item#getCreativeItems()");
        return Item.getCreativeItems(560);
    }

    public static ArrayList<Item> getCreativeItems(int n) {
        switch (n) {
            case 137: 
            case 140: 
            case 141: 
            case 150: 
            case 160: 
            case 201: 
            case 223: 
            case 224: 
            case 261: {
                return new ArrayList<Item>(v);
            }
            case 274: {
                return new ArrayList<Item>(Item.n);
            }
            case 281: 
            case 282: 
            case 291: {
                return new ArrayList<Item>(l);
            }
            case 313: {
                return new ArrayList<Item>(k);
            }
            case 332: {
                return new ArrayList<Item>(r);
            }
            case 340: {
                return new ArrayList<Item>(q);
            }
            case 354: 
            case 361: 
            case 388: {
                return new ArrayList<Item>(p);
            }
            case 389: 
            case 390: {
                return new ArrayList<Item>(o);
            }
            case 407: 
            case 408: 
            case 409: 
            case 410: 
            case 411: 
            case 419: 
            case 420: 
            case 422: 
            case 423: 
            case 424: 
            case 428: 
            case 431: 
            case 433: 
            case 434: 
            case 435: {
                return new ArrayList<Item>(t);
            }
            case 440: {
                return new ArrayList<Item>(s);
            }
            case 448: 
            case 453: {
                return new ArrayList<Item>(m);
            }
            case 465: {
                return new ArrayList<Item>(b);
            }
            case 471: 
            case 474: 
            case 476: {
                return new ArrayList<Item>(u);
            }
            case 475: {
                return new ArrayList<Item>(d);
            }
            case 485: 
            case 486: {
                return new ArrayList<Item>(h);
            }
            case 503: {
                return new ArrayList<Item>(f);
            }
            case 524: 
            case 526: 
            case 527: {
                return new ArrayList<Item>(c);
            }
            case 534: {
                return new ArrayList<Item>(a);
            }
            case 544: 
            case 545: 
            case 553: 
            case 554: 
            case 557: 
            case 558: {
                return new ArrayList<Item>(e);
            }
            case 560: {
                return new ArrayList<Item>(j);
            }
        }
        throw new IllegalArgumentException("Tried to get creative items for unsupported protocol version: " + n);
    }

    public static void addCreativeItem(Item item) {
        Server.mvw("Item#addCreativeItem(Item)");
        Item.addCreativeItem(560, item);
    }

    public static void addCreativeItem(int n, Item item) {
        switch (n) {
            case 137: {
                v.add(item.clone());
            }
            case 274: {
                Item.n.add(item.clone());
            }
            case 291: {
                l.add(item.clone());
                break;
            }
            case 313: {
                k.add(item.clone());
                break;
            }
            case 332: {
                r.add(item.clone());
                break;
            }
            case 340: {
                q.add(item.clone());
                break;
            }
            case 354: {
                p.add(item.clone());
                break;
            }
            case 389: {
                o.add(item.clone());
                break;
            }
            case 407: {
                t.add(item.clone());
                break;
            }
            case 440: {
                s.add(item.clone());
                break;
            }
            case 448: {
                m.add(item.clone());
                break;
            }
            case 465: {
                b.add(item.clone());
                break;
            }
            case 471: {
                u.add(item.clone());
                break;
            }
            case 475: {
                d.add(item.clone());
                break;
            }
            case 486: {
                h.add(item.clone());
                break;
            }
            case 503: {
                f.add(item.clone());
                break;
            }
            case 527: {
                c.add(item.clone());
                break;
            }
            case 534: {
                a.add(item.clone());
                break;
            }
            case 544: {
                e.add(item.clone());
                break;
            }
            case 560: {
                j.add(item.clone());
                break;
            }
            default: {
                throw new IllegalArgumentException("Tried to register creative items for unsupported protocol version: " + n + " (Supported: 131, 274, 291, 313, 332, 340, 345, 389, 407, 440, 448, 465, 471, 475, 486, 503, 527, 534, 544, 560)");
            }
        }
    }

    public static void removeCreativeItem(Item item) {
        Server.mvw("Item#removeCreativeItem(Item)");
        Item.removeCreativeItem(560, item);
    }

    public static void removeCreativeItem(int n, Item item) {
        block0: {
            int n2 = Item.getCreativeItemIndex(n, item);
            if (n2 == -1) break block0;
            Item.getCreativeItems(n).remove(n2);
        }
    }

    public static boolean isCreativeItem(Item item) {
        Server.mvw("Item#isCreativeItem(Item)");
        return Item.isCreativeItem(560, item);
    }

    public static boolean isCreativeItem(int n, Item item) {
        for (Item item2 : Item.getCreativeItems(n)) {
            if (!item.equals(item2, !item.isTool())) continue;
            return true;
        }
        return false;
    }

    public static Item getCreativeItem(int n) {
        Server.mvw("Item#getCreativeItem(int)");
        return Item.getCreativeItem(560, n);
    }

    public static Item getCreativeItem(int n, int n2) {
        ArrayList<Item> arrayList = Item.getCreativeItems(n);
        return n2 >= 0 && n2 < arrayList.size() ? arrayList.get(n2) : null;
    }

    public static int getCreativeItemIndex(Item item) {
        Server.mvw("Item#getCreativeItemIndex(Item)");
        return Item.getCreativeItemIndex(560, item);
    }

    public static int getCreativeItemIndex(int n, Item item) {
        ArrayList<Item> arrayList = Item.getCreativeItems(n);
        for (int k = 0; k < arrayList.size(); ++k) {
            if (!item.equals(arrayList.get(k), !item.isTool())) continue;
            return k;
        }
        return -1;
    }

    public static Item get(int n) {
        return Item.get(n, 0);
    }

    public static Item get(int n, Integer n2) {
        return Item.get(n, n2, 1);
    }

    public static Item get(int n, Integer n2, int n3) {
        return Item.get(n, n2, n3, new byte[0]);
    }

    public static Item get(int n, Integer n2, int n3, byte[] byArray) {
        try {
            Class clazz;
            if (n < 0) {
                int n4 = 255 - n;
                clazz = Block.list[n4];
            } else {
                clazz = list[n];
            }
            Item item = clazz == null ? new Item(n, n2, n3) : (n < 256 && n != 166 ? (n2 >= 0 ? new ItemBlock(Block.get(n, n2), n2, n3) : new ItemBlock(Block.get(n), n2, n3)) : (Item)clazz.getConstructor(Integer.class, Integer.TYPE).newInstance(n2, n3));
            if (byArray.length != 0) {
                item.setCompoundTag(byArray);
            }
            return item.initItem();
        }
        catch (Exception exception) {
            return new Item(n, n2, n3).setCompoundTag(byArray).initItem();
        }
    }

    public static Item getSaved(int n, Integer n2, int n3, Tag tag) {
        try {
            Class clazz;
            if (n < 0) {
                int n4 = 255 - n;
                clazz = Block.list[n4];
            } else {
                clazz = list[n];
            }
            Item item = clazz == null ? new Item(n, n2, n3) : (n < 256 && n != 166 ? (n2 >= 0 ? new ItemBlock(Block.get(n, n2), n2, n3) : new ItemBlock(Block.get(n), n2, n3)) : (Item)clazz.getConstructor(Integer.class, Integer.TYPE).newInstance(n2, n3));
            if (item.count > item.getMaxStackSize()) {
                item.count = item.getMaxStackSize();
                if (tag instanceof CompoundTag) {
                    ((CompoundTag)tag).putByte("Count", item.getMaxStackSize());
                }
            }
            if (tag instanceof CompoundTag) {
                ListTag<? extends Tag> listTag;
                if ((item.id == 218 || item.id == 205) && (listTag = ((CompoundTag)tag).getList("Items")) != null) {
                    for (CompoundTag compoundTag : listTag.getAll()) {
                        int n5 = compoundTag.getShort("id");
                        if (n5 != 218 && n5 != 205) continue;
                        ((CompoundTag)tag).remove("Items");
                        break;
                    }
                }
                item.setCompoundTag((CompoundTag)tag);
            }
            return item.initItem();
        }
        catch (Exception exception) {
            Item item;
            block9: {
                item = new Item(n, n2, n3);
                if (!(tag instanceof CompoundTag)) break block9;
                item.setCompoundTag((CompoundTag)tag);
            }
            return item.initItem();
        }
    }

    public static Item fromString(String string) {
        String[] stringArray = string.trim().replace(' ', '_').replace("minecraft:", "").split(":");
        int n = 0;
        int n2 = 0;
        Pattern pattern = Pattern.compile("^[-1-9]\\d*$");
        if (pattern.matcher(stringArray[0]).matches()) {
            n = Integer.parseInt(stringArray[0]);
        } else {
            try {
                n = BlockID.class.getField(stringArray[0].toUpperCase()).getInt(null);
                if (n > 255) {
                    n = 255 - n;
                }
            }
            catch (Exception exception) {
                try {
                    n = ItemID.class.getField(stringArray[0].toUpperCase()).getInt(null);
                }
                catch (Exception exception2) {
                    // empty catch block
                }
            }
        }
        if (stringArray.length != 1) {
            n2 = Integer.parseInt(stringArray[1]) & 0xFFFF;
        }
        return Item.get(n, n2);
    }

    public static Item fromJson(Map<String, Object> map) {
        String string = (String)map.get("nbt_b64");
        byte[] byArray = string != null ? Base64.getDecoder().decode(string) : ((string = (String)map.getOrDefault("nbt_hex", null)) == null ? new byte[]{} : Utils.parseHexBinary(string));
        return Item.get(Utils.toInt(map.get("id")), Utils.toInt(map.getOrDefault("damage", 0)), Utils.toInt(map.getOrDefault("count", 1)), byArray);
    }

    public static Item fromJsonOld(Map<String, Object> map) {
        String string = (String)map.getOrDefault("nbt_hex", "");
        return Item.get(Utils.toInt(map.get("id")), Utils.toInt(map.getOrDefault("damage", 0)), Utils.toInt(map.getOrDefault("count", 1)), string.isEmpty() ? new byte[]{} : Utils.parseHexBinary(string));
    }

    public static Item[] fromStringMultiple(String string) {
        String[] stringArray = string.split(",");
        Item[] itemArray = new Item[stringArray.length - 1];
        for (int k = 0; k < stringArray.length; ++k) {
            itemArray[k] = Item.fromString(stringArray[k]);
        }
        return itemArray;
    }

    public Item setCompoundTag(CompoundTag compoundTag) {
        this.setNamedTag(compoundTag);
        return this;
    }

    public Item setCompoundTag(byte[] byArray) {
        this.i = byArray;
        this.w = null;
        return this;
    }

    public byte[] getCompoundTag() {
        return this.i;
    }

    public boolean hasCompoundTag() {
        return this.i != null && this.i.length > 0;
    }

    public boolean hasCustomBlockData() {
        if (!this.hasCompoundTag()) {
            return false;
        }
        CompoundTag compoundTag = this.getNamedTag();
        return compoundTag.contains("BlockEntityTag") && compoundTag.get("BlockEntityTag") instanceof CompoundTag;
    }

    public Item clearCustomBlockData() {
        block1: {
            if (!this.hasCompoundTag()) {
                return this;
            }
            CompoundTag compoundTag = this.getNamedTag();
            if (!compoundTag.contains("BlockEntityTag") || !(compoundTag.get("BlockEntityTag") instanceof CompoundTag)) break block1;
            compoundTag.remove("BlockEntityTag");
            this.setNamedTag(compoundTag);
        }
        return this;
    }

    public Item setCustomBlockData(CompoundTag compoundTag) {
        CompoundTag compoundTag2 = compoundTag.copy();
        compoundTag2.setName("BlockEntityTag");
        CompoundTag compoundTag3 = !this.hasCompoundTag() ? new CompoundTag() : this.getNamedTag();
        compoundTag3.putCompound("BlockEntityTag", compoundTag2);
        this.setNamedTag(compoundTag3);
        return this;
    }

    public CompoundTag getCustomBlockData() {
        Tag tag;
        if (!this.hasCompoundTag()) {
            return null;
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag.contains("BlockEntityTag") && (tag = compoundTag.get("BlockEntityTag")) instanceof CompoundTag) {
            return (CompoundTag)tag;
        }
        return null;
    }

    public boolean hasEnchantments() {
        if (!this.hasCompoundTag()) {
            return false;
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag.contains("ench")) {
            Tag tag = compoundTag.get("ench");
            return tag instanceof ListTag;
        }
        return false;
    }

    public Enchantment getEnchantment(int n) {
        return this.getEnchantment((short)(n & 0xFFFF));
    }

    public Enchantment getEnchantment(short s2) {
        if (!this.hasEnchantments()) {
            return null;
        }
        for (CompoundTag compoundTag : this.getNamedTag().getList("ench", CompoundTag.class).getAll()) {
            Enchantment enchantment;
            if (compoundTag.getShort("id") != s2 || (enchantment = Enchantment.getEnchantment(compoundTag.getShort("id"))) == null) continue;
            enchantment.setLevel(compoundTag.getShort("lvl"));
            return enchantment;
        }
        return null;
    }

    public void addEnchantment(Enchantment ... enchantmentArray) {
        ListTag<Object> listTag;
        CompoundTag compoundTag = !this.hasCompoundTag() ? new CompoundTag() : this.getNamedTag();
        if (!compoundTag.contains("ench")) {
            listTag = new ListTag("ench");
            compoundTag.putList(listTag);
        } else {
            listTag = compoundTag.getList("ench", CompoundTag.class);
        }
        for (Enchantment enchantment : enchantmentArray) {
            boolean bl = false;
            for (int k = 0; k < listTag.size(); ++k) {
                CompoundTag compoundTag2 = (CompoundTag)listTag.get(k);
                if (compoundTag2.getShort("id") != enchantment.getId()) continue;
                listTag.add(k, new CompoundTag().putShort("id", enchantment.getId()).putShort("lvl", enchantment.getLevel()));
                bl = true;
                break;
            }
            if (bl) continue;
            listTag.add(new CompoundTag().putShort("id", enchantment.getId()).putShort("lvl", enchantment.getLevel()));
        }
        this.setNamedTag(compoundTag);
    }

    public Enchantment[] getEnchantments() {
        if (!this.hasEnchantments()) {
            return new Enchantment[0];
        }
        ArrayList<Enchantment> arrayList = new ArrayList<Enchantment>();
        ListTag<CompoundTag> listTag = this.getNamedTag().getList("ench", CompoundTag.class);
        for (CompoundTag compoundTag : listTag.getAll()) {
            Enchantment enchantment = Enchantment.getEnchantment(compoundTag.getShort("id"));
            if (enchantment == null) continue;
            enchantment.setLevel(compoundTag.getShort("lvl"));
            arrayList.add(enchantment);
        }
        return arrayList.toArray(new Enchantment[0]);
    }

    public boolean hasEnchantment(int n) {
        Enchantment enchantment = this.getEnchantment(n);
        return enchantment != null && enchantment.getLevel() > 0;
    }

    public boolean hasEnchantment(short s2) {
        return this.getEnchantment(s2) != null;
    }

    public int getEnchantmentLevel(int n) {
        Enchantment enchantment = this.getEnchantment(n);
        return enchantment == null ? 0 : enchantment.getLevel();
    }

    public boolean hasCustomName() {
        if (!this.hasCompoundTag()) {
            return false;
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag.contains("display")) {
            Tag tag = compoundTag.get("display");
            return tag instanceof CompoundTag && ((CompoundTag)tag).contains("Name") && ((CompoundTag)tag).get("Name") instanceof StringTag;
        }
        return false;
    }

    public String getCustomName() {
        Tag tag;
        if (!this.hasCompoundTag()) {
            return "";
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag.contains("display") && (tag = compoundTag.get("display")) instanceof CompoundTag && ((CompoundTag)tag).contains("Name") && ((CompoundTag)tag).get("Name") instanceof StringTag) {
            return ((CompoundTag)tag).getString("Name");
        }
        return "";
    }

    public Item setCustomName(String string) {
        CompoundTag compoundTag;
        if (string == null || string.isEmpty()) {
            this.clearCustomName();
            return this;
        }
        if (string.length() > 100) {
            string = string.substring(0, 100);
        }
        if ((compoundTag = !this.hasCompoundTag() ? new CompoundTag() : this.getNamedTag()).contains("display") && compoundTag.get("display") instanceof CompoundTag) {
            compoundTag.getCompound("display").putString("Name", string);
        } else {
            compoundTag.putCompound("display", new CompoundTag("display").putString("Name", string));
        }
        this.setNamedTag(compoundTag);
        return this;
    }

    public Item clearCustomName() {
        if (!this.hasCompoundTag()) {
            return this;
        }
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag.contains("display") && compoundTag.get("display") instanceof CompoundTag) {
            compoundTag.getCompound("display").remove("Name");
            if (compoundTag.getCompound("display").isEmpty()) {
                compoundTag.remove("display");
            }
            this.setNamedTag(compoundTag);
        }
        return this;
    }

    public String[] getLore() {
        CompoundTag compoundTag;
        ListTag<StringTag> listTag;
        Tag tag = this.getNamedTagEntry("display");
        ArrayList<String> arrayList = new ArrayList<String>();
        if (tag instanceof CompoundTag && (listTag = (compoundTag = (CompoundTag)tag).getList("Lore", StringTag.class)).size() > 0) {
            for (StringTag stringTag : listTag.getAll()) {
                arrayList.add(stringTag.data);
            }
        }
        return arrayList.toArray(new String[0]);
    }

    public Item setLore(String ... stringArray) {
        CompoundTag compoundTag = !this.hasCompoundTag() ? new CompoundTag() : this.getNamedTag();
        ListTag<StringTag> listTag = new ListTag<StringTag>("Lore");
        for (String string : stringArray) {
            listTag.add(new StringTag("", string));
        }
        if (!compoundTag.contains("display")) {
            compoundTag.putCompound("display", new CompoundTag("display").putList(listTag));
        } else {
            compoundTag.getCompound("display").putList(listTag);
        }
        this.setNamedTag(compoundTag);
        return this;
    }

    public Tag getNamedTagEntry(String string) {
        CompoundTag compoundTag = this.getNamedTag();
        if (compoundTag != null) {
            return compoundTag.contains(string) ? compoundTag.get(string) : null;
        }
        return null;
    }

    public CompoundTag getNamedTag() {
        if (!this.hasCompoundTag()) {
            return null;
        }
        if (this.w == null) {
            this.w = Item.parseCompoundTag(this.i);
        }
        this.w.setName("");
        return this.w;
    }

    public Item setNamedTag(CompoundTag compoundTag) {
        if (compoundTag.isEmpty()) {
            return this.clearNamedTag();
        }
        compoundTag.setName(null);
        this.w = compoundTag;
        this.i = this.writeCompoundTag(compoundTag);
        return this;
    }

    public Item clearNamedTag() {
        return this.setCompoundTag(new byte[0]);
    }

    public static CompoundTag parseCompoundTag(byte[] byArray) {
        try {
            return NBTIO.read(byArray, ByteOrder.LITTLE_ENDIAN);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public byte[] writeCompoundTag(CompoundTag compoundTag) {
        try {
            compoundTag.setName("");
            return NBTIO.write(compoundTag, ByteOrder.LITTLE_ENDIAN);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int n) {
        this.count = n;
    }

    public boolean isNull() {
        return this.count <= 0 || this.id == 0;
    }

    public final String getName() {
        return this.hasCustomName() ? this.getCustomName() : this.name;
    }

    public final boolean canBePlaced() {
        return this.block != null && this.block.canBePlaced();
    }

    public Block getBlock() {
        if (this.block != null) {
            return this.block.clone();
        }
        return Block.get(0);
    }

    public Block getBlockUnsafe() {
        return this.block;
    }

    public int getBlockId() {
        return this.block == null ? 0 : this.block.getId();
    }

    public int getId() {
        return this.id;
    }

    public int getDamage() {
        return this.meta == 65535 ? 0 : this.meta;
    }

    public void setDamage(Integer n) {
        if (n != null) {
            this.meta = n & 0xFFFF;
        } else {
            this.hasMeta = false;
        }
    }

    public int getMaxStackSize() {
        return 64;
    }

    public final Short getFuelTime() {
        if (!Fuel.duration.containsKey(this.id)) {
            return null;
        }
        if (this.id != 325 || this.meta == 10) {
            return Fuel.duration.get(this.id);
        }
        return null;
    }

    public boolean useOn(Entity entity) {
        return false;
    }

    public boolean useOn(Block block) {
        return false;
    }

    public boolean isTool() {
        return false;
    }

    public int getMaxDurability() {
        return -1;
    }

    public int getTier() {
        return 0;
    }

    public boolean isPickaxe() {
        return false;
    }

    public boolean isAxe() {
        return false;
    }

    public boolean isSword() {
        return false;
    }

    public boolean isShovel() {
        return false;
    }

    public boolean isHoe() {
        return false;
    }

    public boolean isShears() {
        return false;
    }

    public boolean isArmor() {
        return false;
    }

    public boolean isHelmet() {
        return false;
    }

    public boolean canBePutInHelmetSlot() {
        return false;
    }

    public boolean isChestplate() {
        return false;
    }

    public boolean isLeggings() {
        return false;
    }

    public boolean isBoots() {
        return false;
    }

    public int getEnchantAbility() {
        return 0;
    }

    public int getAttackDamage() {
        return 1;
    }

    public int getArmorPoints() {
        return 0;
    }

    public int getToughness() {
        return 0;
    }

    public boolean isUnbreakable() {
        return false;
    }

    public boolean onUse(Player player, int n) {
        return false;
    }

    public boolean onRelease(Player player, int n) {
        return false;
    }

    public final String toString() {
        CompoundTag compoundTag;
        String string = "Item " + this.name + " (" + this.id + ':' + (!this.hasMeta ? "?" : Integer.valueOf(this.meta)) + ")x" + this.count;
        if (Nukkit.DEBUG > 1 && (compoundTag = this.getNamedTag()) != null) {
            string = string + '\n' + compoundTag.toString();
        }
        return string;
    }

    public boolean onActivate(Level level, Player player, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4) {
        return false;
    }

    public boolean onClickAir(Player player, Vector3 vector3) {
        return false;
    }

    public final boolean equals(Object object) {
        return object instanceof Item && this.equals((Item)object, true);
    }

    public final boolean equals(Item item, boolean bl) {
        return this.equals(item, bl, true);
    }

    public final boolean equals(Item item, boolean bl, boolean bl2) {
        if (!(this.id != item.id || bl && this.meta != item.meta)) {
            if (bl2) {
                if (Arrays.equals(this.getCompoundTag(), item.getCompoundTag())) {
                    return true;
                }
                if (this.hasCompoundTag() && item.hasCompoundTag()) {
                    return this.getNamedTag().equals(item.getNamedTag());
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public final boolean equalsExact(Item item) {
        return this.equals(item, true, true) && this.count == item.count;
    }

    public final boolean deepEquals(Item item) {
        return this.equals(item, true);
    }

    public final boolean deepEquals(Item item, boolean bl) {
        return this.equals(item, bl, true);
    }

    public final boolean deepEquals(Item item, boolean bl, boolean bl2) {
        return this.equals(item, bl, bl2);
    }

    public int getRepairCost() {
        Tag tag;
        CompoundTag compoundTag;
        if (this.hasCompoundTag() && (compoundTag = this.getNamedTag()).contains("RepairCost") && (tag = compoundTag.get("RepairCost")) instanceof IntTag) {
            return ((IntTag)tag).data;
        }
        return 0;
    }

    public Item setRepairCost(int n) {
        if (n <= 0 && this.hasCompoundTag()) {
            return this.setNamedTag(this.getNamedTag().remove("RepairCost"));
        }
        CompoundTag compoundTag = !this.hasCompoundTag() ? new CompoundTag() : this.getNamedTag();
        return this.setNamedTag(compoundTag.putInt("RepairCost", n));
    }

    public Item clone() {
        try {
            Item item = (Item)super.clone();
            item.i = (byte[])this.i.clone();
            return item;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public final RuntimeItemMapping.RuntimeEntry getRuntimeEntry() {
        Server.mvw("Item#getRuntimeEntry()");
        return this.getRuntimeEntry(CURRENT_PROTOCOL);
    }

    public final RuntimeItemMapping.RuntimeEntry getRuntimeEntry(int n) {
        return RuntimeItems.getMapping(n).toRuntime(this.getId(), this.getDamage());
    }

    public final int getNetworkId() {
        Server.mvw("Item#getNetworkId()");
        return this.getNetworkId(CURRENT_PROTOCOL);
    }

    public final int getNetworkId(int n) {
        if (n < 419) {
            return this.getId();
        }
        return this.getRuntimeEntry(n).getRuntimeId();
    }

    public Item initItem() {
        return this;
    }

    public boolean isSupportedOn(int n) {
        return true;
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

