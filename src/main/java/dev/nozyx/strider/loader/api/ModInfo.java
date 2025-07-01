package dev.nozyx.strider.loader.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the metadata of a mod loaded by StriderLoader.
 * <p>
 * Contains information such as mod ID, version, description, author, and
 * the targeted Minecraft side (client, server, or both).
 * </p>
 * <p>
 * This class is primarily used by mods and the loader to query mod information.
 * </p>
 */
public final class ModInfo {
    private final String id;
    private final String version;
    private final String description;
    private final String author;
    private final MinecraftSide side;

    @StriderLoaderInternal
    private final String modClass;

    @StriderLoaderInternal
    private final Map<String, String> dependencies;

    @StriderLoaderInternal
    private final List<ModPermission> permissions;

    /**
     * Internal constructor for creating a ModInfo instance.
     *
     * @param id           The unique mod identifier.
     * @param version      The mod version string.
     * @param description  A short description of the mod.
     * @param author       The mod author.
     * @param side         The Minecraft side this mod targets.
     * @param modClass     Internal: the fully qualified name of the mod’s main class.
     * @param dependencies Internal: map of dependencies and their version ranges.
     * @param permissions Internal: list of permissions.
     */
    @StriderLoaderInternal
    public ModInfo(String id, String version, String description, String author, MinecraftSide side, String modClass, Map<String, String> dependencies, List<ModPermission> permissions) {
        this.id = id;
        this.version = version;
        this.description = description;
        this.author = author;
        this.side = side;
        this.modClass = modClass;
        this.dependencies = dependencies;
        this.permissions = permissions;
    }

    /**
     * Parses a JSON string representing mod metadata and returns a ModInfo instance.
     * <p>
     * Internal use only.
     * </p>
     * @param json JSON string representing the mod metadata.
     * @return A ModInfo instance parsed from the JSON.
     * @throws JSONException if parsing fails or data is invalid.
     */
    @StriderLoaderInternal
    public static ModInfo parseJSON(String json) throws JSONException {
        JSONObject modJSON = new JSONObject(json);
        String id = modJSON.getString("id");
        String version = modJSON.getString("version");
        String description = modJSON.getString("description");
        String author = modJSON.getString("author");
        String modClass = modJSON.getString("modClass");
        MinecraftSide side = MinecraftSide.valueOf(modJSON.getString("side").toUpperCase());

        Map<String, String> dependencies = new HashMap<>();

        JSONObject dependenciesObject = modJSON.getJSONObject("dependencies");
        dependenciesObject.toMap().forEach((depId, depVersionRangeStr) -> dependencies.put(depId, (String) depVersionRangeStr));

        List<ModPermission> permissions = new ArrayList<>();

        JSONArray permissionsArray = modJSON.getJSONArray("permissions");
        permissionsArray.forEach((permId) -> permissions.add(ModPermission.valueOf(((String) permId).toUpperCase())));

        return new ModInfo(
                id,
                version,
                description,
                author,
                side,
                modClass,
                dependencies,
                permissions
        );
    }

    /**
     * Returns the unique mod identifier.
     *
     * @return The mod ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the version string of this mod.
     *
     * @return The mod version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns the short description of the mod.
     *
     * @return The mod description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the author of the mod.
     *
     * @return The mod author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the Minecraft side this mod targets.
     *
     * @return The MinecraftSide enum value (CLIENT, SERVER, BOTH).
     */
    public MinecraftSide getSide() {
        return side;
    }

    /**
     * Internal use only: returns the fully qualified name of the mod’s main class.
     *
     * @return The main class name.
     */
    @StriderLoaderInternal
    public String getModClass() {
        return modClass;
    }

    /**
     * Internal use only: returns a map of dependencies with their version ranges.
     *
     * @return Map from dependency mod IDs to Semver version ranges.
     */
    @StriderLoaderInternal
    public Map<String, String> getDependencies() {
        return dependencies;
    }

    /**
     * Internal use only: returns a list of permissions.
     *
     * @return List of permissions.
     */
    @StriderLoaderInternal
    public List<ModPermission> getPermissions() {
        return permissions;
    }
}
