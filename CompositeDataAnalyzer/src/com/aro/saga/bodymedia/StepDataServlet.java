package com.aro.saga.bodymedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class StepDataServlet
 */
@WebServlet(description = "Reads and processes BodyMedia step data.", urlPatterns = { "/StepDataServlet" })
public class StepDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StepDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		response.setContentType("text/html");
		
		// TODO: add whichDay parameter. 
		// TODO: How to combine into location. 
		
		try {

			
			
			  String filename = "/WEB-INF/data/step_v2.json";
			  ServletContext context = getServletContext();
			  
			  
			  
			  InputStream inp = context.getResourceAsStream(filename);
			  if (inp != null) {
				  InputStreamReader isr = new InputStreamReader(inp);
				  
				  Object stepData = mapper.readValue(isr, Object.class);
				  /*
				  BufferedReader reader = new BufferedReader(isr);
				  
				  
				  PrintWriter pw = response.getWriter();
				  pw.println();
				  String step_json = "";
				  while ((step_json = reader.readLine()) != null) {
					  pw.println("<h2><i><b>"+step_json+"</b></i></b><br>");
					  }
					  }*/
					  
			/*
			String propPath = getServletContext().getResource("/WEB-INF/myPropFolder/myProp.props").toString();
			/* I have to cut the 6 first characterrs which are "file:/"*/
			/* and I don't know why these ccharacteres are here 
			propPath = propPath.substring(6,propPath.length());
			Properties props = new Properties();
			prop.load(new FileInputStream(path));
			*/
				  
				  
			Object days = ((Map)stepData).get("days"); 
			
			// Get the first date (list)
			Map day1 = (Map)(((ArrayList)days).get(0)); 
			
			String whichDay = (String)day1.get("date");
			
			ArrayList<Map> hours = (ArrayList<Map>)day1.get("hours"); 
			
			StringBuffer output = new StringBuffer(); 
			
			for (Map hourMap : hours) {
				output.append(hourMap.get("totalSteps")); 
				output.append(", "); 
			}
			
			//response.getWriter().write("273, 114, 154, 232, 34, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17"); 
			response.getWriter().write(output.toString()); 
			
			
			//Object hours = ((Map)days.get("date")); 
			
			System.out.println(stepData); 
			  }
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		response.getWriter().flush(); 
		response.getWriter().close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
