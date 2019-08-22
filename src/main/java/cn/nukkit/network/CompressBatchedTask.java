package cn.nukkit.network;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Zlib;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author MagicDroidX
 * Nukkit Project
 */
public class CompressBatchedTask extends AsyncTask {

    public int level;
    public byte[][] data;
    public byte[] finalData;
    public List<InetSocketAddress> targets;

    public CompressBatchedTask(byte[][] data, List<InetSocketAddress> targets, int level) {
        this.data = data;
        this.targets = targets;
        this.level = level;
    }

    @Override
    public void onRun() {
        try {
            this.finalData = Zlib.deflate(this.data, this.level);
            this.data = null;
        } catch (Exception ignored) {}
    }

    @Override
    public void onCompletion(Server server) {
        server.broadcastPacketsCallback(this.finalData, this.targets);
    }
}