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

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private String currentUserID;
    private DatabaseReference RootRef;
    private ProgressDialog progressDialog;
    private EditText email,password;
    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog( LoginActivity.this);
        progressDialog.setContentView ( R.layout.loading );
        progressDialog.setTitle ( "Please Wait..." );
        progressDialog.setCanceledOnTouchOutside ( false );
        progressDialog.setMessage ( "Tips: Please Cheak your Internet or Wi-fi Connection" );
        mAuth = FirebaseAuth.getInstance ();
        RootRef = FirebaseDatabase.getInstance ().getReference ();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        button = findViewById(R.id.login_buuton);
        textView = findViewById(R.id.login_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stringemail = email.getText().toString();
                String stringpassword  = password.getText().toString();

                if (TextUtils.isEmpty(stringemail))
                {
                    Toast.makeText(LoginActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(stringpassword))
                {
                    Toast.makeText(LoginActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
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
                Intent inlo = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(inlo);
            }
        });
    }

    private void SignInWithEmail(String stringemail, String stringpassword) {

        progressDialog.show();

        mAuth.signInWithEmailAndPassword ( stringemail, stringpassword )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()) {

                            SendUserToMainActivity();
                            Toast.makeText ( LoginActivity.this, "Successfully Logged IN", Toast.LENGTH_SHORT ).show ();
                            progressDialog.dismiss();


                        } else {

                            String message = task.getException ().toString ();
                            Toast.makeText ( LoginActivity.this, "Error" + message, Toast.LENGTH_SHORT ).show ();
                            progressDialog.dismiss();

                        }
                    }
                } );

    }
    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent ( LoginActivity.this,MainActivity.class );
        loginIntent.addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity ( loginIntent );
        finish ();
    }
}