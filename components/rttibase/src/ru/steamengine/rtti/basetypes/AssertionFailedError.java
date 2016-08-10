package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 04.09.2010 18:49:17
 *
 * @author Christopher Marlowe
 */
public class AssertionFailedError extends Error {

    private static final long serialVersionUID= 1L;

    public AssertionFailedError() {
    }

    public AssertionFailedError(String message) {
        super(message);
    }

}
