package br.com.maegava.digitalsign.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import br.com.maegava.digitalsign.util.HttpClientUtils;

@Service
public class DigitalSignService {
	
	private String DS_REST_API_URL = "demo.docusign.net/restapi";
	private String O_AUTH_URL = "account-d.docusign.com";
	
	//private String publicKeyFilename = "C:\\Users\\guilhermetsuneo\\eclipse-workspace\\DIGITAL-SIGN-SERVICES\\src\\main\\resources\\rsakey.pub";
	//private String privateKeyFilename = "C:\\Users\\guilhermetsuneo\\eclipse-workspace\\DIGITAL-SIGN-SERVICES\\src\\main\\resources\\rsakey";
	
	//private String userId = "cc5bf558-4f98-4104-a11f-a39fdc92f4a0";
	//private String IntegratorKey = "cec113fe-9846-4713-89d1-0880806a4518";
//	private String SecretKey = "55c26e0c-2110-45a0-b03e-2999a4b53b0e";	
	
	private String accountId;
	private String baseUrl;
	
	//TODO: Generate JWT with jsonwebtoken Library
	private String assertion = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJjZWMxMTNmZS05ODQ2LTQ3MTMtODlkMS0wODgwODA2YTQ1MTgiLCJzdWIiOiJjYzViZjU1OC00Zjk4LTQxMDQtYTExZi1hMzlmZGM5MmY0YTAiLCJpYXQiOjE0OTkyOTM4OTMsImV4cCI6MTQ5OTI5NzQ5MywiYXVkIjoiYWNjb3VudC1kLmRvY3VzaWduLmNvbSIsInNjb3BlIjoic2lnbmF0dXJlIn0.BtcmZysotj6E_rQcd7ZJ6DE3lUNnw_jNI3joGn5DVRe67wxk-lWrieIos34geqdnYxkkPHX6P0sSDHqCjmcFWtS2Gqp8XKrrR2L9YoIaiNMwCZ0kO4zI-9plnPHvHyksZdzuNQAZ-SKmM3zutwfw7sMMdrhpfBM0HUAS7lc73jQEZ1MmVHUlfxhxVdY9KpeJMJB6CGYWeMzZOInItVC7BjNw0uueSJYTEFiX63Rj2kgXvs9nwSLIVq6xGGrRygY9_pv-3HUIf-Q4_TsFEqJNblthiFHf_P9H5tSXyXO7MxXaY1C_mK8tsZ5t30mXbwNOfwiYrRU6oPSeyoxHTSF05A";
	
	private String accessToken = "";
	
	String base64Doc = "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cGUgL0ZvbnQKL1N1YnR5cGUgL1R5cGUxCi9CYXNlRm9udCAvVGltZXMtSXRhbGljCi9FbmNvZGluZyAvV2luQW5zaUVuY29kaW5nCj4+CmVuZG9iagp4cmVmCjAgMTEKMDAwMDAwMDAwMCA2NTUzNSBmDQowMDAwMDAwMDE1IDAwMDAwIG4NCjAwMDAwMDAxNjkgMDAwMDAgbg0KMDAwMDAwMDA3OCAwMDAwMCBuDQowMDAwMDAwMjI2IDAwMDAwIG4NCjAwMDAwMDAzNDIgMDAwMDAgbg0KMDAwMDAwMTUwMyAwMDAwMCBuDQowMDAwMDAxNTM2IDAwMDAwIG4NCjAwMDAwMDE1ODggMDAwMDAgbg0KMDAwMDAwMTY4NiAwMDAwMCBuDQowMDAwMDAxNzg1IDAwMDAwIG4NCnRyYWlsZXIKPDwKL1Jvb3QgMSAwIFIKL0luZm8gMyAwIFIKL0lEIFs8QUNCOTI3NkQ4NEVCMTc0OEVGODEzOUUxRUUwQkVGNDc+IDxBQ0I5Mjc2RDg0RUIxNzQ4RUY4MTM5RTFFRTBCRUY0Nz5dCi9TaXplIDExCj4+CnN0YXJ0eHJlZgoxODg2CiUlRU9GCg==";
	
