package com.example.alairaner.androiddesigntest.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.alairaner.androiddesigntest.Fragment.CartFragment;
import com.example.alairaner.androiddesigntest.R;

public class CartActivity extends AppCompatActivity {
    private CartFragment cartFragment;

    public CartActivity() {
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (cartFragment == null) {
            cartFragment = new CartFragment();
        }
        getFragmentManager().beginTransaction()
                .add(R.id.main_frame, cartFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
