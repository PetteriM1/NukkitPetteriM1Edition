package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class BookEditPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.BOOK_EDIT_PACKET;

    public Action action;
    public int inventorySlot;
    public int pageNumber;
    public int secondaryPageNumber;

    public String text;
    public String photoName;

    public String title;
    public String author;
    public String xuid;

    public enum Action {
        REPLACE_PAGE,
        ADD_PAGE,
        DELETE_PAGE,
        SWAP_PAGES,
        SIGN_BOOK
    }

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        if (protocol >= ProtocolInfo.v1_26_0) {
            this.inventorySlot = this.getVarInt();
            this.action = Action.values()[(int) this.getUnsignedVarInt()];
        } else {
            this.action = Action.values()[this.getByte()];
            this.inventorySlot = this.getByte();
        }

        switch (this.action) {
            case REPLACE_PAGE:
            case ADD_PAGE:
                if (protocol >= ProtocolInfo.v1_26_0) {
                    this.pageNumber = this.getVarInt();
                } else {
                    this.pageNumber = this.getByte();
                }
                this.text = this.getString();
                this.photoName = this.getString();
                break;
            case DELETE_PAGE:
                if (protocol >= ProtocolInfo.v1_26_0) {
                    this.pageNumber = this.getVarInt();
                } else {
                    this.pageNumber = this.getByte();
                }
                break;
            case SWAP_PAGES:
                if (protocol >= ProtocolInfo.v1_26_0) {
                    this.pageNumber = this.getVarInt();
                    this.secondaryPageNumber = this.getVarInt();
                } else {
                    this.pageNumber = this.getByte();
                    this.secondaryPageNumber = this.getByte();
                }
                break;
            case SIGN_BOOK:
                this.title = this.getString();
                this.author = this.getString();
                this.xuid = this.getString();
                break;
        }
    }

    @Override
    public void encode() {
        this.encodeUnsupported();
    }
}
