package mongoclient;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoClientUtil {
	private static final ThreadLocal<MongoDatabase> db = new ThreadLocal<>();
	
	public static MongoDatabase getDatabase() {
		if(db.get() == null) {
			MongoClient mongoClient = MongoClients.create("mongodb://oauth2clientAdmin:Raut0110@oauth2client:27017/?authSource=ClientRepository&authMechanism=SCRAM-SHA-256");
			db.set(mongoClient.getDatabase("ClientRepository"));
		}
		return db.get();
	}
	
	
}