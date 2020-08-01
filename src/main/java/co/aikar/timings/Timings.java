/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.command.Command;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.Event;
import cn.nukkit.event.Listener;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.plugin.EventExecutor;
import cn.nukkit.plugin.MethodEventExecutor;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.scheduler.TaskHandler;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import static co.aikar.timings.TimingIdentifier.DEFAULT_GROUP;

public final class Timings {

    private static boolean timingsEnabled = false;
    private static boolean verboseEnabled = false;
    private static boolean privacy;
    private static Set<String> ignoredConfigSections = new HashSet<>();

    private static final int MAX_HISTORY_FRAMES = 12;
    private static int historyInterval = -1;
    private static int historyLength = -1;

    public static final FullServerTickTiming fullServerTickTimer;
    public static Timing timingsTickTimer;
    public static Timing pluginEventTimer;

    public static Timing connectionTimer;
    public static Timing schedulerTimer;
    public static Timing schedulerAsyncTimer;
    public static Timing schedulerSyncTimer;
    public static Timing commandTimer;
    public static Timing serverCommandTimer;
    public static Timing levelSaveTimer;

    public static Timing playerNetworkSendTimer;
    public static Timing playerNetworkReceiveTimer;
    public static Timing playerChunkOrderTimer;
    public static Timing playerChunkSendTimer;
    public static Timing playerCommandTimer;

    public static Timing tickEntityTimer;
    public static Timing tickBlockEntityTimer;
    public static Timing entityMoveTimer;
    public static Timing entityBaseTickTimer;
    public static Timing livingEntityBaseTickTimer;

    public static Timing generationTimer;
    public static Timing populationTimer;
    public static Timing generationCallbackTimer;

    public static Timing permissibleCalculationTimer;
    public static Timing permissionDefaultTimer;

    private static final String empty = "?";

    static {
        setTimingsEnabled(Server.getInstance().getPropertyBoolean("enable-timings", false));
        setVerboseEnabled(Server.getInstance().getPropertyBoolean("timings-verbose", false));
        setHistoryInterval(Server.getInstance().getPropertyInt("timings-history-interval", 6000));
        setHistoryLength(Server.getInstance().getPropertyInt("timings-history-length", 72000));
        privacy = Server.getInstance().getPropertyBoolean("timings-privacy", false);

        Server.getInstance().getLogger().debug("Timings: \n" +
                "Enabled - " + timingsEnabled + '\n' +
                "Verbose - " + verboseEnabled + '\n' +
                "History Interval - " + historyInterval + '\n' +
                "History Length - " + historyLength);

        fullServerTickTimer = new FullServerTickTiming();
        init();
    }

    public static void init() {
        timingsTickTimer = TimingsManager.getTiming(DEFAULT_GROUP.name, "Timings Tick", fullServerTickTimer);
        pluginEventTimer = TimingsManager.getTiming("Plugin Events");

        connectionTimer = TimingsManager.getTiming("Connection Handler");
        schedulerTimer = TimingsManager.getTiming("Scheduler");
        schedulerAsyncTimer = TimingsManager.getTiming("## Scheduler - Async Tasks");
        schedulerSyncTimer = TimingsManager.getTiming("## Scheduler - Sync Tasks");
        commandTimer = TimingsManager.getTiming("Commands");
        serverCommandTimer = TimingsManager.getTiming("Server Command");
        levelSaveTimer = TimingsManager.getTiming("Level Save");

        playerNetworkSendTimer = TimingsManager.getTiming("Player Network Send");
        playerNetworkReceiveTimer = TimingsManager.getTiming("Player Network Receive");
        playerChunkOrderTimer = TimingsManager.getTiming("Player Order Chunks");
        playerChunkSendTimer = TimingsManager.getTiming("Player Send Chunks");
        playerCommandTimer = TimingsManager.getTiming("Player Command");

        tickEntityTimer = TimingsManager.getTiming("## Entity Tick");
        tickBlockEntityTimer = TimingsManager.getTiming("## BlockEntity Tick");
        entityMoveTimer = TimingsManager.getTiming("## Entity Move");
        entityBaseTickTimer = TimingsManager.getTiming("## Entity Base Tick");
        livingEntityBaseTickTimer = TimingsManager.getTiming("## LivingEntity Base Tick");

        generationTimer = TimingsManager.getTiming("Level Generation");
        populationTimer = TimingsManager.getTiming("Level Population");
        generationCallbackTimer = TimingsManager.getTiming("Level Generation Callback");

        permissibleCalculationTimer = TimingsManager.getTiming("Permissible Calculation");
        permissionDefaultTimer = TimingsManager.getTiming("Default Permission Calculation");
    }

