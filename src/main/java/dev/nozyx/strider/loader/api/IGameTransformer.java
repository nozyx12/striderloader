package dev.nozyx.strider.loader.api;

/**
 * Interface to register game bytecode transformations.
 * <p>
 * Supports method injections, method replacements, and field replacements.
 * <br>
 * The classes provided for method delegation and advice must follow ByteBuddy conventions:
 * their methods and signatures must be compatible with ByteBuddy's {@code MethodDelegation} and {@code Advice} mechanisms.
 * </p>
 */
public interface IGameTransformer {

    /**
     * Registers a method injection via ByteBuddy Advice.
     * 
     * @param className the fully qualified name of the target class
     * @param methodName the name of the method to inject into
     * @param adviceClass the class containing the advice methods, following ByteBuddy Advice conventions
     */
    void registerMethodInjection(String className, String methodName, Class<?> adviceClass);

    /**
     * Registers a method replacement via ByteBuddy MethodDelegation.
     * 
     * @param className the fully qualified name of the target class
     * @param methodName the name of the method to replace
     * @param methodDelegationClass the class providing the replacement method, following ByteBuddy MethodDelegation conventions
     */
    void registerMethodReplacement(String className, String methodName, Class<?> methodDelegationClass);

    /**
     * Registers a replacement for a field of type Object.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, String newValue);

    /**
     * Registers a replacement for a field of type boolean.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, boolean newValue);

    /**
     * Registers a replacement for a field of type int.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, int newValue);

    /**
     * Registers a replacement for a field of type long.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, long newValue);

    /**
     * Registers a replacement for a field of type float.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, float newValue);

    /**
     * Registers a replacement for a field of type double.
     * 
     * @param className the fully qualified name of the target class
     * @param fieldName the name of the field to replace
     * @param newValue the new value to set
     */
    void registerFieldReplacement(String className, String fieldName, double newValue);
}
