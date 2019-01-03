package ir.rahbod.habibi.api;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ir.rahbod.habibi.controller.MyApp;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.AccessToken;
import ir.rahbod.habibi.model.Authorization;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private ApiService apiService;
    private static final String CLIENT_ID = "78750f0bea1bd9acd7ff1b07bc21c066";
    private static final String CLIENT_SECRET = "a74b4aa72bdfc21ec893fa86edd13384";
    private static final String BASE_URL = "http://www.rahbodvps.ir/";

    public ApiClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder();
                builder.addHeader("Content-Type", "application/json");
                if (SessionManager.getExtrasPref(MyApp.context).checkLogin() && !tokenIsExpired()) {
                    builder.addHeader("Authorization", "Bearer " + SessionManager.getExtrasPref(MyApp.context).getAccessToken());
                } else {
                    builder.addHeader("Authorization", getHeaders());
                }
                builder.method(original.method(), original.body());
                Request request = builder.build();
                return chain.proceed(request);
            }
        });
        okHttpClient.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                Authorization auth = new Authorization();
                auth.refresh_token = SessionManager.getExtrasPref(MyApp.context).getRefreshToken();
                auth.grantType = "refresh_token";
                Call<AccessToken> call = apiService.getToken(auth);
                retrofit2.Response<AccessToken> tokenResponse = call.execute();
                if (tokenResponse.isSuccessful()) {
                    SessionManager.getExtrasPref(MyApp.context).updateAccessToken(
                            tokenResponse.body().token.accessToken,
                            tokenResponse.body().token.expireIn
                    );
                    return response.request().newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer " + SessionManager.getExtrasPref(MyApp.context).getAccessToken())
                            .build();
                } else
                    return null;
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApi() {
        return apiService;
    }

    private String getHeaders() {
        String token = Base64.encodeToString(
                (CLIENT_ID + ":" + CLIENT_SECRET).getBytes(), Base64.NO_WRAP);
        return "Basic " + token;
    }

    private boolean tokenIsExpired() {
        long now = System.currentTimeMillis() / 1000;
        long lastTimeGetToken = SessionManager.getExtrasPref(MyApp.context).getLong(PutKey.TOKEN_EXPIRE_TIME);
        return lastTimeGetToken < now;
    }

    public void getError(String str) {
        JSONObject object;
        try {
            object = new JSONObject(str);
            String message = object.getString("message");
            Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
