package admin.example.satyadummy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {



    private EditText email,password;
    private Button button;
    private ProgressDialog progressDialog;
    private TextView textView;
    private DatabaseReference RootRef;
    private DatabaseReference RootRefmain;
    private FirebaseAuth mauth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mauth = FirebaseAuth.getInstance ();
        RootRefmain = FirebaseDatabase.getInstance ().getReference ();
        RootRef= FirebaseDatabase.getInstance ().getReference ();

        progressDialog = new ProgressDialog( RegisterActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        button = findViewById(R.id.signup_button);
        textView = findViewById(R.id.signup_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringemail = email.getText().toString();
                String stringpassword  = password.getText().toString();

                if (TextUtils.isEmpty(stringemail))
                {
                    Toast.makeText(RegisterActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(stringpassword))
                {
                    Toast.makeText(RegisterActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SignInWithEmail(stringemail,stringpassword);
                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inlo = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(inlo);
            }
        });
    }

    private void SignInWithEmail(String stringemail, String stringpassword) {

        progressDialog.show();

        mauth.createUserWithEmailAndPassword ( stringemail,stringpassword )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ())
                        {
                            String cuurrentUserID = mauth.getCurrentUser ().getUid ();




                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Email" ).setValue ( stringemail );

                            String key = RootRef.child("Users").child(cuurrentUserID).child("Website").push().getKey();
                            String key2 = RootRef.child("Users").child(cuurrentUserID).child("Website").push().getKey();

                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Website" ).child(key).child("Text").setValue ( "Facebook" );
                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Website" ).child(key).child("Link").setValue ( "https://www.facebook.com" );

                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Website" ).child(key2).child("Text").setValue ( "Instagram" );
                            RootRef.child ( "Users" ).child ( cuurrentUserID ).child ( "Website" ).child(key2).child("Link").setValue ( "https://www.instagram.com" );


                            Toast.makeText (RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT ).show ();

                                        Intent inlot = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(inlot);
                                        progressDialog.dismiss();



                        }
                        else
                        {
                            progressDialog.dismiss();
                            String message = task.getException ().toString ();

                            Toast.makeText ( RegisterActivity.this, "Error" + message, Toast.LENGTH_SHORT ).show ();

                        }
                    }
                } );






    }
}