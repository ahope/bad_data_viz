package com.aro.saga.bodymedia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.aro.util.BodyMediaAPI;


public class BodyMediaDataGrabber {
	private static final BodyMediaDataGrabber singleton = new BodyMediaDataGrabber(); 
	
	private final static String API_KEY = "tzmujexwpvzvfsb8ha89pxabq2reshjf";
	private final static String API_SECRET = "6pGNr5fR9MXhKGDASr7xpjBWvdqq88mga4sGdh4ka2ECwSQrWHQeKqRvXH7kDYX4";

	private final static String user_token = "0764691b-8b6c-43ca-9b75-2b0461e9d8e1";
	private final static String user_secret =  "0e3df298-4f15-4bb3-b866-8efa10068e29"; 
	
	private Token accessToken; 
	private final OAuthService oauthService;


	/**
	 * @param args
	 */
	public static void main(String[] args) {        

	    OAuthService oauthService = new ServiceBuilder().provider(new BodyMediaAPI(API_KEY))
	    .apiKey(API_KEY).apiSecret(API_SECRET).build();

	    BodyMediaDataGrabber demo = getSingleton(); //new BodyMediaDataGrabber(oauthService);
	    try {
	        String burn = demo.getDayBurnJson("20130412"); 
	        System.out.println(burn); 
	    } catch (Exception e) {           
	        e.printStackTrace();
	    }        

	}
	
	public static BodyMediaDataGrabber getSingleton(){
		return singleton; 
	}
	
	private BodyMediaDataGrabber() {
		this(new ServiceBuilder().provider(new BodyMediaAPI(API_KEY))
			    .apiKey(API_KEY).apiSecret(API_SECRET).build());

	}
	
	private BodyMediaDataGrabber(OAuthService oauthService) {
		
		this.oauthService = oauthService;
		if (user_token != null && user_token.length() <= 1){
			accessToken = getAccessToken(); 
		}else{
			accessToken = new Token(user_token, user_secret);
		}
	}
	
	public Token getAccessToken() {		
	    //get the request token
	    Token requestToken = oauthService.getRequestToken();
	    System.out.println("Got a request Token: " + requestToken.getToken());

	    String url =  this.oauthService.getAuthorizationUrl(requestToken);        
	    try {
			java.awt.Desktop.getDesktop().browse(URI.create(url));
		

		    System.out.println(url);
		    System.out.print("Please login to the above URL and press Enter in this console to continue. ");
	
		    //  open up standard input
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    //  wait for some input
		    try {
		       br.readLine();
		    } catch (IOException ioe) {          
		       System.exit(1);
		    }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //exchange the request token for the access token after login
	    Token accessToken = oauthService.getAccessToken(requestToken, new Verifier(""));
	    System.out.println("Got an access token: " + accessToken.getToken());
	   // this.user_secret = accessToken.getSecret(); 
	   // this.user_token = accessToken.getToken(); 
	    
	    return accessToken; 
	    
	}
	
	public String getDayBurnJson(String whichDay ) {
		OAuthRequest requestDayBurn = new OAuthRequest(Verb.GET, "http://api.bodymedia.com/v2/json/burn/day/minute/"+whichDay+"?api_key=" + API_KEY);
	    //OAuthRequest requestDayBurn = new OAuthRequest(Verb.GET, "http://api.bodymedia.com/v1.0/energy/minute/20130501?api_key=" + API_KEY);
	    
	    this.oauthService.signRequest(accessToken, requestDayBurn);

	    Response responseDayBurn = requestDayBurn.send();

	    return responseDayBurn.getBody(); 
	}

}
