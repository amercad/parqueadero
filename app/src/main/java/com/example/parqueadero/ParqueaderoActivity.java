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

public class ParqueaderoActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView etPlaca, etTipoVehiculo, etHoraLlegada, etHoraSalida, etValor;
    CheckBox cbActivo;

    String placa, tipoVehiculo, horaLlegada, horaSalida, valor, placaId;

    byte sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parqueadero);

        getSupportActionBar().hide();
        etPlaca = findViewById(R.id.EtPlaca);
        etTipoVehiculo = findViewById(R.id.EtVehiculo);
        etHoraLlegada = findViewById(R.id.EtLlegada);
        etHoraSalida = findViewById(R.id.EtSalida);
        etValor = findViewById(R.id.EtValor);
        cbActivo = findViewById(R.id.cbActivo);
        etPlaca.requestFocus();
        sw = 0;
    }

    public void adicionar(View view) {
        placa = etPlaca.getText().toString();
        tipoVehiculo = etTipoVehiculo.getText().toString();
        horaLlegada = etHoraLlegada.getText().toString();
        horaSalida = etHoraSalida.getText().toString();
        valor = etValor.getText().toString();
        if (placa.isEmpty() || tipoVehiculo.isEmpty() || horaLlegada.isEmpty()
                || horaSalida.isEmpty() || valor.isEmpty()) {
            Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
            etPlaca.requestFocus();
        } else {
            Map<String, Object> vehiculo = new HashMap<>();
            vehiculo.put("Placa", placa);
            vehiculo.put("TipoVehiculo", tipoVehiculo);
            vehiculo.put("HoraLlegada", horaLlegada);
            vehiculo.put("HoraSalida", horaSalida);
            vehiculo.put("Valor", valor);
            vehiculo.put("Activo", "si");

            // Add a new document with a generated ID
            db.collection("parking-c37bd")
                .add(vehiculo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(ParqueaderoActivity.this, "Datos guardados!", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ParqueaderoActivity.this, "Error, guardando campos!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    public void consultar(View view) {
        placa = etPlaca.getText().toString();
        if (placa.isEmpty()) {
            Toast.makeText(this, "La placa es requerido", Toast.LENGTH_SHORT).show();
            etPlaca.requestFocus();
        } else {
            db.collection("parking-c37bd")
                .whereEqualTo("Placa", placa)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            sw = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                placaId = document.getId();
                                etPlaca.setText(document.getString("Placa"));
                                etTipoVehiculo.setText(document.getString("TipoVehiculo"));
                                etHoraLlegada.setText(document.getString("HoraLlegada"));
                                etHoraSalida.setText(document.getString("HoraSalida"));
                                etValor.setText(document.getString("Valor"));
                                if (document.getString("Activo").equals("si")) {
                                    cbActivo.setChecked(Boolean.TRUE);
                                } else {
                                    cbActivo.setChecked(Boolean.FALSE);
                                }
                            }
                        } else {
                            Toast.makeText(ParqueaderoActivity.this, "Error consultando datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    public void modificar(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para modificar debe primero consultar", Toast.LENGTH_SHORT).show();
            etPlaca.requestFocus();
        } else {
            placa = etPlaca.getText().toString();
            tipoVehiculo = etTipoVehiculo.getText().toString();
            horaLlegada = etHoraLlegada.getText().toString();
            horaSalida = etHoraSalida.getText().toString();
            valor = etValor.getText().toString();
            if (placa.isEmpty() || tipoVehiculo.isEmpty() || horaLlegada.isEmpty()
                    || horaSalida.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                etPlaca.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> vehiculo = new HashMap<>();
                vehiculo.put("Placa", placa);
                vehiculo.put("TipoVehiculo", tipoVehiculo);
                vehiculo.put("HoraLlegada", horaLlegada);
                vehiculo.put("HoraSalida", horaSalida);
                vehiculo.put("Valor", valor);
                vehiculo.put("Activo", "si");
                db.collection("parking-c37bd").document(placaId)
                    .set(vehiculo)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ParqueaderoActivity.this, "Vehiculo actualizado correctmente...", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ParqueaderoActivity.this, "Error actualizando vehiculo...", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        }

    }

    public void Anular(View view) {
        if (sw == 0) {
            Toast.makeText(this, "Para anular debe primero consultar", Toast.LENGTH_SHORT).show();
            etPlaca.requestFocus();
        } else {
            placa = etPlaca.getText().toString();
            tipoVehiculo = etTipoVehiculo.getText().toString();
            horaLlegada = etHoraLlegada.getText().toString();
            horaSalida = etHoraSalida.getText().toString();
            valor = etValor.getText().toString();
            if (placa.isEmpty() || tipoVehiculo.isEmpty() || horaLlegada.isEmpty()
                    || horaSalida.isEmpty() || valor.isEmpty()) {
                Toast.makeText(this, "Todos los datos requeridos", Toast.LENGTH_SHORT).show();
                etPlaca.requestFocus();
            } else {
                // Create a new user with a first and last name
                Map<String, Object> vehiculo = new HashMap<>();
                vehiculo.put("Placa", placa);
                vehiculo.put("TipoVehiculo", tipoVehiculo);
                vehiculo.put("HoraLlegada", horaLlegada);
                vehiculo.put("HoraSalida", horaSalida);
                vehiculo.put("Valor", valor);
                vehiculo.put("Activo", "no");
                db.collection("parking-c37bd").document(placaId)
                        .set(vehiculo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ParqueaderoActivity.this, "Vehiculo anulado correctmente...", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                                cbActivo.setClickable(false);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ParqueaderoActivity.this, "Error anulando vehiculo...", Toast.LENGTH_SHORT).show();
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
        etPlaca.setText("");
        etTipoVehiculo.setText("");
        etHoraLlegada.setText("");
        etHoraSalida.setText("");
        etValor.setText("");
        cbActivo.setChecked(false);
        sw = 0;
        etPlaca.requestFocus();
    }
}