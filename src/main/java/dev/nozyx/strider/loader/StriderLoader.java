package dev.nozyx.strider.loader;

import dev.nozyx.strider.loader.api.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.util.*;

final class StriderLoader implements IStriderLoader {
    static final String LOADER_VERSION = "0.0.2-pre";

    private final Instrumentation instrumentation;

    private final Logger logger;

    private final File modsFolder = new File("mods");

    private IGameTransformer gameTransformer;

    private String minecraftVersion;
    private MinecraftSide minecraftSide;

    private boolean nogui = false;

    private final ModManager modManager;

    private final StriderUI ui = new StriderUI();

    StriderLoader(String agentArgs, Instrumentation instrumentation) {
        this.instrumentation = instrumentation;

        this.logger = LogManager.getLogger("StriderLoader");

        this.modManager = new ModManager(this, instrumentation);

        start(agentArgs);
    }

    private void start(String agentArgs) {
        try {
            logger.info("Starting StriderLoader v" + LOADER_VERSION);
            logger.debug("Raw loader args: " + agentArgs);

            setupEnvironment(agentArgs);

            if (!nogui) ui.start();

            displayEnvironmentInfo();

            startModLoad();

            if (!nogui) {
                new Thread(() -> {
                    ui.setStatus("StriderLoader initialized !");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {}

                    ui.close();
                }).start();
            }

            logger.info("StriderLoader initialized !");
        } catch (Throwable th) {
            crash("An uncaught exception occurred in StriderLoader", th);
        }
    }

    private void startModLoad() {
        logger.info("Searching for mods ...");
        if (!nogui) ui.setStatus("Searching for mods");
        modsFolder.mkdir();
        List<File> modJars = modManager.findJARs(modsFolder);

        logger.info("Found " + modJars.size() + " mods");

        if (!modJars.isEmpty()) {
            logger.info("Starting to load mods !");

            logger.info("Analysing mods ...");
            if (!nogui) ui.setStatus("Analysing mods");

            for (File modJar : modJars) modManager.analyseMod(modJar);

            logger.info("Resolving load order and dependencies ...");
            if (!nogui) ui.setStatus("Resolving load order and dependencies");
            modManager.resolveLoadOrder();

            logger.info("Loading mods ...");
            if (!nogui) ui.setStatus("Loading mods");

            for (String modId : modManager.getLoadOrder()) {
                ModContainer modContainer = modManager.getModMap().get(modId);
                modManager.loadMod(modContainer.getJarPath().toFile(), modContainer.getInfo());
            }
        }
    }

    private void displayEnvironmentInfo() {
        logger.debug("Environment info:");
        logger.debug("- Minecraft {} {}", minecraftSide.name(), minecraftVersion);
        logger.debug("- NoGui mode {}", nogui);
        logger.debug("- OS: {} {} ({})", System.getProperty("os.name"), System.getProperty("os.version"), System.getProperty("os.arch"));
        logger.debug("- JVM: {} {} (Vendor: {})", System.getProperty("java.vm.name"), System.getProperty("java.vm.version"), System.getProperty("java.vendor"));
        logger.debug("- Java Home: {}", System.getProperty("java.home"));
    }

    private void setupEnvironment(String agentArgs) {
        logger.debug("Setting up environment ...");

        LoaderArgs loaderArgs = new LoaderArgs(agentArgs);

        String mcVersion = loaderArgs.getOrDefault(
                "mcVersion",
                "null"
        );

        if (mcVersion.equals("null")) crash("Essential loader argument 'mcVersion' is not set");

        String mcSideStr = loaderArgs.getOrDefault(
                "mcSide",
                "null"
        );

        if (mcSideStr.equals("null")) crash("Essential loader argument 'mcSide' is not set");

        MinecraftSide mcSide = null;

        switch (mcSideStr) {
            case "client":
                mcSide = MinecraftSide.CLIENT;
                break;
            case "server":
                mcSide = MinecraftSide.SERVER;
                break;
            default:
                crash("Essential loader argument 'mcSide' isn't valid: must be 'client' or 'server'");
        }

        nogui = Boolean.parseBoolean(
                loaderArgs.getOrDefault(
                        "nogui",
                        "false"
                )
        );

        minecraftVersion = mcVersion;
        minecraftSide = mcSide;
        gameTransformer = new GameTransformer(instrumentation);

        DefaultTransformations.registerTransformations(mcSide, gameTransformer);
    }

    @Override
    public Map<String, ModContainer> getMods() {
        return Collections.unmodifiableMap(new HashMap<>(modManager.getModMap()));
    }

    @Override
    public void crash(String msg) {
        logger.fatal("✘ -- STRIDERLOADER CRASH -- ✘");
        logger.fatal("Message: {}", msg);

        if (!nogui) {
            ui.setStatus("CRASH !");
            CrashDialog.showCrashDialog(ui.getFrame(), msg);
        }

        Runtime.getRuntime().halt(-1);
    }

    @Override
    public void crash(String msg, Throwable th) {
        logger.fatal("✘ -- STRIDERLOADER CRASH -- ✘");
        logger.fatal("Message: {}", msg, th);

        if (!nogui) {
            ui.setStatus("CRASH !");
            CrashDialog.showCrashDialog(ui.getFrame(), msg, th);
        }

        Runtime.getRuntime().halt(-1);
    }

    @Override
    public void crash(Throwable th) {
        logger.fatal("✘ -- STRIDERLOADER CRASH -- ✘");
        logger.fatal(th);

        if (!nogui) {
            ui.setStatus("CRASH !");
            CrashDialog.showCrashDialog(ui.getFrame(), th);
        }

        Runtime.getRuntime().halt(-1);
    }

    @Override
    public String getLoaderVersion() {
        return LOADER_VERSION;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public MinecraftSide getMinecraftSide() {
        return minecraftSide;
    }

    @Override
    public IGameTransformer getGameTransformer() {
        return gameTransformer;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean isGuiEnabled() {
        return !nogui;
    }
}