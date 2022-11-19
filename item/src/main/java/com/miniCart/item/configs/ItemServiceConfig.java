package com.miniCart.item.configs;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "item")
public class ItemServiceConfig {

	private String msg;
	private String buildVersion;
	private Map<String, String> mailDetails;
	private List<String> activeDBs;

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
