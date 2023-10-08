package com.rocketteam.passkeeper.util;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;

public class InputTextWatcher implements TextWatcher {

    private TextInputLayout textInputLayout;

    public InputTextWatcher(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Realiza acciones según el texto cambiado
        String input = s.toString().trim();
        if (input.isEmpty()) {
            textInputLayout.setError("Campo obligatorio");
        } else {
            textInputLayout.setError(null); // Borra el mensaje de error si el campo no está vacío
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
