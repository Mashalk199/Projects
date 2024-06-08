package app;

import java.util.ArrayList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for Managing the JDBC Connection to a SQLLite Database. Allows SQL
 * queries to be used with the SQLLite Databse in Java.
 *
 * This is an example JDBC Connection that has a single query for the Movies
 * Database This is similar to the project workshop JDBC examples.
 *
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 */
public class JDBCConnection {


    // Name of database file (contained in database folder)
    private static final String DATABASE = "jdbc:sqlite:database/ctg copy.db";

    public JDBCConnection() {
        System.out.println("Created JDBC Connection Object");
    }

    /**
     * Get all of the Movies in the database
     */
    public ArrayList<String> dropdownLGA() {
        ArrayList<String> areas = new ArrayList<String>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT lga_name16 FROM LGAs ORDER BY lga_name16 ASC;";

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String lga = results.getString("lga_name16");

                // For now we will just store the movieName and ignore the id
                areas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return areas;
    }

 
    public int returnSingleLGA(String table, String lga, String status, String sex, String category) {
        String param = "SchoolStatistics";
        int count = -1;
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SUM(count) FROM " + table;
            query = query + " NATURAL JOIN LGAs WHERE SEX LIKE '" + sex + "' AND INDIGENOUS_STATUS LIKE '" + status
                    + "' AND " + param + " LIKE '" + category + "'";
            query = query + " AND lga_name16 LIKE '" + lga + "';";
            // Get Result
            ResultSet results = statement.executeQuery(query);
            count = results.getInt("SUM(count)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return count;
    }

    public double returnSingleLGAProp(String table, String lga, String status, String sex, String category) {
        String param = "SchoolStatistics";
        double prop = -1;
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT count FROM SchoolStatistics";
            // String query = "SELECT SUM(count) FROM " + table;
            // query = query + " NATURAL JOIN LGAs WHERE SEX LIKE '" + sex + "' AND
            // INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + " LIKE '"+ category +
            // "'";
            // query = query + " AND lga_name16 LIKE '" + lga + "';";

            query = "SELECT SUM(PERC) FROM (SELECT STATS.count as COUNT,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                    + table + " AS C " + "NATURAL JOIN LGAs WHERE sex LIKE '" + sex + "' AND INDIGENOUS_STATUS LIKE '"
                    + status + "' AND " + param + "  LIKE '" + category + "' AND lga_name16 LIKE '" + lga + "')"
                    + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                    + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16)";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            prop = results.getDouble("SUM(PERC)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return prop;
    }

    public ArrayList<String> returnAllLGA(String table, String status, String sex, String category, String sort) {
        ArrayList<String> lgas = new ArrayList<String>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT lga_name16 FROM " + table;
                query = query + " NATURAL JOIN LGAs WHERE sex LIKE \'" + sex + "\' AND INDIGENOUS_STATUS LIKE \'"
                        + status + "\' AND " + param + " LIKE \'" + category + "\' ORDER BY count " + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE sex LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String lga = results.getString("lga_name16");

                // For now we will just store the movieName and ignore the id
                lgas.add(lga);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return lgas;
    }

    public ArrayList<String> returnAllStatus(String table, String status, String sex, String category, String sort) {
        ArrayList<String> istatuses = new ArrayList<String>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";
        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT INDIGENOUS_STATUS FROM " + table;
                query = query + " NATURAL JOIN LGAs WHERE sex LIKE \'" + sex + "\' AND INDIGENOUS_STATUS LIKE \'"
                        + status + "\' AND " + param + " LIKE \'" + category + "\' ORDER BY count " + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS.INDIGENOUS_STATUS,STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE sex LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String istatus = results.getString("INDIGENOUS_STATUS");
                istatus = switchStatus(istatus);
                // For now we will just store the movieName and ignore the id
                istatuses.add(istatus);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return istatuses;
    }

    public ArrayList<String> returnAllSexes(String table, String status, String sex, String category, String sort) {
        ArrayList<String> sexes = new ArrayList<String>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT sex FROM " + table;
                query = query + " NATURAL JOIN LGAs WHERE sex LIKE \'" + sex + "\' AND INDIGENOUS_STATUS LIKE \'"
                        + status + "\' AND " + param + " LIKE \'" + category + "\' ORDER BY count " + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS.sex,STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE sex LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String gender = results.getString("sex");
                gender = switchSex(gender);
                // For now we will just store the movieName and ignore the id
                sexes.add(gender);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return sexes;
    }

