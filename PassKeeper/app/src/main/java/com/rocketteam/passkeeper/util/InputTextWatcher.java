package com.rocketteam.passkeeper.util;

import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputLayout;

/**
 * Esta clase implementa la interfaz TextWatcher y se utiliza para validar los campos de texto
 * en tiempo real. Muestra un mensaje de error si el campo está vacío y borra el mensaje de
 * error si el campo no está vacío.
 */
public class InputTextWatcher implements TextWatcher {

    private final TextInputLayout textInputLayout;

    /**
     * Constructor de la clase InputTextWatcher.
     *
     * @param textInputLayout El TextInputLayout al que se asociará el TextWatcher.
     */
    public InputTextWatcher(TextInputLayout textInputLayout) {
        this.textInputLayout = textInputLayout;
    }

    /**
     * Se llama cuando el texto en el EditText cambia.
     *
     * @param s      La secuencia de caracteres actual en el EditText.
     * @param start  El índice de inicio del cambio en el texto.
     * @param before La longitud del texto que fue reemplazado.
     * @param count  La longitud del nuevo texto.
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Realiza acciones según el texto cambiado
        String input = s.toString().trim();
        if (input.isEmpty()) {
            textInputLayout.setError("Ingrese una opción válida.");
        } else {
            textInputLayout.setError(null); // Borra el mensaje de error si el campo no está vacío
        }
    }

    /**
     * Se llama para notificar que algún texto dentro del EditText está a punto de ser reemplazado.
     *
     * @param s      La secuencia de caracteres actual en el EditText.
     * @param start  El índice de inicio del cambio en el texto.
     * @param count  La longitud del texto que será reemplazado.
     * @param after  La longitud del nuevo texto que reemplazará al antiguo.
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No se utiliza en esta implementación
    }

    /**
     * Se llama para notificar que algún texto dentro del EditText ha sido reemplazado.
     *
     * @param s La secuencia de caracteres actual en el EditText después de los cambios.
     */
    @Override
    public void afterTextChanged(Editable s) {
        // No se utiliza en esta implementación
    }
}

