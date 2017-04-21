package us.bojie.learnrxjava2.imageloader;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class RequestCreator {

    private static final String TAG = RequestCreator.class.getSimpleName();
    private MemoryCacheObservable mMemoryCacheObservable;
    private DiskCacheObservable mDiskCacheObservable;
    private NetworkCacheObservable mNetworkCacheObservable;

    public RequestCreator(Context context) {
        mMemoryCacheObservable = new MemoryCacheObservable();
        mDiskCacheObservable = new DiskCacheObservable(context);
        mNetworkCacheObservable = new NetworkCacheObservable();
    }

    public Observable<Image> getImageFromMemory(String url) {
        return mMemoryCacheObservable.getImage(url)
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return image != null;
                    }
                });
    }

    public Observable<Image> getImageFromDisk(String url) {
        return mDiskCacheObservable.getImage(url)
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return image != null;
                    }
                })
                .doAfterNext(new Consumer<Image>() {
                    @Override
                    public void accept(Image image) throws Exception {
                        mMemoryCacheObservable.putDataToCache(image);
                    }
                });
    }

    public Observable<Image> getImageFromNetwork(String url) {
        return mNetworkCacheObservable.getImage(url)
                .filter(new Predicate<Image>() {
                    @Override
                    public boolean test(Image image) throws Exception {
                        return image != null;
                    }
                })
                .doAfterNext(new Consumer<Image>() {
            @Override
            public void accept(Image image) throws Exception {
                mDiskCacheObservable.putDataToCache(image);
                mMemoryCacheObservable.putDataToCache(image);
            }
        });
    }
}
