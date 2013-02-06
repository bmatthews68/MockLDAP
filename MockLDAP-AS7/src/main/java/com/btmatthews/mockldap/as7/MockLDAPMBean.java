package com.btmatthews.mockldap.as7;

public interface MockLDAPMBean {

	String getBaseDn();

	void setBaseDn(String dn);

	int getPort();

	void setPort(int port);

	String getLdifFile();

	void setLdifFile(String ldifFile);

	String getUserDn();

	void setUserDn(String dn);

	String getPassword();

	void setPassword(String password);
}