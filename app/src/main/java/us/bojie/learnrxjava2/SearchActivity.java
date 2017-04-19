package us.bojie.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.edit_search)
    EditText mEditSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        RxTextView.textChanges(mEditSearch)
                .debounce(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence charSequence) throws Exception {
                        // filter data
                        Log.d(TAG, "test: filter" + charSequence);
                        return charSequence.toString().trim().length() > 0;
                    }
                })

                .switchMap(new Function<CharSequence, ObservableSource<List<String>>>() {
                    @Override
                    public ObservableSource<List<String>> apply(CharSequence charSequence) throws Exception {
                        Log.d(TAG, "apply: switchMap = " + charSequence);

                        // search
                        List<String> list = new ArrayList<String>();
                        list.add("abc");
                        list.add("ada");
                        return Observable.just(list);
                    }
                })
//                .flatMap(new Function<CharSequence, ObservableSource<List<String>>>() {
//                    @Override
//                    public ObservableSource<List<String>> apply(CharSequence charSequence) throws Exception {
//                        Log.d(TAG, "apply: flatmap = " + charSequence);
//
//                        // search
//                        List<String> list = new ArrayList<String>();
//                        list.add("abc");
//                        list.add("ada");
//                        return Observable.just(list);
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        Log.d(TAG, "accept: " + strings);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }
}
