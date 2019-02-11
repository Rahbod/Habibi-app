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

import com.squareup.picasso.Picasso;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterFactor;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.CircleTransform;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.ItemRequest;
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
    private CardView card4;
    public static Activity requestInfo;
    private ImageView btnBack, avatar;
    private String repairManID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        requestInfo = this;
        bind();
        btnBack.setOnClickListener(this);
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
                    setValue(response.body());
                } else {
                    snackBar.snackShow(layout);
                    MyDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RequestInfo> call, Throwable t) {
                MyDialog.dismiss();
                snackBar.snackShow(layout);
            }
        });
    }

    private void setValue(RequestInfo info) {
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
        } else {
            linTitle2.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            card4.setVisibility(View.GONE);
        }
        txtSum.setText(info.invoice.sum);
        txtDiscount.setText(info.invoice.discount);
        txtDiscountPercent.setText(info.invoice.discountPercent);
        txtFinalCost.setText(info.invoice.finalCost);
        txtDevice.setText(info.deviceTitle);
        txtDate.setText(info.requestedDate);
        txtAddress.setText(info.requestedTime);
        switch (info.status) {
            case "7":
            case "6":
                txtStatus.setText("پرداخت شده");
                txtStatus.setTextColor(getResources().getColor(R.color.green));
                break;
            case "5":
                txtStatus.setText("در انتظار پرداخت");
                txtStatus.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case "4":
            case "3":
                txtStatus.setText("در صف سرویس");
                txtStatus.setTextColor(getResources().getColor(R.color.blue));
                break;
            case "2":
            case "1":
                txtStatus.setText("در انتظار بررسی");
                txtStatus.setTextColor(getResources().getColor(R.color.gray2));
                break;
            case "-1":
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
        if (info.repairMan.name == null) {
            linRepairMan.setVisibility(View.GONE);
            txtRepairMan.setVisibility(View.GONE);
            lineStatus.setVisibility(View.GONE);
        } else {
            txtRepairMan.setText(info.repairMan.name);
            txtRepairManCode.setText("کد: " + info.repairMan.code);
        }
        if (info.invoice.cost != null)
            txtCost.setText(info.invoice.cost);
        MyDialog.dismiss();
    }

    @Override
    public void retry() {
        sendRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendRequest();
        showComment();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    public void repairMan(View view) {
        Intent intent = new Intent(this, RepairManInfoActivity.class);
        intent.putExtra(PutKey.REPAIR_MAN_ID, repairManID);
        startActivity(intent);
    }

    public void showRate(RequestInfo info){
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.rate_dialog, null);
        ImageView imgAvatar = view.findViewById(R.id.imgAvatar);
        TextView txtName = view.findViewById(R.id.txtName);
        Picasso.with(this).load(info.repairMan.avatar).transform(new CircleTransform()).into(imgAvatar);
        txtName.setText(info.repairMan.name);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public void showComment(){
        Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.comment_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
}
