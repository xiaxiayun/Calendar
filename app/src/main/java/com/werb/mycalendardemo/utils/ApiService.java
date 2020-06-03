package com.werb.mycalendardemo.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *定义接口（封装URL地址和数据请求） 子接口存放的内容
 * Created by acer-pc on 2016/3/10.
 */
public interface ApiService {

    public static String SERVICE_URL=GlobalContants.SERVER_URL;

    @GET("{downUrl}")
    Call<ResponseBody> dowmApk(@Path("downUrl") String url);
}
