package com.nestia.biometriclib;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.nestia.biometriclib.OpenBiometricPromptDialog.OpenCallback;

/**
 * Created by gaoyang on 2018/06/19.
 */
@RequiresApi(Build.VERSION_CODES.M)
public class OpenBiometricPromptApi23 implements OpenIBiometricPromptImpl{

    private static final String TAG = "BiometricPromptApi23";
    private Activity mActivity;
    private OpenBiometricPromptDialog mDialog;
    private FingerprintManager mFingerprintManager;
    private CancellationSignal mCancellationSignal;
    private OpenBiometricPromptManager.Callback mManagerIdentifyCallback;
    private FingerprintManager.AuthenticationCallback mFmAuthCallback
            = new OpenFingerprintManageCallbackImpl();

    public OpenBiometricPromptApi23(Activity activity) {
        mActivity = activity;

        mFingerprintManager = getFingerprintManager(activity);
    }

    @Override
    public void authenticate(@Nullable CancellationSignal cancel,
                             @NonNull OpenBiometricPromptManager.Callback callback) {
        mCancellationSignal = cancel;
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        //指纹识别的回调
        mManagerIdentifyCallback = callback;
        /**
         * 我实现了一个自定义dialog，
         * BiometricPromptDialog.OnBiometricPromptDialogActionCallback是自定义dialog的回调
         */
        mDialog = new OpenBiometricPromptDialog();
        mDialog.OpenCallback(new OpenCallback() {
            @Override
            public void onDialogDismiss() {
                //当dialog消失的时候，包括点击userPassword、点击cancel、和识别成功之后
                if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
                    mCancellationSignal.cancel();
                }
            }

            @Override
            public void onCancel() {
                //点击cancel键
                mDialog.dismiss();
            }

        });
        mDialog.show(mActivity.getFragmentManager(), TAG);

        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            getFingerprintManager(mActivity).authenticate(
                    cryptoObjectHelper.buildCryptoObject(), mCancellationSignal,
                    0, mFmAuthCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class OpenFingerprintManageCallbackImpl extends FingerprintManager.AuthenticationCallback {

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
           // Log.d(TAG, "onAuthenticationError() called with: errorCode = [" + errorCode + "], errString = [" + errString + "]");
            mDialog.setState(BiometricPromptDialog.STATE_ERROR);
            mManagerIdentifyCallback.onError(errorCode, errString.toString());
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Log.d(TAG, "onAuthenticationFailed() called");
           mDialog.setState(BiometricPromptDialog.STATE_FAILED);
            mManagerIdentifyCallback.onFailed();
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
           // Log.d(TAG, "onAuthenticationHelp() called with: helpCode = [" + helpCode + "], helpString = [" + helpString + "]");
//            mDialog.setState(BiometricPromptDialog.STATE_FAILED);
//            mManagerIdentifyCallback.onFailed();

        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
          //  Log.i(TAG, "onAuthenticationSucceeded: ");
            mDialog.setState(BiometricPromptDialog.STATE_SUCCEED);

            mManagerIdentifyCallback.onSucceeded();

        }
    }

    private FingerprintManager getFingerprintManager(Context context) {
        if (mFingerprintManager == null) {
            mFingerprintManager = context.getSystemService(FingerprintManager.class);
        }
        return mFingerprintManager;
    }
    public boolean isHardwareDetected() {
        if (mFingerprintManager != null) {
            return mFingerprintManager.isHardwareDetected();
        }
        return false;
    }

    public boolean hasEnrolledFingerprints() {
        if (mFingerprintManager != null) {
            return mFingerprintManager.hasEnrolledFingerprints();
        }
        return false;
    }
}