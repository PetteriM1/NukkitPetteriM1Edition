package com.nukkitx.network.raknet;

import lombok.Getter;

@Getter
public enum RakNetReliability {

    UNRELIABLE(false, false, false, false),
    UNRELIABLE_SEQUENCED(false, false, true, false),
    RELIABLE(true, false, false, false),
    RELIABLE_ORDERED(true, true, false, false),
    RELIABLE_SEQUENCED(true, false, true, false),
    UNRELIABLE_WITH_ACK_RECEIPT(false, false, false, true),
    RELIABLE_WITH_ACK_RECEIPT(true, false, false, true),
    RELIABLE_ORDERED_WITH_ACK_RECEIPT(true, true, false, true);

    private static final RakNetReliability[] VALUES = values();

    final boolean reliable;
    final boolean ordered;
    final boolean sequenced;
    final boolean withAckReceipt;
    final int size;

    RakNetReliability(boolean reliable, boolean ordered, boolean sequenced, boolean withAckReceipt) {
        this.reliable = reliable;
        this.ordered = ordered;
        this.sequenced = sequenced;
        this.withAckReceipt = withAckReceipt;

        int size = 0;
        if (this.reliable) {
            size += 3;
        }

        if (this.sequenced) {
            size += 3;
        }

        if (this.ordered) {
            size += 4;
        }
        this.size = size;
    }

    public static RakNetReliability fromId(int id) {
        if (id < 0 || id > 7) {
            return null;
        }
        return VALUES[id];
    }
}