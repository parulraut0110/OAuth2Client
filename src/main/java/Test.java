import java.math.BigInteger;
import jwtsignatureverification.TestJWTSignature;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Level;

import org.json.JSONArray;
import org.json.JSONObject;

import cryptokeyutil.CreatePublicKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Test {
	
	
	public static void main(String[] args) {
		String jwks = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"4a59d6d8-e3df-470b-8f8b-47913d74e59c\",\"n\":\"uf3wqJdvg8v_E4LGlLuYv88RtWb8dRvmZqbmmZNAyJi0iGDMrKJ_wz_xMLwGcc5emUcO7PNWlPb1BY_yRpf5t6GrfZJ2dlJXMArFwQd1-dDkalq5t_4-cG2QTyrOTgk6Vd_4SgjqvYIjPuJsS2zpOMtTEqrB98vrHVjclYQvxtOfoQVMQj5iXkk8n8KKYogPPZTxNVG3gZYEK24yCVSRV_qzzvxYaRA41mNG0aKidj5AOXO22GTOhc_nCqTtycSsx0-8nNoMvQKCkzDSRlhaeufvBFS3X0GUUblCnjVKVZFq8uzSbCprAI11b8OwmytAYPisAqlz0qVy1X1rqwPnxQ\"},{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"56e76e68-3878-4951-9f64-5ebc00cf74a7\",\"n\":\"kZpljZiKDhezxdsnnG0SSsZ3nE-weu8A36qL-uqPP0jm_SRDu0pXOnPPrX9qiqHJSOE5MYxOyC-qVU0_uS3KkDWnRNBZirJz2Suydgl8P_UPvyXudp1bsKRjJtJdxMFzr3ia_3nDtKxJjBdjYH1FA_C3EtzBx1FNgLkUxq7YGPOBRXYYmyvQFsGybeMxtsnumWLCQgCkdJ0gXdtpdt0ad4NPmxqjWW4IC-HcoFQfoaT7W7dXrL3b3j1_6QBLSehrxSnjqS2YsLXkTmdziKYsOWaFS3Z_610F6Uw_OcV-ueyZNrDpz444mHZXZK254NoTfhpHfwfDIdmcxTnmEsBR7w\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"secp256k1\",\"kid\":\"a5eae840-1d59-47ce-a397-375274c73aaa\",\"x\":\"23iHmlqVo8gnOKRluMGweTNNOtEHVnZ6QWOxS3kA5i8\",\"y\":\"92gMcKNHqsqUH5uQCmcWNx9Exh5WhjezVrWLW_9XqYE\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"secp256k1\",\"kid\":\"c1364152-95cd-4b0c-90cf-949d63ce6255\",\"x\":\"c2b7V8jIjR6jtPszMKqdcE4VT9NJD7Ws2F-cPyvACCY\",\"y\":\"N0OSLl6fU8hxd_SkLI9zrGuOd6FpJ6HcxFYnogG1LYU\"}]}";
		String tokenKid = "4a59d6d8-e3df-470b-8f8b-47913d74e59c"; 
		String jwt = "eyJraWQiOiI0YTU5ZDZkOC1lM2RmLTQ3MGItOGY4Yi00NzkxM2Q3NGU1OWMiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzY29wZSI6InJlYWQrd3JpdGUiLCJ1c2VyIjoiQWRpdGkifQ.MUdE2SjlRxnXIgcaRUggwyYqxSUW322NOBlIpvT-E1zW1qHbaD-k84eNEiNHmCxJuSWcJ4MhZM3P_2P6xGyTRvtLDnOyP7jhjDu3vH3yZ59I0_x7eSwo5N-DoHkvJPQxotztMunlmzdo86xXDMim2hHrjSE4NX7iBsrverHtMaoBVnjfCQsY8r7n3IRp1uKEoYBe3f9uigFqdGwnit3kvyrJmeqjF9WpC0JMmrErQSA0jdupoXY-hjKdBRuMXNJpX1yUf0FFTk3VydQ1IgsNhE_BaSJcwWosNAIhGnx1ckYPGRwJ5ACWQtltlw8UYAe3OUvpLkR_SW85c_BrUioMqA";
		boolean verified = TestJWTSignature.verifySignature(jwks, tokenKid, jwt);
		System.out.println("verified: " + verified);
		
	}
}
