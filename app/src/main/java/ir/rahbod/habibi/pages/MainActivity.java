package ir.rahbod.habibi.pages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterMain;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.DbHelper;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.DevicesList;
import ir.rahbod.habibi.model.Register;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SnackView {

    public static Activity mainActivity;
    private RecyclerView recyclerView;
    private ApiClient apiClient;
    private DbHelper dbHelper;
    private DrawerLayout drawer;
    private static long backPressed;
    private boolean online;
    private CardView btnRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        apiClient = new ApiClient();
        if (SessionManager.getExtrasPref(this).getBoolean(PutKey.REGISTERED)) {
            if (isNetworkConnected()) {
                setContentView(R.layout.activity_main);
                online = true;
                onCreateMain();
            } else {
                setContentView(R.layout.activity_main_offline);
                online = false;
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
//        smsManager.sendTextMessage
//                ("30004747473705", null, "1",
//                        null, null);
        RelativeLayout layout = findViewById(R.id.mainView);
        MySnackBar snackBar = new MySnackBar(this);
        btnRequest.setEnabled(false);
        snackBar.snackCustomShow(layout, "درخواست شما با موفقیت ثبت شد", "تایید");
    }

    private void onCreateMain() {
        drawer = findViewById(R.id.drawer);
        dbHelper = new DbHelper(this);
        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        ImageView menu = findViewById(R.id.btnMenu);
        menu.setVisibility(View.VISIBLE);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        getDevicesList();
    }

    private void getDevicesList() {
        final MySnackBar presenter = new MySnackBar(this);
        recyclerView = findViewById(R.id.recyclerView);
        ApiService call = apiClient.getApi();
        call.getDevices().enqueue(new Callback<DevicesList>() {
            @Override
            public void onResponse(Call<DevicesList> call, Response<DevicesList> response) {
                if (response.isSuccessful()) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, Utility.calculateNoOfColumns(MainActivity.this)));
                    AdapterMain adapter = new AdapterMain(MainActivity.this, response.body().list, Utility.calculateNoOfColumns(MainActivity.this));
                    recyclerView.setAdapter(adapter);
                    for (int i = 0; i < response.body().list.size(); i++) {
                        dbHelper.addDevices(response.body().list.get(i).title
                                , response.body().list.get(i).id);
                    }
                } else presenter.snackShow(drawer);
            }

            @Override
            public void onFailure(Call<DevicesList> call, Throwable t) {
                presenter.snackShow(drawer);
            }
        });
    }

    private void onCreateRegister() {
        SessionManager.getExtrasPref(this).putExtra(PutKey.IS_LOGIN, false);
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText etGetNumber = findViewById(R.id.etGetNumber);
                if (etGetNumber.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, "لطفا شماره تلفن خود را وارد کنید", Toast.LENGTH_LONG).show();
                else if (!checkMobileNumber(etGetNumber.getText().toString()))
                    Toast.makeText(MainActivity.this, "شماره تلفن وارد شده صحیح نمی باشد", Toast.LENGTH_LONG).show();
                else {
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
                            } else
                                try {
                                    apiClient.getError(response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(Call<Register> call, Throwable t) {
                            if (t.getMessage().equals("timeout"))
                                Toast.makeText(MainActivity.this, "سرور با مشکل مواجه شده است، لطفا بعدا دوباره امتحان کنید", Toast.LENGTH_LONG).show();
                            else if (t instanceof IOException)
                                Toast.makeText(MainActivity.this, "دستگاه شما به اینترنت دسترسی ندارد", Toast.LENGTH_SHORT).show();
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
        if (online)
            getDevicesList();
    }

    public static class Utility {
        static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 160);
        }
    }

    public void menuItem(View view) {
        switch (view.getId()) {
            case R.id.home:
                drawer.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.transaction:
                startActivity(new Intent(this, TransactionListActivity.class));
                closeDrawer();
                break;
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
        if (drawer.isDrawerOpen(Gravity.RIGHT))
            drawer.closeDrawer(Gravity.RIGHT);
        else if (backPressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else {
            Toast.makeText(mainActivity, "لطفا کلید برگشت را مجددا فشار دهید.", Toast.LENGTH_SHORT).show();
            backPressed = System.currentTimeMillis();
        }
    }
}
