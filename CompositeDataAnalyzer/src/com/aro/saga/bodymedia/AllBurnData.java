package com.aro.saga.bodymedia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap; 

import javax.servlet.ServletContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AllBurnData {

	//private Map<String, List<Integer>> stepsPerHourMap = new HashMap<String, List<Integer>>(); 
	
	private Map<String, List<Double>> burnPerMinuteMap = new HashMap<String, List<Double>>();
	
	private static final String dateFormat = "yyyyMMdd";
	private static final SimpleDateFormat dateFormatter= new SimpleDateFormat(dateFormat);
	
	private BodyMediaDataGrabber dataGrabber = BodyMediaDataGrabber.getSingleton();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param whichDay day formatted in a string as "20130401"
	 * @return A list, where each element represents an minute of the day, and the value is the number of calories burned during that hour. 
	 */
	public List<Double> getBurnDataForDay(String whichDay){
		return burnPerMinuteMap.get(whichDay); 
	}
	
	/**
	 * 
	 * @param whichDay
	 * @param startHr 0-23, representing hour of the day
	 * @param startMin 0-59, representing the minute of the hour
	 * @param endHr
	 * @param endMin
	 * @return
	 */
	public double getBurnTotalForDayHrMin(String startDay, int startHr, int startMin, String endDay, int endHr, int endMin){
		if (startDay.equals(endDay)){
			double sum = 0; 
			
			if (!burnPerMinuteMap.containsKey(startDay)){
				grabAndSaveData(startDay); 
			}
			List<Double> dayMins = burnPerMinuteMap.get(startDay); 
			
			int startMinOverall = (startHr * 60) + startMin; 
			int endMinOverall = (endHr * 60) + endMin; 
			
			for (int i=startMinOverall; i<= endMinOverall; i++){
				if (dayMins.size() > i){
					sum += dayMins.get(i).intValue();
				}else{
					System.out.println("No data? : " + startDay + " " +startHr + ":" + startMin + " - " + "endDay "); 
				}
			}
			
			
			return sum; 
		}else{
			// Figure out the startDay and the nextDay, and continue until nextDay is the same as endDay. 
			try {
				Calendar start = Calendar.getInstance(); 
				start.setTime(dateFormatter.parse(startDay));
				
				Calendar end = Calendar.getInstance(); 
				end.setTime(dateFormatter.parse(endDay)); 
				double sum = 0; 
				
				Calendar next = Calendar.getInstance(); 
				next.setTime(start.getTime()); 
				next.add(Calendar.DAY_OF_MONTH, 1);
				
				int newEndHr = 23; 
				int newEndMin = 59; 
				
				if (next.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)){
					newEndHr = endHr; 
					newEndMin = endMin; 
				}

				// If the next day is the same as the end day, then use endHr/endMin. 
				// If the next day is not the same as the end day, then use 23:59. 
				double firstDayCals = getBurnTotalForDayHrMin(dateFormatter.format(start.getTime()), 
						startHr, startMin, 
						dateFormatter.format(start.getTime()), newEndHr, newEndMin);
				double restOfCals = getBurnTotalForDayHrMin(dateFormatter.format(next.getTime()), 0, 0, 
						dateFormatter.format(end.getTime()), endHr, endMin); 
				
				return firstDayCals + restOfCals; 
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1.0; 
			} 
		}
	}
	
	@SuppressWarnings("unchecked")
	private void grabAndSaveData(String whichDay) {
		
		String burnDayJson = dataGrabber.getDayBurnJson(whichDay); 
		
		ObjectMapper mapper = new ObjectMapper();


		try {  

			//	HashMap<String, Object> rawData= (HashMap<String, Object>) mapper.readValue(burnDayJson, Object.class); //isr, Object.class);
			//	List<Object> allDays = (List<Object>) rawData.get("allDays");
			
			HashMap<String, Object> burnData= (HashMap<String, Object>) mapper.readValue(burnDayJson, Object.class); //isr, Object.class);
				
			//	for (int i=0; i<allDays.size(); i++){
			//		Map<String, Object> burnData = (HashMap<String, Object>)allDays.get(i); 
					
					Object startDate = burnData.get("startDate"); 
					ArrayList<Object> days = (ArrayList<Object>) burnData.get("days"); 
					
					HashMap dayStuff = (HashMap) days.get(0);
					
					String date = (String) dayStuff.get("date"); 
					
					List<Double> calMinsToSave = new ArrayList<Double>(); 
					
					List<HashMap> minutes = (List<HashMap>) dayStuff.get("minutes"); 
					
					for (Iterator iterator = minutes.iterator(); iterator.hasNext();) {
						HashMap hashMap = (HashMap) iterator.next();
						String source = (String) hashMap.get("source"); 
						Double calsBurned = (Double) hashMap.get("cals"); 
						calMinsToSave.add(calsBurned); 
					}
					
					burnPerMinuteMap.put(date, calMinsToSave);
				//}
				//System.out.println(burnData.size()); 
	/*
				ArrayList<Map> days = ((Map<String, ArrayList>) stepData).get("days");

				for (Iterator iterator = days.iterator(); iterator.hasNext();) {
					Map day1 = (Map) iterator.next();
					// Get the first date (list)
					//Map day1 = (Map) (( days).get(0));

					String whichDay = (String) day1.get("date");
					List<Integer> hourlyStepCount = new ArrayList<Integer>(); 

					ArrayList<Map<String, Integer>> hours = (ArrayList<Map<String, Integer>>) day1.get("hours");

					
					for (Map<String, Integer> hourMap : hours) {
						Integer totalStepCount = hourMap.get("totalSteps"); 
						System.out.println(totalStepCount); 
						
						hourlyStepCount.add(hourMap.get("totalSteps")); 
					}
					
					stepsPerHourMap.put(whichDay, hourlyStepCount); 
					
				}
				

				// response.getWriter().write("273, 114, 154, 232, 34, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17");
				//response.getWriter().write(output.toString());

				// Object hours = ((Map)days.get("date"));
*/
			
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
	}

}
