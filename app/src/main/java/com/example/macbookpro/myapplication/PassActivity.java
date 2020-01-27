package com.example.macbookpro.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PassActivity extends AppCompatActivity {


    private Button resetPassButton;
    private EditText edtEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);
        resetPassButton = (Button) findViewById(R.id.btn_reset_password);
        edtEmail = (EditText) findViewById(R.id.edit_reset_email);
        mAuth = FirebaseAuth.getInstance();
        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    edtEmail.setError("Entrer votre email!");
                    return;
                }
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(PassActivity.this,
                                    "Vérifiez votre email pour réinitialiser le mot de passe!",Toast.LENGTH_SHORT).show();
                            Log.d("Tag","sucess");
                        }
                        else{
                            Toast.makeText(PassActivity.this, "Erreur",Toast.LENGTH_SHORT).show();
                            Log.d("Tag","failed");

                        }
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PassActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
