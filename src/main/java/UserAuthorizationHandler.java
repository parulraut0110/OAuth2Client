
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.oauth2.sdk.AuthorizationRequest;
import com.nimbusds.oauth2.sdk.ResponseType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.id.State;

import mongoclient.MongoClientUtil;


@WebServlet("/UserAuthorizationHandler")
public class UserAuthorizationHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	
	
    
    public UserAuthorizationHandler() {
        super();
       }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		URI authzEndpoint = null;
		try {
			authzEndpoint = new URI("https://oauth2server:8643/OAuth2Server/Auth");
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		FileHandler fileHandler = new FileHandler("C:\\logs\\UserRequestHandler.log");
		fileHandler.setLevel(Level.INFO);
		logger.addHandler(fileHandler);
		logger.setLevel(Level.INFO);
		fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
		
		MongoDatabase database = MongoClientUtil.getDatabase();
		logger.log(Level.INFO, database.getName());
		MongoCollection clientDetails = database.getCollection("ClientDetails");
		Document clientDoc = (Document) clientDetails.find().first();
		logger.log(Level.INFO, clientDoc.toJson());
		ClientID clientID = new ClientID(clientDoc.getString("client_id"));
		logger.log(Level.INFO, clientID.getValue());
		
		// The requested scope values for the token
		Scope scope = new Scope("read", "write");

		// The client callback URI, typically pre-registered with the server
		URI callback = null;
		
		try {
			
			callback = new URI(clientDoc.getString("redirect_uri"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// Generate random state string for pairing the response to the request
		State state = new State();

		// Build the request
		AuthorizationRequest authRequest = new AuthorizationRequest.Builder(
		    new ResponseType(ResponseType.Value.CODE), clientID)
		    .scope(scope)
		    .state(state)
		    .redirectionURI(callback)
		    .endpointURI(authzEndpoint)
		    .build();
		
		logger.log(Level.INFO, "RequestQuery : " + authRequest.toQueryString());
        
		MongoCollection userDetails = database.getCollection("UserDetails");
		userDetails.insertOne(new Document("user", request.getParameter("user")).append("state", state.getValue()));
		
		request.setAttribute("request_uri", authRequest);
		
	}

}