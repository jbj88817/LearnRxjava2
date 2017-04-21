package us.bojie.learnrxjava2.imageloader;

import android.content.Context;
import android.widget.ImageView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class RxImageLoader {

    private static final String TAG = RxImageLoader.class.getSimpleName();
    static RxImageLoader mInstance;
    private String mUrl;
    private RequestCreator mRequestCreator;

    private RxImageLoader(Builder builder) {
        mRequestCreator = new RequestCreator(builder.mContext);
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
                .first(new Image(mUrl, null)).toObservable()
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
            return new RxImageLoader(this);
        }
    }
}
