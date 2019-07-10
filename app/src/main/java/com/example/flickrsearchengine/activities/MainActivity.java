package com.example.flickrsearchengine.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flickrsearchengine.R;
import com.example.flickrsearchengine.database.mDatabase;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private LoginButton fLoginButton;
    private TextView txtSignUp;
    private EditText txtMail;
    private EditText txtPass;
    private Button loginButton;
    private CallbackManager callbackManager;
    private mDatabase database;
    private FirebaseAuth mAuth;
    boolean isLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSignUp = findViewById(R.id.textView);
        txtMail = findViewById(R.id.txtMail);
        txtPass = findViewById(R.id.txtPass);
        database = (mDatabase) mDatabase.getInstance(getApplicationContext());
        fLoginButton = findViewById(R.id.fLoginButton);
        loginButton = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();
        fLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
        fLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });
        setLogin();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                            startActivity(intent);
                        } else {

                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken != null) {
                setLogin();
            } else {
               mAuth.signOut();
               setLogin();
            }

        }

    };



    public void giris(View view) {
        if (isLogin){
            Toast.makeText(MainActivity.this,"Facebook Giriş",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
            startActivity(intent);
            return;
        }

        mAuth.signInWithEmailAndPassword(txtMail.getText().toString(),txtPass.getText().toString()).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this,"Başarılı Giriş",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void clickSignUp(View view) {
        Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);
    }
    public void setLogin(){
        if (mAuth.getCurrentUser()==null){
            isLogin=false;
            txtMail.setVisibility(View.VISIBLE);
            txtPass.setVisibility(View.VISIBLE);
        }else{
            isLogin=true;
            txtMail.setVisibility(View.GONE);
            txtPass.setVisibility(View.GONE);
        }

    }

}

