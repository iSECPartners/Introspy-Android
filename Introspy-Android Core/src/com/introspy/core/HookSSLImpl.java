package com.introspy.core;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;

// init(KeyManager[] km, TrustManager[] tm, SecureRandom sr) -> 
// 		init(null, trustManagers, null);
class Func_SSL_CHECK_TRUST_MANAGER extends FuncParent { 
	public void execute(Object... args) {
		
		_l.logBasicInfo();
		TrustManager[] tm_arr = (TrustManager[]) args[1];
		// check the trust manager
		if (tm_arr != null && tm_arr[0] != null) {
			X509TrustManager tm = (X509TrustManager) tm_arr[0];
			X509Certificate[] chain = null;
			boolean check = false;
			try {
				tm.checkClientTrusted(chain, "");
				tm.checkServerTrusted(chain, "");
			} catch (CertificateException e) {
				// if it goes here with an invalid cert
				// the app may verify certs
				check = true;
			}
			if (check)
				_l.logFlush_W("The app does not verify SSL certs");
			else
				_l.logFlush_I("Use of a custom Trust Manager, the app may do cert. pinning OR validate any cert");
		}
	}
}

//setSSLSocketFactory(SSLSocketFactory sf)
class Func_SSL_CHECK_TRUST_SOCKETFACTORY extends FuncParent { 
	public void execute(Object... args) {		
		// should only display data when there is a potential issue
		
		// check not implemented yet
		_l.logBasicInfo();
		_l.logFlush_W("Use of a custom SSLSocketFactory, the app may do cert. pinning OR validate any cert");
	}
}

// setHostnameVerifier(X509HostnameVerifier hostnameVerifier)
class Func_CHECK_HOSTNAME_VERIFIER extends FuncParent { 
	public void execute(Object... args) {
		
		// this only display data when there is a potential issue
		if ((org.apache.http.conn.ssl.X509HostnameVerifier)args[0] == 
				SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER) {
			_l.logBasicInfo();
			_l.logParameter("SSLSocketFactory", "ALLOW_ALL_HOSTNAME_VERIFIER");
			_l.logFlush_W("HostNameVerifier set to accept ANY hostname");
		}
	}
}

class Func_CHECK_URI extends FuncParent { 
	public void execute(Object... args) {
		
		// this is noisy so only display data when there is a potential issue
		// arg0 is a uri (string or uri (this may not actually work))
		String uri = (String) args[0];
		if (uri.contains("http:")) {
			_l.logBasicInfo();
			_l.logFlush_W("No SSL: ["+uri+"]");
		}
	}
}
