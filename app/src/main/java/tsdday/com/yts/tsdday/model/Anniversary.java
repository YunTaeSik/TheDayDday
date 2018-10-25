package tsdday.com.yts.tsdday.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Anniversary extends RealmObject {
    @PrimaryKey
    public String title;

    public String date;
    public int dday;
    public String ddayString;
    public Boolean isAdded;
    public Boolean isRepeatYear;
    public Boolean isRepeatMonth;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDday() {
        return dday;
    }

    public void setDday(int dday) {
        this.dday = dday;
    }

    public String getDdayString() {
        return ddayString;
    }

    public void setDdayString(String ddayString) {
        this.ddayString = ddayString;
    }

    public Boolean getAdded() {
        return isAdded != null ? isAdded : false;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public Boolean getRepeatYear() {
        return isRepeatYear != null ? isRepeatYear : false;
    }

    public void setRepeatYear(Boolean repeat) {
        isRepeatYear = repeat;
    }


    public Boolean getRepeatMonth() {
        return isRepeatMonth != null ? isRepeatMonth : false;
    }

    public void setRepeatMonth(Boolean repeatMonth) {
        isRepeatMonth = repeatMonth;
    }
}
