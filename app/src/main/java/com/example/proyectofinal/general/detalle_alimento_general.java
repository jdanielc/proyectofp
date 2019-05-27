package com.example.proyectofinal.general;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.FragmentMenu;
import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.ObjetosFire.ControlFavoritos;
import com.example.proyectofinal.ObjetosFire.MySQLFirebase;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.R;
import com.example.proyectofinal.menu_creacion;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
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
public class detalle_alimento_general extends Fragment implements View.OnClickListener {
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
    Button idIconoFav;

    Button btLike;

    Bundle objetoAlimento;

    boolean sonMisAlimentos;
    String nombreAlimento;
    String usuario;


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
        btLike = vista.findViewById(R.id.btUpvote);

        //OnClick para favoritos y push
        idIconoFav.setOnClickListener(this);
        btLike.setOnClickListener(this);

        objetoAlimento = getArguments();

        //si hay contenido en el bundle lo volcamos al objeto alimentoVo
        if(objetoAlimento != null){
            alimentoVo = (alimentoVo) objetoAlimento.getSerializable("objeto");
            imageDetalle.setImageResource(alimentoVo.getImagenDetalle());
            txtDetalle.setText(alimentoVo.getDescripcion());
            sonMisAlimentos = objetoAlimento.getBoolean("feed");
        }

        setHasOptionsMenu(true);

        //Cambio el titulo a la actividad e inicializo las opciones necesarios para el borrado en caso de ser necesario
        nombreAlimento= alimentoVo.getNombre();
        getActivity().setTitle(nombreAlimento);
        usuario = alimentoVo.getUser();


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
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.idIconoFav:

                String usuario = objetoAlimento.getSerializable("usuario").toString();
                String alimento = alimentoVo.getID()+"";

                MySQLFirebase.ListarAllFavorites listarAllFavorites = new MySQLFirebase.ListarAllFavorites(getContext());

                listarAllFavorites.execute(
                        usuario,
                        alimento
                );

                break;

            case R.id.btUpvote:

                FancyToast.makeText(getContext(), "Aun no implementado", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                break;
        }

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
        menu.clear();
        inflater.inflate(R.menu.main, menu);

        menu.findItem(R.id.idBuscar).setVisible(false);
        MenuItem mod = menu.findItem(R.id.idMenuModificar);
        MenuItem eliminar = menu.findItem(R.id.idMenuEliminar);

        if(sonMisAlimentos){
            btLike.setVisibility(View.INVISIBLE);
            idIconoFav.setVisibility(View.INVISIBLE);

        }else{
            mod.setVisible(false);
            eliminar.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.idMenuModificar) {
            abrirformulario(2);

        }else if(id == R.id.idMenuEliminar){

        //En caso de que se seleccione eliminar, llamo a la clase para eliminar y vuelvo al fragment anterior
        Eliminar eliminar = new Eliminar();

        eliminar.execute();

        Utilidades.waitingServer(getContext());

         //con esto vuelvo al fragment anterior
         getFragmentManager().popBackStack();

        }


        return super.onOptionsItemSelected(item);
    }

    private void abrirformulario(int tipo) {

        menu_creacion creacion1 = new menu_creacion();

        //Usamos el Bundle para pasar el tipo de accion a realizar
        Bundle bundle = new Bundle();

        bundle.putInt("tipo",tipo);
        bundle.putSerializable("alimento", alimentoVo);
        bundle.putString("usuario", usuario);

        creacion1.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, creacion1,"fragment_preguntas");
        transaction.commit();

        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_detalle_alimento_general, creacion1).commit();

    }




    //Llama al metodo de actualizado de la base de datos
    public void Actualizar(){

    }

    private class Eliminar extends AsyncTask<String,Integer,Boolean> {


        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpPut del =
                    new HttpPut("http://damnation.ddns.net/daniel/phpRestPFG/public/api/delalimento");

            del.setHeader("content-type", "application/json");

            try
            {
                JSONObject dato = new JSONObject();

                dato.put("usuario", usuario);
                dato.put("alimento", nombreAlimento);


                StringEntity entity = new StringEntity(dato.toString());
                del.setEntity(entity);

                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result == true)
            {

            }
        }
    }

}
