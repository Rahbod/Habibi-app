package ir.rahbod.habibi.pages;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import ir.map.sdk_map.Mapir;
import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterMain;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.DevicesList;
import ir.rahbod.habibi.model.Register;
import ir.rahbod.habibi.notification.NotificationUtils;
import ir.rahbod.habibi.notification.NotifyData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.pushpole.sdk.NotificationButtonData;
import com.pushpole.sdk.NotificationData;
import com.pushpole.sdk.PushPole;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static final String TAG = "MyFireBAseMessaging";
    private static final String ACTION = "action";
    private static final String SELECT_REPAIR_MAN = "selectRepairMan";
    private static final String INVOICING = "invoicing";
    private static final String MESSAGE = "message";
    private static final String DEVICE_ID = "id";
    private Intent intent;
    private NotificationUtils notificationUtils;
    private NotifyData data;

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

        PushPole.initialize(this,true);

        Mapir.getInstance(this, "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjJlMmFhZmUzZjI3YzE4ODJlNGM5YWEwYWU1OTUzYzQ1MGJjNmVhYWUyOTdmM2JlMTNlNzFjZDUyODdmNzg4NzUxMDM0MDU1NjE0YmYyNmM1In0.eyJhdWQiOiI1MzU2IiwianRpIjoiMmUyYWFmZTNmMjdjMTg4MmU0YzlhYTBhZTU5NTNjNDUwYmM2ZWFhZTI5N2YzYmUxM2U3MWNkNTI4N2Y3ODg3NTEwMzQwNTU2MTRiZjI2YzUiLCJpYXQiOjE1NjY1MDA0MDQsIm5iZiI6MTU2NjUwMDQwNCwiZXhwIjoxNTY5MTM3NDc0LCJzdWIiOiIiLCJzY29wZXMiOlsiYmFzaWMiXX0.QwUzOOMm8uts4QeRBd2aTZ5cMoGLOqGyeLjrxq3HW1GyRwlsYb7opxFNkCYGDUgKnvQtjRaoZs2fo_k7c4gVgdqdiOqmmLX3FKKn5xRLoTBn0eEelmh-GRnbSVxV5ieuLpzdR0Xu0SdtuLcXWI59cZeHTwMwiFtzkU3g_OjKYxhUDuLGgn9i4fgkXsSJ_miJK0ItIDxlclaB17IkSHB9_ftw1hgGFI_aWktLJJun-SIKUoI3hxUTYR7XGBa0cWJF9EbECfg9p9I95seaIXAn9LFUfchuHBUrEmqvDNcBZwgMBfdePPOCsPQ0Hv_maz6LDclHX_kjCAvh7nlVXNYUww");

//        // Pusheh Listener
//        PushPole.setNotificationListener(new PushPole.NotificationListener() {
//            @Override
//            public void onNotificationReceived(@NonNull NotificationData notificationData) {
//                JSONObject object = notificationData.getCustomContent();
//                try {
//                    String action = object.getString(ACTION);
//                    switch (action) {
//                        case SELECT_REPAIR_MAN:
//                            intent = new Intent(getApplicationContext(), RequestInfoActivity.class);
//                            intent.putExtra(PutKey.SERVICE_ID, object.getString(DEVICE_ID));
//                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                            notificationUtils = new NotificationUtils(getApplicationContext());
//                            data = new NotifyData();
//                            data.setDescription(object.getString(MESSAGE));
//                            data.setTitle(getResources().getString(R.string.app_name));
//                            if (RequestInfoActivity.requestInfo != null)
//                                RequestInfoActivity.requestInfo.finish();
//                            notificationUtils.showNotificationMessage(data, intent);
//                            break;
//                        case INVOICING:
//                            Intent intent2 = new Intent(getApplicationContext(), RequestInfoActivity.class);
//                            intent2.putExtra(PutKey.SERVICE_ID, object.getString(DEVICE_ID));
//                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
//                            notificationUtils = new NotificationUtils(getApplicationContext());
//                            data = new NotifyData();
//                            data.setDescription(object.getString(MESSAGE));
//                            data.setTitle(getResources().getString(R.string.app_name));
//                            if (RequestInfoActivity.requestInfo != null)
//                                RequestInfoActivity.requestInfo.finish();
//                            notificationUtils.showNotificationMessage(data, intent2);
//                            break;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onNotificationClicked(@NonNull NotificationData notificationData) {
//                // click
//            }
//            @Override
//            public void onNotificationButtonClicked(@NonNull NotificationData notificationData, @NonNull NotificationButtonData clickedButton) {
//                // button click
//            }
//            @Override
//            public void onCustomContentReceived(@NonNull JSONObject customContent) {
//                // custom content (JSON) received
//            }
//            @Override
//            public void onNotificationDismissed(@NonNull NotificationData notificationData) {
//                // dismissed
//            }
//        });
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
