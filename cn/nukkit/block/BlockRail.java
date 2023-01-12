/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockFlowable;
import cn.nukkit.block.BlockRailActivator;
import cn.nukkit.block.BlockRailDetector;
import cn.nukkit.block.BlockRailPowered;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.Rail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockRail
extends BlockFlowable
implements Faceable {
    protected boolean canBePowered = false;

    public BlockRail() {
        this(0);
    }

    public BlockRail(int n) {
        super(n);
    }

    @Override
    public String getName() {
        return "Rail";
    }

    @Override
    public int getId() {
        return 66;
    }

    @Override
    public double getHardness() {
        return 0.7;
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return 3;
    }

    @Override
    public int onUpdate(int n) {
        if (n == 1) {
            Optional<BlockFace> optional = this.getOrientation().ascendingDirection();
            Block block = this.down();
            if (!BlockRail.canStayOnFullNonSolid(this.down()) || optional.isPresent() && this.getSide(optional.get()).isTransparent()) {
                this.getLevel().useBreakOn(this);
                return 1;
            }
        } else if (n == 6) {
            if (this instanceof BlockRailPowered || this instanceof BlockRailDetector || this instanceof BlockRailActivator) {
                return 0;
            }
            boolean bl = this.level.isBlockPowered(this);
            Map<BlockRail, BlockFace> map = this.a(Arrays.asList(BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH));
            int n2 = map.size();
            if (n2 <= 2) {
                return 0;
            }
            ArrayList<BlockRail> arrayList = new ArrayList<BlockRail>(map.keySet());
            ArrayList<BlockFace> arrayList2 = new ArrayList<BlockFace>(map.values());
            if (n2 == 4) {
                if (this.isAbstract()) {
                    if (bl) {
                        this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.NORTH)), BlockFace.NORTH, (BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.WEST)), BlockFace.WEST).metadata());
                    } else {
                        this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.SOUTH)), BlockFace.SOUTH, (BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.EAST)), BlockFace.EAST).metadata());
                    }
                } else {
                    this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.EAST)), BlockFace.EAST, (BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.WEST)), BlockFace.WEST).metadata());
                }
            } else if (!map.isEmpty()) {
                if (this.isAbstract()) {
                    List<BlockFace> list = bl ? Stream.of(Rail.Orientation.CURVED_NORTH_WEST, Rail.Orientation.CURVED_SOUTH_WEST, Rail.Orientation.CURVED_NORTH_EAST).filter(orientation -> arrayList2.containsAll(orientation.connectingDirections())).findFirst().get().connectingDirections() : Stream.of(Rail.Orientation.CURVED_SOUTH_EAST, Rail.Orientation.CURVED_NORTH_EAST, Rail.Orientation.CURVED_SOUTH_WEST).filter(orientation -> arrayList2.containsAll(orientation.connectingDirections())).findFirst().get().connectingDirections();
                    BlockFace blockFace3 = list.get(0);
                    BlockFace blockFace4 = list.get(1);
                    this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace3)), blockFace3, (BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace4)), blockFace4).metadata());
                } else {
                    BlockFace blockFace5 = (BlockFace)((Object)arrayList2.stream().min((blockFace, blockFace2) -> blockFace.getIndex() < blockFace2.getIndex() ? 1 : (this.x == this.y ? 0 : -1)).get());
                    BlockFace blockFace6 = blockFace5.getOpposite();
                    if (arrayList2.contains((Object)blockFace6)) {
                        this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace5)), blockFace5, (BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace6)), blockFace6).metadata());
                    } else {
                        this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace5)), blockFace5).metadata());
                    }
                }
            }
            this.level.setBlock(this, this, true, true);
            if (!this.isAbstract()) {
                this.level.scheduleUpdate(this, this, 0);
            }
        }
        return 0;
    }

    @Override
    public AxisAlignedBB recalculateBoundingBox() {
        return new AxisAlignedBB(this.x, this.y, this.z, this.x + 1.0, this.y + 0.125, this.z + 1.0);
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace3, double d2, double d3, double d4, Player player) {
        if (!BlockRail.canStayOnFullNonSolid(this.down())) {
            return false;
        }
        Map<BlockRail, BlockFace> map = this.a();
        ArrayList<BlockRail> arrayList = new ArrayList<BlockRail>(map.keySet());
        ArrayList<BlockFace> arrayList2 = new ArrayList<BlockFace>(map.values());
        if (map.size() == 1) {
            BlockRail blockRail = (BlockRail)arrayList.get(0);
            this.setDamage(this.a(blockRail, map.get(blockRail)).metadata());
        } else if (map.size() == 4) {
            if (this.isAbstract()) {
                this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.SOUTH)), BlockFace.SOUTH, (BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.EAST)), BlockFace.EAST).metadata());
            } else {
                this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.EAST)), BlockFace.EAST, (BlockRail)arrayList.get(arrayList2.indexOf((Object)BlockFace.WEST)), BlockFace.WEST).metadata());
            }
        } else if (!map.isEmpty()) {
            if (this.isAbstract()) {
                if (map.size() == 2) {
                    BlockRail blockRail = (BlockRail)arrayList.get(0);
                    BlockRail blockRail2 = (BlockRail)arrayList.get(1);
                    this.setDamage(this.a(blockRail, map.get(blockRail), blockRail2, map.get(blockRail2)).metadata());
                } else {
                    List<BlockFace> list = Stream.of(Rail.Orientation.CURVED_SOUTH_EAST, Rail.Orientation.CURVED_NORTH_EAST, Rail.Orientation.CURVED_SOUTH_WEST).filter(orientation -> arrayList2.containsAll(orientation.connectingDirections())).findFirst().get().connectingDirections();
                    BlockFace blockFace4 = list.get(0);
                    BlockFace blockFace5 = list.get(1);
                    this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace4)), blockFace4, (BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace5)), blockFace5).metadata());
                }
            } else {
                BlockFace blockFace6 = (BlockFace)((Object)arrayList2.stream().min((blockFace, blockFace2) -> blockFace.getIndex() < blockFace2.getIndex() ? 1 : (this.x == this.y ? 0 : -1)).get());
                BlockFace blockFace7 = blockFace6.getOpposite();
                if (arrayList2.contains((Object)blockFace7)) {
                    this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace6)), blockFace6, (BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace7)), blockFace7).metadata());
                } else {
                    this.setDamage(this.a((BlockRail)arrayList.get(arrayList2.indexOf((Object)blockFace6)), blockFace6).metadata());
                }
            }
        }
        this.level.setBlock(this, this, true, true);
        if (!this.isAbstract()) {
            this.level.scheduleUpdate(this, this, 0);
        }
        return true;
    }

    private Rail.Orientation a(BlockRail blockRail, BlockFace blockFace, BlockRail blockRail2, BlockFace blockFace2) {
        this.a(blockRail, blockFace);
        this.a(blockRail2, blockFace2);
        if (blockFace.getOpposite() == blockFace2) {
            int n = (int)(this.y - blockRail.y);
            int n2 = (int)(this.y - blockRail2.y);
            if (n == -1) {
                return Rail.Orientation.ascending(blockFace);
            }
            if (n2 == -1) {
                return Rail.Orientation.ascending(blockFace2);
            }
        }
        return Rail.Orientation.straightOrCurved(blockFace, blockFace2);
    }

    private Rail.Orientation a(BlockRail blockRail, BlockFace blockFace) {
        int n = (int)(this.y - blockRail.y);
        Map<BlockRail, BlockFace> map = blockRail.checkRailsConnected();
        if (map.isEmpty()) {
            blockRail.setOrientation(n == 1 ? Rail.Orientation.ascending(blockFace.getOpposite()) : Rail.Orientation.straight(blockFace));
            return n == -1 ? Rail.Orientation.ascending(blockFace) : Rail.Orientation.straight(blockFace);
        }
        if (map.size() == 1) {
            BlockFace blockFace2 = map.values().iterator().next();
            if (blockRail.isAbstract() && blockFace2 != blockFace) {
                blockRail.setOrientation(Rail.Orientation.curved(blockFace.getOpposite(), blockFace2));
                return n == -1 ? Rail.Orientation.ascending(blockFace) : Rail.Orientation.straight(blockFace);
            }
            if (blockFace2 == blockFace) {
                if (!blockRail.getOrientation().isAscending()) {
                    blockRail.setOrientation(n == 1 ? Rail.Orientation.ascending(blockFace.getOpposite()) : Rail.Orientation.straight(blockFace));
                }
                return n == -1 ? Rail.Orientation.ascending(blockFace) : Rail.Orientation.straight(blockFace);
            }
            if (blockRail.getOrientation().hasConnectingDirections(BlockFace.NORTH, BlockFace.SOUTH)) {
                blockRail.setOrientation(n == 1 ? Rail.Orientation.ascending(blockFace.getOpposite()) : Rail.Orientation.straight(blockFace));
                return n == -1 ? Rail.Orientation.ascending(blockFace) : Rail.Orientation.straight(blockFace);
            }
        }
        return Rail.Orientation.STRAIGHT_NORTH_SOUTH;
    }

    private Map<BlockRail, BlockFace> a() {
        Map<BlockRail, BlockFace> map = this.a(Arrays.asList(BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH));
        return map.keySet().stream().filter(blockRail -> blockRail.checkRailsConnected().size() != 2).collect(Collectors.toMap(blockRail -> blockRail, map::get));
    }

    private Map<BlockRail, BlockFace> a(Collection<BlockFace> collection) {
        HashMap<BlockRail, BlockFace> hashMap = new HashMap<BlockRail, BlockFace>();
        collection.forEach(blockFace -> {
            Block block2 = this.getSide((BlockFace)((Object)blockFace));
            Stream.of(block2, block2.up(), block2.down()).filter(Rail::isRailBlock).forEach(block -> hashMap.put((BlockRail)block, (BlockFace)((Object)blockFace)));
        });
        return hashMap;
    }

    protected Map<BlockRail, BlockFace> checkRailsConnected() {
        Map<BlockRail, BlockFace> map = this.a(this.getOrientation().connectingDirections());
        return map.keySet().stream().filter(blockRail -> blockRail.getOrientation().hasConnectingDirections(((BlockFace)((Object)((Object)map.get(blockRail)))).getOpposite())).collect(Collectors.toMap(blockRail -> blockRail, map::get));
    }

    public boolean isAbstract() {
        return this.getId() == 66;
    }

    public boolean canPowered() {
        return this.canBePowered;
    }

    public Rail.Orientation getOrientation() {
        return Rail.Orientation.byMetadata(this.getRealMeta());
    }

    public void setOrientation(Rail.Orientation orientation) {
        if (orientation.metadata() != this.getRealMeta()) {
            this.setDamage(orientation.metadata());
            this.level.setBlock(this, this, false, true);
        }
    }

    public int getRealMeta() {
        if (!this.isAbstract()) {
            return this.getDamage() & 7;
        }
        return this.getDamage();
    }

    public boolean isActive() {
        return (this.getDamage() & 8) != 0;
    }

    public void setActive(boolean bl) {
        if (bl) {
            this.setDamage(this.getDamage() | 8);
        } else {
            this.setDamage(this.getDamage() & 7);
        }
        this.level.setBlock(this, this, true, true);
    }

    @Override
    public Item toItem() {
        return new ItemBlock((Block)this, 0);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{Item.get(66)};
    }

    @Override
    public BlockFace getBlockFace() {
        return BlockFace.fromHorizontalIndex(this.getDamage() & 7);
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }
}

