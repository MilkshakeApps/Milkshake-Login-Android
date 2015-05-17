package com.milkshakeapps.milkshake.control;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.milkshakeapps.milkshake.R;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;


public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, this.getString(R.string.parse_app_id),
                this.getString(R.string.parse_secret_key));
        ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(this);
    }
}