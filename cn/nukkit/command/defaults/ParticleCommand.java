/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.defaults.VanillaCommand;
import cn.nukkit.item.Item;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.AngryVillagerParticle;
import cn.nukkit.level.particle.BlockForceFieldParticle;
import cn.nukkit.level.particle.BubbleParticle;
import cn.nukkit.level.particle.CriticalParticle;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.level.particle.EnchantParticle;
import cn.nukkit.level.particle.EnchantmentTableParticle;
import cn.nukkit.level.particle.ExplodeParticle;
import cn.nukkit.level.particle.FlameParticle;
import cn.nukkit.level.particle.HappyVillagerParticle;
import cn.nukkit.level.particle.HeartParticle;
import cn.nukkit.level.particle.HugeExplodeParticle;
import cn.nukkit.level.particle.HugeExplodeSeedParticle;
import cn.nukkit.level.particle.InkParticle;
import cn.nukkit.level.particle.InstantEnchantParticle;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.level.particle.LavaDripParticle;
import cn.nukkit.level.particle.LavaParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.level.particle.PortalParticle;
import cn.nukkit.level.particle.RainSplashParticle;
import cn.nukkit.level.particle.RedstoneParticle;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.level.particle.SplashParticle;
import cn.nukkit.level.particle.SporeParticle;
import cn.nukkit.level.particle.TerrainParticle;
import cn.nukkit.level.particle.WaterDripParticle;
import cn.nukkit.level.particle.WaterParticle;
import cn.nukkit.math.Vector3;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleCommand
extends VanillaCommand {
    private static final String[] i = new String[]{"explode", "hugeexplosion", "hugeexplosionseed", "bubble", "splash", "wake", "water", "crit", "smoke", "spell", "instantspell", "dripwater", "driplava", "townaura", "spore", "portal", "flame", "lava", "reddust", "snowballpoof", "slime", "itembreak", "terrain", "heart", "ink", "droplet", "enchantmenttable", "happyvillager", "angryvillager", "forcefield"};

    public ParticleCommand(String string) {
        super(string, "%nukkit.command.particle.description", "%nukkit.command.particle.usage");
        this.setPermission("nukkit.command.particle");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{new CommandParameter("name", false, i), new CommandParameter("position", CommandParamType.POSITION, false), new CommandParameter("count", CommandParamType.INT, true), new CommandParameter("data", true)});
    }

    @Override
    public boolean execute(CommandSender commandSender, String string, String[] stringArray) {
        Particle particle;
        double d2;
        double d3;
        double d4;
        if (!this.testPermission(commandSender)) {
            return true;
        }
        if (stringArray.length < 4) {
            commandSender.sendMessage(new TranslationContainer("commands.generic.usage", this.usageMessage));
            return true;
        }
        Position position = commandSender instanceof Player ? ((Player)commandSender).getPosition() : new Position(0.0, 0.0, 0.0, commandSender.getServer().getDefaultLevel());
        String string2 = stringArray[0].toLowerCase();
        try {
            d4 = ParticleCommand.a(stringArray[1], position.getX());
            d3 = ParticleCommand.a(stringArray[2], position.getY());
            d2 = ParticleCommand.a(stringArray[3], position.getZ());
        }
        catch (Exception exception) {
            return false;
        }
        Position position2 = new Position(d4, d3, d2, position.getLevel());
        int n = 1;
        if (stringArray.length > 4) {
            try {
                double d5 = Double.parseDouble(stringArray[4]);
                n = (int)d5;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        n = Math.max(1, n);
        int n2 = -1;
        if (stringArray.length > 5) {
            try {
                double d6 = Double.parseDouble(stringArray[8]);
                n2 = (int)d6;
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if ((particle = ParticleCommand.a(string2, position2, n2)) == null) {
            position2.level.addParticleEffect(position2.asVector3f(), stringArray[0], -1L, position2.level.getDimension(), new Player[0]);
            return true;
        }
        commandSender.sendMessage(new TranslationContainer("commands.particle.success", string2, String.valueOf(n)));
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (int k = 0; k < n; ++k) {
            particle.setComponents(position2.x + (double)(threadLocalRandom.nextFloat() * 2.0f - 1.0f), position2.y + (double)(threadLocalRandom.nextFloat() * 2.0f - 1.0f), position2.z + (double)(threadLocalRandom.nextFloat() * 2.0f - 1.0f));
            position2.getLevel().addParticle(particle);
        }
        return true;
    }

    private static Particle a(String stringArray, Vector3 vector3, int n) {
        String[] stringArray2;
        switch (stringArray) {
            case "explode": {
                return new ExplodeParticle(vector3);
            }
            case "hugeexplosion": {
                return new HugeExplodeParticle(vector3);
            }
            case "hugeexplosionseed": {
                return new HugeExplodeSeedParticle(vector3);
            }
            case "bubble": {
                return new BubbleParticle(vector3);
            }
            case "splash": {
                return new SplashParticle(vector3);
            }
            case "wake": 
            case "water": {
                return new WaterParticle(vector3);
            }
            case "crit": {
                return new CriticalParticle(vector3);
            }
            case "smoke": {
                return new SmokeParticle(vector3, n != -1 ? n : 0);
            }
            case "spell": {
                return new EnchantParticle(vector3);
            }
            case "instantspell": {
                return new InstantEnchantParticle(vector3);
            }
            case "dripwater": {
                return new WaterDripParticle(vector3);
            }
            case "driplava": {
                return new LavaDripParticle(vector3);
            }
            case "townaura": 
            case "spore": {
                return new SporeParticle(vector3);
            }
            case "portal": {
                return new PortalParticle(vector3);
            }
            case "flame": {
                return new FlameParticle(vector3);
            }
            case "lava": {
                return new LavaParticle(vector3);
            }
            case "reddust": {
                return new RedstoneParticle(vector3, n != -1 ? n : 1);
            }
            case "snowballpoof": {
                return new ItemBreakParticle(vector3, Item.get(332));
            }
            case "slime": {
                return new ItemBreakParticle(vector3, Item.get(341));
            }
            case "itembreak": {
                if (n == -1 || n == 0) break;
                return new ItemBreakParticle(vector3, Item.get(n));
            }
            case "terrain": {
                if (n == -1 || n == 0) break;
                return new TerrainParticle(vector3, Block.get(n));
            }
            case "heart": {
                return new HeartParticle(vector3, n != -1 ? n : 0);
            }
            case "ink": {
                return new InkParticle(vector3, n != -1 ? n : 0);
            }
            case "droplet": {
                return new RainSplashParticle(vector3);
            }
            case "enchantmenttable": {
                return new EnchantmentTableParticle(vector3);
            }
            case "happyvillager": {
                return new HappyVillagerParticle(vector3);
            }
            case "angryvillager": {
                return new AngryVillagerParticle(vector3);
            }
            case "forcefield": {
                return new BlockForceFieldParticle(vector3);
            }
        }
        if (stringArray.startsWith("iconcrack_")) {
            stringArray2 = stringArray.split("_");
            if (stringArray2.length == 3) {
                return new ItemBreakParticle(vector3, Item.get(Integer.parseInt(stringArray2[1]), Integer.valueOf(stringArray2[2])));
            }
        } else if (stringArray.startsWith("blockcrack_")) {
            stringArray2 = stringArray.split("_");
            if (stringArray2.length == 2) {
                return new TerrainParticle(vector3, Block.get(Integer.parseInt(stringArray2[1]) & 0xFF, Integer.parseInt(stringArray2[1]) >> 12));
            }
        } else if (stringArray.startsWith("blockdust_") && (stringArray2 = stringArray.split("_")).length >= 4) {
            return new DustParticle(vector3, Integer.parseInt(stringArray2[1]) & 0xFF, Integer.parseInt(stringArray2[2]) & 0xFF, Integer.parseInt(stringArray2[3]) & 0xFF, stringArray2.length >= 5 ? Integer.parseInt(stringArray2[4]) & 0xFF : 255);
        }
        return null;
    }

    private static double a(String string, double d2) {
        if (string.startsWith("~")) {
            String string2 = string.substring(1);
            if (string2.isEmpty()) {
                return d2;
            }
            return d2 + Double.parseDouble(string2);
        }
        return Double.parseDouble(string);
    }

    private static Exception a(Exception exception) {
        return exception;
    }
}

