package com.example.proyectofinal.Principal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.general.alimentoGeneral;

import java.util.ArrayList;

public class AdapterInicio extends RecyclerView.Adapter<AdapterInicio.InicioViewHolder> {
     ArrayList<alimentoGeneral> list = new ArrayList<>();

     public AdapterInicio(ArrayList<alimentoGeneral> list){
         this.list = list;
     }


    @NonNull
    @Override
    public InicioViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inicial,null,false);
        return new InicioViewHolder(view);
     }

    @Override
    public void onBindViewHolder(@NonNull InicioViewHolder inicioViewHolder, int i) {

        inicioViewHolder.idInfo.setText(list.get(i).getInfo());
        inicioViewHolder.idNombre.setText(list.get(i).getNombre());
        inicioViewHolder.idImagen.setImageResource(list.get(i).getImagenId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<alimentoGeneral> listaAlimentos) {
         list = new ArrayList<>();
         list = listaAlimentos;
         notifyDataSetChanged();
    }

    public class InicioViewHolder extends RecyclerView.ViewHolder {
         TextView idNombre;
         TextView idInfo;
         ImageView idImagen;




        public InicioViewHolder(@NonNull View itemView) {
            super(itemView);

            idImagen = itemView.findViewById(R.id.idImagenInicioItem);
            idInfo = itemView.findViewById(R.id.idInfoItemInicio);
            idNombre = itemView.findViewById(R.id.idNombreItemInici);
        }
    }
}
