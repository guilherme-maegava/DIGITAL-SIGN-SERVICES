package br.com.maegava.digitalsign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.maegava.digitalsign.model.AssignDocumentRequest;
import br.com.maegava.digitalsign.model.Response;
import br.com.maegava.digitalsign.service.DigitalSignService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(path = "/api/digitalsign", produces="application/json")
public class DigitalSignController {
	
	@Autowired DigitalSignService digitalSignSvn;
	
	@RequestMapping(path = "", method = RequestMethod.POST)
	public ResponseEntity<?> assignDocument(@RequestBody AssignDocumentRequest request) {
		ResponseEntity<Response> response = null;
		String redirecturl;
		
		try {
			redirecturl = digitalSignSvn
							.sendDocumentWithEmbeddedSign(
									request.getBase64Document(),
									request.getEmail(), 
									request.getName());
			
			response = new ResponseEntity<Response>(new Response(redirecturl), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<Response>(new Response(-1, e.getMessage().toString(), e),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String returnUrl (@RequestParam(required = true) String event) {
		return digitalSignSvn.defaultPage(event);
	}
}
