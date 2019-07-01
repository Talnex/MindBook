package com.talnex.wrongsbook.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static String res = "";

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //登录按钮
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //注册按钮
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("账户或密码格式错误");
            return;
        }
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("认证中……");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET
                , Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE}, 1);
        //服务器验证账户密码
        new Thread() {
            @Override
            public void run() {
                try {
                    String path = "http://129.28.168.201:8888/login?email="
                            + email + "&password=" + password;
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    String data = "email=" + email + "&password=" + password;
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
                                onLoginSuccess(email);
                                break;
                            default:
                                res = "用户名或密码错误";
                                break;
                        }

                    } else {
                        res = "error"+code;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                    }
                }, 1000);

        while (res.equals("")) ;
        onLoginFailed(res);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String email) {
        Userinfo.useremail = email;
        Userinfo.username = email;
        finish();
        Intent intent = new Intent(LoginActivity.this,MindTreeEngine.class);
        startActivity(intent);
    }

    public void onLoginFailed(String info) {
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("输入一个合法的邮箱地址");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("密码在4到10个字符之间");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
