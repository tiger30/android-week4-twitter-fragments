package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.twitter.TwitterApplication;
import com.codepath.apps.mysimpletweets.twitter.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by barbara on 2/18/16.
 */
public class CreateTweetFragment extends DialogFragment {
    public static final int MAX_COUNT = 140;

    public interface OnNewTweetCreatedListener {
        public void tweetCreated();
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
        Rect displayRectangle = new Rect();
        getDialog().getWindow().getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        View layout = inflater.inflate(R.layout.create_tweet, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.8f));
        layout.setMinimumHeight((int)(displayRectangle.height() * 0.8f));
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
                createTweet(etTweetBody.getText().toString());
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

    public void createTweet(String tweetBody) {
        TwitterClient client = TwitterApplication.getTwitterClient();
        client.createSimpleTweet(tweetBody, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweetCreatedListener.tweetCreated();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                  JSONObject errorResponse) {
                Toast.makeText(getActivity(), errorResponse.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
