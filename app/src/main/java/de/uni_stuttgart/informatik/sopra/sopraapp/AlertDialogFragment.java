package de.uni_stuttgart.informatik.sopra.sopraapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class AlertDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (this instanceof DialogInterface.OnClickListener) {
            listener = (DialogInterface.OnClickListener) this;
        }
        Log.d("onAttach", "Working");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WifiConnect wifiConnect = new WifiConnect();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage("Es besteht bereits eine aktive WLAN Verbindung. Falls sie nicht mit dem WLAN Netzwerk '" + wifiConnect.ssid +
                "' verbunden sind löschen Sie bitte alle Ihre gespeicherten WLAN Netzwerke");
        builder.setCancelable(false);
        builder.setPositiveButton("Verstanden", listener);
        Log.d("Dialog", "DialogWorking");
        return builder.create();
    }
}

