package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class UpdateTradePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_TRADE_PACKET;

    public byte windowId;
    public byte windowType = 15;
    public int size;
    public int unknownVarInt2;
    public int unknownVarInt3;
    public int tradeTier;
    public long trader;
    public long player;
    public String displayName;
    public boolean screen2;
    public boolean isWilling;
    public byte[] offers;

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(size);
        if (protocol < 354) {
            this.putVarInt(unknownVarInt2);
            if (protocol >= 313) {
                this.putVarInt(unknownVarInt3);
            }
            this.putBoolean(isWilling);
        } else {
            this.putVarInt(tradeTier);
        }
        this.putEntityUniqueId(trader);
        this.putEntityUniqueId(player);
        this.putString(displayName);
        if (protocol >= 354) {
            this.putBoolean(screen2);
            this.putBoolean(isWilling);
        }
        this.put(this.offers);
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
