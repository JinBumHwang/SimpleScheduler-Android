package hofio.simplescheduler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class dday extends Activity {
    /** Called when the activity is first created. */
    int cn[]=new int[10];
    int ddn[]=new int[10];
    CheckBox checkview;
    TextView ddayview;
    byte checknum;
    static Calendar cal;
    static int number;
    SharedPreferences pref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dday);
        // TODO Auto-generated method stub
        SharedPreferences pref = getSharedPreferences("Preference", 0);// 프리퍼런스
        for (int i = 0; i < 10; i++) {
            cn[i] = getResources().getIdentifier("check"+(i+1),
                    "id", getPackageName());
            ddn[i] = getResources().getIdentifier("dtext"+(i+1),
                    "id", getPackageName());
            checkview =findViewById(cn[i]);
            if(pref.getLong(i+"v",0)>0)
            {
                checkview.setChecked(true);;
                checknum++;
            }
            checkview.setOnClickListener(l);
            ddayview =findViewById(ddn[i]);
            ddayview.setOnClickListener(m);
        }
        cal=Calendar.getInstance();
        ddayview();
    }
    CheckBox.OnClickListener l=new CheckBox.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int num;
            for(num=0;num<10;num++)
            {
                if(v.getId()==cn[num])
                {
                    checkview=findViewById(cn[num]);
                    ddayview=findViewById(ddn[num]);
                    break;
                }
            }
            if(checkview.isChecked()){checknum++;}else{checknum--;}//체크숫자 관리
            if(ddayview.length()==0)//체크 할 수 있나?
            {
                Toast.makeText(getApplicationContext(), "내용이 없습니다.", Toast.LENGTH_SHORT).show();
                checkview.setChecked(false);checknum--;
            }
            else if(checknum>4)//체크 4개 넘었나?
            {
                Toast.makeText(getApplicationContext(), "4개 항목만 보여줄 수 있습니다.", Toast.LENGTH_SHORT).show();
                checkview.setChecked(false);checknum--;
            }
            else
            {
                ddayview();
            }
        }
    };
    TextView.OnClickListener m=new TextView.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            boolean create=false;
            int num=0;
            for(num=0;num<10;num++)//작은 숫자부터 생성(어디에서나)
            {
                ddayview =  findViewById(ddn[num]);
                if(ddayview.length()==0)
                {
                    create=true;
                    break;
                }
                else if(v.getId()==ddn[num])
                {
                    break;
                }
            }//'
            if(create)//생성 가능
            {
                Intent intent=new Intent(dday.this,calendar.class);
                intent.putExtra("number",num);
                intent.putExtra("year",MainActivity.cYear);
                intent.putExtra("month",MainActivity.cMonth);
                intent.putExtra("day",MainActivity.cDay);
                intent.putExtra("changedate", false);
                startActivity(intent);
                finish();
            }
            else
            {
                pref=getSharedPreferences("Preference",0);
                final int ii=num;
                final String date=pref.getString(""+ii, "");
                final View dialogView = (View)View.inflate(dday.this,R.layout.edit,null);
                final EditText dlgedit=(EditText)dialogView.findViewById(R.id.edittext);
                dlgedit.setText(""+read(date));
                AlertDialog.Builder dlg=new AlertDialog.Builder(dday.this);
                dlg.setTitle(""+ddayview.getText().toString());
                dlg.setView(dialogView);
                //수정
                dlg.setPositiveButton("수정",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //저장부분
                        try {
                            FileOutputStream outFs=openFileOutput(date,0);
                            String str=dlgedit.getText().toString();
                            outFs.write(str.getBytes());
                            outFs.close();
                            if(str.length()==0)
                            {
                                deleteFile(date);
                                SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                                for(int j=ii;j<9;j++)
                                {
                                    edit.putString(""+j, pref.getString(""+(j+1), ""));
                                    edit.putLong(j+"v", pref.getLong(""+(j+1), 0));
                                    edit.commit();
                                }
                            }
                        } catch (Exception e) {}
                        ddayview();
                    }
                });
                //삭제
                dlg.setNeutralButton("삭제",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        deleteFile(date);
                        SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                        for(int j=ii;j<9;j++)
                        {
                            edit.putString(""+j, pref.getString(""+(j+1), ""));
                            edit.putLong(j+"v", pref.getLong((j+1)+"v", 0));
                            edit.commit();
                        }
                        check();
                        ddayview();
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
                //
            }
        }

    };
    String read(String name)//파일 읽기 (내용 반환)
    {
        String diaryStr = null;
        try {
            FileInputStream inFs = openFileInput(name);
            byte[] txt = new byte[18];
            inFs.read(txt);
            inFs.close();
            diaryStr = (new String(txt)).trim();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return diaryStr;
    }
    void ddayview()
    {
        long currentcal=System.currentTimeMillis()/86400000;
        SharedPreferences pref=getSharedPreferences("Preference", 0);
        for (int i = 0; i < 10; i++) {
            ddayview=findViewById(ddn[i]);
            checkview=findViewById(cn[i]);

            String content=read(pref.getString(""+i, ""));//내용 불러오기
            String date=pref.getString(""+i, "");//날짜(파일이름) 불러오기
            if(date.length()!=0)
            {
                String calyear=date.substring(1, 5);
                int m=date.indexOf("-",6);
                String calmonth=date.substring(6,m);
                String calday=date.substring(m+1,date.length());
                cal.set(Integer.parseInt(calyear),Integer.parseInt(calmonth)-1,Integer.parseInt(calday));
                long comparecal=(cal.getTimeInMillis()+6000)/86400000;
                Log.d("Dday","/'comparecal="+comparecal+"-"+currentcal);
                String ddaytext;
                if(comparecal-currentcal>0)
                {
                    ddaytext=" D-"+(comparecal-currentcal)+"일";
                }
                else if(currentcal==comparecal)
                {
                    ddaytext=" 오늘";
                }
                else
                {
                    ddaytext=" "+(currentcal-comparecal+1)+"일째";
                }
                //디데이 계산(프리퍼런스로 값 가져옴)/'
                ddayview.setText(Html.fromHtml("<Font color="+"#7777ff"+">"+content+"</Font><Font color="+"#ff7777"+">"+ddaytext+"</Font></Font><Font color="+"#888888"+">"+date+"</font>"));

                SharedPreferences.Editor edit = pref.edit();// 프리퍼런스.에딧
                if(checkview.isChecked())
                {
                    edit.putLong(i+"v",comparecal);
                }
                else
                {
                    edit.remove(i+"v");
                }
                edit.commit();
            }
            else
            {
                ddayview.setText("");
            }
        }
    }//ddayview()//'
    void check()//체크 확인
    {
        SharedPreferences pref=getSharedPreferences("Preference", 0);
        for(int i=0;i<10;i++)
        {
            checkview=findViewById(cn[i]);
            if(pref.getLong(i+"v", 0)>0)
            {
                checkview.setChecked(true);
            }
            else
            {
                checkview.setChecked(false);
            }
        }
    }
}
