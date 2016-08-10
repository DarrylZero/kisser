package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp. in 22.12.2009 22:55:41
 *
 * Public API
 * @author Christopher Marlowe
 */
public class PropertyValueException extends RuntimeException {

    public PropertyValueException() {
    }

    public PropertyValueException(String message) {
        super(message);
    }

    public PropertyValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyValueException(Throwable cause) {
        super(cause);
    }
}
