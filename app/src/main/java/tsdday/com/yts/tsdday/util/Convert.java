package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.util.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tsdday.com.yts.tsdday.GlideApp;
import tsdday.com.yts.tsdday.model.Couple;

public class Convert {

    public static Observable<String> contentUriToFilePath(final Context context, final ArrayList<String> imageUrls) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                try {
                    for (String url : imageUrls) {
                        Bitmap bitmap = GlideApp.with(context).asBitmap().load(url).submit().get();
                        if (bitmap != null) {
                            File file = CreateBitmap.create(context, bitmap);
                            emitter.onNext(file.getPath());
                        }
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Crashlytics.logException(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<byte[]> contentUriToByteArray(final Context context, final ArrayList<String> imageUrls) {
        return Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                try {
                    for (String url : imageUrls) {
                        byte[] byteArray;
                        Bitmap bitmap = GlideApp.with(context).asBitmap().load(url).submit().get();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
                        byteArray = stream.toByteArray();
                        bitmap.recycle();
                        emitter.onNext(byteArray);
                    }
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                    Crashlytics.logException(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Single<byte[]> filePathToByteArray(final Context context, final String filePath) {
        return Single.create(new SingleOnSubscribe<byte[]>() {
            @Override
            public void subscribe(SingleEmitter<byte[]> emitter) throws Exception {
                try {
                    byte[] byteArray;
                    Bitmap bitmap = GlideApp.with(context).asBitmap().load(filePath).submit().get();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
                    byteArray = stream.toByteArray();
                    bitmap.recycle();

                    emitter.onSuccess(byteArray);
                } catch (Exception e) {
                    emitter.onError(e);
                    Crashlytics.logException(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
