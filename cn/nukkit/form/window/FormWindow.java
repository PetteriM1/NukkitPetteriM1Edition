/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.form.window;

import cn.nukkit.form.handler.FormResponseHandler;
import cn.nukkit.form.response.FormResponse;
import com.google.gson.Gson;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

public abstract class FormWindow {
    static final Gson GSON = new Gson();
    protected transient boolean closed = false;
    protected final transient List<FormResponseHandler> handlers = new ObjectArrayList<FormResponseHandler>();

    public String getJSONData() {
        return GSON.toJson(this);
    }

    public abstract void setResponse(String var1);

    public abstract FormResponse getResponse();

    public boolean wasClosed() {
        return this.closed;
    }

    public void addHandler(FormResponseHandler formResponseHandler) {
        this.handlers.add(formResponseHandler);
    }

    public List<FormResponseHandler> getHandlers() {
        return this.handlers;
    }
}