    public static boolean isTimingsEnabled() {
        return timingsEnabled;
    }

    public static void setTimingsEnabled(boolean enabled) {
        timingsEnabled = enabled;
        if (enabled) {
            init();
        }
        TimingsManager.reset();
    }

    public static boolean isVerboseEnabled() {
        return verboseEnabled;
    }

    public static void setVerboseEnabled(boolean enabled) {
        verboseEnabled = enabled;
        TimingsManager.needsRecheckEnabled = true;
    }

    public static boolean isPrivacy() {
        return privacy;
    }

    public static Set<String> getIgnoredConfigSections() {
        return ignoredConfigSections;
    }

    public static int getHistoryInterval() {
        return historyInterval;
    }

    public static void setHistoryInterval(int interval) {
        historyInterval = Math.max(1200, interval);
        if (historyLength != -1) {
            setHistoryLength(historyLength);
        }
    }

    public static int getHistoryLength() {
        return historyLength;
    }

    public static void setHistoryLength(int length) {
        int maxLength = historyInterval * MAX_HISTORY_FRAMES;
        if (Server.getInstance().getPropertyBoolean("timings-bypass-max", false)) {
            maxLength = Integer.MAX_VALUE;
        }

        historyLength = Math.max(Math.min(maxLength, length), historyInterval);

        Queue<TimingsHistory> oldQueue = TimingsManager.HISTORY;
        int frames = (historyLength / historyInterval);
        if (length > maxLength) {
            Server.getInstance().getLogger().warning(
                    "Timings Length too high. Requested " + length + ", max is " + maxLength
                            + ". To get longer history, you must increase your interval. Set Interval to "
                            + Math.ceil((float) length / MAX_HISTORY_FRAMES)
                            + " to achieve this length.");
        }

        TimingsManager.HISTORY = new TimingsManager.BoundedQueue<>(frames);
        TimingsManager.HISTORY.addAll(oldQueue);
    }

    public static void reset() {
        TimingsManager.reset();
    }

    public static Timing getCommandTiming(Command command) {
        return TimingsManager.getTiming(DEFAULT_GROUP.name, "Command: " + command.getLabel(), commandTimer);
    }

    public static Timing getTaskTiming(TaskHandler handler, long period) {
        String repeating;
        if (period > 0) {
            repeating = " (interval:" + period + ')';
        } else {
            repeating = " (Single)";
        }

        if (handler.getTask() instanceof PluginTask) {
            String owner = ((PluginTask) handler.getTask()).getOwner().getName();
            return TimingsManager.getTiming(owner, "PluginTask: " + handler.getTaskId() + repeating, schedulerSyncTimer);
        } else if (!handler.isAsynchronous()) {
            return TimingsManager.getTiming(DEFAULT_GROUP.name, "Task: " + handler.getTaskId() + repeating, schedulerSyncTimer);
        } else {
            return null;
        }
    }

    public static Timing getPluginEventTiming(Class<? extends Event> event, Listener listener, EventExecutor executor, Plugin plugin) {
        Timing group = TimingsManager.getTiming(plugin.getName(), "Combined Total", pluginEventTimer);

        return TimingsManager.getTiming(plugin.getName(), "Event: " + listener.getClass().getName() + '.'
                + (executor instanceof MethodEventExecutor ? ((MethodEventExecutor) executor).getMethod().getName() : "???")
                + " (" + (timingsEnabled ? event.getSimpleName() : empty) + ')', group);
    }

    public static Timing getEntityTiming(Entity entity) {
        return TimingsManager.getTiming(DEFAULT_GROUP.name, "## Entity Tick: " + (timingsEnabled ? entity.getClass().getSimpleName() : empty), tickEntityTimer);
    }

    public static Timing getBlockEntityTiming(BlockEntity blockEntity) {
        return TimingsManager.getTiming(DEFAULT_GROUP.name, "## BlockEntity Tick: " + (timingsEnabled ? blockEntity.getClass().getSimpleName() : empty), tickBlockEntityTimer);
    }

    public static Timing getReceiveDataPacketTiming(DataPacket pk) {
        return TimingsManager.getTiming(DEFAULT_GROUP.name, "## Receive Packet: " + (timingsEnabled ? pk.getClass().getSimpleName() : empty), playerNetworkReceiveTimer);
    }

    public static Timing getSendDataPacketTiming(DataPacket pk) {
        return TimingsManager.getTiming(DEFAULT_GROUP.name, "## Send Packet: " + (timingsEnabled ? pk.getClass().getSimpleName() : empty), playerNetworkSendTimer);
    }

    public static void stopServer() {
        setTimingsEnabled(false);
        TimingsManager.recheckEnabled();
    }
}
