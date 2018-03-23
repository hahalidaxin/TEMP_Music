package com.example.daxinli.tempmusic.musicTouch;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daxinli.tempmusic.R;
import com.example.daxinli.tempmusic.constant.GameData;
import com.example.daxinli.tempmusic.thread.Login_NetworkThread;
import com.example.daxinli.tempmusic.util.KeyThread;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button register;
    public Login_NetworkThread nt;
    TextView registerUsername;
    TextView registerPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置Activity为全屏模式
        setContentView(R.layout.activity_register);
        //重新设置Toolbar为ActionBar

        nt = new Login_NetworkThread();
        nt.start();
        nt.setFather(this);

        initView();
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        //启用ActionBar的Home按钮
        if (actionbar != null) {
            actionbar.setTitle(" ");
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        String username = getIntent().getStringExtra("username");
        String password = getIntent().getStringExtra("password");
        registerUsername = (TextView)findViewById(R.id.register_username);
        registerPassword = (TextView)findViewById(R.id.register_password);
        registerUsername.setText(username);
        registerPassword.setText(password);
        register = (Button) findViewById(R.id.register_enter);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.register_enter:
                GameData.lockNetworkThread = true;          //锁数据
                String username = registerUsername.getText().toString();
                String password = registerPassword.getText().toString();
                if(!LoginActivity.checkUserInfo(this,username,password)) return ;    // TODO: 2018/3/18 需要检查

                KeyThread.broadcastRegister(this,username,password);

                while(true) {       //异步等待
                    synchronized (GameData.lock) {
                        if(!GameData.lockNetworkThread) break;
                    }
                }

                if(GameData.register_infoQ == null) {
                    Toast.makeText(this, "服务器坏掉了(ŎдŎ；)", Toast.LENGTH_SHORT).show();
                    return ;
                }
                switch(GameData.register_infoQ) {
                    case "CON_1":   //用户已经存在
                        ShowAlertDialog();
                        break;
                    case "CON_2":   //创建成功
                        setContentView(R.layout.activity_register_successful);
                        TextView susername = (TextView)findViewById(R.id.registerSUC_username);
                        susername.setText(username);
                        Button loginButton = (Button) findViewById(R.id.registerSUC_enter);
                        loginButton.setOnClickListener(this);
                        break;
                    default:    //网络不允许 创建失败
                        Toast.makeText(this, "对不起网络撑不住了，创建失败╮(╯﹏╰）╭", Toast.LENGTH_SHORT).show();
                        GameData.register_infoQ = null;
                        break;
                }
                break;
            case R.id.registerSUC_enter:
                Intent intent = new Intent();
                String returnString =((TextView)findViewById(R.id.registerSUC_username)).getText().toString();
                intent.putExtra("data_return",returnString);        //传回username
                setResult(RESULT_OK,intent);
                nt.setFlag(false);
                removeActivity();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                this.removeActivity();
                nt.setFlag(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("注册失败");
        builder.setMessage("用户名已经被注册。");
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((EditText)findViewById(R.id.register_username)).setText("");
                ((EditText)findViewById(R.id.register_password)).setText("");
            }
        });
        builder.show();
    }

}
