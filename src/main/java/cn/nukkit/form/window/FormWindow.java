package cn.nukkit.form.window;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.network.protocol.ProtocolInfo;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;

public abstract class FormWindow {

    static final Gson GSON = new Gson();

    private static final Gson noLegacyButtons = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> c) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass() == FormWindowSimple.class && f.getName().equals("buttons");
        }
    }).create();

    private static final Gson noNewElements = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipClass(Class<?> c) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return (f.getDeclaringClass() == FormWindowSimple.class && f.getName().equals("elements")) ||
                    (f.getDeclaringClass() == ElementButton.class && f.getName().equals("type")) ||
                    f.getName().equals("tooltip");
        }
    }).create();

    protected transient boolean closed = false;
    protected final transient List<FormResponseHandler> handlers = new ObjectArrayList<>();

    public abstract void setResponse(String data);

    public List<FormResponseHandler> getHandlers() {
        return handlers;
    }

    public String getJSONData() {
        return getJSONData(ProtocolInfo.CURRENT_PROTOCOL);
    }

    public abstract FormResponse getResponse();

    public void addHandler(FormResponseHandler handler) {
        this.handlers.add(handler);
    }

    public String getJSONData(int protocol) {
        if (this instanceof FormWindowSimple) {
            if (protocol >= ProtocolInfo.v1_21_70_24) {
                return FormWindow.noLegacyButtons.toJson(this);
            }
            return FormWindow.noNewElements.toJson(this);
        }
        return FormWindow.GSON.toJson(this);
    }

    public boolean wasClosed() {
        return closed;
    }
}
