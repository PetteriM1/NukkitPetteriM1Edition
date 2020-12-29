package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class ModalFormRequestPacket extends DataPacket {

    public int formId;
    public String data;

    @Override
    public byte pid() {
        return ProtocolInfo.MODAL_FORM_REQUEST_PACKET;
    }

    @Override
    public void decodePayload(int protocolId) {
    }

    @Override
    public void encodePayload(int protocolId) {
        this.putVarInt(this.formId);
        this.putString(this.data);
    }
}
