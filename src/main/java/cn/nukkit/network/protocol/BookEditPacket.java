package cn.nukkit.network.protocol;

import lombok.ToString;

@ToString
public class BookEditPacket extends DataPacket {

    public Action action;
    public int inventorySlot;
    public int pageNumber;
    public int secondaryPageNumber;

    public String text;
    public String photoName;

    public String title;
    public String author;
    public String xuid;

    @Override
    public byte pid() {
        return ProtocolInfo.BOOK_EDIT_PACKET;
    }

    @Override
    public void decode(int protocolId) {
        this.action = Action.values()[this.getByte()];
        this.inventorySlot = this.getByte();

        switch (this.action) {
            case REPLACE_PAGE:
            case ADD_PAGE:
                this.pageNumber = this.getByte();
                this.text = this.getString();
                this.photoName = this.getString();
                break;
            case DELETE_PAGE:
                this.pageNumber = this.getByte();
                break;
            case SWAP_PAGES:
                this.pageNumber = this.getByte();
                this.secondaryPageNumber = this.getByte();
                break;
            case SIGN_BOOK:
                this.title = this.getString();
                this.author = this.getString();
                this.xuid = this.getString();
                break;
        }
    }

    @Override
    public void encode(int protocolId) {

    }

    public enum Action {
        REPLACE_PAGE,
        ADD_PAGE,
        DELETE_PAGE,
        SWAP_PAGES,
        SIGN_BOOK
    }
}
