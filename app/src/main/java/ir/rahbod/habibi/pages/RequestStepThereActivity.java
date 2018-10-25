package ir.rahbod.habibi.pages;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterRequestStepThere;
import ir.rahbod.habibi.model.Address;

public class RequestStepThereActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_there);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");

        //Add Address
        LinearLayout btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(RequestStepThereActivity.this);
                View dialogView = LayoutInflater.from(RequestStepThereActivity.this).inflate(R.layout.dialog_request_3, null);
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recRequestStepThere);
        List<Address> list = new ArrayList<>();
        Address address = new Address();
        for(int i = 0; i<12; i++){
            address.setAddress("کیوانفر، کوچه 14، پلاک 53، واحد 7");
            list.add(address);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdapterRequestStepThere adapter = new AdapterRequestStepThere(this, list);
        recyclerView.setAdapter(adapter);
    }
}
