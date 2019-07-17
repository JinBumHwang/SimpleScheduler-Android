package hofio.simplescheduler;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class widget extends AppWidgetProvider {
    public void onDisabled(Context context) {
        // TODO Auto-generated method stub
        Log.d("widget","/'onDisabled");
    }
    @Override
    public void onEnabled(Context context) {
        // TODO Auto-generated method stub
        Log.d("widget","/'onEnabled");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.d("widget","/'onReceive");
        String action=intent.getAction();
        Log.d("widget","/'intent.gA="+action);
        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
            Log.d("widget","/'APPWIDGET_UPDATE");
            intent = new Intent(context , service.class);
            context.startService(intent);
        }
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        Log.d("widget","/'onDeleted");
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // TODO Auto-generated method stub
        Log.d("widget","/'onUpdate");
    }
}
