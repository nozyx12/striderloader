package dev.nozyx.strider.loader.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Represents a mod and its associated metadata and JAR path.
 * This class allows extracting the contents of the mod JAR for further processing.
 */
public final class ModContainer {
    private final ModInfo info;
    private final Path jarPath;

    /**
     * Constructs a new {@link ModContainer} with the given metadata and JAR file path.
     * <p>
     * This is internal to StriderLoader and should not be instantiated by external code.
     *
     * @param info    the mod metadata
     * @param jarPath the path to the mod's JAR file
     */
    @StriderLoaderInternal
    public ModContainer(ModInfo info, Path jarPath) {
        this.info = info;
        this.jarPath = jarPath;
    }

    /**
     * Extracts the contents of the mod's JAR file to a temporary directory.
     *
     * @return the path to the extracted temporary directory
     * @throws IOException if the JAR file cannot be read or extracted
     */
    public Path openJar() throws IOException {
        Path tempDir = Files.createTempDirectory("extractedJar_");

        try (JarFile jar = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                Path outPath = tempDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(outPath);
                } else {
                    Files.createDirectories(outPath.getParent());
                    try (InputStream in = jar.getInputStream(entry);
                         OutputStream out = Files.newOutputStream(outPath)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = in.read(buffer)) != -1) out.write(buffer, 0, bytesRead);
                    }
                }
            }
        }

        return tempDir;
    }

    /**
     * Returns the metadata information of the mod.
     *
     * @return the {@link ModInfo} instance
     */
    public ModInfo getInfo() {
        return info;
    }

    /**
     * Returns the file system path to the mod's JAR file.
     *
     * @return the JAR path
     */
    public Path getJarPath() {
        return jarPath;
    }
}
