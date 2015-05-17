package com.milkshakeapps.milkshake.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milkshakeapps.milkshake.R;
import com.milkshakeapps.milkshake.utility.Constants;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.Collection;


public class LoginActivity extends Activity {
    private boolean mShowingPassword = true, mLoginMode = true;

    private View mLoading, mForgotPassword;

    private EditText mEmailField, mPasswordField;

    private TextView mMessageField;

    private Button mFaceBookButton, mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getIntent() != null && !getIntent().getBooleanExtra("isLoginMode", true)) {
            mLoginMode = false;
        }
        mLoading = findViewById(R.id.loading);
        mMessageField = (TextView)findViewById(R.id.message);
        mForgotPassword = findViewById(R.id.forgotPassword);
        mEmailField = (EditText)findViewById(R.id.email);
        mPasswordField = (EditText)findViewById(R.id.password);
        final TextView passwordToggle = (TextView)findViewById(R.id.passwordToggle);
        mFaceBookButton = (Button)findViewById(R.id.facebookLogin);
        mFaceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mMessageField.setVisibility(View.INVISIBLE);
                toggleFields(false);
                facebookLogin();
            }
        });
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoading.setVisibility(View.VISIBLE);
                mMessageField.setVisibility(View.INVISIBLE);
                toggleFields(false);
                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                if (mLoginMode) {
                    login(email, password);
                } else {
                    signup(email, password);
                }
            }
        });
        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mPasswordField.getText().toString();
                mPasswordField.setText("");
                if (mShowingPassword) {
                    passwordToggle.setText("SHOW");
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mPasswordField.setSelection(mPasswordField.getText().length());
                } else {
                    passwordToggle.setText("HIDE");
                    mPasswordField.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                mPasswordField.setText(text);
                mPasswordField.setSelection(mPasswordField.getText().length());
                mShowingPassword = !mShowingPassword;
            }
        });
        if (mLoginMode) {
            mLoginButton.setText("Log in");
            mFaceBookButton.setText("Log in with Facebook");
            mForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                }
            });
        } else {
            mLoginButton.setText("Sign up");
            mFaceBookButton.setText("Sign up with Facebook");
            mForgotPassword.setVisibility(View.INVISIBLE);
        }
    }

    private void login(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    gotoMainPage();
                } else {
                    if ("invalid login parameters".equals(e.getMessage())) {
                        showUserMessage("Incorrect email or password");
                    } else {
                        showUserMessage("An error occurred please try again later");
                    }
                }
            }
        });
    }

    private void signup(String email, String password) {
        if (!email.matches(Constants.EMAIL_REGEX)) {
            showUserMessage("Must enter a valid email address");
        } else if (password.length() < 6) {
            showUserMessage("Password must be at least 6 characters");
        } else {
            ParseUser user = new ParseUser();
            user.setUsername(email);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        gotoMainPage();
                    } else {
                        if (e.getMessage().startsWith("username ")) {
                            showUserMessage("Email address already taken.  Please log in!");
                        } else {
                            showUserMessage("An error occurred please try again later");
                        }
                    }
                }
            });
        }
    }

    private void facebookLogin() {
        Collection<String> permissions = Arrays.asList("email", "basic_info", "user_about_me",
                "user_location");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this,
                permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            showUserMessage("Facebook login failed, please try again later");
                        } else if (user.isNew()) {
                            gotoMainPage();
                        } else {
                            gotoMainPage();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void showUserMessage(String message) {
        mMessageField.setText(message);
        mMessageField.setVisibility(View.VISIBLE);
        mLoading.setVisibility(View.GONE);
        toggleFields(true);
    }

    private void toggleFields(boolean enabled) {
        mPasswordField.setEnabled(enabled);
        mEmailField.setEnabled(enabled);
        mLoginButton.setEnabled(enabled);
        mForgotPassword.setEnabled(enabled);
        mFaceBookButton.setEnabled(enabled);
    }

    private void gotoMainPage() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
