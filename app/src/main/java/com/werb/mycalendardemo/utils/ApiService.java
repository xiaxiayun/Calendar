package com.werb.mycalendardemo.utils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *����ӿڣ���װURL��ַ���������� �ӽӿڴ�ŵ�����
 * Created by acer-pc on 2016/3/10.
 */
public interface ApiService {

    public static String SERVICE_URL=GlobalContants.SERVER_URL;

    @GET("{downUrl}")
    Call<ResponseBody> dowmApk(@Path("downUrl") String url);
}
