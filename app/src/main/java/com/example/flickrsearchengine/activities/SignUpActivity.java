package com.example.flickrsearchengine.activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flickrsearchengine.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText txtMail;
    private EditText txtPass;
    private EditText txtPass2;
    private Button btnSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();


    }

    private void init() {
        txtMail = findViewById(R.id.txtSMail);
        txtPass = findViewById(R.id.txtSPass);
        txtPass2 = findViewById(R.id.txtSPass2);
        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();
    }

    public void signUp(View view){
        if (txtMail.getText().toString().equals("")||txtPass.getText().toString().equals("")||txtPass2.getText().toString().equals("")) {
            if (txtMail.getText().toString().equals("")){
                txtMail.setError("Boş Bırakmayın");
            }
            if (txtPass.getText().toString().equals("")){
                txtPass.setError("Boş Bırakmayın");
            }
            if (txtPass2.getText().toString().equals("")){
                txtPass2.setError("Boş Bırakmayın");
            }
           return;
        }
        if (!txtPass.getText().toString().equals(txtPass2.getText().toString())){
            Toast.makeText(getApplicationContext(),"Parolalar Uyuşmuyor !!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(txtMail.getText().toString(),txtPass.getText().toString())
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignUpActivity.this,"Başarılı",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}

