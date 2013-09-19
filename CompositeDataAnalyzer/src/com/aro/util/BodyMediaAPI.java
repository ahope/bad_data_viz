package com.aro.util;



import org.scribe.builder.api.DefaultApi10a; 
import org.scribe.model.Token;

public class BodyMediaAPI extends DefaultApi10a{
	public static final String DEFAULT_OAUTH_ENDPOINT = "https://api.bodymedia.com/oauth"; // Don't put an ending "/" on the URL

	private static final String REQUEST_TOKEN_ENDPOINT = "/requestToken?api_key=%s";
	private static final String ACCESS_TOKEN_ENDPOINT = "/accessToken?api_key=%s";
	private static final String AUTHORIZATION_URL = "/authorize?oauth_token=%s&api_key=%s";

	private String oauthEndpoint = DEFAULT_OAUTH_ENDPOINT;
	private String apiKey = null;

	public BodyMediaAPI(String apiKey) {
	    this.apiKey = apiKey;
	}

	@Override
	public String getAccessTokenEndpoint() {
	    return String.format(oauthEndpoint + ACCESS_TOKEN_ENDPOINT,apiKey);
	}

	@Override
	public String getAuthorizationUrl(Token token) {
	    return String.format(oauthEndpoint + AUTHORIZATION_URL, token.getToken(),apiKey);
	}

	@Override
	public String getRequestTokenEndpoint() {
	    return String.format(oauthEndpoint + REQUEST_TOKEN_ENDPOINT,apiKey);
	}

	public void setOauthEndpoint(String oauthEndpoint) {
	    oauthEndpoint = oauthEndpoint.trim();

	    if (oauthEndpoint.endsWith("/")) {
	        this.oauthEndpoint = oauthEndpoint.substring(0, oauthEndpoint.lastIndexOf("/"));
	    }
	    else {
	        this.oauthEndpoint = oauthEndpoint;
	    }
	}

	public String getOauthEndpoint() {
	    return oauthEndpoint;
	}

	public String getApiKey() {
	    return apiKey;
	}

	public void apiKey(String apiKey) {
	    this.apiKey = apiKey;
	}

}
