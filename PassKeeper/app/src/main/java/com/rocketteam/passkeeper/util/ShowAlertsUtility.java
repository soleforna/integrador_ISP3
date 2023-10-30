package com.rocketteam.passkeeper.util;

import cn.pedant.SweetAlert.SweetAlertDialog;

import android.content.Context;
import android.graphics.Color;
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

    public static SweetAlertDialog mostrarSweetAlertDeletePassword(Context context, int tipo, String titulo, String mensaje, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (context != null) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, tipo);
            sweetAlertDialog.setTitleText(titulo);
            sweetAlertDialog.setContentText(mensaje);
            sweetAlertDialog.setConfirmText("Aceptar");
            sweetAlertDialog.setCancelText("Cancelar");

            sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
                sweetAlertDialog1.dismissWithAnimation();
                if (confirmListener != null) {
                    confirmListener.onClick(sweetAlertDialog1);
                }
            });

            sweetAlertDialog.setCancelClickListener(sweetAlertDialog1 -> {
                sweetAlertDialog1.dismissWithAnimation();
                if (cancelListener != null) {
                    cancelListener.onClick(sweetAlertDialog1);
                }
            });

            sweetAlertDialog.show();

            return sweetAlertDialog; // Devuelve la instancia de la alerta
        } else {
            Log.e("ShowAlertsUtility", "El contexto es nulo en mostrarSweetAlertDeletePassword");
            return null;
        }
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
