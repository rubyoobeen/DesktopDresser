package model.exception;

// represents general log exceptions that occur in event log
public class LogException extends Exception {
    public LogException(String msg) {
        super(msg);
    }
}
