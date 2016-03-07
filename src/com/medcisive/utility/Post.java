package com.medcisive.utility;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author vhapalchambj
 */
public class Post<T> {

    private final String USER_AGENT = "Mozilla/5.0";
    private static final Gson _gson = new Gson();

    public Post() {
    }

    public T getResponse(Object returnType, String url) throws Exception {
        return getResponse(returnType, url, "", false);
    }

    public T getResponse(Object returnType, String url, String param) throws Exception {
        return getResponse(returnType, url, param, false);
    }

    public T getResponse(Object returnType, String url, String param, boolean decompress) throws Exception {
        if (returnType == null || param == null) {
            throw new java.lang.NullPointerException("Parameter returnType is null.");
        }
        if(!param.isEmpty()){
            url += "?" + param;
        }
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.flush();
        wr.close();

        InputStream is = con.getInputStream();
        String result;
        if (decompress) {
            result = Util.Unzip(IOUtils.toByteArray(is));
        } else {
            result = IOUtils.toString(is);
        }
        is.close();
        con.disconnect();
        return (T) _gson.fromJson(result, returnType.getClass());
    }
}
