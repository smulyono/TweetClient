package com.codepath.apps.tweetclient.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetclient.R;
import com.codepath.apps.tweetclient.activity.TimelineActivity;
import com.squareup.picasso.Picasso;

/**
 * Created by smulyono on 3/7/15.
 */
public class ComposeTweetDialog extends DialogFragment {

    private ImageView ivProfileInCompose;
    private TextView tvCharacterLeft;
    private TextView tvTitle;

    private EditText etCompose;
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
        ivProfileInCompose = (ImageView) titleView.findViewById(R.id.ivProfileImageInCompose);
        tvTitle = (TextView) titleView.findViewById(R.id.tvTitle);

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

        if (parentActivity.userInfo != null){
            ivProfileInCompose.setImageResource(R.mipmap.ic_nopic);
            // load user info image
            Picasso.with(getActivity().getApplicationContext())
                    .load(parentActivity.userInfo.getProfileImageUrl())
                    .into(ivProfileInCompose);

            tvTitle.setText(Html.fromHtml(parentActivity.userInfo.getName() + "<br /> @" + parentActivity.userInfo.getScreenName()));
        }

        builder.setCustomTitle(titleView)
               .setView(bodyView)
                .setPositiveButton(R.string.compose_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parentActivity.onCompose(etCompose.getText().toString());
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().cancel();
                    }
                })
        ;

        return builder.create();
    }
}
