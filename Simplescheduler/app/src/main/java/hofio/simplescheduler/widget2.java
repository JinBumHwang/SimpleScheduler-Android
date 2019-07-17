package hofio.simplescheduler;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class widget2 extends AppWidgetProvider {
    public void onDisabled(Context context) {
        // TODO Auto-generated method stub
        Log.d("widget2","/'onDisabled");
    }
    @Override
    public void onEnabled(Context context) {
        // TODO Auto-generated method stub
        Log.d("widget2","/'onEnabled");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.d("widget2","/'onReceive");
        String action=intent.getAction();
        Log.d("widget2","/'intent.gA="+action);
        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
            Log.d("widget2","/'APPWIDGET_UPDATE");
            intent = new Intent(context , service.class);
            context.startService(intent);
        }
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        Log.d("widget2","/'onDeleted");
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // TODO Auto-generated method stub
        Log.d("widget2","/'onUpdate");
    }
}