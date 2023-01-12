/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.blockentity.BlockEntitySpawnable;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.ByteTag;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import java.util.Arrays;
import java.util.Objects;

public class BlockEntitySign
extends BlockEntitySpawnable {
    private String[] b;

    public BlockEntitySign(FullChunk fullChunk, CompoundTag compoundTag) {
        super(fullChunk, compoundTag);
    }

    @Override
    protected void initBlockEntity() {
        this.b = new String[4];
        if (!this.namedTag.contains("Text")) {
            for (int k = 1; k <= 4; ++k) {
                String string;
                String string2 = "Text" + k;
                if (!this.namedTag.contains(string2)) continue;
                this.b[k - 1] = string = this.namedTag.getString(string2);
                this.namedTag.remove(string2);
            }
        } else {
            String[] stringArray = this.namedTag.getString("Text").split("\n", 4);
            for (int k = 0; k < this.b.length; ++k) {
                this.b[k] = k < stringArray.length ? stringArray[k] : "";
            }
        }
        if (this.b != null) {
            BlockEntitySign.a(this.b);
        }
        if (!this.namedTag.contains("SignTextColor") || !(this.namedTag.get("SignTextColor") instanceof IntTag)) {
            this.setColor(DyeColor.BLACK.getSignColor());
        }
        if (!this.namedTag.contains("IgnoreLighting") || !(this.namedTag.get("IgnoreLighting") instanceof ByteTag)) {
            this.setGlowing(false);
        }
        super.initBlockEntity();
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        this.namedTag.remove("Creator");
    }

    @Override
    public boolean isBlockEntityValid() {
        return this.getLevelBlock() instanceof BlockSignPost;
    }

    public boolean setText(String ... stringArray) {
        for (int k = 0; k < 4; ++k) {
            this.b[k] = k < stringArray.length ? stringArray[k] : "";
        }
        this.namedTag.putString("Text", String.join((CharSequence)"\n", this.b));
        this.spawnToAll();
        if (this.chunk != null) {
            this.setDirty();
        }
        return true;
    }

    public String[] getText() {
        return this.b;
    }

    public BlockColor getColor() {
        return new BlockColor(this.namedTag.getInt("SignTextColor"), true);
    }

    public void setColor(BlockColor blockColor) {
        this.namedTag.putInt("SignTextColor", blockColor.getARGB());
    }

    public boolean isGlowing() {
        return this.namedTag.getBoolean("IgnoreLighting");
    }

    public void setGlowing(boolean bl) {
        this.namedTag.putBoolean("IgnoreLighting", bl);
    }

    @Override
    public boolean updateCompoundTag(CompoundTag compoundTag, Player player) {
        if (!compoundTag.getString("id").equals("Sign")) {
            return false;
        }
        Object[] objectArray = new String[4];
        Arrays.fill(objectArray, "");
        String[] stringArray = compoundTag.getString("Text").split("\n", 4);
        System.arraycopy(stringArray, 0, objectArray, 0, stringArray.length);
        BlockEntitySign.a((String[])objectArray);
        SignChangeEvent signChangeEvent = new SignChangeEvent(this.getBlock(), player, (String[])objectArray);
        if (!this.namedTag.contains("Creator") || !Objects.equals(player.getUniqueId().toString(), this.namedTag.getString("Creator"))) {
            signChangeEvent.setCancelled();
        }
        if (player.getRemoveFormat()) {
            for (int k = 0; k < objectArray.length; ++k) {
                objectArray[k] = TextFormat.clean((String)objectArray[k]);
            }
        }
        this.server.getPluginManager().callEvent(signChangeEvent);
        if (!signChangeEvent.isCancelled()) {
            this.setText(signChangeEvent.getLines());
            return true;
        }
        return false;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        return new CompoundTag().putString("id", "Sign").putString("Text", this.namedTag.getString("Text")).putInt("SignTextColor", this.getColor().getARGB()).putBoolean("IgnoreLighting", this.isGlowing()).putBoolean("TextIgnoreLegacyBugResolved", true).putInt("x", (int)this.x).putInt("y", (int)this.y).putInt("z", (int)this.z);
    }

    private static void a(String[] stringArray) {
        for (int k = 0; k < stringArray.length; ++k) {
            if (stringArray[k] == null) continue;
            stringArray[k] = Server.getInstance().suomiCraftPEMode() ? stringArray[k].substring(0, Math.min(200, stringArray[k].length())).replace("script>", "").replaceAll("[\\uE000-\\uE0EA]", "") : stringArray[k].substring(0, Math.min(200, stringArray[k].length()));
        }
    }
}

