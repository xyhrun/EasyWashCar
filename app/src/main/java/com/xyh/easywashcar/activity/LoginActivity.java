package com.xyh.easywashcar.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xyh.easywashcar.R;
import com.xyh.easywashcar.base.MyAppcation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 向阳湖 on 2016/5/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    @Bind(R.id.previous_id)
    Button previous;
    @Bind(R.id.login_btn_id)
    Button login;
    @Bind(R.id.register_account_id)
    Button register_account;
    @Bind(R.id.retrieve_password_id)
    Button retrieve_password;
    @Bind(R.id.account_id)
    EditText account;
    @Bind(R.id.password_id)
    EditText password;

    private String inputAccount;
    private String inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setOnClickListener();
    }

    private void setOnClickListener() {
        previous.setOnClickListener(this);
        login.setOnClickListener(this);
        register_account.setOnClickListener(this);
        retrieve_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        inputAccount = account.getText().toString();
        inputPassword = password.getText().toString();
        switch (v.getId()) {
            case R.id.previous_id:
                finish();
                break;
            case R.id.login_btn_id:
                if (inputAccount.equals("666666") && inputPassword.equals("666666")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setIcon(R.mipmap.paopao)
                            .setCancelable(true)
                            .setTitle("向阳湖")
                            .setMessage("全都是泡沫,一戳就破...");
                    builder.show();
                } else {
                    MyAppcation.myToast("无该账号,请先注册!");
                }
                break;
            case R.id.register_account_id:
                MyAppcation.myToast("不好意思,此功能正在完善中...");
                break;
            case R.id.retrieve_password_id:
                MyAppcation.myToast("不好意思,此功能正在完善中...");
                break;
        }
    }
}
