package us.bojie.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import us.bojie.learnrxjava2.imageloader.RxImageLoader;

public class LoadImageActivity extends AppCompatActivity {

    private static final String TAG = LoadImageActivity.class.getSimpleName();
    @BindView(R.id.button)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
        ButterKnife.bind(this);

        RxImageLoader.with(this).load("").into(null);


//        final Observable<String> memoryObservable = Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext(null);
//                e.onComplete();
//            }
//        });

        // https://github.com/ReactiveX/RxJava/issues/4475
        final Observable<String> memoryObservable = Completable.complete().toObservable();

        final Observable<String> diskObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("disk");
                e.onComplete();
            }
        });

        final Observable<String> networkObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("network");
                e.onComplete();
            }
        });

        RxView.clicks(mButton).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Observable.concat(memoryObservable, diskObservable, networkObservable)
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) throws Exception {
                                return !TextUtils.isEmpty(s);
                            }
                        })
                        .firstElement()
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Log.d(TAG, "accept: " + s);
                            }
                        });
            }
        });
    }

}
