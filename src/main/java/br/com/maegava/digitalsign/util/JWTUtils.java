package br.com.maegava.digitalsign.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTUtils {
	
	public static byte[] decodeRSA64(String filename) {
		List<String> lFile64 = null;
		String raw64 = "";
		
		try {
			lFile64 = Files.readAllLines(Paths.get(filename));
			for(int i = 1; i < lFile64.size()-1; i ++) {
				raw64 += lFile64.get(i);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Base64.getDecoder().decode(raw64);
	}
	
	//TODO
	private static void getRSAKeys(String publicKeyFilename, String privateKeyFilename, RSAPublicKey publicKey, RSAPrivateKey privateKey) throws Exception {
		
		
		publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodeRSA64(publicKeyFilename)));
		privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodeRSA64(privateKeyFilename)));
		
		System.out.println(decodeRSA64(publicKeyFilename));
	}
	
	//TODO
	public static String generateJWT(String publicKeyFilename, String privateKeyFilename, String iss, String sub, Date iat, Date exp, String aud, String scope) {
		RSAPublicKey publicKey = null;
		RSAPrivateKey privateKey = null;
		String token = null;
		
		try {
			getRSAKeys(publicKeyFilename,privateKeyFilename,publicKey,privateKey);
			Algorithm algorithmRS = Algorithm.RSA256(publicKey, privateKey);
			token = JWT.create()
					.withIssuer(iss)
					.withSubject(sub)
					.withIssuedAt(iat)
					.withExpiresAt(exp)
					.withAudience(aud)
					.withClaim("scope", scope)
					.sign(algorithmRS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return token;
	}
}
