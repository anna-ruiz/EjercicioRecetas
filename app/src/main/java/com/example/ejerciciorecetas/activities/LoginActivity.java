package com.example.ejerciciorecetas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ejerciciorecetas.R;
import com.example.ejerciciorecetas.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instanciamos el objeto auth
        auth = FirebaseAuth.getInstance();


        binding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardamos el valor de los input
                String email = binding.txtEmailLogin.getText().toString();
                String password = binding.txtPasswordLogin.getText().toString();

                //comprobamos q no estan vacios
                if (!email.isEmpty() && !password.isEmpty()){
                    doLogin(email,password);
                }

            }
        });

        binding.btnRegisterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardamos el valor de los input
                String email = binding.txtEmailLogin.getText().toString();
                String password = binding.txtPasswordLogin.getText().toString();

                //comprobamos q no estan vacios
                if (!email.isEmpty() && !password.isEmpty()){
                    doRegister(email,password);
                }

            }
        });

    }

    private void doRegister(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override //Si va bien
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ //si ha ido bien la autenticacion
                            //creamos metodo para poder cambiar de ventana (lo recomienda la documentacion)
                            updateUI(auth.getCurrentUser()); //le pasamos el auth.getCurrentUser xq tendra el user si va bien o null

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override //Si va mal
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void doLogin(String email, String password) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override //Si va bien
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){ //si ha ido bien la autenticacion
                            //creamos metodo para poder cambiar de ventana (lo recomienda la documentacion)
                            updateUI(auth.getCurrentUser()); //le pasamos el auth.getCurrentUser xq tendra el user si va bien o null

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override //Si va mal
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){ //si si ha podido loggear
            finish(); //Cerramos la ventana
        }
    }
}