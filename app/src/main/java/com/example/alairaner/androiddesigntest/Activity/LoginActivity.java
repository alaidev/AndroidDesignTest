package com.example.alairaner.androiddesigntest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alairaner.androiddesigntest.Entity.MemberEntity;
import com.example.alairaner.androiddesigntest.R;
import com.example.alairaner.androiddesigntest.http.ProgressDialogSubscriber;
import com.example.alairaner.androiddesigntest.http.presenter.MemberPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity{
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.query_regesit)
    Button regesit;
    @BindView(R.id.find_password)
    Button find_pwd;
    @BindView(R.id.login_button)
    Button login;
    @BindView(R.id.name)
    EditText loginname;
    @BindView(R.id.password)
    EditText loginpassword;
    @BindView(R.id.cbDisplayPassword)
    CheckBox mCbDisplayPassword;
    private final int REQUEST_CODE_REGISTER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mCbDisplayPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //如果选中，显示密码
                    loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });
    }
    @OnClick({R.id.query_regesit,R.id.find_password,R.id.login_button,R.id.back})
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.query_regesit:
                Intent intent=new Intent(this,RegesitActivity.class);
                startActivityForResult(intent,REQUEST_CODE_REGISTER);
                break;
            case R.id.find_password:
                break;
            case R.id.login_button:
                login();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void login(){
        final String username=loginname.getText().toString().trim();
        final String password=loginpassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        MemberPresenter.login(new ProgressDialogSubscriber<MemberEntity>(this) {
            @Override
            public void onNext(MemberEntity memberEntity) {
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
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
        },username,password);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK && requestCode == REQUEST_CODE_REGISTER){
            Intent returnIntent = new Intent();
            returnIntent.putExtra("logined", true);
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
