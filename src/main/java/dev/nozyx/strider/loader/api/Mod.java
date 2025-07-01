package dev.nozyx.strider.loader.api;

/**
 * Represents a mod entry point for StriderLoader.
 * <p>
 * Mods must implement this interface to receive the load event.
 * </p>
 */
public interface Mod {
    /**
     * Called when the mod is loaded by the loader.
     *
     * @param loader The {@link IStriderLoader} instance.
     */
    void onLoad(IStriderLoader loader);
}
