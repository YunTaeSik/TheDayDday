package tsdday.com.yts.tsdday.util;

import android.content.Context;

import com.crashlytics.android.Crashlytics;

import java.sql.Time;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.model.Anniversary;
import tsdday.com.yts.tsdday.model.Couple;

public class DateFormat {

    public static String getLocaleDateString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (E)");
        Date d = null;
        try {
            d = formatter.parse(date);
            formatter.format(d);
            return formatter.format(d);
        } catch (ParseException e) {
            Crashlytics.logException(e);
        }
        return "";
    }

    public static long getStringToTime(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (E)");
        Date d = null;
        try {
            d = formatter.parse(date);
            formatter.format(d);
            return d.getTime();
        } catch (ParseException e) {
            Crashlytics.logException(e);
        }
        return 0;
    }

    public static GregorianCalendar getCalendarFromString(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (E)");

        Calendar current = Calendar.getInstance();
        GregorianCalendar calendar = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE), 0, 0, 0);
        try {
            calendar.setTime(formatter.parse(date));
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return calendar;
    }

    public static String getDateString(Calendar gregorianCalendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (E)");
        return formatter.format(gregorianCalendar.getTime());
    }

    public static String getDateString(Calendar gregorianCalendar, boolean isStartOne) {
        int date = isStartOne ? gregorianCalendar.get(Calendar.DATE) : gregorianCalendar.get(Calendar.DATE) - 1;
        GregorianCalendar curreuntCalendar = new GregorianCalendar(gregorianCalendar.get(Calendar.YEAR), gregorianCalendar.get(Calendar.MONTH), date, 0, 0, 0);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd (E)");
        return formatter.format(curreuntCalendar.getTime());
    }

    public static int getDdayFromCalendar(GregorianCalendar calendar, boolean isStartOne) {
        Calendar current = Calendar.getInstance();
        int date = isStartOne ? current.get(Calendar.DATE) : current.get(Calendar.DATE) + 1;
        GregorianCalendar curreuntCalendar = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), date, 0, 0, 0);

        int dday = (int) ((curreuntCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000 / 3600 / 24);

        return dday;
    }

    public static String getDdayStringFromCalendar(Context context, GregorianCalendar calendar, boolean isStartOne) {
        Calendar current = Calendar.getInstance();
        int date = isStartOne ? current.get(Calendar.DATE) : current.get(Calendar.DATE) + 1;
        GregorianCalendar curreuntCalendar = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), date, 0, 0, 0);

        int dday = (int) ((curreuntCalendar.getTimeInMillis() - calendar.getTimeInMillis()) / 1000 / 3600 / 24);

        if (dday == 0) {
            return "D-Day";
        } else if (dday > 0) {
            return dday + context.getString(R.string.days_ago);
        }
        return Math.abs(dday) + context.getString(R.string.days_remaining);
    }

    public static String getDdayStringFromCouple(Context context, Couple couple) {
        Calendar current = Calendar.getInstance();
        int date = couple.getStartOne() ? current.get(Calendar.DATE) : current.get(Calendar.DATE) + 1;
        GregorianCalendar curreuntCalendar = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), date, 0, 0, 0);

        GregorianCalendar startCalendar = getCalendarFromString(couple.getStartDate());
        int dday = (int) ((curreuntCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / 1000 / 3600 / 24);

        return dday + " " + context.getString(R.string.day);
    }

    /**
     * 일반 기념일 객체 생성
     */
    public static Anniversary getAnniversaryFromCalender(Context context, GregorianCalendar gregorianCalendar, String title, boolean isStartOne) {
        Anniversary anniversary = new Anniversary();
        anniversary.setTitle(title);

        anniversary.setDate(getDateString(gregorianCalendar, isStartOne));
        anniversary.setDday(getDdayFromCalendar(gregorianCalendar, isStartOne));
        anniversary.setDdayString(getDdayStringFromCalendar(context, gregorianCalendar, isStartOne));
        anniversary.setAdded(false);
        return anniversary;
    }

    /**
     * 저장되어있는 기념일로부터 dday 계산 및 ddayString 계산
     */
    public static Anniversary getAnniversaryFromAnniversary(Context context, Anniversary anniversary, boolean isStartOne) {

        Calendar currentCalendar = Calendar.getInstance();
        GregorianCalendar current = new GregorianCalendar(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DATE), 0, 0, 0);

        GregorianCalendar dateCal = getCalendarFromString(anniversary.getDate());
        if (anniversary.getRepeatYear()) {
            dateCal.set(Calendar.YEAR, current.get(Calendar.YEAR));
        }
        if (anniversary.getRepeatMonth()) {
            dateCal.set(Calendar.YEAR, current.get(Calendar.YEAR));
            dateCal.set(Calendar.MONTH, current.get(Calendar.MONTH));
        }


        anniversary.setDday(getDdayFromCalendar(dateCal, true));
        anniversary.setDdayString(getDdayStringFromCalendar(context, dateCal, true));

        anniversary.setAdded(false);
        return anniversary;
    }

    public static void setSpecialAnniversaryList(Context context, List<Anniversary> anniversaryArrayList, GregorianCalendar current, Couple couple) {
        if (couple.getOneUser().getBirth() != null && couple.getOneUser().getName() != null) {
            GregorianCalendar oneUserBirth = getCalendarFromString(couple.getOneUser().getBirth());
            oneUserBirth.set(Calendar.YEAR, current.get(Calendar.YEAR));
            anniversaryArrayList.add(getAnniversaryFromCalender(context, oneUserBirth, couple.getOneUser().getName() + " " + context.getString(R.string.birth), true));
        }
        if (couple.getTwoUser().getBirth() != null && couple.getTwoUser().getName() != null) {
            GregorianCalendar twoUserBirth = getCalendarFromString(couple.getTwoUser().getBirth());
            twoUserBirth.set(Calendar.YEAR, current.get(Calendar.YEAR));
            anniversaryArrayList.add(getAnniversaryFromCalender(context, twoUserBirth, couple.getTwoUser().getName() + " " + context.getString(R.string.birth), true));
        }
        if (SharedPrefsUtils.getBooleanPreference(context, Keys.isSpecialAnniversaryList, true)) {
            GregorianCalendar valentine_day = new GregorianCalendar(current.get(Calendar.YEAR), 1, 14, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, valentine_day, context.getString(R.string.valentine_day), true));

            GregorianCalendar white_day = new GregorianCalendar(current.get(Calendar.YEAR), 2, 14, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, white_day, context.getString(R.string.white_day), true));

            GregorianCalendar rose_day = new GregorianCalendar(current.get(Calendar.YEAR), 4, 14, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, rose_day, context.getString(R.string.rose_day), true));

            GregorianCalendar kiss_day = new GregorianCalendar(current.get(Calendar.YEAR), 5, 14, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, kiss_day, context.getString(R.string.kiss_day), true));

            GregorianCalendar pepero_day = new GregorianCalendar(current.get(Calendar.YEAR), 10, 11, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, pepero_day, context.getString(R.string.pepero_day), true));

            GregorianCalendar hug_day = new GregorianCalendar(current.get(Calendar.YEAR), 11, 14, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, hug_day, context.getString(R.string.hug_day), true));

            GregorianCalendar christmas = new GregorianCalendar(current.get(Calendar.YEAR), 11, 25, 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, christmas, context.getString(R.string.christmas), true));
        }
    }

    public static void setAnniversaryList(Context context, List<Anniversary> anniversaryArrayList, Couple couple) {
        GregorianCalendar startCalendar = getCalendarFromString(couple.getStartDate());
        for (int i = 1; i < 101; i++) {
            GregorianCalendar dayCalendar = new GregorianCalendar(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE) + (100 * i), 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, dayCalendar, (100 * i) + context.getString(R.string.day), couple.getStartOne()));

            GregorianCalendar yearCalendar = new GregorianCalendar(startCalendar.get(Calendar.YEAR) + i, startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DATE), 0, 0, 0);
            anniversaryArrayList.add(getAnniversaryFromCalender(context, yearCalendar, (i) + context.getString(R.string.anniversary), couple.getStartOne()));
        }
    }


    /**
     * 기념일 출력
     *
     * @return
     */
    public static List<Anniversary> getAnniversaryListFromCouple(Context context, Couple couple) {
        List<Anniversary> anniversaryArrayList = new ArrayList<>();
        if (couple != null) {
            Calendar current = Calendar.getInstance();
            GregorianCalendar curreuntCalendar = new GregorianCalendar(current.get(Calendar.YEAR), current.get(Calendar.MONTH), current.get(Calendar.DATE), 0, 0, 0);

            setSpecialAnniversaryList(context, anniversaryArrayList, curreuntCalendar, couple);

            setAnniversaryList(context, anniversaryArrayList, couple);
        }
        return anniversaryArrayList;
    }

    public static Comparator<Anniversary> comparator = new Comparator<Anniversary>() {
        @Override
        public int compare(Anniversary anniversary, Anniversary anniversary2) {
            return anniversary.getDday() > anniversary2.getDday() ? -1 : 1;
        }
    };
}
