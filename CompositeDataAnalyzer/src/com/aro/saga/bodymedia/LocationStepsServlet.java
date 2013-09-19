package com.aro.saga.bodymedia;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.util.calendar.CalendarUtils;

/**
 * Servlet implementation class LocationStepsServlet
 */
@WebServlet("/LocationStepsServlet")
public class LocationStepsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static final String dateFormat = "h:mm a"; 
	
	private static final SimpleDateFormat dateDisplayFormatter = new SimpleDateFormat(dateFormat); 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationStepsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dateParameter = request.getParameter("date"); 
		
		// Load Location slices
		AllLocationData locationSlices = AllLocationData.getSingleton(getServletContext());//new AllLocationData(); 
		//locationSlices.loadData(getServletContext()); 
		
		// Load step slices
		AllStepData stepData = new AllStepData(); 
		stepData.loadStepData(getServletContext());
		
		// 
		List<TimeSlice> slices = locationSlices.getDailySlices(dateParameter); // "2013-04-12");
		List<Integer> stepHours = stepData.getStepDataForDay(dateParameter.replace("-", "")); //"20130412");
		
		StringBuffer locations = new StringBuffer(); 
		StringBuffer stepValues = new StringBuffer(); 
		
		for (Iterator iterator = slices.iterator(); iterator.hasNext();) {
			TimeSlice timeSlice = (TimeSlice) iterator.next();
			//buffer.append(timeSlice.getDurationInSeconds()); 
			locations.append(timeSlice.getPlaceName());
			
			locations.append( "<BR>(");
			
			// Count steps in this location
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(timeSlice.getStartTime()); 
			int firstHour =  cal.get(Calendar.HOUR_OF_DAY);
			locations.append(dateDisplayFormatter.format(timeSlice.getStartTime()));//firstHour); 
			locations.append("-");
			cal.setTime(timeSlice.getEndTime()); 
			
			int lastHour = cal.get(Calendar.HOUR_OF_DAY); 
			locations.append(dateDisplayFormatter.format(timeSlice.getEndTime())); //lastHour); 
			locations.append(")"); 
			
			int totalSteps = 0; 
			
			for (int i=firstHour; i<lastHour; i++){
				totalSteps += stepHours.get(i).intValue(); 
			}
			
			stepValues.append(totalSteps); 
			
			if (iterator.hasNext()){
				locations.append(", "); 
				stepValues.append(", "); 
			}
		}
		/*
		
		StringBuffer steps = new StringBuffer(); 
		
		for (Iterator iterator = stepHours.iterator(); iterator.hasNext();) {
			Integer stepHour = (Integer) iterator.next();
			//buffer.append(timeSlice.getDurationInSeconds()); 
			steps.append(stepHour.toString());
			
			if (iterator.hasNext()){
				steps.append(", "); 
			}
		}
		*/
		response.setContentType("text/html");
		//response.getWriter().write("Step values: "); 
		//response.getWriter().write("||"); 
		response.getWriter().write(stepValues.toString()); 

		//response.getWriter().write("Labels: "); 
		response.getWriter().write("||"); 
		response.getWriter().write(locations.toString()); 
		
		
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
