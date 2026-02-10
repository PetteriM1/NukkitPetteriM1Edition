package cn.nukkit.entity.data;

import java.util.Objects;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public abstract class EntityData<T> {

    private int id;

    protected EntityData(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityData && ((EntityData) obj).id == this.id && Objects.equals(((EntityData) obj).getData(), this.getData());
    }

    public abstract T getData();

    public int getId() {
        return id;
    }

    public abstract int getType();

    public abstract void setData(T data);

    public EntityData setId(int id) {
        this.id = id;
        return this;
    }
}
