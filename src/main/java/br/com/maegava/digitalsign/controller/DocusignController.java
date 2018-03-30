package br.com.maegava.digitalsign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.maegava.digitalsign.service.DocusignService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(path = "/api/docusign", produces="application/json")
public class DocusignController {
	
	@Autowired DocusignService docusignSvn;
	
	@RequestMapping(path = "/", method = RequestMethod.POST)
	public boolean assignDocument() {
		return docusignSvn.sendDocument();
	}
	
	@RequestMapping(path = "/redirect", method = RequestMethod.POST)
	public void receiveAssginedDocument() {
		//docusignSvn.sendDocument();
	}
}
