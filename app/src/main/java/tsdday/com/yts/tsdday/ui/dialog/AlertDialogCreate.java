package tsdday.com.yts.tsdday.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import tsdday.com.yts.tsdday.R;

public class AlertDialogCreate {
    private AlertDialog.Builder alertDialog;

    public AlertDialogCreate(Context context) {
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    public void delete(DialogInterface.OnClickListener clickListener) {
        alertDialog.setTitle(R.string.delete_album);
        alertDialog.setMessage(R.string.delete_album_message);
        alertDialog.setPositiveButton(R.string.delete, clickListener);
        alertDialog.show();
    }

    public void deleteAlbumItem(DialogInterface.OnClickListener clickListener) {
        alertDialog.setTitle(R.string.delete);
        alertDialog.setMessage(R.string.delete_album_image);
        alertDialog.setPositiveButton(R.string.delete, clickListener);
        alertDialog.show();
    }

    public void deleteAnniversary(DialogInterface.OnClickListener clickListener) {
        alertDialog.setTitle(R.string.delete);
        alertDialog.setMessage(R.string.delete_anniversary);
        alertDialog.setPositiveButton(R.string.delete, clickListener);
        alertDialog.show();
    }
}
