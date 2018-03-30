package br.com.maegava.digitalsign.service;

import java.awt.Desktop;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.oltu.oauth2.common.token.BasicOAuthToken;
import org.springframework.stereotype.Service;

import com.docusign.esign.api.AuthenticationApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.client.auth.AccessTokenListener;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.LoginInformation;

@Service
public class DocusignService {
	
	private String DS_REST_API_URL = "https://demo.docusign.net/restapi";
	private String DS_REDIRECT_URL = "https://180.239.199.25/api/docusign/redirect";
	
	private String IntegratorKey = "cec113fe-9846-4713-89d1-0880806a4518";
	private String SecretKey = "55c26e0c-2110-45a0-b03e-2999a4b53b0e";
	
	private String AuthServerUrl = "https://account-d.docusign.com";
	
	private ApiClient dsApi;
	private AuthenticationApi dsAuth;
	
	private String accountId;
	private String baseUrl;
	
	DocusignService() {
		setApiClient();
		setAuthToken();
	}
	
	private void setApiClient() {
		dsApi = new ApiClient(AuthServerUrl,"docusignAccessCode", IntegratorKey, "");
		dsApi.setBasePath(DS_REST_API_URL);
		dsApi.configureAuthorizationFlow(IntegratorKey, "", DS_REDIRECT_URL);
		Configuration.setDefaultApiClient(dsApi);
	}
	
	private void setAuthToken() {
		try {
			String oauthLoginUrl = dsApi.getAuthorizationUri();
			System.out.println(oauthLoginUrl);
			//Desktop.getDesktop().browse(URI.create(oauthLoginUrl));
			String code = IntegratorKey+":"+SecretKey;
			System.out.println("TOKEN: " + code);
			System.out.println("TOKEN 64: " + new String(Base64.getEncoder().encode(code.getBytes())));
			
			dsApi.getTokenEndPoint().setCode(new String(Base64.getEncoder().encode(code.getBytes())));
			
			dsApi.registerAccessTokenListener(new AccessTokenListener() {
				@Override
				public void notify(BasicOAuthToken token) {
					System.out.println("AccessTokenListener - Got a fresh token: " + token.getAccessToken());
				}
			});
			
			dsApi.updateAccessToken();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void authentication() throws ApiException {
		dsAuth = new AuthenticationApi(dsApi);
		LoginInformation loginInfo = dsAuth.login();
		
		accountId = loginInfo.getLoginAccounts().get(0).getAccountId();
		baseUrl = loginInfo.getLoginAccounts().get(0).getBaseUrl();
		
		String[] accountDomain = baseUrl.split("/v2");
		
		dsApi.setBasePath(accountDomain[0]);
		
		Configuration.setDefaultApiClient(dsApi);	
	}
	
	private byte[] loadFile() {
		byte[] fileBytes = null;
		String filePath = "C:/Users/guilhermetsuneo/Downloads/decoded%20(1).pdf";
		
		try {
			Path path = Paths.get(filePath);
			fileBytes = Files.readAllBytes(path);
		} catch (Exception ex) {
			System.out.println(ex);
			fileBytes = null;
		}
		
		return fileBytes;
	}
	
	private EnvelopeDefinition setEnvelopeDefinition(byte[] fileBytes) {
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		envDef.setEmailSubject("Request Sign for Document");
		
		Document doc = new Document();
		doc.setDocumentBase64(Base64.getEncoder().encode(fileBytes).toString());
		doc.setName("assignedDoc");
		doc.setFileExtension(".pdf");
		doc.setDocumentId("1");
		
		List<Document> docs = new ArrayList<Document>();
		docs.add(doc);
		
		envDef.setDocuments(docs);
		envDef.setStatus("created");
		
		return envDef;
	}
	
	private void sendEnvelope(EnvelopeDefinition envDef) {
		EnvelopesApi envApi = new EnvelopesApi();
		try {
			EnvelopeSummary envSumm = envApi.createEnvelope(accountId, envDef);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean sendDocument() {
		boolean bResult = false;
		
		try {
			authentication();
			sendEnvelope(setEnvelopeDefinition(loadFile()));
			bResult = true;
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
		return bResult;
	}
}
