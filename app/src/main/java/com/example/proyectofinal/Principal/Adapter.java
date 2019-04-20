package com.example.proyectofinal.Principal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyectofinal.R;
import com.example.proyectofinal.general.alimentoVo;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.AlimentoViewHolder>{

    ArrayList<alimentoVo> listaAlimentos;

    public Adapter(ArrayList<alimentoVo> listaAlimentos){
        this.listaAlimentos = listaAlimentos;
    }

    @NonNull
    @Override
    public AlimentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new AlimentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlimentoViewHolder alimentoViewHolder, int i) {
        alimentoViewHolder.txtNombre.setText(listaAlimentos.get(i).getNombre());
        alimentoViewHolder.txtInformacion.setText(listaAlimentos.get(i).getInfo());
        alimentoViewHolder.foto.setImageResource(listaAlimentos.get(i).getImagenId());
    }

    @Override
    public int getItemCount() {
        return listaAlimentos.size();
    }

    public class AlimentoViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre,txtInformacion;
        ImageView foto;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtInformacion= (TextView) itemView.findViewById(R.id.idInfo);
            foto= (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
