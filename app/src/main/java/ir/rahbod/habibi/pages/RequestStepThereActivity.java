package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestStepThere;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.DbHelper;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.Address;
import ir.rahbod.habibi.model.AddressList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestStepThereActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private RecyclerView recyclerView;
    private CardView btnAddAddress;
    private Dialog dialog;
    private EditText etAddress, etPhone;
    private ApiService call;
    private CardView btnOk;
    private ImageView btnBack;
    private List<Address> addressList;
    private MySnackBar snackBar;
    private LinearLayout layout, linTitle;
    private ApiClient apiClient;
    private TextView txtEmpty;
    public static Activity there;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_there);
        bind();

        btnAddAddress.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void bind() {
        there = this;
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        recyclerView = findViewById(R.id.recRequestStepThere);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        apiClient = new ApiClient();
        call = apiClient.getApi();
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);
        snackBar = new MySnackBar(this);
        layout = findViewById(R.id.mainLayout);
        linTitle = findViewById(R.id.linTitle);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    private void getAddress() {
        if (!MyDialog.dialog.isShowing())
            MyDialog.show(this);
        call.getAddress().enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                if (response.isSuccessful()) {
                    if (response.body().list.isEmpty()) {
                        linTitle.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    } else {
                        linTitle.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.INVISIBLE);
                    }
                    addressList = new ArrayList<>();
                    addressList.addAll(response.body().list);
                    Collections.reverse(addressList);
                    AdapterRequestStepThere adapter = new AdapterRequestStepThere(
                            RequestStepThereActivity.this, addressList);
                    recyclerView.setAdapter(adapter);
                    MyDialog.dismiss();
                } else {
                    MyDialog.dismiss();
                    snackBar.snackShow(layout);
                }
            }

            @Override
            public void onFailure(Call<AddressList> call, Throwable t) {
                MyDialog.dismiss();
                snackBar.snackShow(layout);
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
                if (etAddress.getText().toString().trim().isEmpty())
                    etAddress.setError("لطفا آدرس خود را وارد کنید");
                else if (etPhone.getText().toString().trim().isEmpty()) {
                    etAddress.setError(null);
                    etPhone.setError("لطفا تلفن خود راوارد کنید");
                } else {
                    etAddress.setError(null);
                    etPhone.setError(null);
                    addAddress(etAddress, etPhone);
                }
                break;
            case R.id.btnOk:
                if (addressList.isEmpty())
                    Toast.makeText(this, "لطفا آدرس خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_Address).isEmpty())
                    Toast.makeText(this, "لطفا آدرس خود را انتخاب کنید", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(this, RequestStepFourActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
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

    private void addAddress(EditText etAddress, EditText etPhone) {
        btnAddAddress.setEnabled(false);
        MyDialog.show(this);
        Address address = new Address();
        address.address = etAddress.getText().toString();
        address.telephone = etPhone.getText().toString();
        call.addAddress(address).enqueue(new Callback<AddressList>() {
            @Override
            public void onResponse(Call<AddressList> call, Response<AddressList> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    getAddress();
                    btnAddAddress.setEnabled(true);
                } else {
                    try {
                        apiClient.getError(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddressList> call, Throwable t) {
                MyDialog.dismiss();
                Toast.makeText(RequestStepThereActivity.this, "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //send Request
        addressList = new ArrayList<>();
        getAddress();
        AdapterRequestStepThere adapter = new AdapterRequestStepThere(
                RequestStepThereActivity.this, addressList);
        recyclerView.setAdapter(adapter);
        if (!SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_Address).isEmpty())
            SessionManager.getExtrasPref(this).remove(PutKey.SERVICE_Address);
    }

    @Override
    public void retry() {
        getAddress();
    }
}
