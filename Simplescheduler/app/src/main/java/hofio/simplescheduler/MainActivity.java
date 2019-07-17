package hofio.simplescheduler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    static final long HARU = 86400000;
    static Calendar cal;
    static int cYear, cMonth, cDay;
    static String cWeek;
    static String filename, str, dstr;
    static int a, b, c, d, e, f, g;
    TextView preview;
    EditText edit;
    View dialogView;
    RadioButton radio1, radio2, radio3, radio4, radio5;
    SeekBar seek1, seek2, seek3;
    Button btncolortest;
    boolean backcolor;
    SeekBar.OnSeekBarChangeListener cl = new SeekBar.OnSeekBarChangeListener() {//시크바 리스너
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            if (backcolor) {
                btncolortest.setBackgroundColor(Color.rgb(seek1.getProgress(),
                        seek2.getProgress(), seek3.getProgress()));
            } else {
                btncolortest.setTextColor(Color.rgb(seek1.getProgress(),
                        seek2.getProgress(), seek3.getProgress()));
            }
        }
        public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        }

        public void onStartTrackingTouch(SeekBar arg0) {
        }
    };
    Button.OnClickListener l = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.btnclr) {
                edit.setText("");
            }
            if (v.getId() == R.id.btncal) {
                Intent intent = new Intent(MainActivity.this, calendar.class);
                intent.putExtra("year", cYear);
                intent.putExtra("month", cMonth);
                intent.putExtra("day", cDay);
                intent.putExtra("changedate", true);
                startActivity(intent);
                exit(); //달력 보기
            }
            if (v.getId() == R.id.btnsize) {
                dialogView = (View) View.inflate(MainActivity.this,
                        R.layout.size, null);
                // 현재클래스,보여줄 레이아웃,뷰그룹
                radio1 =  dialogView.findViewById(R.id.size1);
                radio2 =  dialogView.findViewById(R.id.size2);
                radio3 =  dialogView.findViewById(R.id.size3);
                radio4 =  dialogView.findViewById(R.id.size4);
                radio5 =  dialogView.findViewById(R.id.size5);
                switch (g) {
                    case 15:
                        radio2.setChecked(true);
                        break;
                    case 18:
                        radio3.setChecked(true);
                        break;
                    case 21:
                        radio4.setChecked(true);
                        break;
                    case 24:
                        radio5.setChecked(true);
                        break;
                    default:
                        radio1.setChecked(true);
                        break;
                }
                AlertDialog.Builder dlg = new AlertDialog.Builder(
                        MainActivity.this);
                dlg.setTitle("글자 크기 수정");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (radio2.isChecked()) {
                            g = 15;
                        } else if (radio3.isChecked()) {
                            g = 18;
                        } else if (radio4.isChecked()) {
                            g = 21;
                        } else if (radio5.isChecked()) {
                            g = 24;
                        } else {
                            g = 12;
                        }
                        SharedPreferences pref = getSharedPreferences(
                                "Preference", 0);// 프리퍼런스
                        SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                        edit.putInt("g", g);
                        edit.commit();
                        preview.setTextSize(g);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
                //글자 크기 수정
            }
            if (v.getId() == R.id.btnclose) {
                exit();
                //닫기
            }
            if (v.getId() == R.id.btnback) {
                backcolor = true;
                colorchange();
                //배경색
            }
            if (v.getId() == R.id.btncolor) {
                backcolor = false;
                colorchange();
                //글자색
            }
            if (v.getId() == R.id.btndday) {
                Intent intent = new Intent(MainActivity.this, dday.class);
                startActivity(intent);
                //디데이 보기
            }
            if (v.getId() == R.id.btnhelp) {
                AlertDialog.Builder help = new AlertDialog.Builder(MainActivity.this);
                help.setCancelable(false);
                help.setMessage("◆홈 화면에 위젯 등록 방법\n1.홈 화면 길게 누르기\n2.위젯 추가\n3.(2x2)또는(2x4) 선택 \n\n※ 위젯에 오늘 날짜, D-day, 일정이 표시됩니다.");
                help.setNegativeButton("닫기", null);
                help.show();
            }
        }
    };

    static void init()// 달력 초기화(오늘)
    {
        cal = Calendar.getInstance();
        cYear = cal.get(Calendar.YEAR);
        cMonth = cal.get(Calendar.MONTH);
        cDay = cal.get(Calendar.DAY_OF_MONTH);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                cWeek = "일";
                break;
            case 2:
                cWeek = "월";
                break;
            case 3:
                cWeek = "화";
                break;
            case 4:
                cWeek = "수";
                break;
            case 5:
                cWeek = "목";
                break;
            case 6:
                cWeek = "금";
                break;
            default:
                cWeek = "토";
                break;
        }
    }

    static String filename() { // 파일 이름 설정
        filename = Integer.toString(cYear) + "-" + Integer.toString(cMonth + 1)
                + "-" + Integer.toString(cDay);
        return filename;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
        dday.cal = Calendar.getInstance();
        cYear = getIntent().getIntExtra("year", cYear);
        cMonth = getIntent().getIntExtra("month", cMonth);
        cDay = getIntent().getIntExtra("day", cDay);//인텐트 값 가져오기/
        cal.set(cYear, cMonth, cDay);//달력 설정/
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                cWeek = "일";
                break;
            case 2:
                cWeek = "월";
                break;
            case 3:
                cWeek = "화";
                break;
            case 4:
                cWeek = "수";
                break;
            case 5:
                cWeek = "목";
                break;
            case 6:
                cWeek = "금";
                break;
            default:
                cWeek = "토";
                break;
        }
        TextView date =findViewById(R.id.date);
        date.setText(cYear + "." + (cMonth + 1) + "." + cDay + "." + cWeek);
        edit =findViewById(R.id.text);
        Button btnsav, btncal, btnsize, btnback, btncolor, btnclose, btndday, btnhelp;
        btnsav =findViewById(R.id.btnclr);
        btnsav.setOnClickListener(l);
        btncal =findViewById(R.id.btncal);
        btncal.setOnClickListener(l);
        btnsize =findViewById(R.id.btnsize);
        btnsize.setOnClickListener(l);
        btnclose =findViewById(R.id.btnclose);
        btnclose.setOnClickListener(l);
        btnback =findViewById(R.id.btnback);
        btnback.setOnClickListener(l);
        btncolor =findViewById(R.id.btncolor);
        btncolor.setOnClickListener(l);
        btndday =findViewById(R.id.btndday);
        btndday.setOnClickListener(l);
        btnhelp =findViewById(R.id.btnhelp);
        btnhelp.setOnClickListener(l);
        preview =findViewById(R.id.preview);

        str = read(filename(), false);//파일 읽기
        edit.setText(str);

        SharedPreferences pref = getSharedPreferences("Preference", 0);
        a = pref.getInt("a", 255);
        b = pref.getInt("b", 255);
        c = pref.getInt("c", 255);
        d = pref.getInt("d", 0);
        e = pref.getInt("e", 0);
        f = pref.getInt("f", 85);
        g = pref.getInt("g", 15);//프리퍼런스 값 가져오기
        preview.setBackgroundColor(Color.rgb(a, b, c));
        preview.setTextColor(Color.rgb(d, e, f));
        preview.setTextSize(g);
        preview.setText(str);
        edit.addTextChangedListener(new TextWatcher() {//텍스트 리스너
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                Log.d("Mainactivity", "/'ontextchanged");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                Log.d("Mainactivity", "/'beforetextchanged");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Log.d("Mainactivity", "/'afterontextchanged");
                preview.setText(edit.getText().toString());
            }
        });
    }

    String read(String name, boolean dday)//파일 읽기 (내용 반환)
    {
        String diaryStr = null;
        try {
            FileInputStream inFs = openFileInput(name);
            byte[] txt = null;
            if (dday) {
                txt = new byte[18];
            } else {
                txt = new byte[300];
            }
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return diaryStr;
    }

    void save()// 저장
    {
        try {
            filename();
            FileOutputStream outFs = openFileOutput(filename, 0);
            str = edit.getText().toString();
            if (str.length() != 0) {
                outFs.write(str.getBytes());
            } else {
                deleteFile(filename);
            }
            outFs.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    void colorchange() { //색깔 바꾸기 (배경 or 컬러)
        dialogView = (View) View.inflate(MainActivity.this, R.layout.color,
                null);
        seek1 =  dialogView.findViewById(R.id.seek1);
        seek2 =  dialogView.findViewById(R.id.seek2);
        seek3 =  dialogView.findViewById(R.id.seek3);
        btncolortest =dialogView.findViewById(R.id.btncolortest);
        btncolortest.setBackgroundColor(Color.rgb(a, b, c));
        btncolortest.setText("" + edit.getText().toString());
        btncolortest.setTextSize(g);
        btncolortest.setTextColor(Color.rgb(d, e, f));
        seek1.setProgress(backcolor ? a : d);
        seek1.setOnSeekBarChangeListener(cl);
        seek2.setProgress(backcolor ? b : e);
        seek2.setOnSeekBarChangeListener(cl);
        seek3.setProgress(backcolor ? c : f);
        seek3.setOnSeekBarChangeListener(cl);
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setTitle(backcolor ? "배경 색깔 설정" : "글자 색깔 설정");
        dlg.setView(dialogView);
        dlg.setPositiveButton("확인", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                SharedPreferences pref = getSharedPreferences("Preference", 0);// 프리퍼런스
                SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                if (backcolor) {
                    a = seek1.getProgress();
                    b = seek2.getProgress();
                    c = seek3.getProgress();
                    preview.setBackgroundColor(Color.rgb(a, b, c));
                } else {
                    d = seek1.getProgress();
                    e = seek2.getProgress();
                    f = seek3.getProgress();
                    preview.setTextColor(Color.rgb(d, e, f));
                }
                edit.putInt(backcolor ? "a" : "d", backcolor ? a : d);
                edit.putInt(backcolor ? "b" : "e", backcolor ? b : e);
                edit.putInt(backcolor ? "c" : "f", backcolor ? c : f);
                edit.commit();
            }
        });
        dlg.setNegativeButton("취소", null);
        dlg.show();
    }

    void exit()//중료
    {
        save();
        init();
        str = read(filename(), false);

        SharedPreferences pref = getSharedPreferences("Preference", 0);
        boolean first = true;
        dstr = "";
        long currentcal = System.currentTimeMillis() / HARU;
        for (int i = 0; i < 10; i++) {
            //
            String content = read(pref.getString("" + i, ""), true);//내용 불러오기
            String date = pref.getString("" + i, "");//날짜(파일이름) 불러오기
            if (pref.getLong(i + "v", 0) > 0 && date.length() != 0) {
                String calyear = date.substring(1, 5);
                int m = date.indexOf("-", 6);
                String calmonth = date.substring(6, m);
                String calday = date.substring(m + 1, date.length());
                dday.cal.set(Integer.parseInt(calyear), Integer.parseInt(calmonth) - 1, Integer.parseInt(calday));
                long comparecal = (dday.cal.getTimeInMillis() + 6000) / HARU;
                Log.d("service", "/'comparecal=" + comparecal + "-" + currentcal);
                String compareday = null;
                if (comparecal - currentcal > 0) {
                    compareday = "D-" + (comparecal - currentcal) + " " + content;
                } else if (comparecal - currentcal == 0) {
                    compareday = "오늘 " + content;
                } else {
                    compareday = content + " " + (currentcal - comparecal + 1) + "일째";
                }
                if (first) {
                    MainActivity.dstr = compareday;
                    first = false;
                } else {
                    MainActivity.dstr = MainActivity.dstr + ",\n" + compareday;
                }
            }
        }//디데이 계산(프리퍼런스로 값 가져옴)/'

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.widgetview, str);
        views.setTextViewText(R.id.widgetdday, dstr);
        if (dstr.length() == 0) {
            views.setInt(R.id.widgetdday, "setVisibility", 8);
        } else {
            views.setInt(R.id.widgetdday, "setVisibility", 0);
        }
        views.setTextViewText(R.id.widgetdate, filename + " " + cWeek);
        views.setInt(R.id.widgetview, "setBackgroundColor", Color.rgb(a, b, c));
        views.setInt(R.id.widgetview, "setTextColor", Color.rgb(d, e, f));
        views.setFloat(R.id.widgetview, "setTextSize", g);
        // 리모트뷰로 위젯에 변화된 값 넣기/'

        Intent intent2 = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(MainActivity.this, 0, intent2, 0);

        views.setOnClickPendingIntent(R.id.widgetlayout, pendingintent);//위젯을 누를 경우 이동
        AppWidgetManager wm = AppWidgetManager
                .getInstance(MainActivity.this);
        ComponentName widget = new ComponentName(getApplicationContext(), widget.class);
        ComponentName widget2 = new ComponentName(getApplicationContext(), widget2.class);
        wm.updateAppWidget(widget, views);
        wm.updateAppWidget(widget2, views);
        // 리모트뷰, 앱위젯매니저
        finish();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)//중료
        {
            exit();
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {//''
            if (edit.getSelectionStart() > 0) {
                int delete = edit.getSelectionStart();
                String temp = edit.getText().toString();
                String temp1 = temp.substring(0, delete - 1);
                String temp2 = temp.substring(delete, temp.length());
                edit.setText(temp1 + temp2);
                edit.setSelection(delete - 1);
            }
        }
        return false;
    }
}
