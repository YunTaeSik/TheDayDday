package tsdday.com.yts.tsdday.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.airbnb.lottie.LottieAnimationView;

import java.util.concurrent.Executors;

import tsdday.com.yts.tsdday.R;
import tsdday.com.yts.tsdday.util.ToastMake;

public class LockActivity extends AppCompatActivity implements View.OnClickListener {
    private LottieAnimationView btn_login;
    private BiometricPrompt.PromptInfo mBiPromptInfo;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        btn_login = findViewById(R.id.btn_login);
        mProgress = findViewById(R.id.progress);

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))) { // 지원 안하면
            startActivity(new Intent(LockActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }


        btn_login.setOnClickListener(this);

        mBiPromptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.app_name))
                .setNegativeButtonText(getString(R.string.cancel))
                .build();
    }

    private BiometricPrompt.AuthenticationCallback authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);

            startActivity(new Intent(LockActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();

            Log.e("onAuthenticationFailed", "onAuthenticationFailed");
        }
    };

    @Override
    public void onClick(View v) {
        try {
            BiometricPrompt biometricPrompt = new BiometricPrompt(this, Executors.newSingleThreadExecutor(), authenticationCallback);
            biometricPrompt.authenticate(mBiPromptInfo);
        }catch (Exception e){
            ToastMake.make(this,"오류가 발생했습니다.");
            startActivity(new Intent(LockActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            e.printStackTrace();
        }
    }
}
