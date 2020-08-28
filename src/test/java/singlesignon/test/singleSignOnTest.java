package singlesignon.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class singleSignOnTest {
	private String token = "";

	@Before
	public void doLogin() throws ClientProtocolException, IOException, JSONException {

		String postEndpoint = "http://localhost:8080/authenticate";

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(postEndpoint);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");

		String inputJson = "{\"username\":\"techgeeknext\",\"password\":\"password\"}";

		StringEntity stringEntity = new StringEntity(inputJson);
		httpPost.setEntity(stringEntity);

		System.out.println("Executing request " + httpPost.getRequestLine());

		HttpResponse response = httpclient.execute(httpPost);

		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		// Throw runtime exception if status code isn't 200
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		// Create the StringBuffer object and store the response into it.
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println("Response : \n" + result.append(line));
		}

		// Lets validate if a text 'employee_salary' is present in the response

		JSONObject jsonObject = new JSONObject(result.toString());
		token = jsonObject.getString("token");

		assertTrue(true);

	}

	@Test
	public void multipleCallTest() throws ClientProtocolException, IOException, JSONException, InterruptedException {
		int maxcnt = 0;
		while (maxcnt < 10) {
			Random r = new Random();
			test(r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256));
			getStd();
			System.out.println("Executing cnt " + maxcnt);

			Thread.sleep((long) (Math.random() * 1000));
			maxcnt++;

		}
		getStd();
	}

	
	public void test(String ip) throws ClientProtocolException, IOException, JSONException {
		String postEndpoint = "http://localhost:8080/ipinfo?ip=" + ip;

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(postEndpoint);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + token);

		String inputJson = "{}";

		StringEntity stringEntity = new StringEntity(inputJson);
		httpPost.setEntity(stringEntity);

		System.out.println("Executing request " + httpPost.getRequestLine());

		HttpResponse response = httpclient.execute(httpPost);

		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		// Throw runtime exception if status code isn't 200
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		// Create the StringBuffer object and store the response into it.
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println("Response : \n" + result.append(line));
		}

		// Lets validate if a text 'employee_salary' is present in the response


		System.out.println(result.toString());
		assertTrue(true);
	}

	@Test
	public void getStd() throws ClientProtocolException, IOException, JSONException {
		String postEndpoint = "http://localhost:8080/ipstat";

		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpPost httpPost = new HttpPost(postEndpoint);

		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Authorization", "Bearer " + token);

		String inputJson = "{}";

		StringEntity stringEntity = new StringEntity(inputJson);
		httpPost.setEntity(stringEntity);

		System.out.println("Executing request " + httpPost.getRequestLine());

		HttpResponse response = httpclient.execute(httpPost);

		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

		// Throw runtime exception if status code isn't 200
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
		}

		// Create the StringBuffer object and store the response into it.
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			System.out.println("Response : \n" + result.append(line));
		}

		// Lets validate if a text 'employee_salary' is present in the response

		JSONObject jsonObject = new JSONObject(result.toString());

		System.out.println(jsonObject);
		assertTrue(true);
	}

}
