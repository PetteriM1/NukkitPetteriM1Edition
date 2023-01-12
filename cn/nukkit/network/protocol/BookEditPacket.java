/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.c;

public class BookEditPacket
extends DataPacket {
    public static final byte NETWORK_ID = 97;
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
        return 97;
    }

    @Override
    public void decode() {
        this.action = Action.values()[this.getByte()];
        this.inventorySlot = this.getByte();
        switch (c.a[this.action.ordinal()]) {
            case 1: 
            case 2: {
                this.pageNumber = this.getByte();
                this.text = this.getString();
                this.photoName = this.getString();
                break;
            }
            case 3: {
                this.pageNumber = this.getByte();
                break;
            }
            case 4: {
                this.pageNumber = this.getByte();
                this.secondaryPageNumber = this.getByte();
                break;
            }
            case 5: {
                this.title = this.getString();
                this.author = this.getString();
                this.xuid = this.getString();
            }
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "BookEditPacket(action=" + (Object)((Object)this.action) + ", inventorySlot=" + this.inventorySlot + ", pageNumber=" + this.pageNumber + ", secondaryPageNumber=" + this.secondaryPageNumber + ", text=" + this.text + ", photoName=" + this.photoName + ", title=" + this.title + ", author=" + this.author + ", xuid=" + this.xuid + ")";
    }

    public static enum Action {
        REPLACE_PAGE,
        ADD_PAGE,
        DELETE_PAGE,
        SWAP_PAGES,
        SIGN_BOOK;

    }
}

