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
public class L3P1 implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/L3P1.html";
    JDBCConnection jdbc = new JDBCConnection();
    ArrayList<String> areas = jdbc.dropdownLGA();

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "";
        html = html
                + """
                            <html>

                            <head>
                                <link rel=\"stylesheet\" href=\"style.css\">
                                <style>

                                        ul.navbar {
                                            clear: both;
                                        }

                                        p {
                                            color:white;
                                        }

                                        h3 {
                                            postion aboslute;
                                            top: 650px;
                                            left: 600px;
                                        }

                                </style>
                                </head>

                                <body> <img class='background' src='steve-johnson-BgDoX0zk4zw-unsplash.jpg' width='100%'>
                        <h1 id='logo'>the bridge</h1>
                        <nav role='navigation' aria-label='main menu'>
                                <ul class='navbar'>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='/'>Home</a></li>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='L1P2.html'>What you need to know</a></li>

                                        <li style='color:rgba(255, 255, 255, 0.884);'><a href='L2P1.html'>Glance at whats happening</a></li>

                                    <a href='L3P1.html'><li style='color:rgba(0, 255, 0);'>Deep dive</li></a>
                                    <li style='color:rgba(255, 255, 255, 0.884);'><a href='AboutUs.html'>About us</a></li>
                                </ul>
                            </nav>

                            <div>



                                <div>
                                <form  class='container' action='/L3P1.html' method='POST' style='width:500px;position:relative;left:120px;'>

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
        html = html
                + """

                                            </select>
                                            </div>

                                    <div>
                                        <p>School Completion</p>

                                            <select name='school_drop'>
                                            <option value='any'>Any</option>
                                                <option value='no_school'>Didn't go to school</option>
                                                <option value='y8_below'>Completed Year 8</option>
                                                <option value='y10_equiv'>Completed Year 10</option>
                                                <option value='y12_equiv'>Completed Year 12</option>

                                            </select>
                                    </div>


                                    <div>
                                        <p>Age</p>

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
                                        <p>Sort By (Indigenous)</p>
                                            <select name='sort_drop'>
                                                <option value='DESC'>Highest Count</option>
                                                <option value='HProp'>Highest Proportion</option>
                                                <option value='HGap'>Highest Gap</option>
                                                <option value='ASC'>Lowest Count</option>
                                                <option value='LProp'>Lowest Proportion</option>
                                                <option value='LGap'>Lowest Gap</option>
                                            </select>

                                    </div>
                                    <br><br>
                                    <div>
                                        <p>Limit by</p>
                                            <select name='limit_drop'>
                                                <option value='INCOUNT'>Count</option>
                                                <option value='INPROP'>Proportion</option>
                                                <option value='GAP'>Gap</option>
                                            </select>
                                    </div>

                                    <div>
                                    <label for='min' style='position:relative;top:45px;color:white;'>Min</label>
                                    <input type='number' name='min' style='width:100px;height:30px;position:relative;top:45px;padding-right:0px;margin-right:0px;'>
                                    </div>

                                    <div>
                                    <label for='max' style='position:relative;top:45px;color:white;'>Max</label>
                                    <input type='number' name='max' style='width:100px;height:30px;position:relative;top:45px;padding-left:0px;margin-left:0px;'>
                                    </div>


                                    <div>
                                    <p><p/>
                                    <br><button type='submit' style='width:100px;height:50px;'>Go!</button>
                                    </div>
                        </form>

                        <br><br>
                                </div>

                                <div class='TableFixHead' style='position:relative;bottom:0px;'>
                                <table style='width:600px;'>
                                    <thead>
                                    <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th colspan='2' style='border-right:1px solid;border-left:1px solid;border-color: #d3d3d3e3;'>Indigenous</th>
                                    <th colspan='2' style='border-right:1px solid;border-left:1px solid;border-color: #d3d3d3e3;'>Non-Indigenous</th>

                                    </tr>

                                        <tr>
                                            <th>Local Government Area</th>
                                            <th>Gender</th>
                                            <th>Category</th>
                                            <th style='border-left:1px solid;border-color: #d3d3d3e3;'>Count</th>
                                            <th style='border-right:1px solid;border-color: #d3d3d3e3;'>Proportion (%)</th>
                                            <th style='border-left:1px solid;border-color: #d3d3d3e3';>Count</th>
                                            <th style='border-right:1px solid;border-color: #d3d3d3e3';>Proportion (%)</th>
                                            <th>Gap (%)</th>
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
        String limit = context.formParam("limit_drop");
        String max = context.formParam("max");
        String min = context.formParam("min");

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
        if (limit != null) {
            if (min == null) {
                if (limit.equals("INCOUNT")) {
                    min = "0";
                } 
                else if (limit.equals("INPROP")) {
                    min = "0";
                } 
                else if (limit.equals("GAP")) {
                    min = "-100";
                }
            }
            if (max == null) {
                if (limit.equals("INCOUNT")) {
                    max = "25000000";
                } 
                else if (limit.equals("INPROP")) {
                    max = "100";
                } 
                else if (limit.equals("GAP")) {
                    max = "100";
                }
            }
        }
        

        // double maxv = 100;
        // double minv = 0;

        // max = "3000";
        // min = "2000";
        
        if (min == null) {
            min = "2000";
        }
        if (max == null) {
            max = "3000";
        }

        // if (min != null) {
        //     min = "2";
        // }
        // if (max != null) {
        //     max = "3000";
        // }
        double maxv = Double.parseDouble(max);
         double minv = Double.parseDouble(min);

        if (lga == null) {
            html = html + "";
        } else if (!lga.equals("all")) {
            html = html + outputAllHLGA(outcome, status, gender, age, schoolyear, higher, labour, sort, lga,minv,maxv,limit);
        } else if (lga.equals("all")) {
            lga = "%%";
            html = html + outputAllHLGA(outcome, status, gender, age, schoolyear, higher, labour, sort, lga,minv,maxv,limit);
        }
        html = html
                + """
                                    </tr>
                                </thead>
                            </table>
                        </div>

                        <div>
                            <form  class='container' action='/L3P1.html' method='POST'>

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
                            <p>School Completion</p>

                                <select name='school_drop'>
                                <option value='any'>Any</option>
                                    <option value='no_school'>Didn't go to school</option>
                                    <option value='y8_below'>Completed Year 8</option>
                                    <option value='y10_equiv'>Completed Year 10</option>
                                    <option value='y12_equiv'>Completed Year 12</option>

                                </select>
                        </div>


                        <div>
                            <p>Age</p>

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
                        <p>Distance</p>
                            <select name='distance_drop'>
                            <option value='1000'> <1000 Km</option>
                            <option value='2000'><2000 Km</option>
                            <option value='3000'><3000 Km</option>
                            <option value='4000'><4000 Km</option>
                            <option value='5000'><5000 Km</option>
                            <option value='10000'><10000 Km</option>
                            <option value='20000'><20000 Km</option>
                            <option value='50000'><50000 Km</option>
                            </select>
                        </div>


                        <div>
                        <p><p/>
                        <br><button type='submit' style='width:100px;height:50px;'>Go!</button>
                    </div>
                </form>
                </div>

                <br>
                <br>

                <div class='TableFixHead'>
                <table style='width:600px;'>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Name</th>
                            <th>State</th>
                            <th>Type</th>
                            <th>Size (KM²)</th>
                            <th>Population</th>
                            <th>Distance (KM)</th>
                            <th>Matched</th>
                        </tr>
                       <tr> """;

        String outcome2 = context.formParam("outcome_drop");
        String gender2 = context.formParam("gender_drop");
        String status2 = context.formParam("indig_drop");
        String lga2 = context.formParam("lga_drop");
        String schoolyear2 = context.formParam("school_drop");
        String age2 = context.formParam("age_drop");
        String higher2 = context.formParam("higher_drop");
        String labour2 = context.formParam("labour_drop");
        String distRadius = context.formParam("distance_drop");
        double range = 4000;

        // if (distRadius.equals("1000")){
        // range = 1000;
        // }
        // else if (distRadius.equals("2000")){
        // range = 2000;
        // }
        // else if (distRadius.equals("3000")){
        // range = 3000;
        // }
        // else if (distRadius.equals("4000")){
        // range = 4000;
        // }
        // else if (distRadius.equals("5000")){
        // range = 5000;
        // }
        // else if (distRadius.equals("10000")){
        // range = 10000;
        // }
        // else if (distRadius.equals("20000")){
        // range = 20000;
        // }
        // else if (distRadius.equals("50000")){
        // range = 50000;
        // }
        // else {
        // range = 100000;
        // }

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

        if (lga2 == null) {
            html = html + "";
        } else if (lga2.equals("all")) {
            html = html + "";
        } else {
            html = html + distanceFromLGA(outcome2, lga2, status2, gender2, age2, schoolyear2, higher2, labour2, range);
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

    public String outputSingleLGA(String table, String lga, String status, String sex, String age, String school,
            String higher, String labour, String sort) {
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
        ArrayList<String> statuses = jdbc.returnAllStatus(table, status, sex, category, sort);
        ArrayList<String> genders = jdbc.returnAllSexes(table, status, sex, category, sort);
        ArrayList<String> categories = jdbc.returnAllCategories(table, status, sex, category, sort);
        ArrayList<Integer> counts = jdbc.returnAllCounts(table, status, sex, category, sort);
        ArrayList<Double> proportions = jdbc.returnAllProportions(table, status, sex, category, sort);
        int i = 0;

        for (String sexes : genders) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + lga + "</td><td>" + statuses.get(i) + "</td><td>" + sexes + "</td><td>"
                    + categories.get(i) + "</td><td>" + counts.get(i) + "</td><td>"
                    + String.format("%.2f", proportions.get(i)) + "</td>";
            html = html + "</tr>"; // end of row
            ++i;
        }

        return html;
    }

    public String outputAllHLGA(String table, String status, String sex, String age, String school, String higher,
            String labour, String sort, String lga, Double min, Double max, String limit) {
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
        ArrayList<Object> Data = jdbc.returnAllHData(table, status, sex, category, sort, lga,min,max,limit);

        // Writes the data to the html table, when ALL LGAs are selected

        int i = 0;
        int j = 0;
        while (i < Data.size()) {
            html = html + "<tr>"; // new row
            while (j < 8) {
                if (((Object) Data.get(i)).getClass().getSimpleName().equals("String")) {
                    html = html + "<td>" + Data.get(i) + "</td>";
                } else {
                    html = html + "<td>" + String.format("%.2f", Data.get(i)) + "</td>";

                }
                ++i;
                ++j;
            }
            j = 0;
            html = html + "</tr>"; // end of row
        }

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

    public String distanceFromLGA(String table, String lga, String status, String sex, String age, String school,
            String higher, String labour, Double range) {
        ArrayList<Integer> lgaCounts = new ArrayList<Integer>();
        ArrayList<Integer> lgaPops = new ArrayList<Integer>();
        ArrayList<String> lgaNames = new ArrayList<String>();
        ArrayList<Double> lgaSizes = new ArrayList<Double>();
        ArrayList<String> lgaState = new ArrayList<String>();
        ArrayList<String> lgaTypes = new ArrayList<String>();
        JDBCConnection jdbc = new JDBCConnection();
        String html = "";

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

        ArrayList<Double> latLon = jdbc.findLatLon(lga);
        Double lat = latLon.get(0);
        Double lon = latLon.get(1);

        ArrayList<Integer> lgaList = jdbc.nearestLGACode(lat, lon, range);
        ArrayList<Integer> lgaDistances = jdbc.distanceFromLGA(lat, lon, range);
        // ArrayList<Integer> lgaSizes = jdbc.sizeFromLGA(lat, lon, range);
        // ArrayList<String> lgaNames = jdbc.nameFromLGA(lat, lon, range);

        int i = 0;
        while (i < lgaList.size()) {
            int count = jdbc.lgaCountUsingCode(table, lgaList.get(i), status, sex, category);
            int pop = jdbc.lgaPopUsingCode(lgaList.get(i));
            double size = jdbc.lgaSizeUsingCode(lgaList.get(i));
            String type = jdbc.lgaTypeUsingCode(lgaList.get(i));
            String state = jdbc.lgaStateUsingCode(lgaList.get(i));
            String name = jdbc.lgaNameUsingCode(lgaList.get(i));

            switch (type) {
            case "C":
                type = "City";
                break;
            case "A":
                type = "Areas";
                break;
            case "RC":
                type = "Rural City";
                break;
            case "B":
                type = "Borough";
                break;
            case "S":
                type = "Shire";
                break;
            case "T":
                type = "Town";
                break;
            case "R":
                type = "Regional Council";
                break;
            case "RegC":
                type = "Regional Councils";
                break;
            case "AC":
                type = "Aboriginal Councils";
                break;
            case "M":
                type = "Municipal Council";
                break;
            case "DC":
                type = "District Council";
                break;
            }

            lgaCounts.add(count);
            lgaPops.add(pop);
            lgaNames.add(name);
            lgaSizes.add(size);
            lgaState.add(state);
            lgaTypes.add(type);

        }

        i = 0;
        while (i < lgaList.size()) {
            html = html + "<tr>"; // new row
            html = html + "<td>" + lgaList.get(i) + "</td><td>" + lgaNames.get(i) + "</td><td>" + lgaTypes.get(i)
                    + "</td><td>" + lgaState.get(i) + "</td><td>" + lgaSizes.get(i) + "</td><td>" + lgaPops.get(i)
                    + "</td><td>" + lgaDistances.get(i) + "</td><td>" + lgaCounts.get(i) + "</td";
            html = html + "</tr>"; // end of row
            ++i;
        }
        return html;

    }

    public double toDouble(String number) {
        double num = Double.parseDouble(number);
        return num;
    }

}
