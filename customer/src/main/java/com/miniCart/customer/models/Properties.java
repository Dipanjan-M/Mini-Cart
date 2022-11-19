package com.miniCart.customer.models;

import java.util.List;
import java.util.Map;

public class Properties {

	private String msg;
	private String buildVersion;
	private Map<String, String> mailDetails;
	private List<String> activeDBs;

	public Properties(String msg, String buildVersion, Map<String, String> mailDetails, List<String> activeDBs) {
		this.msg = msg;
		this.buildVersion = buildVersion;
		this.mailDetails = mailDetails;
		this.activeDBs = activeDBs;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public Map<String, String> getMailDetails() {
		return mailDetails;
	}

	public void setMailDetails(Map<String, String> mailDetails) {
		this.mailDetails = mailDetails;
	}

	public List<String> getActiveDBs() {
		return activeDBs;
	}

	public void setActiveDBs(List<String> activeDBs) {
		this.activeDBs = activeDBs;
	}

}
