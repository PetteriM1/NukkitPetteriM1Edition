package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class ChickenPlopSound extends LevelEventSound {
    public ChickenPlopSound(Vector3 pos) {
        this(pos, 0);
    }

    public ChickenPlopSound(Vector3 pos, float pitch) {
        super(pos, LevelSoundEventPacket.SOUND_PLOP, pitch);
    }
}
