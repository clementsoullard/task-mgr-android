package com.clement.tvscheduler.task;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.clement.tvscheduler.MainActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by clement on 06/09/16.
 */
public abstract class BaseTask extends AsyncTask<Integer, Integer, Long> {

    static SSLContext context;
    protected MainActivity mainActivity;
    public BaseTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * @param url
     * @return
     * @throws Exception
     */
    protected InputStream getInputStreamConnection(URL url) throws Exception {
        if (context == null) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
            AssetManager assetManager = mainActivity.getAssets();
            InputStream caInput = assetManager.open("raspberrypi.crt");
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.d(MainActivity.TAG, "Ouverture du certificat " + "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {

                caInput.close();
            }
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
        }
        if (url.toString().startsWith("https:")) {
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = urlConnection.getInputStream();
            return in;
        } else {
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            return in;
        }
    }
}