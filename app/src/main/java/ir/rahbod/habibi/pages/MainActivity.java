package ir.rahbod.habibi.pages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import ir.rahbod.habibi.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_offline);

        //change back icon
        ImageView back = findViewById(R.id.btnBack);
        back.setImageResource(R.drawable.wrench_icon);
        back.setPadding(34, 34, 34, 34);

        ImageView menu = findViewById(R.id.btnMenu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "rr", Toast.LENGTH_SHORT).show();

            }
        });

//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        List<Categories> list = new ArrayList<>();
//        Categories categories = new Categories();
//        for (int i = 0; i<10; i++){
//            categories.setTitle("تست" + i);
//            categories.setUri("http://rozanehonline.com/wp-content/uploads/2018/02/%D8%AF%D8%A7%D9%86%D9%84%D9%88%D8%AF-%D8%B9%DA%A9%D8%B3-%D9%86%D9%88%D8%B4%D8%AA%D9%87-%D9%87%D8%A7%DB%8C-%D8%B2%DB%8C%D8%A8%D8%A7-43.jpg");
//            list.add(categories);
//        }
//        AdapterMain adapter = new AdapterMain(this, list);
//        recyclerView.setAdapter(adapter);
    }
}
