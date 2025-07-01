package dev.nozyx.strider.loader.api;

/**
 * Exception thrown when a mod attempts to use a feature that requires a permission
 * it has not been granted.
 * <p>
 * This exception is mainly used internally by StriderLoader to enforce permission checks
 * on mod API features.
 */
public final class PermissionDeniedException extends RuntimeException {

    /**
     * Constructs a new PermissionDeniedException.
     * <p>
     * This exception is not meant to be thrown by mods directly,
     * but only by the loader to enforce permission checks.
     *
     * @param requiredPermission The permission required to use the attempted feature.
     * @param modId The identifier of the mod that attempted the unauthorized action.
     */
    @StriderLoaderInternal
    public PermissionDeniedException(ModPermission requiredPermission, String modId) {
        super("Mod '" + modId + "' attempted to use feature requiring permission '" + requiredPermission.name() + "' but does not have it");
    }
}
