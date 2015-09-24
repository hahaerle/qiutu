package com.lenote.qiutu.utils.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lenote.qiutu.MyApplication;
import com.lenote.qiutu.utils.Utils;

/**
 * Created by lenote on 2015/9/10.
 */
public class VolleyQueueController {

    private static final Object TAG = new Object();
    static VolleyQueueController instance;

    public static synchronized VolleyQueueController getInstance() {
        if(instance==null){
            instance=new VolleyQueueController();
        }
        return instance;
    }

    private final RequestQueue mRequestQueue;
    private final ImageLoader mImageLoader;

    private VolleyQueueController() {
        MyApplication instance = MyApplication.getInstance();
        HttpStack stack = new HurlStack();

        ImageSpecialNetWork imageSpecialNetWork = new ImageSpecialNetWork(stack);
        MyDisLruCache disLruCache = new MyDisLruCache(instance,
                instance.getPackageCodePath(), 30 * 1024 * 1024);
        imageSpecialNetWork.setMyDisLruCache(disLruCache);

        mRequestQueue = new RequestQueue(
                new DiskBasedCache(Utils.getCacheDirectory(instance, "volley")),
                imageSpecialNetWork
        );
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
    }


    public static RequestQueue getRequestQueue(){
        return getInstance().mRequestQueue;
    }

    public static ImageLoader getImageLoader(){
        return getInstance().mImageLoader;
    }

    public static void execute(Request request){
        execute(request,TAG);
    }

    public static void execute(Request request,Object tag){
        request.setTag(tag);
        getRequestQueue().add(request);
    }

    public static void cancel(){
        cancel(TAG);
    }

    public static void cancel(Object tag){
        getRequestQueue().cancelAll(tag);
    }
    public static void setImageUrl(NetworkImageView imageView, String imgUrl){
        imageView.setImageUrl(imgUrl,getImageLoader());
    }
    public static void setImageUrl(NetworkImageView imageView, String imgUrl,int defaultResId){
        imageView.setDefaultImageResId(defaultResId);
        setImageUrl(imageView,imgUrl);
    }
}
