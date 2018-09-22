package com.example.alairaner.androiddesigntest.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.example.alairaner.androiddesigntest.Fragment.TitleFragment;
import com.example.alairaner.androiddesigntest.R;

public class MainActivity extends AppCompatActivity {
    private TitleFragment titleFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleFragment = new TitleFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_fragment, titleFragment).commit();
    }
    public boolean isLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        Log.d("1111",sharedPreferences.getString("username","")+"1");
        return TextUtils.isEmpty(sharedPreferences.getString("username",""));
    }
    public void showGoods(int goodsid){
        Intent intent = new Intent(this, GoodsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("goods_id", goodsid);
        startActivity(intent);
    }
    public void showGoodsList(int cid, int brand){
        Intent intent = new Intent(this, GoodsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("cid", cid);
        intent.putExtra("brand", brand);
        startActivity(intent);
    }
    public void showSeckillList(){
        Intent intent = new Intent(this, GoodsListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("seckill", 1);
        startActivity(intent);
    }
    /**
     * 切换下方的tab标签
     *
     * @param index
     */
    public void changeTab(int index) {
        switch (index) {
            case 1:
                this.titleFragment.setTabSelection(R.id.tab_item_home);
                break;
            case 2:
                this.titleFragment.setTabSelection(R.id.tab_item_category);
                break;
            case 3:
                this.titleFragment.setTabSelection(R.id.tab_item_cart);
                break;
            case 4:
                this.titleFragment.setTabSelection(R.id.tab_item_person);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getStringExtra("action");
        if ("toIndex".equals(action)) {
            changeTab(1);
        }
    }

    /**
     * 点击返回按键监听
     */
    @Override
    public void onBackPressed() {
        if (titleFragment.currentId != R.id.tab_item_home) {
            changeTab(1);
            return;
        }
        super.onBackPressed();
    }
}
