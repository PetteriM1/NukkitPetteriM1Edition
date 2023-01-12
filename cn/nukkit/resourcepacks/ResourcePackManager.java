/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.resourcepacks;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.resourcepacks.ChemistryResourcePack;
import cn.nukkit.resourcepacks.ResourcePack;
import cn.nukkit.resourcepacks.ZippedResourcePack;
import com.google.common.io.Files;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResourcePackManager {
    @NOBF
    private final Map<UUID, ResourcePack> resourcePacksById = new HashMap<UUID, ResourcePack>();
    @NOBF
    private ResourcePack[] resourcePacks;

    public ResourcePackManager(File file) {
        if (!file.exists()) {
            file.mkdirs();
        } else if (!file.isDirectory()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage().translateString("nukkit.resources.invalid-path", file.getName()));
        }
        ArrayList<ResourcePack> arrayList = new ArrayList<ResourcePack>();
        for (File file2 : file.listFiles()) {
            try {
                ResourcePack illegalArgumentException = null;
                String string = Files.getFileExtension(file2.getName());
                if (!file2.isDirectory() && !string.equals("key")) {
                    switch (string) {
                        case "zip": 
                        case "mcpack": {
                            illegalArgumentException = new ZippedResourcePack(file2);
                            break;
                        }
                        default: {
                            Server.getInstance().getLogger().warning(Server.getInstance().getLanguage().translateString("nukkit.resources.unknown-format", file2.getName()));
                        }
                    }
                }
                if (illegalArgumentException == null) continue;
                arrayList.add(illegalArgumentException);
                this.resourcePacksById.put(illegalArgumentException.getPackId(), illegalArgumentException);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                Server.getInstance().getLogger().warning(Server.getInstance().getLanguage().translateString("nukkit.resources.fail", file2.getName(), illegalArgumentException.getMessage()));
            }
        }
        if (Server.getInstance().getPropertyBoolean("chemistry-resources-enabled")) {
            ChemistryResourcePack chemistryResourcePack = new ChemistryResourcePack();
            arrayList.add(chemistryResourcePack);
            this.resourcePacksById.put(UUID.fromString("0fba4063-dba1-4281-9b89-ff9390653530"), chemistryResourcePack);
        }
        this.resourcePacks = arrayList.toArray(new ResourcePack[0]);
        Server.getInstance().getLogger().info(Server.getInstance().getLanguage().translateString("nukkit.resources.success", String.valueOf(this.resourcePacks.length)));
    }

    public ResourcePack[] getResourceStack() {
        return this.resourcePacks;
    }

    public ResourcePack getPackById(UUID uUID) {
        return this.resourcePacksById.get(uUID);
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

