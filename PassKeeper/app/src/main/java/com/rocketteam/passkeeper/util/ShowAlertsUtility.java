package com.rocketteam.passkeeper.util;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.content.Context;
import android.util.Log;

public class ShowAlertsUtility {

    /**
     * Muestra un diálogo emergente SweetAlertDialog con un título, mensaje y botón de aceptar personalizado.
     *
     * @param context  El contexto de la actividad en dode se utilizará
     * @param tipo     El tipo de diálogo SweetAlertDialog (por ejemplo, SweetAlertDialog.ERROR_TYPE).
     * @param titulo   El título que se mostrará en el diálogo.
     * @param mensaje  El mensaje que se mostrará en el diálogo.
     */
    public static void mostrarSweetAlert(Context context, int tipo, String titulo, String mensaje) {
        // Registra un mensaje de depuración para indicar el tipo de SweetAlertDialog.
        Log.i("TAG", "Mostrando SweetAlertDialog de tipo: " + tipo);
        // Crea una instancia de SweetAlertDialog con el contexto actual y el tipo especificado.
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, tipo);
        // Establece el título del diálogo.
        sweetAlertDialog.setTitleText(titulo);
        // Establece el mensaje del diálogo.
        sweetAlertDialog.setContentText(mensaje);
        // Establece el texto del botón de confirmación como "Aceptar".
        sweetAlertDialog.setConfirmText("Aceptar");
        // Establece un escuchador para el botón de confirmación.
        sweetAlertDialog.setConfirmClickListener(SweetAlertDialog::dismissWithAnimation);
        sweetAlertDialog.show();
    }
}
