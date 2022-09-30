package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Stand-alone Java file for processing the database CSV files.
 * <p>
 * You can run this file using the "Run" or "Debug" options from within VSCode.
 * This won't conflict with the web server.
 * <p>
 * This program opens a CSV file from the Closing-the-Gap data set and uses JDBC
 * to load up data into the database.
 * <p>
 * To use this program you will need to change: 1. The input file location 2.
 * The output file location
 * <p>
 * This assumes that the CSV files are the the **database** folder.
 * <p>
 * WARNING: This code may take quite a while to run as there will be a lot of
 * SQL insert statments!
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * 
 */
public class ProcessCSV2 {

   // MODIFY these to load/store to/from the correct locations

   private static final String DATABASE = "jdbc:sqlite:database/ctg copy.db";
   private static String CSV_FILE = "database/lga_indigenous_status_by_age_by_sex_census_2016.csv";

   public static void main(String[] args) {
      // The following arrays define the order of columns in the INPUT CSV.
      // The order of each array MUST match the order of the CSV.
      // These are specific to the given file and should be changed for each file.
      // This is a *simple* way to help you get up and running quickly wihout being
      // confusing
      String category[] = { "_0_4", "_5_9", "_10_14", "_15_19", "_20_24", "_25_29", "_30_34", "_35_39", "_40_44",
            "_45_49", "_50_54", "_55_59", "_60_64", "_65_yrs_ov" };
      String status[] = { "indig", "non_indig", "indig_ns" };
      String sex[] = { "f", "m" };

      // JDBC Database Object
      Connection connection = null;

      // Like JDBCConnection, we need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE));

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Setup JDBC
         // Connect to JDBC data base
         connection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();

            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // These indicies track which column we are up to
            int indexStatus = 0;
            int indexSex = 0;
            int indexCategory = 0;

            // Save the lga_code as we need it for the foreign key
            String lgaCode = rowScanner.next();

            // Skip lga_name
            String lgaName = rowScanner.next();

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext() && indexCategory < category.length) {
               String count = rowScanner.next();

               // Prepare a new SQL Query & Set a timeout
               Statement statement = connection.createStatement();
               // statement.setQueryTimeout(30);

               // Create Insert Statement
               String query = "INSERT into PopulationStatistics VALUES (" + lgaCode + "," + "'" + status[indexStatus]
                     + "'," + "'" + sex[indexSex] + "'," + "'" + category[indexCategory] + "'," + count + ")";

               // Execute the INSERT
               System.out.println("Executing: " + query);
               statement.execute(query);

               // Update indices - go to next sex
               indexSex++;
               if (indexSex >= sex.length) {
                  // Go to next status
                  indexSex = 0;
                  indexStatus++;

                  if (indexStatus >= status.length) {
                     // Go to next Category
                     indexStatus = 0;
                     indexCategory++;
                  }
               }
               row++;
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
      CSV_FILE = "database/lga_labour_force_by_indigenous_status_by_sex_census_2016.csv";

      String labcategory[] = { "in_lf_emp", "in_lf_unemp", "indsec_gov", "indsec_priv", "self_employed", "not_in_lf", };
      String labstatus[] = { "indig", "non_indig", "indig_ns" };
      String labsex[] = { "f", "m" };

      // JDBC Database Object
      Connection labconnection = null;

      // Like JDBCConnection, we need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE));

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Setup JDBC
         // Connect to JDBC data base
         labconnection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();

            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // These indicies track which column we are up to
            int LabindexStatus = 0;
            int LabindexSex = 0;
            int LabindexCategory = 0;

            // Save the lga_code as we need it for the foreign key
            String lgaCode = rowScanner.next();

            // Skip lga_name
            String lgaName = rowScanner.next();

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext() && LabindexCategory < labcategory.length) {
               String count = rowScanner.next();

               // Prepare a new SQL Query & Set a timeout
               Statement statement = labconnection.createStatement();
               // statement.setQueryTimeout(30);

               // Create Insert Statement
               String query = "INSERT into LabourStatistics VALUES (" + lgaCode + "," + "'" + labstatus[LabindexStatus]
                     + "'," + "'" + labsex[LabindexSex] + "'," + "'" + labcategory[LabindexCategory] + "'," + count + ")";

               // Execute the INSERT
               System.out.println("Executing: " + query);
               statement.execute(query);

               // Update indices - go to next sex
               LabindexSex++;
               if (LabindexSex >= labsex.length) {
                  // Go to next status
                  LabindexSex = 0;
                  LabindexStatus++;

                  if (LabindexStatus >= labstatus.length) {
                     // Go to next Category
                     LabindexStatus = 0;
                     LabindexCategory++;
                  }
               }
               row++;
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }

