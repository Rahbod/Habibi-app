package ir.rahbod.habibi.notification;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.pages.RequestInfoActivity;

public class MyPushListenerService extends FirebaseMessagingService {
    private static final String ACTION = "action";
    private static final String SELECT_REPAIR_MAN = "selectRepairMan";
    private static final String INVOICING = "invoicing";
    private static final String MESSAGE = "message";
    private static final String DEVICE_ID = "id";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        JSONObject object = new JSONObject(remoteMessage.getData());
        try {
            JSONObject content = new JSONObject(object.getString("custom_content"));
            String action = content.getString(ACTION);
            switch (action) {
                case SELECT_REPAIR_MAN:
                    Intent intent = new Intent(getApplicationContext(), RequestInfoActivity.class);
                    intent.putExtra(PutKey.SERVICE_ID, content.getString(DEVICE_ID));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                    NotifyData data = new NotifyData();
                    data.setDescription(content.getString(MESSAGE));
                    data.setTitle(getResources().getString(R.string.app_name));
                    if (RequestInfoActivity.requestInfo != null)
                        RequestInfoActivity.requestInfo.finish();
                    notificationUtils.showNotificationMessage(data, intent);
                    break;
                case INVOICING:
                    Intent intent2 = new Intent(getApplicationContext(), RequestInfoActivity.class);
                    intent2.putExtra(PutKey.SERVICE_ID, content.getString(DEVICE_ID));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                    notificationUtils = new NotificationUtils(getApplicationContext());
                    data = new NotifyData();
                    data.setDescription(content.getString(MESSAGE));
                    data.setTitle(getResources().getString(R.string.app_name));
                    if (RequestInfoActivity.requestInfo != null)
                        RequestInfoActivity.requestInfo.finish();
                    notificationUtils.showNotificationMessage(data, intent2);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}