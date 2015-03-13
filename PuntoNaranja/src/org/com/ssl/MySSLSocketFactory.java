/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.com.ssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


public class MySSLSocketFactory {

    private SSLContext sslc = null;
    private SSLServerSocketFactory serverFactory = null;
    private SSLSocketFactory socketFactory = null;

    private String keyStore = null;
    private String password = null;
    private String keyPassword = null;
    private String serverName;
    private boolean clientAuthNeeded = false;
    private boolean serverAuthNeeded = false;
    private String[] enabledCipherSuites;
    
    

//    private Configuration cfg;

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setClientAuthNeeded(boolean clientAuthNeeded) {
        this.clientAuthNeeded = clientAuthNeeded;
    }

    public void setServerAuthNeeded(boolean serverAuthNeeded) {
        this.serverAuthNeeded = serverAuthNeeded;
    }

    private TrustManager[] getTrustManagers(KeyStore ks)
            throws GeneralSecurityException {
        if (serverAuthNeeded) {
            TrustManagerFactory tm = TrustManagerFactory.getInstance("SunX509");
            tm.init(ks);
            return tm.getTrustManagers();
        } else {
            return new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };
        }
    }

    private SSLContext getSSLContext() {
        if (password == null) {
            password = getPassword();
        }
        if (keyPassword == null) {
            keyPassword = getKeyPassword();
        }
        if (keyStore == null || keyStore.length() == 0) {
            keyStore = System.getProperty("user.home") + File.separator + ".keystore";
        }
        SSLContext sslc = null;
        try {

            KeyStore ks = KeyStore.getInstance("JKS");
            //InputStream fis = new FileInputStream(new File(keyStore));
            InputStream fis = getClass().getResourceAsStream("/mykeystore.keystore"); 
            //BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            
            //InputStream fis = new FileInputStream(new File(getClass().getResourceAsStream("/mykeystore.keystore").toString()));
            ks.load(fis, password.toCharArray());
            fis.close();
            KeyManagerFactory km = KeyManagerFactory.getInstance("SunX509");
            km.init(ks, keyPassword.toCharArray());
            KeyManager[] kma = km.getKeyManagers();
            TrustManager[] tma = getTrustManagers(ks);
            sslc = SSLContext.getInstance("SSL");
            sslc.init(kma, tma, SecureRandom.getInstance("SHA1PRNG"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            password = null;
            keyPassword = null;
        }
        return sslc;
    }

    protected SSLServerSocketFactory createServerSocketFactory() {
        if (sslc == null) {
            sslc = getSSLContext();
        }
        return sslc.getServerSocketFactory();
    }

    protected SSLSocketFactory createSocketFactory() {
        if (sslc == null) {
            sslc = getSSLContext();
        }
        return sslc.getSocketFactory();
    }

    public ServerSocket createServerSocket(int port) {
        ServerSocket socket = null;
        try {
            if (serverFactory == null) {
                serverFactory = createServerSocketFactory();
            }
            socket = serverFactory.createServerSocket(port);
            SSLServerSocket serverSocket = (SSLServerSocket) socket;
            serverSocket.setNeedClientAuth(clientAuthNeeded);
            if (enabledCipherSuites != null && enabledCipherSuites.length > 0) {
                serverSocket.setEnabledCipherSuites(enabledCipherSuites);
            }
        } catch (Exception ex) {

        }

        return socket;
    }

    public Socket createSocket(String host, int port) {
        SSLSocket s = null;
        try {
            if (socketFactory == null) {
                socketFactory = createSocketFactory();
            }
            s = (SSLSocket) socketFactory.createSocket(host, port);
            verifyHostname(s);

        } catch (Exception ex) {

        }
        return s;
    }

    private void verifyHostname(SSLSocket socket)
            throws SSLPeerUnverifiedException, UnknownHostException {
        if (!serverAuthNeeded) {
            return;
        }

        SSLSession session = socket.getSession();

        if (serverName == null || serverName.length() == 0) {
            serverName = session.getPeerHost();
            try {
                InetAddress addr = InetAddress.getByName(serverName);
            } catch (UnknownHostException uhe) {
                throw new UnknownHostException("Could not resolve SSL "
                        + "server name " + serverName);
            }
        }

        X509Certificate[] certs = session.getPeerCertificateChain();
        if (certs == null || certs.length == 0) {
            throw new SSLPeerUnverifiedException("No server certificates found");
        }

        String dn = certs[0].getSubjectDN().getName();

        String cn = getCN(dn);
        if (!serverName.equalsIgnoreCase(cn)) {
            throw new SSLPeerUnverifiedException("Invalid SSL server name. "
                    + "Expected '" + serverName
                    + "', got '" + cn + "'");
        }
    }

    private String getCN(String dn) {
        int i = dn.indexOf("CN=");
        if (i == -1) {
            return null;
        }
        dn = dn.substring(i + 3);
        char[] dncs = dn.toCharArray();
        for (i = 0; i < dncs.length; i++) {
            if (dncs[i] == ',' && i > 0 && dncs[i - 1] != '\\') {
                break;
            }
        }
        return dn.substring(0, i);
    }

    public String getKeyStore() {
        return keyStore;
    }

    protected String getPassword() {
        return System.getProperty("jpos.ssl.storepass", "password");
    }

    protected String getKeyPassword() {
        return System.getProperty("jpos.ssl.keypass", "password");
    }

    public String getServerName() {
        return serverName;
    }

    public boolean getClientAuthNeeded() {
        return clientAuthNeeded;
    }

    public boolean getServerAuthNeeded() {
        return serverAuthNeeded;
    }

    public void setEnabledCipherSuites(String[] enabledCipherSuites) {
        this.enabledCipherSuites = enabledCipherSuites;
    }

    public String[] getEnabledCipherSuites() {
        return enabledCipherSuites;
    }

}
