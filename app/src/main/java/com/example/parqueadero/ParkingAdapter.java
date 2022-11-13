package com.example.parqueadero;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter <ParkingAdapter.ParkingViewHolder> {

    ArrayList<Parking> objParking;

    public ParkingAdapter(ArrayList<Parking> objParking) {
        this.objParking = objParking;
    }

    @NonNull
    @Override
    public ParkingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parkingresource,null,false);
        return new ParkingAdapter.ParkingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingViewHolder holder, int position) {
        holder.placa.setText("Placa: " + objParking.get(position).getPlaca());
        holder.tipoVehiculo.setText("Tipo de vehiculo: " + objParking.get(position).getTipoVehiculo());
        holder.horaLlegada.setText("Hora de llegada: " + objParking.get(position).getHoraLlegada());
        holder.horaSalida.setText("Hora de salida: " + objParking.get(position).getHoraSalida());
        holder.valor.setText("Valor: " + objParking.get(position).getValor());
        holder.estado.setText("Estado: " + objParking.get(position).getEstado());
    }

    @Override
    public int getItemCount() {
        return this.objParking.size();
    }

    public class ParkingViewHolder extends RecyclerView.ViewHolder {
        TextView placa, tipoVehiculo, horaLlegada, horaSalida, valor, estado;
        public ParkingViewHolder(@NonNull View itemView) {
            super(itemView);
            placa = itemView.findViewById(R.id.tvPlaca);
            tipoVehiculo = itemView.findViewById(R.id.tvTipoVehiculo);
            horaLlegada = itemView.findViewById(R.id.tvHoraLlegada);
            horaSalida = itemView.findViewById(R.id.tvHoraSalida);
            valor = itemView.findViewById(R.id.tvValor);
            estado = itemView.findViewById(R.id.tvActivo);

        }
    }
}
