/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.item.enchantment;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.EnchantmentBindingCurse;
import cn.nukkit.item.enchantment.EnchantmentDurability;
import cn.nukkit.item.enchantment.EnchantmentEfficiency;
import cn.nukkit.item.enchantment.EnchantmentFireAspect;
import cn.nukkit.item.enchantment.EnchantmentFrostWalker;
import cn.nukkit.item.enchantment.EnchantmentKnockback;
import cn.nukkit.item.enchantment.EnchantmentLure;
import cn.nukkit.item.enchantment.EnchantmentMending;
import cn.nukkit.item.enchantment.EnchantmentSilkTouch;
import cn.nukkit.item.enchantment.EnchantmentSoulSpeed;
import cn.nukkit.item.enchantment.EnchantmentSwiftSneak;
import cn.nukkit.item.enchantment.EnchantmentThorns;
import cn.nukkit.item.enchantment.EnchantmentType;
import cn.nukkit.item.enchantment.EnchantmentVanishingCurse;
import cn.nukkit.item.enchantment.EnchantmentWaterBreath;
import cn.nukkit.item.enchantment.EnchantmentWaterWalker;
import cn.nukkit.item.enchantment.EnchantmentWaterWorker;
import cn.nukkit.item.enchantment.a;
import cn.nukkit.item.enchantment.bow.EnchantmentBowFlame;
import cn.nukkit.item.enchantment.bow.EnchantmentBowInfinity;
import cn.nukkit.item.enchantment.bow.EnchantmentBowKnockback;
import cn.nukkit.item.enchantment.bow.EnchantmentBowPower;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbowMultishot;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbowPiercing;
import cn.nukkit.item.enchantment.crossbow.EnchantmentCrossbowQuickCharge;
import cn.nukkit.item.enchantment.damage.EnchantmentDamageAll;
import cn.nukkit.item.enchantment.damage.EnchantmentDamageArthropods;
import cn.nukkit.item.enchantment.damage.EnchantmentDamageSmite;
import cn.nukkit.item.enchantment.loot.EnchantmentLootDigging;
import cn.nukkit.item.enchantment.loot.EnchantmentLootFishing;
import cn.nukkit.item.enchantment.loot.EnchantmentLootWeapon;
import cn.nukkit.item.enchantment.protection.EnchantmentProtectionAll;
import cn.nukkit.item.enchantment.protection.EnchantmentProtectionExplosion;
import cn.nukkit.item.enchantment.protection.EnchantmentProtectionFall;
import cn.nukkit.item.enchantment.protection.EnchantmentProtectionFire;
import cn.nukkit.item.enchantment.protection.EnchantmentProtectionProjectile;
import cn.nukkit.item.enchantment.trident.EnchantmentTridentChanneling;
import cn.nukkit.item.enchantment.trident.EnchantmentTridentImpaling;
import cn.nukkit.item.enchantment.trident.EnchantmentTridentLoyalty;
import cn.nukkit.item.enchantment.trident.EnchantmentTridentRiptide;
import cn.nukkit.utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class Enchantment
implements Cloneable {
    protected static Enchantment[] enchantments;
    public static final int ID_PROTECTION_ALL = 0;
    public static final int ID_PROTECTION_FIRE = 1;
    public static final int ID_PROTECTION_FALL = 2;
    public static final int ID_PROTECTION_EXPLOSION = 3;
    public static final int ID_PROTECTION_PROJECTILE = 4;
    public static final int ID_THORNS = 5;
    public static final int ID_WATER_BREATHING = 6;
    public static final int ID_WATER_WALKER = 7;
    public static final int ID_WATER_WORKER = 8;
    public static final int ID_DAMAGE_ALL = 9;
    public static final int ID_DAMAGE_SMITE = 10;
    public static final int ID_DAMAGE_ARTHROPODS = 11;
    public static final int ID_KNOCKBACK = 12;
    public static final int ID_FIRE_ASPECT = 13;
    public static final int ID_LOOTING = 14;
    public static final int ID_EFFICIENCY = 15;
    public static final int ID_SILK_TOUCH = 16;
    public static final int ID_DURABILITY = 17;
    public static final int ID_FORTUNE_DIGGING = 18;
    public static final int ID_BOW_POWER = 19;
    public static final int ID_BOW_KNOCKBACK = 20;
    public static final int ID_BOW_FLAME = 21;
    public static final int ID_BOW_INFINITY = 22;
    public static final int ID_FORTUNE_FISHING = 23;
    public static final int ID_LURE = 24;
    public static final int ID_FROST_WALKER = 25;
    public static final int ID_MENDING = 26;
    public static final int ID_BINDING_CURSE = 27;
    public static final int ID_VANISHING_CURSE = 28;
    public static final int ID_TRIDENT_IMPALING = 29;
    public static final int ID_TRIDENT_RIPTIDE = 30;
    public static final int ID_TRIDENT_LOYALTY = 31;
    public static final int ID_TRIDENT_CHANNELING = 32;
    public static final int ID_CROSSBOW_MULTISHOT = 33;
    public static final int ID_CROSSBOW_PIERCING = 34;
    public static final int ID_CROSSBOW_QUICK_CHARGE = 35;
    public static final int ID_SOUL_SPEED = 36;
    public static final int ID_SWIFT_SNEAK = 37;
    public final int id;
    private final Rarity a;
    public EnchantmentType type;
    protected int level = 1;
    protected final String name;
    public static final String[] words;

    public static void init() {
        enchantments = new Enchantment[256];
        Enchantment.enchantments[0] = new EnchantmentProtectionAll();
        Enchantment.enchantments[1] = new EnchantmentProtectionFire();
        Enchantment.enchantments[2] = new EnchantmentProtectionFall();
        Enchantment.enchantments[3] = new EnchantmentProtectionExplosion();
        Enchantment.enchantments[4] = new EnchantmentProtectionProjectile();
        Enchantment.enchantments[5] = new EnchantmentThorns();
        Enchantment.enchantments[6] = new EnchantmentWaterBreath();
        Enchantment.enchantments[8] = new EnchantmentWaterWorker();
        Enchantment.enchantments[7] = new EnchantmentWaterWalker();
        Enchantment.enchantments[9] = new EnchantmentDamageAll();
        Enchantment.enchantments[10] = new EnchantmentDamageSmite();
        Enchantment.enchantments[11] = new EnchantmentDamageArthropods();
        Enchantment.enchantments[12] = new EnchantmentKnockback();
        Enchantment.enchantments[13] = new EnchantmentFireAspect();
        Enchantment.enchantments[14] = new EnchantmentLootWeapon();
        Enchantment.enchantments[15] = new EnchantmentEfficiency();
        Enchantment.enchantments[16] = new EnchantmentSilkTouch();
        Enchantment.enchantments[17] = new EnchantmentDurability();
        Enchantment.enchantments[18] = new EnchantmentLootDigging();
        Enchantment.enchantments[19] = new EnchantmentBowPower();
        Enchantment.enchantments[20] = new EnchantmentBowKnockback();
        Enchantment.enchantments[21] = new EnchantmentBowFlame();
        Enchantment.enchantments[22] = new EnchantmentBowInfinity();
        Enchantment.enchantments[23] = new EnchantmentLootFishing();
        Enchantment.enchantments[24] = new EnchantmentLure();
        Enchantment.enchantments[25] = new EnchantmentFrostWalker();
        Enchantment.enchantments[26] = new EnchantmentMending();
        Enchantment.enchantments[27] = new EnchantmentBindingCurse();
        Enchantment.enchantments[28] = new EnchantmentVanishingCurse();
        Enchantment.enchantments[29] = new EnchantmentTridentImpaling();
        Enchantment.enchantments[31] = new EnchantmentTridentLoyalty();
        Enchantment.enchantments[30] = new EnchantmentTridentRiptide();
        Enchantment.enchantments[32] = new EnchantmentTridentChanneling();
        Enchantment.enchantments[33] = new EnchantmentCrossbowMultishot();
        Enchantment.enchantments[34] = new EnchantmentCrossbowPiercing();
        Enchantment.enchantments[35] = new EnchantmentCrossbowQuickCharge();
        Enchantment.enchantments[36] = new EnchantmentSoulSpeed();
        Enchantment.enchantments[37] = new EnchantmentSwiftSneak();
    }

    public static Enchantment get(int n) {
        Enchantment enchantment = null;
        if (n >= 0 && n < enchantments.length) {
            enchantment = enchantments[n];
        }
        if (enchantment == null) {
            return new a(n);
        }
        return enchantment;
    }

    public static Enchantment getEnchantment(int n) {
        return Enchantment.get(n).clone();
    }

    public static Enchantment[] getEnchantments() {
        ArrayList<Enchantment> arrayList = new ArrayList<Enchantment>();
        for (Enchantment enchantment : enchantments) {
            if (enchantment == null) break;
            arrayList.add(enchantment);
        }
        return arrayList.toArray(new Enchantment[0]);
    }

    protected Enchantment(int n, String string, Rarity rarity, EnchantmentType enchantmentType) {
        this.id = n;
        this.a = rarity;
        this.type = enchantmentType;
        this.name = string;
    }

    public int getLevel() {
        return this.level;
    }

    public Enchantment setLevel(int n) {
        return this.setLevel(n, true);
    }

    public Enchantment setLevel(int n, boolean bl) {
        if (!bl) {
            this.level = n;
            return this;
        }
        this.level = n > this.getMaxLevel() ? this.getMaxLevel() : Math.max(n, this.getMinLevel());
        return this;
    }

    public int getId() {
        return this.id;
    }

    public Rarity getRarity() {
        return this.a;
    }

    public int getWeight() {
        return this.a.getWeight();
    }

    public int getMinLevel() {
        return 1;
    }

    public int getMaxLevel() {
        return 1;
    }

    public int getMaxEnchantableLevel() {
        return this.getMaxLevel();
    }

    public int getMinEnchantAbility(int n) {
        return 1 + n * 10;
    }

    public int getMaxEnchantAbility(int n) {
        return this.getMinEnchantAbility(n) + 5;
    }

    public float getProtectionFactor(EntityDamageEvent entityDamageEvent) {
        return 0.0f;
    }

    public double getDamageBonus(Entity entity) {
        return 0.0;
    }

    public void doPostAttack(Entity entity, Entity entity2) {
    }

    public void doAttack(Entity entity, Entity entity2) {
    }

    public void doPostHurt(Entity entity, Entity entity2) {
    }

    public final boolean isCompatibleWith(Enchantment enchantment) {
        return this.checkCompatibility(enchantment) && enchantment.checkCompatibility(this);
    }

    protected boolean checkCompatibility(Enchantment enchantment) {
        return this != enchantment;
    }

    public String getName() {
        return "%enchantment." + this.name;
    }

    public boolean canEnchant(Item item) {
        return this.type.canEnchantItem(item);
    }

    public boolean isMajor() {
        return false;
    }

    public boolean isTreasure() {
        return false;
    }

    protected Enchantment clone() {
        try {
            return (Enchantment)super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public static String getRandomName() {
        HashSet<String> hashSet = new HashSet<String>();
        while (hashSet.size() < Utils.random.nextInt(3, 6)) {
            hashSet.add(words[Utils.random.nextInt(0, words.length)]);
        }
        CharSequence[] charSequenceArray = hashSet.toArray(new String[0]);
        return String.join((CharSequence)" ", charSequenceArray);
    }

    static {
        words = new String[]{"the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale"};
    }

    public static enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int a;

        private Rarity(int n2) {
            this.a = n2;
        }

        public int getWeight() {
            return this.a;
        }

        public static Rarity fromWeight(int n) {
            if (n < 2) {
                return VERY_RARE;
            }
            if (n < 5) {
                return RARE;
            }
            if (n < 10) {
                return UNCOMMON;
            }
            return COMMON;
        }
    }
}

