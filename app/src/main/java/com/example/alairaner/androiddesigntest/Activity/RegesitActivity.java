package com.example.alairaner.androiddesigntest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alairaner.androiddesigntest.Entity.MemberEntity;
import com.example.alairaner.androiddesigntest.R;
import com.example.alairaner.androiddesigntest.http.ProgressDialogSubscriber;
import com.example.alairaner.androiddesigntest.http.presenter.MemberPresenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegesitActivity extends AppCompatActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.name)
    EditText Loginname;
    @BindView(R.id.email)
    EditText Loginemail;
    @BindView(R.id.password)
    EditText Loginpassword;
    @BindView(R.id.password1)
    EditText Loginpassword1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regesit);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.back,R.id.submit})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                register();
                break;
        }
    }
    private void register(){
        String username=Loginname.getText().toString().trim();
        String email=Loginemail.getText().toString().trim();
        String password=Loginpassword.getText().toString().trim();
        String password1=Loginpassword1.getText().toString().trim();
        checkUsername(username);
        checkEmail(email);
        checkPwd(password,password1);
        MemberPresenter.register(new ProgressDialogSubscriber<MemberEntity>(this) {
            @Override
            public void onNext(MemberEntity memberEntity) {
                Toast.makeText(RegesitActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor localEditor=getSharedPreferences("user",0).edit();
                localEditor.putInt("member_id",memberEntity.getMember_id());
                localEditor.putString("username",memberEntity.getUname());
                localEditor.putString("email",memberEntity.getEmail());
                localEditor.putString("image",memberEntity.getImage());
                localEditor.commit();
                Intent intent=new Intent();
                intent.putExtra("logined",true);
                setResult(RESULT_OK,intent);
                finish();
            }
        },username,email,password);
    }
    private void checkUsername(String username){
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.length()<4||username.length()>20){
            Toast.makeText(this, "用户名的长度为4-20个字符!", Toast.LENGTH_SHORT).show();
            return;
        }if(username.contains("@")){
            Toast.makeText(this, "用户名中不能包含@等特殊字符!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void checkEmail(String email){
        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "邮箱不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        Pattern pattern=Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher= pattern.matcher(email);
        if (!matcher.matches()){
            Toast.makeText(this, "邮箱格式不正确!", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void checkPwd(String password,String pwd){
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "密码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }if (!password.equals(pwd)){
            Toast.makeText(this, "两次输入密码不相同！", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
