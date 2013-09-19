package com.aro.saga.bodymedia;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap; 

import javax.servlet.ServletContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AllStepData {

	private Map<String, List<Integer>> stepsPerHourMap = new HashMap<String, List<Integer>>(); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param whichDay day formatted in a string as "20130401"
	 * @return A list, where each element represents an hour of the day, and the value is the number of steps taken during that hour. 
	 */
	public List<Integer> getStepDataForDay(String whichDay){
		return stepsPerHourMap.get(whichDay); 
	}
	
	public void loadStepData(ServletContext context) {
		ObjectMapper mapper = new ObjectMapper();


		try {

			String filename = "/WEB-INF/data/step_v2.json";

			InputStream inp = context.getResourceAsStream(filename);
			if (inp != null) {
				InputStreamReader isr = new InputStreamReader(inp);

				Object stepData = mapper.readValue(isr, Object.class);
	
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
						//System.out.println(totalStepCount); 
						
						hourlyStepCount.add(hourMap.get("totalSteps")); 
					}
					
					stepsPerHourMap.put(whichDay, hourlyStepCount); 
					
				}
				

				// response.getWriter().write("273, 114, 154, 232, 34, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17");
				//response.getWriter().write(output.toString());

				// Object hours = ((Map)days.get("date"));

				isr.close();
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
	}

}
