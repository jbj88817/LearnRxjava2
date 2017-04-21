package us.bojie.learnrxjava2.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class NetworkCacheObservable extends CacheObservable {

    @Override
    public Image getDataFromCache(String url) {
        Log.d("getDataFromCache", "getDataFromNetworkCache");
        Bitmap bitmap = downloadImage(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(Image image) {

    }

    private Bitmap downloadImage(String url) {

        Bitmap bitmap = null;
        InputStream inputStream = null;
        try {
            final URLConnection con = new URL(url).openConnection();
            inputStream = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