    public ArrayList<Integer> returnAllCounts(String table, String status, String sex, String category, String sort) {
        ArrayList<Integer> counts = new ArrayList<Integer>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";
        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT count FROM " + table;
                query = query + " NATURAL JOIN LGAs WHERE SEX LIKE \'" + sex + "\' AND INDIGENOUS_STATUS LIKE \'"
                        + status + "\' AND " + param + " LIKE \'" + category + "\' ORDER BY count " + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }

            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                int count = results.getInt("count");

                // For now we will just store the movieName and ignore the id
                counts.add(count);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return counts;
    }

    public ArrayList<String> returnAllCategories(String table, String status, String sex, String category,
            String sort) {
        ArrayList<String> categories = new ArrayList<String>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";
            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT " + param + " FROM " + table;
                query = query + " NATURAL JOIN LGAs WHERE SEX LIKE \'" + sex + "\' AND INDIGENOUS_STATUS LIKE \'"
                        + status + "\' AND " + param + " LIKE \'" + category + "\' ORDER BY count " + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS." + param
                        + ",count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String categorie = results.getString(param);
                categorie = switchCategory(categorie);
                // For now we will just store the movieName and ignore the id
                categories.add(categorie);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return categories;
    }

    public ArrayList<Double> returnAllProportions(String table, String status, String sex, String category,
            String sort) {
        ArrayList<Double> proportions = new ArrayList<Double>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";
            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT STATS." + param
                        + ",STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY count "
                        + sort + ";";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT STATS." + param
                        + ",STATS.count,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM "
                        + table + " AS C " + "NATURAL JOIN LGAs WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + "  LIKE '" + category + "') "
                        + "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table
                        + " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16 ORDER BY PERC "
                        + sort + ";";
            }
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                double proportion = results.getDouble("PERC");
                // For now we will just store the movieName and ignore the id
                proportions.add(proportion);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return proportions;
    }

    public int returnSingleState (String table, String state, String status, String sex, String category) {
        String param = "SchoolStatistics";
        int count=-1;
        if (table.equals("HigherStatistics")) {
            param = "qualification";
            
        }
        else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        }
        else if (table.equals("PopulationStatistics")) {
            param = "age";
        }
        else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        
        int high = 0;
        int low = 0;
        switch(state){
            
            case "NSW": 
            low = 10000;
            high = 20000;
            break;
        
            case "VIC":
            low = 20000;
            high = 30000;
            break;

            case "QLD": 
            low = 30000;
            high = 40000;
            break;

            case "SA": 
            low = 40000;
            high = 50000;
            break;

            case "WA": 
            low = 50000;
            high = 60000;
            break;

            case "TAS": 
            low = 60000;
            high = 70000;
            break;

            case "NT": 
            low = 70000;
            high = 80000;
            break;

            case "ACT": 
            low = 80000;
            high = 90000;
            break;

            case "Other": 
            low = 90000;
            high = 100000;
            break;
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SUM(count) FROM " + table;
            query = query + " NATURAL JOIN LGAs WHERE SEX LIKE '" + sex + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + " LIKE '"+ category + "'";
            query = query + " AND lga_code16 > '" + low + "' AND lga_code16 < '" + high + "';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            count = results.getInt("SUM(count)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return count;
    }
    public double returnSingleStateProp (String table, String state, String status, String sex, String category) {
        String param = "SchoolStatistics";
        double prop=-1;
        if (table.equals("HigherStatistics")) {
            param = "qualification";
            
        }
        else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        }
        else if (table.equals("PopulationStatistics")) {
            param = "age";
        }
        else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        
        int high = 0;
        int low = 0;
        switch(state){
            
            case "NSW": 
            low = 10000;
            high = 20000;
            break;
        
            case "VIC":
            low = 20000;
            high = 30000;
            break;

            case "QLD": 
            low = 30000;
            high = 40000;
            break;

            case "SA": 
            low = 40000;
            high = 50000;
            break;

            case "WA": 
            low = 50000;
            high = 60000;
            break;

            case "TAS": 
            low = 60000;
            high = 70000;
            break;

            case "NT": 
            low = 70000;
            high = 80000;
            break;

            case "ACT": 
            low = 80000;
            high = 90000;
            break;

            case "Other": 
            low = 90000;
            high = 100000;
            break;
        }

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT count FROM SchoolStatistics";

            query = "SELECT SUM(PERC) FROM (SELECT STATS.count as COUNT,STATS.lga_name16,STATS.SEX,TOTAL,(count * 1.00/TOTAL)*100 AS PERC FROM (SELECT * FROM " + table + " AS C " +
                "NATURAL JOIN LGAs WHERE sex LIKE '" + sex + "' AND INDIGENOUS_STATUS LIKE '" +
                status + "' AND " + param + "  LIKE '" + category + "' AND lga_code16 > '";
            
            query = query + low + "AND lga_code16 < '" + high + "')"  +
                "AS STATS JOIN (SELECT SUM(count) AS TOTAL,lga_name16 FROM " + table +
                " NATURAL JOIN LGAs GROUP BY lga_name16) AS CO ON STATS.lga_name16=CO.lga_name16)";
                
            // Get Result
            ResultSet results = statement.executeQuery(query);
            prop = results.getDouble("SUM(PERC)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return prop;
    }
    
    public ArrayList<Integer> returnAllStateCounts (String table, String status, String sex, String category, String sort) {
        ArrayList<String> stateNames = new ArrayList<String>();
        ArrayList<Integer> stateCounts = new ArrayList<Integer>();
        // ArrayList<Double> stateProps = new ArrayList<Double>();
        // String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            table = "qualification";
            
        }
        else if (table.equals("SchoolStatistics")) {
            table = "yearlevel";
        }
        else if (table.equals("PopulationStatistics")) {
            table = "age";
        }
        else if (table.equals("LabourStatistics")) {
            table = "labour";
        }
        // int tempCount;
        stateNames.add("NSW");
        stateNames.add("VIC");
        stateNames.add("QLD");
        stateNames.add("SA");
        stateNames.add("WA");
        stateNames.add("TAS");
        stateNames.add("NT");
        stateNames.add("ACT");
        stateNames.add("OTHER");
        
        for (String i : stateNames) {
            int count = returnSingleState(table, i, status, sex, category);
            stateCounts.add(count);
        }
        
        // Finally we return all of the movies
        return stateCounts;
    }

    public ArrayList<Double> returnAllStateProps (String table, String status, String sex, String category, String sort) {
        ArrayList<String> stateNames = new ArrayList<String>();
        ArrayList<Double> stateProps = new ArrayList<Double>();
        // String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            table = "qualification";
            
        }
        else if (table.equals("SchoolStatistics")) {
            table = "yearlevel";
        }
        else if (table.equals("PopulationStatistics")) {
            table = "age";
        }
        else if (table.equals("LabourStatistics")) {
            table = "labour";
        }
        // int tempCount;
        stateNames.add("NSW");
        stateNames.add("VIC");
        stateNames.add("QLD");
        stateNames.add("SA");
        stateNames.add("WA");
        stateNames.add("TAS");
        stateNames.add("NT");
        stateNames.add("ACT");
        stateNames.add("OTHER");
        
        for (String i : stateNames) {
            double prop = returnSingleStateProp(table, i, status, sex, category);
            stateProps.add(prop);
        }
        
        // Finally we return all of the movies
        return stateProps;
    }

    // Deep dive methods

    public ArrayList<String> returnSingleHPropIndig(String table, String status, String sex, String category,
            String sort) {
        ArrayList<String> prop = new ArrayList<String>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";

        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }

        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT PERC FROM (SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM (" + "SELECT * FROM "
                        + table + " AS C NATURAL JOIN LGAs AS LLGAS " + "WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE 'non_indig' AND " + param + " LIKE '" + category
                        + "') AS NONSTATS " +

                        "JOIN (SELECT SUM(COUNT) AS TOTAL,IC.LGA_CODE16 FROM '" + table + "' AS IC "
                        + "GROUP BY IC.LGA_CODE16) AS CO " + "ON NONSTATS.LGA_CODE16=CO.LGA_CODE16 ORDER BY COUNT "
                        + sort + ") AS NSTATS";
            } else if (sort.equals("HProp") || sort.equals("LProp")) {
                // Get Result
                switch (sort) {
                case "HProp":
                    sort = "DESC";
                    break;
                case "LProp":
                    sort = "ASC";
                    break;
                }
                query = "SELECT PERC FROM (SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM (" + "SELECT * FROM "
                        + table + " AS C NATURAL JOIN LGAs AS LLGAS " + "WHERE SEX LIKE '" + sex
                        + "' AND INDIGENOUS_STATUS LIKE 'non_indig' AND " + param + " LIKE '" + category
                        + "') AS NONSTATS " +

                        "JOIN (SELECT SUM(COUNT) AS TOTAL,IC.LGA_CODE16 FROM '" + table + "' AS IC "
                        + "GROUP BY IC.LGA_CODE16) AS CO " + "ON NONSTATS.LGA_CODE16=CO.LGA_CODE16 ORDER BY PERC "
                        + sort + ") AS NSTATS";
            }
            // Get Result
            ResultSet results = statement.executeQuery(query);

            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                String pro = results.getString("PERC");

                // For now we will just store the movieName and ignore the id
                prop.add(pro);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }
        return prop;
    }

    public ArrayList<Object> returnAllHData(String table, String status, String sex, String category, String sort, String lgas, double min, double max, String limit) {
        ArrayList<Object> Data = new ArrayList<Object>();
        String param = "nonparam";
        if (table.equals("HigherStatistics")) {
            param = "qualification";
        } else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        } else if (table.equals("PopulationStatistics")) {
            param = "age";
        } else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            String query = "SELECT count FROM SchoolStatistics";

            // The Query
            if (sort.equals("ASC") || sort.equals("DESC")) {
                query = "SELECT INSTATS.LGA_NAME16 LGA,INSTATS.SEX INSEX,INSTATS."+param+" INCAT,INSTATS.COUNT INCOUNT,INSTATS.PERC INPROP,NOSTATS.COUNT NONCOUNT,NOSTATS.PERC NONPROP,NOSTATS.PERC-INSTATS.PERC GAP FROM " +
                "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM (" +
                "SELECT * FROM "+ table + " AS C NATURAL JOIN LGAs AS LLGAS "+
                "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'non_indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS "+
        
                "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAI.LGA_NAME16 FROM "+ table + " AS IC NATURAL JOIN LGAS LGAI " +
                "GROUP BY LGAI.LGA_NAME16) AS CO "+
                "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS NOSTATS JOIN "+
        
        
                "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM ("+
                "SELECT * FROM "+ table +" AS C NATURAL JOIN LGAs AS LLGAS " +
                "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS " +
        
                "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAII.LGA_NAME16 FROM "+table+" AS IC NATURAL JOIN LGAS LGAII " +
                "GROUP BY LGAII.LGA_NAME16) AS CO "+
                "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS INSTATS "+
        
                "ON NOSTATS.SEX=INSTATS.SEX AND NOSTATS."+param+"=INSTATS."+param+" AND INSTATS.LGA_CODE16=NOSTATS.LGA_CODE16 "+
                "WHERE " + limit + " < " + max + " AND " + limit + " > " + min + " ORDER BY INCOUNT "+sort+";";
                
    } else if (sort.equals("HProp") || sort.equals("LProp")) {
        // Get Result
        switch (sort) {
        case "HProp":
            sort = "DESC";
            break;
        case "LProp":
            sort = "ASC";
            break;
        }
         
        query = "SELECT INSTATS.LGA_NAME16 LGA,INSTATS.SEX INSEX,INSTATS."+param+" INCAT,INSTATS.COUNT INCOUNT,INSTATS.PERC INPROP,NOSTATS.COUNT NONCOUNT,NOSTATS.PERC NONPROP,NOSTATS.PERC-INSTATS.PERC GAP FROM " +
        "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM (" +
        "SELECT * FROM "+ table + " AS C NATURAL JOIN LGAs AS LLGAS "+
        "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'non_indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS "+

        "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAI.LGA_NAME16 FROM "+ table + " AS IC NATURAL JOIN LGAS LGAI " +
        "GROUP BY LGAI.LGA_NAME16) AS CO "+
        "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS NOSTATS JOIN "+


        "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM ("+
        "SELECT * FROM "+ table +" AS C NATURAL JOIN LGAs AS LLGAS " +
        "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS " +

        "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAII.LGA_NAME16 FROM "+table+" AS IC NATURAL JOIN LGAS LGAII " +
        "GROUP BY LGAII.LGA_NAME16) AS CO "+
        "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS INSTATS "+

        "ON NOSTATS.SEX=INSTATS.SEX AND NOSTATS."+param+"=INSTATS."+param+" AND INSTATS.LGA_CODE16=NOSTATS.LGA_CODE16" + 
        " WHERE " + limit + " < " + max + " AND " + limit + " > " + min + " ORDER BY ORDER BY INPROP "+sort+";";
    }
    else if (sort.equals("HGap") || sort.equals("LGap")) {
        sort = switchSort(sort); // converts sort to SQL syntax

        query = "SELECT INSTATS.LGA_NAME16 LGA,INSTATS.SEX INSEX,INSTATS."+param+" INCAT,INSTATS.COUNT INCOUNT,INSTATS.PERC INPROP,NOSTATS.COUNT NONCOUNT,NOSTATS.PERC NONPROP,NOSTATS.PERC-INSTATS.PERC GAP FROM " +
        "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM (" +
        "SELECT * FROM "+ table + " AS C NATURAL JOIN LGAs AS LLGAS "+
        "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'non_indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS "+

        "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAI.LGA_NAME16 FROM "+ table + " AS IC NATURAL JOIN LGAS LGAI " +
        "GROUP BY LGAI.LGA_NAME16) AS CO "+
        "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS NOSTATS JOIN "+


        "(SELECT *,TOTAL,(COUNT * 1.00/TOTAL)*100 AS PERC FROM ("+
        "SELECT * FROM " + table +" AS C NATURAL JOIN LGAs AS LLGAS " + 
        "WHERE C.SEX LIKE '"+ sex +"' AND C.INDIGENOUS_STATUS LIKE 'indig' AND C."+param+" LIKE '" +category+"' AND LLGAS.LGA_NAME16 LIKE '"+lgas+"') AS NONSTATS " +

        "JOIN (SELECT SUM(COUNT) AS TOTAL,LGAII.LGA_NAME16 FROM "+table+" AS IC NATURAL JOIN LGAS LGAII " +
        "GROUP BY LGAII.LGA_NAME16) AS CO "+
        "ON NONSTATS.LGA_NAME16=CO.LGA_NAME16) AS INSTATS "+

        "ON NOSTATS.SEX=INSEX AND NOSTATS."+param+"=INCAT AND INSTATS.LGA_CODE16=NOSTATS.LGA_CODE16 " +
        " WHERE " + limit + " < " + max + " AND " + limit + " > " + min + " ORDER BY GAP "+sort+";";
    }
    // Get Result
    ResultSet results = statement.executeQuery(query);

    // Process all of the results
    // The "results" variable is similar to an array
    // We can iterate through all of the database query results
    while (results.next()) {
        // We can lookup a column of the a single record in the
        // result using the column name
        // BUT, we must be careful of the column type!
        String lga = results.getString("LGA");
        String gender = results.getString("INSEX");
        String categ = results.getString("INCAT");
        String incount = results.getString("INCOUNT");
        Double inprop = results.getDouble("INPROP");
        String noncount = results.getString("NONCOUNT");
        Double nonprop = results.getDouble("NONPROP");
        Double gap = results.getDouble("GAP");

        gender = switchSex(gender);
        categ = switchCategory(categ);



                Data.add(lga);
                Data.add(gender);
                Data.add(categ);
                Data.add(incount);
                Data.add(inprop);
                Data.add(noncount);
                Data.add(nonprop);
                Data.add(gap);
            }

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return Data;
    }

    

    public String switchSex(String sex) {
        switch (sex) {
        case "m":
            sex = "Male";
            break;
        case "f":
            sex = "Female";
            break;
        case "%%":
            sex = "Any";
            break;
        }
        return sex;
    }

    public String switchStatus(String status) {
        switch (status) {
        case "indig":
            status = "Indigenous";
            break;
        case "non_indig":
            status = "Non-Indigenous";
            break;
        case "indig_ns":
            status = "Non-Stated";
            break;
        case "%%":
            status = "Any";
            break;
        }
        return status;
    }

    public String switchCategory(String category) {
        switch (category) {
        case "%%":
            category = "Any";
            break;
        case "in_lf_emp":
            category = "Employed";
            break;
        case "in_lf_unemp":
            category = "Unemployed";
            break;
        case "indsec_gov":
            category = "Industry Sector Government";
            break;
        case "indsec_priv":
            category = "Industry Sector Private";
            break;
        case "self_employed":
            category = "Self-Employed";
            break;
        case "not_in_lf":
            category = "Not in the Labour Force";
            break;
        case "no_school":
            category = "Did not attend School";
            break;
        case "y8_below":
            category = "Year 8 or Below";
            break;
        case "y10_equiv":
            category = "Year 10 or Equivalent";
            break;
        case "y12_equiv":
            category = "Year 12 or Equivalent";
            break;
        case "nsq_ce_ii":
            category = "Certificate II";
            break;
        case "nsq_ad_dl":
            category = "Advanced Diploma";
            break;
        case "nsq_bdl":
            category = "Bachelor Degree";
            break;
        case "nsq_gd_gcl":
            category = "Graduate Diploma/Graduate Certificate";
            break;
        case "nsq_pgdl":
            category = "Postgraduate Degree";
            break;
        case "_0_4":
            category = "0-4";
            break;
        case "_5_9":
            category = "5-9";
            break;
        case "_10_14":
            category = "10-14";
            break;
        case "_15_19":
            category = "15-19";
            break;
        case "_20_24":
            category = "20-24";
            break;
        case "_25_29":
            category = "25-29";
            break;
        case "_30_34":
            category = "30-34";
            break;
        case "_35_39":
            category = "35-39";
            break;
        case "_40_44":
            category = "40-44";
            break;
        case "_45_49":
            category = "45-49";
            break;
        case "_50_54":
            category = "50-54";
            break;
        case "_55_59":
            category = "55-59";
            break;
        case "_60_64":
            category = "60-64";
            break;
        case "_65_yrs_ov":
            category = "65+";
            break;
        }
        return category;
    }

    public String switchSort(String sort) {
        switch (sort) {
        case "HProp":
            sort = "DESC";
            break;
        case "LProp":
            sort = "ASC";
            break;
        case "LGap":
            sort = "ASC";
            break;
        case "HGap":
            sort = "DESC";
            break;

        }
        return sort;
    }


    public double distanceLGA (double lat1, double lat2, double lon1, double lon2) {
        
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);
             
        double c = 2 * Math.asin(Math.sqrt(a));
        // Constant for earths radius
        double r = 6371;

        return(c * r);
    }

    public ArrayList<Double> findLatLon(String lga) {
        ArrayList<Double> latlon = new ArrayList<Double>();

        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT latitude, longitude FROM LGAs WHERE lga_name16 LIKE \'" + lga + "\';";
            
            
            // Get Result
            ResultSet results = statement.executeQuery(query);
            double lat = results.getDouble("latitude");
            double lon = results.getDouble("longitude");
            latlon.add(lat);
            latlon.add(lon);
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }



        return latlon;
    }

    public ArrayList<Integer> nearestLGACode(double lat, double lon, double range) {
        

        // Setup the variable for the JDBC connection
        Connection connection = null;
        ArrayList<Integer> lgaList = new ArrayList<Integer>();

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM (SELECT *, (((acos(sin((" + lat + " * pi() / 180)) * sin((latittude * pi() / 180)) + cos ((" + lat + " * pi() / 180)) * cos((latitude *pi() / 180)) * cos(((" + lon + " - longitude) * pi() / 180)))) * 180 / pi()) * 60 * 1.1515 * 1.609344) as distance FROM LGAs) LGAs WHERE distance <= " + range + " LIMIT 10;";
            
            
            // Get Result
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                Integer lgaCode = results.getInt("lga_code16");
                // String lgaName = results.getString("lga_name16");
                // String lgaType = results.getString("lga_type16");
                // Double area = results.getDouble("area_sqkm");
                // Double lgaLat = results.getDouble("latitude");
                // Double lgaLon = results.getDouble("longitude");
                // Double distanceFrom = results.getDouble("distance");
        
                lgaList.add(lgaCode);
                // lgaList.add(lgaName);
                // lgaList.add(lgaType);
                // lgaList.add(area);
                // lgaList.add(lgaLat);
                // lgaList.add(lgaLon);
                // lgaList.add(distanceFrom);
            }
            
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }



        return lgaList;
    }
    

    public Integer lgaCountUsingCode (String table, Integer lgaCode, String status, String sex, String category) {    
        
        // ArrayList<Integer> lgaCounts = new ArrayList<Integer>();
        String param = "SchoolStatistics";
        int count=-1;
        if (table.equals("HigherStatistics")) {
            param = "qualification";
            
        }
        else if (table.equals("SchoolStatistics")) {
            param = "yearlevel";
        }
        else if (table.equals("PopulationStatistics")) {
            param = "age";
        }
        else if (table.equals("LabourStatistics")) {
            param = "labour";
        }
        // int tempCount;
        // Setup the variable for the JDBC connection
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SUM(count) FROM " + table;
            query = query + " NATURAL JOIN LGAs WHERE SEX LIKE '" + sex + "' AND INDIGENOUS_STATUS LIKE '" + status + "' AND " + param + " LIKE '"+ category + "'";
            query = query + " AND lga_code16 = \'" + lgaCode + "\';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            count = results.getInt("SUM(count)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return count;
    }
    public Integer lgaPopUsingCode (Integer lgaCode){
        int count = -1;
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT SUM(count) FROM PopulationStatistics WHERE lga_code16 = /'" + lgaCode + "\';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            count = results.getInt("SUM(count)");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return count;
    }
    public double lgaSizeUsingCode (Integer lgaCode){
        double size = -1;
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT area_sqkm FROM LGAs WHERE lga_code16 = /'" + lgaCode + "\';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            size = results.getDouble("area_sqkm");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return size;
    }
    
    public ArrayList<Integer> distanceFromLGA(double lat, double lon, double range) {
        

        // Setup the variable for the JDBC connection
        Connection connection = null;
        ArrayList<Integer> lgaDistances = new ArrayList<Integer>();

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT * FROM (SELECT *, (((acos(sin((" + lat + " * pi() / 180)) * sin((latittude * pi() / 180)) + cos ((" + lat + " * pi() / 180)) * cos((latitude *pi() / 180)) * cos(((" + lon + " - longitude) * pi() / 180)))) * 180 / pi()) * 60 * 1.1515 * 1.609344) as distance FROM LGAs) LGAs WHERE distance <= " + range + " LIMIT 10;";
            
            
            // Get Result
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                // We can lookup a column of the a single record in the
                // result using the column name
                // BUT, we must be careful of the column type!
                Integer distance = results.getInt("distance");
                // String lgaName = results.getString("lga_name16");
                // String lgaType = results.getString("lga_type16");
                // Double area = results.getDouble("area_sqkm");
                // Double lgaLat = results.getDouble("latitude");
                // Double lgaLon = results.getDouble("longitude");
                // Double distanceFrom =;
        
                lgaDistances.add(distance);
                // lgaList.add(lgaName);
                // lgaList.add(lgaType);
                // lgaList.add(area);
                // lgaList.add(lgaLat);
                // lgaList.add(lgaLon);
                // lgaList.add(distanceFrom);
            }
            
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }



        return lgaDistances;
    }

    public String lgaTypeUsingCode (Integer lgaCode){
        String type = "NA";
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT lga_type16 FROM LGAs WHERE lga_code16 = /'" + lgaCode + "\';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            type = results.getString("lga_type16");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return type;
    }
        
    public String lgaNameUsingCode (Integer lgaCode){
        String name = "NA";
        Connection connection = null;

        try {
            // Connect to JDBC data base
            connection = DriverManager.getConnection(DATABASE);

            // Prepare a new SQL Query & Set a timeout
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            // The Query
            String query = "SELECT lga_name16 FROM LGAs WHERE lga_code16 = /'" + lgaCode + "\';";

            // Get Result
            ResultSet results = statement.executeQuery(query);
            name = results.getString("lga_name16");
            // Process all of the results
            // The "results" variable is similar to an array
            // We can iterate through all of the database query results

            // Close the statement because we are done with it
            statement.close();
        } catch (SQLException e) {
            // If there is an error, lets just pring the error
            System.err.println(e.getMessage());
        } finally {
            // Safety code to cleanup
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e.getMessage());
            }
        }

        // Finally we return all of the movies
        return name;
    }

    public String lgaStateUsingCode (Integer lgaCode){
        String state = "NA";
        if (lgaCode < 20000){
            state = "New South Wales";
        }
        else if (lgaCode < 30000){
            state = "Victoria";
        }
        else if (lgaCode < 40000){
            state = "Queensland";
        }
        else if (lgaCode < 50000){
            state = "South Australia";
        }
        else if (lgaCode < 60000){
            state = "Western Austrlia";
        }
        else if (lgaCode < 70000){
            state = "Tasmania";
        }
        else if (lgaCode < 80000){
            state = "Northern Territory";
        }
        else if (lgaCode < 90000) {
            state = "ACT";
        }
        else {
            state = "Other";
        }

        // Finally we return all of the movies
        return state;
    }

}





