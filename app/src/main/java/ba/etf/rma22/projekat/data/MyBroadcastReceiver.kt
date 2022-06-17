package ba.etf.rma22.projekat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import ba.etf.rma22.projekat.data.models.InternetConnection

class MyBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        InternetConnection.prisutna=true
        val cm=context.getSystemService(Context.CONNECTIVITY_SERVICE)as ConnectivityManager
        val activeNetwork = cm.activeNetwork
        val capabilities = cm.getNetworkCapabilities(activeNetwork)
        if (capabilities != null) {
            InternetConnection.prisutna=true
            val toast = Toast.makeText(context, "Connected", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            InternetConnection.prisutna=false
            val toast = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}