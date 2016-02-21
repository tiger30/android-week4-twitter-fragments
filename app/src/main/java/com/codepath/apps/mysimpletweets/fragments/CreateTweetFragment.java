package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;

/**
 * Created by barbara on 2/18/16.
 */
public class CreateTweetFragment extends DialogFragment {
    public static final int MAX_COUNT = 140;

    public interface OnNewTweetCreatedListener {
        public void createTweet(String tweetBody);
    }

    private OnNewTweetCreatedListener tweetCreatedListener;
    private Button btnSave;
    private EditText etTweetBody;
    private TextView etCount;
    private int greenColor;
    private int grayColor;
    private int redColor;
    private int blackColor;
    private int blueColor;
    private int darkGrayColor;

    public CreateTweetFragment() {
    }

    public static CreateTweetFragment newInstance() {
        CreateTweetFragment frag = new CreateTweetFragment();
        return frag;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tweetCreatedListener = (OnNewTweetCreatedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnNewTweetCreatedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        getDialog().getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

// inflate and adjust layout
        //LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.create_tweet, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.8f));

//        dialog.setView(layout);
//
//
//        return inflater.inflate(R.layout.create_tweet, container);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        blackColor = getResources().getColor(R.color.black);
        redColor = getResources().getColor(R.color.red);
        grayColor = getResources().getColor(R.color.gray);
        blueColor = getResources().getColor(R.color.blue);
        darkGrayColor = getResources().getColor(R.color.dark_gray);

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setEnabled(false);
        btnSave.setBackgroundColor(grayColor);
        btnSave.setTextColor(darkGrayColor);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tweetCreatedListener.createTweet(etTweetBody.getText().toString());
                getDialog().dismiss();
            }
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        etCount = (TextView) view.findViewById(R.id.tvCount);

        etTweetBody = (EditText) view.findViewById(R.id.etTweetBody);
        etTweetBody.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length == 0) {
                    etCount.setText("");
                    btnSave.setEnabled(false);
                    btnSave.setBackgroundColor(grayColor);
                    btnSave.setTextColor(darkGrayColor);
                } else if (length <= MAX_COUNT) {
                    etCount.setText(Integer.toString(length));
                    etCount.setTextColor(blackColor);
                    btnSave.setEnabled(true);
                    btnSave.setBackgroundColor(blueColor);
                    btnSave.setTextColor(blackColor);
                } else {
                    etCount.setText(Integer.toString(MAX_COUNT - length));
                    etCount.setTextColor(redColor);
                    btnSave.setEnabled(false);
                    btnSave.setBackgroundColor(grayColor);
                    btnSave.setTextColor(darkGrayColor);
                }
            }
        });
    }



}
