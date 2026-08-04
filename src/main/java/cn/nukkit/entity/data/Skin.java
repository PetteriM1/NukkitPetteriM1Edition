package cn.nukkit.entity.data;

import cn.nukkit.Nukkit;
import cn.nukkit.Server;
import cn.nukkit.nbt.stream.FastByteArrayOutputStream;
import cn.nukkit.utils.*;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
@ToString
public class Skin {

    public static final int SINGLE_SKIN_SIZE = 8192;
    public static final int DOUBLE_SKIN_SIZE = 16384;
    public static final int SKIN_128_64_SIZE = 32768;
    public static final int SKIN_128_128_SIZE = 65536;

    private static final int MAX_DATA_SIZE = 262144;

    private static final Gson GSON = new Gson();

    public static final String GEOMETRY_CUSTOM = convertLegacyGeometryName("geometry.humanoid.custom");
    public static final String GEOMETRY_CUSTOM_SLIM = convertLegacyGeometryName("geometry.humanoid.customSlim");

    public static final String STEVE_SKIN = "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ll9B/5ZfQf+WX0H/lm9b/6p9Zv+Wb1v/lm9b/4dVO/+WX0H/ll9B/5ZfQf+qfWb/lm9b/6p9Zv+Wb1v/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABWScz/VknM/1ZJzP9WScz/KCgo/ygoKP8oKCj/KCgo/wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKio/wDMzP8AzMz/AKio/1ExJf9qQDD/UTEl/2pAMP8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA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";
    public static final String STEVE_GEOMETRY = "{\"geometry.humanoid\":{\"bones\":[{\"name\":\"body\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,16]}]},{\"name\":\"waist\",\"neverRender\":true,\"pivot\":[0,12,0]},{\"name\":\"head\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,24,-4],\"size\":[8,8,8],\"uv\":[0,0]}]},{\"name\":\"hat\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,24,-4],\"size\":[8,8,8],\"uv\":[32,0],\"inflate\":0.5}],\"neverRender\":true},{\"name\":\"rightArm\",\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}]},{\"name\":\"leftArm\",\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}],\"mirror\":true},{\"name\":\"rightLeg\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,16]}]},{\"name\":\"leftLeg\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,16]}],\"mirror\":true}]},\"geometry.cape\":{\"texturewidth\":64,\"textureheight\":32,\"bones\":[{\"name\":\"cape\",\"pivot\":[0,24,-3],\"cubes\":[{\"origin\":[-5,8,-3],\"size\":[10,16,1],\"uv\":[0,0]}],\"material\":\"alpha\"}]},\"geometry.humanoid.custom:geometry.humanoid\":{\"bones\":[{\"name\":\"hat\",\"neverRender\":false,\"material\":\"alpha\",\"pivot\":[0,24,0]},{\"name\":\"leftArm\",\"reset\":true,\"mirror\":false,\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[32,48]}]},{\"name\":\"rightArm\",\"reset\":true,\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,16]}]},{\"name\":\"rightItem\",\"pivot\":[-6,15,1],\"neverRender\":true,\"parent\":\"rightArm\"},{\"name\":\"leftSleeve\",\"pivot\":[5,22,0],\"cubes\":[{\"origin\":[4,12,-2],\"size\":[4,12,4],\"uv\":[48,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightSleeve\",\"pivot\":[-5,22,0],\"cubes\":[{\"origin\":[-8,12,-2],\"size\":[4,12,4],\"uv\":[40,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"leftLeg\",\"reset\":true,\"mirror\":false,\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[16,48]}]},{\"name\":\"leftPants\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,48],\"inflate\":0.25}],\"pos\":[1.9,12,0],\"material\":\"alpha\"},{\"name\":\"rightPants\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,32],\"inflate\":0.25}],\"pos\":[-1.9,12,0],\"material\":\"alpha\"},{\"name\":\"jacket\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,32],\"inflate\":0.25}],\"material\":\"alpha\"}]},\"geometry.humanoid.customSlim:geometry.humanoid\":{\"bones\":[{\"name\":\"hat\",\"neverRender\":false,\"material\":\"alpha\"},{\"name\":\"leftArm\",\"reset\":true,\"mirror\":false,\"pivot\":[5,21.5,0],\"cubes\":[{\"origin\":[4,11.5,-2],\"size\":[3,12,4],\"uv\":[32,48]}]},{\"name\":\"rightArm\",\"reset\":true,\"pivot\":[-5,21.5,0],\"cubes\":[{\"origin\":[-7,11.5,-2],\"size\":[3,12,4],\"uv\":[40,16]}]},{\"pivot\":[-6,14.5,1],\"neverRender\":true,\"name\":\"rightItem\",\"parent\":\"rightArm\"},{\"name\":\"leftSleeve\",\"pivot\":[5,21.5,0],\"cubes\":[{\"origin\":[4,11.5,-2],\"size\":[3,12,4],\"uv\":[48,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightSleeve\",\"pivot\":[-5,21.5,0],\"cubes\":[{\"origin\":[-7,11.5,-2],\"size\":[3,12,4],\"uv\":[40,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"leftLeg\",\"reset\":true,\"mirror\":false,\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[16,48]}]},{\"name\":\"leftPants\",\"pivot\":[1.9,12,0],\"cubes\":[{\"origin\":[-0.1,0,-2],\"size\":[4,12,4],\"uv\":[0,48],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"rightPants\",\"pivot\":[-1.9,12,0],\"cubes\":[{\"origin\":[-3.9,0,-2],\"size\":[4,12,4],\"uv\":[0,32],\"inflate\":0.25}],\"material\":\"alpha\"},{\"name\":\"jacket\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,12,-2],\"size\":[8,12,4],\"uv\":[16,32],\"inflate\":0.25}],\"material\":\"alpha\"}]}}";
    private static final Color DEFAULT_COLOR = new Color(0, true);
    @SuppressWarnings("StaticNonFinalField")
    public static Skin NO_PERSONA_SKIN;
    private boolean noPlayFab; // Don't attempt to generate missing play fab id multiple times
    private String fullSkinId;
    private String skinId;
    private String playFabId = "";
    private String skinResourcePatch = GEOMETRY_CUSTOM;
    private SerializedImage skinData;
    private final List<SkinAnimation> animations = new ArrayList<>();
    private final List<PersonaPiece> personaPieces = new ArrayList<>();
    private final List<PersonaPieceTint> tintColors = new ArrayList<>();
    private SerializedImage capeData;
    private String geometryData;
    private String animationData;
    private boolean premium;
    private boolean persona;
    private boolean capeOnClassic;
    private boolean primaryUser = true;
    private String capeId;
    public boolean isLegacySlim;
    private Color color;
    private String skinColor;
    private String armSize = "wide";
    private boolean trusted = true;
    private String geometryDataEngineVersion = "0.0.0";
    private boolean overridingPlayerAppearance = true;
    @Getter
    @Setter
    private String profileHash = "";

