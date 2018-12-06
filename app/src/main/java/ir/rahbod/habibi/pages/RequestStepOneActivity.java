package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;

public class RequestStepOneActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtServiceTitle;
    private ImageView icon;
    private EditText etGEtDescription;
    private LinearLayout btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_one);
        bind();
        txtServiceTitle.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TITLE));
        Picasso.with(this).load(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_ICON)).into(icon);
        btnOk.setOnClickListener(this);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        txtServiceTitle = findViewById(R.id.txtServiceTitle);
        icon = findViewById(R.id.icon);
        etGEtDescription = findViewById(R.id.etGEtDescription);
        btnOk = findViewById(R.id.btnOk);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                Intent intent = new Intent(this, RequestStepTowActivity.class);
                SessionManager.getExtrasPref(this).putExtra(PutKey.SERVICE_DESCRIPTION, etGEtDescription.getText().toString());
                startActivity(intent);
        }
    }
}
