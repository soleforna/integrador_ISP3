package com.rocketteam.passkeeper.util;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Clase personalizada que implementa TextWatcher para validar campos de texto en tiempo real.
 * Permite mostrar un mensaje de error en un TextInputLayout si el campo está vacío.
 */
public class InputTextWatcher implements TextWatcher {

    private TextInputLayout textInputLayout;

    /**
     * Constructor que toma un TextInputLayout como parámetro.
     *
     * @param textInputLayout TextInputLayout asociado al campo de texto que se debe validar.
     */
    public InputTextWatcher(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    /**
     * Se llama cuando el texto en el campo de texto cambia.
     *
     * @param s      Texto actual en el campo de texto.
     * @param start  Índice del primer carácter modificado.
     * @param before Número de caracteres eliminados.
     * @param count  Número de caracteres agregados.
     */
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

    /**
     * Se llama antes de que el texto en el campo de texto cambie.
     *
     * @param s      Texto actual en el campo de texto.
     * @param start  Índice del primer carácter a ser cambiado.
     * @param count  Número de caracteres a ser cambiados.
     * @param after  Número de caracteres que serán agregados después de 'start'.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Se llama después de que el texto en el campo de texto cambia.
     *
     * @param s Texto actual en el campo de texto después del cambio.
     */
    @Override
    public void afterTextChanged(Editable s) {
    }
}
