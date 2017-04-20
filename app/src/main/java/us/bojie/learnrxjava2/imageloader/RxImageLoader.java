package us.bojie.learnrxjava2.imageloader;

import android.content.Context;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class RxImageLoader {

    private static final String TAG = RxImageLoader.class.getSimpleName();
    static RxImageLoader mInstance;
    private String mUrl;
    private RequestCreator mRequestCreator;

    private RxImageLoader() {
        mRequestCreator = new RequestCreator();
    }

    public static RxImageLoader with(Context context) {
        if (mInstance == null) {
            synchronized (RxImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new Builder(context).build();
                }
            }
        }
        return mInstance;
    }

    public RxImageLoader load(String url) {
        mUrl = url;
        return mInstance;
    }

    public void into(final ImageView imgView) {
        Observable.concat(mRequestCreator.getImageFromMemory(mUrl),
                mRequestCreator.getImageFromDisk(mUrl),
                mRequestCreator.getImageFromNetwork(mUrl))
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return false;
                    }
                })
                .firstElement()
                .subscribe(new Consumer<Image>() {
                    @Override
                    public void accept(Image image) throws Exception {
                        imgView.setImageBitmap(image.getBitmap());
                    }
                });
    }

    public static class Builder {
        private Context mContext;

        public Builder(Context context) {
            mContext = context;
        }

        public RxImageLoader build() {
            return new RxImageLoader();
        }
    }
}
