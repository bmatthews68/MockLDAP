package com.btmatthews.mockldap.as7;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.DN;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldif.LDIFReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MockLDAP implements MockLDAPMBean {

    private String baseDn;
    private int port;
    private String ldifFile;
    private String userDn;
    private String password;
    private InMemoryDirectoryServer server;

    @Override
    public String getBaseDn() {
        return baseDn;
    }

    @Override
    public void setBaseDn(final String dn) {
        this.baseDn = dn;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void setPort(final int port) {
        this.port = port;
    }

    @Override
    public String getLdifFile() {
        return ldifFile;
    }

    @Override
    public void setLdifFile(final String ldifFile) {
        this.ldifFile = ldifFile;
    }

    @Override
    public String getUserDn() {
        return userDn;
    }

    @Override
    public void setUserDn(final String dn) {
        this.userDn = dn;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    public void start() throws Exception {
        final InMemoryListenerConfig listenerConfig = InMemoryListenerConfig.createLDAPConfig("default", getPort());
        final InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(new DN(getBaseDn()));
        config.setListenerConfigs(listenerConfig);
        if (getUserDn() != null) {
            config.addAdditionalBindCredentials(getUserDn(), getPassword());
        }
        server = new InMemoryDirectoryServer(config);
        server.add(new Entry(getBaseDn(), new Attribute("objectclass", "domain", "top")));
        if (getLdifFile() != null) {
            final File file = new File(getLdifFile());
            final InputStream in = new FileInputStream(file);
            try {
                final LDIFReader reader = new LDIFReader(in);
                server.importFromLDIF(false, reader);
            } finally {
                in.close();
            }
        }
        server.startListening();    }

    public void stop() throws Exception {
        server.shutDown(true);
    }

}