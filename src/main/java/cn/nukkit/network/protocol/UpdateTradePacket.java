package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class UpdateTradePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.UPDATE_TRADE_PACKET;

    public byte windowId;
    public byte windowType = 15;
    public int unknownVarInt1;
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
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(unknownVarInt1);
        this.putVarInt(tradeTier);
        this.putEntityUniqueId(trader);
        this.putEntityUniqueId(player);
        this.putString(displayName);
        this.putBoolean(screen2);
        this.putBoolean(isWilling);
        this.put(this.offers);
    }
}
