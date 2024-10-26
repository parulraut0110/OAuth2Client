package jwtsignatureverification;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONObject;

import cryptokeyutil.CreatePublicKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class TestJWTSignature {

	public static boolean verifySignature(String keySet, String tokenKid, String jwt) {
		boolean status = false;
		JSONObject jwks = new JSONObject(keySet);
		//System.out.println("jwks : " + jwks.get("keys"));
		JSONArray jsonArray =  (JSONArray) jwks.get("keys");
		System.out.println("jwks : " + jsonArray);
		for(int i = 0; i < jsonArray.length(); i++) {
			JSONObject jwk = jsonArray.getJSONObject(i);
			System.out.println("kid : " + jwk.getString("kid"));

			if(tokenKid.equals(jwk.getString("kid"))) {
				System.out.println("Inside If block");

				String kty = jwk.getString("kty");
				if("RSA".equals(kty)) {
					System.out.println("Inside inner if block");

					String e = jwk.getString("e");
					String n = jwk.getString("n");
					
					
					try {
						Claims claims = Jwts.parserBuilder()
								.setSigningKey(CreatePublicKey.getRSAPublic(n, e))
								.build()
								.parseClaimsJws(jwt)
								.getBody();
						String user = claims.get("user", String.class);
				        String scope = claims.get("scope", String.class);

				        // Print the claims
				        System.out.println("User: " + user);
				        System.out.println("Scope: " + scope);
						status = true;
					} catch (Exception e1) {
						status = false;
					}
				}
				else {
					String x = jwk.getString("x");
					String y = jwk.getString("y");
					//logger.log(Level.INFO, "kid: " + jwk.getString("kid") + " x: " + x + " y: " + y);
					
					try {
						Claims claims = Jwts.parserBuilder()
								.setSigningKey(CreatePublicKey.getECPublic(x, y))
								.build()
								.parseClaimsJws(jwt)
								.getBody();
						
						String user = claims.get("user", String.class);
				        String scope = claims.get("scope", String.class);

				        // Print the claims
				        System.out.println("User: " + user);
				        System.out.println("Scope: " + scope);
				        
						status = true;
					} catch (Exception e1) {
						status = false;
					}
				}

			}    
		}
		
		return status;
	}

	
}

