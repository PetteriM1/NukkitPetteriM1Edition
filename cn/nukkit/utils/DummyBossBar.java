/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Attribute;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.BossEventPacket;
import cn.nukkit.network.protocol.MoveEntityAbsolutePacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.network.protocol.UpdateAttributesPacket;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.BossBarColor;
import cn.nukkit.utils.Utils;
import cn.nukkit.utils.a;

public class DummyBossBar {
    private final Player b;
    private final long a;
    private String c;
    private float d;
    private BossBarColor e;

    private DummyBossBar(Builder builder) {
        this.b = builder.c;
        this.a = builder.a;
        this.c = builder.d;
        this.d = builder.b;
        this.e = builder.e;
    }

    public Player getPlayer() {
        return this.b;
    }

    public long getBossBarId() {
        return this.a;
    }

    public String getText() {
        return this.c;
    }

    public void setText(String string) {
        if (!this.c.equals(string)) {
            this.c = string;
            this.e();
            this.f();
        }
    }

    public float getLength() {
        return this.d;
    }

    public void setLength(float f2) {
        if (this.d != f2) {
            this.d = f2;
            this.d();
            this.b();
        }
    }

    public void setColor(BossBarColor bossBarColor) {
        if (this.e == null || !this.e.equals((Object)bossBarColor)) {
            this.e = bossBarColor;
            this.g();
        }
    }

    public BossBarColor getColor() {
        return this.e;
    }

    public void setColor(BlockColor blockColor) {
        Server.getInstance().getLogger().warning("Unsupported API usage: DummyBossBar.setColor(BlockColor)");
    }

    public void setColor(int n, int n2, int n3) {
        Server.getInstance().getLogger().warning("Unsupported API usage: DummyBossBar.setColor(int,int,int)");
    }

    private void a() {
        AddEntityPacket addEntityPacket = new AddEntityPacket();
        addEntityPacket.type = 33;
        addEntityPacket.entityUniqueId = this.a;
        addEntityPacket.entityRuntimeId = this.a;
        addEntityPacket.x = (float)this.b.x;
        addEntityPacket.y = -10.0f;
        addEntityPacket.z = (float)this.b.z;
        addEntityPacket.speedX = 0.0f;
        addEntityPacket.speedY = 0.0f;
        addEntityPacket.speedZ = 0.0f;
        addEntityPacket.metadata = new EntityMetadata().putLong(0, 0L).putShort(7, 400).putShort(42, 400).putLong(37, -1L).putString(4, this.c).putFloat(38, 0.0f);
        this.b.dataPacket(addEntityPacket);
    }

    private void d() {
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.entityId = this.a;
        Attribute attribute = Attribute.getAttribute(4);
        attribute.setMaxValue(100.0f);
        attribute.setValue(this.d);
        updateAttributesPacket.entries = new Attribute[]{attribute};
        this.b.dataPacket(updateAttributesPacket);
    }

    private void i() {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.bossEid = this.a;
        bossEventPacket.type = 0;
        bossEventPacket.title = this.c;
        bossEventPacket.healthPercent = this.b.protocol >= 361 ? this.d / 100.0f : this.d;
        this.b.dataPacket(bossEventPacket);
    }

    private void h() {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.bossEid = this.a;
        bossEventPacket.type = 2;
        this.b.dataPacket(bossEventPacket);
    }

    private void g() {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.bossEid = this.a;
        bossEventPacket.type = 7;
        bossEventPacket.color = this.e.ordinal();
        this.b.dataPacket(bossEventPacket);
    }

    private void f() {
        BossEventPacket bossEventPacket = new BossEventPacket();
        bossEventPacket.bossEid = this.a;
        bossEventPacket.type = 5;
        bossEventPacket.title = this.c;
        bossEventPacket.healthPercent = this.b.protocol >= 361 ? this.d / 100.0f : this.d;
        this.b.dataPacket(bossEventPacket);
    }

    private void b() {
        if (this.b.protocol >= 361) {
            BossEventPacket bossEventPacket = new BossEventPacket();
            bossEventPacket.bossEid = this.a;
            bossEventPacket.type = 4;
            bossEventPacket.healthPercent = this.d / 100.0f;
            this.b.dataPacket(bossEventPacket);
        }
    }

    public void updateBossEntityPosition() {
        MoveEntityAbsolutePacket moveEntityAbsolutePacket = new MoveEntityAbsolutePacket();
        moveEntityAbsolutePacket.eid = this.a;
        moveEntityAbsolutePacket.x = this.b.x;
        moveEntityAbsolutePacket.y = -10.0;
        moveEntityAbsolutePacket.z = this.b.z;
        moveEntityAbsolutePacket.headYaw = 0.0;
        moveEntityAbsolutePacket.yaw = 0.0;
        moveEntityAbsolutePacket.pitch = 0.0;
        this.b.dataPacket(moveEntityAbsolutePacket);
    }

    private void e() {
        SetEntityDataPacket setEntityDataPacket = new SetEntityDataPacket();
        setEntityDataPacket.eid = this.a;
        setEntityDataPacket.metadata = new EntityMetadata().putString(4, this.c);
        this.b.dataPacket(setEntityDataPacket);
    }

    private void c() {
        RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
        removeEntityPacket.eid = this.a;
        this.b.dataPacket(removeEntityPacket);
    }

    public void create() {
        this.a();
        this.d();
        this.i();
        this.b();
        if (this.e != null) {
            this.g();
        }
    }

    public void reshow() {
        this.updateBossEntityPosition();
        this.i();
        this.b();
    }

    public void destroy() {
        this.h();
        this.c();
    }

    /* synthetic */ DummyBossBar(Builder builder, a a2) {
        this(builder);
    }

    public static class Builder {
        private final Player c;
        private final long a;
        private String d = "";
        private float b = 100.0f;
        private BossBarColor e = null;

        public Builder(Player player) {
            this.c = player;
            this.a = 0xFF00000000L + Utils.random.nextLong(0L, Integer.MAX_VALUE);
        }

        public Builder text(String string) {
            this.d = string;
            return this;
        }

        public Builder length(float f2) {
            if (f2 >= 0.0f && f2 <= 100.0f) {
                this.b = f2;
            }
            return this;
        }

        public Builder color(BossBarColor bossBarColor) {
            this.e = bossBarColor;
            return this;
        }

        public Builder color(BlockColor blockColor) {
            Server.getInstance().getLogger().warning("Unsupported API usage: DummyBossBar.Builder.color(BlockColor)");
            return this;
        }

        public Builder color(int n, int n2, int n3) {
            Server.getInstance().getLogger().warning("Unsupported API usage: DummyBossBar.Builder.color(int,int,int)");
            return this;
        }

        public DummyBossBar build() {
            return new DummyBossBar(this, null);
        }
    }
}

