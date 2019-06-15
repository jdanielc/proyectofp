package com.example.proyectofinal.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.proyectofinal.Datos.Listar;
import com.example.proyectofinal.Adaptadores.Adapter;
import com.example.proyectofinal.Adaptadores.IComunicaFragments;
import com.example.proyectofinal.R;
import com.example.proyectofinal.Modelos.alimentoVo;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentAlimentosPrincipal.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentAlimentosPrincipal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAlimentosPrincipal extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    public ArrayList<alimentoVo> listaAlimentos;

    Activity activity;
    IComunicaFragments interfaceComunicaFragments;
    public Adapter adapter;

    public String usuario;
    FloatingActionButton btNuevo;
    boolean sonMisAlimentos;

    int AlimentosSeleccionados = 0;

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
        setHasOptionsMenu(true);
        View vista = inflater.inflate(R.layout.fragment_fragment_alimentos_principal, container, false);
        btNuevo = vista.findViewById(R.id.fab);


        try {
            //ASIGNO EL RECYCLERVIEW Y EL ARRAYLIST
            listaAlimentos = new ArrayList<>();
            recyclerView = (RecyclerView) vista.findViewById(R.id.recyclerAlimentosId);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //LLAMO A LA CLASE QUE LLENA EL ARRAYLIST, Y OBTENGO EL BOOLEAN QUE ME INDICA QUE DATOS CARGAR
            /**/
            if (getArguments() != null) {
                AlimentosSeleccionados = getArguments().getInt("tipo");
                usuario = getArguments().getString("usuario");

            }


            switch (AlimentosSeleccionados){
                case 0:
                    getActivity().setTitle("Comprobados");
                    FancyToast.makeText(getContext(), "Alimentos Comprobados", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();


                    btNuevo.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    getActivity().setTitle("Alimentos Generales");
                    FancyToast.makeText(getContext(), "Alimentos Generales", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                    btNuevo.setVisibility(View.INVISIBLE);

                    break;
                case 2:
                    getActivity().setTitle("Mis Alimentos");
                    FancyToast.makeText(getContext(), "Sus Alimentos", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();

                    btNuevo.setVisibility(View.VISIBLE);
                    btNuevo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            menu_creacion menu_creacion1 = new menu_creacion();

                            //Usamos el Bundle para pasar el tipo de accion a realizar
                            Bundle bundle = new Bundle();

                            bundle.putInt("tipo", 1);
                            bundle.putString("usuario", usuario);

                            menu_creacion1.setArguments(bundle);
                            //En caso de que se pulse el boton

                            btNuevo.setImageResource(R.drawable.ic_autorenew);
                            getActivity().getSupportFragmentManager().beginTransaction().
                                    replace(R.id.fragment_alimentos_principal, menu_creacion1).addToBackStack(null).commit();
                        }
                    });
                    sonMisAlimentos = true;

                    break;
                case 3:
                    btNuevo.setVisibility(View.INVISIBLE);
                    getActivity().setTitle("Favoritos");
                    FancyToast.makeText(getContext(), "Sus Favoritos", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();


                    break;
            }



            new Listar(this, AlimentosSeleccionados, getContext()).execute();
            adapter = new Adapter(listaAlimentos);
            recyclerView.setAdapter(adapter);

            adapter.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
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

        if (context instanceof Activity) {
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

        for (alimentoVo componente : listaAlimentos) {
            if (componente.getNombre().toLowerCase().contains(userInput) || componente.getDescripcion().toLowerCase().contains(userInput)) {
                newList.add(componente);
            }
        }

        if (newList.size() > 0) {
            adapter.updateList(newList);
        } else {
            FancyToast.makeText(getContext(), "Aun no ha añadido aquí alimentos", FancyToast.LENGTH_SHORT, FancyToast.INFO, true).show();
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        Bundle bundleEnvio = new Bundle();
        alimentoVo alimento = listaAlimentos.get(recyclerView.getChildAdapterPosition(v));

        bundleEnvio.putSerializable("objeto", alimento);
        bundleEnvio.putSerializable("usuario", usuario);

        //Este boleano nos indicara si se carga el recycler desde el feed o el de mis alimentos
        //Lo usaremos para saber si ocultar o no el menu

            bundleEnvio.putInt("tipo", AlimentosSeleccionados);


        detalle_alimento_general detalle = new detalle_alimento_general();
        detalle.setArguments(bundleEnvio);

        //cargar el fragment en el activity
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, detalle).addToBackStack(null)
                .commit();
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