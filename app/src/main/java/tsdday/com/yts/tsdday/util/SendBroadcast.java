package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;


public class SendBroadcast {

    public static void close(Context context) {
        Intent send = new Intent(Keys.SEND_CLOSE);
        context.sendBroadcast(send);
    }

    public static void hideKeyboard(Context context) {
        Intent send = new Intent(Keys.SEND_HIDE_KEYBOARD);
        context.sendBroadcast(send);
    }

    public static void startLottieAnimationEmpty(Context context) {
        Intent send = new Intent(Keys.SEND_LOTTIE_ANIMATION_EMPTY);
        context.sendBroadcast(send);
    }

    public static void stopLottieAnimationEmpty(Context context) {
        Intent send = new Intent(Keys.SEND_LOTTIE_ANIMATION_EMPTY_STOP);
        context.sendBroadcast(send);
    }

    public static void startDateDialog(Context context, String date) {
        Intent send = new Intent(Keys.SEND_START_DATE_DIALOG);
        send.putExtra(Keys.DATE, date);
        context.sendBroadcast(send);
    }

    public static void changeDate(Context context, String date) {
        Intent send = new Intent(Keys.SEND_DATE_CHANGE);
        send.putExtra(Keys.DATE, date);
        context.sendBroadcast(send);
    }

    public static void startAlbum(Context context, String date) {
        Intent send = new Intent(Keys.SEND_START_ALBUM);
        send.putExtra(Keys.DATE, date);
        context.sendBroadcast(send);
    }

    public static void editAlbum(Context context) {
        Intent send = new Intent(Keys.SEND_EDIT_ALBUM);
        context.sendBroadcast(send);
    }

    public static void likeAlbum(Context context, int position) {
        Intent send = new Intent(Keys.SEND_LIKE);
        send.putExtra(Keys.POSITION, position);
        context.sendBroadcast(send);
    }

    public static void deleteAlbum(Context context, int position) {
        Intent send = new Intent(Keys.SEND_DELETE);
        send.putExtra(Keys.POSITION, position);
        context.sendBroadcast(send);
    }

    public static void deleteAlbumImage(Context context, int position) {
        Intent send = new Intent(Keys.SEND_DELETE_ALBUM_IMAGE);
        send.putExtra(Keys.POSITION, position);
        context.sendBroadcast(send);
    }

    public static void addAlbum(Context context, ArrayList<String> imageUrls) {
        Intent send = new Intent(Keys.SEND_IMAGE_URLS);
        send.putExtra(Keys.IMAGE_URLS, imageUrls);
        context.sendBroadcast(send);
    }

    public static void albumList(Context context) {
        Intent send = new Intent(Keys.SEND_ALBUM_LIST_MODE);
        context.sendBroadcast(send);
    }

    public static void albumGrid(Context context) {
        Intent send = new Intent(Keys.SEND_ALBUM_GRID_MODE);
        context.sendBroadcast(send);
    }


}
