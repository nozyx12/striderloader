package dev.nozyx.strider.loader.exceptions;

import dev.nozyx.strider.loader.api.StriderLoaderInternal;

@StriderLoaderInternal
public final class ReservedModIdException extends RuntimeException {
    public ReservedModIdException(String modId) {
        super(modId + " is a reserved mod ID");
    }
}
