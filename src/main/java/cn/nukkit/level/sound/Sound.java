package cn.nukkit.level.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.DataPacket;

/**
 * Legacy sound class
 *
 * Everything in package cn.nukkit.level.sound will be removed in the future.
 * Please use cn.nukkit.level.Sound class instead.
 *
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class Sound extends Vector3 {

    public Sound() {
        super(0, 0, 0);
    }

    public Sound(double x) {
        super(x, 0, 0);
    }

    public Sound(double x, double y) {
        super(x, y, 0);
    }

    public Sound(double x, double y, double z) {
        super(x, y, z);
    }

    abstract public DataPacket encode();
}
