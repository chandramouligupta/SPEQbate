package com.qbate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTopicDialogFragment extends DialogFragment {

    String topicTitle;

    public interface InputTopicDetails{
        public void getInputTopicTitle(String inputTopicTitle);
    }

    InputTopicDetails inputTopicDetails;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Topic Title");
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View v = inflater.inflate(R.layout.add_topic, null);
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Add Topic To Table
                        EditText et = v.findViewById(R.id.add_topic_edittext);
                        inputTopicDetails.getInputTopicTitle(et.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Log.d("testing","cancel clicked");
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            inputTopicDetails = (InputTopicDetails) context;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " Must implement InputTopicDetails Interface methods");
        }
    }
}
