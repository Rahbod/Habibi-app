package ir.rahbod.habibi.pages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterTransaction;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.model.TransactionList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);
        bind();
        sendRequest();
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("لیست تراکنش ها");
        recyclerView = findViewById(R.id.recTransactionList);
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
                }
            }
            @Override
            public void onFailure(Call<TransactionList> call, Throwable t) {

            }
        });
    }
}
