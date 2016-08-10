package ru.steamengine.streaming.basetypes;

/**
 * Reading error
 *
 * Created by Steam engine corp. in 22.12.2009 22:11:29
 *
 * Public API
 * @author Christopher Marlowe
 */

public class ReadError extends RuntimeException {
    public ReadError() {
    }

    public ReadError(String message) {
        super(message);
    }

    public ReadError(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadError(Throwable cause) {
        super(cause);
    }
}
