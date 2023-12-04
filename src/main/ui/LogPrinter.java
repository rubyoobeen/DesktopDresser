package ui;

import model.EventLog;
import model.exception.LogException;

public interface LogPrinter {

    // prints event log, exceptions throw for any reason when printing fails
    void printLog(EventLog el) throws LogException;
}
