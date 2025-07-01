package dev.nozyx.strider.loader.api;

/**
 * <p>
 * Internal enum used by StriderLoader to define permissions that can be declared by mods
 * in their <code>stridermod.json</code> descriptor. These permissions control access to
 * advanced or sensitive features of the loader and runtime.
 * <p>
 * This enum is not intended to be used directly by mods in code.
 */
@StriderLoaderInternal
public enum ModPermission {

    /** Allows the mod to access the internal logger instance of the loader. */
    ACCESS_LOGGER,

    /** Allows the mod to crash the loader intentionally (e.g., for testing or blocking loading) using {@link IGameTransformer}. */
    CRASH_LOADER,

    /** Allows the mod to inject code into existing methods of other classes using {@link IGameTransformer}. */
    INJECT_METHODS,

    /** Allows the mod to fully replace methods in existing classes using {@link IGameTransformer}. */
    REPLACE_METHODS,

    /** Allows the mod to replace or modify existing fields in classes using {@link IGameTransformer} (even static/final). */
    REPLACE_FIELDS,

    /** Allows the mod to retrieve mod containers of all loaded mods including the mod itself. */
    GET_MOD_CONTAINERS
}
