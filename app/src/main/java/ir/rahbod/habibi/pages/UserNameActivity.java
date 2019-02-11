package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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
import ir.rahbod.habibi.model.UserName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private EditText etGetName, etGEtReagent;
    private Button btnSave;
    private ApiClient apiClient;
    private ScrollView layout;
    private MySnackBar snackBar;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        bind();

        btnSave.setOnClickListener(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        }
                    }
                });
    }

    private void bind() {
        etGetName = findViewById(R.id.etGetName);
        etGEtReagent = findViewById(R.id.etGetReagent);
        btnSave = findViewById(R.id.btnRegister);
        apiClient = new ApiClient();
        layout = findViewById(R.id.mainLayout);
        snackBar = new MySnackBar(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                if (etGetName.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا نام خود را وارد کنید", Toast.LENGTH_SHORT).show();
                else
                    sendRequest();
        }
    }

    private void sendRequest() {
        btnSave.setEnabled(false);
        MyDialog.show(this);
        ApiService call = apiClient.getApi();
        UserName userName = new UserName();
        userName.name = (etGetName.getText().toString().trim());
        userName.regToken = token;
        if (!etGEtReagent.getText().toString().trim().isEmpty()) {
            if (!etGEtReagent.getText().toString().trim().substring(0, 2).equals("AR")) {
                Toast.makeText(this, "کد معرف صحیح نمی باشد", Toast.LENGTH_LONG).show();
                MyDialog.dismiss();
                btnSave.setEnabled(true);
            }else {
                userName.reagent = etGEtReagent.getText().toString().trim();
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
                            btnSave.setEnabled(true);
                        } else {
                            MyDialog.dismiss();
                            snackBar.snackShow(layout);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserName> call, Throwable t) {
                        MyDialog.dismiss();
                        snackBar.snackShow(layout);
                    }
                });
            }
        } else
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
                        btnSave.setEnabled(true);
                    } else {
                        MyDialog.dismiss();
                        snackBar.snackShow(layout);
                    }
                }

                @Override
                public void onFailure(Call<UserName> call, Throwable t) {
                    MyDialog.dismiss();
                    snackBar.snackShow(layout);
                }
            });
    }

    @Override
    public void retry() {
        sendRequest();
    }
}
