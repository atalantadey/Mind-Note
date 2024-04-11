package com.mindnote;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class editNoteActivity extends AppCompatActivity {

    ImageView bullets,checklist,colour,select_image,select_url,delNote;
    ImageView save,back,Nimage;
    EditText Ntitle,Ncontent,Nsubtitle;
    TextView Nurl,Ndate;
    CardView NoteCard;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    StorageReference storageReference= firebaseStorage.getReference().child("images/");
    String imageUri;
    private AlertDialog dialogAddUrl;
    LinearLayout ll_url;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        bullets=findViewById(R.id.btnBullets);
        checklist=findViewById(R.id.btnCheckboxes);
        colour=findViewById(R.id.btnColor);
        select_image=findViewById(R.id.btnAddImage);
        select_url=findViewById(R.id.btnAddUrl);
        delNote=findViewById(R.id.btnDel);
        save=findViewById(R.id.imageSave);
        back=findViewById(R.id.imageBack);
        Nimage=findViewById(R.id.imageViewNoteImage);
        Ntitle=findViewById(R.id.editTextNoteTitle);
        Ncontent=findViewById(R.id.editTextNoteContent);
        Nsubtitle=findViewById(R.id.editTextNoteSubtitle);
        Nurl=findViewById(R.id.editTextNoteUrl);
        ll_url=findViewById(R.id.ll_webUrlEditNote);
        Ndate=findViewById(R.id.editTextNoteDate);
        NoteCard=findViewById(R.id.card_view);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        ll_url.setVisibility(View.GONE);
        Intent data=getIntent();
        Ntitle.setText(data.getStringExtra("Title"));
        Ncontent.setText(data.getStringExtra("Content"));
        Nsubtitle.setText(data.getStringExtra("Subtitle"));
        Ndate.setText(data.getStringExtra("Date"));
        String Noteid=data.getStringExtra("NoteID");

        if(Nurl!=null){
            ll_url.setVisibility(View.VISIBLE);
            Nurl.setText(data.getStringExtra("Url"));
        }
        NoteCard.setCardBackgroundColor(Color.parseColor(data.getStringExtra("Color")));

        imageUri=data.getStringExtra("Image");
        if(imageUri!=null){
            setImage();
        }

        bullets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO BULLETS
            }
        });
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO CHECKLIST
            }
        });
        colour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO COLOUR
            }
        });
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO IMAGE
            }
        });
        select_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO URL
                showAddDialog();
            }
        });
        delNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference documentReference =firebaseFirestore.
                        collection("notes").
                        document(firebaseUser.getUid()).
                        collection("mynotes").document(Noteid);
                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(editNoteActivity.this, "Note Deleted Succesfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(editNoteActivity.this,MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(editNoteActivity.this, "Failed To Delete Note", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String Title=Ntitle.getText().toString();
                String Content=Ncontent.getText().toString();
                String Subtitle=Nsubtitle.getText().toString();
                String Date=Ndate.getText().toString();
                String url=Nurl.getText().toString();
                if(Title.isEmpty()|| Content.isEmpty()){
                    Toast.makeText(editNoteActivity.this,
                            "Please Enter the Fields Correctly",
                            Toast.LENGTH_SHORT).show();
                }else{
                    assert Noteid != null;
                    DocumentReference documentReference = firebaseFirestore.
                            collection("notes").
                            document(firebaseUser.getUid()).
                            collection("mynotes").document(Noteid);
                    Map<String,Object> note=new HashMap<>();
                    note.put("Title",Title);
                    note.put("Content",Content);
                    note.put("Subtitle",Subtitle);
                    note.put("Date",Date);
                    note.put("URL",url);
                    //note.put("Image",image);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editNoteActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editNoteActivity.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editNoteActivity.this, "Failed to Update, Please Try Again", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editNoteActivity.this,MainActivity.class));
                        }
                    });
                }
            }
        });

    }
    private void setImage() {
        if(imageUri!=null){
            try {
                InputStream inputStream= getContentResolver()
                        .openInputStream(Uri.parse(imageUri));
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                Nimage.setImageBitmap(bitmap);
                Nimage.setVisibility(View.VISIBLE);
                String selectedImagePath =getpathFromUri(Uri.parse(imageUri));
                Toast.makeText(this, "Image Displayed", Toast.LENGTH_SHORT).show();
            }catch (Exception exception){
                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getpathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor =getContentResolver().query(contentUri,null,null,null,null);
        if(cursor==null){
            filePath=contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index=cursor.getColumnIndex("_data");
            filePath=cursor.getString(index);
            cursor.close();
        }return  filePath;
    }
    private void showAddDialog(){
        if(dialogAddUrl==null){
            AlertDialog.Builder builder= new AlertDialog.Builder(editNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_addurl,(ViewGroup) findViewById(R.id.Layout_addURLContainer));
            builder.setView(view);
            dialogAddUrl =builder.create();
            if(dialogAddUrl.getWindow()!=null){
                dialogAddUrl.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputUrl =view.findViewById(R.id.inputURL);
            inputUrl.requestFocus();
            view.findViewById(R.id.tv_addUrl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inputUrl.getText().toString().trim().isEmpty()){
                        Toast.makeText(editNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    }else if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches()){
                        Toast.makeText(editNoteActivity.this, "Enter a Valid URL", Toast.LENGTH_SHORT).show();
                    }else{
                        Nurl.setText(inputUrl.getText().toString());
                        Nurl.setVisibility(View.VISIBLE);
                        dialogAddUrl.dismiss();
                    }
                }
            });
            view.findViewById(R.id.tv_CancelUrl).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddUrl.dismiss();
                }
            });
        }dialogAddUrl.show();
    }
}