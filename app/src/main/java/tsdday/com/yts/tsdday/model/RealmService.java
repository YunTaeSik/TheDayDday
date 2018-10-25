package tsdday.com.yts.tsdday.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import tsdday.com.yts.tsdday.util.DateFormat;

public class RealmService {

    public static Single<RealmResults<Album>> getAlbumList(Realm realm, Boolean onlyLike) {
        if (onlyLike) {
            return Single.just(realm.where(Album.class).equalTo("isLike", true).findAll().sort("date", Sort.DESCENDING)/*.sort("isLike", Sort.DESCENDING)*/)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Single.just(realm.where(Album.class).findAll().sort("date", Sort.DESCENDING)/*.sort("isLike", Sort.DESCENDING)*/)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static Observable<Album> getAlbum(final Realm realm, final String date) {
        return Observable.create(new ObservableOnSubscribe<Album>() {
            @Override
            public void subscribe(ObservableEmitter<Album> emitter) throws Exception {
                Album realmAlbum = realm.where(Album.class).equalTo("date", date).findFirst();
                Album album = new Album();
                album.setDate(date);
                album = realmAlbum != null ? realm.copyFromRealm(realmAlbum) : album;
                emitter.onNext(album);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Album>> getAlbumList(Realm realm) {
        return Observable.just(realm.copyFromRealm(realm.where(Album.class).findAll().sort("date", Sort.DESCENDING)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Album>> getAlbumList(Realm realm, boolean onlyLike) {
        if (onlyLike) {
            return Observable.just(realm.copyFromRealm(realm.where(Album.class).equalTo("isLike", true).findAll().sort("date", Sort.DESCENDING)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Observable.just(realm.copyFromRealm(realm.where(Album.class).findAll().sort("date", Sort.DESCENDING)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    public static void saveAlbum(Realm realm, Album album, Realm.Transaction transaction) {
        realm.executeTransaction(transaction);
    }

    public static Single<Couple> createCouple(Realm realm) {
        Couple couple = new Couple();
        couple.setStartDate(DateFormat.getDateString(Calendar.getInstance()));
        couple.setOneUser(new User());
        couple.setTwoUser(new User());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(couple);
        realm.commitTransaction();
        return Single.just(realm.where(Couple.class).findFirst())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Single<Couple> getCouple(Realm realm, boolean hasCreate) {
        try {
            return Single.just(realm.where(Couple.class).findFirst())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } catch (Exception e) {
            if (hasCreate) {
                return createCouple(realm);
            }
        }
        return null;
    }

    public static List<Anniversary> getAnniversaryList(final Realm realm, final Context context, final Couple couple, final boolean onlyAdded) {
        RealmResults<Anniversary> anniversaries = realm.where(Anniversary.class).findAll();
        List<Anniversary> list = new ArrayList<>();
        try {
            list = realm.copyFromRealm(anniversaries);
            for (int i = 0; i < list.size(); i++) {
                Anniversary anniversary = list.get(i);
                list.set(i, DateFormat.getAnniversaryFromAnniversary(context, anniversary, couple.getStartOne()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        if (!onlyAdded) {
            list.addAll(DateFormat.getAnniversaryListFromCouple(context, couple));
        }
        Collections.sort(list, DateFormat.comparator);

        return list;
    }

    public static void like(Realm realm, String date, boolean isLike) {
        realm.beginTransaction();
        Album data = realm.where(Album.class).equalTo("date", date).findFirst();
        data.setLike(isLike);
        realm.commitTransaction();
    }

    public static void deleteAlbum(Realm realm, String date) {
        realm.beginTransaction();
        realm.where(Album.class).equalTo("date", date).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}
