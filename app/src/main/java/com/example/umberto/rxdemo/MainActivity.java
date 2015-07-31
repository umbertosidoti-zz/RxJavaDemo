package com.example.umberto.rxdemo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

    EditText t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);
        t = (EditText) findViewById(R.id.editText);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              flatMap() takes the emissions of one Observable and returns the emissions of another Observable to take its place
//              filter() emits the same item it received, but only if it passes the boolean check.
//              flatMap() takes the emissions of one Observable and returns the emissions of another Observable
//              doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the payload.
                MockServerCall.getListOfUrl()
                        .subscribeOn(Schedulers.newThread())
                        .flatMap(FunctionAndAction.getFunctionConvertListToStringFunction())
                        .filter(FunctionAndAction.getFunctionFilterNullValue())
                        .flatMap(FunctionAndAction.getFunctionForGetPayload())
                        .doOnNext(FunctionAndAction.getActionSaveInDb())
                        .map(FunctionAndAction.getFunctionMapUrl())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNextAction, onErrorAction);
            }
        });
    }

    private final Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            t.append(s);
        }
    };

    private final Action1<java.lang.Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(MainActivity.this,throwable.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("ERROR",throwable.getMessage());
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
