package com.milkshakeapps.milkshake.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milkshakeapps.milkshake.R;
import com.milkshakeapps.milkshake.utility.Constants;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


public class ForgotPasswordActivity extends Activity {

    private View mLoading;

    TextView mMessageField;

    EditText mEmailField;

    Button mSubmitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mLoading = findViewById(R.id.loading);
        mMessageField = (TextView)findViewById(R.id.message);
        mEmailField = (EditText) findViewById(R.id.email);
        mSubmitButton = (Button)findViewById(R.id.submitButton);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mMessageField.setVisibility(View.INVISIBLE);
                toggleFields(false);
                Log.d("debugg", "email " + mEmailField.getText().toString());
                recoverPassword(mEmailField.getText().toString());
            }
        });
    }

    private void recoverPassword(String email) {
        if (!email.matches(Constants.EMAIL_REGEX)) {
            showUserMessage("Must enter a valid email address", false);
        } else {
            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        showUserMessage("Success!  You should receive an email shortly", true);
                    } else {
                        if (e.getMessage().startsWith("no user found with email")) {
                            showUserMessage("Email address not found", false);
                        } else {
                            showUserMessage("Something went wrong, please try again later", false);
                        }
                    }
                }
            });
        }
    }

    private void showUserMessage(String message, boolean success) {
        mMessageField.setText(message);
        mMessageField.setVisibility(View.VISIBLE);
        if (success) {
            mMessageField.setBackgroundColor(Color.parseColor("##A5D6A7"));
        }
        mLoading.setVisibility(View.GONE);
        toggleFields(true);
    }

    private void toggleFields(boolean enabled) {
        mEmailField.setEnabled(enabled);
        mSubmitButton.setEnabled(enabled);
    }
}
