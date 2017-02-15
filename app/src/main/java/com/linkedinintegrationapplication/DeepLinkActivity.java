package com.linkedinintegrationapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkedin.platform.DeepLinkHelper;
import com.linkedin.platform.errors.LIDeepLinkError;
import com.linkedin.platform.listeners.DeepLinkListener;

public class DeepLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);



        Button liMyProfileButton = (Button) findViewById(R.id.myProfile);
        liMyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
                deepLinkHelper.openCurrentProfile(DeepLinkActivity.this, new DeepLinkListener() {
                    @Override
                    public void onDeepLinkSuccess() {
//                        ((TextView) findViewById(R.id.deeplink_error)).setText("");
                    }
                    @Override
                    public void onDeepLinkError(LIDeepLinkError error) {
//                        ((TextView) findViewById(R.id.deeplink_error)).setText(error.toString());
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DeepLinkHelper deepLinkHelper = DeepLinkHelper.getInstance();
        deepLinkHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
