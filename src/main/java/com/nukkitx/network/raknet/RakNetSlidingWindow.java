package com.nukkitx.network.raknet;

import static com.nukkitx.network.raknet.RakNetConstants.*;

public class RakNetSlidingWindow {

    private final int mtu;
    private double cwnd;
    private double ssThresh;
    private double estimatedRTT = -1;
    private double deviationRTT = -1;
    private long oldestUnsentAck;
    private int nextCongestionControlBlock;
    private boolean backoffThisBlock;

    public RakNetSlidingWindow(int mtu) {
        this.mtu = mtu;
        this.cwnd = mtu;
    }

    @SuppressWarnings("ManualMinMaxCalculation")
    public long getRtoForRetransmission() {
        if (this.estimatedRTT == -1) {
            return CC_MAXIMUM_THRESHOLD;
        }

        long threshold = (long) ((2.0D * this.estimatedRTT + 4.0D * this.deviationRTT) + CC_ADDITIONAL_VARIANCE);
        return threshold > CC_MAXIMUM_THRESHOLD ? CC_MAXIMUM_THRESHOLD : threshold;
    }

    public boolean isInSlowStart() {
        return this.cwnd <= this.ssThresh || this.ssThresh == 0;
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

    public void onAck(long rtt, long sequenceIndex, int curSequenceIndex) {
        if (this.estimatedRTT == -1) {
            this.estimatedRTT = rtt;
            this.deviationRTT = rtt;
        } else {
            double d = 0.05D;
            double difference = rtt - this.estimatedRTT;
            this.estimatedRTT += d * difference;
            this.deviationRTT += d * (Math.abs(difference) - this.deviationRTT);
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

    public void onNak() {
        if (!this.backoffThisBlock) {
            this.ssThresh = this.cwnd / 2D;
        }
    }

    public void onPacketReceived(long curTime) {
        if (this.oldestUnsentAck == 0) {
            this.oldestUnsentAck = curTime;
        }
    }

    public void onResend(int curSequenceIndex) {
        if (!this.backoffThisBlock && this.cwnd > this.mtu << 1) {
            this.ssThresh = this.cwnd / 2D;

            if (this.ssThresh < this.mtu) {
                this.ssThresh = this.mtu;
            }
            this.cwnd = this.mtu;

            this.nextCongestionControlBlock = curSequenceIndex;
            this.backoffThisBlock = true;
        }
    }

    public void onSendAck() {
        this.oldestUnsentAck = 0;
    }
}
