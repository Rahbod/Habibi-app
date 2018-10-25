package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterBill;
import ir.rahbod.habibi.model.Bill;

public class BillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("فاکتور");

        RecyclerView recyclerView = findViewById(R.id.recBill);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Bill> list = new ArrayList<>();
        Bill bill = new Bill();
        for (int i = 0; i<30; i++){
            bill.setTitle("تعویض تستی");
            bill.setCost("20000 تومان");
            list.add(bill);
        }
        AdapterBill adapter = new AdapterBill(this, list);
        recyclerView.setAdapter(adapter);

        LinearLayout btnPayment = findViewById(R.id.btnPayment);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BillActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
