package com.example.alairaner.androiddesigntest.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.alairaner.androiddesigntest.R;

public class TitleFragment extends Fragment implements View.OnClickListener {
    public static int currentId;
    private LinearLayout tabItemHome,tabItemCategory,tabItemCart,tabItemPerson;
    private ImageButton tabItemHomeBtn,tabItemCategoryBtn,tabItemCartBtn,tabItemPersonBtn;
    private Fragment home,category,cart,person;
    private FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_title,container,false);
        initView(view);
        setTabSelection(R.id.tab_item_home);
        return view;
    }
    public void initView(View view){
        tabItemHome=(LinearLayout)view.findViewById(R.id.tab_item_home);
        tabItemHome.setOnClickListener(this);
        tabItemCart=(LinearLayout)view.findViewById(R.id.tab_item_cart);
        tabItemCart.setOnClickListener(this);
        tabItemCategory=(LinearLayout)view.findViewById(R.id.tab_item_category);
        tabItemCategory.setOnClickListener(this);
        tabItemPerson=(LinearLayout)view.findViewById(R.id.tab_item_person);
        tabItemPerson.setOnClickListener(this);
        tabItemHomeBtn=(ImageButton)view.findViewById(R.id.tab_item_home_btn);
        tabItemCategoryBtn=(ImageButton)view.findViewById(R.id.tab_item_category_btn);
        tabItemCartBtn=(ImageButton)view.findViewById(R.id.tab_item_cart_btn);
        tabItemPersonBtn=(ImageButton)view.findViewById(R.id.tab_item_person_btn);
        fragmentManager=getFragmentManager();
    }
    @Override
    public void onClick(View view) {
        setTabSelection(view.getId());
    }
    public void setTabSelection(int id){
        tabItemPersonBtn.setImageResource(R.drawable.person);
        tabItemHomeBtn.setImageResource(R.drawable.home);
        tabItemCartBtn.setImageResource(R.drawable.cart);
        tabItemCategoryBtn.setImageResource(R.drawable.category);
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if (home!=null)fragmentTransaction.hide(home);
        if (category!=null)fragmentTransaction.hide(category);
        if (cart!=null)fragmentTransaction.hide(cart);
        if (person!=null)fragmentTransaction.hide(person);
        switch (id){
            case R.id.tab_item_home:
                tabItemHomeBtn.setImageResource(R.drawable.home1);
                if (home==null){
                    home=new HomeFragment();
                    fragmentTransaction.add(R.id.content,home);
                }else {
                    fragmentTransaction.show(home);
                }
                break;
            case R.id.tab_item_category:
                tabItemCategoryBtn.setImageResource(R.drawable.category1);
                if (category==null){
                    category=new CategoryFragment();
                    fragmentTransaction.add(R.id.content,category);
                }else {
                    fragmentTransaction.show(category);
                }
                break;
            case R.id.tab_item_cart:
                tabItemCartBtn.setImageResource(R.drawable.cart1);
                if (cart==null){
                    cart=new CartFragment();
                    fragmentTransaction.add(R.id.content,cart);
                }else {
                    fragmentTransaction.show(cart);
                }
                break;
            case R.id.tab_item_person:
                tabItemPersonBtn.setImageResource(R.drawable.person1);
                if (person==null){
                    person=new PersonFragment();
                    fragmentTransaction.add(R.id.content,person);
                }else {
                    fragmentTransaction.show(person);
                }
                break;
        }
        fragmentTransaction.commit();
        currentId=id;
    }
}
