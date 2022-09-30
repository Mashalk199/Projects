package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Example Index HTML class using Javalin
 * <p>
 * Generate a static HTML page using Javalin
 * by writing the raw HTML into a Java String object
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class Index implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "";
        html = html + """
        <!DOCTYPE html>
<html>

<head>
    <link rel='stylesheet' type='text/css' href='style.css'>
    <style>
        img.background {
            position: absolute;
            left: 500px;
            -webkit-transform: rotate(120deg);
            -moz-transform: rotate(120deg);
            -ms-transform: rotate(120deg);
            -o-transform: rotate(120deg);
            transform: rotate(120deg);
            object-fit: cover;
        }
        
        h2 {
            float: right;
            position: relative;
            right: 100px;
            font-family: 'Courier New';
            font-weight: bold;
            top: 20px;
            padding-right: 0px;
            margin-right: 0px;
            color: white;
        }
        
        ul.facts {
            position: relative;
            top: 0px;
        }
        
        h4 {
            text-align: center;
            position: relative;
            top: 40px;
            padding-right: 0px;
            margin-right: 0px;
            color: rgba(0, 255, 55, 0.834);
        }
        
        #extra li {
            text-align: center;
            float: left;
            list-style-type: none;
            margin-left: 40px;
        }
    </style>
</head>

<body>
    <img class='background' src='steve-johnson-BgDoX0zk4zw-unsplash.jpg' width='100%'>
    <h1 id='logo'>the bridge</h1>
    <nav role='navigation' aria-label='main menu'>
        <ul class='navbar'>
            <a href='/'>
                <li style='color:rgba(0, 255, 0, 0.884);'>Home</li>
            </a>
            <li><a href='L1P2.html'>What you need to know</a></li>
            <li><a href='L2P1.html'>Glance at whats happening</a></li>
            <li><a href='L3P1.html'>Deep dive</a></li>
            <li><a href='AboutUs.html'>About us</a></li>
        </ul>
    </nav>
    <p style='clear:left;margin-bottom:0px;padding-bottom:0px;position:relative;left:60px;font-size:larger;padding-right: 0px;margin-right: 0px;'>
        Hi! did you know...</p>
    <h2>What is 'Closing the gap'?</h2>
    <p style='clear:right;float:right;position:relative;top:10px;right:140px;width:270px;margin-right:
        0px;padding-right: 0px;font-size:small;color: white;'>Closing the gap is an initiative from all Australian governments and all Aboriginal and Torres Strait Islander representatives to change the way policies and programs that affect Aboriginal and Torres Strait Islander people are implemented</p>
    <ul class='facts'>
        <li style='background-color: rgb(0, 225, 255);'>Indigenous Australians face an unemployment rate <strong>2.8</strong> times higher than non-Indigenous Australians. </li>
        <li style='position:relative; left:130px; background-color: rgb(0, 255, 55);'>Less than <strong>0.6%</strong> of all AQF-7 bachelors degrees are earned by Indigenous Australians.</li>
        <li style='position:relative; left:260px; background-color: rgb(246, 255, 0);'>123,507 out of 650,000 Indigenous Australians completed year 12. Compared to 9.62 million non-Indigenous Australians.</li>

    </ul>
    <div>
        <h4>More resources</h4>
        <ul id='extra' style='position:relative; left:360px;top:40px;padding-right: 0px;margin-right: 0px;'>
            <li><a href='https://www.closingthegap.gov.au/national-agreement/national-agreement-closing-the-gap'>Government Information</a></li>
            <li><a href='https://coalitionofpeaks.org.au'>Coalition of Peaks</a></li>
        </ul>
    </div>

</body>

</html>
        """;


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
