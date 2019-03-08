package ir.rahbod.habibi.pages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterMain;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.DbHelper;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.DevicesList;
import ir.rahbod.habibi.model.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SnackView, View.OnClickListener {

    public static Activity mainActivity;
    private RecyclerView recyclerView;
    private ApiClient apiClient;
    private DrawerLayout drawer;
    private static long backPressed;
    private CardView btnRequest, btnOk;
    private MySnackBar snackBar;
    private ScrollView layout;
    private boolean showMain = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        apiClient = new ApiClient();
        snackBar = new MySnackBar(this);
        if (SessionManager.getExtrasPref(this).getBoolean(PutKey.REGISTERED)) {
            if (isNetworkConnected()) {
                setContentView(R.layout.activity_main);
                showMain = true;
                onCreateMain();
            } else {
                setContentView(R.layout.activity_main_offline);
                showMain = false;
                onCreateOffline();
            }
        } else {
            setContentView(R.layout.activity_register_step_one);
            onCreateRegister();
        }
    }

    private void onCreateOffline() {
        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        btnRequest = findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1001);
                } else
                    sendMessage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMessage();
            }
        }
    }

    public void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage
                ("30004747473705", null, "1",
                        null, null);
        RelativeLayout layout = findViewById(R.id.mainView);
        MySnackBar snackBar = new MySnackBar(this);
        if (showMain)
            snackBar.snackCustomShow(drawer, "درخواست شما با موفقیت ثبت شد", "تایید");
        else
            snackBar.snackCustomShow(layout, "درخواست شما با موفقیت ثبت شد", "تایید");
    }

    private void onCreateMain() {
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1001);
                } else
                    sendMessage();
            }
        });

        drawer = findViewById(R.id.drawer);
        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        back.setOnClickListener(this);
        ImageView menu = findViewById(R.id.btnMenu);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        getDevicesList();

        // set credit text
        NavigationView navigation = findViewById(R.id.aboutNavView);
        View header = navigation.getHeaderView(0);
        TextView txtCredit = header.findViewById(R.id.txtCredit);
        txtCredit.append(SessionManager.getExtrasPref(this).getString(PutKey.CREDIT_SHOW));
    }

    private void getDevicesList() {
        MyDialog.show(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        ApiService call = apiClient.getApi();
        call.getDevices().enqueue(new Callback<DevicesList>() {
            @Override
            public void onResponse(Call<DevicesList> call, Response<DevicesList> response) {
                if (response.isSuccessful()) {
                    SessionManager.getExtrasPref(MainActivity.this).putExtra(PutKey.CREDIT_SHOW, response.body().showCredit);
                    SessionManager.getExtrasPref(MainActivity.this).putExtra(PutKey.CREDIT, response.body().credit);
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, Utility.calculateNoOfColumns(MainActivity.this)));
                    AdapterMain adapter = new AdapterMain(MainActivity.this, response.body().list, Utility.calculateNoOfColumns(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    MyDialog.dismiss();
                } else {
                    MyDialog.dismiss();
                    snackBar.snackShow(drawer);
                }
            }

            @Override
            public void onFailure(Call<DevicesList> call, Throwable t) {
                MyDialog.dismiss();
                snackBar.snackShow(drawer);
            }
        });
    }

    private void onCreateRegister() {
        layout = findViewById(R.id.mainLayout);
        SessionManager.getExtrasPref(this).putExtra(PutKey.IS_LOGIN, false);
        final Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etGetNumber = findViewById(R.id.etGetNumber);
                if (etGetNumber.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (!checkMobileNumber(etGetNumber.getText().toString()))
                    Toast.makeText(MainActivity.this, "شماره تلفن وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                else {
                    btnOk.setEnabled(false);
                    MyDialog.show(MainActivity.this);
                    ApiService call = apiClient.getApi();
                    Register register = new Register();
                    register.mobile = etGetNumber.getText().toString();
                    call.register(register).enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Call<Register> call, retrofit2.Response<Register> response) {
                            if (response.isSuccessful()) {
                                Intent intent = new Intent(MainActivity.this, CheckCodeActivity.class);
                                intent.putExtra(PutKey.MOBILE, etGetNumber.getText().toString());
                                startActivity(intent);
                                MyDialog.dismiss();
                                btnOk.setEnabled(true);
                            } else {
                                btnOk.setEnabled(true);
                                snackBar.snackCustomShow(layout, "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", "تایید");
                                MyDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Register> call, Throwable t) {
                            btnOk.setEnabled(true);
                            snackBar.snackCustomShow(layout, "خطا در اتصال به شبکه، لطفا مجددا تلاش کنید", "تایید");
                            MyDialog.dismiss();
                        }
                    });
                }
            }
        });
    }


    private boolean checkMobileNumber(String number) {
        return number.matches("^[0][9][0-9]{9}$");
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null) != null;
    }

    @Override
    public void retry() {
        getDevicesList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                startActivity(new Intent(this, RequestListActivity.class));
                break;
        }
    }

    //تقسیم کردن عرض گوشی برای ریسایکلر
    public static class Utility {
        static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 160);
        }
    }

    public void menuItem(View view) {
        switch (view.getId()) {
            case R.id.collaboration:
                startActivity(new Intent(this, IntroductionActivity.class));
                closeDrawer();
                break;
//            case R.id.transaction:
//                startActivity(new Intent(this, TransactionListActivity.class));
//                closeDrawer();
//                break;
            case R.id.cooperation:
                startActivity(new Intent(this, CooperationRequestActivity.class));
                closeDrawer();
                break;
            case R.id.list:
                startActivity(new Intent(this, RequestListActivity.class));
                closeDrawer();
                break;
        }
    }

    private void closeDrawer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.closeDrawer(Gravity.RIGHT);
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        if (showMain)
            if (drawer.isDrawerOpen(Gravity.RIGHT))
                drawer.closeDrawer(Gravity.RIGHT);
            else if (backPressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else {
                Toast.makeText(mainActivity, "لطفا کلید برگشت را مجددا فشار دهید.", Toast.LENGTH_SHORT).show();
                backPressed = System.currentTimeMillis();
            }
        else
            super.onBackPressed();
    }
}
