package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.generic.BaseFullChunk;

public class BiomeCommand extends VanillaCommand {

    public BiomeCommand(String name) {
        super(name, "%nukkit.command.world.biome.description", "%nukkit.command.world.biome.usage");
        this.setPermission("nukkit.command.world.biome");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("debug|set", CommandParamType.STRING, true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(new TranslationContainer("commands.generic.ingame"));
            return false;
        }

        Level level = ((Player) sender).getLevel();
        Position position = ((Player) sender).getPosition();

        BaseFullChunk chunk = level.getChunk(position.getChunkX(), position.getChunkZ(), false);
        if (chunk == null || !chunk.isPopulated() || !chunk.isGenerated()) {
            return false;
        }

        int biomeId = chunk.getBiomeId((int) position.getX() & 0x0f, (int) position.getY(), (int) position.getZ() & 0x0f);
        sender.sendMessage("Current biome is " + biomeId + ", 3dBiomes=" + chunk.has3dBiomes());

        if (args.length > 1 && args[0].equalsIgnoreCase("set")) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    chunk.setBiomeId(x, z, Integer.parseInt(args[1]));
                }
            }
            return true;
        }

        if (args.length < 1 || !args[0].equalsIgnoreCase("debug")) {
            return true;
        }

        int minSection = level.getDimensionData().getMinHeight() >> 4;
        int maxSection = level.getDimensionData().getMaxHeight() >> 4;
        for (int i = minSection; i <= maxSection; i++) {
            int worldY = i << 4;
            int id = chunk.getBiomeId((int) position.getX() & 0x0f, worldY, (int) position.getZ() & 0x0f);
            level.getServer().getLogger().info("Chunk section " + i + ", height=" + worldY + ", biomeId=" + id);
            sender.sendMessage("Chunk section " + i + ", height=" + worldY + ", biomeId=" + id);
        }
        return true;
    }
}
