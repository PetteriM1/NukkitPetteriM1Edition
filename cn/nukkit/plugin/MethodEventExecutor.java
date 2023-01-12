/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.event.Event;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.EventExecutor;
import cn.nukkit.utils.EventException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEventExecutor
implements EventExecutor {
    private final Method a;

    public MethodEventExecutor(Method method) {
        this.a = method;
    }

    @Override
    public void execute(Listener listener, Event event) throws EventException {
        try {
            Class<?>[] classArray;
            for (Class<?> clazz : classArray = this.a.getParameterTypes()) {
                if (!clazz.isAssignableFrom(event.getClass())) continue;
                this.a.invoke(listener, event);
                break;
            }
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new EventException(invocationTargetException.getCause());
        }
        catch (ClassCastException classCastException) {
        }
        catch (Throwable throwable) {
            throw new EventException(throwable);
        }
    }

    public Method getMethod() {
        return this.a;
    }

    private static InvocationTargetException a(InvocationTargetException invocationTargetException) {
        return invocationTargetException;
    }
}

