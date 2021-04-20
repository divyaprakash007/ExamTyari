package dp.ibps.generalawareness.Service;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getNoKtification() != null){
//             String title = remoteMessage.getNotification().getTitle();
//             String body = remoteMessage.getNotification().getBody();
//
////            Notification notification = new NotificationCompat().Builder(this, )
//        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
