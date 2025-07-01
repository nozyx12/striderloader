package dev.nozyx.strider.loader.exceptions;

import dev.nozyx.strider.loader.api.StriderLoaderInternal;

@StriderLoaderInternal
public final class DependencyException extends RuntimeException {
    public DependencyException(String message) {
        super(message);
    }
}
