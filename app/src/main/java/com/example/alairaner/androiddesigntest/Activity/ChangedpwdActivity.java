package com.example.alairaner.androiddesigntest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alairaner.androiddesigntest.Entity.HttpResult;
import com.example.alairaner.androiddesigntest.R;
import com.example.alairaner.androiddesigntest.http.ProgressDialogSubscriber;
import com.example.alairaner.androiddesigntest.http.presenter.MemberPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangedpwdActivity extends AppCompatActivity {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.password_input_oldpass)
    EditText passwordInputOldpass;
    @BindView(R.id.password_input_newpass)
    EditText passwordInputNewpass;
    @BindView(R.id.password_input_repass)
    EditText passwordInputRepass;
    @BindView(R.id.change_button)
    Button changeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedpwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.title_back, R.id.change_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.change_button:
                changePassword();
                break;
        }
    }

    private void changePassword() {
        String old_password = passwordInputOldpass.getText().toString().trim();
        String new_password = passwordInputNewpass.getText().toString().trim();
        String new_rePassword= passwordInputRepass.getText().toString().trim();
        checkPassword(old_password, new_password, new_rePassword);
        int member_id = getSharedPreferences("user", 0).getInt("member_id", -1);
        if (member_id<0) {
            Toast.makeText(this, "取出本地用户id失败，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        //修改密码
        MemberPresenter.changePassword(new ProgressDialogSubscriber<HttpResult>(this) {
            @Override
            public void onNext(HttpResult httpResult) {
                if (httpResult.getStatus() == 0) {//请求成功
                    Toast.makeText(ChangedpwdActivity.this, "修改密码成功,请您重新登录！", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor localEditor = getSharedPreferences("user", 0).edit();
                    localEditor.remove("member_id");
                    localEditor.remove("uname");
                    localEditor.remove("email");
                    localEditor.remove("image");
                    localEditor.commit();
                    //返回到PersonFragment页面重新登录
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("changepass", true);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(ChangedpwdActivity.this, "修改密码失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }, member_id, old_password, new_password);
    }

    private void checkPassword(String old_password, String new_password, String new_rePassword) {
        if (TextUtils.isEmpty(old_password) || TextUtils.isEmpty(new_password) || TextUtils.isEmpty(new_rePassword)) {
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new_password.equals(new_rePassword)) {
            Toast.makeText(this, "两次输入的新密码不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
