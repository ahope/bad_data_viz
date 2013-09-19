package com.aro.saga.bodymedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class LocationBurnRateServlet
 */
@WebServlet("/LocationBurnRateServlet")
public class LocationBurnRateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private static final String dateFormat = "yyyyMMdd";
	private static final SimpleDateFormat burnDateFormatter= new SimpleDateFormat(dateFormat);
	
	private static final String displayDateFormat = "h:mm a"; 
	
	private static final SimpleDateFormat dateDisplayFormatter = new SimpleDateFormat(displayDateFormat); 
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationBurnRateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dateParameter = request.getParameter("date"); 
	//	dateParameter = "2013-04-13";
		
		// Load Location slices
				AllLocationData locationSlices = AllLocationData.getSingleton(getServletContext()); //new AllLocationData(); 
				//locationSlices.loadData(getServletContext()); 
				
		AllBurnData burnData = new AllBurnData(); 
		//burnData.loadData(getServletContext());
		
		List<TimeSlice> locations = locationSlices.getDailySlices(dateParameter);
		StringBuffer burnValues = new StringBuffer(); 
		StringBuffer locationLabels = new StringBuffer(); 
		
		for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
			TimeSlice timeSlice = (TimeSlice) iterator.next();
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(timeSlice.getStartTime());
			String startDay = burnDateFormatter.format(cal.getTime()); 
			
			int startHr = cal.get(Calendar.HOUR_OF_DAY); 
			int startMin = cal.get(Calendar.MINUTE); 
			
			cal.setTime(timeSlice.getEndTime()); 
			String endDay = burnDateFormatter.format(cal.getTime()); 
			
			int endHr = cal.get(Calendar.HOUR_OF_DAY); 
			int endMin = cal.get(Calendar.MINUTE); 
			
			double cals = burnData.getBurnTotalForDayHrMin(startDay, startHr, startMin, 
					endDay, endHr, endMin); 
			burnValues.append(cals);
			locationLabels.append(timeSlice.getPlaceName()); 
			
			
			// Count steps in this location
			//Calendar cal = Calendar.getInstance();
			
			cal.setTime(timeSlice.getStartTime()); 
			locationLabels.append(" ("); 
			int firstHour =  cal.get(Calendar.HOUR_OF_DAY);
			locationLabels.append(dateDisplayFormatter.format(timeSlice.getStartTime()));//firstHour); 
			locationLabels.append("-");
			cal.setTime(timeSlice.getEndTime()); 
			
			int lastHour = cal.get(Calendar.HOUR_OF_DAY); 
			locationLabels.append(dateDisplayFormatter.format(timeSlice.getEndTime())); //lastHour); 
			locationLabels.append(")"); 
			
			if (iterator.hasNext()){
				burnValues.append(", ");
				locationLabels.append(", "); 
			}
		}
		
		response.setContentType("text/html");
		//response.getWriter().write("Step values: "); 
		//response.getWriter().write("||"); 
		response.getWriter().write(burnValues.toString()); 

		//response.getWriter().write("Labels: "); 
		response.getWriter().write("||"); 
		response.getWriter().write(locationLabels.toString()); 
		
		
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
