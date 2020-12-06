package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class UpdateTradePacket extends DataPacket {

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
        return ProtocolInfo.UPDATE_TRADE_PACKET;
    }

    @Override
    public void decode(int protocolId) {
    }

    @Override
    public void encode(int protocolId) {
        this.putByte(windowId);
        this.putByte(windowType);
        this.putVarInt(unknownVarInt1);
        if (protocolId < 354) {
            this.putVarInt(unknownVarInt2);
            if (protocolId >= 313) {
                this.putVarInt(unknownVarInt3);
            }
            this.putBoolean(isWilling);
        } else {
            this.putVarInt(tradeTier);
        }
        this.putEntityUniqueId(player);
        this.putEntityUniqueId(trader);
        this.putString(displayName);
        if (protocolId >= 354) {
            this.putBoolean(screen2);
            this.putBoolean(isWilling);
        }
        this.put(this.offers);
    }
}
