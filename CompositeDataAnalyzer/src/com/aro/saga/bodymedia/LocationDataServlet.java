package com.aro.saga.bodymedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LocationDataServlet
 */
@WebServlet("/LocationDataServlet")
public class LocationDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationDataServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// Read in the csv data
		Map<String, List<TimeSlice>> keyed_slices = new HashMap<String, List<TimeSlice>>(); 
		
		Map<String, List<TimeSlice>> checkins = new HashMap<String, List<TimeSlice>>(); 
		

		String filename = "/WEB-INF/data/locations.csv";
		ServletContext context = getServletContext();

		InputStream inp = context.getResourceAsStream(filename);
		if (inp != null) {
			InputStreamReader isr = new InputStreamReader(inp);

			BufferedReader reader = new BufferedReader(isr);

			String location_line = "";
			String headers = reader.readLine(); 
			
			while ((location_line = reader.readLine()) != null) {
				
				System.out.println(location_line); 
				String[] stuff = location_line.split(","); 
				
				String day = stuff[2].substring(0, 10); 
				
				List<TimeSlice> timeSlices; 
				
				if (keyed_slices.containsKey(day)){
					timeSlices = keyed_slices.get(day); 
				}else{
					timeSlices = new ArrayList<TimeSlice>(); 
					keyed_slices.put(day, timeSlices); 
				}
				
				if (stuff[7].equals("travel") || stuff[7].equals("private") || stuff[7].equals("noData")){
					timeSlices.add(new TimeSlice("Travel", stuff[2].substring(0, stuff[2].length()-3), 
							stuff[3].substring(0, stuff[3].length()-3))); 
				}
				else if (stuff[7].equals("checkin") ){
					/*
					if (!checkins.containsKey(stuff[8])){
						checkins.put(stuff[8], value)
					}
					checkins.add(new TimeSlice(stuff[8], stuff[2].substring(0, stuff[2].length()-3), 
							stuff[3].substring(0, stuff[3].length()-3))); */
				}
				else{
					timeSlices.add(new TimeSlice(stuff[8], stuff[2].substring(0, stuff[2].length()-3), 
							stuff[3].substring(0, stuff[3].length()-3))); 
			
				}
			}
			
			
			
		}
				
		String day_of_interest = "2013-04-13"; 
		List<TimeSlice> daySlices = keyed_slices.get(day_of_interest); 
		
		// Create some data structure to hold the data
		
		// Store in a list
		
		// Sort the list (for fun!) 
		Collections.sort(daySlices, new Comparator<TimeSlice>(){
			 public int compare(TimeSlice o1, TimeSlice o2) 
             {
                 return (o1.getStartTime().compareTo(o2.getStartTime()));
             }
		});
		
		
		// First step for a chart: a bar chart showing the amount of time spent in a location over time. 
		
		StringBuffer buffer = new StringBuffer(); 
		StringBuffer labelBuffer = new StringBuffer(); 
		
		for (Iterator iterator = daySlices.iterator(); iterator.hasNext();) {
			TimeSlice timeSlice = (TimeSlice) iterator.next();
			buffer.append(timeSlice.getDurationInSeconds()); 
			labelBuffer.append(timeSlice.getPlaceName());
			
			if (iterator.hasNext()){
				buffer.append(", ");
				labelBuffer.append(", "); 
			}
		}
		
		// Return back: Series data: labels + data
		// Data is number of seconds
		response.setContentType("text/html");
		response.getWriter().write(buffer.toString()); 
		response.getWriter().write("||"); 
		response.getWriter().write(labelBuffer.toString()); 

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
