package com.bzrglxt.vo;

public class User {
	private int id;
	private String name;
	private String loginName;
	private String loginPass;
	
	/**
	 * �û����� 0 ����Ա 1 ��ְ��
	 */
	private int type;	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPass() {
		return loginPass;
	}
	public void setLoginPass(String loginPass) {
		this.loginPass = loginPass;
	}
	/**
	 * �û����� 0 ����Ա 1 ��ְ��
	 */
	public int getType() {
		return type;
	}
	/**
	 * �û����� 0 ����Ա 1 ��ְ��
	 */
	public void setType(int type) {
		this.type = type;
	}
}
