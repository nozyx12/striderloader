package dev.nozyx.strider.loader.wrappers;

import dev.nozyx.strider.loader.api.*;

@StriderLoaderInternal
public final class GameTransformerWrapper implements IGameTransformer {
    private final IGameTransformer parent;
    private final ModInfo modInfo;

    public GameTransformerWrapper(IGameTransformer parent, ModInfo modInfo) {
        this.parent = parent;
        this.modInfo = modInfo;
    }

    @Override
    public void registerMethodInjection(String className, String methodName, Class<?> adviceClass) {
        if (!modInfo.getPermissions().contains(ModPermission.INJECT_METHODS)) throw new PermissionDeniedException(ModPermission.INJECT_METHODS, modInfo.getId());
        parent.registerMethodInjection(className, methodName, adviceClass);
    }

    @Override
    public void registerMethodReplacement(String className, String methodName, Class<?> methodDelegationClass) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_METHODS)) throw new PermissionDeniedException(ModPermission.REPLACE_METHODS, modInfo.getId());
        parent.registerMethodReplacement(className, methodName, methodDelegationClass);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, String newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, boolean newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, int newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, long newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, float newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }

    @Override
    public void registerFieldReplacement(String className, String fieldName, double newValue) {
        if (!modInfo.getPermissions().contains(ModPermission.REPLACE_FIELDS)) throw new PermissionDeniedException(ModPermission.REPLACE_FIELDS, modInfo.getId());
        parent.registerFieldReplacement(className, fieldName, newValue);
    }
}
