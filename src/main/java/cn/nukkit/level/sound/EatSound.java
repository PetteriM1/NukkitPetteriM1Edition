package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class EatSound extends LevelEventSound {
    public EatSound(Vector3 pos) {
        this(pos, 0);
    }

    public EatSound(Vector3 pos, float pitch) {
        super(pos, LevelSoundEventPacket.SOUND_EAT, pitch);
    }
}
