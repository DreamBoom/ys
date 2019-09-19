package com.nestia.biometriclib;

import android.os.CancellationSignal;
import android.support.annotation.NonNull;

/**
 * Created by gaoyang on 2018/06/19.
 */
interface OpenIBiometricPromptImpl {

    void authenticate(@NonNull CancellationSignal cancel,
                      @NonNull OpenBiometricPromptManager.Callback callback);

}
