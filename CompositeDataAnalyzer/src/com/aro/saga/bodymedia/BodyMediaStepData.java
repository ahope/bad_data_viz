package com.aro.saga.bodymedia;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BodyMediaStepData {
	
	
	public BodyMediaStepData() {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			//File file = new File("data\\step_v2.json");
			//System.out.println(file.getAbsolutePath()); 
			
			URL url = getClass().getClassLoader().getResource("/WEB-INF/data/step_v2.json"); 
			System.out.println(url); 
			//url = getServletContext().getClassLoader().getResource("/WEB-INF/data/step_v2.json"); 
			
			
			/*
			String propPath = getServletContext().getResource("/WEB-INF/myPropFolder/myProp.props").toString();
			/* I have to cut the 6 first characterrs which are "file:/"*/
			/* and I don't know why these ccharacteres are here 
			propPath = propPath.substring(6,propPath.length());
			Properties props = new Properties();
			prop.load(new FileInputStream(path));
			*/
			
			Object stepData = mapper.readValue(new File("data/step_v2.json"), Object.class);
			System.out.println(stepData); 
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	
}

