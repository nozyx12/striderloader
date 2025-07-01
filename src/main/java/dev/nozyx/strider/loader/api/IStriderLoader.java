package dev.nozyx.strider.loader.api;

import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Represents the core mod loader interface.
 * Provides access to loader information, mods, and game transformation utilities.
 */
public interface IStriderLoader {

    /**
     * Returns the logger used by the loader.
     * @return the {@link Logger} instance
     */
    Logger getLogger();

    /**
     * Forces the loader to crash with a custom message.
     *
     * @param msg the crash message
     */
    void crash(String msg);

    /**
     * Forces the loader to crash with an exception.
     *
     * @param th the throwable causing the crash
     */
    void crash(Throwable th);

    /**
     * Forces the loader to crash with a custom message and an exception.
     *
     * @param msg the crash message
     * @param th the throwable causing the crash
     */
    void crash(String msg, Throwable th);

    /**
     * Returns the Minecraft version targeted by the loader.
     * 
     * @return the Minecraft version string
     */
    String getMinecraftVersion();

    /**
     * Returns the current version of StriderLoader.
     * 
     * @return the loader version string
     */
    String getLoaderVersion();

    /**
     * Returns the side (client/server) the loader is running on.
     * 
     * @return the MinecraftSide enum value
     */
    MinecraftSide getMinecraftSide();

    /**
     * Returns the game transformer used to apply bytecode modifications.
     * 
     * @return the {@link IGameTransformer} instance
     */
    IGameTransformer getGameTransformer();

    /**
     * Returns an immutable map of all loaded mods, indexed by their mod IDs.
     * 
     * @return a map of mod ID to {@link ModContainer}
     */
    Map<String, ModContainer> getMods();

    /**
     * Returns whether the StriderLoader user interface (UI) is enabled.
     *
     * @return {@code true} if the UI is enabled, {@code false} otherwise
     */
    boolean isGuiEnabled();
}
