package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestList;
import ir.rahbod.habibi.model.Request;

public class RequestListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("فاکتور");

        //setRecyclerView
        RecyclerView recyclerView = findViewById(R.id.recRequestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Request> list = new ArrayList<>();
        Request request = new Request();
        for (int i = 0; i < 9; i++){
            request.setCondition("بررسی شده");
            list.add(request);
        }
        AdapterRequestList adapter = new AdapterRequestList(this, list);
        recyclerView.setAdapter(adapter);
    }
}
