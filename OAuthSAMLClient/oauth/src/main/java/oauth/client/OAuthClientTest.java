package main.java.oauth.client;

import java.io.IOException;

public class OAuthClientTest {

	public static void main(String[] args) throws IOException {
		OAuthSAMLClient client = new OAuthSAMLClient();
		client.getAccessToken();
	}

}
