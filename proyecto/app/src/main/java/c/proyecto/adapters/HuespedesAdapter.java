package c.proyecto.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import c.proyecto.R;
import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Usuario;


public class HuespedesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface IHuespedesAdapterListener {
        void onUserSubClick(Usuario u, Anuncio anuncio);
    }

    private final List<Usuario> mUsers;
    private IHuespedesAdapterListener mListener;
    private Anuncio mAnuncio;

    public HuespedesAdapter(List<Usuario> users, Anuncio anuncio) {
        mUsers = users;
        mAnuncio = anuncio;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_huesped, parent, false);
        return new HuespedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((HuespedViewHolder) holder).onBind(mUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void updateData(ArrayList<Usuario> listaSolicitantes, Anuncio a) {
        if (a.getKey().equals(mAnuncio.getKey())) {
            mUsers.clear();
            if (listaSolicitantes != null){
                mUsers.addAll(listaSolicitantes);
                notifyDataSetChanged();
            }

        }
    }

    public void setmListener(IHuespedesAdapterListener mListener) {
        this.mListener = mListener;
    }

    class HuespedViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgHuesped;
        private final TextView lblNombre;

        public HuespedViewHolder(View itemView) {
            super(itemView);
            imgHuesped = (ImageView) itemView.findViewById(R.id.imgHuesped);
            lblNombre = (TextView) itemView.findViewById(R.id.lblNombre);
        }

        public void onBind(final Usuario user) {
            Picasso.with(itemView.getContext()).load(user.getFoto()).fit().centerCrop().error(R.drawable.default_user).into(imgHuesped);
            lblNombre.setText(user.getNombre() + " " + user.getApellidos());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onUserSubClick(user, mAnuncio);
                }
            });
        }
    }

}
