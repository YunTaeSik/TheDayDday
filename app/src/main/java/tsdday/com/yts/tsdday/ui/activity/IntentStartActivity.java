package tsdday.com.yts.tsdday.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.util.Keys;
import tsdday.com.yts.tsdday.util.ToastMake;

public class IntentStartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_start);

        String kind = getIntent().getStringExtra(Keys.KIND);
        String number = getIntent().getStringExtra(Keys.NUMBER);
        finish();
        try {
            if (number != null && number.length() > 0) {
                if (kind.equals(Keys.PHONE)) {
                    Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                    startActivity(dial);
                } else if (kind.equals(Keys.SMS)) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + number));
                    startActivity(intent);
                }
            } else {
                ToastMake.make(this, getString(R.string.hint_number));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
