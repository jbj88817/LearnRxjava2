package us.bojie.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.tv_text);
    }

    public void click(View view) {
        Observable<String> observable = getObservable();
//        Observer<String> observer = getObserver();
//        observable.subscribe(observer);
        observable.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
                mTextView.append(s);
                mTextView.append("\n");
            }
        });

    }

    public Observable<String> getObservable() {

//        return Observable.just("dabaojian", "yedian", "liaomei");
//        return Observable.fromArray("dabaojian", "yedian", "liaomei");
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "dabaojian";
            }
        });

//        return Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext("dabaojian");
//                e.onNext("yedian");
//                e.onComplete();
////                e.onError(new );
//            }
//        });
    }

    public Observer<String> getObserver() {
        return new Observer<String>() {

            Disposable dd = null;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: ");
                dd = d;
            }

            @Override
            public void onNext(String value) {
                Log.d(TAG, "onNext: " + value);
                if (value.equals("paoniu")) {
                    dd.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }
}
