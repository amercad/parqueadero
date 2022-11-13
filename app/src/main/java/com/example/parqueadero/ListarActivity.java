package com.example.parqueadero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListarActivity extends AppCompatActivity {

    RecyclerView recyclerpaseo;
    ArrayList<Parking> listaParking;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        recyclerpaseo = findViewById(R.id.rvListarParking);
        listaParking = new ArrayList<>();
        recyclerpaseo.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        recyclerpaseo.setHasFixedSize(true);
        cargarParking();
    }

    private void cargarParking() {
        db.collection("parking-c37bd")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                Parking objParking = new Parking();
                                objParking.setPlaca(document.getString("Placa"));
                                objParking.setTipoVehiculo(document.getString("TipoVehiculo"));
                                objParking.setHoraLlegada(document.getString("HoraLlegada"));
                                objParking.setHoraSalida(document.getString("HoraSalida"));
                                objParking.setValor(document.getString("Valor"));
                                objParking.setEstado(document.getString("Activo"));
                                listaParking.add(objParking);
                            }
                            ParkingAdapter adpaseo = new ParkingAdapter(listaParking);
                            recyclerpaseo.setAdapter(adpaseo);
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}