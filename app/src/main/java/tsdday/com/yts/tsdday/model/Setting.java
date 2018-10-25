package tsdday.com.yts.tsdday.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Setting extends RealmObject {
    @PrimaryKey
    public long id;

    public Boolean isNotify;
    public Boolean isPremium;

}
