package android.example.appchoco;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    EditText inputUserName, inputEmail, inputPassword;
    Button btnSignup;
    TextView Gobacktologin;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        inputUserName = findViewById(R.id.inputUserName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        btnSignup = findViewById(R.id.btnSignup);
        Gobacktologin = findViewById(R.id.Gobacktologin);

        Gobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGobacktologin = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentGobacktologin);
            }
        });

        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txtUserName = inputUserName.getText().toString().trim();
                String txtEmail = inputEmail.getText().toString().trim();
                String txtPassword = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(txtUserName)) {
                    inputUserName.setError("UserName is required");
                    return;
                }
                if (TextUtils.isEmpty(txtEmail)) {
                    inputEmail.setError("Email is required");
                    return;
                }
                if (txtPassword.length() < 6) {
                    inputPassword.setError("password must be at least 6 character");
                    return;
                }

                auth.createUserWithEmailAndPassword(txtEmail, txtPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser firebaseUser = auth.getCurrentUser();
                                    String UserID = firebaseUser.getUid();

                                    reference = FirebaseDatabase.getInstance().getReference("User").child(UserID);

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    hashMap.put("ID", UserID);
                                    hashMap.put("UserName", txtUserName);
                                    hashMap.put("ImageURL", "default");


                                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "User Create", Toast.LENGTH_SHORT).show();
                                            Intent intentToLogin = new Intent(getApplicationContext(), LoginActivity.class);
                                            intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intentToLogin);
                                            finish();
                                        }
                                    }
                                });

                                } else {
                                    Toast.makeText(SignupActivity.this, "Error!" + task.getException(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

}