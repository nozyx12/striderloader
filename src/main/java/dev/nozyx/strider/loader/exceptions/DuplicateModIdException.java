package dev.nozyx.strider.loader.exceptions;

import dev.nozyx.strider.loader.api.StriderLoaderInternal;

@StriderLoaderInternal
public final class DuplicateModIdException extends RuntimeException {
    public DuplicateModIdException(String modId) {
        super("Duplicate mod ID detected: " + modId);
    }
}
