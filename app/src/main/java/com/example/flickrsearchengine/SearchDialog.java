package com.example.flickrsearchengine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class SearchDialog extends AppCompatDialogFragment {
    private EditText searchText;
    private SearchDialogListener listener;
    String searchedWord;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_dialog,null);
        builder.setView(view).setNegativeButton("Çık", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .setPositiveButton("Ara", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String searchedWord=searchText.getText().toString();
                        listener.applyTexts(searchedWord);

                    }
                });
        searchText=view.findViewById(R.id.searchText);
        return builder.create();

    }

    public void setListener(SearchDialogListener listener) {
        this.listener = listener;
    }


    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }

    public interface SearchDialogListener{
        void applyTexts(String searchedWord);


    }
}
