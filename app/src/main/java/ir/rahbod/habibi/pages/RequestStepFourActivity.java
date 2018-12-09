package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestStepFourActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private TextView txtDetails, txtDate, txtTime,
            txtAddress, txtDescription;
    private CardView btnOk;
    private ApiClient apiClient;
    private ImageView btnBack;
    private MySnackBar snackBar;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_four);
        bind();
        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        txtDetails.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TITLE));
        txtDate.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DATE));
        if (SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME).equals("am"))
            txtTime.setText("صبح (ساعت 8 الی 12)");
        else if (SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME).equals("pm"))
            txtTime.setText("بعدازظهر (ساعت 12 الی 18)");
        else
            txtTime.setText("شب (ساعت 18 الی 22)");
        txtAddress.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_Address));
        if (!SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DESCRIPTION).isEmpty())
            txtDescription.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DESCRIPTION));
        else
            txtDescription.setText("شما هیچ توضیحاتی را وارد نکرده اید");
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        txtDetails = findViewById(R.id.txtDetails);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        btnOk = findViewById(R.id.btnOk);
        apiClient = new ApiClient();
        btnBack = findViewById(R.id.btnBack);
        snackBar = new MySnackBar(this);
        layout = findViewById(R.id.mainLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                sendRequest();
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    private void sendRequest() {
        ApiService call = apiClient.getApi();
        final Request request = new Request();
        request.deviceID = SessionManager.getExtrasPref(this).getInt(PutKey.SERVICE_ID);
        request.addressID = SessionManager.getExtrasPref(this).getInt(PutKey.SERVICE_Address_ID);
        request.description = SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DESCRIPTION);
        request.date = SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DATE);
        request.time = SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME);
        call.sendRequest(request).enqueue(new Callback<Request>() {
            @Override
            public void onResponse(Call<Request> call, Response<Request> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(RequestStepFourActivity.this, RequestListActivity.class);
                    startActivity(intent);
                    RequestStepOneActivity.one.finish();
                    RequestStepTowActivity.tow.finish();
                    RequestStepThereActivity.there.finish();
                    finish();
                } else
                    snackBar.snackCustomShow(layout, "خطای اتصال به شبکه، لطفا دوباره امتحان کنید", "تایید");
            }

            @Override
            public void onFailure(Call<Request> call, Throwable t) {
                snackBar.snackCustomShow(layout, "خطای اتصال به شبکه، لطفا دوباره امتحان کنید", "تایید");
            }
        });
    }

    @Override
    public void retry() {

    }
}
