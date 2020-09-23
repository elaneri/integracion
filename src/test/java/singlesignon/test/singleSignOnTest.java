package singlesignon.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.http.client.ClientProtocolException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Value;

@RunWith(JUnit4.class)
public class singleSignOnTest {

	 //@Value("EMIEMIEMIEMIEMIEMI")
	@Value("${JUNIT_TENANT}")
	private String junittenant="EMIEMIEMIEMIEMIEMI";
	
	private String appurl = "https://ssoia.herokuapp.com/";
//	private String appurl = "http://localhost:8080/";
	private String expToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1eDBySElCNyIsImlzcyI6IlNTTyIsImV4cCI6MTYwMDYxMzU1MiwiaWF0IjoxNjAwNjEyMzQ0LCJjbGllbnRfaWQiOjZ9.AoQz6092wLmMTFzkJmr_txz32S0qyQue0xjLTuA6MkPZQ7f5SvJjxfio3geGqGJWxUzPdLDeOtSOBv2v95FgDg";
	private String wrongToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJzc28iLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTYwMDYyNDkxOCwiaWF0IjoxNjAwNjI0OTE4fQ.lEKbUUiCZgYXpdt7EKWKHXS1M0TZBkr0HYXq2YYZq5J_QvALWC0T-Wl5F5hrjtCBZp974HGPyQ0uZyeumj_TJw";

	String user;
	String pass;

	public String sendPost(String srvPath, String param, String token) throws ClientProtocolException, IOException {

		URL obj = new URL(appurl + srvPath);

		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

		postConnection.setRequestMethod("POST");

		postConnection.setRequestProperty("Content-Type", "application/json");
		postConnection.setRequestProperty("x-api-key", junittenant);
		if (token != null)
			postConnection.setRequestProperty("Authorization", token);

		postConnection.setDoOutput(true);

		OutputStream os = postConnection.getOutputStream();

		os.write(param.getBytes());

		os.flush();

		os.close();

		int responseCode = postConnection.getResponseCode();

		System.out.println("POST Response Code :  " + responseCode);

		System.out.println("POST Response Message : " + postConnection.getResponseMessage());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success

			BufferedReader in = new BufferedReader(new InputStreamReader(

					postConnection.getInputStream()));

			String inputLine;

			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {

				response.append(inputLine);

			}
			in.close();

			// print result
			return response.toString();

		} else {

			return "POST NOT WORKED";

		}
	}

	private void sendGet(String srvPath) throws Exception {

		HttpURLConnection httpClient = (HttpURLConnection) new URL(appurl + srvPath).openConnection();

		// optional default is GET
		httpClient.setRequestMethod("GET");

		// add request header
		httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
		httpClient.setRequestProperty("x-api-key", junittenant);

		int responseCode = httpClient.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + appurl + srvPath);
		System.out.println("Response Code : " + responseCode);

		try (BufferedReader in = new BufferedReader(new InputStreamReader(httpClient.getInputStream()))) {

			StringBuilder response = new StringBuilder();
			String line;

			while ((line = in.readLine()) != null) {
				response.append(line);
			}

			// print result
			System.out.println(response.toString());

		}

	}

	public void loginUserWrongToken() throws ClientProtocolException, IOException {

		// When
		final String POST_PARAMS = "{\"username\": \"" + user + "\",\"password\": \"" + pass + "\"}";

		System.out.println(POST_PARAMS);

		sendPost("Login", POST_PARAMS, null);

		assertTrue(true);

	}

	/* test crea un usuario y se loguea */
	@Test
	public void testCreateUserAndLogin() throws ClientProtocolException, IOException {

		RandomString rn = new RandomString(8, ThreadLocalRandom.current());
		user = rn.nextString();
		pass = rn.nextString() + "@1A";

		String POST_PARAMS_CREATE = "{ \"nombre\": \"" + user + "\",\"apellido\": \"JUNIT" + user + "\",\"usuario\": \""
				+ user + "\",\"password\": \"" + pass
				+ "\", \"mail\": \"test@test.com\",\"fecha_nacimiento\": \"20200101\",\"telefono\": \"0\",\"enable\": true , \"fechaAlta\": \"20200101\",\"fechaBaja\": null,  \"propiedades\": null}";

		System.out.println(POST_PARAMS_CREATE);
		sendPost("Usuarios", POST_PARAMS_CREATE, null);

		// When
		String POST_PARAMS_LOGIN = "{\"username\": \"" + user + "\",\"password\": \"" + pass + "\"}";

		String token = sendPost("Login", POST_PARAMS_LOGIN, null);

		assertTrue(token.contains("token"));

	}

	/* consuta con token expirado */
	@Test
	public void testInvalidToken() throws ClientProtocolException, IOException {

		// When
		String POST_PARAMS = "{}";

		System.out.println(POST_PARAMS);

		String resp = sendPost("isAlive", POST_PARAMS, expToken);

		assertTrue(resp.equals("POST NOT WORKED"));

	}

	@Test
	public void testWrongToken() throws ClientProtocolException, IOException {

		// When
		String POST_PARAMS = "{}";

		System.out.println(POST_PARAMS);

		String resp = sendPost("isAlive", POST_PARAMS, wrongToken);

		assertTrue(resp.equals("POST NOT WORKED"));

	}

	@Test
	public void getClaimsToken() throws Exception {

		sendGet("Claims/ValidClaims");

		assertTrue(true);

	}
}
