package dev.nozyx.strider.loader.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated method, constant, parameter, constructor, or class is intended
 * for use only by the StriderLoader itself.
 * These elements are not meant to be used by mods.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.CLASS)
public @interface StriderLoaderInternal {}
