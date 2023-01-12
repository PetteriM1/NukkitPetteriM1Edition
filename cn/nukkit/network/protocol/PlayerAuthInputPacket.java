/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.d;
import cn.nukkit.network.protocol.types.AuthInputAction;
import cn.nukkit.network.protocol.types.AuthInteractionModel;
import cn.nukkit.network.protocol.types.ClientPlayMode;
import cn.nukkit.network.protocol.types.InputMode;
import cn.nukkit.network.protocol.types.PlayerActionType;
import cn.nukkit.network.protocol.types.PlayerBlockActionData;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class PlayerAuthInputPacket
extends DataPacket {
    public static final byte NETWORK_ID = -112;
    private float p;
    private float f;
    private float o;
    private Vector3f h;
    private Vector2 q;
    private final Set<AuthInputAction> e = EnumSet.noneOf(AuthInputAction.class);
    private InputMode n;
    private ClientPlayMode i;
    private AuthInteractionModel m;
    private Vector3f j;
    private long k;
    private Vector3f l;
    private final Map<PlayerActionType, PlayerBlockActionData> g = new EnumMap<PlayerActionType, PlayerBlockActionData>(PlayerActionType.class);

    @Override
    public byte pid() {
        return -112;
    }

    @Override
    public void decode() {
        int n;
        this.f = this.getLFloat();
        this.p = this.getLFloat();
        this.h = this.getVector3f();
        this.q = new Vector2(this.getLFloat(), this.getLFloat());
        this.o = this.getLFloat();
        long l = this.getUnsignedVarLong();
        for (n = 0; n < AuthInputAction.size(); ++n) {
            if ((l & 1L << n) == 0L) continue;
            this.e.add(AuthInputAction.from(n));
        }
        this.n = InputMode.fromOrdinal((int)this.getUnsignedVarInt());
        this.i = ClientPlayMode.fromOrdinal((int)this.getUnsignedVarInt());
        this.m = this.protocol >= 524 ? AuthInteractionModel.fromOrdinal((int)this.getUnsignedVarInt()) : AuthInteractionModel.CLASSIC;
        if (this.i == ClientPlayMode.REALITY) {
            this.j = this.getVector3f();
        }
        this.k = this.getUnsignedVarLong();
        this.l = this.getVector3f();
        if (this.e.contains((Object)AuthInputAction.PERFORM_BLOCK_ACTIONS)) {
            n = this.getVarInt();
            if (n > 512) {
                throw new IllegalArgumentException("PlayerAuthInputPacket PERFORM_BLOCK_ACTIONS is too long: " + n);
            }
            block4: for (int k = 0; k < n; ++k) {
                PlayerActionType playerActionType = PlayerActionType.from(this.getVarInt());
                switch (d.a[playerActionType.ordinal()]) {
                    case 1: 
                    case 2: 
                    case 3: 
                    case 4: 
                    case 5: {
                        this.g.put(playerActionType, new PlayerBlockActionData(playerActionType, this.getSignedBlockPosition(), this.getVarInt()));
                        continue block4;
                    }
                    default: {
                        this.g.put(playerActionType, new PlayerBlockActionData(playerActionType, null, -1));
                    }
                }
            }
        }
    }

    @Override
    public void encode() {
        this.a();
    }

    public String toString() {
        return "PlayerAuthInputPacket(yaw=" + this.getYaw() + ", pitch=" + this.getPitch() + ", headYaw=" + this.getHeadYaw() + ", position=" + this.getPosition() + ", motion=" + this.getMotion() + ", inputData=" + this.getInputData() + ", inputMode=" + (Object)((Object)this.getInputMode()) + ", playMode=" + (Object)((Object)this.getPlayMode()) + ", interactionModel=" + (Object)((Object)this.getInteractionModel()) + ", vrGazeDirection=" + this.getVrGazeDirection() + ", tick=" + this.getTick() + ", delta=" + this.getDelta() + ", blockActionData=" + this.getBlockActionData() + ")";
    }

    public float getYaw() {
        return this.p;
    }

    public float getPitch() {
        return this.f;
    }

    public float getHeadYaw() {
        return this.o;
    }

    public Vector3f getPosition() {
        return this.h;
    }

    public Vector2 getMotion() {
        return this.q;
    }

    public Set<AuthInputAction> getInputData() {
        return this.e;
    }

    public InputMode getInputMode() {
        return this.n;
    }

    public ClientPlayMode getPlayMode() {
        return this.i;
    }

    public AuthInteractionModel getInteractionModel() {
        return this.m;
    }

    public Vector3f getVrGazeDirection() {
        return this.j;
    }

    public long getTick() {
        return this.k;
    }

    public Vector3f getDelta() {
        return this.l;
    }

    public Map<PlayerActionType, PlayerBlockActionData> getBlockActionData() {
        return this.g;
    }

    private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
        return illegalArgumentException;
    }
}

