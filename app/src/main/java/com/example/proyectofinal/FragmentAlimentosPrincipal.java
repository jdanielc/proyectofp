package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.proyectofinal.ObjetosFire.ControlFavoritos;
import com.example.proyectofinal.Principal.Adapter;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.Modelo;
import com.example.proyectofinal.general.Utilidades;
import com.example.proyectofinal.general.alimentoVo;
import com.example.proyectofinal.general.detalle_alimento_general;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAlimentosPrincipal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAlimentosPrincipal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlimentosPrincipal extends Fragment implements SearchView.OnQueryTextListener{
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

    String usuario;
    FloatingActionButton btNuevo;
    boolean sonMisAlimentos;

    public FragmentAlimentosPrincipal() {
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

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //
        View vista = inflater.inflate(R.layout.fragment_fragment_alimentos_principal, container, false);

        //SI NO ES LA PRIMEAVEZ QUE SE CARGA EL FRAGMENT HACEMOS ESPERAR DURANTE UN SEGUNDO
        if(savedInstanceState != null){
            Utilidades.waitingServer(getContext());
        }

        //ASIGNO EL RECYCLERVIEW Y EL ARRAYLIST
        listaAlimentos = new ArrayList<>();
        recyclerView = (RecyclerView) vista.findViewById(R.id.recyclerAlimentosId);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //LLAMO A LA CLASE QUE LLENA EL ARRAYLIST, Y OBTENGO EL BOOLEAN QUE ME INDICA QUE DATOS CARGAR
        boolean AlimentosSeleccionados = getArguments().getBoolean("mis");

        //SI SE HA SOLICITA LOS ALIMENTOS DEL USUARIO ACTUAL, TOMAMOS DEL BUNDLE EL USUARIO

        btNuevo = vista.findViewById(R.id.fab);

        if(AlimentosSeleccionados){
            usuario = getArguments().getString("usuario");

            btNuevo.setVisibility(View.VISIBLE);
            btNuevo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    menu_creacion menu_creacion1 = new menu_creacion();

                    //Usamos el Bundle para pasar el tipo de accion a realizar
                    Bundle bundle = new Bundle();

                    bundle.putInt("tipo",1);
                    bundle.putString("usuario",usuario);

                    menu_creacion1.setArguments(bundle);
                    //En caso de que se pulse el boton

                        getActivity().getSupportFragmentManager().beginTransaction().
                                replace(R.id.fragment_alimentos_principal, menu_creacion1).addToBackStack(null).commit();

                         btNuevo.setVisibility(View.INVISIBLE);

                }
            });

            sonMisAlimentos = true;
        }else{
                btNuevo.setVisibility(View.INVISIBLE);


            sonMisAlimentos = false;

        }

        new Listar(AlimentosSeleccionados).execute();

        adapter = new Adapter(listaAlimentos);
        recyclerView.setAdapter(adapter);


        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundleEnvio = new Bundle();

                alimentoVo alimento = listaAlimentos.get(recyclerView.getChildAdapterPosition(v));

                bundleEnvio.putSerializable("objeto", alimento);

                bundleEnvio.putSerializable("usuario", usuario);


                //Este boleano nos indicara si se carga el recycler desde el feed o el de mis alimentos
                //Lo usaremos para saber si ocultar o no el menu
                if(sonMisAlimentos){
                    bundleEnvio.putBoolean("feed", true);
                }else{
                    bundleEnvio.putBoolean("feed", false);

                }

                detalle_alimento_general detalle = new detalle_alimento_general();

                detalle.setArguments(bundleEnvio);

                //cargar el fragment en el activity

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, detalle).addToBackStack(null)
                        .commit();

            }
        });


        setHasOptionsMenu(true);

        if(sonMisAlimentos){
            getActivity().setTitle("Mis Alimentos");
        }else{
            getActivity().setTitle("Forum");
        }

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);


        MenuItem mod = menu.findItem(R.id.idMenuModificar);
        MenuItem eliminar = menu.findItem(R.id.idMenuEliminar);


        mod.setVisible(false);
        eliminar.setVisible(false);

        MenuItem menuItem = menu.findItem(R.id.idBuscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<alimentoVo> newList = new ArrayList<alimentoVo>();
        for(alimentoVo componente : listaAlimentos){
            if(componente.getNombre().toLowerCase().contains(userInput) || componente.getDescripcion().toLowerCase().contains(userInput)){
                newList.add(componente);
            }
        }
        adapter.updateList(newList);
        return true;
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

        boolean AlimentosListar;

        public Listar(boolean AlimentosListar){
            this.AlimentosListar = AlimentosListar;
        }

        private ArrayList<alimentoVo> array = new ArrayList<alimentoVo>();
        private boolean favorito;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet del = null;

            if(AlimentosListar){
                del = new HttpGet("http://damnation.ddns.net/phpRestPFG/public/index.php/api/alimento/" + usuario);

            }else {
                del = new HttpGet("http://damnation.ddns.net/phpRestPFG/public/index.php/api/alimento");

            }

            try
            {

                del.setHeader("content-type", "application/json");

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
                    String descripcion = mensaje.getString("descripcion");
                    int imagen = mensaje.getInt("imagenDetalle");
                    String usuario= mensaje.getString("usuario");


                    alimentoVo elemento = new alimentoVo(id, nombre, info, icono ,descripcion, upvotes, downvotes,
                            usuario, imagen);

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

    }
}