	private String headerAuth() {
		return "Bearer " + accessToken; 
	}
	
	private String getDSAccessToken() throws Exception {
		String result = null;
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		JSONObject responseJSON = HttpClientUtils
									.post(  "https://"+O_AUTH_URL+"/oauth/token", 
											headers, 
											new StringEntity("grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer" + "&assertion=" + assertion));
		
		result = responseJSON.get("access_token").toString();
		
		return result;
	}
	
	private void getDSUserInfo() throws Exception {
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("Authorization", headerAuth());
		JSONObject responseJSON = HttpClientUtils.get("https://"+DS_REST_API_URL+"/v2/login_information", headers);
		
		accountId = responseJSON.getJSONArray("loginAccounts").getJSONObject(0).getString("accountId");
		baseUrl = responseJSON.getJSONArray("loginAccounts").getJSONObject(0).getString("baseUrl");
	}
	
	public String sendDocumentWithEmbeddedSign(String encoded64Document, String email, String name) throws Exception {
		String url = null;
		String envelopeUri = "";
		try {
			accessToken = getDSAccessToken();
			getDSUserInfo();
			
			JSONObject body = new JSONObject()
								.put("status", "sent")
								.put("emailSubject", "XPTO DOCUSIGN")
								.put("emailBlurb", "SIGN THE DOC")
								.put("recipients", new JSONObject()
									.put("signers", new JSONArray()
											.put(new JSONObject()
													.put("email",email)
													.put("name",name)
													.put("recipientId", "1")
													.put("clientUserId", "1234"))
									)
								)
								.put("documents", new JSONArray()
									.put(new JSONObject()
										.put("documentId", "1")
										.put("name", "xpto_benefit_report.pdf")
										.put("documentBase64", encoded64Document)
								));
			
			envelopeUri = sendEnvelope(body);
			url = generateRecepientSignUrl(envelopeUri, email, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		
		return url;
	}
	
	private String sendEnvelope(JSONObject body) throws Exception {
		String returnUri = "";
		Map<String,String> headers = new HashMap<String,String>();
		
		try {
			headers.put("Authorization", headerAuth());
			headers.put("Content-Type", "application/json");
			headers.put("Accept", "application/json");
			
			JSONObject responseJSON = HttpClientUtils
					.post(  "https://"+DS_REST_API_URL+"/v2/accounts/"+accountId+"/envelopes", 
							headers, 
							new StringEntity(body.toString()));
			returnUri = responseJSON.getString("uri");
		} catch (Exception e) {
			throw e;
		}
		
		return returnUri;
	}
	
	private String generateRecepientSignUrl(String envelopeUri, String email, String name) throws Exception {
		Map<String,String> headers = new HashMap<String,String>();
		
		headers.put("Authorization", headerAuth());
		headers.put("Content-Type", "application/json");
		
		JSONObject body = new JSONObject()
							.put("userName", name)
							.put("email", email)
							.put("recipientId", "1")
							.put("clientUserId", "1234")
							.put("authenticationMethod", "email")
							.put("returnUrl", "http://localhost:8081/api/digitalsign");
		
		JSONObject responseJSON = HttpClientUtils
				.post(  "https://"+DS_REST_API_URL+"/v2/accounts/"+accountId+envelopeUri+"/views/recipient", 
						headers, 
						new StringEntity(body.toString()));
		
		return responseJSON.getString("url");
	}
	
	public String defaultPage(String event) {
		String content = "guilherme-maegava/DIGITAL-SIGN-SERVICES.\n" +
						 "Assign Status: " + event +"\n";
//						 cancel - the recipient decides to finish later
//decline - the recipient declines signing
//exception - a processing error occurs during the signing session
//fax_pending - if the recipient choses to print, sign and fax back
//id_check_failed - if authentication was added to the document, this is when the recipient fails
//session_timeout - the signing session times out when recipient goes idle
//signing_complete - the recipient completed signing
//ttl_expired - the token was not used within the timeout period or the token was already accessed
//viewing_complete - a recipient that does not need to sign completes the viewing ceremony
		
		return content;
	}
	
}