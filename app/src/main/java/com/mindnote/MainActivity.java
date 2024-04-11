package com.mindnote;

import static android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Objects;
import java.util.Timer;

//TODO imageButton
//TODO url Button
//TODO Bullets and checkboxes
//TODO View, Edit, Delete Notes
//TODO Search Notes
public class MainActivity extends AppCompatActivity {

    //private ConstraintLayout drawerLayout;
    //private ActionBarDrawerToggle actionBarDrawerToggle;
    //private NavigationView navigationView;
    public static final int REQUEST_CODE_ADD_NOTE =1;
    public static final int REQUEST_CODE_UPDATE_NOTE=2;
    public static final int REQUEST_CODE_SHOW_NOTE=3;
    public static final int REQUEST_CODE_SELECT_IMAGE=4;
    public static final int REQUEST_CODE_STORAGE=5;
    ImageView im_addnoteMain;
    FirebaseAuth fbAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        /*drawerLayout=findViewById(R.id.main);

            actionBarDrawerToggle=new ActionBarDrawerToggle(MainActivity.this,drawerLayout,R.string.navigation_drawer_close,R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            navigationView=new NavigationView(this);
            navigationView.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
            navigationView.inflateMenu(R.menu.main_menu);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    if(menuItem.getItemId()==R.id.nav_logout){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }if(menuItem.getItemId()==R.id.nav_exit){
                        finish();
                    }
                    return true;
                }
            });
            drawerLayout.addView(navigationView);*/

         im_addnoteMain=findViewById(R.id.image_addNoteMain);
         fbAuth=FirebaseAuth.getInstance();
         firebaseFirestore=FirebaseFirestore.getInstance();
         firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
         mrecyclerview=findViewById(R.id.rv_notes);
        im_addnoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),CreateNote_Activity.class),REQUEST_CODE_ADD_NOTE);
            }
        });
        //add a note
        findViewById(R.id.iv_add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),CreateNote_Activity.class),REQUEST_CODE_ADD_NOTE);
            }
        });
        //TODO add an Image in a Note
        findViewById(R.id.iv_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),CreateNote_Activity.class),REQUEST_CODE_SELECT_IMAGE);
            }
        });
        //TODO add a URL in Image
        findViewById(R.id.iv_UrlNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //retrieving data from database
         Query query=firebaseFirestore.
                collection("notes").
                document(firebaseUser.getUid()).
                collection("mynotes").
                orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> allusernotes =new FirestoreRecyclerOptions.
                Builder<firebasemodel>().
                setQuery(query,firebasemodel.class).
                build();
        noteAdapter =new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {


                if(noteViewHolder.noteTitle!=null  &&
                        noteViewHolder.noteSubtitle!=null &&
                        noteViewHolder.noteDate!=null  )
                {  noteViewHolder.noteTitle.setText(firebasemodel.getTitle());
                    noteViewHolder.noteSubtitle.setText(firebasemodel.getSubtitle());
                    noteViewHolder.noteDate.setText(firebasemodel.getDate());

                    String color= firebasemodel.getColor();
                    if(color==null){
                        color="#1D0050";
                    }
                    noteViewHolder.mnote.setBackgroundColor(Color.parseColor(color));

                    if(firebasemodel.getImage()==null){
                        noteViewHolder.imageNote.setVisibility(View.GONE);
                    }else {
                        Glide.with(getApplicationContext()).load(Uri.parse(firebasemodel.getImage())).into(noteViewHolder.imageNote);
                        noteViewHolder.imageNote.setVisibility(View.VISIBLE);
                    }
                }
                String noteID=noteAdapter.getSnapshots().getSnapshot(i).getId();
                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "This item is CLicked", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MainActivity.this,editNoteActivity.class);
                        intent.putExtra("Title",firebasemodel.getTitle());
                        intent.putExtra("Content",firebasemodel.getContent());
                        intent.putExtra("Subtitle",firebasemodel.getSubtitle());
                        intent.putExtra("Date",firebasemodel.getDate());
                        intent.putExtra("Color",firebasemodel.getColor());
                        intent.putExtra("Image",firebasemodel.getImage());
                        intent.putExtra("Url",firebasemodel.getUrl());
                        intent.putExtra("NoteID",noteID);
                        v.getContext().startActivity(intent);

                    }
                });
            }
            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager =new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);

        Intent i =new Intent(MainActivity.this,CreateNote_Activity.class);
        //i.putExtra("")
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView noteTitle;
        private final TextView noteSubtitle;
        private final TextView noteDate;
        LinearLayout mnote;
        RoundedImageView imageNote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle=itemView.findViewById(R.id.TextTitle);
            noteSubtitle=itemView.findViewById(R.id.TextSubtitle);
            noteDate=itemView.findViewById(R.id.textDate);
            mnote=itemView.findViewById(R.id.Layout_note);
            imageNote= itemView.findViewById(R.id.RoundedimageNote);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            //listener.onItemClick(getAdapterPosition());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter!=null)
            noteAdapter.startListening();
    }

}
