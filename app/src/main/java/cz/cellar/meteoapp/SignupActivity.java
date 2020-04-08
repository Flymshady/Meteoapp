package cz.cellar.meteoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText email, password;
    Button btnCreateAcc;
    TextView login;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        login=findViewById(R.id.login);
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(SignupActivity.this, "Pole nesmí být prázdná", Toast.LENGTH_SHORT).show();

                }else if(!(emailValue.isEmpty() && passwordValue.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "Registrace se nezdařila, zkuste to znovu", Toast.LENGTH_SHORT).show();
                            }else{
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(SignupActivity.this, "Došlo k chybě", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

}
