package com.example.macbookpro.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passEdit ;
    private FirebaseAuth myAuth;
    private Button resetPass;
    private CallbackManager mCallbackManager;
    private LoginButton fbLoginButton;
    private static final String TAG = "LoginActivityTAG";
    private Button fbLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        myAuth = FirebaseAuth.getInstance();
        Button inscriptionButton = (Button) findViewById(R.id.inscription_button)  ;
        inscriptionButton.setOnClickListener(inscriptionButtonClick());
        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(loginButtonClick());
        emailEdit = (EditText) findViewById(R.id.email_edit);
        passEdit = (EditText) findViewById(R.id.pass_edit);
        Button anonymeLogin = (Button) findViewById(R.id.anonyme_login) ;
        anonymeLogin.setOnClickListener(anonymeButtonClick());
        resetPass = (Button) findViewById(R.id.btn_reset);
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passIntent = new Intent(LoginActivity.this, PassActivity.class);
                startActivity(passIntent);
                LoginActivity.this.finish();
            }
        });

        // Initialize Facebook Login button


        mCallbackManager = CallbackManager.Factory.create();
        //fbLoginButton =(LoginButton) findViewById(R.id.login_fb_button);
        //fbLoginButton.setReadPermissions("email", "public_profile");
        /*fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });
        fbLogin = (Button) findViewById(R.id.fb_login);
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbLogin.setEnabled(false);
                fbLoginButton.performClick();

            }
        });*/
    }

    private View.OnClickListener anonymeButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
            }
        };
    }

    private View.OnClickListener inscriptionButtonClick() {
        return new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent inscriptionIntent = new Intent(LoginActivity.this, InscriptionActivity.class) ;
                startActivity(inscriptionIntent);
                finish();
            }
        };
    }
    private View.OnClickListener loginButtonClick(){
        return  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = emailEdit.getText().toString() ;
                String passText = passEdit.getText().toString();
                if (emailText.isEmpty()){
                    emailEdit.setError("l'email ne peux pas être vide");
                }
                if (passText.isEmpty()){
                    passEdit.setError("le mod de passe ne peux pas être vide");
                }
                else {
                    myAuth.signInWithEmailAndPassword(emailText,passText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Log.d("TAG", "Sign in successful");
                                updateUI();
                            }
                            else{
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        };
    }
    private void updateUI() {
        Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class) ;
        startActivity(loginIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = myAuth.getCurrentUser();
                            fbLogin.setEnabled(true);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            fbLogin.setEnabled(true);
                            updateUI();
                        }

                        // ...
                    }
                });
    }
}
