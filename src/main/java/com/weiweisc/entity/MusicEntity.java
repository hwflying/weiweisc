package com.weiweisc.entity;

public class MusicEntity {

	private Integer code;
	private String status;
	private String msg;
	private MusicPlayinfo data;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public MusicPlayinfo getData() {
		return data;
	}
	public void setData(MusicPlayinfo data) {
		this.data = data;
	}
	
}
