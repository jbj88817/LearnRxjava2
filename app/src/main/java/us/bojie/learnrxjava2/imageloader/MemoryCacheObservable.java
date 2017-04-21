package us.bojie.learnrxjava2.imageloader;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class MemoryCacheObservable extends CacheObservable {

    int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    int cacheSize = maxMemory / 8;
    LruCache<String, Bitmap> mLruCache = new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }
    };

    @Override
    public Image getDataFromCache(String url) {
        Log.d("getDataFromCache", "getDataFromMemoryCache");
        Bitmap bitmap = mLruCache.get(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }

        return null;
    }

    @Override
    public void putDataToCache(Image image) {
        mLruCache.put(image.getUrl(), image.getBitmap());
    }
}
