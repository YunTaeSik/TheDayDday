package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.crashlytics.android.Crashlytics;

import java.io.ByteArrayOutputStream;
import java.io.File;

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

public class Convert {

    public static Observable<String> contentUriToFilePath(final Context context, final ArrayList<String> imageUrls) {
        return Observable.create((ObservableOnSubscribe<String>) emitter -> {
            try {
                for (String url : imageUrls) {
                    Bitmap bitmap = GlideApp.with(context).asBitmap().load(url).submit().get();
                    if (bitmap != null) {
                        File file = CreateBitmap.create(context, bitmap);
                        if (file != null) {
                            emitter.onNext(file.getPath());
                        }
                    }
                }
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
                Crashlytics.logException(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
