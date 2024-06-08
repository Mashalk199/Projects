package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Temporary HTML as an example page.
 * 
 * Based on the Project Workshop code examples. This page currently: - Provides
 * a link back to the index page - Displays the list of movies from the Movies
 * Database using the JDBCConnection
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class L2P1 implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/L2P1.html";
    JDBCConnection jdbc = new JDBCConnection();
    ArrayList<String> areas = jdbc.dropdownLGA();

    @Override
    public void handle(Context context) throws Exception {

        // Create a simple HTML webpage in a String
        String html = "";
        html = html
                + """
                        <!DOCTYPE html>
                        <html>

                        <head>
                            <link type='text/css' rel='stylesheet' href='style.css'>
                            <style>
                                h1 {
                                    clear: both;
                                    position: relative;
                                    left: 80px;
                                    bottom: 20px;
                                }

                                h4 {
                                    position: absolute;
                                    top: 150px;
                                    left: 600px;
                                }

                                h4#state {
                                    position: absolute;
                                    top: 650px;
                                    left: 600px;
                                }

                                ul.navbar {
                                    clear: both;
                                }

                                
                                
                            </style>

                        </head>



                        <body>     <img class='background' src='steve-johnson-BgDoX0zk4zw-unsplash.jpg' width='100%'>
                            <h1 id='logo'>the bridge</h1>
                            <nav role='navigation' aria-label='main menu'>
                                <ul class='navbar'>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='/'>Home</a></li>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='L1P2.html'>What you need to know</a></li>
                                    <a href='/L2P1.html'>
                                        <li style='color:rgba(0, 255, 0, 0.884);'>Glance at whats happening</li>
                                    </a>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='L3P1.html'>Deep dive</a></li>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='AboutUs.html'>About us</a></li>
                                </ul>
                            </nav>
                            <h1> Here is whats happening...</h1>



                            <div>


                            <h4>Filter by LGA</h4>

                                <div>
                                <form  class='container' action='/L2P1.html' method='POST'>

                                    <div>
                                        <p>Select Socio-Economic Outcome</p>
                                            <select name='outcome_drop'>
                                                <option value='SchoolStatistics'>Highest Year of School Completion</option>
                                                <option value='PopulationStatistics'>Indigenous Status by Age</option>
                                                <option value='LabourStatistics'>Labour force by Indigenous Status</option>
                                                <option value='HigherStatistics'>Non-school Education by Indigenous Status</option>
                                            </select>
                                    </div>


                                    <div>
                                        <p>Gender</p>
                                            <select name='gender_drop'>
                                            <option value='any'>Any</option>
                                                <option value='m'>Male</option>
                                                <option value='f'>Female</option>

                                            </select>
                                    </div>



                                    <div>
                                        <p>Indigenous Status</p>

                                            <select name='indig_drop'>
                                            <option value='any'>Any</option>
                                                <option value='indig'>Indigenous</option>
                                                <option value='non_indig'>Non-Indigenous</option>
                                                <option value='indig_ns'>Non-Stated</option>
                                            </select>
                                    </div>


                                    <div>
                                        <p>Local Government Area</p>
                                            <select name='lga_drop'>
                                            <option value='all'>All</option>
                                            """;
        for (String area : areas) {
            html = html + "<option value='" + area + "'>" + area + "</option>";
        }
        html = html + """

                                    </select>
                            </div>
                            <div>
                                <p style='color: white;'>School Completion</p>

                                    <select name='school_drop'>
                                    <option value='any'>Any</option>
                                        <option value='no_school'>Didn't go to school</option>
                                        <option value='y8_below'>Completed Year 8</option>
                                        <option value='y10_equiv'>Completed Year 10</option>
                                        <option value='y12_equiv'>Completed Year 12</option>

                                    </select>
                            </div>


                            <div>
                                <p style='color: white;'>Age</p>

                                    <select name='age_drop'>
                                    <option value='any'>Any</option>

                                        <option value='_0_4'>0-4</option>
                                        <option value='_5_9'>5-9</option>
                                        <option value='_10_14'>10-14</option>
                                        <option value='_15_19'>15-19</option>
                                        <option value='_20_24'>20-24</option>
                                        <option value='_25_29'>25-29</option>
                                        <option value='_30_34'>30-34</option>
                                        <option value='_35_39'>35-39</option>
                                        <option value='_40_44'>40-44</option>
                                        <option value='_45_49'>45-49</option>
                                        <option value='_50_54'>50-54</option>
                                        <option value='_55_59'>55-59</option>
                                        <option value='_60_64'>60-64</option>
                                        <option value='_65_yrs_ov'>65+</option>
                                    </select>
                            </div>

                            <div>
                                <p>Higher Education Level</p>
                                    <select name='higher_drop'>
                                    <option value='any'>Any</option>
                                        <option value='nsq_ce_ii'>Certificate II, AQF 2</option>
                                        <option value='nsq_ad_dl'>Advanced Diploma, AQF 6</option>
                                        <option value='nsq_bdl'>Bachelor Degree, AQF 7</option>
                                        <option value='nsq_gd_gcl'>Graduate Diploma/Graduate Certificate, AQF 8</option>
                                        <option value='nsq_pgdl'>Postgraduate Degree, AQF 9</option>
                                    </select>
                            </div>

                            <div>
                                <p>Employment Status</p>
                                    <select name='labour_drop'>
                                    <option value='any'>Any</option>

                                        <option value='in_lf_emp'>Employed</option>
                                        <option value='in_lf_unemp'>Unemployed</option>
                                        <option value='indsec_gov'>Industry sector Government</option>
                                        <option value='indsec_priv'>Industry sector Private</option>
                                        <option value='self_employed'>Self Employed</option>
                                        <option value='n_the_lf'>Not in the labour force</option>
                                    </select>
                            </div>


                            <div>
                                <p style='color: white;'>Sort By</p>
                                    <select name='sort_drop'>
                                        <option value='DESC'>Highest Count</option>
                                        <option value='HProp'>Highest Proportion</option>
                                        <option value='ASC'>Lowest Count</option>
                                        <option value='LProp'>Lowest Proportion</option>


                                    </select>

                            </div>

                            <div>
                            <p><p/>
                            <br><button type='submit' style='width:100px;height:50px;'>Go!</button>
                            </div>
                </form><br><br>
                        </div>
                    </div>
                    <div class='TableFixHead '>
                        <table>
                            <thead>
                            
                                <tr>
                                    <th>Local Government Area</th>
                                    <th>Indigenous Status</th>
                                    <th>Gender</th>
                                    <th>Category</th>
                                    <th>Count</th>
                                    <th>Proportion (%)</th>
                                </tr>
                               <tr> """;
        String outcome = context.formParam("outcome_drop");
        String gender = context.formParam("gender_drop");
        String status = context.formParam("indig_drop");
        String lga = context.formParam("lga_drop");
        String sort = context.formParam("sort_drop");
        String schoolyear = context.formParam("school_drop");
        String age = context.formParam("age_drop");
        String higher = context.formParam("higher_drop");
        String labour = context.formParam("labour_drop");


        if (gender != null) {
            if (gender.equals("any")) {
                gender = "%%";
            }
        }
        if (status != null) {
            if (status.equals("any")) {
                status = "%%";
            }
        }
        if (schoolyear != null) {
            if (schoolyear.equals("any")) {
                schoolyear = "%%";
            }
        }
        if (age != null) {
            if (age.equals("any")) {
                age = "%%";
            }
        }
        if (higher != null) {
            if (higher == "any") {
                higher = "%%";
            }
        }
        if (labour != null) {
            if (labour.equals("any")) {
                labour = "%%";
            }
        }
        if (labour != null) {
            if (labour.equals("any")) {
                labour = "%%";
            }
        }

        if (lga == null) {
            html = html + "";
        }
        else if (!lga.equals("all")) {
            html = html + outputSingleLGA(outcome, lga, status, gender, age, schoolyear, higher, labour);
        }
        else if (lga.equals("all") && ((sort.equals("DESC") || (sort.equals("ASC"))))) {
            lga = "%%";
            html = html + outputAllLGA(outcome, status, gender, age, schoolyear, higher, labour, sort);
        }
        else if (lga.equals("all") && ((sort.equals("HProp") || (sort.equals("LProp"))))) {
            lga = "%%";
            html = html + outputAllPropLGA(outcome, status, gender, age, schoolyear, higher, labour, sort);
        }
        html = html + """
                                </tr>
                            </thead>
                        </table>
                    </div> 
                <br>
                <br>
                <br>
                <br>
                <div>
                    <h4 id='state'>Filter by State</h4>

                    <div>
                        <form  class='container' action='/L2P1.html' method='POST'>

                            <div>
                                <p>Select Socio-Economic Outcome</p>
                                    <select name='outcome_drop'>
                                        <option value='SchoolStatistics'>Highest Year of School Completion</option>
                                        <option value='PopulationStatistics'>Indigenous Status by Age</option>
                                        <option value='LabourStatistics'>Labour force by Indigenous Status</option>
                                        <option value='HigherStatistics'>Non-school Education by Indigenous Status</option>
                                    </select>
                            </div>


                            <div>
                                <p>Gender</p>
                                    <select name='gender_drop'>
                                    <option value='any'>Any</option>
                                        <option value='m'>Male</option>
                                        <option value='f'>Female</option>

                                    </select>
                            </div>



                            <div>
                                <p>Indigenous Status</p>

                                    <select name='indig_drop'>
                                    <option value='any'>Any</option>
                                        <option value='indig'>Indigenous</option>
                                        <option value='non_indig'>Non-Indigenous</option>
                                        <option value='indig_ns'>Non-Stated</option>
                                    </select>
                            </div>


                            <div>
                                <p>State</p>
                                    <select name='state_drop'>
                                    <option value='all'>All</option>
                                    <option value='NSW'>New South Wales</option>
                                    <option value='VIC'>Victoria</option>
                                    <option value='QLD'>Queensland</option>
                                    <option value='SA'>South Australia</option>
                                    <option value='WA'>Western Australia</option>
                                    <option value='TAS'>Tasmania</option>
                                    <option value='NT'>Northern Territory</option>
                                    <option value='ACT'>ACT</option>
                                    <option value='OTHER'>Other</option>
                                    </select>
                            </div>

                    <div>
                        <p style='color: white;'>School Completion</p>

                            <select name='school_drop'>
                            <option value='any'>Any</option>
                                <option value='no_school'>Didn't go to school</option>
                                <option value='y8_below'>Completed Year 8</option>
                                <option value='y10_equiv'>Completed Year 10</option>
                                <option value='y12_equiv'>Completed Year 12</option>

                            </select>
                    </div>


                    <div>
                        <p style='color: white;'>Age</p>

                            <select name='age_drop'>
                            <option value='any'>Any</option>

                                <option value='_0_4'>0-4</option>
                                <option value='_5_9'>5-9</option>
                                <option value='_10_14'>10-14</option>
                                <option value='_15_19'>15-19</option>
                                <option value='_20_24'>20-24</option>
                                <option value='_25_29'>25-29</option>
                                <option value='_30_34'>30-34</option>
                                <option value='_35_39'>35-39</option>
                                <option value='_40_44'>40-44</option>
                                <option value='_45_49'>45-49</option>
                                <option value='_50_54'>50-54</option>
                                <option value='_55_59'>55-59</option>
                                <option value='_60_64'>60-64</option>
                                <option value='_65_yrs_ov'>65+</option>
                            </select>
                    </div>

                    <div>
                        <p>Higher Education Level</p>
                            <select name='higher_drop'>
                            <option value='any'>Any</option>
                                <option value='nsq_ce_ii'>Certificate II, AQF 2</option>
                                <option value='nsq_ad_dl'>Advanced Diploma, AQF 6</option>
                                <option value='nsq_bdl'>Bachelor Degree, AQF 7</option>
                                <option value='nsq_gd_gcl'>Graduate Diploma/Graduate Certificate, AQF 8</option>
                                <option value='nsq_pgdl'>Postgraduate Degree, AQF 9</option>
                            </select>
                    </div>

                    <div>
                        <p>Employment Status</p>
                            <select name='labour_drop'>
                            <option value='any'>Any</option>

                                <option value='in_lf_emp'>Employed</option>
                                <option value='in_lf_unemp'>Unemployed</option>
                                <option value='indsec_gov'>Industry sector Government</option>
                                <option value='indsec_priv'>Industry sector Private</option>
                                <option value='self_employed'>Self Employed</option>
                                <option value='n_the_lf'>Not in the labour force</option>
                            </select>
                    </div>


                    <div>
                        <p style='color: white;'>Sort By</p>
                            <select name='sort_drop'>
                                <option value='DESC'>Highest Count</option>
                                <option value='HProp'>Highest Proportion</option>
                                <option value='ASC'>Lowest Count</option>
                                <option value='LProp'>Lowest Proportion</option>
                            </select>
                    </div>
                    <div>
                        <p><p/>
                        <br>
                        <button type='submit' style='width:100px;height:50px;'>Go!</button>
                    </div>
                </form>
                <br>
                <br>
            </div>
        </div>
                   
        
        <div class='TableFixHead '>
            <table>
                <thead>
                    <tr>
                        <th>State</th>
                        <th>Indigenous Status</th>
                        <th>Gender</th>
                        <th>Category</th>
                        <th>Count</th>
                        <th>Proportion (%)</th>
                    </tr>
                <tr> """;
        String outcome2 = context.formParam("outcome_drop");
        String gender2 = context.formParam("gender_drop");
        String status2 = context.formParam("indig_drop");
        String state = context.formParam("state_drop");
        String sort2 = context.formParam("sort_drop");
        String schoolyear2 = context.formParam("school_drop");
        String age2 = context.formParam("age_drop");
        String higher2 = context.formParam("higher_drop");
        String labour2 = context.formParam("labour_drop");


        if (gender2 != null) {
            if (gender2.equals("any")) {
                gender2 = "%%";
            }
        }
        if (status2 != null) {
            if (status2.equals("any")) {
                status2 = "%%";
            }
        }
        if (schoolyear2 != null) {
            if (schoolyear2.equals("any")) {
                schoolyear2 = "%%";
            }
        }
        if (age2 != null) {
            if (age2.equals("any")) {
                age2 = "%%";
            }
        }
        if (higher2 != null) {
            if (higher2 == "any") {
                higher2 = "%%";
            }
        }
        if (labour2 != null) {
            if (labour2.equals("any")) {
                labour2 = "%%";
            }
        }
        if (labour2 != null) {
            if (labour2.equals("any")) {
                labour2 = "%%";
            }
        }

        if (state == null) {
            html = html + "";
        }
        else if (!state.equals("all")) {
            html = html + outputSingleState(outcome2, state, status2, gender2, age2, schoolyear2, higher2, labour2);
        }
        else if (state.equals("all") && ((sort2.equals("DESC") || (sort2.equals("ASC"))))) {
            state = "%%";
            html = html + outputAllState(outcome2, status2, gender2, age2, schoolyear2, higher2, labour2, sort2);
        }
        else if (state.equals("all") && ((sort2.equals("HProp") || (sort2.equals("LProp"))))) {
            state = "%%";
            html = html + outputAllPropState(outcome2, status2, gender2, age2, schoolyear2, higher2, labour2, sort2);
        }
        html = html + """
                                </tr>
                            </thead>
                        </table>
                    </div> 


                    


                </body>

                </html>
                """;

        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

    
    public String outputAllPropLGA(String table, String status, String sex, String age, String school, String higher,
    String labour, String sort) {
        String html = "";
        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        ArrayList<String> lgas = jdbc.returnAllLGA(table, status, sex, category, sort);
        ArrayList<String> statuses = jdbc.returnAllStatus(table, status, sex, category, sort);
        ArrayList<String> sexes = jdbc.returnAllSexes(table, status, sex, category, sort);
        ArrayList<String> categories = jdbc.returnAllCategories(table, status, sex, category, sort);
        ArrayList<Integer> counts = jdbc.returnAllCounts(table, status, sex, category, sort);
        ArrayList<Double> proportions = jdbc.returnAllProportions(table, status, sex, category, sort);
        int i = 0;
        for (String Lga : lgas) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + Lga + "</td><td>" + statuses.get(i) + "</td><td>" + sexes.get(i) + "</td><td>"
                    + categories.get(i) + "</td><td>" + counts.get(i) + "</td><td>" + String.format("%.2f",proportions.get(i)) +"</td>";
            html = html + "</tr>"; // end of row
            ++i;
        }

        return html;
    }

    public String outputAllLGA(String table, String status, String sex, String age, String school, String higher,
            String labour, String sort) {
        String html = "";
        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        ArrayList<String> lgas = jdbc.returnAllLGA(table, status, sex, category, sort);
        ArrayList<String> statuses = jdbc.returnAllStatus(table, status, sex, category, sort);
        ArrayList<String> sexes = jdbc.returnAllSexes(table, status, sex, category, sort);
        ArrayList<String> categories = jdbc.returnAllCategories(table, status, sex, category, sort);
        ArrayList<Integer> counts = jdbc.returnAllCounts(table, status, sex, category, sort);
        ArrayList<Double> proportions = jdbc.returnAllProportions(table, status, sex, category, sort);

        

        // Writes the data to the html table, when ALL LGAs are selected

        int i = 0;
        for (String Lga : lgas) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + Lga + "</td><td>" + statuses.get(i) + "</td><td>" + sexes.get(i) + "</td><td>"
                    + categories.get(i) + "</td><td>" + counts.get(i) + "</td><td>" + String.format("%.2f",proportions.get(i)) +"</td>";
            html = html + "</tr>"; // end of row
            ++i;
        }

        return html;
    }



    public String outputSingleLGA(String table, String lga, String status, String sex, String age, String school,
            String higher, String labour) {
        String html = "";

        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        int count = jdbc.returnSingleLGA(table, lga, status, sex, category);
        double prop = jdbc.returnSingleLGAProp(table, lga, status, sex, category);

        status = switchStatus(status);
        sex = switchSex(sex);
        category = switchCategory(category);

        html = html + "<td>" + lga + "</td><td>" + status + "</td><td>" + sex + "</td>";
        html = html + "<td>" + category + "</td>" + "<td>" + count + "</td><td>" + String.format("%.2f",prop) + "</td>";

        return html;
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
            case "n_the_lf":
                category = "Not in Labour Force";
                break;
            case "did_not_go_to_school":
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

    public String outputAllState(String table, String status, String sex, String age, String school, String higher,
            String labour, String sort) {
        String html = "";
        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        ArrayList<Integer> counts = jdbc.returnAllStateCounts(table, status, sex, category, sort);
        ArrayList<Double> props = jdbc.returnAllStateProps(table, status, sex, category, sort);
        // Creating a list of State names
        ArrayList<String> stateNames = new ArrayList<String>();
        stateNames.add("NSW");
        stateNames.add("VIC");
        stateNames.add("QLD");
        stateNames.add("SA");
        stateNames.add("WA");
        stateNames.add("TAS");
        stateNames.add("NT");
        stateNames.add("ACT");
        stateNames.add("OTHER");
        

        // Writes the data to the html table
        
        // NEED TO SETUP METHOD FOR PROPORTIONS

        int i = 0;
        for (String state : stateNames) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + state + "</td><td>" + status + "</td><td>" + sex + "</td><td>"
                    + category + "</td><td>" + counts.get(i) + "</td><td>" + String.format("%.2f",props.get(i)) +"</td>";
            html = html + "</tr>"; // end of row
            ++i;
        }

        return html;
    }
    public String outputAllPropState(String table, String status, String sex, String age, String school, String higher,
    String labour, String sort) {
        String html = "";
        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        
        ArrayList<Integer> counts = jdbc.returnAllStateCounts(table, status, sex, category, sort);
        ArrayList<Double> proportions = jdbc.returnAllStateProps(table, status, sex, category, sort);

        // Creating a list of State names
        ArrayList<String> stateNames = new ArrayList<String>();
        stateNames.add("NSW");
        stateNames.add("VIC");
        stateNames.add("QLD");
        stateNames.add("SA");
        stateNames.add("WA");
        stateNames.add("TAS");
        stateNames.add("NT");
        stateNames.add("ACT");
        stateNames.add("OTHER");

        int i = 0;
        for (String state : stateNames) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + state + "</td><td>" + status + "</td><td>" + sex + "</td><td>"
                    + category + "</td><td>" + counts.get(i) + "</td><td>" + String.format("%.2f",proportions.get(i)) +"</td>";
            html = html + "</tr>"; // end of row
            ++i;
        }

        return html;
    }
    public String outputSingleState(String table, String state, String status, String sex, String age, String school,
            String higher, String labour) {
        String html = "";

        JDBCConnection jdbc = new JDBCConnection();
        String category = "SchoolStatistics";
        if (table.equals("SchoolStatistics")) {
            category = school;
        } else if (table.equals("PopulationStatistics")) {
            category = age;
        } else if (table.equals("HigherStatistics")) {
            category = higher;
        } else if (table.equals("LabourStatistics")) {
            category = labour;
        }
        int count = jdbc.returnSingleState(table, state, status, sex, category);
        double prop = jdbc.returnSingleStateProp(table, state, status, sex, category);

        status = switchStatus(status);
        sex = switchSex(sex);
        category = switchCategory(category);
        // Creating a list of State names

        html = html + "<td>" + state + "</td><td>" + status + "</td><td>" + sex + "</td>";
        html = html + "<td>" + category + "</td>" + "<td>" + count + "</td><td>" + String.format("%.2f",prop) + "</td>";

        return html;
    }

    
}
