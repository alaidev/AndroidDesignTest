package com.example.alairaner.androiddesigntest.http.service;


import com.example.alairaner.androiddesigntest.Entity.HttpResult;
import com.example.alairaner.androiddesigntest.Entity.OrderEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/24.
 */

public interface OrderService {

    //获取订单列表
    @GET("/order/member/{memberId}")
    Observable<HttpResult<List<OrderEntity>>> orderList(
            @Path("memberId") int memberId,
            @Query("page") int page
    );
}
