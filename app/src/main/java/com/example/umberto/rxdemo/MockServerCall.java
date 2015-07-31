package com.example.umberto.rxdemo;

import java.util.ArrayList;

import rx.Observable;

public class MockServerCall {


    //mock server call
    public static Observable<ArrayList<String>> getListOfUrl(){

        ArrayList<String> demoUrl= new ArrayList<>(4);

        demoUrl.add("www.demourl1.com");
        demoUrl.add("www.demourl2.com");
        demoUrl.add("www.demourl3.com");
        demoUrl.add("www.demourl4.com");

        return Observable.just(demoUrl);
    }

    //mock server call
    public static Observable<String> getPayloadFromUrl(String url){

        StringBuilder builder= new StringBuilder(url);
        builder.append("+");
        builder.append("payload");
        return Observable.just(builder.toString());
    }
}
