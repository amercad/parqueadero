package com.example.parqueadero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UsuarioActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView etUsuario, etNombre, etEmail, etClave;
    CheckBox cbActivo;

    String usuario, nombre, email, clave, usuarioId;

    byte sw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        getSupportActionBar().hide();
        etUsuario = findViewById(R.id.etUsuario);
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etClave = findViewById(R.id.etClave);
        cbActivo = findViewById(R.id.cbUsuarioActivo);
        etUsuario.requestFocus();
        sw = 0;
    }

    public void regresar(View view) {
        Intent intRegistrar=new Intent(this, MainActivity.class);
        startActivity(intRegistrar);
    }

    public void listarUsuario(View view) {
        Intent intListarUsuario =new Intent(this, ListarUsuarioActivity.class);
        startActivity(intListarUsuario);
    }

    public void adicionar(View view) {
        usuario = etUsuario.getText().toString();
        nombre = etNombre.getText().toString();
        email = etEmail.getText().toString();
        clave = etClave.getText().toString();
        if (usuario.isEmpty() || nombre.isEmpty() || email.isEmpty()
                || clave.isEmpty()) {
            Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();
        } else {
            Map<String, Object> usuarios = new HashMap<>();
            usuarios.put("Usuario", usuario);
            usuarios.put("Nombre", nombre);
            usuarios.put("Email", email);
            usuarios.put("Password", clave);
            usuarios.put("Activo", "si");

            // Add a new document with a generated ID
            db.collection("usuarios")
                .add(usuarios)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(UsuarioActivity.this, "Datos guardados!", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UsuarioActivity.this, "Error, guardando campos!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    public void consultar(View view) {
        usuario = etUsuario.getText().toString();
        if (usuario.isEmpty()) {
            Toast.makeText(this, "La placa es requerido", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();
        } else {
            db.collection("usuarios")
                .whereEqualTo("Usuario", usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sw = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuarioId = document.getId();
                                etUsuario.setText(document.getString("Usuario"));
                                etNombre.setText(document.getString("Nombre"));
                                etEmail.setText(document.getString("Email"));
                                etClave.setText(document.getString("Password"));
                                if (document.getString("Activo").equals("si")) {
                                    cbActivo.setChecked(Boolean.TRUE);
                                } else {
                                    cbActivo.setChecked(Boolean.FALSE);
                                }
                            }
                        } else {
                            Toast.makeText(UsuarioActivity.this, "Error consultando datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    public void modificar(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();
        } else {
            usuario = etUsuario.getText().toString();
            nombre = etNombre.getText().toString();
            email = etEmail.getText().toString();
            clave = etClave.getText().toString();
            if (usuario.isEmpty() || nombre.isEmpty() || email.isEmpty()
                    || clave.isEmpty()) {
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                etUsuario.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> usuarios = new HashMap<>();
                usuarios.put("Usuario", usuario);
                usuarios.put("Nombre", nombre);
                usuarios.put("Email", email);
                usuarios.put("Password", clave);
                usuarios.put("Activo", "si");

                db.collection("usuarios").document(usuarioId)
                    .set(usuarios)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UsuarioActivity.this, "Usuario actualizado correctmente...", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UsuarioActivity.this, "Error actualizando vehiculo...", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }

    }

    public void anular(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para anular debe primero consultar", Toast.LENGTH_SHORT).show();
            etUsuario.requestFocus();
        } else {
            usuario = etUsuario.getText().toString();
            nombre = etNombre.getText().toString();
            email = etEmail.getText().toString();
            clave = etClave.getText().toString();
            if (usuario.isEmpty() || nombre.isEmpty() || email.isEmpty()
                    || clave.isEmpty()) {
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                etUsuario.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> usuarios = new HashMap<>();
                usuarios.put("Usuario", usuario);
                usuarios.put("Nombre", nombre);
                usuarios.put("Email", email);
                usuarios.put("Password", clave);
                usuarios.put("Activo", "no");

                db.collection("usuarios").document(usuarioId)
                    .set(usuarios)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UsuarioActivity.this, "Usuario anulado correctmente...", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                            cbActivo.setClickable(false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UsuarioActivity.this, "Error anulando vehiculo...", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }

    }

    public void cancelar(View view) {
        limpiarCampos();
    }

    public void listar(View view){
        Intent intlistar = new Intent(this, ListarActivity.class);
        startActivity(intlistar);
    }

    private void limpiarCampos() {
        etUsuario.setText("");
        etNombre.setText("");
        etEmail.setText("");
        etClave.setText("");
        cbActivo.setChecked(false);
        sw = 0;
        etUsuario.requestFocus();
    }
}