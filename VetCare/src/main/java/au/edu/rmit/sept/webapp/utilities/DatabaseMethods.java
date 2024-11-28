package au.edu.rmit.sept.webapp.utilities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;

import org.springframework.boot.jdbc.DataSourceBuilder;

public class DatabaseMethods {

    Connection connection;
    boolean connectionInitialised = false;

    public DatabaseMethods() {
        if (!this.connectionInitialised) {
            this.initialiseConnection();
        }
    }

    public Connection initialiseConnection() {
        try {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            dataSourceBuilder.driverClassName("org.h2.Driver");
            dataSourceBuilder.url("jdbc:h2:file:/tmp/demodb");
            dataSourceBuilder.username("sa");
            dataSourceBuilder.password("password");
            this.connection = dataSourceBuilder.build().getConnection();
            this.connectionInitialised = true;
            return this.connection;
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
            return null;
        }
    }

    public ResultSet SQLQueryResultSet(String query, boolean ignoreException) throws SQLException {
        try {
            if (!this.connectionInitialised) {
                this.initialiseConnection();
            }
            PreparedStatement stm = this.connection.prepareStatement(query);
            try {
                return stm.executeQuery();
            } catch (SQLException e) {
                // Yes i know this is bad practice, but i cant specifically catch
                // JdbcSQLNonTransientException so deal with it
                // Try just excecute for SQL statements that are not queries
                stm.execute();
                return null;
            }
        } catch (SQLException e) {
            if (!ignoreException) {
                throw e;
            } else {
                new SQLExceptionHandler(e);
            }
        }
        return null;
    }

    public int GetResultSetSize(ResultSet rs) throws SQLException {
        rs.last();
        int size = rs.getRow();
        rs.beforeFirst();
        return size;
    }

    // Overload with implicit ignoring of SQLException
    public ResultSet SQLQueryResultSet(String query) {
        try {
            return SQLQueryResultSet(query, true);
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
        return null;
    }

    public List<List<String>> SQLQuery2dArray(String query, boolean returnHeaders, boolean ignoreException)
            throws SQLException {
        List<List<String>> outStrings = new ArrayList<>();
        try {
            // We can only obtain data by column name, so grab all column names
            List<String> columnNames = new ArrayList<String>();

            // Run query into resultSet
            ResultSet rs = SQLQueryResultSet(query);
            if (rs != null) {
                // Add column names as first sublist in 2d list
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i < rsmd.getColumnCount() + 1; ++i) {
                    columnNames.add(rsmd.getColumnName(i));
                }

                if (returnHeaders) {
                    outStrings.add(columnNames);
                }

                // Add all rows to output list, independent of table size in either dimension
                while (rs.next()) {
                    List<String> rowStrings = new ArrayList<String>();
                    for (int i = 0; i < columnNames.size(); ++i) {
                        rowStrings.add(rs.getString(columnNames.get(i)));
                    }
                    outStrings.add(rowStrings);
                }
            }
        } catch (SQLException e) {
            if (!ignoreException) {
                throw e;
            } else {
                new SQLExceptionHandler(e);
            }
        }
        return outStrings;
    }

