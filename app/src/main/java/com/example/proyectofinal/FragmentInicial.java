package com.example.proyectofinal;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.Principal.Adapter;
import com.example.proyectofinal.Principal.AdapterInicio;
import com.example.proyectofinal.general.alimentoGeneral;
import com.example.proyectofinal.general.alimentoVo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentInicial.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentInicial#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInicial extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //ELEMENTOS QUE VOY CREANDO
    RecyclerView recyclerInicial;
    ArrayList<alimentoGeneral> listaAlimentos;
    AdapterInicio adapterInicio;


    public FragmentInicial() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInicial.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInicial newInstance(String param1, String param2) {
        FragmentInicial fragment = new FragmentInicial();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_fragment_inicial, container, false);

        recyclerInicial = vista.findViewById(R.id.idRecyclerInicial);
        listaAlimentos = new ArrayList<>();
        recyclerInicial.setLayoutManager(new LinearLayoutManager(getContext()));

        new Listar().execute();

        adapterInicio = new AdapterInicio(listaAlimentos);

        recyclerInicial.setAdapter(adapterInicio);

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class Listar extends AsyncTask<String,Integer,Boolean> {

        private ArrayList<alimentoVo> array = new ArrayList<alimentoVo>();
        private boolean favorito;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpGet del =
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juandcepeda/phpRestPFG/public/index.php/api/inicial");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray mensajes = new JSONArray(respStr);

                for(int i =0; i<mensajes.length(); i++){

                    JSONObject mensaje = mensajes.getJSONObject(i);

                    String nombre = mensaje.getString("nombre");
                    String info = mensaje.getString("info");
                    int icono = mensaje.getInt("imagen");
                    int fav =  mensaje.getInt("favorito");

                    ///LOS FAVORIOS AUN NO ESTAN CONTROLADOS
                    isFavorite(fav);

                    alimentoGeneral elemento = new alimentoGeneral(nombre, info, R.drawable.plato, favorito);

                    listaAlimentos.add(elemento);
                }

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {
                //Rellenamos la lista con los nombres de los clientes
                //Rellenamos la lista con los resultados
                adapterInicio.updateList(listaAlimentos);
            }
        }

        private void isFavorite(int fav){
            if(fav == 1){
                favorito = true;
            }else{
                favorito = false;
            }
        }
    }
}
