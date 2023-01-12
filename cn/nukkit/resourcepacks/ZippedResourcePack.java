/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.resourcepacks;

import cn.nukkit.Server;
import cn.nukkit.resourcepacks.AbstractResourcePack;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZippedResourcePack
extends AbstractResourcePack {
    private final File c;
    private byte[] b;
    private String d = "";

    public ZippedResourcePack(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage().translateString("nukkit.resources.zip.not-found", file.getName()));
        }
        this.c = file;
        try (ZipFile zipFile = new ZipFile(file);){
            ZipEntry zipEntry2 = zipFile.getEntry("manifest.json");
            if (zipEntry2 == null) {
                zipEntry2 = zipFile.stream().filter(zipEntry -> zipEntry.getName().toLowerCase().endsWith("manifest.json") && !zipEntry.isDirectory()).filter(zipEntry -> {
                    File file = new File(zipEntry.getName());
                    if (!file.getName().equalsIgnoreCase("manifest.json")) {
                        return false;
                    }
                    return file.getParent() == null || file.getParentFile().getParent() == null;
                }).findFirst().orElseThrow(() -> new IllegalArgumentException(Server.getInstance().getLanguage().translateString("nukkit.resources.zip.no-manifest")));
            }
            this.manifest = new JsonParser().parse(new InputStreamReader(zipFile.getInputStream(zipEntry2), StandardCharsets.UTF_8)).getAsJsonObject();
            File file2 = this.c.getParentFile();
            if (file2 == null || !file2.isDirectory()) {
                throw new IOException("Invalid resource pack path");
            }
            File file3 = new File(file2, this.c.getName() + ".key");
            if (file3.exists()) {
                this.d = new String(Files.readAllBytes(file3.toPath()), StandardCharsets.UTF_8);
            }
        }
        catch (IOException iOException) {
            Server.getInstance().getLogger().logException(iOException);
        }
        if (!this.verifyManifest()) {
            throw new IllegalArgumentException(Server.getInstance().getLanguage().translateString("nukkit.resources.zip.invalid-manifest"));
        }
    }

    @Override
    public int getPackSize() {
        return (int)this.c.length();
    }

    @Override
    public byte[] getSha256() {
        if (this.b == null) {
            try {
                this.b = MessageDigest.getInstance("SHA-256").digest(Files.readAllBytes(this.c.toPath()));
            }
            catch (Exception exception) {
                Server.getInstance().getLogger().logException(exception);
            }
        }
        return this.b;
    }

    @Override
    public byte[] getPackChunk(int n, int n2) {
        byte[] byArray = this.getPackSize() - n > n2 ? new byte[n2] : new byte[this.getPackSize() - n];
        try (FileInputStream fileInputStream = new FileInputStream(this.c);){
            fileInputStream.skip(n);
            fileInputStream.read(byArray);
        }
        catch (Exception exception) {
            Server.getInstance().getLogger().logException(exception);
        }
        return byArray;
    }

    @Override
    public String getEncryptionKey() {
        return this.d;
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

