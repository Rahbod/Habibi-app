package ir.rahbod.habibi.pages;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestStepThere;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.model.Address;
import ir.rahbod.habibi.model.AddressList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestStepThereActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayout btnAddAddress;
    private Dialog dialog;
    private EditText etAddress, etPhone;
    private ApiClient apiClient;
    private ApiService call;
    private LinearLayout btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_there);
        bind();

        //send Request
        getAddress();
        btnAddAddress.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    private void bind() {
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        recyclerView = findViewById(R.id.recRequestStepThere);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnAddAddress = findViewById(R.id.btnAddAddress);
        apiClient = new ApiClient();
        call = apiClient.getApi();
        btnOk = findViewById(R.id.btnOk);
    }

    private void getAddress() {
        call.getAddress().enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                if (response.isSuccessful()) {
                    AdapterRequestStepThere adapter = new AdapterRequestStepThere(
                            RequestStepThereActivity.this, response.body().list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<AddressList> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddAddress:
                showDialog();
                break;
            case R.id.btnCancel:
                dialog.dismiss();
                break;
            case R.id.btnAdd:
                sendAddress(etAddress, etPhone);
                break;
            case R.id.btnOk:
                Intent intent = new Intent(this, RequestStepFourActivity.class);
                startActivity(intent);
        }
    }

    private void showDialog() {
        dialog = new Dialog(RequestStepThereActivity.this);
        View dialogView = LayoutInflater.from(RequestStepThereActivity.this).inflate(R.layout.dialog_request_3, null);
        dialog.setContentView(dialogView);
        dialog.setCancelable(false);
        etAddress = dialogView.findViewById(R.id.etGetAddress);
        etPhone = dialogView.findViewById(R.id.etGetTelephone);
        Button btnOk = dialogView.findViewById(R.id.btnAdd);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void sendAddress(EditText etAddress, EditText etPhone) {
        Address address = new Address();
        address.address = etAddress.getText().toString();
        address.telephone = etPhone.getText().toString();
        call.addAddress(address).enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                if (response.isSuccessful())
                    getAddress();
                else {
                    try {
                        apiClient.getError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressList> call, Throwable t) {
                if (t instanceof IOException)
                    Toast.makeText(RequestStepThereActivity.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
