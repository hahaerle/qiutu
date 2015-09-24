package com.lenote.qiutu.api;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.github.kevinsawicki.http.HttpRequest;
import com.lenote.qiutu.common.AppConfig;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenote on 2015/9/10.
 */
public class NetworkRequest extends JsonRequest<JSONObject>{

    private Request.Priority mPriority = Request.Priority.HIGH;

    public NetworkRequest(int method, String url,
                          Map<String, String> postParams, Response.Listener<JSONObject> listener,
                          Response.ErrorListener errorListener){
        super(method, url, paramstoString(postParams), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public NetworkRequest(String url, Map<String ,Object> params,
                          Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        this(Request.Method.GET, urlBuilder(url, params), null, listener, errorListener);
    }

    public NetworkRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        this(Request.Method.GET, url, null, listener, errorListener);
    }
    public static String get(String url, Object... params) {

        return HttpRequest.get(AppConfig.HOST_URL + url, true, params)
                .body();
    }

    public static String post(String url, Map<String, Object> params) {
        if (params != null) {
            HttpRequest request = HttpRequest.post(AppConfig.HOST_URL + url);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key == null || value == null) {
                    continue;
                }
                request.part(key, silentURLEncode(String.valueOf(value)));
            }
            return request.body();


        } else {
            return HttpRequest.post(AppConfig.HOST_URL + url)
                    .body();
        }
    }
    private static String paramstoString(Map<String, String> params)
    {
        if (params != null && params.size() > 0){
            String paramsEncoding = "UTF-8";
            StringBuilder encodedParams = new StringBuilder();
            try{
                for (Map.Entry<String, String> entry : params.entrySet()){
                    encodedParams.append(URLEncoder.encode(entry.getKey(),
                            paramsEncoding));
                    encodedParams.append('=');
                    encodedParams.append(URLEncoder.encode(entry.getValue(),
                            paramsEncoding));
                    encodedParams.append('&');

                }
                return encodedParams.toString();
            }catch (UnsupportedEncodingException uee){
                throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
            }
        }
        return null;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response){
        try{
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(response));
        }catch (Exception e){
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Priority getPriority(){
        return mPriority;
    }

    public void setPriority(Priority priority){
        mPriority = priority;
    }

    public static String urlBuilder(String url, Map<String ,Object> params){
        url=HttpRequest.append(url,params);
        return  HttpRequest.encode(url);
    }


    public static String silentURLEncode(String value, String charset) {
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException ignored) {
            return value;
        }
    }

    public static String silentURLEncode(String value) {
        return silentURLEncode(value,"UTF-8");
    }
}
