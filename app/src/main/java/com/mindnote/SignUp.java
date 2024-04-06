package com.mindnote;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText msignup_email,msinup_password;
    private Button msignup_button;
    private TextView msignup_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        msignup_email = findViewById(R.id.et_signup_email);
        msinup_password = findViewById(R.id.et_signup_pass);
        msignup_button = findViewById(R.id.btn_signup);
        msignup_login=findViewById(R.id.tv_login_link);
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(SignUp.this, MainActivity.class));
        }
        msignup_button.setOnClickListener(view -> {
            String email = msignup_email.getText().toString().trim();
            String password = msinup_password.getText().toString().trim();
            if (email.isEmpty()) {
                msignup_email.setError("Email is required");
            }
            if(password.isEmpty()) {
                msinup_password.setError("Password is required");
            }else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Toast.makeText(SignUp.this,
                                "Sign Up Successful",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(SignUp.this,
                                "Sign Up Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        msignup_login.setOnClickListener(v -> startActivity(new Intent(SignUp.this, LoginActivity.class)));

    }
}