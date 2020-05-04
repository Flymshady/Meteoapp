package cz.cellar.meteoapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    EditText email, password;
    Button btnLogin;
    TextView signup;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        signup=findViewById(R.id.signup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Jste přihlášen/a", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Přihlašte se", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String emailValue = email.getText().toString();
                    String passwordValue = password.getText().toString();
                    if(emailValue.isEmpty()){
                        email.setError("Prosím zadejte email");
                        email.requestFocus();
                    }else if(passwordValue.isEmpty()){
                        password.setError("Prosím zadejte heslo");
                        password.requestFocus();
                    }else if(emailValue.isEmpty() && passwordValue.isEmpty()){
                        Toast.makeText(MainActivity.this, "Pole nesmí být prázdná", Toast.LENGTH_SHORT).show();

                    }else if(!(emailValue.isEmpty() && passwordValue.isEmpty())){
                        mFirebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Došlo k chybě při přihlášení, zkuste to znovu", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent intToHome = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intToHome);
                                }
                            }
                        });

                    }else{
                        Toast.makeText(MainActivity.this, "Došlo k chybě", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignup = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intSignup);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        signup=findViewById(R.id.signup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null) {
                    Toast.makeText(MainActivity.this, "Jste přihlášen/a", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Přihlašte se", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();
                if(emailValue.isEmpty()){
                    email.setError("Prosím zadejte email");
                    email.requestFocus();
                }else if(passwordValue.isEmpty()){
                    password.setError("Prosím zadejte heslo");
                    password.requestFocus();
                }else if(emailValue.isEmpty() && passwordValue.isEmpty()){
                    Toast.makeText(MainActivity.this, "Pole nesmí být prázdná", Toast.LENGTH_SHORT).show();

                }else if(!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Došlo k chybě při přihlášení, zkuste to znovu", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intToHome = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intToHome);
                            }
                        }
                    });

                }else{
                    Toast.makeText(MainActivity.this, "Došlo k chybě", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignup = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intSignup);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}




