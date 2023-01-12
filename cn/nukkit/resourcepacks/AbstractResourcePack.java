/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.resourcepacks;

import cn.nukkit.resourcepacks.ResourcePack;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.UUID;

public abstract class AbstractResourcePack
implements ResourcePack {
    protected JsonObject manifest;
    private UUID a = null;

    protected boolean verifyManifest() {
        if (this.manifest.has("format_version") && this.manifest.has("header") && this.manifest.has("modules")) {
            JsonObject jsonObject = this.manifest.getAsJsonObject("header");
            return jsonObject.has("description") && jsonObject.has("name") && jsonObject.has("uuid") && jsonObject.has("version") && jsonObject.getAsJsonArray("version").size() == 3;
        }
        return false;
    }

    @Override
    public String getPackName() {
        return this.manifest.getAsJsonObject("header").get("name").getAsString();
    }

    @Override
    public UUID getPackId() {
        if (this.a == null) {
            this.a = UUID.fromString(this.manifest.getAsJsonObject("header").get("uuid").getAsString());
        }
        return this.a;
    }

    @Override
    public String getPackVersion() {
        JsonArray jsonArray = this.manifest.getAsJsonObject("header").get("version").getAsJsonArray();
        return String.join((CharSequence)".", jsonArray.get(0).getAsString(), jsonArray.get(1).getAsString(), jsonArray.get(2).getAsString());
    }

    @Override
    public String getEncryptionKey() {
        return "";
    }
}

