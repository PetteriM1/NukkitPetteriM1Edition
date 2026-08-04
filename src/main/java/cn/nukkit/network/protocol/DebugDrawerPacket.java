package cn.nukkit.network.protocol;

import cn.nukkit.network.protocol.types.debugshape.*;
import cn.nukkit.utils.BinaryStream;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DebugDrawerPacket extends DataPacket {

    public static final byte NETWORK_ID = ProtocolInfo.__INTERNAL__SERVER_SCRIPT_DEBUG_DRAWER_PACKET;

    @Getter
    @Setter
    private List<DebugShape> shapes = new ArrayList<>();

    @Override
    public byte pid() {
        return NETWORK_ID;
    }

    @Override
    public void decode() {
        this.decodeUnsupported();
    }

    @Override
    public void encode() {
        this.reset();
        this.putUnsignedVarInt(this.shapes.size());
        for (DebugShape shape : this.shapes) {
            putUnsignedVarLong(shape.getId());

            if (shape.getType() != null) {
                putBoolean(true);
                putByte((byte) shape.getType().ordinal());
            } else putBoolean(false);

            putOptionalNull(shape.getPosition(), BinaryStream::putVector3f);
            putOptionalNull(shape.getScale(), BinaryStream::putLFloat);
            putOptionalNull(shape.getRotation(), BinaryStream::putVector3f);
            putOptionalNull(shape.getTotalTimeLeft(), BinaryStream::putLFloat);

            if (protocol >= ProtocolInfo.v1_26_20_26) {
                putOptionalNull(shape.getMaximumRenderDistance(), BinaryStream::putLFloat);
            }

            if (shape.getColor() != null) {
                putBoolean(true);
                putLInt(shape.getColor().getRGB());
            } else putBoolean(false);

            if (protocol >= ProtocolInfo.v1_21_120) {
                if (protocol >= ProtocolInfo.v1_26_0) {
                    putOptionalNull(shape.getDimension(), BinaryStream::putVarInt);
                } else {
                    putVarInt(shape.getDimension() == null ? 0 : shape.getDimension());
                }
            }

            if (protocol >= ProtocolInfo.v1_26_0) {
                if (shape.getAttachedToEntityId() != null) {
                    putBoolean(true);
                    putUnsignedVarLong(shape.getAttachedToEntityId());
                } else putBoolean(false);
            }

            if (protocol >= ProtocolInfo.v1_21_120) {
                putUnsignedVarInt(toPayloadType(shape.getType()));
            }

            if (shape.getType() == null) {
                continue;
            }

            if (protocol >= ProtocolInfo.v1_21_120) {
                switch (shape.getType()) {
                    case ARROW:
                        DebugArrow arrow = (DebugArrow) shape;
                        putOptionalNull(arrow.getArrowEndPosition(), BinaryStream::putVector3f);
                        putOptionalNull(arrow.getArrowHeadLength(), BinaryStream::putLFloat);
                        putOptionalNull(arrow.getArrowHeadRadius(), BinaryStream::putLFloat);

                        if (arrow.getArrowHeadSegments() != null) {
                            putBoolean(true);
                            putByte(arrow.getArrowHeadSegments().byteValue());
                        } else putBoolean(false);
                        break;
                    case BOX:
                        DebugBox box = (DebugBox) shape;
                        putVector3f(box.getBoxBounds());
                        break;
                    case CIRCLE:
                        DebugCircle circle = (DebugCircle) shape;
                        putByte(circle.getSegments().byteValue());
                        break;
                    case LINE:
                        DebugLine line = (DebugLine) shape;
                        putVector3f(line.getLineEndPosition());
                        break;
                    case SPHERE:
                        DebugSphere sphere = (DebugSphere) shape;
                        putByte(sphere.getSegments().byteValue());
                        break;
                    case TEXT:
                        DebugText text = (DebugText) shape;
                        putString(text.getText());

                        if (protocol >= ProtocolInfo.v1_26_20_26) {
                            putBoolean(text.isUseRotation());

                            if (text.getBackgroundColor() != null) {
                                putBoolean(true);
                                putLInt(text.getBackgroundColor().getRGB());
                            } else putBoolean(false);

                            putBoolean(text.isDepthTest());
                            putBoolean(text.isShowBackface());
                            putBoolean(text.isShowTextBackface());
                        }
                        break;
                }
            } else {
                switch (shape.getType()) {
                    case ARROW:
                        DebugArrow arrow = (DebugArrow) shape;
                        putBoolean(false);
                        putBoolean(false);
                        putOptionalNull(arrow.getArrowEndPosition(), BinaryStream::putVector3f);
                        putOptionalNull(arrow.getArrowHeadLength(), BinaryStream::putLFloat);
                        putOptionalNull(arrow.getArrowHeadRadius(), BinaryStream::putLFloat);

                        if (arrow.getArrowHeadSegments() != null) {
                            putBoolean(true);
                            putByte(arrow.getArrowHeadSegments().byteValue());
                        } else putBoolean(false);
                        break;
                    case BOX:
                        DebugBox box = (DebugBox) shape;
                        putBoolean(false);
                        putOptionalNull(box.getBoxBounds(), BinaryStream::putVector3f);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        break;
                    case CIRCLE:
                        DebugCircle circle = (DebugCircle) shape;
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);

                        if (circle.getSegments() != null) {
                            putBoolean(true);
                            putByte(circle.getSegments().byteValue());
                        } else putBoolean(false);
                        break;
                    case LINE:
                        DebugLine line = (DebugLine) shape;
                        putBoolean(false);
                        putBoolean(false);
                        putOptionalNull(line.getLineEndPosition(), BinaryStream::putVector3f);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        break;
                    case SPHERE:
                        DebugSphere sphere = (DebugSphere) shape;
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);


                        if (sphere.getSegments() != null) {
                            putBoolean(true);
                            putByte(sphere.getSegments().byteValue());
                        } else putBoolean(false);
                        break;
                    case TEXT:
                        DebugText text = (DebugText) shape;
                        putOptionalNull(text.getText(), BinaryStream::putString);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        putBoolean(false);
                        break;
                }
            }
        }
    }

    private static int toPayloadType(DebugShape.Type type) {
        if (type == null) {
            return 0;
        }

        switch (type) {
            case ARROW:
                return 1;
            case TEXT:
                return 2;
            case BOX:
                return 3;
            case LINE:
                return 4;
            case SPHERE:
            case CIRCLE:
                return 5;
            default:
                throw new IllegalStateException("Unknown debug shape type: " + type);
        }
    }
}
