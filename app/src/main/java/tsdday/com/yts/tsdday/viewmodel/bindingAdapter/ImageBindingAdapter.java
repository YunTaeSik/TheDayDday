package tsdday.com.yts.tsdday.viewmodel.bindingAdapter;

import android.content.Context;
import android.content.res.ColorStateList;

import androidx.databinding.BindingAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import tsdday.com.yts.tsdday.GlideApp;
import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.model.Album;
import tsdday.com.yts.tsdday.model.AlbumItem;
import tsdday.com.yts.tsdday.model.Couple;
import tsdday.com.yts.tsdday.model.User;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.SharedPrefsUtils;

public class ImageBindingAdapter {
    @BindingAdapter("setTint")
    public static void setTint(final View view, boolean isTint) {
        if (isTint) {
            Context context = view.getContext();
            int colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
            int colorPrimary = ContextCompat.getColor(context, R.color.colorAccent);
            ColorStateList backgorundColorPrimary = ContextCompat.getColorStateList(context, R.color.colorPrimary);

            int themeStyle = SharedPrefsUtils.getIntegerPreference(context, Keys.themeStyle, 0);
            if (themeStyle == 0) {
                colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
                backgorundColorPrimary = ContextCompat.getColorStateList(context, R.color.colorPrimary);
            }

            if (view instanceof FloatingActionButton) {
                ((FloatingActionButton) view).setColorFilter(colorAccent);
                ((FloatingActionButton) view).setSupportBackgroundTintList(backgorundColorPrimary);
            } else if (view instanceof LottieAnimationView) {
                ((LottieAnimationView) view).addValueCallback(
                        new KeyPath("**"),
                        LottieProperty.COLOR,
                        new LottieValueCallback<>(colorAccent));
            } else if (view instanceof ImageView) {
                ((ImageView) view).setColorFilter(colorAccent);
            }
        }
    }

    @BindingAdapter("setBackground")
    public static void setBackground(final View view, boolean isLight) {
        Context context = view.getContext();
        int themeStyle = SharedPrefsUtils.getIntegerPreference(context, Keys.themeStyle, 0);

        int colorAccent = ContextCompat.getColor(context, R.color.colorPrimary);
        if (themeStyle == 0) {
            if (isLight) {
                colorAccent = ContextCompat.getColor(context, R.color.colorPrimaryLight);
            } else {
                colorAccent = ContextCompat.getColor(context, R.color.colorPrimary);
            }
        }
        view.setBackgroundColor(colorAccent);
    }

    @BindingAdapter({"setLikeImage"})
    public static void setLikeImage(AppCompatImageView likeImage, boolean isLike) {
        if (isLike) {
            likeImage.setImageResource(R.drawable.ic_favorite_anim);
        } else {
            likeImage.setImageResource(R.drawable.ic_favorite_border_anim);
        }
    }


    @BindingAdapter("setImage")
    public static void setImage(final AppCompatImageView view, byte[] bytes) {
        GlideApp.with(view.getContext()).load(bytes).override(view.getMeasuredWidth(), view.getMeasuredHeight()).thumbnail(0.1f).centerCrop().into(view);
    }

    @BindingAdapter("setImage")
    public static void setImage(final AppCompatImageView view, Couple couple) {
        if (couple != null) {
            String imageDataPath = couple.getBackgroundPath();
            byte[] imageData = couple.getBackground();
            if (imageDataPath != null && imageDataPath.length() > 0) {
                GlideApp.with(view.getContext()).load(imageDataPath).override(view.getMeasuredWidth(), view.getMeasuredHeight()).thumbnail(0.1f).centerCrop().into(view);
            } else if (imageData != null) {
                GlideApp.with(view.getContext()).load(imageData).override(view.getMeasuredWidth(), view.getMeasuredHeight()).thumbnail(0.1f).centerCrop().into(view);
            }
        }
    }

    @BindingAdapter("setImageCircle")
    public static void setImageCircle(final AppCompatImageView view, User user) {
        if (user != null) {
            String imageDataPath = user.getImageDataPath();
            byte[] imageData = user.getImageData();
            if (imageDataPath != null && imageDataPath.length() > 0) {
                GlideApp.with(view.getContext()).load(imageDataPath).thumbnail(0.1f).transform(new CircleCrop()).override(view.getMeasuredWidth(), view.getMeasuredHeight()).into(view);
            } else if (imageData != null) {
                GlideApp.with(view.getContext()).load(imageData).thumbnail(0.1f).transform(new CircleCrop()).override(view.getMeasuredWidth(), view.getMeasuredHeight()).into(view);
            }
        }
    }

    @BindingAdapter("setImageCircle")
    public static void setImageCircle(final AppCompatImageView view, Drawable drawable) {
        GlideApp.with(view.getContext()).load(drawable).thumbnail(0.1f).transform(new CircleCrop()).override(view.getMeasuredWidth(), view.getMeasuredHeight()).into(view);
    }

    @BindingAdapter({"setAlbumImage"})
    public static void setAlbumImage(final AppCompatImageView view, Album album) {
        Log.e("test", "setAlbumImage");
        if (album.getItems() != null && album.getItems().size() > 0) {
            view.setVisibility(View.VISIBLE);
            setAlbumItemImage(view, album.getItems().get(0));
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"setAlbumItemImage"})
    public static void setAlbumItemImage(final PhotoView view, AlbumItem albumItem) {
        Log.e("test", "setAlbumImage");
        final Context context = view.getContext();
        if (albumItem != null) {
            String imageDataPath = albumItem.getImageDataPath();
            byte[] imageData = albumItem.getImageData();
            if (imageDataPath != null && imageDataPath.length() > 0) {
                GlideApp.with(context).load(imageDataPath).fitCenter().into(view);
            } else if (imageData != null) {
                GlideApp.with(context).load(imageData).fitCenter().into(view);
            }
        }
    }

    @BindingAdapter({"setAlbumItemImage"})
    public static void setAlbumItemImage(final AppCompatImageView view, AlbumItem albumItem) {
        Log.e("test", "setAlbumImage");
        if (albumItem != null) {
            String imageDataPath = albumItem.getImageDataPath();
            byte[] imageData = albumItem.getImageData();
            if (imageDataPath != null && imageDataPath.length() > 0) {
                GlideApp.with(view.getContext()).load(imageDataPath).override(view.getMeasuredWidth(), view.getMeasuredHeight()).centerCrop().into(view);
            } else if (imageData != null) {
                GlideApp.with(view.getContext()).load(imageData).override(view.getMeasuredWidth(), view.getMeasuredHeight()).centerCrop().into(view);
            }
        }
    }
}
