package cryptokeyutil;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class CreatePublicKey {
	public static PublicKey getRSAPublic(String n, String e) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(n);
		BigInteger modulus = new BigInteger(1, decodedBytes);
		System.out.println("n : " + modulus);
		
		decodedBytes = Base64.getDecoder().decode(e);
		BigInteger publicExponent = new BigInteger(1, decodedBytes);
		System.out.println("e : " + publicExponent);


		KeyFactory keyFactory;
		PublicKey	publicKey = null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent)); 
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e1) {
			e1.printStackTrace();
		}   
		System.out.println("Public: " + publicKey);
		return publicKey;
	}


	public static PublicKey getECPublic(String x, String y) {

		byte[] xdecodedBytes = Base64.getUrlDecoder().decode(x);

		StringBuilder sbx = new StringBuilder();
		for (byte b : xdecodedBytes) {
			String xhex = Integer.toHexString(0xff & b);
			if (xhex.length() == 1) {
				sbx.append('0');
			}
			sbx.append(xhex);
		}
		String xHex = sbx.toString();
		
		byte[] ydecodedBytes = Base64.getUrlDecoder().decode(y);

		StringBuilder sby = new StringBuilder();
		for (byte b : ydecodedBytes) {
			String yhex = Integer.toHexString(0xff & b);
			if (yhex.length() == 1) {
				sby.append('0');
			}
			sby.append(yhex);
		}
		String yHex = sby.toString();
		
		PublicKey publicKey = null;
		try {
			Security.addProvider(new BouncyCastleProvider());

			BigInteger xCoord = new BigInteger(xHex, 16);
			BigInteger yCoord = new BigInteger(yHex, 16);

			ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

			ECPoint ecPoint = ecSpec.getCurve().createPoint(xCoord, yCoord);

			ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(ecPoint, ecSpec);
			KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
			publicKey = keyFactory.generatePublic(pubKeySpec);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("PubicKey: " + publicKey);
		return publicKey;
	}
	
     
	public static void main(String args[]) {
		String n = "uf3wqJdvg8v_E4LGlLuYv88RtWb8dRvmZqbmmZNAyJi0iGDMrKJ_wz_xMLwGcc5emUcO7PNWlPb1BY_yRpf5t6GrfZJ2dlJXMArFwQd1-dDkalq5t_4-cG2QTyrOTgk6Vd_4SgjqvYIjPuJsS2zpOMtTEqrB98vrHVjclYQvxtOfoQVMQj5iXkk8n8KKYogPPZTxNVG3gZYEK24yCVSRV_qzzvxYaRA41mNG0aKidj5AOXO22GTOhc_nCqTtycSsx0-8nNoMvQKCkzDSRlhaeufvBFS3X0GUUblCnjVKVZFq8uzSbCprAI11b8OwmytAYPisAqlz0qVy1X1rqwPnxQ";
		String e = "AQAB";
		getRSAPublic(n, e);
	}
}