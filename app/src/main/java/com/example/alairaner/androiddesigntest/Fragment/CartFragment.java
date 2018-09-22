package com.example.alairaner.androiddesigntest.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alairaner.androiddesigntest.Activity.MainActivity;
import com.example.alairaner.androiddesigntest.Adapt.CartGoodsAdapter;
import com.example.alairaner.androiddesigntest.Entity.CartGoodsEntity;
import com.example.alairaner.androiddesigntest.Entity.HttpResult;
import com.example.alairaner.androiddesigntest.R;
import com.example.alairaner.androiddesigntest.Utils.NetworkUtils;
import com.example.alairaner.androiddesigntest.http.ProgressDialogSubscriber;
import com.example.alairaner.androiddesigntest.http.presenter.GoodsPresenter;
import com.example.alairaner.androiddesigntest.view.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class CartFragment extends Fragment {
    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.shopping_cart_text)
    TextView editCartGoods;
    @BindView(R.id.cart_checkout)
    TextView checkoutButton;
    @BindView(R.id.cart_forward_index)
    Button homePageButton;
    @BindView(R.id.cart_no_data_layout)
    LinearLayout cartNoDataLayout;
    @BindView(R.id.recyclerView_cart)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_widget)
    SwipeRefreshLayout refreshWidget;
    @BindView(R.id.cart_list_layout)
    LinearLayout cartListLayout;
    @BindView(R.id.cart_total)
    TextView cartTotal;
    private UpdateCart receiver;
    private int memberId;
    private LinearLayoutManager mLayoutManager;
    private List<CartGoodsEntity> listData = new ArrayList<>();
    private CartGoodsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this,view);
        init();
        getCartList(memberId);
        return view;
    }
    private void init() {
        SharedPreferences sp = getActivity().getSharedPreferences("user", 0);
        memberId = sp.getInt("member_id", -1);

        if (!(getActivity() instanceof MainActivity)) {
            titleBack.setVisibility(View.VISIBLE);
        } else {
            titleBack.setVisibility(View.GONE);
        }
        cartNoDataLayout.setVisibility(View.GONE);
        cartListLayout.setVisibility(View.VISIBLE);

        //设置列表样式
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        //设置刷新样式
        refreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color
                        .holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        refreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        //下拉刷新监听器
        refreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新
                if (NetworkUtils.isNetworkAvailable(getActivity())) {//有网络才允许重新刷新
                    getCartList(memberId);
                } else {
                    refreshWidget.setRefreshing(false);//无网络
                    Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //适配数据
        adapter = new CartGoodsAdapter(getActivity(), listData);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener
                        .OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //nothing
                    }

                    @Override
                    public void onLongItemClick(View view, final int position) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("删除购物车")
                                .setMessage("您确认要删除这种商品吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int cartId = listData.get(position).getCart_id();
                                        deleteCartGoods(cartId);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                })
        );
    }
    private void deleteCartGoods(int cartid) {
        GoodsPresenter.cartDelete(new ProgressDialogSubscriber<HttpResult>(getActivity()) {
            @Override
            public void onNext(HttpResult httpResult) {
                if (httpResult.getStatus() == 0) {
                    Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                    refreshWidget.setRefreshing(true);
                    getCartList(memberId);
                }
            }
        }, cartid);
    }
    private void getCartList(int id) {
        if (memberId != -1) {
            GoodsPresenter.cartList(new Subscriber<List<CartGoodsEntity>>() {

                @Override
                public void onCompleted() {
                    refreshWidget.setRefreshing(false);
                }

                @Override
                public void onError(Throwable e) {
                    refreshWidget.setRefreshing(false);
                }

                @Override
                public void onNext(List<CartGoodsEntity> cartGoodsEntities) {
                    listData.clear();
                    listData.addAll(cartGoodsEntities);
                    adapter.notifyDataSetChanged();

                    double totalPrice = 0.0;
                    for (int i = 0; i < cartGoodsEntities.size(); i++) {
                        totalPrice += cartGoodsEntities.get(i).getAmount();
                    }
                    cartTotal.setText("总价：  ￥" + totalPrice);
                }
            }, id);
        }

    }
    private void registerReceiver() {
        UpdateCart receiver = new UpdateCart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.goods.shoopingcart");
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @OnClick({R.id.title_back, R.id.shopping_cart_text, R.id.cart_checkout, R.id.cart_forward_index})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                getActivity().finish();
                break;
            case R.id.shopping_cart_text://编辑
                if (editCartGoods.getText().equals("编辑")) {
                    editCartGoods.setText("长按删除");
                }
                break;
            case R.id.cart_checkout: //结算
                break;
            case R.id.cart_forward_index://回到首页
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("action", "toIndex");
                startActivity(intent);
                break;
        }
    }
    private class UpdateCart extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshWidget.setProgressViewOffset(false, 0, 100);
            refreshWidget.setRefreshing(true);
            getCartList(memberId);
        }
    }
}
