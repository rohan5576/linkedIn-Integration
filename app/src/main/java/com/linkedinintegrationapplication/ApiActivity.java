package com.linkedinintegrationapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkedin.platform.APIHelper;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ApiActivity extends AppCompatActivity {

    private static final String host = "api.linkedin.com";
//    private static final String token = "https://rohan138.auth0.com/login/callback";
    private static final String token= "https://lodhirohan138.auth0.com/authorize/?client_id=YpB5ERswicXFcJaAynnlYaXQ89OnFpPt&response_type=code&redirect_uri=https://rohan138.auth0.com/login/callback";
    //    private static final String topCardUrl = "https://" + host + "/v1/people/:(id,first-name,last-name,headline,location,industry,current-share,num-connections," +
//            "summary,specialties,positions,picture-urls::(original))";
//    private static final String shareUrl = "https://" + host + "/v1/people/~/shares";
private static final String topCardUrl="https://" + host + "/v1/people/~:("
        + "id,"
        + "first-name,last-name,headline,picture-url,industry,summary,specialties,"
        + "positions:("
        + "id,"
        + "title,"
        + "summary,"
        + "start-date,"
        + "end-date,"
        + "is-current,"
        + "company:("
        + "id,"
        + "name,"
        + "type,"
        + "size,"
        + "industry,ticker)"
        +"),"
        + "educations:("
        + "id,"
        + "school-name,"
        + "field-of-study,"
        + "start-date,"
        + "end-date,"
        + "degree,"
        + "activities,"
        + "notes),"+")";
    TextView location, FirstName, SecondName, Formatted,id,headline,industry,currentShare,numconnections;
    ImageView profile_picture;

//        + "associations," /* Full Profile */
//        + "interests,"
//        + "num-recommenders,"
//        + "date-of-birth,"
//        + "publications:("
//        + "id,"
//        + "title,"
//        + "publisher:(name),"
//        + "authors:(id,name),"
//        + "date,"
//        + "url,"
//        + "summary),"
//        + "patents:("
//        + "id,"
//        + "title,"
//        + "summary,"
//        + "number,"
//        + "status:(id,name),"
//        + "office:(name),"
//        + "inventors:(id,name),"
//        + "date,"
//        + "url),"
//        + "languages:("
//        + "id,"
//        + "language:(name),"
//        + "proficiency:(level,name)),"
//        + "skills:("
//        + "id,"
//        + "skill:(name)),"
//        + "certifications:("
//        + "id,"
//        + "name,"
//        + "authority:(name),"
//        + "number,"
//        + "start-date,"
//        + "end-date),"
//        + "courses:("
//        + "id,"
//        + "name,"
//        + "number),"
//        + "recommendations-received:("
//        + "id,"
//        + "recommendation-type,"
//        + "recommendation-text,"
//        + "recommender),"
//        + "honors-awards,"
//        + "three-current-positions,"
//        + "three-past-positions,"
//        + "volunteer"
//        + ")"
//        + "?oauth2_access_token="+ token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        location = (TextView) findViewById(R.id.location);
        id = (TextView) findViewById(R.id.id);
        FirstName = (TextView) findViewById(R.id.FirstName);
        SecondName = (TextView) findViewById(R.id.SecondName);
        headline = (TextView) findViewById(R.id.headline);
        industry = (TextView) findViewById(R.id.industry);
        Formatted = (TextView) findViewById(R.id.Formatted);


        currentShare= (TextView) findViewById(R.id.currentShare);
        numconnections= (TextView) findViewById(R.id.numconnections);

        profile_picture = (ImageView) findViewById(R.id.profile_picture);

        final Button makeApiCall = (Button) findViewById(R.id.makeApiCall);
        makeApiCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
                apiHelper.getRequest(ApiActivity.this, topCardUrl, new ApiListener() {
                    @Override
                    public void onApiSuccess(ApiResponse s) {
                       ((TextView) findViewById(R.id.response)).setText(s.toString());
                        showResult(s.getResponseDataAsJson());
                    }

                    @Override
                    public void onApiError(LIApiError error) {
//                        ((TextView) findViewById(R.id.response)).setText(error.toString());
                    }
                });
            }
        });
    }

    public void showResult(JSONObject response) {

        try {



            id.setText(response.get("id").toString());
            FirstName.setText(response.get("firstName").toString());
            SecondName.setText(response.get("lastName").toString());
            headline.setText(response.get("headline").toString());
            industry.setText(response.get("industry").toString());
            Picasso.with(this).load(response.getString("pictureUrl"))
                    .into(profile_picture);
            Formatted.setText(response.get("formattedName").toString());

            location.setText(response.get("location").toString());


            numconnections.setText(response.get("numConnections").toString());





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//        final Button makePostApiCall = (Button) findViewById(R.id.makePostApiCall);
//        makePostApiCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText shareComment = (EditText) findViewById(R.id.shareComment);
//                String shareJsonText = "{ \n" +
//                        "   \"comment\":\"" + shareComment.getText() + "\"," +
//                        "   \"visibility\":{ " +
//                        "      \"code\":\"anyone\"" +
//                        "   }," +
//                        "   \"content\":{ " +
//                        "      \"title\":\"Test Share Title\"," +
//                        "      \"description\":\"Leverage the Share API to maximize engagement on user-generated content on LinkedIn\"," +
//                        "      \"submitted-url\":\"https://www.americanexpress.com/us/small-business/openforum/programhub/managing-your-money/?pillar=critical-numbers\"," +
//                        "      \"submitted-image-url\":\"http://m3.licdn.com/media/p/3/000/124/1a6/089a29a.png\"" +
//                        "   }" +
//                        "}";
//                APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
//                apiHelper.postRequest(ApiActivity.this, shareUrl, shareJsonText, new ApiListener() {
//                    @Override
//                    public void onApiSuccess(ApiResponse apiResponse) {
//                        ((TextView) findViewById(R.id.response)).setText(apiResponse.toString());
//                    }
//
//                    @Override
//                    public void onApiError(LIApiError error) {
//                        ((TextView) findViewById(R.id.response)).setText(error.toString());
//                    }
//                });
//            }
//        });


}




