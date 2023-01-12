/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockAcaciaSignStanding;
import cn.nukkit.block.BlockAcaciaWallSign;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockAnvil;
import cn.nukkit.block.BlockBamboo;
import cn.nukkit.block.BlockBambooSapling;
import cn.nukkit.block.BlockBanner;
import cn.nukkit.block.BlockBarrel;
import cn.nukkit.block.BlockBarrier;
import cn.nukkit.block.BlockBeacon;
import cn.nukkit.block.BlockBed;
import cn.nukkit.block.BlockBedrock;
import cn.nukkit.block.BlockBedrockInvisible;
import cn.nukkit.block.BlockBeeNest;
import cn.nukkit.block.BlockBeehive;
import cn.nukkit.block.BlockBeetroot;
import cn.nukkit.block.BlockBell;
import cn.nukkit.block.BlockBirchSignStanding;
import cn.nukkit.block.BlockBirchWallSign;
import cn.nukkit.block.BlockBlastFurnace;
import cn.nukkit.block.BlockBlastFurnaceLit;
import cn.nukkit.block.BlockBlueIce;
import cn.nukkit.block.BlockBone;
import cn.nukkit.block.BlockBookshelf;
import cn.nukkit.block.BlockBrewingStand;
import cn.nukkit.block.BlockBricks;
import cn.nukkit.block.BlockBricksEndStone;
import cn.nukkit.block.BlockBricksNether;
import cn.nukkit.block.BlockBricksRedNether;
import cn.nukkit.block.BlockBricksStone;
import cn.nukkit.block.BlockBubbleColumn;
import cn.nukkit.block.BlockButtonAcacia;
import cn.nukkit.block.BlockButtonBirch;
import cn.nukkit.block.BlockButtonDarkOak;
import cn.nukkit.block.BlockButtonJungle;
import cn.nukkit.block.BlockButtonSpruce;
import cn.nukkit.block.BlockButtonStone;
import cn.nukkit.block.BlockButtonWooden;
import cn.nukkit.block.BlockCactus;
import cn.nukkit.block.BlockCake;
import cn.nukkit.block.BlockCampfire;
import cn.nukkit.block.BlockCarpet;
import cn.nukkit.block.BlockCarrot;
import cn.nukkit.block.BlockCartographyTable;
import cn.nukkit.block.BlockCauldron;
import cn.nukkit.block.BlockCauldronLava;
import cn.nukkit.block.BlockChemicalHeat;
import cn.nukkit.block.BlockChemistryTable;
import cn.nukkit.block.BlockChest;
import cn.nukkit.block.BlockChorusFlower;
import cn.nukkit.block.BlockChorusPlant;
import cn.nukkit.block.BlockClay;
import cn.nukkit.block.BlockCoal;
import cn.nukkit.block.BlockCobblestone;
import cn.nukkit.block.BlockCobweb;
import cn.nukkit.block.BlockCocoa;
import cn.nukkit.block.BlockColoredTorchBP;
import cn.nukkit.block.BlockColoredTorchRG;
import cn.nukkit.block.BlockCommandBlock;
import cn.nukkit.block.BlockCommandBlockChain;
import cn.nukkit.block.BlockCommandBlockRepeating;
import cn.nukkit.block.BlockComposter;
import cn.nukkit.block.BlockConcrete;
import cn.nukkit.block.BlockConcretePowder;
import cn.nukkit.block.BlockConduit;
import cn.nukkit.block.BlockCoral;
import cn.nukkit.block.BlockCoralBlock;
import cn.nukkit.block.BlockCoralFan;
import cn.nukkit.block.BlockCoralFanDead;
import cn.nukkit.block.BlockCoralFanHang;
import cn.nukkit.block.BlockCoralFanHang2;
import cn.nukkit.block.BlockCoralFanHang3;
import cn.nukkit.block.BlockCraftingTable;
import cn.nukkit.block.BlockDandelion;
import cn.nukkit.block.BlockDarkOakSignStanding;
import cn.nukkit.block.BlockDarkOakWallSign;
import cn.nukkit.block.BlockDaylightDetector;
import cn.nukkit.block.BlockDaylightDetectorInverted;
import cn.nukkit.block.BlockDeadBush;
import cn.nukkit.block.BlockDiamond;
import cn.nukkit.block.BlockDirt;
import cn.nukkit.block.BlockDispenser;
import cn.nukkit.block.BlockDoorAcacia;
import cn.nukkit.block.BlockDoorBirch;
import cn.nukkit.block.BlockDoorDarkOak;
import cn.nukkit.block.BlockDoorIron;
import cn.nukkit.block.BlockDoorJungle;
import cn.nukkit.block.BlockDoorSpruce;
import cn.nukkit.block.BlockDoorWood;
import cn.nukkit.block.BlockDoublePlant;
import cn.nukkit.block.BlockDoubleSlabRedSandstone;
import cn.nukkit.block.BlockDoubleSlabStone;
import cn.nukkit.block.BlockDoubleSlabStone3;
import cn.nukkit.block.BlockDoubleSlabStone4;
import cn.nukkit.block.BlockDoubleSlabWood;
import cn.nukkit.block.BlockDragonEgg;
import cn.nukkit.block.BlockDriedKelpBlock;
import cn.nukkit.block.BlockDropper;
import cn.nukkit.block.BlockEmerald;
import cn.nukkit.block.BlockEnchantingTable;
import cn.nukkit.block.BlockEndGateway;
import cn.nukkit.block.BlockEndPortal;
import cn.nukkit.block.BlockEndPortalFrame;
import cn.nukkit.block.BlockEndRod;
import cn.nukkit.block.BlockEndStone;
import cn.nukkit.block.BlockEnderChest;
import cn.nukkit.block.BlockFarmland;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockFenceGate;
import cn.nukkit.block.BlockFenceGateAcacia;
import cn.nukkit.block.BlockFenceGateBirch;
import cn.nukkit.block.BlockFenceGateDarkOak;
import cn.nukkit.block.BlockFenceGateJungle;
import cn.nukkit.block.BlockFenceGateSpruce;
import cn.nukkit.block.BlockFenceNetherBrick;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockFletchingTable;
import cn.nukkit.block.BlockFlower;
import cn.nukkit.block.BlockFlowerPot;
import cn.nukkit.block.BlockFurnace;
import cn.nukkit.block.BlockFurnaceBurning;
import cn.nukkit.block.BlockGlass;
import cn.nukkit.block.BlockGlassPane;
import cn.nukkit.block.BlockGlassPaneStained;
import cn.nukkit.block.BlockGlassStained;
import cn.nukkit.block.BlockGlowStick;
import cn.nukkit.block.BlockGlowstone;
import cn.nukkit.block.BlockGold;
import cn.nukkit.block.BlockGrass;
import cn.nukkit.block.BlockGrassPath;
import cn.nukkit.block.BlockGravel;
import cn.nukkit.block.BlockGrindstone;
import cn.nukkit.block.BlockHardGlass;
import cn.nukkit.block.BlockHardGlassPane;
import cn.nukkit.block.BlockHardGlassPaneStained;
import cn.nukkit.block.BlockHardGlassStained;
import cn.nukkit.block.BlockHayBale;
import cn.nukkit.block.BlockHoneyBlock;
import cn.nukkit.block.BlockHoneycombBlock;
import cn.nukkit.block.BlockHopper;
import cn.nukkit.block.BlockHugeMushroomBrown;
import cn.nukkit.block.BlockHugeMushroomRed;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockIce;
import cn.nukkit.block.BlockIceFrosted;
import cn.nukkit.block.BlockIcePacked;
import cn.nukkit.block.BlockInfoUpdate;
import cn.nukkit.block.BlockInfoUpdate2;
import cn.nukkit.block.BlockIron;
import cn.nukkit.block.BlockIronBars;
import cn.nukkit.block.BlockItemFrame;
import cn.nukkit.block.BlockJigsaw;
import cn.nukkit.block.BlockJukebox;
import cn.nukkit.block.BlockJungleSignStanding;
import cn.nukkit.block.BlockJungleWallSign;
import cn.nukkit.block.BlockKelp;
import cn.nukkit.block.BlockLadder;
import cn.nukkit.block.BlockLantern;
import cn.nukkit.block.BlockLapis;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockLavaStill;
import cn.nukkit.block.BlockLeaves;
import cn.nukkit.block.BlockLeaves2;
import cn.nukkit.block.BlockLectern;
import cn.nukkit.block.BlockLever;
import cn.nukkit.block.BlockLightBlock;
import cn.nukkit.block.BlockLiquid;
import cn.nukkit.block.BlockLoom;
import cn.nukkit.block.BlockMagma;
import cn.nukkit.block.BlockMelon;
import cn.nukkit.block.BlockMobSpawner;
import cn.nukkit.block.BlockMonsterEgg;
import cn.nukkit.block.BlockMossStone;
import cn.nukkit.block.BlockMushroomBrown;
import cn.nukkit.block.BlockMushroomRed;
import cn.nukkit.block.BlockMycelium;
import cn.nukkit.block.BlockNetherPortal;
import cn.nukkit.block.BlockNetherReactor;
import cn.nukkit.block.BlockNetherWart;
import cn.nukkit.block.BlockNetherWartBlock;
import cn.nukkit.block.BlockNetherrack;
import cn.nukkit.block.BlockNoteblock;
import cn.nukkit.block.BlockObserver;
import cn.nukkit.block.BlockObsidian;
import cn.nukkit.block.BlockObsidianGlowing;
import cn.nukkit.block.BlockOreCoal;
import cn.nukkit.block.BlockOreDiamond;
import cn.nukkit.block.BlockOreEmerald;
import cn.nukkit.block.BlockOreGold;
import cn.nukkit.block.BlockOreIron;
import cn.nukkit.block.BlockOreLapis;
import cn.nukkit.block.BlockOreQuartz;
import cn.nukkit.block.BlockOreRedstone;
import cn.nukkit.block.BlockOreRedstoneGlowing;
import cn.nukkit.block.BlockPiston;
import cn.nukkit.block.BlockPistonExtension;
import cn.nukkit.block.BlockPistonHead;
import cn.nukkit.block.BlockPistonHeadSticky;
import cn.nukkit.block.BlockPistonSticky;
import cn.nukkit.block.BlockPlanks;
import cn.nukkit.block.BlockPodzol;
import cn.nukkit.block.BlockPotato;
import cn.nukkit.block.BlockPressurePlateAcacia;
import cn.nukkit.block.BlockPressurePlateBirch;
import cn.nukkit.block.BlockPressurePlateDarkOak;
import cn.nukkit.block.BlockPressurePlateJungle;
import cn.nukkit.block.BlockPressurePlateSpruce;
import cn.nukkit.block.BlockPressurePlateStone;
import cn.nukkit.block.BlockPressurePlateWood;
import cn.nukkit.block.BlockPrismarine;
import cn.nukkit.block.BlockPumpkin;
import cn.nukkit.block.BlockPumpkinCarved;
import cn.nukkit.block.BlockPumpkinLit;
import cn.nukkit.block.BlockPurpur;
import cn.nukkit.block.BlockQuartz;
import cn.nukkit.block.BlockRail;
import cn.nukkit.block.BlockRailActivator;
import cn.nukkit.block.BlockRailDetector;
import cn.nukkit.block.BlockRailPowered;
import cn.nukkit.block.BlockRedSandstone;
import cn.nukkit.block.BlockRedstone;
import cn.nukkit.block.BlockRedstoneComparatorPowered;
import cn.nukkit.block.BlockRedstoneComparatorUnpowered;
import cn.nukkit.block.BlockRedstoneLamp;
import cn.nukkit.block.BlockRedstoneLampLit;
import cn.nukkit.block.BlockRedstoneRepeaterPowered;
import cn.nukkit.block.BlockRedstoneRepeaterUnpowered;
import cn.nukkit.block.BlockRedstoneTorch;
import cn.nukkit.block.BlockRedstoneTorchUnlit;
import cn.nukkit.block.BlockRedstoneWire;
import cn.nukkit.block.BlockReserved6;
import cn.nukkit.block.BlockSand;
import cn.nukkit.block.BlockSandstone;
import cn.nukkit.block.BlockSapling;
import cn.nukkit.block.BlockScaffolding;
import cn.nukkit.block.BlockSeaLantern;
import cn.nukkit.block.BlockSeaPickle;
import cn.nukkit.block.BlockSeagrass;
import cn.nukkit.block.BlockShulkerBox;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.block.BlockSkull;
import cn.nukkit.block.BlockSlabRedSandstone;
import cn.nukkit.block.BlockSlabStone;
import cn.nukkit.block.BlockSlabStone3;
import cn.nukkit.block.BlockSlabStone4;
import cn.nukkit.block.BlockSlabWood;
import cn.nukkit.block.BlockSlime;
import cn.nukkit.block.BlockSmithingTable;
import cn.nukkit.block.BlockSmoker;
import cn.nukkit.block.BlockSmokerLit;
import cn.nukkit.block.BlockSmoothStone;
import cn.nukkit.block.BlockSnow;
import cn.nukkit.block.BlockSnowLayer;
import cn.nukkit.block.BlockSoulFire;
import cn.nukkit.block.BlockSoulSand;
import cn.nukkit.block.BlockSponge;
import cn.nukkit.block.BlockSpruceSignStanding;
import cn.nukkit.block.BlockSpruceWallSign;
import cn.nukkit.block.BlockStairsAcacia;
import cn.nukkit.block.BlockStairsAndesite;
import cn.nukkit.block.BlockStairsAndesitePolished;
import cn.nukkit.block.BlockStairsBirch;
import cn.nukkit.block.BlockStairsBrick;
import cn.nukkit.block.BlockStairsCobblestone;
import cn.nukkit.block.BlockStairsDarkOak;
import cn.nukkit.block.BlockStairsDarkPrismarine;
import cn.nukkit.block.BlockStairsDiorite;
import cn.nukkit.block.BlockStairsDioritePolished;
import cn.nukkit.block.BlockStairsEndBrick;
import cn.nukkit.block.BlockStairsGranite;
import cn.nukkit.block.BlockStairsGranitePolished;
import cn.nukkit.block.BlockStairsJungle;
import cn.nukkit.block.BlockStairsMossyCobblestone;
import cn.nukkit.block.BlockStairsMossyStoneBrick;
import cn.nukkit.block.BlockStairsNetherBrick;
import cn.nukkit.block.BlockStairsPrismarine;
import cn.nukkit.block.BlockStairsPrismarineBrick;
import cn.nukkit.block.BlockStairsPurpur;
import cn.nukkit.block.BlockStairsQuartz;
import cn.nukkit.block.BlockStairsRedNetherBrick;
import cn.nukkit.block.BlockStairsRedSandstone;
import cn.nukkit.block.BlockStairsSandstone;
import cn.nukkit.block.BlockStairsSmoothQuartz;
import cn.nukkit.block.BlockStairsSmoothRedSandstone;
import cn.nukkit.block.BlockStairsSmoothSandstone;
import cn.nukkit.block.BlockStairsSpruce;
import cn.nukkit.block.BlockStairsStone;
import cn.nukkit.block.BlockStairsStoneBrick;
import cn.nukkit.block.BlockStairsWood;
import cn.nukkit.block.BlockStemMelon;
import cn.nukkit.block.BlockStemPumpkin;
import cn.nukkit.block.BlockStone;
import cn.nukkit.block.BlockStonecutter;
import cn.nukkit.block.BlockStonecutterBlock;
import cn.nukkit.block.BlockStructureBlock;
import cn.nukkit.block.BlockSugarcane;
import cn.nukkit.block.BlockSweetBerryBush;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.block.BlockTallGrass;
import cn.nukkit.block.BlockTarget;
import cn.nukkit.block.BlockTerracotta;
import cn.nukkit.block.BlockTerracottaGlazedBlack;
import cn.nukkit.block.BlockTerracottaGlazedBlue;
import cn.nukkit.block.BlockTerracottaGlazedBrown;
import cn.nukkit.block.BlockTerracottaGlazedCyan;
import cn.nukkit.block.BlockTerracottaGlazedGray;
import cn.nukkit.block.BlockTerracottaGlazedGreen;
import cn.nukkit.block.BlockTerracottaGlazedLightBlue;
import cn.nukkit.block.BlockTerracottaGlazedLime;
import cn.nukkit.block.BlockTerracottaGlazedMagenta;
import cn.nukkit.block.BlockTerracottaGlazedOrange;
import cn.nukkit.block.BlockTerracottaGlazedPink;
import cn.nukkit.block.BlockTerracottaGlazedPurple;
import cn.nukkit.block.BlockTerracottaGlazedRed;
import cn.nukkit.block.BlockTerracottaGlazedSilver;
import cn.nukkit.block.BlockTerracottaGlazedWhite;
import cn.nukkit.block.BlockTerracottaGlazedYellow;
import cn.nukkit.block.BlockTerracottaStained;
import cn.nukkit.block.BlockTorch;
import cn.nukkit.block.BlockTrapdoor;
import cn.nukkit.block.BlockTrapdoorAcacia;
import cn.nukkit.block.BlockTrapdoorBirch;
import cn.nukkit.block.BlockTrapdoorDarkOak;
import cn.nukkit.block.BlockTrapdoorIron;
import cn.nukkit.block.BlockTrapdoorJungle;
import cn.nukkit.block.BlockTrapdoorSpruce;
import cn.nukkit.block.BlockTrappedChest;
import cn.nukkit.block.BlockTripWire;
import cn.nukkit.block.BlockTripWireHook;
import cn.nukkit.block.BlockTurtleEgg;
import cn.nukkit.block.BlockUnderwaterTorch;
import cn.nukkit.block.BlockUndyedShulkerBox;
import cn.nukkit.block.BlockUnknown;
import cn.nukkit.block.BlockVine;
import cn.nukkit.block.BlockWall;
import cn.nukkit.block.BlockWallBanner;
import cn.nukkit.block.BlockWallSign;
import cn.nukkit.block.BlockWater;
import cn.nukkit.block.BlockWaterLily;
import cn.nukkit.block.BlockWaterStill;
import cn.nukkit.block.BlockWeightedPressurePlateHeavy;
import cn.nukkit.block.BlockWeightedPressurePlateLight;
import cn.nukkit.block.BlockWheat;
import cn.nukkit.block.BlockWitherRose;
import cn.nukkit.block.BlockWood;
import cn.nukkit.block.BlockWood2;
import cn.nukkit.block.BlockWoodBark;
import cn.nukkit.block.BlockWoodStrippedAcacia;
import cn.nukkit.block.BlockWoodStrippedBirch;
import cn.nukkit.block.BlockWoodStrippedDarkOak;
import cn.nukkit.block.BlockWoodStrippedJungle;
import cn.nukkit.block.BlockWoodStrippedOak;
import cn.nukkit.block.BlockWoodStrippedSpruce;
import cn.nukkit.block.BlockWool;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.MovingObjectPosition;
import cn.nukkit.level.Position;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.metadata.MetadataValue;
import cn.nukkit.metadata.Metadatable;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.BlockColor;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class Block
extends Position
implements Metadatable,
Cloneable,
BlockID {
    public static Class[] list = null;
    public static Block[] fullList = null;
    private static Block[] a = null;
    public static int[] light = null;
    public static int[] lightFilter = null;
    public static boolean[] solid = null;
    public static double[] hardness = null;
    public static boolean[] transparent = null;
    public AxisAlignedBB boundingBox = null;
    public AxisAlignedBB collisionBoundingBox = null;
    public static boolean[] hasMeta = null;
    private static final boolean[] b = new boolean[512];

    protected Block() {
    }

    public static void init() {
        Block.b[385] = true;
        Block.b[393] = true;
        Block.b[415] = true;
        if (list == null) {
            list = new Class[512];
            fullList = new Block[8192];
            light = new int[512];
            lightFilter = new int[512];
            solid = new boolean[512];
            hardness = new double[512];
            transparent = new boolean[512];
            hasMeta = new boolean[512];
            Block.list[0] = BlockAir.class;
            Block.list[1] = BlockStone.class;
            Block.list[2] = BlockGrass.class;
            Block.list[3] = BlockDirt.class;
            Block.list[4] = BlockCobblestone.class;
            Block.list[5] = BlockPlanks.class;
            Block.list[6] = BlockSapling.class;
            Block.list[7] = BlockBedrock.class;
            Block.list[8] = BlockWater.class;
            Block.list[9] = BlockWaterStill.class;
            Block.list[10] = BlockLava.class;
            Block.list[11] = BlockLavaStill.class;
            Block.list[12] = BlockSand.class;
            Block.list[13] = BlockGravel.class;
            Block.list[14] = BlockOreGold.class;
            Block.list[15] = BlockOreIron.class;
            Block.list[16] = BlockOreCoal.class;
            Block.list[17] = BlockWood.class;
            Block.list[18] = BlockLeaves.class;
            Block.list[19] = BlockSponge.class;
            Block.list[20] = BlockGlass.class;
            Block.list[21] = BlockOreLapis.class;
            Block.list[22] = BlockLapis.class;
            Block.list[23] = BlockDispenser.class;
            Block.list[24] = BlockSandstone.class;
            Block.list[25] = BlockNoteblock.class;
            Block.list[26] = BlockBed.class;
            Block.list[27] = BlockRailPowered.class;
            Block.list[28] = BlockRailDetector.class;
            Block.list[29] = BlockPistonSticky.class;
            Block.list[30] = BlockCobweb.class;
            Block.list[31] = BlockTallGrass.class;
            Block.list[32] = BlockDeadBush.class;
            Block.list[33] = BlockPiston.class;
            Block.list[34] = BlockPistonHead.class;
            Block.list[35] = BlockWool.class;
            Block.list[37] = BlockDandelion.class;
            Block.list[38] = BlockFlower.class;
            Block.list[39] = BlockMushroomBrown.class;
            Block.list[40] = BlockMushroomRed.class;
            Block.list[41] = BlockGold.class;
            Block.list[42] = BlockIron.class;
            Block.list[43] = BlockDoubleSlabStone.class;
            Block.list[44] = BlockSlabStone.class;
            Block.list[45] = BlockBricks.class;
            Block.list[46] = BlockTNT.class;
            Block.list[47] = BlockBookshelf.class;
            Block.list[48] = BlockMossStone.class;
            Block.list[49] = BlockObsidian.class;
            Block.list[50] = BlockTorch.class;
            Block.list[51] = BlockFire.class;
            Block.list[52] = BlockMobSpawner.class;
            Block.list[53] = BlockStairsWood.class;
            Block.list[54] = BlockChest.class;
            Block.list[55] = BlockRedstoneWire.class;
            Block.list[56] = BlockOreDiamond.class;
            Block.list[57] = BlockDiamond.class;
            Block.list[58] = BlockCraftingTable.class;
            Block.list[59] = BlockWheat.class;
            Block.list[60] = BlockFarmland.class;
            Block.list[61] = BlockFurnace.class;
            Block.list[62] = BlockFurnaceBurning.class;
            Block.list[63] = BlockSignPost.class;
            Block.list[64] = BlockDoorWood.class;
            Block.list[65] = BlockLadder.class;
            Block.list[66] = BlockRail.class;
            Block.list[67] = BlockStairsCobblestone.class;
            Block.list[68] = BlockWallSign.class;
            Block.list[69] = BlockLever.class;
            Block.list[70] = BlockPressurePlateStone.class;
            Block.list[71] = BlockDoorIron.class;
            Block.list[72] = BlockPressurePlateWood.class;
            Block.list[73] = BlockOreRedstone.class;
            Block.list[74] = BlockOreRedstoneGlowing.class;
            Block.list[75] = BlockRedstoneTorchUnlit.class;
            Block.list[76] = BlockRedstoneTorch.class;
            Block.list[77] = BlockButtonStone.class;
            Block.list[78] = BlockSnowLayer.class;
            Block.list[79] = BlockIce.class;
            Block.list[80] = BlockSnow.class;
            Block.list[81] = BlockCactus.class;
            Block.list[82] = BlockClay.class;
            Block.list[83] = BlockSugarcane.class;
            Block.list[84] = BlockJukebox.class;
            Block.list[85] = BlockFence.class;
            Block.list[86] = BlockPumpkin.class;
            Block.list[87] = BlockNetherrack.class;
            Block.list[88] = BlockSoulSand.class;
            Block.list[89] = BlockGlowstone.class;
            Block.list[90] = BlockNetherPortal.class;
            Block.list[91] = BlockPumpkinLit.class;
            Block.list[92] = BlockCake.class;
            Block.list[93] = BlockRedstoneRepeaterUnpowered.class;
            Block.list[94] = BlockRedstoneRepeaterPowered.class;
            Block.list[95] = BlockBedrockInvisible.class;
            Block.list[96] = BlockTrapdoor.class;
            Block.list[97] = BlockMonsterEgg.class;
            Block.list[98] = BlockBricksStone.class;
            Block.list[99] = BlockHugeMushroomBrown.class;
            Block.list[100] = BlockHugeMushroomRed.class;
            Block.list[101] = BlockIronBars.class;
            Block.list[102] = BlockGlassPane.class;
            Block.list[103] = BlockMelon.class;
            Block.list[104] = BlockStemPumpkin.class;
            Block.list[105] = BlockStemMelon.class;
            Block.list[106] = BlockVine.class;
            Block.list[107] = BlockFenceGate.class;
            Block.list[108] = BlockStairsBrick.class;
            Block.list[109] = BlockStairsStoneBrick.class;
            Block.list[110] = BlockMycelium.class;
            Block.list[111] = BlockWaterLily.class;
            Block.list[112] = BlockBricksNether.class;
            Block.list[113] = BlockFenceNetherBrick.class;
            Block.list[114] = BlockStairsNetherBrick.class;
            Block.list[115] = BlockNetherWart.class;
            Block.list[116] = BlockEnchantingTable.class;
            Block.list[117] = BlockBrewingStand.class;
            Block.list[118] = BlockCauldron.class;
            Block.list[119] = BlockEndPortal.class;
            Block.list[120] = BlockEndPortalFrame.class;
            Block.list[121] = BlockEndStone.class;
            Block.list[122] = BlockDragonEgg.class;
            Block.list[123] = BlockRedstoneLamp.class;
            Block.list[124] = BlockRedstoneLampLit.class;
            Block.list[125] = BlockDropper.class;
            Block.list[126] = BlockRailActivator.class;
            Block.list[127] = BlockCocoa.class;
            Block.list[128] = BlockStairsSandstone.class;
            Block.list[129] = BlockOreEmerald.class;
            Block.list[130] = BlockEnderChest.class;
            Block.list[131] = BlockTripWireHook.class;
            Block.list[132] = BlockTripWire.class;
            Block.list[133] = BlockEmerald.class;
            Block.list[134] = BlockStairsSpruce.class;
            Block.list[135] = BlockStairsBirch.class;
            Block.list[136] = BlockStairsJungle.class;
            Block.list[137] = BlockCommandBlock.class;
            Block.list[138] = BlockBeacon.class;
            Block.list[139] = BlockWall.class;
            Block.list[140] = BlockFlowerPot.class;
            Block.list[141] = BlockCarrot.class;
            Block.list[142] = BlockPotato.class;
            Block.list[143] = BlockButtonWooden.class;
            Block.list[144] = BlockSkull.class;
            Block.list[145] = BlockAnvil.class;
            Block.list[146] = BlockTrappedChest.class;
            Block.list[147] = BlockWeightedPressurePlateLight.class;
            Block.list[148] = BlockWeightedPressurePlateHeavy.class;
            Block.list[149] = BlockRedstoneComparatorUnpowered.class;
            Block.list[150] = BlockRedstoneComparatorPowered.class;
            Block.list[151] = BlockDaylightDetector.class;
            Block.list[152] = BlockRedstone.class;
            Block.list[153] = BlockOreQuartz.class;
            Block.list[154] = BlockHopper.class;
            Block.list[155] = BlockQuartz.class;
            Block.list[156] = BlockStairsQuartz.class;
            Block.list[157] = BlockDoubleSlabWood.class;
            Block.list[158] = BlockSlabWood.class;
            Block.list[159] = BlockTerracottaStained.class;
            Block.list[160] = BlockGlassPaneStained.class;
            Block.list[161] = BlockLeaves2.class;
            Block.list[162] = BlockWood2.class;
            Block.list[163] = BlockStairsAcacia.class;
            Block.list[164] = BlockStairsDarkOak.class;
            Block.list[165] = BlockSlime.class;
            Block.list[166] = BlockGlowStick.class;
            Block.list[167] = BlockTrapdoorIron.class;
            Block.list[168] = BlockPrismarine.class;
            Block.list[169] = BlockSeaLantern.class;
            Block.list[170] = BlockHayBale.class;
            Block.list[171] = BlockCarpet.class;
            Block.list[172] = BlockTerracotta.class;
            Block.list[173] = BlockCoal.class;
            Block.list[174] = BlockIcePacked.class;
            Block.list[175] = BlockDoublePlant.class;
            Block.list[176] = BlockBanner.class;
            Block.list[177] = BlockWallBanner.class;
            Block.list[178] = BlockDaylightDetectorInverted.class;
            Block.list[179] = BlockRedSandstone.class;
            Block.list[180] = BlockStairsRedSandstone.class;
            Block.list[181] = BlockDoubleSlabRedSandstone.class;
            Block.list[182] = BlockSlabRedSandstone.class;
            Block.list[183] = BlockFenceGateSpruce.class;
            Block.list[184] = BlockFenceGateBirch.class;
            Block.list[185] = BlockFenceGateJungle.class;
            Block.list[186] = BlockFenceGateDarkOak.class;
            Block.list[187] = BlockFenceGateAcacia.class;
            Block.list[188] = BlockCommandBlockRepeating.class;
            Block.list[189] = BlockCommandBlockChain.class;
            Block.list[190] = BlockHardGlassPane.class;
            Block.list[191] = BlockHardGlassPaneStained.class;
            Block.list[192] = BlockChemicalHeat.class;
            Block.list[193] = BlockDoorSpruce.class;
            Block.list[194] = BlockDoorBirch.class;
            Block.list[195] = BlockDoorJungle.class;
            Block.list[196] = BlockDoorAcacia.class;
            Block.list[197] = BlockDoorDarkOak.class;
            Block.list[198] = BlockGrassPath.class;
            Block.list[199] = BlockItemFrame.class;
            Block.list[200] = BlockChorusFlower.class;
            Block.list[201] = BlockPurpur.class;
            Block.list[202] = BlockColoredTorchRG.class;
            Block.list[203] = BlockStairsPurpur.class;
            Block.list[204] = BlockColoredTorchBP.class;
            Block.list[205] = BlockUndyedShulkerBox.class;
            Block.list[206] = BlockBricksEndStone.class;
            Block.list[207] = BlockIceFrosted.class;
            Block.list[208] = BlockEndRod.class;
            Block.list[209] = BlockEndGateway.class;
            Block.list[213] = BlockMagma.class;
            Block.list[214] = BlockNetherWartBlock.class;
            Block.list[215] = BlockBricksRedNether.class;
            Block.list[216] = BlockBone.class;
            Block.list[218] = BlockShulkerBox.class;
            Block.list[219] = BlockTerracottaGlazedPurple.class;
            Block.list[220] = BlockTerracottaGlazedWhite.class;
            Block.list[221] = BlockTerracottaGlazedOrange.class;
            Block.list[222] = BlockTerracottaGlazedMagenta.class;
            Block.list[223] = BlockTerracottaGlazedLightBlue.class;
            Block.list[224] = BlockTerracottaGlazedYellow.class;
            Block.list[225] = BlockTerracottaGlazedLime.class;
            Block.list[226] = BlockTerracottaGlazedPink.class;
            Block.list[227] = BlockTerracottaGlazedGray.class;
            Block.list[228] = BlockTerracottaGlazedSilver.class;
            Block.list[229] = BlockTerracottaGlazedCyan.class;
            Block.list[231] = BlockTerracottaGlazedBlue.class;
            Block.list[232] = BlockTerracottaGlazedBrown.class;
            Block.list[233] = BlockTerracottaGlazedGreen.class;
            Block.list[234] = BlockTerracottaGlazedRed.class;
            Block.list[235] = BlockTerracottaGlazedBlack.class;
            Block.list[236] = BlockConcrete.class;
            Block.list[237] = BlockConcretePowder.class;
            Block.list[238] = BlockChemistryTable.class;
            Block.list[239] = BlockUnderwaterTorch.class;
            Block.list[240] = BlockChorusPlant.class;
            Block.list[241] = BlockGlassStained.class;
            Block.list[243] = BlockPodzol.class;
            Block.list[244] = BlockBeetroot.class;
            Block.list[245] = BlockStonecutter.class;
            Block.list[246] = BlockObsidianGlowing.class;
            Block.list[247] = BlockNetherReactor.class;
            Block.list[248] = BlockInfoUpdate.class;
            Block.list[249] = BlockInfoUpdate2.class;
            Block.list[250] = BlockPistonExtension.class;
            Block.list[251] = BlockObserver.class;
            Block.list[252] = BlockStructureBlock.class;
            Block.list[253] = BlockHardGlass.class;
            Block.list[254] = BlockHardGlassStained.class;
            Block.list[255] = BlockReserved6.class;
            Block.list[257] = BlockStairsPrismarine.class;
            Block.list[258] = BlockStairsDarkPrismarine.class;
            Block.list[259] = BlockStairsPrismarineBrick.class;
            Block.list[260] = BlockWoodStrippedSpruce.class;
            Block.list[261] = BlockWoodStrippedBirch.class;
            Block.list[262] = BlockWoodStrippedJungle.class;
            Block.list[263] = BlockWoodStrippedAcacia.class;
            Block.list[264] = BlockWoodStrippedDarkOak.class;
            Block.list[265] = BlockWoodStrippedOak.class;
            Block.list[266] = BlockBlueIce.class;
            Block.list[385] = BlockSeagrass.class;
            Block.list[386] = BlockCoral.class;
            Block.list[387] = BlockCoralBlock.class;
            Block.list[388] = BlockCoralFan.class;
            Block.list[389] = BlockCoralFanDead.class;
            Block.list[390] = BlockCoralFanHang.class;
            Block.list[391] = BlockCoralFanHang2.class;
            Block.list[392] = BlockCoralFanHang3.class;
            Block.list[393] = BlockKelp.class;
            Block.list[394] = BlockDriedKelpBlock.class;
            Block.list[395] = BlockButtonAcacia.class;
            Block.list[396] = BlockButtonBirch.class;
            Block.list[397] = BlockButtonDarkOak.class;
            Block.list[398] = BlockButtonJungle.class;
            Block.list[399] = BlockButtonSpruce.class;
            Block.list[400] = BlockTrapdoorAcacia.class;
            Block.list[401] = BlockTrapdoorBirch.class;
            Block.list[402] = BlockTrapdoorDarkOak.class;
            Block.list[403] = BlockTrapdoorJungle.class;
            Block.list[404] = BlockTrapdoorSpruce.class;
            Block.list[405] = BlockPressurePlateAcacia.class;
            Block.list[406] = BlockPressurePlateBirch.class;
            Block.list[407] = BlockPressurePlateDarkOak.class;
            Block.list[408] = BlockPressurePlateJungle.class;
            Block.list[409] = BlockPressurePlateSpruce.class;
            Block.list[410] = BlockPumpkinCarved.class;
            Block.list[411] = BlockSeaPickle.class;
            Block.list[412] = BlockConduit.class;
            Block.list[414] = BlockTurtleEgg.class;
            Block.list[415] = BlockBubbleColumn.class;
            Block.list[416] = BlockBarrier.class;
            Block.list[417] = BlockSlabStone3.class;
            Block.list[418] = BlockBamboo.class;
            Block.list[419] = BlockBambooSapling.class;
            Block.list[421] = BlockSlabStone4.class;
            Block.list[420] = BlockScaffolding.class;
            Block.list[422] = BlockDoubleSlabStone3.class;
            Block.list[423] = BlockDoubleSlabStone4.class;
            Block.list[424] = BlockStairsGranite.class;
            Block.list[425] = BlockStairsDiorite.class;
            Block.list[426] = BlockStairsAndesite.class;
            Block.list[427] = BlockStairsGranitePolished.class;
            Block.list[428] = BlockStairsDioritePolished.class;
            Block.list[429] = BlockStairsAndesitePolished.class;
            Block.list[430] = BlockStairsMossyStoneBrick.class;
            Block.list[431] = BlockStairsSmoothRedSandstone.class;
            Block.list[432] = BlockStairsSmoothSandstone.class;
            Block.list[433] = BlockStairsEndBrick.class;
            Block.list[434] = BlockStairsMossyCobblestone.class;
            Block.list[435] = BlockStairsStone.class;
            Block.list[436] = BlockSpruceSignStanding.class;
            Block.list[437] = BlockSpruceWallSign.class;
            Block.list[438] = BlockSmoothStone.class;
            Block.list[439] = BlockStairsRedNetherBrick.class;
            Block.list[440] = BlockStairsSmoothQuartz.class;
            Block.list[441] = BlockBirchSignStanding.class;
            Block.list[442] = BlockBirchWallSign.class;
            Block.list[443] = BlockJungleSignStanding.class;
            Block.list[444] = BlockJungleWallSign.class;
            Block.list[445] = BlockAcaciaSignStanding.class;
            Block.list[446] = BlockAcaciaWallSign.class;
            Block.list[447] = BlockDarkOakSignStanding.class;
            Block.list[448] = BlockDarkOakWallSign.class;
            Block.list[449] = BlockLectern.class;
            Block.list[450] = BlockGrindstone.class;
            Block.list[451] = BlockBlastFurnace.class;
            Block.list[452] = BlockStonecutterBlock.class;
            Block.list[453] = BlockSmoker.class;
            Block.list[454] = BlockSmokerLit.class;
            Block.list[455] = BlockCartographyTable.class;
            Block.list[456] = BlockFletchingTable.class;
            Block.list[457] = BlockSmithingTable.class;
            Block.list[458] = BlockBarrel.class;
            Block.list[459] = BlockLoom.class;
            Block.list[461] = BlockBell.class;
            Block.list[462] = BlockSweetBerryBush.class;
            Block.list[463] = BlockLantern.class;
            Block.list[464] = BlockCampfire.class;
            Block.list[465] = BlockCauldronLava.class;
            Block.list[466] = BlockJigsaw.class;
            Block.list[467] = BlockWoodBark.class;
            Block.list[468] = BlockComposter.class;
            Block.list[469] = BlockBlastFurnaceLit.class;
            Block.list[470] = BlockLightBlock.class;
            Block.list[471] = BlockWitherRose.class;
            Block.list[472] = BlockPistonHeadSticky.class;
            Block.list[473] = BlockBeeNest.class;
            Block.list[474] = BlockBeehive.class;
            Block.list[475] = BlockHoneyBlock.class;
            Block.list[476] = BlockHoneycombBlock.class;
            Block.list[492] = BlockSoulFire.class;
            Block.list[494] = BlockTarget.class;
            for (int k = 0; k < 512; ++k) {
                Class clazz = list[k];
                if (clazz != null) {
                    int n;
                    Block block;
                    try {
                        block = (Block)clazz.newInstance();
                        try {
                            Constructor constructor = clazz.getDeclaredConstructor(Integer.TYPE);
                            constructor.setAccessible(true);
                            for (n = 0; n < 16; ++n) {
                                Block.fullList[k << 4 | n] = (Block)constructor.newInstance(n);
                            }
                            Block.hasMeta[k] = true;
                        }
                        catch (NoSuchMethodException noSuchMethodException) {
                            for (n = 0; n < 16; ++n) {
                                Block.fullList[k << 4 | n] = block;
                            }
                        }
                    }
                    catch (Exception exception) {
                        Server.getInstance().getLogger().error("Error while registering " + clazz.getName(), exception);
                        for (n = 0; n < 16; ++n) {
                            Block.fullList[k << 4 | n] = new BlockUnknown(k, (Integer)n);
                        }
                        return;
                    }
                    Block.solid[k] = block.isSolid();
                    Block.transparent[k] = block.isTransparent();
                    Block.hardness[k] = block.getHardness();
                    Block.light[k] = block.getLightLevel();
                    if (block.isSolid()) {
                        if (block.isTransparent()) {
                            if (block instanceof BlockLiquid || block instanceof BlockIce) {
                                Block.lightFilter[k] = 2;
                                continue;
                            }
                            Block.lightFilter[k] = 1;
                            continue;
                        }
                        Block.lightFilter[k] = 15;
                        continue;
                    }
                    Block.lightFilter[k] = 1;
                    continue;
                }
                Block.lightFilter[k] = 1;
                for (int i2 = 0; i2 < 16; ++i2) {
                    Block.fullList[k << 4 | i2] = new BlockUnknown(k, (Integer)i2);
                }
            }
        }
        a = (Block[])fullList.clone();
    }

    public static Block get(int n) {
        if (n < 0) {
            n = 255 - n;
        }
        return fullList[n << 4].clone();
    }

    public static Block get(int n, Integer n2) {
        if (n < 0) {
            n = 255 - n;
        }
        if (n2 != null) {
            return fullList[(n << 4) + n2].clone();
        }
        return fullList[n << 4].clone();
    }

    public static Block get(int n, Integer n2, Position position) {
        if (n < 0) {
            n = 255 - n;
        }
        Block block = fullList[n << 4 | (n2 == null ? 0 : n2)].clone();
        if (position != null) {
            block.x = position.x;
            block.y = position.y;
            block.z = position.z;
            block.level = position.level;
        }
        return block;
    }

    public static Block get(int n, int n2) {
        if (n < 0) {
            n = 255 - n;
        }
        return fullList[(n << 4) + n2].clone();
    }

    public static Block get(int n, Level level, int n2, int n3, int n4) {
        Block block = fullList[n].clone();
        block.x = n2;
        block.y = n3;
        block.z = n4;
        block.level = level;
        return block;
    }

    public static Block getFast(int n, Level level, int n2, int n3, int n4) {
        Block block = a[n].fastCloneBlock();
        block.x = n2;
        block.y = n3;
        block.z = n4;
        block.level = level;
        return block;
    }

    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        return this.getLevel().setBlock(this, this, true, true);
    }

    public boolean canHarvestWithHand() {
        return true;
    }

    public boolean isBreakable(Item item) {
        return true;
    }

    public int tickRate() {
        return 10;
    }

    public boolean onBreak(Item item, Player player) {
        return this.onBreak(item);
    }

    public boolean onBreak(Item item) {
        return this.getLevel().setBlock(this, Block.get(0), true, true);
    }

    public int onUpdate(int n) {
        return 0;
    }

    public boolean onActivate(Item item) {
        return this.onActivate(item, null);
    }

    public boolean onActivate(Item item, Player player) {
        return false;
    }

    public double getHardness() {
        return 10.0;
    }

    public double getResistance() {
        return 1.0;
    }

    public int getBurnChance() {
        return 0;
    }

    public int getBurnAbility() {
        return 0;
    }

    public int getToolType() {
        return 0;
    }

    public double getFrictionFactor() {
        return 0.6;
    }

    public int getLightLevel() {
        return 0;
    }

    public boolean canBePlaced() {
        return true;
    }

    public boolean canBeReplaced() {
        return false;
    }

    public boolean isTransparent() {
        return false;
    }

    public boolean isSolid() {
        return true;
    }

    public boolean canBeFlowedInto() {
        return false;
    }

    public boolean canBeActivated() {
        return false;
    }

    public boolean hasEntityCollision() {
        return false;
    }

    public boolean canPassThrough() {
        return false;
    }

    public boolean canBePushed() {
        return this.level.getBlockEntity(this) == null;
    }

    public boolean breakWhenPushed() {
        return false;
    }

    public boolean hasComparatorInputOverride() {
        return false;
    }

    public int getComparatorInputOverride() {
        return 0;
    }

    public boolean canBeClimbed() {
        return false;
    }

    public BlockColor getColor() {
        return BlockColor.VOID_BLOCK_COLOR;
    }

    public abstract String getName();

    public abstract int getId();

    public int getFullId() {
        return this.getId() << 4;
    }

    public void addVelocityToEntity(Entity entity, Vector3 vector3) {
    }

    public int getDamage() {
        return 0;
    }

    public void setDamage(int n) {
    }

    public final void setDamage(Integer n) {
        this.setDamage(n == null ? 0 : n & 0xF);
    }

    public final void position(Position position) {
        this.x = (int)position.x;
        this.y = (int)position.y;
        this.z = (int)position.z;
        this.level = position.level;
        this.boundingBox = null;
    }

    public Item[] getDrops(Item item) {
        if (this.getId() < 0 || this.getId() > list.length) {
            return new Item[0];
        }
        return new Item[]{this.toItem()};
    }

    private static boolean a(int n, Item item) {
        return n == 1 && item.isSword() || n == 2 && item.isShovel() || n == 3 && item.isPickaxe() || n == 4 && item.isAxe() || n == 6 && item.isHoe() || n == 5 && item.isShears() || n == 0;
    }

    public double getBreakTime(Item item, Player player) {
        double d2;
        Objects.requireNonNull(item, "getBreakTime: Item can not be null");
        Objects.requireNonNull(player, "getBreakTime: Player can not be null");
        double d3 = this.getHardness();
        if (d3 == 0.0) {
            return 0.0;
        }
        int n = this.getId();
        if ((n == 30 || n == 418) && item.isSword()) {
            d2 = 0.1;
        } else if ((n == 35 || n == 30 || n == 18 || n == 161) && item.isShears()) {
            d2 = n == 35 ? 0.3 : 0.1;
        } else {
            int n2;
            int n3;
            double d4;
            boolean bl = player.isSubmerged();
            boolean bl2 = bl && !player.getInventory().getHelmetFast().hasEnchantment(8);
            boolean bl3 = item.isTool() && Block.a(this.getToolType(), item);
            double d5 = d4 = item.isSword() ? 1.5 : 1.0;
            if (bl3) {
                d4 = Block.a(item);
                n3 = Optional.ofNullable(item.getEnchantment(15)).map(Enchantment::getLevel).orElse(0);
                if (n3 > 0) {
                    d4 += (double)(n3 * n3 + 1);
                }
            }
            if ((n3 = Optional.ofNullable(player.getEffect(3)).map(Effect::getAmplifier).orElse(-1) + 1) > 0) {
                d4 *= 0.2 * (double)n3 + 1.0;
            }
            if ((n2 = Optional.ofNullable(player.getEffect(4)).map(Effect::getAmplifier).orElse(-1) + 1) > 0) {
                d4 *= Math.pow(0.3, n2);
            }
            if (bl2) {
                d4 /= 5.0;
            } else if (!player.isOnGround() && !bl) {
                d4 /= 5.0;
            }
            double d6 = bl3 || this.canHarvestWithHand() ? (bl2 ? 1.4 : 1.0) : 4.0;
            d2 = d6 / d4;
        }
        return d2 * d3;
    }

    private static double a(Item item) {
        switch (item.getTier()) {
            case 1: {
                return 2.0;
            }
            case 3: {
                return 4.0;
            }
            case 4: {
                return 6.0;
            }
            case 5: {
                return 8.0;
            }
            case 6: {
                return 9.0;
            }
            case 2: {
                return 12.0;
            }
        }
        return 1.0;
    }

    public double getBreakTime(Item item) {
        double d2 = this.getHardness() * 1.5;
        if (this.canBeBrokenWith(item)) {
            if (this.getToolType() == 5 && item.isShears()) {
                d2 /= 15.0;
            } else if (this.getToolType() == 3 && item.isPickaxe() || this.getToolType() == 4 && item.isAxe() || this.getToolType() == 2 && item.isShovel() || this.getToolType() == 6 && item.isHoe()) {
                switch (item.getTier()) {
                    case 1: {
                        d2 /= 2.0;
                        break;
                    }
                    case 3: {
                        d2 /= 4.0;
                        break;
                    }
                    case 4: {
                        d2 /= 6.0;
                        break;
                    }
                    case 5: {
                        d2 /= 8.0;
                        break;
                    }
                    case 6: {
                        d2 /= 9.0;
                        break;
                    }
                    case 2: {
                        d2 /= 12.0;
                    }
                }
            }
        } else {
            d2 *= 3.33;
        }
        if (item.isSword()) {
            d2 *= 0.5;
        }
        return d2;
    }

    public boolean canBeBrokenWith(Item item) {
        return this.getHardness() != -1.0;
    }

    @Override
    public Block getSide(BlockFace blockFace) {
        return this.getSide(blockFace, 1);
    }

    @Override
    public Block getSide(BlockFace blockFace, int n) {
        if (this.isValid()) {
            return this.getLevel().getBlock(super.getSide(blockFace, n));
        }
        return Block.get(0, 0, Position.fromObject(new Vector3(this.x, this.y, this.z).getSide(blockFace, n)));
    }

    @Override
    public Block up() {
        return this.up(1);
    }

    @Override
    public Block up(int n) {
        return this.getSide(BlockFace.UP, n);
    }

    @Override
    public Block down() {
        return this.down(1);
    }

    @Override
    public Block down(int n) {
        return this.getSide(BlockFace.DOWN, n);
    }

    @Override
    public Block north() {
        return this.north(1);
    }

    @Override
    public Block north(int n) {
        return this.getSide(BlockFace.NORTH, n);
    }

    @Override
    public Block south() {
        return this.south(1);
    }

    @Override
    public Block south(int n) {
        return this.getSide(BlockFace.SOUTH, n);
    }

    @Override
    public Block east() {
        return this.east(1);
    }

    @Override
    public Block east(int n) {
        return this.getSide(BlockFace.EAST, n);
    }

    @Override
    public Block west() {
        return this.west(1);
    }

    @Override
    public Block west(int n) {
        return this.getSide(BlockFace.WEST, n);
    }

    @Override
    public String toString() {
        return "Block[" + this.getName() + "] (" + this.getId() + ':' + this.getDamage() + ')';
    }

    public boolean collidesWithBB(AxisAlignedBB axisAlignedBB) {
        return this.collidesWithBB(axisAlignedBB, false);
    }

    public boolean collidesWithBB(AxisAlignedBB axisAlignedBB, boolean bl) {
        AxisAlignedBB axisAlignedBB2 = bl ? this.getCollisionBoundingBox() : this.getBoundingBox();
        return axisAlignedBB2 != null && axisAlignedBB.intersectsWith(axisAlignedBB2);
    }

    public void onEntityCollide(Entity entity) {
    }

    public AxisAlignedBB getBoundingBox() {
        if (this.boundingBox == null) {
            this.boundingBox = this.recalculateBoundingBox();
        }
        return this.boundingBox;
    }

    public AxisAlignedBB getCollisionBoundingBox() {
        if (this.collisionBoundingBox == null) {
            this.collisionBoundingBox = this.recalculateCollisionBoundingBox();
        }
        return this.collisionBoundingBox;
    }

    protected AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.getMinX(), this.getMinY(), this.getMinZ(), this.getMaxX(), this.getMaxY(), this.getMaxZ());
    }

    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    public MovingObjectPosition calculateIntercept(Vector3 vector3, Vector3 vector32) {
        AxisAlignedBB axisAlignedBB = this.getBoundingBox();
        if (axisAlignedBB == null) {
            return null;
        }
        Vector3 vector33 = vector3.getIntermediateWithXValue(vector32, axisAlignedBB.minX);
        Vector3 vector34 = vector3.getIntermediateWithXValue(vector32, axisAlignedBB.maxX);
        Vector3 vector35 = vector3.getIntermediateWithYValue(vector32, axisAlignedBB.minY);
        Vector3 vector36 = vector3.getIntermediateWithYValue(vector32, axisAlignedBB.maxY);
        Vector3 vector37 = vector3.getIntermediateWithZValue(vector32, axisAlignedBB.minZ);
        Vector3 vector38 = vector3.getIntermediateWithZValue(vector32, axisAlignedBB.maxZ);
        if (vector33 != null && !axisAlignedBB.isVectorInYZ(vector33)) {
            vector33 = null;
        }
        if (vector34 != null && !axisAlignedBB.isVectorInYZ(vector34)) {
            vector34 = null;
        }
        if (vector35 != null && !axisAlignedBB.isVectorInXZ(vector35)) {
            vector35 = null;
        }
        if (vector36 != null && !axisAlignedBB.isVectorInXZ(vector36)) {
            vector36 = null;
        }
        if (vector37 != null && !axisAlignedBB.isVectorInXY(vector37)) {
            vector37 = null;
        }
        if (vector38 != null && !axisAlignedBB.isVectorInXY(vector38)) {
            vector38 = null;
        }
        Vector3 vector39 = vector33;
        if (vector34 != null && (vector39 == null || vector3.distanceSquared(vector34) < vector3.distanceSquared(vector39))) {
            vector39 = vector34;
        }
        if (vector35 != null && (vector39 == null || vector3.distanceSquared(vector35) < vector3.distanceSquared(vector39))) {
            vector39 = vector35;
        }
        if (vector36 != null && (vector39 == null || vector3.distanceSquared(vector36) < vector3.distanceSquared(vector39))) {
            vector39 = vector36;
        }
        if (vector37 != null && (vector39 == null || vector3.distanceSquared(vector37) < vector3.distanceSquared(vector39))) {
            vector39 = vector37;
        }
        if (vector38 != null && (vector39 == null || vector3.distanceSquared(vector38) < vector3.distanceSquared(vector39))) {
            vector39 = vector38;
        }
        if (vector39 == null) {
            return null;
        }
        int n = -1;
        if (vector39 == vector33) {
            n = 4;
        } else if (vector39 == vector34) {
            n = 5;
        } else if (vector39 == vector35) {
            n = 0;
        } else if (vector39 == vector36) {
            n = 1;
        } else if (vector39 == vector37) {
            n = 2;
        } else if (vector39 == vector38) {
            n = 3;
        }
        return MovingObjectPosition.fromBlock((int)this.x, (int)this.y, (int)this.z, n, vector39.add(this.x, this.y, this.z));
    }

    public String getSaveId() {
        String string = this.getClass().getName();
        return string.substring(16);
    }

    @Override
    public void setMetadata(String string, MetadataValue metadataValue) throws Exception {
        block0: {
            if (this.getLevel() == null) break block0;
            this.getLevel().getBlockMetadata().setMetadata(this, string, metadataValue);
        }
    }

    @Override
    public List<MetadataValue> getMetadata(String string) throws Exception {
        if (this.getLevel() != null) {
            return this.getLevel().getBlockMetadata().getMetadata(this, string);
        }
        return null;
    }

    @Override
    public boolean hasMetadata(String string) throws Exception {
        return this.getLevel() != null && this.getLevel().getBlockMetadata().hasMetadata(this, string);
    }

    @Override
    public void removeMetadata(String string, Plugin plugin) throws Exception {
        block0: {
            if (this.getLevel() == null) break block0;
            this.getLevel().getBlockMetadata().removeMetadata(this, string, plugin);
        }
    }

    @Override
    public Block clone() {
        return (Block)super.clone();
    }

    public int getWeakPower(BlockFace blockFace) {
        return 0;
    }

    public int getStrongPower(BlockFace blockFace) {
        return 0;
    }

    public boolean isPowerSource() {
        return false;
    }

    public String getLocationHash() {
        return this.getFloorX() + ":" + this.getFloorY() + ':' + this.getFloorZ();
    }

    public int getDropExp() {
        return 0;
    }

    public boolean isNormalBlock() {
        return !this.isTransparent() && this.isSolid() && !this.isPowerSource();
    }

    public static boolean equals(Block block, Block block2) {
        return Block.equals(block, block2, true);
    }

    public static boolean equals(Block block, Block block2, boolean bl) {
        return block != null && block2 != null && block.getId() == block2.getId() && (!bl || block.getDamage() == block2.getDamage());
    }

    public Item toItem() {
        return new ItemBlock(this, this.getDamage(), 1);
    }

    public boolean canSilkTouch() {
        return false;
    }

    public double getMinX() {
        return this.x;
    }

    public double getMinY() {
        return this.y;
    }

    public double getMinZ() {
        return this.z;
    }

    public double getMaxX() {
        return this.x + 1.0;
    }

    public double getMaxY() {
        return this.y + 1.0;
    }

    public double getMaxZ() {
        return this.z + 1.0;
    }

    public Optional<Block> first(Predicate<Block> predicate) {
        Block block = this.getLevelBlock();
        if (predicate.test(block)) {
            return Optional.of(block);
        }
        return Optional.empty();
    }

    protected static boolean canStayOnFullSolid(Block block) {
        if (block.isTransparent()) {
            switch (block.getId()) {
                case 20: 
                case 79: 
                case 89: 
                case 138: 
                case 154: 
                case 169: 
                case 241: 
                case 253: 
                case 254: 
                case 416: 
                case 420: {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    protected static boolean canStayOnFullNonSolid(Block block) {
        if (Block.canStayOnFullSolid(block)) {
            return true;
        }
        switch (block.getId()) {
            case 118: 
            case 465: 
            case 468: {
                return true;
            }
        }
        return false;
    }

    public static boolean hasWater(int n) {
        return n == 8 || n == 9 || b[n];
    }

    public static boolean usesFakeWater(int n) {
        return b[n];
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

