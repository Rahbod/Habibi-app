package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.UserName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etGetName;
    private Button btnSave;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        bind();

        btnSave.setOnClickListener(this);
    }

    private void bind() {
        etGetName = findViewById(R.id.etGetName);
        btnSave = findViewById(R.id.btnSave);
        apiClient = new ApiClient();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                sendRequest();
        }
    }

    private void sendRequest() {
        ApiService call = apiClient.getApi();
        UserName userName = new UserName();
        userName.setName(etGetName.getText().toString());
        call.setName(userName).enqueue(new Callback<UserName>() {
            @Override
            public void onResponse(Call<UserName> call, Response<UserName> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserNameActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    SessionManager.getExtrasPref(UserNameActivity.this).putExtra(PutKey.REGISTERED, true);
                    Intent intent = new Intent(UserNameActivity.this, MainActivity.class);
                    startActivity(intent);
                    MainActivity.mainActivity.finish();
                    CheckCodeActivity.checkCode.finish();
                    finish();
                } else
                    try {
                        apiClient.getError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFailure(Call<UserName> call, Throwable t) {
                if (t instanceof IOException)
                    Toast.makeText(UserNameActivity.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
