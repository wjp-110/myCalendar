package com.werb.mycalendardemo.login;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.database.MyOpenHelper;

public class RegisterActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back;
    private Button btn_register;
    private EditText et_biaoqian,et_myname,et_banji,et_user_name,et_psw;
    private RelativeLayout rl_title_bar;
    private MyOpenHelper moHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        moHelper=new MyOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    private void init(){
        //从main_title_bar.xml获取对应的UI组件
        tv_main_title= (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("注册");
        tv_back = (TextView) findViewById(R.id.tv_back);

        rl_title_bar= (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(Color.TRANSPARENT);

        et_biaoqian= (EditText) findViewById(R.id.et_banji);
        et_myname= (EditText) findViewById(R.id.et_myname);
        et_banji= (EditText) findViewById(R.id.et_banji);
        et_user_name= (EditText) findViewById(R.id.et_user_name);
        et_psw= (EditText) findViewById(R.id.et_psw);

        btn_register= (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mBiaoqian=et_biaoqian.getText().toString().trim();
                String mMyname=et_myname.getText().toString().trim();
                String mBanji=et_banji.getText().toString().trim();
                String mUsername=et_user_name.getText().toString().trim();
                String mPsw=et_psw.getText().toString().trim();
                if (CheckIsDataAlreadyInDBorNot(mUsername)) {
                    Toast.makeText(RegisterActivity.this,"该用户名已被注册，注册失败",Toast.LENGTH_SHORT).show();
                } else {

                    if (register(mBiaoqian,mMyname,mBanji,mUsername,mPsw)) {
                        Toast.makeText(RegisterActivity.this, "插入数据表成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);;
                    }
                }
            }
//向数据库插入数据
            public boolean register(String biaoqian,String myname,String banji,String username,String psw){
                SQLiteDatabase db= moHelper.getWritableDatabase();
                    /*String sql = "insert into userData(name,password) value(?,?)";
                    Object obj[]={username,password};
                    db.execSQL(sql,obj);*/
                ContentValues values=new ContentValues();
                values.put("biaoqian",biaoqian);
                values.put("myname",myname);
                values.put("banji",banji);
                values.put("username",username);
                values.put("psw",psw);
                db.insert("user",null,values);
                db.close();
                //db.execSQL("insert into user (biaoqian,myname,banji,username,psw) values (?,?,?,?,?)",new String[]{biaoqian,myname,banji,username,psw});
                return true;
            }
//             检验用户名是否已存在
            public boolean CheckIsDataAlreadyInDBorNot(String value){

                SQLiteDatabase db=moHelper.getWritableDatabase();
                String Query = "Select * from user where username =?";
                Cursor cursor = db.rawQuery(Query, new String[]{value});
                if (cursor.getCount()>0){
                    cursor.close();
                    return  true;
                }
                cursor.close();
                return false;
            }
        });
    }

}