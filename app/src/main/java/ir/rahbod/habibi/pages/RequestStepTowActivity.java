package ir.rahbod.habibi.pages;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.adapter.AdapterGetTime;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.SessionManager;
import ir.rahbod.habibi.model.GetTime;

public class RequestStepTowActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView txtServiceTitle, date, titleDate, titleTime;
    private ImageView icon, btnBack;
    private RecyclerView recyclerView;
    private CardView btnOk;
    private List<GetTime> list;
    private int mainDay, mainMonth, mainYear;
    public static Activity tow;
    private boolean checkTime = false;
    private AdapterGetTime adapter;
    private PersianCalendar mainCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requset_step_tow);
        bind();

        txtServiceTitle.setText(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TITLE));
        Picasso.with(this).load(SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_ICON)).into(icon);
        date.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setListTime();
    }

    private void setListTime() {
        list = new ArrayList<>();
        GetTime timeAm = new GetTime();
        timeAm.startTime = 8;
        timeAm.endTime = 12;
        timeAm.setCheckTime(false);
        list.add(timeAm);
        GetTime timePm = new GetTime();
        timePm.startTime = 12;
        timePm.endTime = 18;
        timePm.setCheckTime(false);
        list.add(timePm);
        GetTime timeNight = new GetTime();
        timeNight.startTime = 18;
        timeNight.endTime = 22;
        timeNight.setCheckTime(false);
        list.add(timeNight);
        Collections.reverse(list);
    }

    private void bind() {
        tow = this;
        //Set Text Title
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText("درخواست سرویس");
        txtServiceTitle = findViewById(R.id.txtServiceTitle);
        icon = findViewById(R.id.icon);
        date = findViewById(R.id.date);
        recyclerView = findViewById(R.id.recTime);
        btnOk = findViewById(R.id.btnOk);
        btnBack = findViewById(R.id.btnBack);
        titleDate = findViewById(R.id.titleDate);
        titleTime = findViewById(R.id.titleTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                mainCalendar = new PersianCalendar();
                DatePickerDialog dialog = DatePickerDialog.newInstance(
                        RequestStepTowActivity.this
                        , mainCalendar.getPersianYear()
                        , mainCalendar.getPersianMonth()
                        , mainCalendar.getPersianDay());
                mainDay = mainCalendar.getPersianDay();
                mainMonth = mainCalendar.getPersianMonth();
                mainYear = mainCalendar.getPersianYear();
                dialog.show(getFragmentManager(), "Datepickerdialog");
                dialog.setThemeDark(false);
                dialog.setCancelable(true);
                break;
            case R.id.btnOk:
                if (SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DATE).isEmpty()
                        || date.getText().toString().isEmpty()) {
                    titleDate.setError("لطفا تاریخ مورد نظر را انتخاب کنید");
                    titleDate.setTextColor(getResources().getColor(R.color.red));
                } else if (SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME).isEmpty()) {
                    titleDate.setTextColor(getResources().getColor(R.color.mainColor));
                    titleTime.setTextColor(getResources().getColor(R.color.red));
                    titleDate.setError(null);
                    titleTime.setError("لطفا زمان مورد نظر را انتخاب کنید");
                } else {
                    titleDate.setTextColor(getResources().getColor(R.color.mainColor));
                    titleTime.setTextColor(getResources().getColor(R.color.mainColor));
                    titleDate.setError(null);
                    titleTime.setError(null);
                    Intent intent = new Intent(RequestStepTowActivity.this, RequestStepThereActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        PersianCalendar calendar = new PersianCalendar();
        calendar.setPersianDate(year, monthOfYear, dayOfMonth);
        if (!mainCalendar.before(calendar)) {
            Toast.makeText(this, "تاریخ انتخاب شده صحیح نمی باشد", Toast.LENGTH_SHORT).show();
        } else if (("" + mainYear + mainMonth + mainDay).equals("" + year + monthOfYear + dayOfMonth)) {
            checkTime = true;
            if (!SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME).isEmpty()) {
                SessionManager.getExtrasPref(this).remove(PutKey.SERVICE_TIME);
            }
            monthOfYear = monthOfYear + 1;
            String strDate = year + " / " + monthOfYear + " / " + dayOfMonth;
            date.setText(strDate);
            String month = addZero(monthOfYear + "");
            String day = addZero(dayOfMonth + "");
            SessionManager.getExtrasPref(this).putExtra(PutKey.SERVICE_DATE, year + "/" + month + "/" + day);
            setListTime();
            adapter = new AdapterGetTime(this, list, checkTime);
            recyclerView.setAdapter(adapter);
            checkTime = false;
        } else {
            monthOfYear = monthOfYear + 1;
            String strDate = year + " / " + monthOfYear + " / " + dayOfMonth;
            date.setText(strDate);
            String month = addZero(monthOfYear + "");
            String day = addZero(dayOfMonth + "");
            SessionManager.getExtrasPref(this).putExtra(PutKey.SERVICE_DATE, year + "/" + month + "/" + day);
            setListTime();
            adapter = new AdapterGetTime(this, list, checkTime);
            recyclerView.setAdapter(adapter);
            checkTime = false;
        }
    }

    private boolean checkDate(int year, int monthOfYear, int dayOfMonth) {
        if (year >= mainYear)
            if (monthOfYear >= mainMonth)
                return year >= mainYear && monthOfYear >= mainMonth && dayOfMonth >= mainDay;
        return false;
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

    @Override
    protected void onResume() {
        super.onResume();
        date.setText("");
        date.setHint("تاریخ حضور سرویس کار");
        adapter = new AdapterGetTime(this, list, checkTime);
        recyclerView.setAdapter(adapter);
        if (!SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_DATE).isEmpty()
                || !SessionManager.getExtrasPref(this).getString(PutKey.SERVICE_TIME).isEmpty()) {
            SessionManager.getExtrasPref(this).remove(PutKey.SERVICE_TIME);
            SessionManager.getExtrasPref(this).remove(PutKey.SERVICE_DATE);
        }
    }
}
