
import mongoclient.MongoClientUtil;
import jwtsignatureverification.TestJWTSignature;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.StandardCharset;
import com.nimbusds.oauth2.sdk.AuthorizationCode;
import com.nimbusds.oauth2.sdk.id.State;

import cryptokeyutil.CreatePublicKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.json.JSONArray;
import org.json.JSONObject;



@WebServlet("/GetCode")
public class GetCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(GetCodeServlet.class.getName());
	private final String client_id;
	private final String secret;
	private String user = null;
	
	
	public GetCodeServlet() {
        super();
        Document doc = MongoClientUtil.getDatabase().getCollection("ClientDetails").find().first();
        client_id = doc.getString("client_id");
        secret = doc.getString("client_secret");
   }
	 

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FileHandler fileHandler = new FileHandler("C:\\logs\\GetCode.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
		MongoDatabase database = MongoClientUtil.getDatabase();
		MongoCollection<Document> collection = database.getCollection("UserDetails");
		Document userQuery;
		Document doc = collection.find().first();
		String state = doc.getString("state");
		
		String queryCode = null;
		String queryState = null;
		String querySessionId = null; 
		
		String query = request.getQueryString();
		String[] splitQuery = query.split("&", -1);
		for(String s: splitQuery) {
			if(s.startsWith("code")) 
				queryCode = s.split("=")[1];
			if(s.startsWith("state")) 
				queryState = s.split("=")[1];
			if(s.startsWith("session")) 
				querySessionId = s.split("=")[1];
			if(s.startsWith("user")) 
				user = s.split("=")[1];
		}
		logger.log(Level.INFO, "code: " + queryCode + " state: " + queryState + " session id: " + querySessionId + " user: " + user + "state: " + state);
		
		if(!state.equals(queryState))
			response.getWriter().println("code is tampered");
		
		else {
			logger.log(Level.INFO, "code " + queryCode + " state " + queryState);
			/*
			Document query1 = new Document("user", user);
			Bson projection = Projections.include("state");
			Document userInDB = collection.find(query1).projection(projection).first();
			*/
			
			Document query2 = new Document("user", user);
			logger.log(Level.INFO, "query2 : " + query2);
			Document userinfo = collection.find(query2).first();
			String stateInDB = userinfo.getString("state");
			if(!stateInDB.equals(state)) {
				response.getWriter().println("The state value does not match");
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
				logger.log(Level.INFO, "The state value does not match for user " + user );
				return;
			}
			userinfo.append("sessionid", querySessionId);
			try {
				collection.updateOne(Filters.eq("user", user), Updates.set("sessionid", querySessionId));
			} catch(Exception e) {
				logger.log(Level.INFO, "Exception: " + e.getMessage());
			}
			
			String url = "https://oauth2server:8643/OAuth2Server/Token?clientID=" + client_id + "&secret=" + secret + "&code=" + queryCode + "&grant_type=authorization_code";
			logger.log(Level.INFO, "token url: " + url);
			
			response.sendRedirect(url);
		}		
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		String authToken = request.getParameter("token");
        response.getWriter().println("Auth Token : " + authToken);
        String[] splitToken = authToken.split("\\.", -1); 
        
        byte[] headerBytes = Base64.getDecoder().decode(splitToken[0].getBytes());
        String header = new String(headerBytes, StandardCharset.UTF_8);
        response.getWriter().println("Header : " + header);
        
        JSONObject jsonHeader = new JSONObject(header);
        String kid = jsonHeader.getString("kid");
        response.getWriter().println("kid : " + kid);
        
		URI uri = new URI("https://oauth2server:8643/OAuth2Server/WebKeySet");
		HttpsURLConnection conn = (HttpsURLConnection)uri.toURL().openConnection();
		conn.setRequestMethod("GET");
		int responseCode = conn.getResponseCode(); 
		response.getWriter().println("response code : " + responseCode);
		logger.log(Level.INFO, "response code : " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = in.readLine()) != null) 
				sb.append(line);
			String jwks = sb.toString();
			response.getWriter().println("jwks : " + sb.toString());
			response.getWriter().println("reached for before  ....verification");
			
			boolean verified = false;
			response.getWriter().println("declared verified variable");
			try {
			verified = TestJWTSignature.verifySignature(jwks, kid, authToken);
			} catch(Exception e) {
				response.getWriter().println("error: " + e.getMessage());
			}
			response.getWriter().println("reached for after verification");
			if(verified) {
				response.getWriter().println("Verified : True");
				MongoDatabase database = MongoClientUtil.getDatabase();
				MongoCollection<Document> collection = database.getCollection("UserDetails");
				collection.updateOne(Filters.eq("user", user), Updates.set("AuthToken", authToken));
			}
			
		} else {
            System.out.println("Failed to fetch data. HTTP response code: " + responseCode);
            logger.log(Level.WARNING, "Failed to fetch data. HTTP response code: " + responseCode);
        }
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

}