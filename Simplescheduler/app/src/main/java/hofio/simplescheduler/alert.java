package hofio.simplescheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class alert extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.d("alert","/'onReceive");
        intent=new Intent(context,service.class);
        context.startService(intent);
    }

}