      CSV_FILE = "database/lga_highest_year_of_school_completed_by_indigenous_status_by_sex_census_2016.csv";

      String yearcategory[] = { "no_school", "y8_below", "y10_equiv", "y12_equiv" };
      String yearstatus[] = { "indig", "non_indig", "indig_ns" };
      String yearsex[] = { "f", "m" };

      // JDBC Database Object
      Connection yearconnection = null;

      // Like JDBCConnection, we need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE));

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Setup JDBC
         // Connect to JDBC data base
         yearconnection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();

            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // These indicies track which column we are up to
            int YindexStatus = 0;
            int YindexSex = 0;
            int YindexCategory = 0;

            // Save the lga_code as we need it for the foreign key
            String lgaCode = rowScanner.next();

            // Skip lga_name
            String lgaName = rowScanner.next();

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext() && YindexCategory < yearcategory.length) {
               String count = rowScanner.next();

               // Prepare a new SQL Query & Set a timeout
               Statement statement = yearconnection.createStatement();
               // statement.setQueryTimeout(30);

               // Create Insert Statement
               String query = "INSERT into SchoolStatistics VALUES (" + lgaCode + "," + "'" + yearstatus[YindexStatus]
                     + "'," + "'" + yearsex[YindexSex] + "'," + "'" + yearcategory[YindexCategory] + "'," + count + ")";

               // Execute the INSERT
               System.out.println("Executing: " + query);
               statement.execute(query);

               // Update indices - go to next sex
               YindexSex++;
               if (YindexSex >= yearsex.length) {
                  // Go to next status
                  YindexSex = 0;
                  YindexStatus++;

                  if (YindexStatus >= yearstatus.length) {
                     // Go to next Category
                     YindexStatus = 0;
                     YindexCategory++;
                  }
               }
               row++;
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
      CSV_FILE = "database/lga_non_school_education_by_indigenous_status_by_sex_census_2016.csv";

      String highcategory[] = { "nsq_ce_ii", "nsq_ad_dl", "nsq_bdl", "nsq_gd_gcl", "nsq_pgdl" };
      String highstatus[] = { "indig", "non_indig", "indig_ns" };
      String highsex[] = { "f", "m" };

      // JDBC Database Object
      Connection highconnection = null;

      // Like JDBCConnection, we need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE));

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Setup JDBC
         // Connect to JDBC data base
         highconnection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();

            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // These indicies track which column we are up to
            int HindexStatus = 0;
            int HindexSex = 0;
            int HindexCategory = 0;

            // Save the lga_code as we need it for the foreign key
            String lgaCode = rowScanner.next();

            // Skip lga_name
            String lgaName = rowScanner.next();

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext() && HindexCategory < highcategory.length) {
               String count = rowScanner.next();

               // Prepare a new SQL Query & Set a timeout
               Statement statement = highconnection.createStatement();
               // statement.setQueryTimeout(30);

               // Create Insert Statement
               String query = "INSERT into HigherStatistics VALUES (" + lgaCode + "," + "'" + highstatus[HindexStatus]
                     + "'," + "'" + highsex[HindexSex] + "'," + "'" + highcategory[HindexCategory] + "'," + count + ")";

               // Execute the INSERT
               System.out.println("Executing: " + query);
               statement.execute(query);

               // Update indices - go to next sex
               HindexSex++;
               if (HindexSex >= highsex.length) {
                  // Go to next status
                  HindexSex = 0;
                  HindexStatus++;

                  if (HindexStatus >= highstatus.length) {
                     // Go to next Category
                     HindexStatus = 0;
                     HindexCategory++;
                  }
               }
               row++;
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
