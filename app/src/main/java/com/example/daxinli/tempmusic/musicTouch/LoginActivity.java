package com.example.daxinli.tempmusic.musicTouch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.thread.Login_NetworkThread;
import com.example.daxinli.tempmusic.util.KeyThread;


public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    EditText login_username;
    EditText login_password;
    public Login_NetworkThread nt;          //登录的网络交互线程
    public KeyThread kt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        nt = new Login_NetworkThread();                //开启网络线程
        nt.setFather(this);
        nt.start();

        initToolBar();
        initClickListener();
    }
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            // TODO: 2018/3/18 用户上一次信息进行处理 加载cache
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {       //为ActionBar的返回按钮添加时间监听
        switch(item.getItemId()) {
            case android.R.id.home:
                this.removeActivity();
                nt.setFlag(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initClickListener() {
        Button login = (Button) findViewById(R.id.login_enter);
        TextView register = (TextView) findViewById(R.id.login_register);
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);

        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = login_username.getText().toString();
        String password = login_password.getText().toString();
        switch(v.getId()) {
            case R.id.login_enter:
                if(!checkUserInfo(this,username,password)) return ;  //检查用户信息是否符合规范

                GameData.lockNetworkThread = true;          //标志网络请求开始 知道需要等待网络结果的不能运行
                KeyThread.broadcastLogin(this,username,password);   //与服务器进行交互

                // TODO: 2018/3/18 需要添加异步等待
                /*
                ProgressBar progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleSmall);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2,2);
                progressBar.setLayoutParams(lp);
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout_login);
                mainLayout.addView(progressBar);
                */
                while(true) {       //异步等待
                    synchronized (GameData.lock) {
                        if(!GameData.lockNetworkThread) break;
                    }
                }
                String login_InfoQ;
                synchronized (GameData.lock) {
                    login_InfoQ = GameData.login_InfoQ;
                }
                //添加等待

                if(login_InfoQ == null) {
                    Toast.makeText(this, "服务器坏掉了(Ｔ▽Ｔ)", Toast.LENGTH_SHORT).show();
                    return ;
                }
                switch(login_InfoQ) {
                    case "CON_1":                           //登录成功
                        //返回上一界面                        返回信息是否加载成功
                        Intent intent = new Intent();
                        intent.putExtra("Result",login_username.getText().toString());
                        setResult(RESULT_OK,intent);
                        nt.setFlag(false);
                        this.removeActivity();
                        break;
                    case "CON_2":               //用户不存在
                        ShowAlertDialog(3);
                        break;
                    case "CON_3":               //用户密码错误
                        ShowAlertDialog(2);
                        break;
                    default:
                        //恢复数据
                        GameData.login_InfoQ = null;
                        break;
                }
                break;
            case R.id.login_register:
                Intent LoginIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginIntent.putExtra("username",username);
                LoginIntent.putExtra("password",password);
                startActivityForResult(LoginIntent,1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Activity之间的信息传输
        switch(requestCode) {
            case 1:
                if(resultCode==RESULT_OK) {
                    login_username.setText(data.getStringExtra("data_return"));
                }
                break;
        }
    }

    public static boolean checkUserInfo(Context context, String username, String password) {
        if(username.length()<8||username.length()>12) {
            Toast.makeText(context, "用户名长度不规范", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length()<8||password.length()>16) {
            Toast.makeText(context, "密码长度不规范", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0;i<username.length();i++) {  //检查用户名是否符合规范
            char c = username.charAt(i);
            boolean res = (c>='a'&&c<='z')||(c>='A'&&c<='Z')||(c>='0'&&c<='9');
            if(!res) {
                Toast.makeText(context, "用户名字符不符合规范", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    private void ShowAlertDialog(int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setTitle("登录失败");
        if(type==2)
            dialog.setMessage("账号或密码错误，请重新输入。");
        else if(type==3) {
            dialog.setMessage("用户不存在");
        }
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                login_username.setText("");
                login_password.setText("");
            }
        });
        dialog.show();
    }

    //进行network的暂停 在登陆和注册界面维持一个network生存
    @Override
    protected void onResume() {
        super.onResume();
        nt = new Login_NetworkThread();     //新建线程 重启
        nt.start();
        nt.setFather(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        nt.setFlag(false);      //销毁network
    }
}
