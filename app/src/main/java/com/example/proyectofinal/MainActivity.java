package com.example.proyectofinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectofinal.ObjetosFire.MySQLFirebase;
import com.example.proyectofinal.Principal.IComunicaFragments;
import com.example.proyectofinal.general.alimentoVo;
import com.example.proyectofinal.general.detalle_alimento_general;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentAlimentosPrincipal.OnFragmentInteractionListener,
        FragmentInicial.OnFragmentInteractionListener, IComunicaFragments, detalle_alimento_general.OnFragmentInteractionListener,
        Opciones.OnFragmentInteractionListener
        {


    private static final int MY_REQUEST_CODE = 7117;
    List<AuthUI.IdpConfig> providers;
    String usuario;

    FragmentAlimentosPrincipal fragmentAlimentosPrincipal;
    FragmentInicial fragmentInicial;
    detalle_alimento_general detalle_alimento_general;
    menu_creacion pantallaCreacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //CARGA EL FRAGMENT AL INICIAR LA APLICACIÓN
        FragmentTransaction transaction = ((MainActivity) this).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, new FragmentInicial(), "fragment_preguntas");
        transaction.commit();

       // getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new FragmentInicial()).commit();





        //AUTENTIFICACIÓN
        providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),//Email Builder
                new AuthUI.IdpConfig.PhoneBuilder().build(),//Phone Builder
                new AuthUI.IdpConfig.GoogleBuilder().build()//Email Builder

        );


        showSignInOption();


    }

            public void showSignInOption() {
                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setTheme(R.style.MyTheme)
                        .build(), MY_REQUEST_CODE
                );
            }

            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if(requestCode == MY_REQUEST_CODE){
                    IdpResponse response = IdpResponse.fromResultIntent(data);

                    if(resultCode == RESULT_OK){
                        //Get User
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //Show email
                        usuario = user.getUid();


                        if(response.isNewUser()){
                            MySQLFirebase mySQLFirebase = new MySQLFirebase();
                            MySQLFirebase.Insertar insertar = new MySQLFirebase.Insertar();
                            insertar.execute(                                    user.getUid(),
                                    user.getDisplayName(),
                                    user.getEmail()
                            );


                        }
                    }else{
                        Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem añadir = menu.findItem(R.id.idMenuAñadir);
        MenuItem mod = menu.findItem(R.id.idMenuModificar);
        MenuItem eliminar = menu.findItem(R.id.idMenuEliminar);

        añadir.setVisible(false);
        mod.setVisible(false);
        eliminar.setVisible(false);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.idMenuAñadir) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    ///SELECCIONA EL FRAGMENT EN EL MENU LATERAL
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        boolean fragmentSeleccionado = false;


        if (id == R.id.nav_camera) {
            fragment = new FragmentInicial();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_gallery) {
            fragment = new FragmentAlimentosPrincipal();
            fragmentSeleccionado = true;

        } else if (id == R.id.nav_slideshow) {
            fragment = new FragmentInicial();
            fragmentSeleccionado = true;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            fragment = new Opciones();
            fragmentSeleccionado = true;
        }


        if(fragmentSeleccionado){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void enviarAlimento(alimentoVo alimentoVo) {
        detalle_alimento_general = new detalle_alimento_general();

        Bundle bundleEnvio = new Bundle();

        bundleEnvio.putSerializable("objeto", alimentoVo);

        detalle_alimento_general.setArguments(bundleEnvio);

        //cargar el fragment en el activity

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, detalle_alimento_general).addToBackStack(null)
                .commit();
    }

    @Override
    public void tipoAccion(String action) {
        //
        //
    }


    public void changeScene(Fragment fragment, int out){
        getSupportFragmentManager().beginTransaction()
                .replace(out, fragment).addToBackStack(null)
                .commit();
    }


     public String getUsuario() {
                return usuario;
    }
}
