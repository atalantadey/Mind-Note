package com.mindnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNote_Activity extends AppCompatActivity {

    ImageView im_back,im_save;
    static TextView txtDateTime;
    EditText mtitle,msubtitle,mcontext;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_createnote);

        //initialisation
        im_back=findViewById(R.id.imageBack);
        im_save=findViewById(R.id.imageSave);
        mtitle=findViewById(R.id.inputNoteTitle);
        msubtitle=findViewById(R.id.inputNoteSubtitle);
        mcontext=findViewById(R.id.inputNote);
        txtDateTime=findViewById(R.id.textDateTime);


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

       txtDateTime.setText(new SimpleDateFormat("dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));
       //getDateTime();


        //on pressing back button
        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //on pressing Save Button
        im_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mtitle.getText().toString();
                String subtitle=msubtitle.getText().toString();
                String content =mcontext.getText().toString();
                if(title.isEmpty()||content.isEmpty()){
                    Toast.makeText(CreateNote_Activity.this, "Both Fields are Required", Toast.LENGTH_SHORT).show();

                }else {
                    //add data to firestore
                    DocumentReference dr= firebaseFirestore.
                            collection("notes").
                            document(firebaseUser.getUid()).
                            collection("mynotes").document();

                    Map<String,Object> note =new HashMap<>();
                    note.put("title",title);
                    note.put("Subtitle",subtitle);
                    note.put("content",content);

                    dr.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNote_Activity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNote_Activity.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNote_Activity.this, "Failed to Create Note", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(CreateNote_Activity.this, MainActivity.class));
                        }
                    });

                }
            }
        });

    }
    static TextView getDate(){
        return txtDateTime;
    }
}