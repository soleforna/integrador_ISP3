package com.rocketteam.passkeeper.util;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

public class BiometricUtils {

    public static boolean isBiometricPromptEnabled(Context context) {
        // Verificar si el dispositivo tiene capacidad de autenticación biométrica
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                == BiometricManager.BIOMETRIC_SUCCESS;
    }

    public static void showBiometricPrompt(FragmentActivity activity, BiometricPrompt.AuthenticationCallback callback) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación biométrica")
                .setSubtitle("Utiliza tu huella para iniciar sesión")
                .setNegativeButtonText("Cancelar")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, activity.getMainExecutor(), callback);
        biometricPrompt.authenticate(promptInfo);
    }
}
