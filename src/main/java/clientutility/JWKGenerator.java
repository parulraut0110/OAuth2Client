package clientutility;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.util.Base64URL;

class RSAJWKGenerator{
	Base64URL e;
	Base64URL n;
	
	public RSAJWKGenerator(Base64URL e, Base64URL n) {
		this.e = e;
		this.n = n;		
	}
}

public class JWKGenerator {

	public static void main(String[] args) {
		
	
		/*
		 String jwksString = "..."; // Replace with your JWKS content

	        // Parse JWKS
	        JWKSet jwks = JWKSet.parse(jwksString);

	        // Find RSA key
	        JWK rsaJwk = jwks.getKeys().stream()
	                .filter(key -> key.getKeyType().getValue().equals("RSA"))
	                .findFirst()
	                .orElseThrow(() -> new IllegalArgumentException("No RSA key found in JWKS"));

	        // Extract public key parameters
	        Base64URL e = rsaJwk.getModulus();
	        Base64URL n = rsaJwk.getPublicExponent();

		
		Base64URL modulus = new Base64URL(new String(modulus));
		Base64URL exponent = new Base64URL(new String(exponent));

		// Build an RSA JWK object
		JWK rsaJwk = new RSAJWK.Builder(modulus, exponent).build();

		// Use the JWK object for signature verification (replace with your signed data)
		String signedData = "...";
		JWSVerifier verifier = new JWSVerifier(rsaJwk);
		boolean isValid = verifier.verify(signedData);

		if (isValid) {
		    System.out.println("Signature is valid!");
		} else {
		    System.out.println("Signature verification failed!");
		}
		*/
	}

}
