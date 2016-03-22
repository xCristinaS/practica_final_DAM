package c.proyecto.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import c.proyecto.R;
import c.proyecto.activities.MainActivity;
import c.proyecto.adapters.MyRecyclerViewAdapter;
import c.proyecto.models.Anuncio;


public class MyRecyclerViewFragment extends Fragment {

    private static final String ARG_ADAPTER_TYPE = "type_of_adapter";
    //Views
    private RecyclerView rvLista;
    private MyRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    //Variables
    private ArrayList<Anuncio> mAnuncios;
    private int adapter_type;

    public static MyRecyclerViewFragment newInstance(int adapter_type) {
        Bundle args = new Bundle();
        args.putInt(ARG_ADAPTER_TYPE, adapter_type);
        MyRecyclerViewFragment fragment = new MyRecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    private void initViews() {
        Bundle args = getArguments();
        adapter_type = args.getInt(ARG_ADAPTER_TYPE);

        rvLista = (RecyclerView) getView().findViewById(R.id.rvLista);
        mAdapter = new MyRecyclerViewAdapter(adapter_type);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvLista.setAdapter(mAdapter);
        rvLista.setLayoutManager(mLayoutManager);
        rvLista.setItemAnimator(new DefaultItemAnimator());
        //rvLista.setHasFixedSize(true);

        if (adapter_type == MyRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS || adapter_type == MyRecyclerViewAdapter.ADAPTER_TYPE_SUBS) {// Se crea el helper.
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    Anuncio a = mAdapter.getAdvert(viewHolder.getAdapterPosition());
                    if (adapter_type == MyRecyclerViewAdapter.ADAPTER_TYPE_MY_ADVS)
                        ((MainActivity) getActivity()).getmPresenter().removeUserAdvert(a);
                    else
                        ((MainActivity) getActivity()).getmPresenter().removeUserSub(a, ((PrincipalFragment) getParentFragment()).getUser());
                    mAdapter.removeItem(a);
                }
            });
            itemTouchHelper.attachToRecyclerView(rvLista);
        }
    }

    public MyRecyclerViewAdapter getmAdapter() {
        return mAdapter;
    }
}
