package com.lenote.qiutu.api;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.lenote.qiutu.MyApplication;
import com.lenote.qiutu.utils.volley.VolleyQueueController;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by lenote on 2015/9/10.
 */
public abstract class NetworkHelper<T> implements Response.Listener<JSONObject>, Response.ErrorListener
{
    public static final int CODE_SERVER_ERROR = -1000;
    public static final int CODE_NO_INTERNET = -1001;
    public static final int CODE_TIME_OUT = -1002;
    public static final int RESPONSE_FORMAT_ERROR = -1003;
    public static final int RESPONSE_IS_NULL = -1004;
    public static final int RESPONSE_CODE_ERROR = -1005;
    private static final int CODE_UNLOGIN = -413;


    public static final String SERVER_ERROR_DESC = "服务器错误，请稍后再试";
    public static final String TIME_OUT_DESC = "网络超时，请稍后再试!";
    public static final String NO_INTERNET_DESC = "没有连接互联网";
    private static final String TAG = "NetworkHelper";


    private Context context;

    public NetworkHelper(Context context)
    {
        this.context = context;
    }

    protected Context getContext()
    {
        return context;
    }

    protected NetworkRequest getRequestForGet(String url, Map<String ,Object> params)
    {
        if(params == null)
        {
            return new NetworkRequest(url, this, this);
        }
        else
        {
            return new NetworkRequest(url, params, this, this);
        }

    }

    protected NetworkRequest getRequestForPost(String url, Map<String, String> params){
        return new NetworkRequest(Request.Method.POST, url, params, this, this);
    }

    public void sendGETRequest(Context context,String url, Map<String,Object> params,INetworkListener<T> uiDataListener){
        this.uiDataListener=uiDataListener;
        NetworkRequest request=getRequestForGet(url, params);
        VolleyQueueController.execute(request, context);
    }

    public void sendPostRequest(Context context,String url, Map<String, String> params,INetworkListener<T> uiDataListener){
        this.uiDataListener=uiDataListener;
        NetworkRequest request=getRequestForPost(url, params);
        VolleyQueueController.execute(request, context);
    }
    public void cancelRequest(Context context){
        VolleyQueueController.cancel(context);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        if (error != null) {
            if (error instanceof NoConnectionError) {
                notifyErrorHappened(CODE_NO_INTERNET, NO_INTERNET_DESC);
            } else if (error instanceof NetworkError) {
                notifyErrorHappened(CODE_TIME_OUT, TIME_OUT_DESC);
            }else if (error instanceof AuthFailureError) {
                notifyErrorHappened(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
            } else if (error instanceof ParseError) {
                notifyErrorHappened(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
            } else if (error instanceof TimeoutError) {
                notifyErrorHappened(CODE_TIME_OUT, TIME_OUT_DESC);
            } else {
                notifyErrorHappened(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
            }
        } else {
            notifyErrorHappened(CODE_SERVER_ERROR, SERVER_ERROR_DESC);
        }
    }

    @Override
    public void onResponse(JSONObject response)
    {
        if(response==null){
            notifyErrorHappened(RESPONSE_IS_NULL,"response is null");
        }else {
            Log.e(TAG, "response:" + response.toString());
            try {
                if(checkResult(response)) {
                    T bean = disposeResponse(response);
                    notifyDataChanged(bean);
                }else{
                    //error to show err;
                    int code=getServerErrCode(response);
                    String msg=getServerErrMsg(response);
                    notifyErrorHappened(code, msg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                notifyErrorHappened(RESPONSE_FORMAT_ERROR, "response format err");
            }
        }
    }

    private String getServerErrMsg(JSONObject response) throws JSONException{
        //todo get response msg
        return response.optString("msg");
    }

    protected int getServerErrCode(JSONObject response) throws JSONException{
        //todo get response code
        JSONObject data=response.optJSONObject("data");
        return data.optInt("logcode");
    }
    protected abstract T disposeResponse(JSONObject response) throws JSONException;

    private INetworkListener<T> uiDataListener;


    protected void notifyDataChanged(T data){
        if(uiDataListener != null){
            uiDataListener.onSuccess(data);
        }
    }

    protected void notifyErrorHappened(int errorCode, String errorMessage){
        //toast 提示
        errToast(errorMessage);
        if(errorCode==CODE_UNLOGIN){
            MyApplication.getInstance().logout();
        }else {
            if (uiDataListener != null) {
                uiDataListener.onError(errorCode, errorMessage);
            }
        }
    }

    protected void errToast(String msg){
        MyApplication instance = MyApplication.getInstance();
        if (instance != null&&!TextUtils.isEmpty(msg)) {
            Toast.makeText(instance, msg,
                    Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkResult(JSONObject response) {
        /**
         * msg result time agent data
         */
        //todo check response success
        return TextUtils.equals((response.optString("result")).toUpperCase(), "S");
    }
}
