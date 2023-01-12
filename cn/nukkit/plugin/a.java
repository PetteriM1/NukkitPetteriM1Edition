/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.plugin.Library;

final class a
implements Library {
    final String[] val$split;

    a(String[] stringArray) {
        this.val$split = stringArray;
    }

    @Override
    public String getGroupId() {
        return this.val$split[0];
    }

    @Override
    public String getArtifactId() {
        return this.val$split[1];
    }

    @Override
    public String getVersion() {
        return this.val$split[2];
    }
}

