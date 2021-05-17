package admin.example.satyadummy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private EditText editText;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference StudentRef,RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.add_website);
        mAuth= FirebaseAuth.getInstance ();
        currentUserID = mAuth.getCurrentUser ().getUid ();


        RootRef = FirebaseDatabase.getInstance ().getReference ().child("Users").child(currentUserID).child("Website");
        progressDialog = new ProgressDialog( MainActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        recyclerView = findViewById(R.id.Recycler_v);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateMethod();


            }


        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }



    private void CreateMethod() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Set Website");
        alertDialog.setMessage("Enter website anme and url..");

        Context context = MainActivity.this;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
        final EditText input1 = new EditText(context);
        input1.setHint("Enter Website Name");
        layout.addView(input1); // Notice this is an add method

// Add another TextView here for the "Description" label
        final EditText input2 = new EditText(context);
        input2.setHint("Enter Website URL");
        layout.addView(input2); // Another add method


        alertDialog.setView(layout);
        alertDialog.setPositiveButton("Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String nameouser = input1.getText().toString();
                        String linkeouser = input2.getText().toString();

                        if (TextUtils.isEmpty(nameouser))
                        {
                            Toast.makeText(MainActivity.this, "Enter website name ..", Toast.LENGTH_SHORT).show();
                        }
                        else if (TextUtils.isEmpty(linkeouser))
                        {
                            Toast.makeText(MainActivity.this, "Enter website name ..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.show();
                            String key = RootRef.push().getKey();
                            RootRef.child(key).child("Text").setValue ( nameouser );
                            RootRef.child(key).child("Link").setValue ( linkeouser );

                            progressDialog.dismiss();
                        }

                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();




    }












    @Override
    public void onStart() {
        super.onStart ();

        progressDialog.show ();


        FirebaseRecyclerOptions<Website> options =
                new FirebaseRecyclerOptions.Builder<Website> ()
                        .setQuery ( RootRef,Website.class )
                        .build ();


        FirebaseRecyclerAdapter<Website, StudentViewHolder2> adapter =
                new FirebaseRecyclerAdapter<Website, StudentViewHolder2> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final StudentViewHolder2 holder, final int position, @NonNull final Website model) {


                        holder.name.setText ( model.getText());



                        progressDialog.dismiss ();

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink()));
                                startActivity(browserIntent1);
                            }
                        });




                    }

                    @NonNull
                    @Override
                    public StudentViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view  = LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.website_template_layout,viewGroup,false );
                        StudentViewHolder2 viewHolder  = new StudentViewHolder2(  view);
                        return viewHolder;

                    }
                };
        recyclerView.setAdapter ( adapter );
        adapter.startListening ();




    }


    public static class StudentViewHolder2 extends  RecyclerView.ViewHolder
    {

        TextView name;
        public StudentViewHolder2(@NonNull View itemView) {
            super ( itemView );
            name = itemView.findViewById ( R.id.webiste_template_text );

        }
    }

}