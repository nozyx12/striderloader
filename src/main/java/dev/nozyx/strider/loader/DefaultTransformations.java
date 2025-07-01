package dev.nozyx.strider.loader;

import dev.nozyx.strider.loader.api.IGameTransformer;
import dev.nozyx.strider.loader.api.MinecraftSide;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

final class DefaultTransformations {
    static void registerTransformations(MinecraftSide side, IGameTransformer gameTransformer) {
        if (side.equals(MinecraftSide.CLIENT)) {
            gameTransformer.registerMethodReplacement(
                    "net.minecraft.client.ClientBrandRetriever",
                    "getClientModName",
                    ClientBrandInterceptor.class
            );
        }
    }

    public static class ClientBrandInterceptor {
        @RuntimeType
        public static Object intercept(@SuperCall Callable<?> zuper) throws Exception {
            String originalBrand = (String) zuper.call();
            return (originalBrand.equals("vanilla") ? "" : originalBrand + "/") + "striderloader-" + StriderLoader.LOADER_VERSION;
        }
    }
}
