package us.bojie.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.getSimpleName();
    @BindView(R.id.tv_text)
    TextView mTvText;
    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.14:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(Api.class);
    }

    private UserParam getUserParam() {
        return new UserParam("bojie", "123456");
    }

    public void click_map(View view) {

        // login
        // get user info

        Observable.just(getUserParam()).flatMap(new Function<UserParam, ObservableSource<BaseResult>>() {
            @Override
            public ObservableSource<BaseResult> apply(UserParam userParam) throws Exception {
                return mApi.login(userParam);
            }
        }).flatMap(new Function<BaseResult, ObservableSource<User>>() {
            @Override
            public ObservableSource<User> apply(BaseResult baseResult) throws Exception {
                User user = mApi.getUserInfoWithPath(baseResult.getId()).execute().body();
                return Observable.just(user);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
            @Override
            public void accept(User user) throws Exception {
                mTvText.append(user.getId());
                mTvText.append(" ");
                mTvText.append(user.getUsername());
            }
        });

//        Observable<Integer> observable = Observable.just(1);

//        Observable<String> convertedObservable = observable.map(new Function<Integer, String>() {
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return integer + " Bojie";
//            }
//        });
//
//        convertedObservable.subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, "accept: " + s);
//            }
//        });
    }
}
