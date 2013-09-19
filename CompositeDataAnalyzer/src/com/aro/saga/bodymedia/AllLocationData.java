package com.aro.saga.bodymedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;

public class AllLocationData {

	private static final String DATE_IN_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 2013-04-14 02:29:11.577+00 
	
	private static final String DATE_SAVE_FORMAT = "yyyy-MM-dd"; 
	
	private static DateFormat read_day_format = new SimpleDateFormat(DATE_IN_FORMAT); 
	private static DateFormat save_day_format = new SimpleDateFormat(DATE_SAVE_FORMAT); 
	private static DateFormat save_day_timeslice_format = new SimpleDateFormat(DATE_IN_FORMAT); 
	
	
	private static AllLocationData singleton; 
	
	public static AllLocationData getSingleton(ServletContext context){
		if (singleton == null){
			singleton = new AllLocationData();
			singleton.loadData(context); 
		}
		return singleton; 
	}
	
	private AllLocationData() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * A map between the date (key is string, formatted like "2013-04-13") and a list of all the TimeSlice objects for the given day. 
	 */
	private static Map<String, List<TimeSlice>> allSlices = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/***
	 * 
	 * @param whichDay day formatted as yyyy-mm-dd
	 * @return
	 */
	public List<TimeSlice> getDailySlices(String whichDay){
		return allSlices.get(whichDay); 
	}
	
	/***
	 * 
	 * @param startingDay day formatted as yyyy-mm-dd
	 * @return TimeSlices that represent 7 days starting with startingDay
	 */
	public List<TimeSlice> getWeeklySlices(String startingDay){
		List<TimeSlice> weekSlices = new ArrayList<TimeSlice>(); 
		try {
			
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(save_day_format.parse(startingDay));
			
			for (int i=0; i<7; i++){
				weekSlices.addAll(allSlices.get(save_day_format.format(cal.getTime()))); 
				cal.add(Calendar.DATE, 1); 
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return weekSlices; 
	}
	
	private void loadData(ServletContext context) {
		allSlices = new HashMap<String, List<TimeSlice>>();

		Map<String, List<TimeSlice>> checkins = new HashMap<String, List<TimeSlice>>();

		String filename = "/WEB-INF/data/locations.csv";

		InputStream inp = context.getResourceAsStream(filename);
		if (inp != null) {
			InputStreamReader isr = new InputStreamReader(inp);

			BufferedReader reader = new BufferedReader(isr);
			
			read_day_format.setTimeZone(TimeZone.getTimeZone("GMT")); 
			save_day_format.setTimeZone(TimeZone.getDefault());
			save_day_timeslice_format.setTimeZone(TimeZone.getDefault()); 

			String location_line = "";
			try {
				String headers = reader.readLine();

				while ((location_line = reader.readLine()) != null) {

				//	System.out.println(location_line);
					String[] stuff = location_line.split(",");

					String origDay = stuff[2].substring(0, 19);
				//	System.out.println("Orig text: " + stuff[2]); 
				//	System.out.println("Orig day: " + origDay); 
					String endDay = stuff[3].substring(0, 19); 
					
					Date localDay = read_day_format.parse(origDay); 
					Date localEndDay = read_day_format.parse(endDay);
					
				//	System.out.println("New day: " + save_day_timeslice_format.format(localDay)); 
					
					String day_key = save_day_format.format(localDay); 
					
					List<TimeSlice> timeSlices;

					if (allSlices.containsKey(day_key)) {
						timeSlices = allSlices.get(day_key);
					} else {
						timeSlices = new ArrayList<TimeSlice>();
						allSlices.put(day_key, timeSlices);
					}

					if (stuff[7].equals("travel") || stuff[7].equals("private")
							|| stuff[7].equals("noData")) {
						TimeSlice newSlice = new TimeSlice(stuff[7], 
								save_day_timeslice_format.format(localDay), //read_day_format.parse(stuff[2].substring(0, stuff[2].length() - 3))), 
								save_day_timeslice_format.format(localEndDay));
						newSlice.setPlaceType(stuff[7]);
						timeSlices.add(newSlice);//read_day_format.parse(stuff[3].substring(0, stuff[3].length() - 3)))));
						
					} else if (stuff[7].equals("checkin")) {
						/*
						 * if (!checkins.containsKey(stuff[8])){
						 * checkins.put(stuff[8], value) } checkins.add(new
						 * TimeSlice(stuff[8], stuff[2].substring(0,
						 * stuff[2].length()-3), stuff[3].substring(0,
						 * stuff[3].length()-3)));
						 */
					} else {
						TimeSlice newSlice = new TimeSlice(stuff[8], 
								save_day_timeslice_format.format(localDay), //read_day_format.parse(stuff[2].substring(0, stuff[2].length() - 3))), 
								save_day_timeslice_format.format(localEndDay)); 
						
						// TODO: Fix the hack
						if (stuff[8].equals("A.R.O.")){
							newSlice.setPlaceType("Work"); 
							
						}else if(stuff[8].equals("Robot Rookery")){
							newSlice.setPlaceType("Home"); 
							
						}else{
							newSlice.setPlaceType(stuff[10]); 
						}
						
						timeSlices.add(newSlice); //read_day_format.parse(stuff[3].substring(0, stuff[3].length() - 3)))));

					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	/*	String day_of_interest = "2013-04-13";
		List<TimeSlice> daySlices = allSlices.get(day_of_interest);

		// Create some data structure to hold the data

		// Store in a list

		// Sort the list (for fun!)
		Collections.sort(daySlices, new Comparator<TimeSlice>() {
			public int compare(TimeSlice o1, TimeSlice o2) {
				return (o1.getStartTime().compareTo(o2.getStartTime()));
			}
		});

		// First step for a chart: a bar chart showing the amount of time spent
		// in a location over time.

		StringBuffer buffer = new StringBuffer();
		StringBuffer labelBuffer = new StringBuffer();

		for (Iterator iterator = daySlices.iterator(); iterator.hasNext();) {
			TimeSlice timeSlice = (TimeSlice) iterator.next();
			buffer.append(timeSlice.getDurationInSeconds());
			labelBuffer.append(timeSlice.getPlaceName());

			if (iterator.hasNext()) {
				buffer.append(", ");
				labelBuffer.append(", ");
			}
		}*/

	}
}
