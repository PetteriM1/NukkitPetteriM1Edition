package cn.nukkit.entity.weather;

/**
 * Created by funcraft on 2016/2/27.
 */
public interface EntityLightningStrike extends EntityWeather {

    void setEffect(boolean e);

    boolean isEffect();
}
