package findhome.com.example.android.findhomeb.workers

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data.size>0){

            val title= remoteMessage.data["title"]
            val mMessage= remoteMessage.data["message"]

            val intent=Intent("findhome.com.example.android.findhomeb_FCM-MESSAGE")
            intent.putExtra("title",title)
            intent.putExtra("message",mMessage)

            val localBroadcastManager=LocalBroadcastManager.getInstance(this)
            localBroadcastManager.sendBroadcast(intent)

        }

    }
}