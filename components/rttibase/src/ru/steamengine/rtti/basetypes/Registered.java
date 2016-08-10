package ru.steamengine.rtti.basetypes;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Public API.
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Registered {
}
