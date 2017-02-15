package com.linkedinintegrationapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import static com.linkedinintegrationapplication.R.id.response;

public class Authentication extends AppCompatActivity {

    private static final String API_KEY = "81ux869g87xtz6\n";
    private static final String SECRET_KEY = "EBePOyGMcGV5ltU9";
    private static final String STATE = "E3ZYKC1T6H2yP4z";
    private static final String REDIRECT_URI = "http://com.amalbit.redirecturl";
    private static final String SCOPES = "r_fullprofile%20r_emailaddress%20r_network";
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE = "code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";
    WebView webView;
    private ProgressDialog pd;

    private static String getAccessTokenUrl(String authorizationToken) {
        String URL = ACCESS_TOKEN_URL
                + QUESTION_MARK
                + GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE
                + AMPERSAND
                + RESPONSE_TYPE_VALUE + EQUALS + authorizationToken
                + AMPERSAND
                + CLIENT_ID_PARAM + EQUALS + API_KEY
                + AMPERSAND
                + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI
                + AMPERSAND
                + SECRET_KEY_PARAM + EQUALS + SECRET_KEY;
        Log.i("accessToken URL", "" + URL);
        return URL;
    }

    private static String getAuthorizationUrl(){
        String URL = AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND  +CLIENT_ID_PARAM    +EQUALS +API_KEY
                +AMPERSAND  +SCOPE_PARAM        +EQUALS +SCOPES
                +AMPERSAND  +STATE_PARAM        +EQUALS +STATE
                +AMPERSAND  +REDIRECT_URI_PARAM +EQUALS +REDIRECT_URI;
        Log.i("authorization URL",""+URL);
        return URL;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        webView = (WebView) findViewById(R.id.webView);
        webView.requestFocus(View.FOCUS_DOWN);

        pd = new ProgressDialog(this);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {
                if (authorizationUrl
                        .startsWith(REDIRECT_URI)) {
                    Log.i("Authorize", "");
                    Uri uri = Uri.parse(authorizationUrl);
                    //We take from the url the authorizationToken and the state token. We have to check that the state token returned by the Service is the same we sent.
                    //If not, that means the request may be a result of CSRF and must be rejected.
                    String stateToken = uri.getQueryParameter(STATE_PARAM);
                    if (stateToken == null || !stateToken.equals(STATE)) {
                        Log.e("Authorize", "State token doesn't match");
                        return true;
                    }

                    //If the user doesn't allow authorization to our application, the authorizationToken Will be null.
                    String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                    if (authorizationToken == null) {
                        Log.i("Authorize", "The user doesn't allow authorization.");
                        return true;
                    }
                    Log.i("Authorize", "Auth token received: " + authorizationToken);

                    //Generate URL for requesting Access Token
                    String accessTokenUrl = getAccessTokenUrl(authorizationToken);
                    //We make the request in a AsyncTask
                    new PostRequestAsyncTask().execute(accessTokenUrl);

                } else {
                    //Default behaviour
                    Log.i("Authorize", "Redirecting to: " + authorizationUrl);
                    webView.loadUrl(authorizationUrl);
                }
                return true;

            }
        });

        String authUrl = getAuthorizationUrl();
        Log.i("Authorize", "Loading Auth Url: " + authUrl);
        //Load the authorization URL into the webView
        webView.loadUrl(authUrl);

    }

    @Override
    protected void onPostExecute(Boolean status) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (status) {
            //If everything went Ok, change to another activity.
            Intent startProfileActivity = new Intent(Authentication.this, MainLinkedInActivity.class);
            Authentication.this.startActivity(startProfileActivity);
        }
    }

private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean> {

    @Override
    protected void onPreExecute() {
        pd = ProgressDialog.show(Authentication.this, "", "Loading..", true);
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        if (urls.length > 0) {


//
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpPost httpost = new HttpPost(url);

try {

//                HttpResponse response = httpClient.execute(httpost);
//                if (response != null) {
                    //If status is OK 200

                     URL url=new URL(urls[0]);
                    HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                     connection.connect();

//                    if (connection.getStatusLine().getStatusCode() == 200) {
//                        String result = EntityUtils.toString(response.getEntity());
                        //Convert the string result to a JSON Object
                        JSONObject resultJson = new JSONObject(result);
                        //Extract data from JSON Response
                        int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                        String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                        Log.e("Tokenm", "" + accessToken);
                        if (expiresIn > 0 && accessToken != null) {
                            Log.i("Authorize", "This is the access Token: " + accessToken + ". It will expires in " + expiresIn + " secs");

                            //Calculate date of expiration
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.SECOND, expiresIn);
                            long expireDate = calendar.getTimeInMillis();

                            ////Store both expires in and access token in shared preferences
                            SharedPreferences preferences = Authentication.this.getSharedPreferences("user_info", 0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putLong("expires", expireDate);
                            editor.putString("accessToken", accessToken);
                            editor.commit();

                            return true;}

                }
            } catch (IOException e) {
                Log.e("Authorize", "Error Http response " + e.getLocalizedMessage());
            } catch (ParseException e) {
                Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
            } catch (JSONException e) {
                Log.e("Authorize", "Error Parsing Http response " + e.getLocalizedMessage());
            }
        }
        return false;
    }

};
}