    public static void initDefaultSkin() {
        Skin skin = new Skin();
        skin.setGeometryData(STEVE_GEOMETRY);
        skin.setGeometryName("geometry.humanoid.custom");
        skin.setSkinId("Standard_Custom");
        skin.setSkinData(Base64.getDecoder().decode(STEVE_SKIN));
        NO_PERSONA_SKIN = skin;
    }

    public boolean isValid() {
        return isValid(true);
    }

    public boolean isValid(boolean noLimit) {
        return isValidSkin(noLimit) && isValidResourcePatch() && isValidGeometry();
    }

    private boolean isValidSkin(boolean noLimit) {
        try {
            return skinId != null && !skinId.trim().isEmpty() && skinId.length() < 100 &&
                    skinData != null && skinData.width >= 64 && skinData.height >= 32 && skinData.data.length >= SINGLE_SKIN_SIZE &&
                    geometryData != null && !geometryData.isEmpty() &&
                    (noLimit ||
                            (geometryData.getBytes(StandardCharsets.UTF_8).length <= MAX_DATA_SIZE &&
                                    skinData.width <= 512 && skinData.height <= 512 && skinData.data.length <= MAX_DATA_SIZE &&
                                    (capeData == null || capeData.data.length <= MAX_DATA_SIZE) &&
                                    (animationData == null || animationData.getBytes(StandardCharsets.UTF_8).length <= MAX_DATA_SIZE) &&
                                    animations.size() < 100 && personaPieces.size() < 100 && tintColors.size() < 100)) &&
                    (playFabId == null || playFabId.length() < 100) &&
                    (capeId == null || capeId.length() < 100) &&
                    (skinColor == null || skinColor.length() < 100) &&
                    (armSize == null || armSize.length() < 100) &&
                    (fullSkinId == null || fullSkinId.length() < 200) &&
                    (geometryDataEngineVersion == null || geometryDataEngineVersion.length() < 100) &&
                    (profileHash == null || profileHash.length() < 100);
        } catch (Exception ex) {
            if (Nukkit.DEBUG > 1) Server.getInstance().getLogger().logException(ex);
            return false;
        }
    }

