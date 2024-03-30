package com.mindnote;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText loginEmail,loginPassword;
    private Button loginBtn;
    private TextView loginSingupLink;
    private TextView loginForgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.et_login_email);
        loginPassword = findViewById(R.id.et_login_pass);
        loginBtn = findViewById(R.id.btn_login);
        loginSingupLink = findViewById(R.id.tv_signup_link);
        loginForgotPassword = findViewById(R.id.tv_forgot_pass_link);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String pass = loginPassword.getText().toString().trim();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    if(!pass.isEmpty()){
                        mAuth.signInWithEmailAndPassword(email,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Sucessful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        loginPassword.setError("Password is required");
                    }
                } else if (email.isEmpty()) {
                    loginEmail.setError("Email is required");
                } else {
                    loginEmail.setError("Please enter a valid email address");

                }
            }

        });

        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_pass,null);
                EditText emailbox= dialogView.findViewById(R.id.editBox);

                builder.setView(dialogView);
                AlertDialog dialog=builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Useremail=emailbox.getText().toString();
                        if(Useremail.isEmpty()&&!Patterns.EMAIL_ADDRESS.matcher(Useremail).matches()){
                            Toast.makeText(LoginActivity.this, "Enter Your Registered Email ID", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(Useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }else{
                                    Toast.makeText(LoginActivity.this, "Unable to Send, Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if(dialog.getWindow()!=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                }dialog.show();
            }
        });

        loginSingupLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,SignUp.class)));
    }
}