package us.bojie.learnrxjava2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShippingCartCombineLocalAndNetworkDataActivity extends AppCompatActivity {

    private static final String TAG = ShippingCartCombineLocalAndNetworkDataActivity.class.getSimpleName();
    private Api mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_cart_combine_local_and_network_data);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.14:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mApi = retrofit.create(Api.class);
    }

    public void getdata(View view) {

        Observable.merge(getDatasFromLocal(), getDatasFromNetWork())
                .subscribe(new Consumer<List<Course>>() {
                    @Override
                    public void accept(List<Course> courses) throws Exception {
                        for (Course course : courses) {
                            Log.d(TAG, "accept: course: " + course.getName());
                        }
                    }
                });
    }

    private Observable<List<Course>> getDatasFromLocal() {
        List<Course> list = new ArrayList<>();

        list.add(new Course("shopping app"));
        list.add(new Course("news app"));
        return Observable.just(list);
    }

    private Observable<List<Course>> getDatasFromNetWork() {
        return mApi.getCourse().subscribeOn(Schedulers.io());
    }
}
