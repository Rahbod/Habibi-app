package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestList;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.model.ItemRequest;
import ir.rahbod.habibi.model.Request;
import ir.rahbod.habibi.model.RequestList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست های ثبت شده");

        sendRequest();

        //setRecyclerView
        recyclerView = findViewById(R.id.recRequestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

    }

    private void sendRequest() {
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        call.getRequest().enqueue(new Callback<RequestList>() {
            @Override
            public void onResponse(Call<RequestList> call, Response<RequestList> response) {
                if (response.isSuccessful()) {
                    AdapterRequestList adapter = new AdapterRequestList(RequestListActivity.this, response.body().list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<RequestList> call, Throwable t) {

            }
        });
    }
}
