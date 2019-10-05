package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Utils;

public class ItemBookWritten extends Item {

    public static final int GENERATION_ORIGINAL = 0;
	public static final int GENERATION_COPY = 1;
	public static final int GENERATION_COPY_OF_COPY = 2;
	public static final int GENERATION_TATTERED = 3;

	public static final String TAG_GENERATION = "generation"; //TAG_Int
	public static final String TAG_AUTHOR = "author"; //TAG_String
	public static final String TAG_TITLE = "title"; //TAG_String

    protected boolean isWritten = false;

    public ItemBookWritten() {
        this(0, 1);
    }

    public ItemBookWritten(Integer meta, int count) {
        super(Item.WRITTEN_BOOK, 0, count, "Book");
    }

    public Item writeBook(String author, String title, String[] pages) {
        ListTag<CompoundTag> pageList = new ListTag<>("pages");
        for (String page : pages) {
            pageList.add(new CompoundTag().putString("photoname", "").putString("text", page));
        }
        return writeBook(author, title, pageList);
    }

    public Item writeBook(String author, String title, ListTag<CompoundTag> pages) {
        if (pages.size() > 50 || pages.size() <= 0) return this; //Minecraft does not support more than 50 pages
        if (this.isWritten) return this; //Book content can only be updated once
        CompoundTag tag;
        if (!this.hasCompoundTag()) {
            tag = new CompoundTag();
        } else {
            tag = this.getNamedTag();
        }

        tag.putString(TAG_AUTHOR, author);
        tag.putString(TAG_TITLE, title);
        tag.putList(pages);
        tag.putInt(TAG_GENERATION, GENERATION_ORIGINAL);
        tag.putLong("id", 1095216660480L + Utils.random.nextLong(0L, 2147483647L));

        this.isWritten = true;
        return this.setNamedTag(tag);
    }

    public String getAuthor() {
        if (!this.isWritten) return "";
        return this.getNamedTag().getString(TAG_AUTHOR);
    }

    public String getTitle() {
        if (!this.isWritten) return "Book";
        return this.getNamedTag().getString(TAG_TITLE);
    }

    public int getGeneration() {
        if (!this.isWritten) return 0;
        return this.getNamedTag().getInt(TAG_GENERATION);
    }

    public String[] getPages() {
        if (!this.isWritten) return new String[0];
        ListTag<CompoundTag> tag = (ListTag<CompoundTag>) this.getNamedTag().getList("pages");
        String[] pages = new String[tag.size()];
        int i = 0;
        for (CompoundTag pageCompound : tag.getAll()) {
            pages[i] = pageCompound.getString("text");
            i++;
        }
        return pages;
    }
}