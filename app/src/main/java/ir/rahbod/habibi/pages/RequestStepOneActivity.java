package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;

public class RequestStepOneActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtServiceTitle;
    private ImageView icon, btnBack;
    private EditText etGEtDescription;
    private CardView btnOk;
    public static Activity one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_step_one);
        bind();
        txtServiceTitle.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TITLE));
        Picasso.with(this).load(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_ICON)).into(icon);
        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void bind() {
        one = this;
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        txtServiceTitle = findViewById(R.id.txtServiceTitle);
        icon = findViewById(R.id.icon);
        etGEtDescription = findViewById(R.id.etGEtDescription);
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                Intent intent = new Intent(this, RequestStepTowActivity.class);
                SessionManager.getExtrasPref(this).putExtra(PutKey.SERVICE_DESCRIPTION, etGEtDescription.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DESCRIPTION).isEmpty())
            SessionManager.getExtrasPref(this).remove(PutKey.SERVICE_DESCRIPTION);
    }
}
