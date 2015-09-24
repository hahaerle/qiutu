package com.lenote.qiutu.api;

/**
 * Created by lenote on 2015/9/10.
 * 网络访问接口
 */
public interface INetworkListener<T>  {
    void onSuccess(T reponse);
    void onError(int error, String errMsg);
}
