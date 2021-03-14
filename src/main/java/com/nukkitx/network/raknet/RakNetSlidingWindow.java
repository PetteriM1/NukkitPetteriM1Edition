package com.nukkitx.network.raknet;

import static com.nukkitx.network.raknet.RakNetConstants.*;

public class RakNetSlidingWindow {
    private final int mtu;
    private double cwnd;
    private double ssThresh;
    private double estimatedRTT = -1;
    private double lastRTT = -1;
    private double deviationRTT = -1;
    private long oldestUnsentAck;
    private long nextCongestionControlBlock;
    private boolean backoffThisBlock;

    public RakNetSlidingWindow(int mtu) {
        this.mtu = mtu;
        this.cwnd = mtu;
    }

    public int getRetransmissionBandwidth(int unAckedBytes) {
        return unAckedBytes;
    }

    public int getTransmissionBandwidth(int unAckedBytes) {
        if (unAckedBytes <= this.cwnd) {
            return (int) (this.cwnd - unAckedBytes);
        } else {
            return 0;
        }
    }

    public void onPacketReceived(long curTime) {
        if (this.oldestUnsentAck == 0) {
            this.oldestUnsentAck = curTime;
        }
    }

    public void onResend(long curSequenceIndex) {
        if (!this.backoffThisBlock && this.cwnd > this.mtu << 1) {
            this.ssThresh = this.cwnd / 2;

            if (this.ssThresh < this.mtu) {
                this.ssThresh = this.mtu;
            }
            this.cwnd = this.mtu;

            this.nextCongestionControlBlock = curSequenceIndex;
            this.backoffThisBlock = true;
        }
    }

    public void onNak() {
        if (!this.backoffThisBlock) {
            this.ssThresh = this.cwnd / 2D;
        }
    }

    public void onAck(long rtt, long sequenceIndex, long curSequenceIndex) {
        this.lastRTT = rtt;

        if (this.estimatedRTT == -1) {
            this.estimatedRTT = rtt;
            this.deviationRTT = rtt;
        } else {
            double difference = rtt - this.estimatedRTT;
            this.estimatedRTT += 0.5D * difference;
            this.deviationRTT += 0.5 * (Math.abs(difference) - this.deviationRTT);
        }

        boolean isNewCongestionControlPeriod = sequenceIndex > this.nextCongestionControlBlock;

        if (isNewCongestionControlPeriod) {
            this.backoffThisBlock = false;
            this.nextCongestionControlBlock = curSequenceIndex;
        }

        if (this.isInSlowStart()) {
            this.cwnd += this.mtu;

            if (this.cwnd > this.ssThresh && this.ssThresh != 0) {
                this.cwnd = this.ssThresh + this.mtu * this.mtu / this.cwnd;
            }
        } else if (isNewCongestionControlPeriod) {
            this.cwnd += this.mtu * this.mtu / this.cwnd;
        }
    }

    public boolean isInSlowStart() {
        return this.cwnd <= this.ssThresh || this.ssThresh == 0;
    }

    public void onSendAck() {
        this.oldestUnsentAck = 0;
    }

    public long getRtoForRetransmission() {
        if (this.estimatedRTT == -1) {
            return CC_MAXIMUM_THRESHOLD;
        }

        long threshold = (long) ((2.0D * this.estimatedRTT + 4.0D * this.deviationRTT) + CC_ADDITIONAL_VARIANCE);

        return Math.min(threshold, CC_MAXIMUM_THRESHOLD);
    }

    public double getRTT() {
        return this.estimatedRTT;
    }

    public boolean shouldSendAcks(long curTime) {
        long rto = this.getSenderRtoForAck();

        return rto == -1 || curTime >= this.oldestUnsentAck + CC_SYN;
    }

    public long getSenderRtoForAck() {
        if (this.lastRTT == -1) {
            return -1;
        } else {
            return (long) (this.lastRTT + CC_SYN);
        }
    }
}
