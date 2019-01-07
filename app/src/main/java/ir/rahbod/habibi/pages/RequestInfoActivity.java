package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

    private TextView txtDevice, txtDate, txtTime, txtAddress, txtDescription, txtStatus,
            txtRepairMan, txtCost, txtInvoiceDescription, txtCostMode, txtTotalDiscount, txtCostDescription;
    private String strDevice, strDate, strTime, strAddress, strDescription, strStatus, strRepairMan, strCost;
    private LinearLayout layout, linTitle2, linRepairMan, linAvatarRepairMan;
    private MySnackBar snackBar;
    private View lineStatus;
    private RecyclerView recyclerView;
    private AdapterFactor adapter;
    private CardView card2, card3, card4;
    public static Activity requestInfo;
    private ImageView btnBack, avatar;

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
        card2 = findViewById(R.id.card_2);
        card3 = findViewById(R.id.card_3);
        card4 = findViewById(R.id.card_4);
        txtCost = findViewById(R.id.txtCost);
        btnBack = findViewById(R.id.btnBack);
        txtInvoiceDescription = findViewById(R.id.txtInvoiceDescription);
        txtCostMode = findViewById(R.id.txtCostMode);
        avatar = findViewById(R.id.avatar);
        txtTotalDiscount = findViewById(R.id.totalDiscount);
        txtCostDescription = findViewById(R.id.txtCostDescription);
        linAvatarRepairMan = findViewById(R.id.linAvatarRepairMan);
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
                    //info
                    strDevice = response.body().deviceTitle;
                    strDate = response.body().requestedDate;
                    strTime = response.body().requestedTime;
                    strAddress = response.body().address;
                    strDescription = response.body().description;
                    strStatus = response.body().status;

                    //repairMan
                    if (response.body().repairMan != null) {
                        linAvatarRepairMan.setVisibility(View.VISIBLE);
                        linRepairMan.setVisibility(View.VISIBLE);
                        strRepairMan = response.body().repairMan.name;
                        if (response.body().repairMan.avatar.equals(""))
                            Picasso.with(RequestInfoActivity.this).load(R.drawable.expertise_icon).transform(new CircleTransform()).into(avatar);
                        else
                            Picasso.with(RequestInfoActivity.this).load(response.body().repairMan.avatar).transform(new CircleTransform()).into(avatar);
                    }

                    //invoice
                    if (response.body().invoice != null) {
                        adapter = new AdapterFactor(RequestInfoActivity.this, response.body().invoice.factors);
                        recyclerView.setAdapter(adapter);
                        strCost = response.body().invoice.cost;
                        txtCostDescription.setText(response.body().invoice.additionalCost);
                        if (response.body().invoice.totalDiscont.equals("0"))
                            card3.setVisibility(View.GONE);
                        if (response.body().invoice.additionalCost.equals("0")) {
                            card2.setVisibility(View.GONE);
                        }
                        if (!response.body().invoice.totalDiscont.equals("0"))
                            txtTotalDiscount.setText(response.body().invoice.totalDiscont);
                        if (!response.body().invoice.description.isEmpty()) {
                            txtInvoiceDescription.setText(response.body().invoice.description);
                        }
                        if (response.body().invoice.paymentMethod.equals("cash"))
                            txtCostMode.setText("روش پرداخت: نقدی");
                        else
                            txtCostMode.setText("روش پرداخت: کارت خوان");
                    } else {
                        linTitle2.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        card4.setVisibility(View.GONE);
                        card2.setVisibility(View.GONE);
                        card3.setVisibility(View.GONE);
                    }

                    //setValue
                    setValue();
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

    private void setValue() {
        txtDevice.setText(strDevice);
        txtDate.setText(strDate);
        txtAddress.setText(strAddress);
        switch (strStatus) {
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
        if (strDescription.isEmpty())
            txtDescription.setText("شما هیچ توضیحاتی وارد نکرده اید");
        else
            txtDescription.setText(strDescription);

        switch (strTime) {
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
        if (strRepairMan == null) {
            linRepairMan.setVisibility(View.GONE);
            txtRepairMan.setVisibility(View.GONE);
            lineStatus.setVisibility(View.GONE);
        } else
            txtRepairMan.setText(strRepairMan);
        if (strCost != null)
            txtCost.setText(strCost);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
