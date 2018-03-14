package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelSoundEventPacket;

public class CowMilkSound extends LevelEventSound {
    public CowMilkSound(Vector3 pos) {
        this(pos, 0);
    }

    public CowMilkSound(Vector3 pos, float pitch) {
        super(pos, LevelSoundEventPacket.SOUND_MILK	, pitch);
    }
}
