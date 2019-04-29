package com.example.proyectofinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.proyectofinal.Principal.IComunicaFragments;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link menu_creacion.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link menu_creacion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class menu_creacion extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    IComunicaFragments interfaceComunica;
    String action;

    //ELEMENTOS VISUALES DE LA PANTALLA
    Spinner spinner;

    TextView txtID;
    TextView txtSpinner;

    EditText newID;
    EditText newNombre;
    EditText newInfo;
    EditText newDescripcion;
    Button btAceptar;

    public menu_creacion() {
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

        Bundle tipoAccion = getArguments();

        if(tipoAccion != null){
            action = (String) tipoAccion.getSerializable("accion");

        }



     //   newID = vista.findViewById(R.id.txtID);
        newNombre = vista.findViewById(R.id.txtNombre);
        newInfo = vista.findViewById(R.id.txtInfo);
        newDescripcion = vista.findViewById(R.id.txtDescripcion);

        //txtID = vista.findViewById(R.id.txtID);
        txtSpinner = vista.findViewById(R.id.textSpinner);

        //CONFIGURANDO AL SPINNER
        spinner = vista.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.tipos, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();

                switch (selection){
                    case "Bebidas": ;break;
                    case "Bebidas Alcoholicas": ;break;
                    case "Cereales y Legumbres": ;break;
                    case "Dulces y Golosinas": ;break;
                    case "Platos Precocinados": ;break;
                    case "Salsas y Especias": ;break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seleccion();


        return vista;
    }

    private void seleccion() {
        switch (action){
            case "añadir":
                newID.setVisibility(View.INVISIBLE);
                txtID.setVisibility(View.INVISIBLE);
            break;
            case "eliminar":
                spinner.setVisibility(View.INVISIBLE);
                txtSpinner.setVisibility(View.INVISIBLE);
                break;

            case "": ;break;
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
