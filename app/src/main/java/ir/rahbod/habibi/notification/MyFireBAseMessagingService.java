package ir.rahbod.habibi.notification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import ir.rahbod.habibi.R;
import ir.rahbod.habibi.helper.PutKey;
import ir.rahbod.habibi.pages.RequestInfoActivity;

public class MyFireBAseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFireBAseMessaging";
    private static final String ACTION = "action";
    private static final String SELECT_REPAIR_MAN = "selectRepairMan";
    private static final String INVOICING = "invoicing";
    private static final String MESSAGE = "message";
    private static final String DEVICE_ID = "id";
    private Intent intent;
    private NotificationUtils notificationUtils;
    private NotificationData data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        JSONObject object = new JSONObject(remoteMessage.getData());
        try {
            String action = object.getString(ACTION);
            switch (action) {
                case SELECT_REPAIR_MAN:
                    intent = new Intent(getApplicationContext(), RequestInfoActivity.class);
                    intent.putExtra(PutKey.SERVICE_ID, object.getString(DEVICE_ID));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    notificationUtils = new NotificationUtils(getApplicationContext());
                    data = new NotificationData();
                    data.setDescription(object.getString(MESSAGE));
                    data.setTitle(getResources().getString(R.string.app_name));
                    if (RequestInfoActivity.requestInfo != null)
                        RequestInfoActivity.requestInfo.finish();
                    notificationUtils.showNotificationMessage(data, intent);
                    break;
                case INVOICING:
                    Intent intent2 = new Intent(getApplicationContext(), RequestInfoActivity.class);
                    intent2.putExtra(PutKey.SERVICE_ID, object.getString(DEVICE_ID));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                    notificationUtils = new NotificationUtils(getApplicationContext());
                    data = new NotificationData();
                    data.setDescription(object.getString(MESSAGE));
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
