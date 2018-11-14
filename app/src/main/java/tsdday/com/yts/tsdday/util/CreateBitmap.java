package tsdday.com.yts.tsdday.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;

public class CreateBitmap {
    public static File create(Context context, Bitmap bitmap) {
        try {
            ContextWrapper contextWrapper = new ContextWrapper(context);

            File file = contextWrapper.getCacheDir(); // 프로바이더의 이름이 같아야함 provider.xml
            if (!file.exists()) {
                file.mkdirs();
            }



            File image = new File(file, System.currentTimeMillis() + ".png");

            FileOutputStream fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush(); // Not really required
            fileOutputStream.close();
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap loadBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return bitmap;
    }
}
