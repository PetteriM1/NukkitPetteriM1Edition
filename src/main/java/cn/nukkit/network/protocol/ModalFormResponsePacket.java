package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ModalFormResponsePacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.MODAL_FORM_RESPONSE_PACKET;

    public int formId;
    public String data = "null";
    public int cancelReason;

    @Override
    public void decode() {
        this.formId = this.getVarInt();
        if (protocol >= ProtocolInfo.v1_19_20) {
            if (this.getBoolean()) {
                this.data = this.getString();
            }
            if (this.getBoolean()) {
                this.cancelReason = this.getByte();
            }
        } else {
            this.data = this.getString(); // Data will be null if player close form without submit (by cross button or ESC)
        }
    }

    @Override
    public void encode() {
        this.encodeUnsupported();
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }
}
