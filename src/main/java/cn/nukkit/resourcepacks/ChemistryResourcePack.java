package cn.nukkit.resourcepacks;

import java.util.UUID;

public class ChemistryResourcePack extends AbstractResourcePack {

    @Override
    public int getPackSize() {
        return 0;
    }

    @Override
    public byte[] getSha256() {
        return new byte[0];
    }

    @Override
    public byte[] getPackChunk(int off, int len) {
        return new byte[0];
    }

    @Override
    public String getPackName() {
        return "";
    }

    @Override
    public UUID getPackId() {
        return UUID.fromString("0fba4063-dba1-4281-9b89-ff9390653530");
    }

    @Override
    public String getPackVersion() {
        return "1.0.0";
    }
}
