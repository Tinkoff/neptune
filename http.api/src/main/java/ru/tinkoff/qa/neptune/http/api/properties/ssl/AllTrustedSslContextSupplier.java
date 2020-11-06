package ru.tinkoff.qa.neptune.http.api.properties.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class AllTrustedSslContextSupplier extends DefaultHttpSslContextProperty.SslContextSupplier {

    @Override
    public SSLContext get() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            TrustManager[] trustManager = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(X509Certificate[] certificate, String str) {
                        }

                        public void checkServerTrusted(X509Certificate[] certificate, String str) {
                        }
                    }
            };
            context.init(null, trustManager, new SecureRandom());
            return context;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}
