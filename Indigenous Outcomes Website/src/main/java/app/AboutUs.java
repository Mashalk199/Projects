package app;

import java.util.ArrayList;

import io.javalin.http.Context;
import io.javalin.http.Handler;

/**
 * Temporary HTML as an example page.
 * 
 * Based on the Project Workshop code examples.
 * This page currently:
 *  - Provides a link back to the index page
 *  - Displays the list of movies from the Movies Database using the JDBCConnection
 *
 * @author Timothy Wiley, 2021. email: timothy.wiley@rmit.edu.au
 * @author Santha Sumanasekara, 2021. email: santha.sumanasekara@rmit.edu.au
 */
public class AboutUs implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/AboutUs.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "";
        html = html + """
        <!DOCTYPE html>
<html>
    <head>
        <!-- Include the external css file -->
        <link rel='stylesheet' href='style.css'>
        <style>
            body {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
            }
    
            ul {
                margin-left: 0px;
                width: 100%;
            }
    
            h2#green {
                background-color: rgb(0, 255, 0);
                box-sizing: border-box;
                height: 33px;
                width: 700px;
                padding: 0px;
                margin-left: 0px;
                bottom: 40px;
                float: left;
                transform: skew(10deg);
                box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.1);
            }
            h2#blue{
                background-color: rgb(0, 140, 255);
                box-sizing: border-box;
                height: 33px;
                width: 700px;
                padding: 0px;
                margin-right: 0px; 
                bottom: 40px;
                float: right;
                transform: skew(10deg);
                box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.1);

            }
            img#background{
                position: absolute;
                top: 0px;
                right: 0px;
                width:700px;
                height:650px;
                clip-path: polygon(100% 0, 0 0, 100% 100%);
            }

            p {
                position:relative;
                left:80px;
                clear:left;
                text-align: left;
                width:50%;
            }

            p#alignright {
                position:relative;
                right:80px;
                text-align:right;
                padding-left:35%;
            }

            p#bottomright{
                bottom:0;
                right:0;
                position:fixed;
                clear:right;
                padding-left:67%;

            }
            ul#outcome li {
            box-sizing: border-box;
            padding: 0px;
            margin-left: 80px;
            text-align: left;
        }
        </style>
    </head>

    <body>
        <img id='background' src='lizard-background.jpg' width='100%'>
        
        <h1 id='logo'>the bridge</h1>
        <nav role='navigation' aria-label='main menu'>
        <ul class='navbar'>
       
            <li style='color:rgba(0, 255, 0, 0.884);'> <a href='/'>Home</a></li>
        
        <li><a href='L1P2.html'>What you need to know</a></li>
        <li><a href='L2P1.html'>Glance at whats happening</a></li>
        <li><a href='L3P1.html'>Deep dive</a></li>
        <a href='AboutUs.html'><li style='color:rgba(0, 255, 0);'>About us</li></a>
    </ul>
        </nav>

        <h2 class='subheading' id='green'>Who are we?</h2>
            <br>
            <br>
            <br>
            <br>
            <br>
            <br>
            <br>
            <br>
            
            <div>
                <p><b>We are</b> computer science students at RMIT tasked with 'addressing a social challenge'. On this website we provide in depth information on the National Agreement on Closing the Gap (National Agreement).
                </p>
                <p><b>We help</b> governments, Indigenous leaders and the general public track the progress being made towards closing the gap for Aboriginal and Torres Strait Islander people</p>
            
                <p><b>We aim</b> to present insightful information on comprehensive and insightful information on the progress towards tracking the following subset of the 17 socioeconomic outcomes of the Closing the Gap agreement:
                    <ul id='outcome'>
                        <li>Outcome 1: Aboriginal and Torres Strait Islander people enjoy long and healthy lives.</li>
                        <li>Outcome 5: Aboriginal and Torres Strait Islander students achieve their full learning potential.</li>
                        <li>Outcome 6: Aboriginal and Torres Strait Islander students reach their full potential through further education pathways.</li>
                        <li>Outcome 8: Strong economic participation and development of Aboriginal and Torres Strait Islander people and communities.</li>
                    </ul>
                </p>
                <p><b>We hope</b> to engage the Australian people by providing refined and extensive information on the challenges faced by Aboriginal and Torres Strait Islander people. By allowing people to effortlessly comprehend the obstacles affecting the Indigenous community and giving them tools to help them better understand the National Agreement and the targets its set to meet.
                </p>
            </div>
        
        <h2 class='subheading' id=blue>What is the national argreement?</h2>
            <br>
            <br>
            <br>
            <br>
            <br>
            <br>
            <div>
                <p id='alignright'>The National Agreement on Closing the gap (National Agreement) is an acknowledgement of the ongoing struggle of Aboriginal and Torres Strait Islander people. It is an effort for governments to work with Aboriginal and Torres Strait Islander people in order to overcome inequality and achieve life outcomes equal to all Australians. The agreement seeks to achieve outcomes in the following areas: 
                education, employment, health and wellbeing, justice, safety, housing, land and waters, and languages. To improve in these areas, the agreement consists of 17 specific and measurable goals to monitor and demonstrate how progress is being made to achieve these outcomes.</p>
            </div>
            
            <p id='bottomright'>This website design was created by Mashal Khan (S3906303) and Mussab Syed (S3846092)</p>

    </body>
        """;


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
