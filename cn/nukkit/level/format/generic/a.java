/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.level.format.generic;

import cn.nukkit.level.format.generic.BaseLevelProvider;
import java.util.HashMap;

class a
extends HashMap<String, Object> {
    final BaseLevelProvider this$0;

    a(BaseLevelProvider baseLevelProvider) {
        this.this$0 = baseLevelProvider;
        this.put("preset", this.this$0.levelData.getString("generatorOptions"));
    }
}

