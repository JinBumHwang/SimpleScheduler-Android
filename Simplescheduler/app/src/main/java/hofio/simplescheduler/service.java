package hofio.simplescheduler;

import java.io.FileInputStream;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class service extends Service {
    final long HARU=86400000;
    Calendar cal;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        Log.d("service", "/'onBind");
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.d("service", "/'onstartCommand,"+flags+","+startId);
        MainActivity.init();
        MainActivity.str = read(MainActivity.filename(),false);
        dday.cal=Calendar.getInstance();
        update();
        //알람 소스 ==//
        intent = new Intent(this, alert.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent,0);

        // 알람을 받을 시간 설정
        cal=Calendar.getInstance();
        cal.set(MainActivity.cYear, MainActivity.cMonth,(MainActivity.cDay+1),0,0);

        // 알람 매니저에 알람을 등록
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);

        return START_STICKY;
    }
    String read(String name,boolean dday) {//파일 읽기(일정,디데이)
        String diaryStr = null;
        try {
            FileInputStream inFs = openFileInput(name);
            byte[] txt = null;
            if(dday)
            {
                txt=new byte[18];
            }
            else
            {
                txt=new byte[300];
            }
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return diaryStr;
    }

    void update() { //Mainactivity.exit()와 유사
        Log.d("service","/'update()");
        SharedPreferences pref = getSharedPreferences("Preference", 0);
        boolean first = true;
        MainActivity.dstr="";
        long currentcal=System.currentTimeMillis()/HARU;
        for(int i=0;i<10;i++)
        {
            String content=read(pref.getString(""+i, ""),true);//내용 불러오기
            String date=pref.getString(""+i, "");//날짜(파일이름) 불러오기
            if(pref.getLong(i+"v",0)>0 && date.length()!=0)
            {
                String calyear=date.substring(1, 5);
                int m=date.indexOf("-",6);
                String calmonth=date.substring(6,m);
                String calday=date.substring(m+1,date.length());
                dday.cal.set(Integer.parseInt(calyear),Integer.parseInt(calmonth)-1,Integer.parseInt(calday));
                long comparecal=(dday.cal.getTimeInMillis()+6000)/86400000;
                Log.d("service","/'comparecal="+comparecal+"-"+currentcal);
                String compareday=null;
                if(comparecal-currentcal>0)
                {
                    compareday="D-"+(comparecal-currentcal)+" "+content;
                }
                else if(comparecal-currentcal==0)
                {
                    compareday="오늘 "+content;
                }
                else
                {
                    compareday=content+" "+(currentcal-comparecal+1)+"일째";
                }
                if(first)
                {
                    MainActivity.dstr=compareday;
                    first=false;
                }
                else
                {
                    MainActivity.dstr=MainActivity.dstr+",\n"+compareday;
                }
            }
        }
        MainActivity.a = pref.getInt("a", 255);
        MainActivity.b = pref.getInt("b", 255);
        MainActivity.c = pref.getInt("c", 255);
        MainActivity.d = pref.getInt("d", 0);
        MainActivity.e = pref.getInt("e", 0);
        MainActivity.f = pref.getInt("f", 85);
        MainActivity.g = pref.getInt("g", 15);

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.widgetview, MainActivity.str);
        views.setTextViewText(R.id.widgetdday, MainActivity.dstr);
        if(MainActivity.dstr.length()==0)
        {
            views.setInt(R.id.widgetdday,"setVisibility",8);
        }
        else
        {
            views.setInt(R.id.widgetdday,"setVisibility",0);
        }
        views.setTextViewText(R.id.widgetdate, MainActivity.filename+" "+MainActivity.cWeek);
        views.setInt(R.id.widgetview, "setBackgroundColor",
                Color.rgb(MainActivity.a, MainActivity.b, MainActivity.c));
        views.setInt(R.id.widgetview, "setTextColor",
                Color.rgb(MainActivity.d, MainActivity.e, MainActivity.f));
        views.setFloat(R.id.widgetview, "setTextSize", MainActivity.g);
        Intent intent2 = new Intent(service.this, MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(service.this,0, intent2, 0);

        views.setOnClickPendingIntent(R.id.widgetlayout, pendingintent);//위젯을 누를 경우 이동

        AppWidgetManager wm = AppWidgetManager.getInstance(service.this);
        ComponentName widget = new ComponentName(getApplicationContext(),
                widget.class);
        ComponentName widget2 = new ComponentName(getApplicationContext(),
                widget2.class);
        wm.updateAppWidget(widget, views);
        wm.updateAppWidget(widget2, views);
    }//'

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.d("service", "/'destory");
        super.onDestroy();
    }
}
