package com.example.proyectofinal;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal.Principal.Adapter;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.alimentoVo;
import com.example.proyectofinal.general.detalle_alimento_general;

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
 * {@link FragmentAlimentosPrincipal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAlimentosPrincipal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlimentosPrincipal extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    ArrayList<alimentoVo> listaAlimentos;

    Activity activity;
    IComunicaFragments interfaceComunicaFragments;
    Adapter adapter;

    public FragmentAlimentosPrincipal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAlimentosPrincipal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAlimentosPrincipal newInstance(String param1, String param2) {
        FragmentAlimentosPrincipal fragment = new FragmentAlimentosPrincipal();
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
        View vista = inflater.inflate(R.layout.fragment_fragment_alimentos_principal, container, false);

        listaAlimentos = new ArrayList<>();
        recyclerView = (RecyclerView) vista.findViewById(R.id.recyclerAlimentosId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       // llenarLista();
        new Listar().execute();

        adapter = new Adapter(listaAlimentos);
        recyclerView.setAdapter(adapter);


        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Selección" + listaAlimentos.get(recyclerView.getChildAdapterPosition(v)).getNombre(), Toast.LENGTH_LONG).show();

                interfaceComunicaFragments.enviarAlimento(listaAlimentos.get(recyclerView.getChildAdapterPosition(v)));
            }
        });

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

        if(context instanceof  Activity){
            this.activity = (Activity) context;
            interfaceComunicaFragments = (IComunicaFragments) this.activity;
        }

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
                    new HttpGet("https://dam2.ieslamarisma.net/2019/juandcepeda/phpRestPFG/public/index.php/api/alimento");

            del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray mensajes = new JSONArray(respStr);

                for(int i =0; i<mensajes.length(); i++){

                    JSONObject mensaje = mensajes.getJSONObject(i);

                    int id = mensaje.getInt("ID");
                    String nombre = mensaje.getString("nombre");
                    String info = mensaje.getString("info");
                    int icono = mensaje.getInt("iconoId");
                    int upvotes = mensaje.getInt("upvotes");
                    int downvotes= mensaje.getInt("downvotes");
                    int usuario= mensaje.getInt("usuario");
                    String descripcion = mensaje.getString("descripcion");
                    int imagen = mensaje.getInt("imagenDetalle");

                    int fav =  mensaje.getInt("favoritos");

                    isFavorite(fav);

                    alimentoVo elemento = new alimentoVo(id, nombre, info, R.drawable.plato,descripcion, upvotes, downvotes,
                            usuario, R.drawable.plato, favorito);

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
                adapter.updateList(listaAlimentos);
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
