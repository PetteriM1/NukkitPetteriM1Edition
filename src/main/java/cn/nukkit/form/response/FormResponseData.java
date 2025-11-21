package cn.nukkit.form.response;

public class FormResponseData {

    private final int elementID;
    private final String elementContent;

    public FormResponseData(int id, String content) {
        this.elementID = id;
        this.elementContent = content;
    }

    public String getElementContent() {
        return elementContent;
    }

    public int getElementID() {
        return elementID;
    }
}
