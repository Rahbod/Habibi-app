package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterFactor;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.CircleTransform;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.Invoice;
import ir.rahbod.habibi.model.ItemRequest;
import ir.rahbod.habibi.model.Payment;
import ir.rahbod.habibi.model.RequestInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestInfoActivity extends AppCompatActivity implements SnackView, View.OnClickListener {

    private TextView txtDevice, txtDate, txtTime, txtAddress, txtDescription, txtStatus, txtRepairManCode,
            txtRepairMan, txtCost, txtSum, txtDiscount, txtDiscountPercent, txtFinalCost;
    private LinearLayout layout, linTitle2, linRepairMan, linAvatarRepairMan;
    private MySnackBar snackBar;
    private View lineStatus;
    private RecyclerView recyclerView;
    private AdapterFactor adapter;
    private CardView card4, cardPayment, btnCancel;
    public static Activity requestInfo;
    private ImageView btnBack, avatar;
    private String repairManID;
    private TextView payCard, payPos;
    private Invoice invoice;
    private boolean payment, getOrder;
    private String paymentMethod;
    private int requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        requestInfo = this;
        bind();
        btnBack.setOnClickListener(this);
        payCard.setOnClickListener(this);
        payPos.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("فاکتور");
        txtDevice = findViewById(R.id.txtDetails);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtAddress = findViewById(R.id.txtAddress);
        txtDescription = findViewById(R.id.txtDescription);
        txtStatus = findViewById(R.id.txtStatus);
        txtRepairMan = findViewById(R.id.txtRepairMan);
        layout = findViewById(R.id.mainLayout);
        linTitle2 = findViewById(R.id.linTitle2);
        linRepairMan = findViewById(R.id.linRepairMan);
        lineStatus = findViewById(R.id.lineStatus);
        snackBar = new MySnackBar(RequestInfoActivity.this);
        recyclerView = findViewById(R.id.recBill);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        card4 = findViewById(R.id.card_4);
        txtCost = findViewById(R.id.txtCost);
        btnBack = findViewById(R.id.btnBack);
        avatar = findViewById(R.id.avatar);
        linAvatarRepairMan = findViewById(R.id.linAvatarRepairMan);
        txtRepairManCode = findViewById(R.id.txtRepairManCode);
        txtSum = findViewById(R.id.txtSum);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtDiscountPercent = findViewById(R.id.txtDiscountPercent);
        txtFinalCost = findViewById(R.id.txtFinalCost);
        payCard = findViewById(R.id.payCash);
        payPos = findViewById(R.id.payPos);
        cardPayment = findViewById(R.id.cardPayment);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void sendRequest() {
        MyDialog.show(this);
        final ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.deviceID = Integer.parseInt(getIntent().getStringExtra(PutKey.SERVICE_ID));
        call.getDevicesInfo(itemRequest).enqueue(new Callback<RequestInfo>() {
            @Override
            public void onResponse(Call<RequestInfo> call, Response<RequestInfo> response) {
                if (response.isSuccessful()) {
                    invoice = response.body().invoice;
                    setValue(response.body());
                } else {
                    getOrder = true;
                    snackBar.snackShow(layout);
                    MyDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RequestInfo> call, Throwable t) {
                getOrder = true;
                MyDialog.dismiss();
                snackBar.snackShow(layout);
            }
        });
    }

    private void setValue(RequestInfo info) {
        requestID = info.ID;
        //repairMan
        if (info.repairMan != null) {
            repairManID = info.repairMan.code;
            linAvatarRepairMan.setVisibility(View.VISIBLE);
            linRepairMan.setVisibility(View.VISIBLE);
            if (info.repairMan.avatar.equals(""))
                Picasso.with(RequestInfoActivity.this).load(R.drawable.expertise_icon).transform(new CircleTransform()).into(avatar);
            else
                Picasso.with(RequestInfoActivity.this).load(info.repairMan.avatar).transform(new CircleTransform()).into(avatar);
        }

        //invoice
        if (info.invoice != null) {
            adapter = new AdapterFactor(RequestInfoActivity.this, info.invoice.factors);
            recyclerView.setAdapter(adapter);
            txtSum.setText(info.invoice.sum);
            txtDiscount.setText(info.invoice.discount);
            if (!info.invoice.discount.equals("0"))
                txtDiscountPercent.setText(info.invoice.discountPercent);
            txtFinalCost.setText(info.invoice.finalCost);
        } else {
            linTitle2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            card4.setVisibility(View.GONE);
        }
        txtDevice.setText(info.deviceTitle);
        txtDate.setText(info.requestedDate);
        txtAddress.setText(info.address);
        switch (info.status) {
            case "7":
            case "6":
                txtStatus.setText("پرداخت شده");
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                cardPayment.setVisibility(View.GONE);
                break;
            case "5":
                txtStatus.setText("در انتظار پرداخت");
                txtStatus.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case "4":
            case "3":
                txtStatus.setText("در صف سرویس");
                txtStatus.setTextColor(getResources().getColor(R.color.blue));
                btnCancel.setVisibility(View.VISIBLE);
                break;
            case "2":
            case "1":
                txtStatus.setText("در انتظار بررسی");
                txtStatus.setTextColor(getResources().getColor(R.color.gray2));
                btnCancel.setVisibility(View.VISIBLE);
                break;
            case "-1":
            case "-2":
                txtStatus.setText("لغو شده");
                txtStatus.setTextColor(getResources().getColor(R.color.secondColor));
                break;
        }
        if (info.description.isEmpty())
            txtDescription.setText("شما هیچ توضیحاتی وارد نکرده اید");
        else
            txtDescription.setText(info.description);

        switch (info.requestedTime) {
            case "am":
                txtTime.setText("صبح (ساعت 8 الی 12)");
                break;
            case "pm":
                txtTime.setText("بعدازظهر (ساعت 12 الی 18)");
                break;
            case "night":
                txtTime.setText("شب (ساعت 18 الی 22)");
                break;
        }
        if (info.repairMan == null) {
            linRepairMan.setVisibility(View.GONE);
            txtRepairMan.setVisibility(View.GONE);
            lineStatus.setVisibility(View.GONE);
        } else {
            txtRepairMan.setText(info.repairMan.name);
            txtRepairManCode.setText("کد: " + info.repairMan.code);
        }
        MyDialog.dismiss();
    }

    @Override
    public void retry() {
        if (payment) {
            paymentInvoice(paymentMethod);
            payment = false;
        } else if (getOrder) {
            getOrder = false;
            sendRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
            case R.id.payCash:
                paymentInvoice("cash");
                break;
            case R.id.payPos:
                paymentInvoice("pos");
                break;
            case R.id.btnCancel:
                cancelRequest();
        }
    }

    private void cancelRequest() {
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.ID = requestID;
        call.cancelRequest(requestInfo).enqueue(new Callback<RequestInfo>() {
            @Override
            public void onResponse(Call<RequestInfo> call, Response<RequestInfo> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RequestInfoActivity.this, "" + response.body().message, Toast.LENGTH_LONG).show();
                    txtStatus.setText("لغو شده");
                    txtStatus.setTextColor(getResources().getColor(R.color.secondColor));
                    btnCancel.setVisibility(View.GONE);
                } else {
                    Toast.makeText(RequestInfoActivity.this, "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RequestInfo> call, Throwable t) {
                Toast.makeText(RequestInfoActivity.this, "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void paymentInvoice(final String method) {
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        Payment pay = new Payment();
        pay.id = invoice.id;
        pay.method = method;
        call.paymentInvoice(pay).enqueue(new Callback<Payment>() {
            @Override
            public void onResponse(Call<Payment> call, Response<Payment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RequestInfoActivity.this, response.body().message, Toast.LENGTH_LONG).show();
                    cardPayment.setVisibility(View.GONE);
                } else {
                    payment = true;
                    paymentMethod = method;
                    snackBar.snackShow(layout);
                }
            }

            @Override
            public void onFailure(Call<Payment> call, Throwable t) {
                payment = true;
                paymentMethod = method;
                snackBar.snackShow(layout);
            }
        });
    }

    public void repairMan(View view) {
        Intent intent = new Intent(this, RepairManInfoActivity.class);
        intent.putExtra(PutKey.REPAIR_MAN_ID, repairManID);
        startActivity(intent);
    }

    public void showRate(RequestInfo info) {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.rate_dialog, null);
        ImageView imgAvatar = view.findViewById(R.id.imgAvatar);
        TextView txtName = view.findViewById(R.id.txtName);
        Picasso.with(this).load(info.repairMan.avatar).transform(new CircleTransform()).into(imgAvatar);
        txtName.setText(info.repairMan.name);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showComment() {
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.comment_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
