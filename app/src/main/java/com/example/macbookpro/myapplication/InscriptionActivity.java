package com.example.macbookpro.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionActivity extends AppCompatActivity {

    private EditText email, pass, confirmPass;
    private Button valider,login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email_inscr) ;
        pass = (EditText) findViewById(R.id.pass_inscr);
        confirmPass = (EditText) findViewById(R.id.confirm_pass_inscr);
        valider = (Button) findViewById(R.id.valider_inscr);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validerPressed();
            }
        });
        login = (Button) findViewById(R.id.login_button_insc);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(InscriptionActivity.this, LoginActivity.class) ;
                startActivity(loginIntent);
                finish();
            }
        });


    }

    private void validerPressed() {

        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email.getText().toString());

        if (email.getText().toString().isEmpty()) {
            email.setError("Veuillez remplir tous les champs !");

        }
        else if (pass.getText().toString().isEmpty()){
            pass.setError("Veuillez remplir tous les champs !");
        }
        else if (confirmPass.getText().toString().isEmpty()){
            confirmPass.setError("Veuillez remplir tous les champs !");
        }
        else{
            if (m.matches()) {
            if (!pass.getText().toString().equals(confirmPass.getText().toString())){
                pass.getText().clear();
                confirmPass.getText().clear();
                pass.setError("Les mots de passe ne sont pas identiques !");
                pass.requestFocus();

            }
            else{
                String emailText = email.getText().toString() ;
                String passText = pass.getText().toString();
                Log.d("Tag","En cours");
                mAuth.createUserWithEmailAndPassword(emailText,passText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InscriptionActivity.this);
                        if (task.isSuccessful()){

                            builder.setTitle("vous êtes désormais inscrit");

                            Log.d("TAG","success");
                        } else {
                            builder.setTitle("il y a un problème avec votre requête");
                            Log.d("TAG","failure");
                        }
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                    }
                });


            }
        }
        else {
                email.setError("Mail incorrect !");
                email.requestFocus();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(InscriptionActivity.this, LoginActivity.class) ;
        startActivity(loginIntent);
        finish();
    }
}
