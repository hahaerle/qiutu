package com.lenote.qiutu.utils.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageRequest;
import com.lenote.qiutu.utils.Utils;

import java.util.HashMap;

/**
 * Created by lenote on 2015/9/10.
 */
public class ImageSpecialNetWork extends BasicNetwork {

    private MyDisLruCache myDisLruCache;

    public ImageSpecialNetWork(HttpStack httpStack) {
        super(httpStack);
    }


    public MyDisLruCache getMyDisLruCache() {
        return myDisLruCache;
    }

    public void setMyDisLruCache(MyDisLruCache myDisLruCache) {
        this.myDisLruCache = myDisLruCache;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {

        if (request instanceof ImageRequest) {
            ImageRequest imageRequest = (ImageRequest) request;
            String url = imageRequest.getUrl();
            String key = Utils.toMD5(url);
            if (myDisLruCache != null) {
                byte[] data = myDisLruCache.get(key);
                if (data != null) {
                    return new NetworkResponse(200, data, new HashMap<String, String>(), false);
                } else {
                    NetworkResponse networkResponse = super.performRequest(request);
                    myDisLruCache.put(key, networkResponse.data);
                    return networkResponse;
                }
            }
        }
        return super.performRequest(request);
    }
}