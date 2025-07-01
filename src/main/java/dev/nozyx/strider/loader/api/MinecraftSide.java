package dev.nozyx.strider.loader.api;

/**
 * Defines the side where a mod is intended to run.
 */
public enum MinecraftSide {
    /**
     * Mod runs only on the client side.
     */
    CLIENT,

    /**
     * Mod runs only on the server side.
     */
    SERVER,

    /**
     * Mod runs on both client and server sides.
     * <p>
     * This value is internal to StriderLoader and should not be used by mods.
     * </p>
     */
    @StriderLoaderInternal
    COMMON
}