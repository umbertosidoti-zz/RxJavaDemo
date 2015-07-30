package com.example.umberto.rxdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends ActionBarActivity {

    EditText t;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b= (Button) findViewById(R.id.button);
        t= (EditText) findViewById(R.id.editText);





        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //flatMap takes the emissions of one Observable and returns the emissions of another Observable
                getFakeListOfUrlLikeAnService()
                        .flatMap(convertLisToString)
                        .map(mapFunction)
                        .subscribe(onNextAction);
            }
        });
    }

    private Observable<ArrayList<String>> getFakeListOfUrlLikeAnService(){

        ArrayList<String> demoUrl= new ArrayList<>(4);

        demoUrl.add("www.demourl1.com");
        demoUrl.add("www.demourl2.com");
        demoUrl.add("www.demourl3.com");
        demoUrl.add("www.demourl4.com");

        return Observable.just(demoUrl);
    }

    private final Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            t.append(s);
        }
    };

    private final Func1<List<String>,Observable<String>> convertLisToString= new Func1<List<String>, Observable<String>>() {
        @Override
        public Observable<String> call(List<String> urls) {

//          Observable.from(urls); that takes a collection of items and emits each them one at a time
            return Observable.from(urls);
        }
    };

    private final Func1<String,String> mapFunction= new Func1<String, String>() {
        @Override
        public String call(String s) {
            return s + " -with map \n";
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
