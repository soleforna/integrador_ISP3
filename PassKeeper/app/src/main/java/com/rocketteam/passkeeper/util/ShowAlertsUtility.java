package com.rocketteam.passkeeper.util;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.content.Context;

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
        sweetAlertDialog.setTitleText(titulo);
        sweetAlertDialog.setContentText(mensaje);
        sweetAlertDialog.setConfirmText("Aceptar");

        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            sweetAlertDialog1.dismissWithAnimation();

            if (tipo == SweetAlertDialog.ERROR_TYPE) {
                // Manejar el comportamiento específico en caso de error
            }
        });

        sweetAlertDialog.show();
    }*/
}
