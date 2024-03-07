//package id.ottopay.kasir.services
//
//import android.support.v4.app.NotificationCompat
//import android.support.v4.app.NotificationManagerCompat
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import id.ottopay.kasir.R
//
//class FirebaseNotificationService : FirebaseMessagingService() {
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
//        super.onMessageReceived(remoteMessage)
//
//        val mBuilder = NotificationCompat.Builder(this, "4CHAN")
//                .setSmallIcon(R.mipmap.ic_launchera)
//                .setBadgeIconType(R.mipmap.ic_launchera)
////                .setContentTitle(remoteMessage!!.data?.get("title"))
////                .setContentText(remoteMessage!!.data?.get("body"))
//                .setContentTitle(remoteMessage!!.data?.get("title"))
//                .setContentText(remoteMessage!!.data?.get("body"))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        val notificationManager = NotificationManagerCompat.from(this)
//        notificationManager.notify(774477, mBuilder.build())
//    }
//}
