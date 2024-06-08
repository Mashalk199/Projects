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
public class L1P2 implements Handler {

    // URL of this page relative to http://localhost:7000/
    public static final String URL = "/L1P2.html";

    @Override
    public void handle(Context context) throws Exception {
        // Create a simple HTML webpage in a String
        String html = "";
        html = html + """
        <html>
        
        <head>
            <link rel=\"stylesheet\" href=\"style.css\">
            <style>
                body {
                    position: absolute;
                    top: 0px;
                    left: 0px;
                    width: 100%;
                    padding: 0px;
                    margin: 0px;
                    height:100%;}
        
                h3 {
                    float: right;
                    position: relative;
                    right: 100px;}
                h2 {
                    font-family: 'Courier New';
                    font-weight: bold;}
                ul {
                    margin-left: 0px;
                    width: 100%;
                    padding: 0px;}
        
                ul#outcome li {
                    background-color: rgb(0, 225, 255);
                    box-sizing: border-box;
                    height: 70px;
                    width: 1000px;
                    padding: 0px;
                    margin-left: 0px;
                    bottom: 0px;
                    float: left;
                    margin-bottom: 30px;
                    text-align: center;
                    display: flex;
                    justify-content: center;
                    align-items: center;}
                    z-index:0;
                    
            </style>
            </head>
        
            <body> <img class='background' src='steve-johnson-BgDoX0zk4zw-unsplash.jpg' width='100%'>
    <h1 id='logo' style='left:58px;'>the bridge</h1>
    <nav role='navigation' aria-label=\"main menu\">
        <ul class='navbar' style='position:relative;left:48px;'>
            <li><a href='/'>Home</a></li>
            <a href='L1P2.html'>
                <li style='color:rgba(0, 255, 0, 0.884);'>What you need to know</li>
            </a>
            <li><a href='L2P1.html'>Glance at whats happening</a></li>
            <li><a href='L3P1.html'>Deep dive</a></li>
            <li><a href='AboutUs.html'>About us</a></li>
        </ul>
    </nav>
    <h2 class='subheading'>Know what else is crazy?</h1>
        <ul class='facts' id='outcome'>
            <li style='position:relative;text-align: center;padding:25px;background-color: rgb(26, 255, 0);'>Only 8% of Indigenous Australians are older than 60, compared to 20.7% non-Indigenous Australians</li>
            <li style='position:relative;float:right;'>Indigenous Australians are  1.6x  more likely to not have went to school than non-Indigenous Australians</li>
            <li style='position:relative;background-color: rgb(26, 255, 0);'>Indigenous Australians earn 8 times less postgraduate certifications than non-indigenous Australians</li>
            <li style='position:relative;float:right;'>Indigenous Australians face an extremely high unemployment rate of 22%</li>

        </ul>
</body>
            </html>
            """;


        // DO NOT MODIFY THIS
        // Makes Javalin render the webpage
        context.html(html);
    }

}
