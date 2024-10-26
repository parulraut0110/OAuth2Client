import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientInformationResponse;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientRegistrationResponseParser;

import mongoclient.MongoClientUtil;
import net.minidev.json.JSONObject;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.Scope;
import com.nimbusds.oauth2.sdk.client.ClientMetadata;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationErrorResponse;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationRequest;
import com.nimbusds.oauth2.sdk.client.ClientRegistrationResponse;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

@WebServlet("/ClientRegistrationServlet")
public class ClientRegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClientRegistrationServlet() {
		super();
	}




	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();

		try {

			BearerAccessToken registrationToken = new BearerAccessToken();

			URI regstrEndPoint = new URI("https://oauth2server:8643/OAuth2Server/RegstrEndPoint");

			ClientMetadata clientRegstrData = new ClientMetadata();
			clientRegstrData.setGrantTypes(Set.of(GrantType.AUTHORIZATION_CODE, GrantType.REFRESH_TOKEN));
			Scope scope = new Scope("read write");
			clientRegstrData.setScope(scope);
			clientRegstrData.setLogoURI(new URI("")); //add your logo uri 

			clientRegstrData.setRedirectionURI(new URI("https://OAuth2Client:8543/oauth2client/GetCode"));    

			clientRegstrData.setName("OIDC Client");

			ClientRegistrationRequest regRequest = new ClientRegistrationRequest(
					regstrEndPoint, clientRegstrData, registrationToken
					);

			/*
		HTTPRequest req = regRequest.toHTTPRequest();
		JSONObject jsonObj= req.getBodyAsJSONObject();

		String reqBody = jsonObj.toJSONString();
		System.out.println(reqBody);
			 */

			HTTPResponse httpResponse = regRequest.toHTTPRequest().send();        //post method used 
			ClientRegistrationResponse regResponse = OIDCClientRegistrationResponseParser.parse(httpResponse);
			if (! regResponse.indicatesSuccess()) {
				ClientRegistrationErrorResponse errorResponse = (ClientRegistrationErrorResponse)regResponse;
				System.err.println(errorResponse.getErrorObject());
				return;
			}
			OIDCClientInformationResponse successResponse = (OIDCClientInformationResponse)regResponse;
			if (successResponse instanceof OIDCClientInformationResponse) {
				OIDCClientInformation clientInfo = successResponse.getOIDCClientInformation();

				if (clientInfo != null) {
					System.out.println("Client name : " + clientInfo.getMetadata().getName());
					System.out.println("Client id : " + clientInfo.getID());
					System.out.println("Client id issued at : " + clientInfo.getIDIssueDate());
					System.out.println("Client secret : " + clientInfo.getSecret().getValue());

					writer.println("Client name : " + clientInfo.getMetadata().getName());
					writer.println("Client id : " + clientInfo.getID());
					writer.println("Client id issued at : " + clientInfo.getIDIssueDate());
					writer.println("Client secret : " + clientInfo.getSecret().getValue());

					//System.out.println("grant types : " + clientInfo.getMetadata().getGrantTypes().toArray(new GrantType[2])[1]);
					//System.out.println("Client registration URI : " + clientInfo.getOIDCMetadata().getRedirectionURI());

					System.setProperty("client_secret", clientInfo.getSecret().getValue());
                    
					try {
						
					MongoClient mongoClient = MongoClients.create("mongodb://oauth2clientAdmin:Raut0110@oauth2client:27017/?authSource=ClientRepository&authMechanism=SCRAM-SHA-256");
					MongoDatabase db = mongoClient.getDatabase("ClientRepository");
		           
		            MongoCollection clientDetails = db.getCollection("ClientDetails");
		            Document clientDoc = new Document("client_id", clientInfo.getID().getValue())
							.append("client_secret", clientInfo.getSecret().getValue())
							.append("client_id_issued_at", clientInfo.getIDIssueDate())
							.append("jwks_uri", "https://oauth2server:8643/oauth2server/WebKeySet");
             
                    writer.println("client Doc : " + clientDoc.toString());
					clientDetails.insertOne(clientDoc);
                    
					}catch(Exception e) {
						writer.println(e.getMessage());
					}
					
     


				} else {
					System.err.println("Client information is null.");
				}
			} else {
				System.err.println("Unexpected response type."); 
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);

	}

}