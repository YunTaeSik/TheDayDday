package tsdday.com.yts.tsdday.model.migration;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import tsdday.com.yts.tsdday.model.AlbumItem;

public class Migration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {
            RealmObjectSchema coupleSchema = schema.get("Couple");
            coupleSchema.addField("number", String.class);
            oldVersion++;
        }
        if (oldVersion == 1) {
            RealmObjectSchema albumSchema = schema.get("Album");
            albumSchema.addField("content", String.class);
            oldVersion++;
        }

    }


}
