package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.ObjetosFire.MySQLFirebase;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.Utilidades;
import com.example.proyectofinal.general.alimentoVo;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link menu_creacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link menu_creacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_creacion extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    int tipoAccion;
    //ELEMENTOS VISUALES DE LA PANTALLA
    Spinner spinner;

    TextView txtSpinner;

    EditText newNombre;
    EditText newInfo;
    EditText newDescripcion;
    Button btAceptar;

    int ImagenSeleccion;
    String usuario;
    alimentoVo alimento;

    public menu_creacion(){
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment menu_creacion.
     */
    // TODO: Rename and change types and number of parameters
    public static menu_creacion newInstance(String param1, String param2) {
        menu_creacion fragment = new menu_creacion();
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

        View vista = inflater.inflate(R.layout.fragment_menu_creacion, container, false);
        vista.setBackgroundColor(Color.parseColor("#ffffff"));

        //Declaramos los elementos de la vista
        newNombre = vista.findViewById(R.id.newNombreID);
        newInfo = vista.findViewById(R.id.newInfoID);
        newDescripcion = vista.findViewById(R.id.newDescripcionID);
        spinner = vista.findViewById(R.id.spinner);
        btAceptar = vista.findViewById(R.id.btAceptar);
        txtSpinner = vista.findViewById(R.id.textSpinner);


        //Toma el id del tipo de accion que se desea realzar
        tipoAccion = getArguments().getInt("tipo");

        //Ajusta las variables necesarios dependiendo de la accion a realizar
        usuario = getArguments().getString("usuario");



        alimento = (alimentoVo) getArguments().getSerializable("alimento");


            alimento = new alimentoVo();



        //CONFIGURANDO AL SPINNER
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.tipos, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();
                btAceptar.setEnabled(true);
                switch (selection){
                    case "Bebidas": ImagenSeleccion = R.drawable.bebidas_normales ;break;
                    case "Bebidas Alcoholicas": ImagenSeleccion = R.drawable.precocinados; break;
                    case "Cereales y Legumbres":ImagenSeleccion = R.drawable.plato; break;
                    case "Dulces y Golosinas": ImagenSeleccion = R.drawable.precocinados; break;
                    case "Platos Precocinados":ImagenSeleccion = R.drawable.precocinados; break;
                    case "Salsas y Especias":ImagenSeleccion = R.drawable.especias; break;
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btAceptar.setEnabled(false);
                Toast.makeText(getActivity(), "Seleccione un tipo de alimento", Toast.LENGTH_SHORT).show();
            }
        });

          seleccion(tipoAccion);

        btAceptar.setOnClickListener(this);

        return vista;
    }

    private void seleccion(int tipoAccion) {
        switch(tipoAccion){
            //Case 1, añadir alimento
            case 1:
                getActivity().setTitle("Añadir Alimento");

                break;
            case 2:
                getActivity().setTitle("Modificar Alimento");

                //Pone los valores del alimento en los campos del formulario
                newNombre.setText(alimento.getNombre());
                newInfo.setText(alimento.getInfo());
                newDescripcion.setText(alimento.getDescripcion());

                //Pone el spinner en la posición del alimento
                ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter();
                int spinnerPosition = myAdap.getPosition(alimento.getImagenId());
                spinner.setSelection(spinnerPosition);

                break;
        }

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

                //Si todos los datos se han introducido, llamamos a la clase que añade el alimento y despues volvemos
                if(!newNombre.getText().toString().isEmpty() && !newInfo.getText().toString().isEmpty() && !newDescripcion.getText().toString().isEmpty()){

                    String nombre = newNombre.getText().toString();
                    String info = newInfo.getText().toString();
                    String descripcion = newDescripcion.getText().toString();


                    MySQLFirebase.Action action = new MySQLFirebase.Action(tipoAccion);


                    if(tipoAccion == 1){

                        action.execute(
                                nombre,
                                info,
                                descripcion,
                                usuario,
                                ImagenSeleccion+""

                        );

                    }else{
                        action.execute(
                                nombre,
                                info,
                                descripcion,
                                usuario,
                                ImagenSeleccion+"",
                                alimento.getID()+""

                        );

                    }


                    Utilidades.waitingServer(getContext());

                    Bundle bundle = new Bundle();
                    Fragment fragment = new FragmentAlimentosPrincipal();
                    bundle.putString("usuario", usuario);
                    bundle.putInt("tipo", 2);
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();

                }else {
                    FancyToast.makeText(getContext(), "Ha de rellenar todos los campos", FancyToast.LENGTH_SHORT, FancyToast.ERROR, true).show();
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

}
