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

public class Adapter extends RecyclerView.Adapter<Adapter.AlimentoViewHolder> implements View.OnClickListener {

    ArrayList<alimentoVo> listaAlimentos;
    private View.OnClickListener listener;

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

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public void updateList(ArrayList<alimentoVo> listaNueva){
        listaAlimentos = new ArrayList<>();
        listaAlimentos.addAll(listaNueva);
        notifyDataSetChanged();
    }



    public class AlimentoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtNombre,txtInformacion;
        ImageView foto;

        public AlimentoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre= (TextView) itemView.findViewById(R.id.idNombre);
            txtInformacion= (TextView) itemView.findViewById(R.id.idInfo);
            foto= (ImageView) itemView.findViewById(R.id.idImagen);

            itemView.setOnClickListener((View.OnClickListener) this);

        }


        @Override
        public void onClick(View v) {
            if(listener!=null){
                listener.onClick(v);
            }
        }
    }
}
