package tsdday.com.yts.tsdday.model;


import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Album extends RealmObject {
    @PrimaryKey
    public String date;

    public String title;
    public String content;
    public Boolean isLike;
    public RealmList<AlbumItem> items = new RealmList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLike() {
        return isLike != null ? isLike : false;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public List<AlbumItem> getItems() {
        return items;
    }

    public void setItems(RealmList<AlbumItem> items) {
        this.items = items;
    }

}
