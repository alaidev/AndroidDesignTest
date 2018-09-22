package com.example.alairaner.androiddesigntest.http.presenter;
import com.example.alairaner.androiddesigntest.Entity.AddressEntity;
import com.example.alairaner.androiddesigntest.Entity.HttpResult;
import com.example.alairaner.androiddesigntest.http.HttpMethods;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/10/27.
 */

public class AddressPresenter extends HttpMethods {
    //新增收货地址
    public static void add(Subscriber<AddressEntity> subscriber, Map<String, Object> address) {
        Observable observable = addressService.add(address)
                .map(new HttpResultFunc<AddressEntity>());
        toSubscribe(observable, subscriber);
    }

    //查询收货地址
    public static void load(Subscriber<List<AddressEntity>> subscriber, String memberId) {
        Observable observable = addressService.load(memberId)
                .map(new HttpResultFunc<List<AddressEntity>>());
        toSubscribe(observable, subscriber);
    }

    //删除收货地址
    public static void delete(Subscriber<HttpResult> subscriber, String addressId) {
        Observable observable = addressService.delete(addressId);
        toSubscribe(observable, subscriber);
    }
}