    private boolean isValidResourcePatch() {
        if (skinResourcePatch == null || skinResourcePatch.length() > 1000) {
            return false;
        }
        if (skinResourcePatch == GEOMETRY_CUSTOM) {
            return true;
        }
        try {
            Map<String, Object> geometry = (Map<String, Object>) GSON.fromJson(skinResourcePatch, Map.class).get("geometry");
            if (geometry == null) {
                return false;
            }
            return geometry.get("default") instanceof String;
        } catch (ClassCastException e) {
            return false;
        }
    }

    private boolean isValidGeometry() {
        if (geometryData == null || geometryData.length() < 2) {
            return false;
        }
        if (geometryData == STEVE_GEOMETRY) {
            return true;
        }
        try {
            GSON.fromJson(geometryData, Object.class);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public SerializedImage getSkinData() {
        if (skinData == null) {
            return SerializedImage.EMPTY;
        }
        return skinData;
    }

    public String getSkinId() {
        if (this.skinId == null) {
            Server.getInstance().getLogger().debug("Missing skin ID, generating new");
            this.generateSkinId("Custom");
        }
        return skinId;
    }

    public void setSkinId(String skinId) {
        if (skinId == null || skinId.trim().isEmpty()) {
            if (Nukkit.DEBUG > 1) {
                Server.getInstance().getLogger().warning("Skin ID cannot be empty! ", new Throwable(""));
            }
            return;
        }
        this.skinId = skinId;
    }

    public void generateSkinId(String name) {
        byte[] data = Binary.appendBytes(getSkinData().data, getSkinResourcePatch().getBytes(StandardCharsets.UTF_8));
        this.skinId = UUID.nameUUIDFromBytes(data) + "." + name;
    }

    public void setSkinData(byte[] skinData) {
        setSkinData(SerializedImage.fromLegacy(skinData));
    }

    public void setSkinData(BufferedImage image) {
        setSkinData(parseBufferedImage(image));
    }

    public void setSkinData(SerializedImage skinData) {
        Objects.requireNonNull(skinData, "skinData");
        this.skinData = skinData;
    }

    public void setSkinResourcePatch(String skinResourcePatch) {
        if (skinResourcePatch == null || skinResourcePatch.trim().isEmpty()) {
            this.skinResourcePatch = GEOMETRY_CUSTOM;
            return;
        }
        this.skinResourcePatch = skinResourcePatch;
    }

    public void setGeometryName(String geometryName) {
        // Hack for slim skins on older versions
        if (geometryName.equals("geometry.humanoid.customSlim")) {
            this.isLegacySlim = true;
        }

        if (geometryName.trim().isEmpty()) {
            this.skinResourcePatch = GEOMETRY_CUSTOM;
            return;
        }

        this.skinResourcePatch = "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}";
    }

    public String getSkinResourcePatch() {
        return skinResourcePatch;
    }

    public SerializedImage getCapeData() {
        if (capeData == null) {
            return SerializedImage.EMPTY;
        }
        return capeData;
    }

    public String getCapeId() {
        if (capeId == null) {
            return "";
        }
        return capeId;
    }

    public void setCapeId(String capeId) {
        if (capeId == null || capeId.trim().isEmpty()) {
            capeId = null;
        }
        this.capeId = capeId;
    }

    public void setCapeData(byte[] capeData) {
        Objects.requireNonNull(capeData, "capeData");
        if (capeData.length == SINGLE_SKIN_SIZE) {
            setCapeData(new SerializedImage(64, 32, capeData));
        }
    }

    public void setCapeData(BufferedImage image) {
        setCapeData(parseBufferedImage(image));
    }

    public void setCapeData(SerializedImage capeData) {
        Objects.requireNonNull(capeData, "capeData");
        this.capeData = capeData;
    }

    public String getGeometryData() {
        if (geometryData == null || geometryData.length() < 2) {
            return STEVE_GEOMETRY;
        }
        return geometryData;
    }

    public void setGeometryData(String geometryData) {
        Preconditions.checkNotNull(geometryData, "geometryData");
        if (!geometryData.equals(this.geometryData)) {
            this.geometryData = geometryData;
        }
    }

    public String getAnimationData() {
        if (animationData == null) {
            return "";
        }
        return animationData;
    }

    public void setAnimationData(String animationData) {
        Preconditions.checkNotNull(animationData, "animationData");
        if (!animationData.equals(this.animationData)) {
            this.animationData = animationData;
        }
    }

    public List<SkinAnimation> getAnimations() {
        return animations;
    }

    public List<PersonaPiece> getPersonaPieces() {
        return personaPieces;
    }

    public List<PersonaPieceTint> getTintColors() {
        return tintColors;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isPersona() {
        return persona;
    }

    public void setPersona(boolean persona) {
        this.persona = persona;
    }

    public boolean isCapeOnClassic() {
        return capeOnClassic;
    }

    public void setCapeOnClassic(boolean capeOnClassic) {
        this.capeOnClassic = capeOnClassic;
    }

    public void setPrimaryUser(boolean primaryUser) {
        this.primaryUser = primaryUser;
    }

    public boolean isPrimaryUser() {
        return primaryUser;
    }

    public void setGeometryDataEngineVersion(String geometryDataEngineVersion) {
        this.geometryDataEngineVersion = geometryDataEngineVersion;
    }

    public String getGeometryDataEngineVersion() {
        return geometryDataEngineVersion;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted(boolean trusted) {
        this.trusted = trusted;
    }

    public String getSkinColor() {
        if (skinColor == null || skinColor.isEmpty()) {
            if (color != null && color.getAlpha() != 0) {
                skinColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
            } else {
                skinColor = "#0";
            }
        }
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public Color getColor() {
        if (color == null) {
            if (skinColor != null && !skinColor.isEmpty()) {
                if (skinColor.equals("#0")) {
                    color = new Color(0, true);
                } else {
                    color = new Color((int) Long.parseLong(skinColor.startsWith("#") ? skinColor.substring(1) : skinColor, 16), true);
                }
            } else {
                color = DEFAULT_COLOR;
            }
        }
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getArmSize() {
        return armSize;
    }

    public void setArmSize(String armSize) {
        this.armSize = armSize;
    }

    public void setFullSkinId(String fullSkinId) {
        this.fullSkinId = fullSkinId;
        this.noPlayFab = false; // Allow another attempt to generate it using the new id
    }

    public String getFullSkinId() {
        if (this.fullSkinId == null) {
            this.fullSkinId = this.getSkinId() + this.getCapeId();
            this.noPlayFab = false; // Allow another attempt to generate it using the new id
        }
        return this.fullSkinId;
    }

    public void setPlayFabId(String playFabId) {
        this.playFabId = playFabId;
        this.noPlayFab = false;
    }

    public String getPlayFabId() {
        if (this.noPlayFab) {
            return "";
        }
        if ((this.playFabId == null || this.playFabId.isEmpty()) && this != NO_PERSONA_SKIN) {
            String[] split = this.getFullSkinId().split("-", 6);
            if (split.length > 5) {
                this.playFabId = split[5];
                this.noPlayFab = false;
            } else {
                try {
                    this.playFabId = this.getFullSkinId().replace("-", "").substring(16);
                    this.noPlayFab = false;
                } catch (Exception ignore) {
                    Server.getInstance().getLogger().debug("Couldn't generate Skin playFabId for " + this.getFullSkinId());
                    this.playFabId = "";
                    this.noPlayFab = true;
                }
            }
        }
        return this.playFabId;
    }

    private static SerializedImage parseBufferedImage(BufferedImage image) {
        FastByteArrayOutputStream outputStream = ThreadCache.fbaos.get().reset();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();
        return new SerializedImage(image.getWidth(), image.getHeight(), outputStream.toByteArray());
    }

    private static String convertLegacyGeometryName(String geometryName) {
        return "{\"geometry\" : {\"default\" : \"" + geometryName + "\"}}";
    }

    public void setOverridingPlayerAppearance(boolean overridingPlayerAppearance) {
        this.overridingPlayerAppearance = overridingPlayerAppearance;
    }

    public boolean isOverridingPlayerAppearance() {
        return this.overridingPlayerAppearance;
    }
}
