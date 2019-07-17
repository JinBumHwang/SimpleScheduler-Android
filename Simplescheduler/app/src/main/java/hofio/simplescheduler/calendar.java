package hofio.simplescheduler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class calendar extends Activity {
    TextView yearview, monthview, dayview, textview;
    LinearLayout rayview;
    Button monthbutton;
    int firstday, initYear, initMonth, initDay;
    int dn[] = new int[37];
    int rn[] = new int[37];
    int tn[] = new int[37];
    int sul[]={100214,110203,120123,130210,140131,150219,160208,170128,180216,190205,
            200125,210212,220201,230122,240210,250129,260217,270207,280127,290213,300203};
    int choo[]={100922,110912,120930,130919,140908,150927,160915,171004,180924,190913,
            201001,210921,220910,230929,240917,251006,260925,270915,281003,290922,300912};
    int suk[]={100521,110510,120528,130517,140506,150525,160514,170503,180522,190512,
            200430,210519,220508,230527,240515,250505,260524,270513,280502,290520,300509};
    int sulf[]={100213,110202,120122,130209,140130,150218,160207,170127,180215,190204,
            200124,210211,220131,230121,240209,250128,260216,270206,280126,290212,300202};
    int sulr[]={100215,110204,120124,130211,140201,150220,160209,170129,180217,190206,
            200126,210213,220202,230123,240211,250130,260218,270208,280128,290214,300204};
    int choof[]={100921,110911,120929,130918,140907,150926,160914,171003,180923,190912,
            200930,210920,220909,230928,240916,251005,260924,270914,281002,290921,300911};
    int choor[]={100923,110913,121001,130920,140909,150928,160916,171005,180925,190914,
            201002,210922,220911,230930,240918,251007,260926,270916,281004,290923,300913};
    boolean changedate;
    EditText dlgedit;
    View dialogView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changedate = getIntent().getBooleanExtra("changedate", true);
        if (changedate) {
            setTitle("날짜 변경");
        } else {
            setTitle("디데이 설정");
            dday.number = getIntent().getIntExtra("number", 0);
        }
        setContentView(R.layout.calendar);
        yearview =  findViewById(R.id.year);
        monthview = findViewById(R.id.month);
        monthbutton =  findViewById(R.id.nextmonth);
        monthbutton.setOnClickListener(l);
        monthbutton =  findViewById(R.id.backmonth);
        monthbutton.setOnClickListener(l);

        MainActivity.cYear = getIntent()
                .getIntExtra("year", MainActivity.cYear);
        MainActivity.cMonth = getIntent().getIntExtra("month",
                MainActivity.cMonth);
        MainActivity.cDay = getIntent().getIntExtra("day", MainActivity.cDay);
        initYear = MainActivity.cYear;
        initMonth = MainActivity.cMonth;
        initDay = MainActivity.cDay;

        for (int i = 0; i < 37; i++) {
            dn[i] = getResources().getIdentifier("d" + i / 7 + (i % 7 + 1),
                    "id", getPackageName());
            tn[i] = getResources().getIdentifier("t" + i / 7 + (i % 7 + 1),
                    "id", getPackageName());
            rn[i] = getResources().getIdentifier("r" + i / 7 + (i % 7 + 1),
                    "id", getPackageName());
            rayview =  findViewById(rn[i]);
            rayview.setOnClickListener(l);
        }
        viewmonth();
        // TODO Auto-generated method stub
    }

    Button.OnClickListener l = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.nextmonth:
                    MainActivity.cMonth++;
                    viewmonth();
                    break;
                case R.id.backmonth:
                    MainActivity.cMonth--;
                    viewmonth();
                    break;
                default:
                    for (int i = 0; i < MainActivity.cal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        int j = firstday - 1 + i;
                        if (v.getId() == rn[j]) {
                            if (changedate) {
                                Intent intent = new Intent(calendar.this,MainActivity.class);
                                intent.putExtra("year", MainActivity.cYear);
                                intent.putExtra("month", MainActivity.cMonth);
                                intent.putExtra("day", i + 1);
                                startActivity(intent);
                                finish();
                            } else {
                                final int ii = i + 1;
                                dialogView =(View)View.inflate(calendar.this,R.layout.edit, null);
                                dlgedit =(EditText)dialogView.findViewById(R.id.edittext);
                                MainActivity.filename="　"+ Integer.toString(MainActivity.cYear)+"-"+Integer
                                        .toString(MainActivity.cMonth + 1)+"-"+Integer.toString(ii);
                                MainActivity.str = read(MainActivity.filename);
                                SharedPreferences pref = getSharedPreferences("Preference", 0);// 프리퍼런스
                                if (MainActivity.str != null) {
                                    for (int k = 0; k < 10; k++) {
                                        if (MainActivity.filename.equals(pref.getString("" + k, "")))
                                        {
                                            dlgedit.setText(MainActivity.str);
                                            dday.number = k;
                                            break;
                                        }
                                    }
                                }
                                AlertDialog.Builder dlg = new AlertDialog.Builder(calendar.this);
                                dlg.setTitle("디데이 입력");
                                dlg.setView(dialogView);
                                dlg.setPositiveButton("입력",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,int which) {
                                                SharedPreferences pref = getSharedPreferences("Preference", 0);// 프리퍼런스
                                                SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                                                edit.putString("" + dday.number,MainActivity.filename);// '
                                                edit.commit();
                                                try {
                                                    FileOutputStream outFs = openFileOutput(MainActivity.filename,0);
                                                    MainActivity.str = dlgedit.getText().toString();
                                                    outFs.write(MainActivity.str.getBytes());
                                                    outFs.close();
                                                } catch (Exception e) {
                                                }
                                                Intent intent = new Intent(calendar.this,dday.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                dlg.setNegativeButton("취소", null);
                                dlg.show();
                            }
                        }
                    }
                    break;
            }
        }
    };

    void viewmonth() {
        for (int i = 0; i < 37; i++) {
            dayview =findViewById(dn[i]);
            dayview.setText("");
            textview =findViewById(tn[i]);
            textview.setText("");
            rayview=findViewById(rn[i]);
            rayview.setBackgroundColor(Color.WHITE);
            if (i % 7 == 0) {
                dayview.setTextColor(Color.RED);
            } else if (i % 7 == 6) {
                dayview.setTextColor(Color.BLUE);
            } else {
                dayview.setTextColor(Color.BLACK);
            }
        }
        if (MainActivity.cMonth > 11) {
            MainActivity.cMonth = 0;
            MainActivity.cYear++;
        }
        if (MainActivity.cMonth < 0) {
            MainActivity.cMonth = 11;
            MainActivity.cYear--;
        }
        MainActivity.cal.set(MainActivity.cYear, MainActivity.cMonth, 1);
        yearview.setText("" + MainActivity.cYear);
        monthview.setText("" + (MainActivity.cMonth + 1));
        firstday = MainActivity.cal.get(Calendar.DAY_OF_WEEK);
        Calendar cal2=Calendar.getInstance();
        if(cal2.get(Calendar.YEAR)==MainActivity.cYear && cal2.get(Calendar.MONTH)==MainActivity.cMonth)
        {
            rayview=findViewById(rn[firstday-2+cal2.get(Calendar.DAY_OF_MONTH)]);
            rayview.setBackgroundColor(Color.rgb(200, 255, 211));
        }
        for (int i = 0; i < MainActivity.cal
                .getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayview =findViewById(dn[firstday - 1 + i]);
            dayview.setText("" + (i + 1));
            // 열기
            final int ii = i + 1;
            try {
                String filename = null;
                if (changedate) {
                    filename = Integer.toString(MainActivity.cYear) + "-"
                            + Integer.toString(MainActivity.cMonth + 1) + "-"
                            + Integer.toString(ii);
                } else {
                    filename = "　" + Integer.toString(MainActivity.cYear) + "-"
                            + Integer.toString(MainActivity.cMonth + 1) + "-"
                            + Integer.toString(ii);
                }
                FileInputStream inFs = openFileInput(filename);
                byte[] txt = new byte[45];
                inFs.read(txt);
                inFs.close();
                String memo = (new String(txt)).trim();
                textview =findViewById(tn[firstday - 1 + i]);
                textview.setText(memo);
            } catch (Exception e) {
                // TODO: handle exception
            }
            holiday(i);
        } // end of viewMonth
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_UP) {
            if (changedate) {
                Intent intent = new Intent(calendar.this, MainActivity.class);
                intent.putExtra("year", initYear);
                intent.putExtra("month", initMonth);
                intent.putExtra("day", initDay);
                startActivity(intent);
            }
            finish();
        }
        return false;
    }

    String read(String name)// return filename
    {
        String diaryStr = null;
        try {
            FileInputStream inFs = openFileInput(name);
            byte[] txt = new byte[22];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return diaryStr;
    }

    void holiday(int day)// 월-1,일-1 exempt 석가탄신일,설날,추석
    {
        boolean isholiday = false;
//        for (int y=0;y<sul.length;y++)
//        {
//            if(MainActivity.cYear==2010+y)
//            {
//                isholiday(choof[y],day);
//                isholiday(choo[y],day);
//                isholiday(choor[y],day);
//                isholiday(sulf[y],day);
//                isholiday(sul[y],day);
//                isholiday(sulr[y],day);
//                isholiday(suk[y],day);
//                break;
//            }
//        } delete!!
        int y=MainActivity.cYear-2010;

        if((y>-1 && y<31)&&(isholiday(choof[y],day) ||isholiday(choo[y],day)|| isholiday(choor[y],day)||isholiday(sulf[y],day)||isholiday(sul[y],day)|| isholiday(sulr[y],day)||isholiday(suk[y],day))){
            isholiday=true;
        }
        else if (MainActivity.cMonth == 0 && day == 0) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 2 && day == 0) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 4 && day == 4) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 5 && day == 5) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 7 && day == 14) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 9 && day == 2) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 9 && day == 8) {
            isholiday = true;
        }
        else if (MainActivity.cMonth == 11 && day == 24) {
            isholiday = true;
        }
        Log.d("calendar","/'isholiday="+isholiday);
        if (isholiday) {
            dayview.setTextColor(Color.RED);
        }
    }
    boolean isholiday(int date,int day)
    {
        boolean isholiday=false;
        int holmonth=date/100%100-1;int holday=date%100-1;
        Log.d("calendar","/'holmonth="+holmonth+", holday="+holday);
        if (MainActivity.cMonth == holmonth && day == holday) {
            Log.d("calendar","/'isholiday true");
            isholiday = true;
        }
        return isholiday;
    }
}
// dayview.setText(Html.fromHtml("<Font color="+"#444444"+">"+"a"+"</Font>"));
// 중간글자색변경
