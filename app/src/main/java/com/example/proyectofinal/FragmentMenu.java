package com.example.proyectofinal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMenu extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    View vista;

    Button btInicio;
    Button btLista;
    Button btForum;
    Button btFav;
    Button btMis;
    Button btOpciones;



    public FragmentMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMenu newInstance(String param1, String param2) {
        FragmentMenu fragment = new FragmentMenu();
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
        getActivity().setTitle("Inicio");
        // Inflate the layout for this fragment
        vista =  inflater.inflate(R.layout.fragment_menu, container, false);

        btInicio = vista.findViewById(R.id.btIniciom);
        btLista = vista.findViewById(R.id.btLitam);
        btForum= vista.findViewById(R.id.btForumm);
        btFav = vista.findViewById(R.id.btFavm);
        btMis= vista.findViewById(R.id.btMism);
        btOpciones = vista.findViewById(R.id.btOpcm);

        btInicio.setOnClickListener(this);
        btLista.setOnClickListener(this);
        btForum.setOnClickListener(this);
        btFav.setOnClickListener(this);
        btMis.setOnClickListener(this);
        btOpciones.setOnClickListener(this);

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
        Fragment fragment;
        Fragment sustit;
        Bundle bundle = new Bundle();
        String usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();

        switch (v.getId()) {
            case R.id.btIniciom:

                fragment = new FragmentMenu();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();

                break;
            case R.id.btLitam:
                fragment = new FragmentInicial();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                break;
            case R.id.btForumm:

                fragment = new FragmentAlimentosPrincipal();
                bundle.putString("usuario", usuario);
                bundle.putInt("tipo", 1);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                break;
            case R.id.btMism:
                fragment = new FragmentAlimentosPrincipal();
                bundle.putString("usuario", usuario);
                bundle.putInt("tipo", 2);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                break;
            case R.id.btFavm:

                fragment = new FragmentAlimentosPrincipal();
                bundle.putString("usuario", usuario);
                bundle.putInt("tipo", 3);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
                break;
            case R.id.btOpcm:
                fragment = new Opciones();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
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

}
