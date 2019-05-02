package com.example.proyectofinal.general;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.ObjetosFire.ControlFavoritos;
import com.example.proyectofinal.ObjetosFire.MySQLFirebase;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.R;
import com.example.proyectofinal.menu_creacion;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link detalle_alimento_general.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link detalle_alimento_general#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detalle_alimento_general extends Fragment implements IComunicaFragments {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    TextView txtDetalle;
    ImageView imageDetalle;

    menu_creacion menu_creacion;

    alimentoVo alimentoVo = null;
    ImageView idIconoFav;

    Bundle objetoAlimento;

    public detalle_alimento_general() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment detalle_alimento_general.
     */
    // TODO: Rename and change types and number of parameters
    public static detalle_alimento_general newInstance(String param1, String param2) {
        detalle_alimento_general fragment = new detalle_alimento_general();
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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_detalle_alimento_general, container, false);

        txtDetalle = vista.findViewById(R.id.descripcionDetalleId);
        imageDetalle = vista.findViewById(R.id.imgenDetalleId);
        idIconoFav = vista.findViewById(R.id.idIconoFav);

        //OnClick para favoritos
        idIconoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alimentoVo.setFavorito(!alimentoVo.isFavorito());

                Actualizar();

                //getFragmentManager().popBackStack();

            }
        });

        objetoAlimento = getArguments();




        //si hay contenido en el bundle lo volcamos al objeto alimentoVo
        if(objetoAlimento != null){
            alimentoVo = (com.example.proyectofinal.general.alimentoVo) objetoAlimento.getSerializable("objeto");
            imageDetalle.setImageResource(alimentoVo.getImagenDetalle());
            txtDetalle.setText(alimentoVo.getDescripcion());
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
    public void enviarAlimento(alimentoVo alimentoVo) {
        //
    }

    @Override
    public void tipoAccion(String action) {
       menu_creacion = new menu_creacion();

        Bundle bundleEnvio = new Bundle();


        //AQUI SE PONE EL TIPO DE ACCION A REALIZAR
        bundleEnvio.putSerializable("accion", "");

        menu_creacion.setArguments(bundleEnvio);

       // MainActivity main = new MainActivity();
       // main.changeScene(menu_creacion);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.idBuscar).setVisible(false);

        MenuItem añadir = menu.findItem(R.id.idMenuAñadir);
        MenuItem mod = menu.findItem(R.id.idMenuModificar);
        MenuItem eliminar = menu.findItem(R.id.idMenuEliminar);

        añadir.setVisible(true);
        mod.setVisible(true);
        eliminar.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Fragment fragment = null;
        boolean fragmentSeleccionado = false;
        MainActivity mainActivity = ((MainActivity)getActivity());

        //noinspection SimplifiableIfStatement
        if (id == R.id.idMenuAñadir) {

            fragment = new menu_creacion();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_detalle_alimento_general, fragment).commit();


            return true;
        }




        return super.onOptionsItemSelected(item);
    }


    public void Downvote(View view){
        int preDown = alimentoVo.getDownvotes();
        preDown++;
        alimentoVo.setDownvotes(preDown);
    }

    public void Upvote(View view){
        int preDown = alimentoVo.getUpvotes();
        preDown++;
        alimentoVo.setUpvotes(preDown);

    }


    //Llama al metodo de actualizado de la base de datos
    public void Actualizar(){



        String usuario = objetoAlimento.getSerializable("usuario").toString();
        String alimento = alimentoVo.getID()+"";

        MySQLFirebase.ListarAllFavorites listarAllFavorites = new MySQLFirebase.ListarAllFavorites();

        listarAllFavorites.execute(
                usuario,
                alimento
        );



    }




}
