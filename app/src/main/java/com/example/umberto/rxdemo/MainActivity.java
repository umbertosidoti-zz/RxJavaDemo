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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.OnClickEvent;
import rx.android.view.ViewObservable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends ActionBarActivity {

    private EditText t;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);
        t = (EditText) findViewById(R.id.editText);
        ViewObservable.clicks(b).subscribe(clickViewAction);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null)
            subscription.unsubscribe();
    }

    private final Action1<String> onNextAction = new Action1<String>() {
        @Override
        public void call(String s) {
            t.append(s);
        }
    };

    Action1<? super OnClickEvent> clickViewAction = new Action1<OnClickEvent>() {
        @Override
        public void call(OnClickEvent onClickEvent) {
            //flatMap() takes the emissions of one Observable and returns the emissions of another Observable to take its place
            //filter() emits the same item it received, but only if it passes the boolean check.
            //doOnNext() allows us to add extra behavior each time an item is emitted, in this case saving the payload.
            subscription = MockServerCall.getListOfUrl()
                    .subscribeOn(Schedulers.newThread())
                    .flatMap(FunctionAndAction.getFunctionConvertListToStringFunction())
                    .filter(FunctionAndAction.getFunctionFilterNullValue())
                    .flatMap(FunctionAndAction.getFunctionForGetPayload())
                    .doOnNext(FunctionAndAction.getActionSaveInDb())
                    .map(FunctionAndAction.getFunctionMapUrl())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNextAction, onErrorAction);
        }
    };

    private final Action1<java.lang.Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ERROR", throwable.getMessage());
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
