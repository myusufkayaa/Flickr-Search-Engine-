package com.example.flickrsearchengine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
private  LoginButton fLoginButton;
private Button loginButton;
private CircleImageView circleImageView;
private TextView txtName;
private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fLoginButton= findViewById(R.id.fLoginButton);
        loginButton=findViewById(R.id.loginButton);
        circleImageView=findViewById(R.id.circleImageView);
        txtName=findViewById(R.id.txtName);
        callbackManager=CallbackManager.Factory.create();
        fLoginButton.setPermissions(Arrays.asList("email","public_profile"));
        fLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    AccessTokenTracker accessTokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken!=null){
                loadProfile(currentAccessToken);
            }else{
                txtName.setText("");
                circleImageView.setImageResource(0);
            }

        }
    };
    private void loadProfile(AccessToken accessToken){
        GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String firstName=object.getString("first_name");
                    String lastName=object.getString("last_name");
                    String id=object.getString("id");
                    String imgUrl="https://graph.facebook.com/"+id+"/picture?type=normal";
                    txtName.setText(firstName+" "+lastName.toUpperCase());

                    Picasso.get().load(imgUrl).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }


    public void giris(View view){
        Intent intent=new Intent(getApplicationContext(), Search.class);
        startActivity(intent);

    }
}
