package ir.rahbod.habibi.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {

    private String regToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        regToken = FirebaseInstanceId.getInstance().getToken();
        sendRegTokenToServer();
    }

    private void sendRegTokenToServer() {
        final ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        TokenModel token = new TokenModel();
        token.regToken = regToken;
        call.sendRegToken(token).enqueue(new Callback<TokenModel>() {
            @Override
            public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
                Log.e("qqqq", "onResponse: " + response.code() );
                if (response.isSuccessful()) {
                    Log.e("qqqq", "onResponse: true");
                } else{
                    try {
                        apiClient.getError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sendRegTokenToServer();}
            }

            @Override
            public void onFailure(Call<TokenModel> call, Throwable t) {
                Log.e("qqqq", "onFailure: " + t.getMessage() );
                sendRegTokenToServer();
            }
        });
    }
}
