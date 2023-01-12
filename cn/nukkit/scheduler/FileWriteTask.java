/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.scheduler;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import cn.nukkit.utils.Utils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileWriteTask
extends AsyncTask {
    private final File d;
    private final InputStream e;

    public FileWriteTask(String string, String string2) {
        this(new File(string), string2);
    }

    public FileWriteTask(String string, byte[] byArray) {
        this(new File(string), byArray);
    }

    public FileWriteTask(String string, InputStream inputStream) {
        this.d = new File(string);
        this.e = inputStream;
    }

    public FileWriteTask(File file, String string) {
        this.d = file;
        this.e = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
    }

    public FileWriteTask(File file, byte[] byArray) {
        this.d = file;
        this.e = new ByteArrayInputStream(byArray);
    }

    public FileWriteTask(File file, InputStream inputStream) {
        this.d = file;
        this.e = inputStream;
    }

    @Override
    public void onRun() {
        try {
            Utils.writeFile(this.d, this.e);
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
        }
    }
}

