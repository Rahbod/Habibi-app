package ir.rahbod.habibi.pages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestList;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.MyDialog;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.RequestList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private RecyclerView recyclerView;
    private ImageView btnBack;
    private LinearLayout layout, linTitle;
    private MySnackBar snackBar;
    private TextView txtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        bind();
        snackBar = new MySnackBar(RequestListActivity.this);
        btnBack.setOnClickListener(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست های ثبت شده");
        btnBack = findViewById(R.id.btnBack);
        //setRecyclerView
        recyclerView = findViewById(R.id.recRequestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        layout = findViewById(R.id.mainView);
        linTitle = findViewById(R.id.linTitle);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    private void sendRequest() {
        MyDialog.show(this);
        final ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        call.getRequest().enqueue(new Callback<RequestList>() {
            @Override
            public void onResponse(Call<RequestList> call, Response<RequestList> response) {
                if (response.isSuccessful()) {
                    if (response.body().list.isEmpty()) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        linTitle.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.INVISIBLE);
                        linTitle.setVisibility(View.VISIBLE);
                    }
                    Collections.reverse(response.body().list);
                    AdapterRequestList adapter = new AdapterRequestList(RequestListActivity.this, response.body().list);
                    recyclerView.setAdapter(adapter);
                    MyDialog.dismiss();
                } else {
                    MyDialog.dismiss();
                    snackBar.snackShow(layout);
                }
            }

            @Override
            public void onFailure(Call<RequestList> call, Throwable t) {
                MyDialog.dismiss();
                snackBar.snackShow(layout);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                onBackPressed();
                break;
        }
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
}
