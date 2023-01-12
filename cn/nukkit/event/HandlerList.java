/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.event;

import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.RegisteredListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class HandlerList {
    private volatile RegisteredListener[] b = null;
    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> c = new EnumMap(EventPriority.class);
    private static final ArrayList<HandlerList> a = new ArrayList();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void bakeAll() {
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            for (HandlerList handlerList : a) {
                handlerList.bake();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll() {
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            Iterator<HandlerList> iterator = a.iterator();
            while (iterator.hasNext()) {
                HandlerList handlerList;
                HandlerList handlerList2 = handlerList = iterator.next();
                synchronized (handlerList2) {
                    for (List list : handlerList.c.values()) {
                        list.clear();
                    }
                    handlerList.b = null;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll(Plugin plugin) {
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            for (HandlerList handlerList : a) {
                handlerList.unregister(plugin);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregisterAll(Listener listener) {
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            for (HandlerList handlerList : a) {
                handlerList.unregister(listener);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public HandlerList() {
        for (EventPriority eventPriority : EventPriority.values()) {
            this.c.put(eventPriority, new ArrayList());
        }
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            a.add(this);
        }
    }

    public synchronized void register(RegisteredListener registeredListener) {
        if (this.c.get((Object)registeredListener.getPriority()).contains(registeredListener)) {
            throw new IllegalStateException("This listener is already registered to priority " + registeredListener.getPriority().toString());
        }
        this.b = null;
        this.c.get((Object)registeredListener.getPriority()).add(registeredListener);
    }

    public void registerAll(Collection<RegisteredListener> collection) {
        for (RegisteredListener registeredListener : collection) {
            this.register(registeredListener);
        }
    }

    public synchronized void unregister(RegisteredListener registeredListener) {
        block0: {
            if (!this.c.get((Object)registeredListener.getPriority()).remove(registeredListener)) break block0;
            this.b = null;
        }
    }

    public synchronized void unregister(Plugin plugin) {
        block2: {
            boolean bl = false;
            for (List list : this.c.values()) {
                ListIterator listIterator = list.listIterator();
                while (listIterator.hasNext()) {
                    if (!((RegisteredListener)listIterator.next()).getPlugin().equals(plugin)) continue;
                    listIterator.remove();
                    bl = true;
                }
            }
            if (!bl) break block2;
            this.b = null;
        }
    }

    public synchronized void unregister(Listener listener) {
        block2: {
            boolean bl = false;
            for (List list : this.c.values()) {
                ListIterator listIterator = list.listIterator();
                while (listIterator.hasNext()) {
                    if (!((RegisteredListener)listIterator.next()).getListener().equals(listener)) continue;
                    listIterator.remove();
                    bl = true;
                }
            }
            if (!bl) break block2;
            this.b = null;
        }
    }

    public synchronized void bake() {
        if (this.b != null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<EventPriority, ArrayList<RegisteredListener>> entry : this.c.entrySet()) {
            arrayList.addAll(entry.getValue());
        }
        this.b = arrayList.toArray(new RegisteredListener[0]);
    }

    public RegisteredListener[] getRegisteredListeners() {
        RegisteredListener[] registeredListenerArray;
        while (true) {
            registeredListenerArray = this.b;
            if (this.b != null) break;
            this.bake();
        }
        return registeredListenerArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ArrayList<RegisteredListener> getRegisteredListeners(Plugin plugin) {
        ArrayList<RegisteredListener> arrayList = new ArrayList<RegisteredListener>();
        ArrayList<HandlerList> arrayList2 = a;
        synchronized (arrayList2) {
            Iterator<HandlerList> iterator = a.iterator();
            while (iterator.hasNext()) {
                HandlerList handlerList;
                HandlerList handlerList2 = handlerList = iterator.next();
                synchronized (handlerList2) {
                    for (List list : handlerList.c.values()) {
                        for (RegisteredListener registeredListener : list) {
                            if (!registeredListener.getPlugin().equals(plugin)) continue;
                            arrayList.add(registeredListener);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static ArrayList<HandlerList> getHandlerLists() {
        ArrayList<HandlerList> arrayList = a;
        synchronized (arrayList) {
            return new ArrayList<HandlerList>(a);
        }
    }

    private static IllegalStateException a(IllegalStateException illegalStateException) {
        return illegalStateException;
    }
}

