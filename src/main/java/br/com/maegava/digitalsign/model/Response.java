package br.com.maegava.digitalsign.model;

import java.io.Serializable;

public class Response implements Serializable{
	
	private static final long serialVersionUID = -530462064683939540L;
	
	private Integer code;
	private String  description;
	private Object content;
	
	private final static String MESSAGE_SUCCESS = "DS_STATUS_OK";
	private final static String MESSAGE_ERROR = "DS_STATUS_ERROR";
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	
	public Response() {
		super();
	}

	public Response(Object content) {
		super();
		this.code = (content != null ? 0 : -1);
		this.description = (content != null ? MESSAGE_SUCCESS : MESSAGE_ERROR );
		this.content = content;
	}
	
	public Response(Integer code, String description, Object content) {
		super();
		this.code = code;
		this.description = description;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return "ResponseModel [code=" + code + ", description=" + description + ", content=" + content + "]";
	}
}
