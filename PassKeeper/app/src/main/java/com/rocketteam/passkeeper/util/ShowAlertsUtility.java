package com.rocketteam.passkeeper.util;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.content.Context;
import android.util.Log;

public class ShowAlertsUtility {

    public static void mostrarSweetAlert(Context context, int tipo, String titulo, String mensaje, SweetAlertDialog.OnSweetClickListener listener) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, tipo);
        sweetAlertDialog.setTitleText(titulo);
        sweetAlertDialog.setContentText(mensaje);
        sweetAlertDialog.setConfirmText("Aceptar");

        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            sweetAlertDialog1.dismissWithAnimation();
            if (listener != null) {
                listener.onClick(sweetAlertDialog1);
            }
        });

        sweetAlertDialog.show();
    }


    /*public static void mostrarSweetAlert(Context context, int tipo, String titulo, String mensaje) {


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
    }*/
}
