package com.example.alairaner.androiddesigntest.http.service;


import com.example.alairaner.androiddesigntest.Entity.HttpResult;
import com.example.alairaner.androiddesigntest.Entity.MemberEntity;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/12.
 */

public interface MemberService {

    //用户注册
    @FormUrlEncoded
    @POST("member")
    Observable<HttpResult<MemberEntity>> register(
            @Field("uname") String uname,
            @Field("password") String password,
            @Field("email") String email);

    //登陆
    @FormUrlEncoded
    @POST("member/login")
    Observable<HttpResult<MemberEntity>> login(
            @Field("uname") String uname,
            @Field("password") String password
    );

    //修改密码
    @FormUrlEncoded
    @PUT("member/{memberId}")
    Observable<HttpResult> changePassword(
            @Path("memberId") Integer memberId,
            @Field("oldPwd") String oldPwd,
            @Field("newPwd") String newPpwd
    );

    //找回密码
    @POST("member/pwd")
    Observable<HttpResult> findPassword(
            @Field("email") String email
    );
}
