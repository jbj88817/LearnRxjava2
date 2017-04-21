package us.bojie.learnrxjava2.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by bojiejiang on 4/19/17.
 */

public class DiskCacheObservable extends CacheObservable {

    private DiskLruCache mDiskLruCache;
    private Context mContext;

    private int maxSize = 20 * 1024 * 1024;

    public DiskCacheObservable(Context context) {
        mContext = context;
        initDiskLruCache();
    }

    @Override
    public Image getDataFromCache(String url) {
        Log.d("getDataFromCache","getDataFromDiskCache");
        Bitmap bitmap = getDataFromDiskLruCache(url);
        if (bitmap != null) {
            return new Image(url, bitmap);
        }
        return null;
    }

    @Override
    public void putDataToCache(final Image image) {
        Observable.create(new ObservableOnSubscribe<Image>() {
            @Override
            public void subscribe(ObservableEmitter<Image> e) throws Exception {
                putDataToDiskLruCache(image);
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private void initDiskLruCache() {
        try {
            File cacheDir = DiskCacheUtils.getDiskCacheDir(mContext, "img_cache");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            int versionCode = DiskCacheUtils.getAppVersionCode(mContext);
            mDiskLruCache = DiskLruCache.open(cacheDir, versionCode, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getDataFromDiskLruCache(String url) {
        FileDescriptor fileDescriptor = null;
        FileInputStream fileInputStream = null;
        DiskLruCache.Snapshot snapshot = null;

        try {
            // gen key
            final String key = DiskCacheUtils.getMd5String(url);
            // find cache
            snapshot = mDiskLruCache.get(key);

            if (snapshot != null) {
                fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                fileDescriptor = fileInputStream.getFD();
            }
            Bitmap bitmap = null;
            if (fileDescriptor != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            }

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileDescriptor == null && fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void putDataToDiskLruCache(Image img) {
        try {
            String key = DiskCacheUtils.getMd5String(img.getUrl());
            DiskLruCache.Editor editor = mDiskLruCache.edit(key);

            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                boolean isSuccessful = download(img.getUrl(), outputStream);

                if (isSuccessful) {
                    editor.commit();
                } else {
                    editor.abort();

                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean download(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
