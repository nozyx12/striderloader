package dev.nozyx.strider.loader.impl;

import dev.nozyx.strider.loader.api.StriderLoaderInternal;

import java.lang.instrument.Instrumentation;

@StriderLoaderInternal
public final class StriderAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        new StriderLoader(args, instrumentation);
    }
}
