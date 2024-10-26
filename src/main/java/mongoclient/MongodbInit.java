package mongoclient;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nimbusds.oauth2.sdk.id.ClientID;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;

import mongoclient.MongoClientUtil;
public class MongodbInit { 

	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
		try {
            //MongoDatabase database = MongoClientUtil.getDatabase();
			MongoClient mongoClient = MongoClients.create("mongodb://oauth2clientAdmin:Raut0110@oauth2client:27017/?authSource=ClientRepository&authMechanism=SCRAM-SHA-256");
			MongoDatabase db = mongoClient.getDatabase("ClientRepository");
           
            MongoCollection collection = db.getCollection("ClientDetails"); 
            
            
            Document clientDoc = new Document("client_id", "c1234")
						.append("client_secret", "zyxw123");
			collection.insertOne(clientDoc);

	}catch(Exception e) {
		System.out.println(e.getMessage());
	}

}
}

