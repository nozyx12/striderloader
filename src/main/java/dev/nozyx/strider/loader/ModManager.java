package dev.nozyx.strider.loader;

import com.vdurmont.semver4j.Semver;
import dev.nozyx.strider.loader.api.*;
import dev.nozyx.strider.loader.exceptions.DependencyException;
import dev.nozyx.strider.loader.exceptions.DuplicateModIdException;
import dev.nozyx.strider.loader.exceptions.ReservedModIdException;
import dev.nozyx.strider.loader.wrappers.LoaderWrapper;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class ModManager {
    private final IStriderLoader loader;
    private final Instrumentation instrumentation;

    private final Map<String, ModContainer> modMap = new HashMap<>();
    private final Map<String, File> modFiles = new HashMap<>();
    private final List<String> loadOrder = new ArrayList<>();

    ModManager(IStriderLoader loader, Instrumentation instrumentation) {
        this.loader = loader;
        this.instrumentation = instrumentation;
    }

    void resolveLoadOrder() {
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();

        modMap.keySet().forEach(modId -> visit(modId, visited, temp));
    }

    private void visit(String modId, Set<String> visited, Set<String> temp) {
        try {
            if (visited.contains(modId)) return;
            if (temp.contains(modId)) throw new DependencyException("Cycle detected with mod: " + modId);

            temp.add(modId);
            ModContainer mod = modMap.get(modId);
            ModInfo info = mod.getInfo();

            info.getDependencies().forEach((depId, depVersionRange) -> {
                if (depId.equals("minecraft") || depId.equals("striderloader")) return;
                if (depId.equals(modId)) throw new DependencyException("Mod '" + modId + "' cannot depend on itself");
                if (!modMap.containsKey(depId)) throw new DependencyException("Missing dependency: " + depId + " for " + modId);

                Semver installedDepSemver = new Semver(modMap.get(depId).getInfo().getVersion(), Semver.SemverType.NPM);
                if (!installedDepSemver.satisfies(depVersionRange)) throw new DependencyException("Dependency version mismatch: mod '" + modId + "' requires '" + depId + "' in version range '" + depVersionRange + "' but found version " + modMap.get(depId).getInfo().getVersion());
                visit(depId, visited, temp);
            });

            String mcVersion = info.getDependencies().get("minecraft");
            String loaderVersion = info.getDependencies().get("striderloader");

            if (mcVersion != null) {
                Semver semverMinecraftVersion = new Semver(loader.getMinecraftVersion(), Semver.SemverType.NPM);
                if (!semverMinecraftVersion.satisfies(mcVersion)) throw new DependencyException("Mod requires Minecraft version in range '" + mcVersion + "' but the current version is " + loader.getMinecraftVersion());
            }

            if (loaderVersion != null) {
                Semver semverStriderLoaderVersion = new Semver(loader.getLoaderVersion(), Semver.SemverType.NPM);
                if (!semverStriderLoaderVersion.satisfies(loaderVersion)) throw new DependencyException("Mod requires StriderLoader version in range '" + loaderVersion + "' but the current version is " + loader.getLoaderVersion());
            }

            temp.remove(modId);
            visited.add(modId);
            loadOrder.add(modId);
        } catch (Throwable th) {
            loader.crash("Mod dependency resolution failed:\nFailed to resolve mod dependencies and compute load order for mod '" + modId + "'", th);
        }
    }

    void analyseMod(File modFile) {
        loader.getLogger().info("--- Analysing: " + modFile.getPath() + " ---");
        try (JarFile jar = new JarFile(modFile)) {
            JarEntry entry = jar.getJarEntry("stridermod.json");
            if (entry == null) throw new FileNotFoundException("File 'stridermod.json' not found");

            try (InputStream is = jar.getInputStream(entry)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int read;
                while ((read = is.read(buffer)) != -1) baos.write(buffer, 0, read);

                String json = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                ModInfo info = ModInfo.parseJSON(json);

                if (info.getId().equals("minecraft") || info.getId().equals("striderloader")) throw new ReservedModIdException(info.getId());
                if (modMap.containsKey(info.getId())) throw new DuplicateModIdException(info.getId());

                loader.getLogger().info("ID: {}", info.getId());
                loader.getLogger().info("Version: {}", info.getVersion());
                loader.getLogger().info("Description: {}", info.getDescription());
                loader.getLogger().info("Author: {}", info.getAuthor());
                loader.getLogger().info("Side: {}", info.getSide().name());
                loader.getLogger().info("Mod class: {}", info.getModClass());

                if (!info.getSide().equals(loader.getMinecraftSide()) && !info.getSide().equals(MinecraftSide.COMMON)) throw new RuntimeException("Mod with ID '" + info.getId() + "' is made for Minecraft " + info.getSide() + " but the current side is " + loader.getMinecraftSide());

                modMap.put(info.getId(), new ModContainer(info, modFile.toPath()));
                modFiles.put(info.getId(), modFile);

                loader.getLogger().info("> ✔ MOD ANALYSIS SUCCESS");
            }
        } catch (Throwable th) {
            loader.crash("Mod analysis failed:\nFailed to analyse mod file: " + modFile.getPath(), th);
        }
    }

    void loadMod(File modFile, ModInfo modInfo) {
        loader.getLogger().info("--- Loading: " + modFile.getPath() + " ---");

        loader.getLogger().info("> Adding mod jar to system classloader...");
        try (JarFile jar = new JarFile(modFile)) {
            instrumentation.appendToSystemClassLoaderSearch(jar);
        } catch (IOException e) {
            loader.crash("Mod load failed:\nCould not open mod JAR to add it to the system classloader for mod '" + modInfo.getId() + "' : " + modFile.getPath(), e);
        }

        loader.getLogger().info("> Loading mod class ...");

        Mod modInstance = null;
        try {
            Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(modInfo.getModClass());
            if (Mod.class.isAssignableFrom(clazz)) {
                Class<? extends Mod> modClazz = clazz.asSubclass(Mod.class);
                modInstance = modClazz.getDeclaredConstructor().newInstance();
            } else throw new RuntimeException("Class '" + modInfo.getModClass() + "' does not implement 'dev.nozyx.strider.loader.api.Mod'");
        } catch (Throwable th) {
            if (th instanceof ClassNotFoundException) loader.crash("Mod load failed:\nMod class not found for mod '" + modInfo.getId() + "' : " + modInfo.getModClass(), th);

            loader.crash("Mod load failed:\nMod class load and instantiation failed for mod '" + modInfo.getId() + "' : " + modInfo.getModClass(), th);
        }

        loader.getLogger().info("> Running 'onLoad' method ...");

        try {
            modInstance.onLoad(new LoaderWrapper(loader, modInfo));
        } catch (Throwable th) {
            loader.crash("Mod load failed:\nMod 'onLoad' method failed for mod '" + modInfo.getId() + "'", th);
        }

        loader.getLogger().info("> ✔ MOD LOAD SUCCESS");
    }

    List<File> findJARs(File directory) {
        List<File> jarFiles = new ArrayList<>();
        if (directory == null || !directory.isDirectory()) return jarFiles;

        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".stridermod"));
        if (files != null) for (File f : files) if (f.isFile()) jarFiles.add(f);

        return jarFiles;
    }

    List<String> getLoadOrder() {
        return loadOrder;
    }

    Map<String, ModContainer> getModMap() {
        return modMap;
    }

    Map<String, File> getModFiles() {
        return modFiles;
    }
}
