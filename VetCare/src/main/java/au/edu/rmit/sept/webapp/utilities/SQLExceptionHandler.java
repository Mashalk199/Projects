package au.edu.rmit.sept.webapp.utilities;

import java.sql.SQLException;

import org.springframework.jdbc.datasource.init.UncategorizedScriptException;

public class SQLExceptionHandler {
    public SQLExceptionHandler(SQLException e) {
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Message: " + e.getMessage());
    }

    public SQLExceptionHandler(SQLException e, String message) {
        new SQLExceptionHandler(e);
        throw new UncategorizedScriptException(message, e);
    }
}
