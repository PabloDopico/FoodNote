package com.example.apprecetas.Register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apprecetas.Login.LoginActivity;
import com.example.apprecetas.Login_Or_Register;
import com.example.apprecetas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombreUsuarioEditText;
    private EditText emailEditText;
    private EditText contraseñaEditText;
    private EditText confirmarContraseñaEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        nombreUsuarioEditText = findViewById(R.id.nombreUsuario);
        emailEditText = findViewById(R.id.email);
        contraseñaEditText = findViewById(R.id.contraseña);
        confirmarContraseñaEditText = findViewById(R.id.confirmarContraseña);

        Button registrarseButton = findViewById(R.id.register);
        registrarseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarUsuario();
            }
        });

        TextView textoIniciarSesion = findViewById(R.id.iniciarSesion);

        textoIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registrarUsuario() {
        String nombreUsuario = nombreUsuarioEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String contraseña = contraseñaEditText.getText().toString().trim();
        String confirmarContraseña = confirmarContraseñaEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nombreUsuario)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce el nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce el email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(contraseña)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmarContraseña)) {
            Toast.makeText(getApplicationContext(), "Por favor, introduce la confirmación de contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            Toast.makeText(getApplicationContext(), "¡Las contraseñas no coinciden!", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Se ha creado el usuario "+nombreUsuario+" correctamente", Toast.LENGTH_SHORT).show();

                            // Si se crea el usuario correctamente
                            FirebaseUser usuario = mAuth.getCurrentUser();
                            // Cambiamos el perfil del usuario y añadimos su  username
                            UserProfileChangeRequest modificarPerfil = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nombreUsuario)
                                    .build();
                            usuario.updateProfile(modificarPerfil);
                            // Al registrar al usuario volvemos a la pantalla principal (modificar por el HomeActivity cuando este creado)

                                    Intent intent = new Intent(RegisterActivity.this, Login_Or_Register.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Si hay error en la creacion del usuario mostramos mensaje de error
                            Toast.makeText(RegisterActivity.this, "Se ha producido un error en la creación del usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
        });

    }
}
