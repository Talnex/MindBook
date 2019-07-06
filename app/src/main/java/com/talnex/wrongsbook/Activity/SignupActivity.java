package com.talnex.wrongsbook.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.talnex.wrongsbook.Net.Userinfo;
import com.talnex.wrongsbook.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 注册模块
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    private String res = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void signup() {
        if (!validate()) {
            onSignupFailed("格式错误");
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("创建账户……");
        progressDialog.show();

        final String name = _nameText.getText().toString();
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        //转圈的dialog
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 3000);

        //服务器获取请求
        new Thread() {
            @Override
            public void run() {
                try {
                    String path = "http://129.28.168.201:8888/signup?email="
                            + email + "&password=" + password + "&name=" + name;
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "email=" + email + "&password=" + password + "&name=" + name;
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.setDoOutput(true); // 设置要向服务器写数据
                    conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));

                    int code = conn.getResponseCode(); // 服务器的响应码 200 OK //404 页面找不到
                    if (code == 200) {
                        InputStream is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        //获取响应文本
                        String responseText = sb.toString();
                        is.close();
                        switch (responseText) {
                            case "success":
                                res = responseText;
                                onSignupSuccess(email);
                                break;
                            default:
                                res = responseText;
                        }
                    } else {
                        res = "error:" + code;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //这样不是很好
        while (res.equals("")) ;
        Log.d("test", res);
        onSignupFailed(res);
    }


    /**
     * 服务器返回成功登录的消息后进入这个函数
     *
     * @param email 用户的邮箱
     */
    public void onSignupSuccess(String email) {
        Intent intent = new Intent(SignupActivity.this, MindTreeEngine.class);
        Userinfo.useremail = email;
        startActivity(intent);
        this.finish();
    }

    /**
     * 服务器返回错误后进入这个函数
     *
     * @param info 错误信息
     */
    public void onSignupFailed(String info) {
        Toast.makeText(this, res, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /**
     * 合法性验证的函数
     *
     * @return 是否合法
     */
    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("至少3个字符");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("请输入一个合法的邮箱");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("4到10个字符之间");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("两次密码不一致");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}