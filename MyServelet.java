import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;





@WebServlet("/MyServelet")
public class MyServelet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
//		response.getWriter().append("Served at:").append(request.getContextPath());
		response.sendRedirect("index.html");
	}

	


	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		String inputData  = request.getParameter("CityName");
		
		String apiKey = "db1e4c72cae3e6862eb660db88e1d5df";
		
		String city = request.getParameter("city");
		
		String apiUrl =  "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

		
		
		   try {
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	                InputStream inputStream = connection.getInputStream();
	                InputStreamReader reader = new InputStreamReader(inputStream);
	               // System.out.println(reader);
	                
	                Scanner scanner = new Scanner(reader);
	                StringBuilder responseContent = new StringBuilder();

	                while (scanner.hasNext()) {
	                    responseContent.append(scanner.nextLine());
	                }
	                
	               // System.out.println(responseContent);
	                scanner.close();
	                
	                // Parse the JSON response to extract temperature, date, and humidity
	                Gson gson = new Gson();
	                JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
        
       //out.println(jsonObject);

      //Date & Time
        long dateTimestamp = ((com.google.gson.JsonObject) jsonObject).get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();
        
        //Temperature
        double temperatureKelvin = ((com.google.gson.JsonObject) jsonObject).getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
       
        //Humidity
        int humidity = ((com.google.gson.JsonObject) jsonObject).getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = ((com.google.gson.JsonObject) jsonObject).getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = ((com.google.gson.JsonObject) jsonObject).getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
        
        // Set the data as request attributes (for sending to the jsp page)
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responseContent.toString());
        
        connection.disconnect();
		   } catch (IOException e) {
	            e.printStackTrace();
	        }
        
     // Forward the request to the weather.jsp page for rendering
        // Pass the response content to the JSP page
        
        request.getRequestDispatcher("/index2.jsp").forward(request, response);
	}
	
}
