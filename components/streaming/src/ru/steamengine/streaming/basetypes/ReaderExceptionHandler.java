package ru.steamengine.streaming.basetypes;

/**
 *
 * Created by Steam engine corp 02.12.2009 21:09:33
 *
 * Public API
 * @author Christopher Marlowe
 */
public interface ReaderExceptionHandler {

    public static final ReaderExceptionHandler ALWAYS_HANDLED = new ReaderExceptionHandler() {

        @Override
        public boolean isHandled(Throwable e) {
            return true;
        }

        @Override
        public void readingStarted() {
        }

        @Override
        public void readingFinished() {
        }
    };

    /**
     * checks if e is handled
     * @param e - an exception to check
     * @return true if exception is checked
     */
    boolean isHandled(Throwable e);


    /**
     * called before actual reading is started
     *
     */
    void readingStarted();


    /**
     * called when reading is finished
     */
    void readingFinished();


}
