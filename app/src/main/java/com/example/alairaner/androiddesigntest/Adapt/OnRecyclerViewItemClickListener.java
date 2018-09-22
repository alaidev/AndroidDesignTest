package com.example.alairaner.androiddesigntest.Adapt;

import android.view.View;

import com.example.alairaner.androiddesigntest.Entity.CategoryEntity;

/**
 * Created by Alairaner on 2018/5/29.
 */

public interface OnRecyclerViewItemClickListener {
    void onItemClick(View view, CategoryEntity entity);
}
