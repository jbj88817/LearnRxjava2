package us.bojie.learnrxjava2.imageloader;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class MemoryCacheObservable extends CacheObservable {

    @Override
    public Image getDataFromCache(String url) {
        return null;
    }

    @Override
    public void putDataToCache(Image image) {

    }
}
