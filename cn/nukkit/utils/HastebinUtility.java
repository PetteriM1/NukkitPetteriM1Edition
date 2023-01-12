/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HastebinUtility {
    public static final String BIN_URL;
    public static final String USER_AGENT;
    public static final Pattern PATTERN;

    public static String upload(String string) throws IOException {
        URL uRL = new URL("https://hastebin.com/documents");
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpURLConnection.setDoOutput(true);
        Object object = new DataOutputStream(httpURLConnection.getOutputStream());
        Object object2 = null;
        try {
            ((FilterOutputStream)object).write(string.getBytes());
            ((DataOutputStream)object).flush();
        }
        catch (Throwable throwable) {
            object2 = throwable;
            throw throwable;
        }
        finally {
            if (object != null) {
                if (object2 != null) {
                    try {
                        ((FilterOutputStream)object).close();
                    }
                    catch (Throwable throwable) {
                        ((Throwable)object2).addSuppressed(throwable);
                    }
                } else {
                    ((FilterOutputStream)object).close();
                }
            }
        }
        object2 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        Throwable throwable = null;
        try {
            String string2;
            object = new StringBuilder();
            while ((string2 = ((BufferedReader)object2).readLine()) != null) {
                ((StringBuilder)object).append(string2);
            }
        }
        catch (Throwable throwable2) {
            throwable = throwable2;
            throw throwable2;
        }
        finally {
            if (object2 != null) {
                if (throwable != null) {
                    try {
                        ((BufferedReader)object2).close();
                    }
                    catch (Throwable throwable3) {
                        throwable.addSuppressed(throwable3);
                    }
                } else {
                    ((BufferedReader)object2).close();
                }
            }
        }
        object2 = PATTERN.matcher(((StringBuilder)object).toString());
        if (((Matcher)object2).matches()) {
            return "https://hastebin.com/documents/" + ((Matcher)object2).group(1);
        }
        throw new RuntimeException("Couldn't read response!");
    }

    public static String upload(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList<String>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));){
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                if (string.contains("rcon.password=")) continue;
                arrayList.add(string);
            }
        }
        for (int k = Math.max(0, arrayList.size() - 1000); k < arrayList.size(); ++k) {
            stringBuilder.append((String)arrayList.get(k)).append('\n');
        }
        return HastebinUtility.upload(stringBuilder.toString());
    }

    static {
        USER_AGENT = "Mozilla/5.0";
        BIN_URL = "https://hastebin.com/documents";
        PATTERN = Pattern.compile("\\{\"key\":\"([\\S\\s]*)\"}");
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

