package com.mindnote;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNote_Activity extends AppCompatActivity {
    ImageView im_back,im_save,imageNote;
    View viewSubtitleIndicator;
    private static String selectedNoteColor;
    static TextView txtDateTime;
    EditText mtitle,msubtitle,mcontext;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    public static final int REQUEST_CODE_STORAGE_PERMISSION=1;
    public static final int REQUEST_CODE_SELECT_IMAGE=2;
    private TextView tv_url;
    private LinearLayout layoutWeburl;
    private AlertDialog dialogAddUrl;
    Uri selectedImageUri;
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
        viewSubtitleIndicator =findViewById(R.id.ViewSubtitleIndicator);
        selectedNoteColor = "#1D0050";
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        imageNote=findViewById(R.id.imageNote);
        layoutWeburl=findViewById(R.id.ll_webUrlCreateNote);
        tv_url =findViewById(R.id.tv_UrlCreateNote);

        String selectedImagePath = "";
       txtDateTime.setText(new SimpleDateFormat("dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date()));
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
                String date =txtDateTime.getText().toString();
                String Image = selectedImageUri.toString();
                String Color= selectedNoteColor;
                String Url="";
                if(layoutWeburl.getVisibility()==View.VISIBLE){
                     Url =tv_url.getText().toString();
                }

                if(title.isEmpty()||content.isEmpty()){
                    Toast.makeText(CreateNote_Activity.this, "Both Fields are Required", Toast.LENGTH_SHORT).show();

                }else {
                    //add data to firestore
                    DocumentReference dr= firebaseFirestore.
                            collection("notes").
                            document(firebaseUser.getUid()).
                            collection("mynotes").document();
                    //dr.update("noteImage",imageNote,"noteDate",txtDateTime);

                    Map<String,Object> note =new HashMap<>();
                    note.put("title",title);
                    note.put("Subtitle",subtitle);
                    note.put("content",content);
                    note.put("Date",date);
                    note.put("Image",Image);
                    note.put("Color",Color);
                    note.put("URL",Url);
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
        initOptions();
        setViewSubtitleIndicator();
        setSelectedNoteColour();
    }
    private void initOptions(){
        final LinearLayout layoutOptions = findViewById(R.id.layoutOptions);
        final BottomSheetBehavior bottomSheetBehavior =BottomSheetBehavior.from(layoutOptions);
        layoutOptions.findViewById(R.id.textOptions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        final ImageView imageColor1 = layoutOptions.findViewById(R.id.image_color1);
        final ImageView imageColor2 = layoutOptions.findViewById(R.id.image_color2);
        final ImageView imageColor3 = layoutOptions.findViewById(R.id.image_color3);
        final ImageView imageColor4 = layoutOptions.findViewById(R.id.image_color4);

        layoutOptions.findViewById(R.id.viewcolor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#1D0050";
                setViewSubtitleIndicator();
            }
        });
        layoutOptions.findViewById(R.id.viewcolor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                setViewSubtitleIndicator();
            }
        });
        layoutOptions.findViewById(R.id.viewcolor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#b0f2c2";
                setViewSubtitleIndicator();
            }
        });
        layoutOptions.findViewById(R.id.viewcolor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#6c8dfa";
                setViewSubtitleIndicator();
            }
        });
        layoutOptions.findViewById(R.id.viewcolor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#ff8fab";
                setViewSubtitleIndicator();
            }
        });
        layoutOptions.findViewById(R.id.layout_addImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            CreateNote_Activity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE_PERMISSION
                    );
                }else{
                    selectImage();
                }
            }
        });
        layoutOptions.findViewById(R.id.layout_addUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddDialog();
            }
        });
        layoutOptions.findViewById(R.id.layout_delNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Delete
            }
        });

    }
    private void setViewSubtitleIndicator(){
        GradientDrawable gradientDrawable =(GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
     static int setSelectedNoteColour(){
        int mcolor;
        if(selectedNoteColor!=null){
             mcolor=Color.parseColor(selectedNoteColor);
        }else{
            String Colorhex = "#FF1D0050";
             mcolor=Color.parseColor(Colorhex);
        }
        return mcolor;
     }
     private void selectImage(){
        Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
     }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_STORAGE_PERMISSION && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_SELECT_IMAGE && resultCode==RESULT_OK){
            if(data!=null){
                 selectedImageUri=data.getData();
                if(selectedImageUri!=null){
                    try {
                        InputStream inputStream= getContentResolver()
                                .openInputStream(selectedImageUri);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        //selectedImagePath =getpathFromUri(selectedImageUri);
                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                
            }
        }
    }
    private void showAddDialog(){
        if(dialogAddUrl==null){
            AlertDialog.Builder builder= new AlertDialog.Builder(CreateNote_Activity.this);
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
                        Toast.makeText(CreateNote_Activity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    }else if (!Patterns.WEB_URL.matcher(inputUrl.getText().toString().trim()).matches()){
                        Toast.makeText(CreateNote_Activity.this, "Enter a Valid URL", Toast.LENGTH_SHORT).show();
                    }else{
                        tv_url.setText(inputUrl.getText().toString());
                        layoutWeburl.setVisibility(View.VISIBLE);
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