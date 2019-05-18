package ru.com.testdribbble.core.data;

import org.androidannotations.annotations.EBean;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.com.testdribbble.core.data.model.Shot;

@EBean
public class ShotsDb {

    private RealmConfiguration mRealmConfiguration;

    public ShotsDb() {
        mRealmConfiguration = Realm.getDefaultConfiguration();
    }

    public Single<List<Shot>> getShots() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        List<Shot> shots = realm.copyFromRealm(realm.where(Shot.class).findAll());
        realm.close();
        return Single.just(shots);
    }

    public Single<Shot> getShot(long id) {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        Shot shot = null;
        Shot realmShot = realm.where(Shot.class).equalTo("id", id).findFirst();
        if (realmShot != null) {
            shot = realm.copyFromRealm(realmShot);
        }
        realm.close();
        return Single.just(shot);
    }

    public Completable updateShots(List<Shot> shots) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(mRealmConfiguration);
            realm.executeTransaction(realm1 -> {
                realm.insertOrUpdate(shots);
            });
            realm.close();
            Realm.compactRealm(mRealmConfiguration);
        });
    }

    public Completable updateShot(Shot shot) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(mRealmConfiguration);
            realm.executeTransaction(realm1 -> {
                realm.insertOrUpdate(shot);
            });
            realm.close();
            Realm.compactRealm(mRealmConfiguration);
        });
    }

    public Completable clearAllShots() {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(mRealmConfiguration);
            realm.executeTransaction(realm1 -> {
                realm.deleteAll();
            });
            realm.close();
            Realm.compactRealm(mRealmConfiguration);
        });
    }

    public boolean isEmpty() {
        Realm realm = Realm.getInstance(mRealmConfiguration);
        boolean isEmpty = realm.isEmpty();
        realm.close();
        return isEmpty;
    }
}
