package cn.nukkit.resourcepacks;

import cn.nukkit.Server;
import com.google.common.io.Files;

import java.io.File;
import java.util.*;

public class ResourcePackManager {

    private final Map<UUID, ResourcePack> resourcePacksById = new HashMap<>();
    private ResourcePack[] resourcePacks;

    public ResourcePackManager(File path) {
        if (!path.exists()) {
            path.mkdirs();
        } else if (!path.isDirectory()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage()
                    .translateString("nukkit.resources.invalid-path", path.getName()));
        }

        List<ResourcePack> loadedResourcePacks = new ArrayList<>();
        for (File pack : path.listFiles()) {
            try {
                ResourcePack resourcePack = null;

                if (!pack.isDirectory()) {
                    switch (Files.getFileExtension(pack.getName())) {
                        case "zip":
                        case "mcpack":
                            resourcePack = new ZippedResourcePack(pack);
                            break;
                        default:
                            Server.getInstance().getLogger().warning(Server.getInstance().getLanguage()
                                    .translateString("nukkit.resources.unknown-format", pack.getName()));
                            break;
                    }
                }

                if (resourcePack != null) {
                    loadedResourcePacks.add(resourcePack);
                    this.resourcePacksById.put(resourcePack.getPackId(), resourcePack);
                }
            } catch (IllegalArgumentException e) {
                Server.getInstance().getLogger().warning(Server.getInstance().getLanguage()
                        .translateString("nukkit.resources.fail", pack.getName(), e.getMessage()));
            }
        }

        if (Server.getInstance().getPropertyBoolean("chemistry-resources-enabled")) {
            ChemistryResourcePack crp = new ChemistryResourcePack();
            loadedResourcePacks.add(crp);
            this.resourcePacksById.put(UUID.fromString("0fba4063-dba1-4281-9b89-ff9390653530"), crp);
        }

        this.resourcePacks = loadedResourcePacks.toArray(new ResourcePack[0]);
        Server.getInstance().getLogger().info(Server.getInstance().getLanguage()
                .translateString("nukkit.resources.success", String.valueOf(this.resourcePacks.length)));
    }

    public ResourcePack[] getResourceStack() {
        return this.resourcePacks;
    }

    public ResourcePack getPackById(UUID id) {
        return this.resourcePacksById.get(id);
    }
}
