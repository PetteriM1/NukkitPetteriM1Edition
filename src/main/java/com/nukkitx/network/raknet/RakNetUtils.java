package com.nukkitx.network.raknet;

import com.nukkitx.network.raknet.util.IntRange;
import io.netty.buffer.ByteBuf;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Queue;

@UtilityClass
public class RakNetUtils {

    public static int writeIntRanges(ByteBuf buffer, Queue<IntRange> ackQueue, int mtu) {
        int lengthIndex = buffer.writerIndex();
        buffer.writeZero(2);
        mtu -= 2;

        int count = 0;
        IntRange ackRange;
        while ((ackRange = ackQueue.poll()) != null) {

            IntRange nextRange;
            while ((nextRange = ackQueue.peek()) != null && (ackRange.end + 1) == nextRange.start) {
                ackQueue.remove();
                ackRange.end = nextRange.end;
            }

            if (ackRange.start == ackRange.end) {
                if (mtu < 4) {
                    break;
                }
                mtu -= 4;

                buffer.writeBoolean(true);
                buffer.writeMediumLE(ackRange.start);
            } else {
                if (mtu < 7) {
                    break;
                }
                mtu -= 7;

                buffer.writeBoolean(false);
                buffer.writeMediumLE(ackRange.start);
                buffer.writeMediumLE(ackRange.end);
            }
            count++;
        }

        int finalIndex = buffer.writerIndex();
        buffer.writerIndex(lengthIndex);
        buffer.writeShort(count);
        buffer.writerIndex(finalIndex);
        return count;
    }

    public static boolean verifyUnconnectedMagic(ByteBuf buffer) {
        byte[] readMagic = new byte[RakNetConstants.RAKNET_UNCONNECTED_MAGIC.length];
        buffer.readBytes(readMagic);

        return Arrays.equals(readMagic, RakNetConstants.RAKNET_UNCONNECTED_MAGIC);
    }

    public static void writeUnconnectedMagic(ByteBuf buffer) {
        buffer.writeBytes(RakNetConstants.RAKNET_UNCONNECTED_MAGIC);
    }

    public static int clamp(int value, int low, int high) {
        return value < low ? low : Math.min(value, high);
    }

    public static int powerOfTwoCeiling(int value) {
        value--;
        value |= value >> 1;
        value |= value >> 2;
        value |= value >> 4;
        value |= value >> 8;
        value |= value >> 16;
        value++;
        return value;
    }
}
