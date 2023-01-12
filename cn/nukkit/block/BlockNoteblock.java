/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockSolid;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityMusic;
import cn.nukkit.item.Item;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.BlockEventPacket;
import cn.nukkit.utils.BlockColor;

public class BlockNoteblock
extends BlockSolid {
    @Override
    public String getName() {
        return "Note Block";
    }

    @Override
    public int getId() {
        return 25;
    }

    @Override
    public int getToolType() {
        return 4;
    }

    @Override
    public double getHardness() {
        return 0.8;
    }

    @Override
    public double getResistance() {
        return 4.0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean place(Item item, Block block, Block block2, BlockFace blockFace, double d2, double d3, double d4, Player player) {
        this.getLevel().setBlock(block, this, true);
        BlockEntity.createBlockEntity("Music", this.getChunk(), BlockEntity.getDefaultCompound(this, "Music"), new Object[0]);
        return true;
    }

    public int getStrength() {
        BlockEntityMusic blockEntityMusic = this.a();
        return blockEntityMusic != null ? blockEntityMusic.getPitch() : 0;
    }

    public void increaseStrength() {
        BlockEntityMusic blockEntityMusic = this.a();
        if (blockEntityMusic != null) {
            blockEntityMusic.changePitch();
        }
    }

    public Instrument getInstrument() {
        switch (this.down().getId()) {
            case 41: {
                return Instrument.GLOCKENSPIEL;
            }
            case 82: {
                return Instrument.FLUTE;
            }
            case 174: {
                return Instrument.CHIME;
            }
            case 35: {
                return Instrument.GUITAR;
            }
            case 216: {
                return Instrument.XYLOPHONE;
            }
            case 42: {
                return Instrument.VIBRAPHONE;
            }
            case 88: {
                return Instrument.COW_BELL;
            }
            case 86: {
                return Instrument.DIDGERIDOO;
            }
            case 133: {
                return Instrument.SQUARE_WAVE;
            }
            case 170: {
                return Instrument.BANJO;
            }
            case 89: {
                return Instrument.ELECTRIC_PIANO;
            }
            case 5: 
            case 17: 
            case 25: 
            case 47: 
            case 53: 
            case 54: 
            case 58: 
            case 63: 
            case 64: 
            case 68: 
            case 72: 
            case 84: 
            case 85: 
            case 96: 
            case 99: 
            case 100: 
            case 107: 
            case 134: 
            case 135: 
            case 136: 
            case 146: 
            case 151: 
            case 157: 
            case 158: 
            case 162: 
            case 163: 
            case 164: 
            case 176: 
            case 177: 
            case 178: 
            case 183: 
            case 184: 
            case 185: 
            case 186: 
            case 187: 
            case 193: 
            case 194: 
            case 195: 
            case 196: 
            case 197: {
                return Instrument.BASS;
            }
            case 12: 
            case 13: 
            case 237: {
                return Instrument.DRUM;
            }
            case 20: 
            case 102: 
            case 138: 
            case 160: 
            case 169: 
            case 241: {
                return Instrument.STICKS;
            }
            case 1: 
            case 4: 
            case 7: 
            case 14: 
            case 15: 
            case 16: 
            case 21: 
            case 23: 
            case 24: 
            case 43: 
            case 44: 
            case 45: 
            case 48: 
            case 49: 
            case 52: 
            case 56: 
            case 61: 
            case 62: 
            case 67: 
            case 70: 
            case 73: 
            case 74: 
            case 87: 
            case 98: 
            case 108: 
            case 109: 
            case 112: 
            case 113: 
            case 114: 
            case 116: 
            case 120: 
            case 121: 
            case 125: 
            case 128: 
            case 129: 
            case 130: 
            case 139: 
            case 153: 
            case 155: 
            case 156: 
            case 159: 
            case 168: 
            case 172: 
            case 173: 
            case 179: 
            case 180: 
            case 181: 
            case 182: 
            case 201: 
            case 203: 
            case 206: 
            case 213: 
            case 215: 
            case 236: 
            case 245: 
            case 246: 
            case 251: {
                return Instrument.BASS_DRUM;
            }
        }
        return Instrument.PIANO;
    }

    public void emitSound() {
        if (this.up().getId() != 0) {
            return;
        }
        Instrument instrument = this.getInstrument();
        this.level.addLevelSoundEvent(this, 81, instrument.ordinal() << 8 | this.getStrength());
        BlockEventPacket blockEventPacket = new BlockEventPacket();
        blockEventPacket.x = this.getFloorX();
        blockEventPacket.y = this.getFloorY();
        blockEventPacket.z = this.getFloorZ();
        blockEventPacket.case1 = instrument.ordinal();
        blockEventPacket.case2 = this.getStrength();
        this.getLevel().addChunkPacket(this.getChunkX(), this.getChunkZ(), blockEventPacket);
    }

    @Override
    public boolean onActivate(Item item, Player player) {
        if (player.isSneaking()) {
            return false;
        }
        this.increaseStrength();
        this.emitSound();
        return true;
    }

    @Override
    public int onUpdate(int n) {
        BlockEntityMusic blockEntityMusic;
        if (n == 6 && (blockEntityMusic = this.a()) != null) {
            if (this.getLevel().isBlockPowered(this)) {
                if (!blockEntityMusic.isPowered()) {
                    this.emitSound();
                }
                blockEntityMusic.setPowered(true);
            } else {
                blockEntityMusic.setPowered(false);
            }
        }
        return super.onUpdate(n);
    }

    private BlockEntityMusic a() {
        BlockEntity blockEntity = this.getLevel().getBlockEntity(this);
        if (blockEntity instanceof BlockEntityMusic) {
            return (BlockEntityMusic)blockEntity;
        }
        return null;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.WOOD_BLOCK_COLOR;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    public static enum Instrument {
        PIANO(Sound.NOTE_HARP),
        BASS_DRUM(Sound.NOTE_BD),
        DRUM(Sound.NOTE_SNARE),
        STICKS(Sound.NOTE_HAT),
        BASS(Sound.NOTE_BASS),
        GLOCKENSPIEL(Sound.NOTE_BELL),
        FLUTE(Sound.NOTE_FLUTE),
        CHIME(Sound.NOTE_CHIME),
        GUITAR(Sound.NOTE_GUITAR),
        XYLOPHONE(Sound.NOTE_XYLOPHONE),
        VIBRAPHONE(Sound.NOTE_IRON_XYLOPHONE),
        COW_BELL(Sound.NOTE_COW_BELL),
        DIDGERIDOO(Sound.NOTE_DIDGERIDOO),
        SQUARE_WAVE(Sound.NOTE_BIT),
        BANJO(Sound.NOTE_BANJO),
        ELECTRIC_PIANO(Sound.NOTE_PLING);

        private final Sound b;

        private Instrument(Sound sound) {
            this.b = sound;
        }

        public Sound getSound() {
            return this.b;
        }
    }
}

