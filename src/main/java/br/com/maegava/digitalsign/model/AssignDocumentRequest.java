package br.com.maegava.digitalsign.model;

import java.io.Serializable;

public class AssignDocumentRequest implements Serializable {

	private static final long serialVersionUID = -7112178909039510428L;
	
	private String base64Document;
	private String email;
	private String name;
	
	public AssignDocumentRequest(String base64Document, String email, String name) {
		super();
		this.base64Document = base64Document;
		this.email = email;
		this.name = name;
	}
	
	public AssignDocumentRequest() {
		super();
	}
	
	public String getBase64Document() {
		return base64Document;
	}
	
	public void setBase64Document(String base64Document) {
		this.base64Document = base64Document;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
