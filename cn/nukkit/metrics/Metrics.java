/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.metrics;

import cn.nukkit.utils.MainLogger;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;

public class Metrics {
    private static final int f = 1;
    private final ScheduledExecutorService e = Executors.newScheduledThreadPool(1);
    private static final String g = "https://bStats.org/submitData/server-implementation";
    private final List<CustomChart> a = new ArrayList<CustomChart>();
    private final String b;
    private final String c;
    private final MainLogger d;

    public Metrics(String string, String string2, boolean bl, MainLogger mainLogger) {
        this.b = string;
        this.c = string2;
        this.d = mainLogger;
        this.c();
    }

    public void addCustomChart(CustomChart customChart) {
        if (customChart == null) {
            throw new IllegalArgumentException("Chart cannot be null!");
        }
        this.a.add(customChart);
    }

    private void c() {
        Runnable runnable = this::d;
        long l = (long)(60000.0 * (3.0 + Math.random() * 3.0));
        long l2 = (long)(60000.0 * (Math.random() * 30.0));
        this.e.schedule(runnable, l, TimeUnit.MILLISECONDS);
        this.e.scheduleAtFixedRate(runnable, l + l2, 1800000L, TimeUnit.MILLISECONDS);
        this.d.debug("Metrics started");
    }

    private JSONObject b() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("pluginName", this.b);
        JSONArray jSONArray = new JSONArray();
        for (CustomChart customChart : this.a) {
            JSONObject jSONObject2 = customChart.a();
            if (jSONObject2 == null) continue;
            jSONArray.add(jSONObject2);
        }
        jSONObject.put("customCharts", jSONArray);
        return jSONObject;
    }

    private JSONObject a() {
        String string = System.getProperty("os.name");
        String string2 = System.getProperty("os.arch");
        String string3 = System.getProperty("os.version");
        int n = Runtime.getRuntime().availableProcessors();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("serverUUID", this.c);
        jSONObject.put("osName", string);
        jSONObject.put("osArch", string2);
        jSONObject.put("osVersion", string3);
        jSONObject.put("coreCount", n);
        return jSONObject;
    }

    private void d() {
        JSONObject jSONObject = this.a();
        JSONArray jSONArray = new JSONArray();
        jSONArray.add(this.b());
        jSONObject.put("plugins", jSONArray);
        try {
            Metrics.a(jSONObject);
        }
        catch (Exception exception) {
            this.d.debug("Could not submit stats of " + this.b, exception);
        }
    }

    private static void a(JSONObject jSONObject) throws Exception {
        if (jSONObject == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL("https://bStats.org/submitData/server-implementation").openConnection();
        byte[] byArray = Metrics.a(jSONObject.toString());
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.addRequestProperty("Accept", "application/json");
        httpsURLConnection.addRequestProperty("Connection", "close");
        httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
        httpsURLConnection.addRequestProperty("Content-Length", String.valueOf(byArray.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("User-Agent", "MC-Server/1");
        httpsURLConnection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
        dataOutputStream.write(byArray);
        dataOutputStream.flush();
        dataOutputStream.close();
        httpsURLConnection.getInputStream().close();
    }

    private static byte[] a(String string) throws IOException {
        if (string == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
        gZIPOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static Exception a(Exception exception) {
        return exception;
    }

    public static class AdvancedBarChart
    extends CustomChart {
        private final Callable<Map<String, int[]>> b;

        public AdvancedBarChart(String string, Callable<Map<String, int[]>> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            Map<String, int[]> map = this.b.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean bl = true;
            for (Map.Entry<String, int[]> entry : map.entrySet()) {
                if (entry.getValue().length == 0) continue;
                bl = false;
                JSONArray jSONArray = new JSONArray();
                for (int n : entry.getValue()) {
                    jSONArray.add(n);
                }
                jSONObject2.put(entry.getKey(), jSONArray);
            }
            if (bl) {
                return null;
            }
            jSONObject.put("values", jSONObject2);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class SimpleBarChart
    extends CustomChart {
        private final Callable<Map<String, Integer>> b;

        public SimpleBarChart(String string, Callable<Map<String, Integer>> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            Map<String, Integer> map = this.b.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                JSONArray jSONArray = new JSONArray();
                jSONArray.add(entry.getValue());
                jSONObject2.put(entry.getKey(), jSONArray);
            }
            jSONObject.put("values", jSONObject2);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class MultiLineChart
    extends CustomChart {
        private final Callable<Map<String, Integer>> b;

        public MultiLineChart(String string, Callable<Map<String, Integer>> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            Map<String, Integer> map = this.b.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean bl = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) continue;
                bl = false;
                jSONObject2.put(entry.getKey(), entry.getValue());
            }
            if (bl) {
                return null;
            }
            jSONObject.put("values", jSONObject2);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class SingleLineChart
    extends CustomChart {
        private final Callable<Integer> b;

        public SingleLineChart(String string, Callable<Integer> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            int n = this.b.call();
            if (n == 0) {
                return null;
            }
            jSONObject.put("value", n);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class DrilldownPie
    extends CustomChart {
        private final Callable<Map<String, Map<String, Integer>>> b;

        public DrilldownPie(String string, Callable<Map<String, Map<String, Integer>>> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        public JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            Map<String, Map<String, Integer>> map = this.b.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean bl = true;
            for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
                JSONObject jSONObject3 = new JSONObject();
                boolean bl2 = true;
                for (Map.Entry<String, Integer> entry2 : map.get(entry.getKey()).entrySet()) {
                    jSONObject3.put(entry2.getKey(), entry2.getValue());
                    bl2 = false;
                }
                if (bl2) continue;
                bl = false;
                jSONObject2.put(entry.getKey(), jSONObject3);
            }
            if (bl) {
                return null;
            }
            jSONObject.put("values", jSONObject2);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class AdvancedPie
    extends CustomChart {
        private final Callable<Map<String, Integer>> b;

        public AdvancedPie(String string, Callable<Map<String, Integer>> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            Map<String, Integer> map = this.b.call();
            if (map == null || map.isEmpty()) {
                return null;
            }
            boolean bl = true;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 0) continue;
                bl = false;
                jSONObject2.put(entry.getKey(), entry.getValue());
            }
            if (bl) {
                return null;
            }
            jSONObject.put("values", jSONObject2);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static class SimplePie
    extends CustomChart {
        private final Callable<String> b;

        public SimplePie(String string, Callable<String> callable) {
            super(string);
            this.b = callable;
        }

        @Override
        protected JSONObject getChartData() throws Exception {
            JSONObject jSONObject = new JSONObject();
            String string = this.b.call();
            if (string == null || string.isEmpty()) {
                return null;
            }
            jSONObject.put("value", string);
            return jSONObject;
        }

        private static Exception a(Exception exception) {
            return exception;
        }
    }

    public static abstract class CustomChart {
        final String a;

        CustomChart(String string) {
            if (string == null || string.isEmpty()) {
                throw new IllegalArgumentException("ChartId cannot be null or empty!");
            }
            this.a = string;
        }

        private JSONObject a() {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("chartId", this.a);
            try {
                JSONObject jSONObject2 = this.getChartData();
                if (jSONObject2 == null) {
                    return null;
                }
                jSONObject.put("data", jSONObject2);
            }
            catch (Throwable throwable) {
                return null;
            }
            return jSONObject;
        }

        protected abstract JSONObject getChartData() throws Exception;

        private static IllegalArgumentException a(IllegalArgumentException illegalArgumentException) {
            return illegalArgumentException;
        }
    }
}

