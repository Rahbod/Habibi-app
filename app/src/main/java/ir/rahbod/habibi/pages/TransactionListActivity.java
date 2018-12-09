package ir.rahbod.habibi.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterTransaction;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.TransactionList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionListActivity extends AppCompatActivity implements View.OnClickListener, SnackView {

    private RecyclerView recyclerView;
    private ImageView btnBack;
    private RelativeLayout layout;
    private MySnackBar snackBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        bind();
        sendRequest();
        btnBack.setOnClickListener(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("لیست تراکنش ها");
        recyclerView = findViewById(R.id.recTransactionList);
        btnBack = findViewById(R.id.btnBack);
        layout = findViewById(R.id.mainLayout);
        snackBar = new MySnackBar(this);
    }

    private void sendRequest() {
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        call.getTransaction().enqueue(new Callback<TransactionList>() {
            @Override
            public void onResponse(Call<TransactionList> call, Response<TransactionList> response) {
                if (response.isSuccessful()) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(TransactionListActivity.this));
                    AdapterTransaction adapter = new AdapterTransaction(TransactionListActivity.this, response.body().list);
                    recyclerView.setAdapter(adapter);
                } else
                    snackBar.snackShow(layout);
            }

            @Override
            public void onFailure(Call<TransactionList> call, Throwable t) {
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
}
