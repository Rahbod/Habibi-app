package ir.rahbod.habibi.pages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterGetTime;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.GetTime;

public class RequestStepTowActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView txtServiceTitle, date;
    private ImageView icon;
    private RecyclerView recyclerView;
    private LinearLayout btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requset_step_tow);
        bind();

        txtServiceTitle.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TITLE));
        Picasso.with(this).load(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_ICON)).into(icon);
        date.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        List<GetTime> list = new ArrayList<>();
        int a = 8;
        for (int i = 0; i < 3; i++) {
            GetTime time = new GetTime();
            time.startTime = a;
            time.endTime = a + 4;
            a = a + 4;
            list.add(time);
        }
        AdapterGetTime adapter = new AdapterGetTime(this, list);
        recyclerView.setAdapter(adapter);
    }

    private void bind() {
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        txtServiceTitle = findViewById(R.id.txtServiceTitle);
        icon = findViewById(R.id.icon);
        date = findViewById(R.id.date);
        recyclerView = findViewById(R.id.recTime);
        btnOk = findViewById(R.id.btnOk);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                PersianCalendar calendar = new PersianCalendar();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        RequestStepTowActivity.this
                        , calendar.getPersianYear()
                        , calendar.getPersianMonth()
                        , calendar.getPersianDay());
                dialog.show(getFragmentManager(), "Datepickerdialog");
                dialog.setThemeDark(false);
                dialog.setCancelable(true);
                break;
            case R.id.btnOk:
                Intent intent = new Intent(RequestStepTowActivity.this, RequestStepThereActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        String strDate = year + " / " + monthOfYear + " / " + dayOfMonth;
        date.setText(strDate);
        String month = addZero(monthOfYear + "");
        String day = addZero(dayOfMonth + "");
        SessionManager.getExtrasPref(this).putExtra(PutKey.SERVICE_DATE, year + "/" + month + "/" + day);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {

    }

    private String addZero(String data) {
        if (data.length() == 1) {
            data = "0" + data;
        }
        return data;
    }
}
