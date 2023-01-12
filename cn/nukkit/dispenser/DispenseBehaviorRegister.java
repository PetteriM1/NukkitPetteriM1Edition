/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.dispenser;

import cn.nukkit.Server;
import cn.nukkit.dispenser.BoatDispenseBehavior;
import cn.nukkit.dispenser.BucketDispenseBehavior;
import cn.nukkit.dispenser.DefaultDispenseBehavior;
import cn.nukkit.dispenser.DispenseBehavior;
import cn.nukkit.dispenser.DyeDispenseBehavior;
import cn.nukkit.dispenser.FireChargeDispenseBehavior;
import cn.nukkit.dispenser.FireworksDispenseBehavior;
import cn.nukkit.dispenser.FlintAndSteelDispenseBehavior;
import cn.nukkit.dispenser.ProjectileDispenseBehavior;
import cn.nukkit.dispenser.ShearsDispenseBehaviour;
import cn.nukkit.dispenser.ShulkerBoxDispenseBehavior;
import cn.nukkit.dispenser.SpawnEggDispenseBehavior;
import cn.nukkit.dispenser.TNTDispenseBehavior;
import cn.nukkit.dispenser.UndyedShulkerBoxDispenseBehavior;
import cn.nukkit.dispenser.a;
import cn.nukkit.dispenser.b;
import cn.nukkit.dispenser.c;
import cn.nukkit.dispenser.d;
import cn.nukkit.dispenser.e;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public final class DispenseBehaviorRegister {
    private static final Int2ObjectMap<DispenseBehavior> b = new Int2ObjectOpenHashMap<DispenseBehavior>();
    private static final DispenseBehavior a = new DefaultDispenseBehavior();

    public static void registerBehavior(int n, DispenseBehavior dispenseBehavior) {
        b.put(n, dispenseBehavior);
    }

    public static DispenseBehavior getBehavior(int n) {
        return b.getOrDefault(n, a);
    }

    public static void removeDispenseBehavior(int n) {
        b.remove(n);
    }

    public static void init() {
        DispenseBehaviorRegister.registerBehavior(333, new BoatDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(325, new BucketDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(351, new DyeDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(401, new FireworksDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(259, new FlintAndSteelDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(218, new ShulkerBoxDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(205, new UndyedShulkerBoxDispenseBehavior());
        if (Server.getInstance().spawnEggsEnabled) {
            DispenseBehaviorRegister.registerBehavior(383, new SpawnEggDispenseBehavior());
        }
        DispenseBehaviorRegister.registerBehavior(46, new TNTDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(385, new FireChargeDispenseBehavior());
        DispenseBehaviorRegister.registerBehavior(359, new ShearsDispenseBehaviour());
        DispenseBehaviorRegister.registerBehavior(262, new a("Arrow"));
        DispenseBehaviorRegister.registerBehavior(344, new ProjectileDispenseBehavior("Egg"));
        DispenseBehaviorRegister.registerBehavior(332, new ProjectileDispenseBehavior("Snowball"));
        DispenseBehaviorRegister.registerBehavior(384, new b("ThrownExpBottle"));
        DispenseBehaviorRegister.registerBehavior(438, new c("ThrownPotion"));
        DispenseBehaviorRegister.registerBehavior(441, new d("LingeringPotion"));
        DispenseBehaviorRegister.registerBehavior(455, new e("ThrownTrident"));
    }
}

