package com.werb.mycalendardemo.login;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.werb.mycalendardemo.MainActivity;
import com.werb.mycalendardemo.R;
import com.werb.mycalendardemo.database.MyOpenHelper;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back, tv_register, tv_find_psw;
    private Button btn_login;
    private String userName, psw;
    private EditText et_user_name, et_psw;

    private MyOpenHelper moHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        moHelper=new MyOpenHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
    }
    private void init(){
        //从main_title_bar.xml获取对应的UI组件
        tv_main_title= (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("登录");
        tv_back = (TextView) findViewById(R.id.tv_back);

        et_user_name = (EditText) findViewById(R.id.et_user_name);
        et_psw = (EditText) findViewById(R.id.et_psw);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName=et_user_name.getText().toString().trim();
                String psw=et_psw.getText().toString().trim();


                if (login(userName,psw)){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }


//            验证登录
            public boolean login(String username,String password) {
                SQLiteDatabase db = moHelper.getWritableDatabase();
                String sql = "select * from user where username=? and psw=?";
                Cursor cursor = db.rawQuery(sql, new String[]{username, password});
                if (cursor.moveToFirst()) {
                    cursor.close();
                    return true;
}
return false;
        }
        });
        }

}