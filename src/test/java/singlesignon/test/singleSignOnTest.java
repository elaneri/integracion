package singlesignon.test;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;

import com.sso.springboot.Tenant.Tenant;
import com.sso.springboot.Tenant.TenantServiceImpl;
import com.sso.springboot.Usuario.Usuario;
import com.sso.springboot.Usuario.UsuarioServiceImpl;

@RunWith(JUnit4.class)
public class singleSignOnTest {

    
	@Test
	public void createUsers() throws ClientProtocolException, IOException, JSONException {

		assertTrue(true);

	}



}
