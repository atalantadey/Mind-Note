package com.mindnote;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ADD_NOTE =1;
    static TextView mdate;
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
         im_addnoteMain=findViewById(R.id.image_addNoteMain);
         fbAuth=FirebaseAuth.getInstance();
         firebaseFirestore=FirebaseFirestore.getInstance();
         firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
         mrecyclerview=findViewById(R.id.rv_notes);
         //mdate=CreateNote_Activity.getDate();
        //Log.d("Date",mdate.toString());

        im_addnoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(),CreateNote_Activity.class),REQUEST_CODE_ADD_NOTE);
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
                if(noteViewHolder.noteTitle!=null && noteViewHolder.noteSubtitle!=null )
                {  noteViewHolder.noteTitle.setText(firebasemodel.getTitle());
                    noteViewHolder.noteSubtitle.setText(firebasemodel.getSubtitle());
                    //noteViewHolder.noteDate.setText(mdate.toString());
                    }
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

    }
    public class NoteViewHolder extends RecyclerView.ViewHolder{
        private final TextView noteTitle;
        private final TextView noteSubtitle;
        private final TextView noteDate;
        LinearLayout mnote;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle=itemView.findViewById(R.id.TextTitle);
            noteSubtitle=itemView.findViewById(R.id.TextSubtitle);
            noteDate=itemView.findViewById(R.id.textDateTime);
            mnote=itemView.findViewById(R.id.Layout_note);
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
