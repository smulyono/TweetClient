package com.codepath.apps.tweetclient.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.activity.TimelineActivity;

/**
 * Created by smulyono on 3/7/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    private ImageView ivProfileInCompose;
    private EditText etCompose;
    private TextView tvCharacterLeft;
    private TimelineActivity parentActivity;

    public static ComposeTweetDialog newInstance(){
        ComposeTweetDialog newDialog = new ComposeTweetDialog();
        return newDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = (TimelineActivity) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View titleView = inflater.inflate(R.layout.compose_tweet_title, null);
        View bodyView = inflater.inflate(R.layout.compose_tweet, null);


        tvCharacterLeft = (TextView) titleView.findViewById(R.id.tvCharcount);
        etCompose = (EditText) bodyView.findViewById(R.id.etCompose);

        etCompose.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int charCount = (140 - etCompose.getText().length());
                tvCharacterLeft.setText(charCount + " character" + (charCount > 1 ? "s" : "") + " left");
                return false;
            }
        });

        etCompose.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                parentActivity.onCompose(etCompose.getText().toString());
                getDialog().dismiss();
                return false;
            }
        });

        builder.setCustomTitle(titleView)
               .setView(bodyView)
                .setPositiveButton(R.string.compose_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parentActivity.onCompose(etCompose.getText().toString());
                        dismiss();
                    }
                });

        return builder.create();
    }
}
