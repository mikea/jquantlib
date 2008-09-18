package org.jquantlib.lang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This annotation is intended to mark a type as package private
 * 
 * @author Richard Gomes
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
public @interface PackagePrivate {
    // tagging annotation
}
