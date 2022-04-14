package com.example.empowered;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class SalesDialog extends AppCompatDialogFragment {

    private TextInputEditText editName;
    private TextInputEditText editEircode;
    private TextInputEditText editMobile;
    private TextInputEditText editSupplier;
    private TextInputEditText editReturn;
    private TextInputEditText editTime;
    private TextInputEditText editComment;

    private SalesDialogListener listener;


    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder salesInfo = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_salesinput, null);

        editName = view.findViewById(R.id.name_input);
        editEircode = view.findViewById(R.id.eircode_input);
        editMobile = view.findViewById(R.id.mobile_input);
        editSupplier = view.findViewById(R.id.supplier_input);
        editReturn = view.findViewById(R.id.return_input);
        editTime = view.findViewById(R.id.time_input);
        editComment = view.findViewById(R.id.comments_input);

        salesInfo.setView(view)
                .setTitle("Customer Information")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String name = editName.getText().toString();
                        String eircode = editEircode.getText().toString();
                        String mobile = editMobile.getText().toString();
                        String supplier = editSupplier.getText().toString();
                        String returns = editReturn.getText().toString();
                        String time = editTime.getText().toString();
                        String comments = editComment.getText().toString();


                        listener.applyTexts(name, eircode, mobile, supplier, returns, time, comments);

                    }
                });


        return salesInfo.create();

    }

    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SalesDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement SalesDialogListener ");
        }
    }

    public interface SalesDialogListener{

        void applyTexts(String name, String eircode, String mobile, String supplier, String returns, String time,
                        String comments);
    }
}