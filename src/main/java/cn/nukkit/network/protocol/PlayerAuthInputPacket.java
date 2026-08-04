package cn.nukkit.network.protocol;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector2f;
import cn.nukkit.math.Vector3f;
import cn.nukkit.network.protocol.types.*;
import lombok.Getter;
import lombok.ToString;

import java.util.*;

@ToString
@Getter
public class PlayerAuthInputPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.PLAYER_AUTH_INPUT_PACKET;

    private static final Vector2f EMPTY_VECTOR2F = new Vector2f();
    private static final Vector3f EMPTY_VECTOR3F = new Vector3f();

    private float yaw;
    private float pitch;
    private float headYaw;
    private Vector3f position;
    private Vector2 motion; // Vector2 for backwards compatibility
    private final Set<AuthInputAction> inputData = EnumSet.noneOf(AuthInputAction.class);
    private InputMode inputMode;
    private ClientPlayMode playMode;
    /**
     * Note: On versions below 1.19.0.29 this will always return AuthInteractionModel.CLASSIC
     */
    private AuthInteractionModel interactionModel;
    private Vector3f vrGazeDirection;
    private long tick;
    private Vector3f delta;
    private final Map<PlayerActionType, PlayerBlockActionData> blockActionData = new EnumMap<>(PlayerActionType.class);
    /**
     * Note: 1.20.60+
     */
    private long predictedVehicle;
    /**
     * Note: 1.20.70.24+
     */
    private Vector2f analogMoveVector = EMPTY_VECTOR2F;
    /**
     * Note: 1.20.70+
     */
    private Vector2f vehicleRotation = EMPTY_VECTOR2F;
    /**
     * Note: 1.21.40+
     */
    private Vector2f interactRotation = EMPTY_VECTOR2F;
    /**
     * Note: 1.21.40+
     */
    private Vector3f cameraOrientation = EMPTY_VECTOR3F;
    /**
     * Note: 1.21.50+
     */
    private Vector2f rawMoveVector = EMPTY_VECTOR2F;

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.pitch = this.getLFloat();
        this.yaw = this.getLFloat();
        this.position = this.getVector3f();
        this.motion = new Vector2(this.getLFloat(), this.getLFloat());
        this.headYaw = this.getLFloat();

        if (protocol < ProtocolInfo.v1_26_40) {
            long inputData = this.getUnsignedVarLong();
            for (int i = 0; i < AuthInputAction.size(); i++) {
                if ((inputData & (1L << i)) != 0) {
                    this.inputData.add(AuthInputAction.from(i));
                }
            }
        } else if (this.getBoolean()) {
            int len = (int) this.getUnsignedVarInt();
            for (int i = 0; i < len; i++) {
                this.inputData.add(AuthInputAction.from(this.getVarInt()));
            }
        }

        this.inputMode = InputMode.fromOrdinal((int) this.getUnsignedVarInt());
        this.playMode = ClientPlayMode.fromOrdinal((int) this.getUnsignedVarInt());

        if (protocol >= ProtocolInfo.v1_26_40) {
            this.interactionModel = AuthInteractionModel.fromOrdinal(this.getVarInt());
        } else if (protocol >= ProtocolInfo.v1_19_0_29) {
            this.interactionModel = AuthInteractionModel.fromOrdinal((int) this.getUnsignedVarInt());
        } else {
            this.interactionModel = AuthInteractionModel.CLASSIC;
        }

        if (protocol >= ProtocolInfo.v1_21_40) {
            this.interactRotation = this.getVector2f();
        } else if (this.playMode == ClientPlayMode.REALITY) {
            this.vrGazeDirection = this.getVector3f();
        }

        this.tick = this.getUnsignedVarLong();
        this.delta = this.getVector3f();

        if (protocol >= ProtocolInfo.v1_26_40 && this.getBoolean() && this.getBoolean()) {
            throw new IllegalStateException("PERFORM_ITEM_INTERACTION unsupported in legacy mode");
        }

        if (protocol >= ProtocolInfo.v1_26_40 && this.getBoolean() && this.getBoolean()) {
            throw new IllegalStateException("PERFORM_ITEM_STACK_REQUEST unsupported in legacy mode");
        }

        if ((protocol >= ProtocolInfo.v1_26_40 && this.getBoolean() && this.getBoolean()) || (protocol < ProtocolInfo.v1_26_40 && this.inputData.contains(AuthInputAction.PERFORM_BLOCK_ACTIONS))) {
            int arraySize = protocol >= ProtocolInfo.v1_26_40 ? (int) this.getUnsignedVarInt() : this.getVarInt();
            if (arraySize > 100) {
                throw new IllegalArgumentException("PlayerAuthInputPacket PERFORM_BLOCK_ACTIONS is too long: " + arraySize);
            }

            for (int i = 0; i < arraySize; i++) {
                PlayerActionType type = PlayerActionType.from(this.getVarInt());
                switch (type) {
                    case START_DESTROY_BLOCK:
                    case ABORT_DESTROY_BLOCK:
                    case CRACK_BLOCK:
                    case PREDICT_DESTROY_BLOCK:
                    case CONTINUE_DESTROY_BLOCK:
                        this.blockActionData.put(type, new PlayerBlockActionData(type, this.getSignedBlockPosition(), this.getVarInt()));
                        break;
                    default:
                        if (protocol >= ProtocolInfo.v1_26_40) {
                            throw new IllegalStateException("Unexpected " + type);
                        }
                        this.blockActionData.put(type, new PlayerBlockActionData(type, null, -1));
                }
            }
        }

        if (protocol >= ProtocolInfo.v1_26_40) {
            if (this.getBoolean() && this.getBoolean()) {
                this.vehicleRotation = this.getVector2f();
            }

            if (this.getBoolean() && this.getBoolean()) {
                this.predictedVehicle = this.getVarLong();
            }
        } else if (protocol >= ProtocolInfo.v1_20_60 && this.inputData.contains(AuthInputAction.IN_CLIENT_PREDICTED_IN_VEHICLE)) {
            if (protocol >= ProtocolInfo.v1_20_70) {
                this.vehicleRotation = this.getVector2f();
            }

            this.predictedVehicle = this.getVarLong();
        }

        if (protocol >= ProtocolInfo.v1_19_70_24) {
            this.analogMoveVector = this.getVector2f();

            if (protocol >= ProtocolInfo.v1_21_40) {
                this.cameraOrientation = this.getVector3f();

                if (protocol >= ProtocolInfo.v1_21_50_28) {
                    this.rawMoveVector = this.getVector2f();
                }
            }
        }
    }

    @Override
    public void encode() {
        this.encodeUnsupported();
    }
}
