package dev.nozyx.strider.loader.wrappers;

import dev.nozyx.strider.loader.api.*;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@StriderLoaderInternal
public final class LoaderWrapper implements IStriderLoader {
    private final IStriderLoader parent;
    private final ModInfo modInfo;

    public LoaderWrapper(IStriderLoader parent, ModInfo modInfo) {
        this.parent = parent;
        this.modInfo = modInfo;
    }

    @Override
    public Logger getLogger() {
        if (!modInfo.getPermissions().contains(ModPermission.ACCESS_LOGGER)) throw new PermissionDeniedException(ModPermission.ACCESS_LOGGER, modInfo.getId());
        return parent.getLogger();
    }

    @Override
    public void crash(String msg) {
        if (!modInfo.getPermissions().contains(ModPermission.CRASH_LOADER)) throw new PermissionDeniedException(ModPermission.CRASH_LOADER, modInfo.getId());
        parent.crash("(Crash triggered by mod '" + modInfo.getId() + "') " + msg);
    }

    @Override
    public void crash(Throwable th) {
        if (!modInfo.getPermissions().contains(ModPermission.CRASH_LOADER)) throw new PermissionDeniedException(ModPermission.CRASH_LOADER, modInfo.getId());
        parent.crash("(Crash triggered by mod '" + modInfo.getId() + "') No message", th);
    }

    @Override
    public void crash(String msg, Throwable th) {
        if (!modInfo.getPermissions().contains(ModPermission.CRASH_LOADER)) throw new PermissionDeniedException(ModPermission.CRASH_LOADER, modInfo.getId());
        parent.crash("(Crash triggered by mod '" + modInfo.getId() + "') " + msg, th);
    }

    @Override
    public String getMinecraftVersion() {
        return parent.getMinecraftVersion();
    }

    @Override
    public String getLoaderVersion() {
        return parent.getLoaderVersion();
    }

    @Override
    public MinecraftSide getMinecraftSide() {
        return parent.getMinecraftSide();
    }

    @Override
    public IGameTransformer getGameTransformer() {
        return new GameTransformerWrapper(parent.getGameTransformer(), modInfo);
    }

    @Override
    public Map<String, ModContainer> getMods() {
        if (!modInfo.getPermissions().contains(ModPermission.GET_MOD_CONTAINERS)) throw new PermissionDeniedException(ModPermission.GET_MOD_CONTAINERS, modInfo.getId());
        return parent.getMods();
    }

    @Override
    public boolean isGuiEnabled() {
        return parent.isGuiEnabled();
    }
}
