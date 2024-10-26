
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.shaded.json.parser.ParseException;




public class SearchJWK {

	public static void main(String[] args) throws ParseException, java.text.ParseException {
        String keySet = "{\"keys\":[{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"4a59d6d8-e3df-470b-8f8b-47913d74e59c\",\"n\":\"uf3wqJdvg8v_E4LGlLuYv88RtWb8dRvmZqbmmZNAyJi0iGDMrKJ_wz_xMLwGcc5emUcO7PNWlPb1BY_yRpf5t6GrfZJ2dlJXMArFwQd1-dDkalq5t_4-cG2QTyrOTgk6Vd_4SgjqvYIjPuJsS2zpOMtTEqrB98vrHVjclYQvxtOfoQVMQj5iXkk8n8KKYogPPZTxNVG3gZYEK24yCVSRV_qzzvxYaRA41mNG0aKidj5AOXO22GTOhc_nCqTtycSsx0-8nNoMvQKCkzDSRlhaeufvBFS3X0GUUblCnjVKVZFq8uzSbCprAI11b8OwmytAYPisAqlz0qVy1X1rqwPnxQ\"},{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"56e76e68-3878-4951-9f64-5ebc00cf74a7\",\"n\":\"kZpljZiKDhezxdsnnG0SSsZ3nE-weu8A36qL-uqPP0jm_SRDu0pXOnPPrX9qiqHJSOE5MYxOyC-qVU0_uS3KkDWnRNBZirJz2Suydgl8P_UPvyXudp1bsKRjJtJdxMFzr3ia_3nDtKxJjBdjYH1FA_C3EtzBx1FNgLkUxq7YGPOBRXYYmyvQFsGybeMxtsnumWLCQgCkdJ0gXdtpdt0ad4NPmxqjWW4IC-HcoFQfoaT7W7dXrL3b3j1_6QBLSehrxSnjqS2YsLXkTmdziKYsOWaFS3Z_610F6Uw_OcV-ueyZNrDpz444mHZXZK254NoTfhpHfwfDIdmcxTnmEsBR7w\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"secp256k1\",\"kid\":\"a5eae840-1d59-47ce-a397-375274c73aaa\",\"x\":\"23iHmlqVo8gnOKRluMGweTNNOtEHVnZ6QWOxS3kA5i8\",\"y\":\"92gMcKNHqsqUH5uQCmcWNx9Exh5WhjezVrWLW_9XqYE\"},{\"kty\":\"EC\",\"use\":\"sig\",\"crv\":\"secp256k1\",\"kid\":\"c1364152-95cd-4b0c-90cf-949d63ce6255\",\"x\":\"c2b7V8jIjR6jtPszMKqdcE4VT9NJD7Ws2F-cPyvACCY\",\"y\":\"N0OSLl6fU8hxd_SkLI9zrGuOd6FpJ6HcxFYnogG1LYU\"}]}";
		JSONObject jwks = new JSONObject(keySet);
		//System.out.println("jwks : " + jwks.get("keys"));
		JSONArray jsonArray =  (JSONArray) jwks.get("keys");
		System.out.println("jwks : " + jsonArray);
        for(int i = 0; i < 4; i++) {
        	JSONObject jwk = jsonArray.getJSONObject(i);
        	System.out.println("kid : " + jwk.getString("kid"));
        	String kty = jwk.getString("kty");
        	if(kty.equals("RSA")) {
        		String e = jwk.getString("e");
        		String n = jwk.getString("n");
        		System.out.println("e : " + e + " n : " + n);
        	} else {
        		String x = jwk.getString("x");
        		String y = jwk.getString("y");
        		System.out.println("x : " + x + "  y : " + y);
        	}
        		
        }    
	}
}


