/*
 * Decompiled with CFR 0.152.
 */
package cn.nukkit.plugin;

import cn.nukkit.NOBF;
import cn.nukkit.Server;
import cn.nukkit.block.i;
import cn.nukkit.event.plugin.PluginDisableEvent;
import cn.nukkit.event.plugin.PluginEnableEvent;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginClassLoader;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginLoader;
import cn.nukkit.utils.PluginException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JavaPluginLoader
implements PluginLoader {
    private final Server a;
    @NOBF
    private final Map<String, Class> classes = new HashMap<String, Class>();
    @NOBF
    private final Map<String, PluginClassLoader> classLoaders = new HashMap<String, PluginClassLoader>();

    public JavaPluginLoader(Server server) {
        this.a = server;
    }

    @Override
    public Plugin loadPlugin(File file) throws Exception {
        PluginDescription pluginDescription = this.getPluginDescription(file);
        if (pluginDescription != null) {
            this.a.getLogger().info(this.a.getLanguage().translateString("nukkit.plugin.load", pluginDescription.getFullName()));
            File file2 = new File(file.getParentFile(), pluginDescription.getName());
            if (file2.exists() && !file2.isDirectory()) {
                throw new IllegalStateException("Projected dataFolder '" + file2.toString() + "' for " + pluginDescription.getName() + " exists and is not a directory");
            }
            String string = pluginDescription.getMain();
            PluginClassLoader pluginClassLoader = new PluginClassLoader(this, this.getClass().getClassLoader(), file);
            this.classLoaders.put(pluginDescription.getName(), pluginClassLoader);
            try {
                Class<?> clazz = pluginClassLoader.loadClass(i.a(string));
                if (!PluginBase.class.isAssignableFrom(clazz)) {
                    throw new PluginException("Main class `" + pluginDescription.getMain() + "' does not extend PluginBase");
                }
                try {
                    Class<PluginBase> clazz2 = clazz.asSubclass(PluginBase.class);
                    PluginBase pluginBase = clazz2.newInstance();
                    this.a(pluginBase, pluginDescription, file2, file);
                    return pluginBase;
                }
                catch (ClassCastException classCastException) {
                    throw new PluginException("Error whilst initializing main class `" + pluginDescription.getMain() + '\'', classCastException);
                }
                catch (IllegalAccessException | InstantiationException reflectiveOperationException) {
                    Server.getInstance().getLogger().logException(reflectiveOperationException);
                }
            }
            catch (ClassNotFoundException classNotFoundException) {
                throw new PluginException("Couldn't load plugin " + pluginDescription.getName() + ": main class not found");
            }
        }
        return null;
    }

    @Override
    public Plugin loadPlugin(String string) throws Exception {
        return this.loadPlugin(new File(string));
    }

    /*
     * Exception decompiling
     */
    @Override
    public PluginDescription getPluginDescription(File var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [9[TRYBLOCK]], but top level block is 0[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public PluginDescription getPluginDescription(String string) {
        return this.getPluginDescription(new File(string));
    }

    @Override
    public Pattern[] getPluginFilters() {
        return new Pattern[]{Pattern.compile("^.+\\.jar$")};
    }

    private void a(PluginBase pluginBase, PluginDescription pluginDescription, File file, File file2) {
        pluginBase.init(this, this.a, pluginDescription, file, file2);
        pluginBase.onLoad();
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        block0: {
            if (!(plugin instanceof PluginBase) || plugin.isEnabled()) break block0;
            this.a.getLogger().info(this.a.getLanguage().translateString("nukkit.plugin.enable", plugin.getDescription().getFullName()));
            ((PluginBase)plugin).setEnabled(true);
            this.a.getPluginManager().callEvent(new PluginEnableEvent(plugin));
        }
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        block0: {
            if (!(plugin instanceof PluginBase) || !plugin.isEnabled()) break block0;
            this.a.getLogger().info(this.a.getLanguage().translateString("nukkit.plugin.disable", plugin.getDescription().getFullName()));
            this.a.getServiceManager().cancel(plugin);
            this.a.getPluginManager().callEvent(new PluginDisableEvent(plugin));
            ((PluginBase)plugin).setEnabled(false);
        }
    }

    Class<?> a(String string) {
        Class<?> clazz = this.classes.get(string);
        if (clazz != null) {
            return clazz;
        }
        for (PluginClassLoader pluginClassLoader : this.classLoaders.values()) {
            try {
                clazz = pluginClassLoader.findClass(string, false);
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            if (clazz == null) continue;
            return clazz;
        }
        return null;
    }

    void a(String string, Class<?> clazz) {
        block0: {
            if (this.classes.containsKey(string)) break block0;
            this.classes.put(string, clazz);
        }
    }

    private static Throwable a(Throwable throwable) {
        return throwable;
    }
}

