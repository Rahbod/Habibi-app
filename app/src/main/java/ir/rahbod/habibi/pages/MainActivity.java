package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        apiClient = new ApiClient();
        if (SessionManager.getExtrasPref(this).getBoolean(PutKey.REGISTERED)) {
            if (isNetworkConnected()) {
                setContentView(R.layout.activity_main);
                onCreateMain();
            } else {
                setContentView(R.layout.activity_main_offline);
            }
        } else {
            setContentView(R.layout.activity_register_step_one);
            onCreateRegister();
        }
    }

    private void onCreateMain() {
        drawer = findViewById(R.id.drawer);
        dbHelper = new DbHelper(this);
        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        back.setPadding(34, 34, 34, 34);
        ImageView menu = findViewById(R.id.btnMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionListActivity.class);
                startActivity(intent);
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
                }
            }

            @Override
            public void onFailure(Call<DevicesList> call, Throwable t) {
                presenter.showSnack(drawer);
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
        getDevicesList();
    }

    public static class Utility {
        static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 160);
        }
    }
}