    // SQLQuery2dArray with manual toggle of returnHeaders
    public List<List<String>> SQLQuery2dArray(String query, boolean returnHeaders) {
        List<List<String>> out = new ArrayList<List<String>>();
        try {
            out = SQLQuery2dArray(query, returnHeaders, true);
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
        return out;
    }

    // SQLQuery2dArray with given returned error message string
    public List<List<String>> SQLQuery2dArray(String query, String errorMessage) {
        List<List<String>> out = new ArrayList<List<String>>();
        try {
            out = SQLQuery2dArray(query, true, true);
        } catch (SQLException e) {
            System.out.println(errorMessage);
        }
        return out;
    }

    // SQLQuery2dArray with default parameters
    public List<List<String>> SQLQuery2dArray(String query) {
        List<List<String>> out = new ArrayList<List<String>>();
        try {
            out = SQLQuery2dArray(query, true, true);
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
        return out;
    }

    public HashMap<String, List<String>> SQLQueryHashMap(String query, boolean ignoreException) throws SQLException {
        HashMap<String, List<String>> outStrings = null;
        try {
            // Run query into resultSet
            ResultSet rs = SQLQueryResultSet(query);
            if (rs != null) {
                outStrings = new HashMap<String, List<String>>();
                // We can only obtain data by column name, so grab all column names
                List<String> columnNames = new ArrayList<String>();
                // Add column names as keys in hashmap
                ResultSetMetaData rsmd = rs.getMetaData();

                for (int i = 1; i < rsmd.getColumnCount() + 1; ++i) {
                    outStrings.put(rsmd.getColumnName(i), new ArrayList<String>());
                    columnNames.add(rsmd.getColumnName(i));
                }

                // Add all rows to output list, independent of table size in either dimension
                while (rs.next()) {
                    for (int i = 0; i < columnNames.size(); ++i) {
                        // Get array at key columnNames.get(i), and add corresponding resultSet value
                        outStrings.get(columnNames.get(i)).add(rs.getString(columnNames.get(i)));
                    }
                }
            }
        } catch (SQLException e) {
            if (!ignoreException) {
                throw e;
            } else {
                new SQLExceptionHandler(e);
            }
        }
        return outStrings;
    }

    public HashMap<String, List<String>> SQLQueryHashMap(String query, String errorMessage) {
        HashMap<String, List<String>> out = new HashMap<String, List<String>>();
        try {
            out = SQLQueryHashMap(query, true);
        } catch (SQLException e) {
            System.out.println(errorMessage);
        }
        return out;
    }

    public HashMap<String, List<String>> SQLQueryHashMap(String query) {
        HashMap<String, List<String>> out = new HashMap<String, List<String>>();

        try {
            out = SQLQueryHashMap(query, true);
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
        return out;
    }

    public HashMap<String, String> SQLQueryHashMapSingle(String query, boolean ignoreException)
            throws SQLException {
        HashMap<String, String> outStrings = null;
        try {
            outStrings = new HashMap<String, String>();
            // Run SQLQueryHashMap and convert to HashMap<String, String>
            HashMap<String, List<String>> listOutput = SQLQueryHashMap(query, ignoreException);
            for (Map.Entry<String, List<String>> entry : listOutput.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    outStrings.put(entry.getKey(), entry.getValue().get(0));
                } else {
                    outStrings.put(entry.getKey(), null);
                }
            }

            // This code was removed because it was misbehaving with empty returns
            // TODO remove or fix
            // // Run query into resultSet
            // ResultSet rs = SQLQueryResultSet(query);
            // // if (rs != null && GetResultSetSize(rs) > 0) {
            // if (rs != null) {
            // outStrings = new HashMap<String, String>();
            // ResultSetMetaData rsmd = rs.getMetaData();
            // // rs.next();
            // for (int i = 0; i < rsmd.getColumnCount(); ++i) {
            // // Set key to column name, value to single string that it corresponds to
            // outStrings.put(rsmd.getColumnName(i + 1), rs.getString(rsmd.getColumnName(i +
            // 1)));
            // }
            // }
            return outStrings;
        } catch (SQLException e) {
            if (!ignoreException) {
                throw e;
            } else {
                new SQLExceptionHandler(e);
            }
            return null;
        }
    }

    public HashMap<String, String> SQLQueryHashMapSingle(String query, String errorMessage) {
        HashMap<String, String> out = new HashMap<String, String>();

        try {
            out = SQLQueryHashMapSingle(query, true);
        } catch (SQLException e) {
            System.out.println(errorMessage);
        }
        return out;
    }

    public HashMap<String, String> SQLQueryHashMapSingle(String query) {
        HashMap<String, String> out = new HashMap<String, String>();

        try {
            out = SQLQueryHashMapSingle(query, true);
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
        return out;
    }

    public List<List<String>> getTable(String tableName) throws SQLException {
        return SQLQuery2dArray(String.format("SELECT * FROM %s;", tableName));
    }

    @Override
    protected void finalize() {
        try {
            connection.close();
        } catch (SQLException e) {
            new SQLExceptionHandler(e);
        }
    }
}
