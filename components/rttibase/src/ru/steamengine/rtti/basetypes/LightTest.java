package ru.steamengine.rtti.basetypes;

/**
 *
 *
 * Idea taken from JUnit
 * Created by Steam engine corp. in 04.09.2010 18:16:44
 * @author Christopher Marlowe
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
public @interface LightTest {
    java.lang.Class<? extends java.lang.Throwable> expected() default None.class;

    long timeout() default 0L;
    static class None extends java.lang.Throwable {
        private static final long serialVersionUID = 1L;

        private None() { /* compiled code */ }
    }
}
