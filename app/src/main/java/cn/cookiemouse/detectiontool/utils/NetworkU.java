package cn.cookiemouse.detectiontool.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NetworkU {

    private static final String TAG = "NetworkU";

    private static final int TIMEOUT = 30;

    private OkHttpClient mOkHttpClient;

    public NetworkU() {
        this.mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    //  调用请求
    public void connectUrl(String tag, String url, HashMap<String, String> parameter, Callback callback) {
        Log.i(TAG, "connectUrl: url-->" + url);
        if (!RegularU.isWebAddress(url)) {
            url = "http://" + url;
        }

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : parameter.keySet()) {
            builder.add(key, parameter.get(key));
            Log.i(TAG, "connectUrl: parameter.key-->" + key + ", value-->" + parameter.get(key));
        }

        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .post(builder.build())
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
