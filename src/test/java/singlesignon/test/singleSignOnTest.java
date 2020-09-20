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

	// @Value("${JUNIT_TENANT}")
	@Value("EMIEMIEMIEMIEMIEMI")
	private String junittenant = "EMIEMIEMIEMIEMIEMI";
	// private String appurl = "https://ssoia.herokuapp.com/";
	private String appurl = "http://localhost:8080/";
	private String expToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1eDBySElCNyIsImlzcyI6IlNTTyIsImV4cCI6MTYwMDYxMzU1MiwiaWF0IjoxNjAwNjEyMzQ0LCJjbGllbnRfaWQiOjZ9.AoQz6092wLmMTFzkJmr_txz32S0qyQue0xjLTuA6MkPZQ7f5SvJjxfio3geGqGJWxUzPdLDeOtSOBv2v95FgDg";


	private String wrongToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2MDA2MDYyNzcsImV4cCI6MTYzMjE0MjI3NywiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIkdpdmVuTmFtZSI6IkpvaG5ueSIsIlN1cm5hbWUiOiJSb2NrZXQiLCJFbWFpbCI6Impyb2NrZXRAZXhhbXBsZS5jb20iLCJSb2xlIjpbIk1hbmFnZXIiLCJQcm9qZWN0IEFkbWluaXN0cmF0b3IiXX0.lAsNQL6BGa1wqlmyMmRLEZh4Oi0kDXTWuIr973PgPe0";
	
	String user;
	String pass;

	public String sendPost(String srvPath, String param, String token) throws ClientProtocolException, IOException {

		URL obj = new URL(appurl + srvPath);

		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();

		postConnection.setRequestMethod("POST");

		postConnection.setRequestProperty("Content-Type", "application/json");
		postConnection.setRequestProperty("x-api-key", junittenant);
		if (token!=null)
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

	public void loginUserWrongToken() throws ClientProtocolException, IOException {

		// When
		final String POST_PARAMS = "{\"username\": \"" + user + "\",\"password\": \"" + pass + "\"}";

		System.out.println(POST_PARAMS);

		sendPost("Login", POST_PARAMS, null);

		assertTrue(true);

	}
	
	
	/*test crea un usuario y se loguea*/
	@Test
	public void testCreateUserAndLogin() throws ClientProtocolException, IOException {

		RandomString rn = new RandomString(8, ThreadLocalRandom.current());
		user = rn.nextString();
		pass = rn.nextString() + "@1";

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
	/*consuta con token expirado*/
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
	
	// Bearer
	// eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1eDBySElCNyIsImlzcyI6IlNTTyIsImV4cCI6MTYwMDYxMzU1MiwiaWF0IjoxNjAwNjEyMzQ0LCJjbGllbnRfaWQiOjZ9.AoQz6092wLmMTFzkJmr_txz32S0qyQue0xjLTuA6MkPZQ7f5SvJjxfio3geGqGJWxUzPdLDeOtSOBv2v95FgDg
}
