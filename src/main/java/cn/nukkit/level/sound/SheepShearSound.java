package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class SheepShearSound extends LevelEventSound {
    public SheepShearSound(Vector3 pos) {
        this(pos, 0);
    }

    public SheepShearSound(Vector3 pos, float pitch) {
        super(pos, LevelSoundEventPacket.SOUND_SHEAR, pitch);
    }
}
