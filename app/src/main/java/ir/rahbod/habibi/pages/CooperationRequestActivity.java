package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.Cooperation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CooperationRequestActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private ImageView btnBack;
    private CardView btnOk;
    private EditText userName, expertise, experience, phone;
    private MySnackBar snackBar;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooperation_request);
        bind();
        btnBack.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        snackBar = new MySnackBar(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست همکاری");
        btnBack = findViewById(R.id.btnBack);
        btnOk = findViewById(R.id.btnOk);
        userName = findViewById(R.id.userName);
        expertise = findViewById(R.id.expertise);
        experience = findViewById(R.id.experience);
        phone = findViewById(R.id.phone);
        layout = findViewById(R.id.mainLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.btnOk:
                if (userName.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا نام و نام خانوادگی خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (expertise.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا تخصص خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (experience.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا میزان تجربه خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (phone.getText().toString().trim().isEmpty())
                    Toast.makeText(this, "لطفا تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                else
                    sendRequest();
                break;
        }
    }

    private void sendRequest() {
        MyDialog.show(this);
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        Cooperation cooperation = new Cooperation();
        cooperation.name = userName.getText().toString().trim();
        cooperation.expertise = expertise.getText().toString().trim();
        cooperation.experience = experience.getText().toString().trim();
        cooperation.phone = phone.getText().toString().trim();
        call.sendCooperation(cooperation).enqueue(new Callback<Cooperation>() {
            @Override
            public void onResponse(Call<Cooperation> call, Response<Cooperation> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CooperationRequestActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    MyDialog.dismiss();
                    userName.setText("");
                    expertise.setText("");
                    experience.setText("");
                    phone.setText("");
                }else {
                    snackBar.snackShow(layout);
                    MyDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Cooperation> call, Throwable t) {
                snackBar.snackShow(layout);
                MyDialog.dismiss();
            }
        });
    }

    @Override
    public void retry() {
        sendRequest();
    }
}
