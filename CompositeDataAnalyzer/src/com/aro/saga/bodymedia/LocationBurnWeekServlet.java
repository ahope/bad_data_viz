package com.aro.saga.bodymedia;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LocationBurnWeekServlet
 */
@WebServlet("/LocationBurnWeekServlet")
public class LocationBurnWeekServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String dateFormat = "yyyyMMdd";
	private static final SimpleDateFormat burnDateFormatter= new SimpleDateFormat(dateFormat);
	
	private static final String displayDateFormat = "h:mm a"; 
	
	private static final SimpleDateFormat dateDisplayFormatter = new SimpleDateFormat(displayDateFormat); 
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationBurnWeekServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dateParameter = request.getParameter("date"); 
		if (dateParameter == null || dateParameter.length()==0){
			dateParameter = "2013-04-13";
		}
			
			// Load Location slices
					AllLocationData locationSlices = AllLocationData.getSingleton(getServletContext()); //new AllLocationData(); 
					//locationSlices.loadData(getServletContext()); 
					
			AllBurnData burnData = new AllBurnData(); 
			//burnData.loadData(getServletContext());
			
			List<TimeSlice> locations = locationSlices.getWeeklySlices(dateParameter);
			
			Map<String, List<Double>> placeBurnSeries = new HashMap<String, List<Double>>(); 
			
			int numEntries = 0; 
			
			// Go through all of the locations. 
			// If it's a new location, create a new series. 
			// 		Add enough 0s to make it the same length as the previous series
			// For each location, add a 0 or the number of calories. 
			for (TimeSlice timeSlice : locations) {
				if (!placeBurnSeries.containsKey(timeSlice.getPlaceName())){
					List<Double> newLocationSeries = new ArrayList<Double>(); 
					for (int i=0; i<numEntries; i++){
						newLocationSeries.add(0.0); 
					}
					placeBurnSeries.put(timeSlice.getPlaceName(), newLocationSeries); 
				}
				for (Iterator<String> iterator = placeBurnSeries.keySet().iterator(); iterator
						.hasNext();) {
					
					String placeName = (String) iterator.next();
					List<Double> burnVals = placeBurnSeries.get(placeName); 
					if (placeName.equals(timeSlice.getPlaceName())){
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
						burnVals.add(cals); 
					}
					else{
						burnVals.add(0.0); 
					}
				}	
				numEntries++;
			}
			
			
		
			// Create strings to output the data to the response. 
			
			StringBuffer labels = new StringBuffer(); 
			
			StringBuffer allData = new StringBuffer(); 
			
			StringBuffer dayPlotBand = new StringBuffer(); 
			
			int whichDay = 1; 
			int whichSlice = 0; 
			
			Calendar curDay = null;
			
			Calendar newSlice = Calendar.getInstance(); 
			
			for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
				TimeSlice slice = (TimeSlice) iterator.next();
				labels.append(slice.getPlaceName()); //placeName); 
				
				// need a newline here? 
				if (iterator.hasNext()){
					labels.append(", "); 
				}
				
				// Check for plot band stuff
				if (curDay == null){
					curDay = Calendar.getInstance(); 
					curDay.setTime(slice.getStartTime()); 
					dayPlotBand.append("Day "); 
					dayPlotBand.append(whichDay); 
					dayPlotBand.append(","); 
					dayPlotBand.append(whichSlice);
					dayPlotBand.append(","); 
				}
				
				newSlice.setTime(slice.getEndTime()); 
				
				if (newSlice.get(Calendar.DATE) > curDay.get(Calendar.DATE)){
					// Finish the current band
					//dayPlotBand.append(","); 
					dayPlotBand.append(whichSlice); 
					dayPlotBand.append(";"); 
					
					curDay.setTime(slice.getEndTime()); 
					whichDay++; 
					
					if (iterator.hasNext()){
						dayPlotBand.append("Day "); 
						dayPlotBand.append(whichDay); 
						dayPlotBand.append(","); 
						dayPlotBand.append(whichSlice);
						dayPlotBand.append(","); 
					}
				}
				
				whichSlice++; 
				
			}
			
			//System.out.println(dayPlotBand.toString()); 
			
			for (Iterator<String> iterator = placeBurnSeries.keySet().iterator(); iterator.hasNext();) {
				String placeName = (String) iterator.next();
				
				allData.append(placeName); 
				allData.append(", "); 
				List<Double> vals = placeBurnSeries.get(placeName); 
				for(int i=0; i<vals.size(); i++){
					allData.append(vals.get(i)); 
					if (i<vals.size()-1){
						allData.append(", "); 
					}
				}
				allData.append("||"); 
			}
			
			
	/*		//StringBuffer burnValues = new StringBuffer(); 
			//StringBuffer locationLabels = new StringBuffer(); 
			
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
	*/		
			response.setContentType("text/html");
			
			// Write the date
			response.getWriter().write(dateParameter);
			
			// Seperator
			response.getWriter().write("||"); 
			
			// Write out plot bands
			response.getWriter().write(dayPlotBand.toString());//"day 1, -0.5, 9; day 2, 9, 24; day 3, 24, 30.5");
			
			// Seperator
			response.getWriter().write("||"); 
			
			// Write out labels
			response.getWriter().write(labels.toString()); 

			// Seperator
			response.getWriter().write("||"); 
			
			// Write out the data
			response.getWriter().write(allData.toString()); 
			
			
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
