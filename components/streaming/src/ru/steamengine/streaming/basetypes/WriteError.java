package ru.steamengine.streaming.basetypes;

/**
 * write error
 *
 * Created by Steam engine corp. in 22.12.2009 22:11:42
 *
 * Public API
 * @author Christopher Marlowe
 */

public class WriteError extends RuntimeException {

    public WriteError() {
    }

    public WriteError(String message) {
        super(message);
    }

    public WriteError(String message, Throwable cause) {
        super(message, cause);
    }

    public WriteError(Throwable cause) {
        super(cause);
    }
}
