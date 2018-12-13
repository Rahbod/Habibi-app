package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.AccessToken;
import ir.rahbod.habibi.model.Authorization;
import ir.rahbod.habibi.model.CheckCode;
import retrofit2.Call;
import retrofit2.Callback;

public class CheckCodeActivity extends AppCompatActivity implements View.OnClickListener, SnackView, TextWatcher {
    private Button btnOk;
    private EditText etGetCodeNumber;
    public static Activity checkCode;
    private ApiClient apiClient;
    private MySnackBar snackBar;
    private ScrollView layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);
        checkCode = this;
        bind();

        //button
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        etGetCodeNumber.addTextChangedListener(this);
    }

    private void bind() {
        etGetCodeNumber = findViewById(R.id.etGetCodeNumber);
        apiClient = new ApiClient();
        snackBar = new MySnackBar(this);
        layout = findViewById(R.id.mainLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                if (etGetCodeNumber.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا کد فعال سازی را وارد کنید", Toast.LENGTH_LONG).show();
                else
                    sendRequest();
                break;
        }
    }

    private void sendRequest() {
        btnOk.setEnabled(false);
        MyDialog.show(this);
        ApiService call = apiClient.getApi();
        CheckCode code = new CheckCode();
        code.mobile = getIntent().getStringExtra(PutKey.MOBILE);
        code.code = etGetCodeNumber.getText().toString();
        call.checkCode(code).enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(Call<Authorization> call, retrofit2.Response<Authorization> response) {
                if (response.isSuccessful()) {
                    Authorization authorization = new Authorization();
                    authorization.auth = response.body().getAuth();
                    authorization.grantType = "access_token";
                    ApiService callToken = apiClient.getApi();
                    callToken.getToken(authorization).enqueue(new Callback<AccessToken>() {
                        @Override
                        public void onResponse(Call<AccessToken> call, retrofit2.Response<AccessToken> response) {
                            if (response.isSuccessful()) {
                                SessionManager.getExtrasPref(CheckCodeActivity.this).setAccessToken(
                                        response.body().token.getAccessToken(),
                                        response.body().token.getRefreshToken(),
                                        response.body().token.getExpireIn()
                                );
                                FirebaseMessaging.getInstance().subscribeToTopic("all");
                                Intent intent = new Intent(CheckCodeActivity.this, UserNameActivity.class);
                                startActivity(intent);
                                MyDialog.dismiss();
                                btnOk.setEnabled(true);
                            } else {
                                MyDialog.dismiss();
                                snackBar.snackShow(layout);
                            }
                        }

                        @Override
                        public void onFailure(Call<AccessToken> call, Throwable t) {
                            MyDialog.dismiss();
                            snackBar.snackShow(layout);
                        }
                    });
                } else {
                    btnOk.setEnabled(true);
                    MyDialog.dismiss();
                    try {
                        apiClient.getError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                MyDialog.dismiss();
                snackBar.snackShow(layout);
            }
        });
    }

    @Override
    public void retry() {
        sendRequest();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        etGetCodeNumber.setGravity(Gravity.CENTER);
    }
}
