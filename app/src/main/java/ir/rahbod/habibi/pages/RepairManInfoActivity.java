package ir.rahbod.habibi.pages;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.api.ApiClient;
import ir.rahbod.habibi.api.ApiService;
import ir.rahbod.habibi.helper.CircleTransform;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.helper.snackBar.MySnackBar;
import ir.rahbod.habibi.helper.snackBar.SnackView;
import ir.rahbod.habibi.model.RepairManInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepairManInfoActivity extends AppCompatActivity implements SnackView {

    private TextView txtCode, txtName, txtMobile, txtExpertise, txtExperience, txtDescription;
    private MySnackBar snackBar;
    private LinearLayout layout;
    private ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_man_info);
        bind();
        sendRequest();
    }

    private void bind() {
        avatar = findViewById(R.id.avatar);
        txtCode = findViewById(R.id.txtCode);
        snackBar = new MySnackBar(this);
        layout = findViewById(R.id.mainLayout);
        txtMobile = findViewById(R.id.txtMobile);
        txtExpertise = findViewById(R.id.txtExpertise);
        txtExperience = findViewById(R.id.txtExperience);
        txtDescription = findViewById(R.id.txtDescription);
        txtName = findViewById(R.id.txtName);
    }

    private void sendRequest() {
        ApiClient apiClient = new ApiClient();
        ApiService call = apiClient.getApi();
        String ID = getIntent().getStringExtra(PutKey.REPAIR_MAN_ID);
        call.getRepairManInfo(ID).enqueue(new Callback<RepairManInfo>() {
            @Override
            public void onResponse(Call<RepairManInfo> call, Response<RepairManInfo> response) {
                if (response.isSuccessful()) {
                    setValue(response.body());
                } else {
                    snackBar.snackShow(layout);
                }
            }

            @Override
            public void onFailure(Call<RepairManInfo> call, Throwable t) {
                snackBar.snackShow(layout);
            }
        });
    }

    private void setValue(RepairManInfo repairManInfo) {
        txtCode.setText(repairManInfo.getCode());
        txtName.setText(repairManInfo.getName());
        txtMobile.setText(repairManInfo.getMobile());
        txtExpertise.setText(repairManInfo.getExpertise());
        txtExperience.setText(repairManInfo.getExperience());
        txtDescription.setText(repairManInfo.getDescription());
        Picasso.with(this).load(repairManInfo.getAvatar()).transform(new CircleTransform()).into(avatar);
    }

    @Override
    public void retry() {
        sendRequest();
    }
}
