package com.aro.saga.bodymedia;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeSlice {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 2013-04-14 02:29:11.577+00 
	private static DateFormat read_day_format = new SimpleDateFormat(DATE_FORMAT); 

	private static DateFormat write_day_format = new SimpleDateFormat(DATE_FORMAT); 

	{
		read_day_format.setTimeZone(TimeZone.getDefault()); //getTimeZone("GMT")); 
		write_day_format.setTimeZone(TimeZone.getDefault()); 
	}
	
	private Date day; 
	
	private Date startTime; 
	private Date endTime; 
	
	private String placeName; 
	private String placeType; 
	
	public TimeSlice(){
		
		
	}
	
	public TimeSlice(String plName, String startTime, String endTime){
		setPlaceName(plName); 
		setDay(startTime); 
		setStartTime(startTime); 
		setEndTime(endTime);
	}
	
	
	
	public Date getDay(){
		return day; 
	}
	
	public void setDay(String newDay){
		try {
			day =   read_day_format.parse(newDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String getPlaceName(){
		return placeName; 
	}
	
	public void setPlaceName(String newName){
		this.placeName = newName; 
	}
	
	public String getPlaceType(){
		return placeType; 
	}
	
	public void setPlaceType(String newPlaceType){
		this.placeType = newPlaceType; 
	}
	
	public void setStartTime(String start){
		try {
			startTime =   read_day_format.parse(start);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void setEndTime(String end){
		try {
			endTime =   read_day_format.parse(end);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
	}
	
	public Date getStartTime(){
		return this.startTime; 
	}
	
	public Date getEndTime(){
		return this.endTime; 
	}
	
	public long getDurationInSeconds(){
		return (this.endTime.getTime() - this.startTime.getTime())/1000; 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		buffer.append("TimeSlice: " ); 
		buffer.append(getPlaceName()); 
		buffer.append(","); 
		buffer.append(write_day_format.format(getStartTime())); 
		buffer.append(" - "); 
		buffer.append(write_day_format.format(getEndTime())); 
		
		return buffer.toString();
	}
	
	

}
