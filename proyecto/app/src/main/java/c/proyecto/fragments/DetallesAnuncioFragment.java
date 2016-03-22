package c.proyecto.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.adapters.PrestacionesAdapter;
import c.proyecto.adapters.PrestacionesDetalladasAdapter;
import c.proyecto.models.Anuncio;


public class DetallesAnuncioFragment extends Fragment implements PrestacionesAdapter.IPrestacionAdapter {

    private static final String ARG_ANUNCIO = "anuncio";
    private Anuncio mAnuncio;
    private ImageView imgFoto;
    private ImageView imgAvatar;
    private TextView lblNombre;
    private TextView lblPrecio;
    private TextView lblTamano;
    private ImageView imgTipoVivienda;
    private TextView lblTipoVivienda;
    private ImageView imgCamas;
    private TextView lblCamas;
    private TextView lblNumCamas;
    private TextView lblNumHuespedes;
    private RecyclerView rvPrestaciones;
    private TextView lblDescripcionNoDisponible;
    private TextView lblDescripcion;
    private PrestacionesAdapter mPrestacionesAdapter;


    public static DetallesAnuncioFragment newInstance(Anuncio anuncio) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ANUNCIO, anuncio);

        DetallesAnuncioFragment fragment = new DetallesAnuncioFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detalles_anuncio, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        mAnuncio = getArguments().getParcelable(ARG_ANUNCIO);

        //Comprobar si es mi anuncio.

        confRecyclerview();
        mostrarDatos();

    }

    private void initViews() {
        Activity act = getActivity();

        imgFoto = (ImageView) act.findViewById(R.id.imgFoto);
        imgAvatar = (ImageView) act.findViewById(R.id.imgAvatar);
        lblNombre = (TextView) act.findViewById(R.id.lblNombre);
        lblPrecio = (TextView) act.findViewById(R.id.lblPrecio);
        lblTamano = (TextView) act.findViewById(R.id.lblTamano);
        imgTipoVivienda = (ImageView) act.findViewById(R.id.imgTipoVivienda);
        lblTipoVivienda = (TextView) act.findViewById(R.id.lblTipoVivienda);
        imgCamas = (ImageView) act.findViewById(R.id.imgCamas);
        lblCamas = (TextView) act.findViewById(R.id.lblCamas);
        lblNumCamas = (TextView) act.findViewById(R.id.lblNumCamas);
        lblNumHuespedes = (TextView) act.findViewById(R.id.lblNumero);
        rvPrestaciones = (RecyclerView) act.findViewById(R.id.rvPrestaciones);
        lblDescripcionNoDisponible = (TextView) act.findViewById(R.id.lblDescripcionNoDisponible);
        lblDescripcion = (TextView) act.findViewById(R.id.lblDescripcion);
    }

    private void confRecyclerview() {
        rvPrestaciones.setHasFixedSize(true);
        mPrestacionesAdapter = new PrestacionesAdapter(mAnuncio.getPrestaciones(), this);
        rvPrestaciones.setAdapter(mPrestacionesAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());
    }

    private void mostrarDatos() {
        if(mAnuncio.getImagenes().size()>0)
           Picasso.with(getActivity()).load(mAnuncio.getImagenes().get(0)).error(R.drawable.default_user).into(imgFoto);
        else
            Picasso.with(getActivity()).load(R.drawable.default_user).error(R.drawable.default_user).into(imgFoto);

        //Conseguir foto y nombre del anunciante

        lblPrecio.setText(String.format("%.2f%s", mAnuncio.getPrecio(), Constantes.MONEDA));
        lblTamano.setText(mAnuncio.getTamanio()+Constantes.UNIDAD);
        switch (mAnuncio.getTipo_vivienda()){
            case Constantes.HABITACION:
                imgTipoVivienda.setImageResource(R.drawable.habitacion);
                imgCamas.setImageResource(R.drawable.cama);
                lblCamas.setText(Constantes.CAMAS);
                break;
            case Constantes.CASA:
                imgTipoVivienda.setImageResource(R.drawable.casa);
                imgCamas.setImageResource(R.drawable.habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
            case Constantes.PISO:
                imgTipoVivienda.setImageResource(R.drawable.piso);
                imgCamas.setImageResource(R.drawable.habitacion);
                lblCamas.setText(Constantes.HABITACION);
                break;
        }
        lblTipoVivienda.setText(mAnuncio.getTipo_vivienda());
        lblNumCamas.setText(String.valueOf(mAnuncio.getHabitaciones_o_camas()));
        //Num Huespedes

        if(mAnuncio.getDescripcion().isEmpty())
            lblDescripcionNoDisponible.setVisibility(View.VISIBLE);
        else
            lblDescripcion.setText(mAnuncio.getDescripcion());

    }

    @Override
    public void onPrestacionClicked() {
        mostrarDialogoPrestaciones();
    }

    private void mostrarDialogoPrestaciones() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        final View dialogView = View.inflate(getActivity(), R.layout.dialog_prestaciones_detalladas, null);
        dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(true);


        rvPrestaciones = (RecyclerView) dialogView.findViewById(R.id.rvPrestaciones);


        rvPrestaciones.setAdapter(new PrestacionesDetalladasAdapter(mAnuncio.getPrestaciones()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvPrestaciones.setLayoutManager(mLayoutManager);
        rvPrestaciones.setItemAnimator(new DefaultItemAnimator());

        dialog.show();
        Point boundsScreen = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(boundsScreen);

        dialog.getWindow().setLayout((int)(boundsScreen.x * Constantes.PORCENTAJE_PANTALLA), WindowManager.LayoutParams.WRAP_CONTENT);
    }

}
