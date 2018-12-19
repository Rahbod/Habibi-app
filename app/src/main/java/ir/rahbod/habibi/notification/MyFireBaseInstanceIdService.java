package ir.rahbod.habibi.notification;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstanceIdService extends FirebaseInstanceIdService {

    private String regToken;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        regToken = FirebaseInstanceId.getInstance().getToken();
    }
}
