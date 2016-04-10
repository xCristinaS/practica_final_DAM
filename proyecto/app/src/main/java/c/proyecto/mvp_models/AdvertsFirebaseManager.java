package c.proyecto.mvp_models;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import c.proyecto.interfaces.MyPresenter;
import c.proyecto.mvp_presenters.MainPresenter;
import c.proyecto.pojo.Anuncio;

public class AdvertsFirebaseManager {

    private static final String URL_ANUNCIOS = "https://proyectofinaldam.firebaseio.com/anuncios/";
    private static final String URL_SOLICITUDES = "https://proyectofinaldam.firebaseio.com/solicitudes/";

    private static boolean userSubRemoved = false;
    private static Firebase mFirebase;
    private static ChildEventListener listener;

    private Usuario currentUser;
    private MyPresenter presenter;

    public AdvertsFirebaseManager(MyPresenter presenter, Usuario currentUser){
        this.presenter = presenter;
        this.currentUser = currentUser;
    }

    public void publishNewAdvert(Anuncio a) { // crea un nuevo anuncio en la rama /anuncios/keyAnuncio/valorAnuncio --> keyAnuncio = numeroIdentificadoUsuarioQuePublica_numeroIdentificadorAnuncio
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(a);
    }

    public void initializeFirebaseListeners() {
        Firebase firebaseSubs = new Firebase(URL_SOLICITUDES).child(currentUser.getKey());
        final Firebase firebaseAdvertSub = new Firebase(URL_ANUNCIOS);
        firebaseSubs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String subKey = data.getKey();
                    firebaseAdvertSub.child(subKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ((MainPresenter)presenter).subHasBeenObtained(dataSnapshot.getValue(Anuncio.class));
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (mFirebase == null)
            mFirebase = new Firebase(URL_ANUNCIOS);

        if (listener != null)
            mFirebase.removeEventListener(listener);

        if (listener == null) {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(currentUser.getKey()))
                        ((MainPresenter)presenter).userAdvertHasBeenObtained(a);
                    else if (!a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter)presenter).advertHasBeenObtained(a);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (dataSnapshot.getKey().contains(currentUser.getKey()))
                        ((MainPresenter)presenter).userAdvertHasBeenModified(a);
                    else if (!userSubRemoved && a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter)presenter).subHasBeenModified(a);
                    else if (userSubRemoved)
                        ((MainPresenter)presenter).advertHasBeenObtained(a);
                    else
                        ((MainPresenter)presenter).adverHasBeenModified(a);

                    userSubRemoved = false;
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Anuncio a = dataSnapshot.getValue(Anuncio.class);
                    if (a.getSolicitantes().containsKey(currentUser.getKey()))
                        ((MainPresenter)presenter).removeSub(a);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
        }
        mFirebase.addChildEventListener(listener);
    }

    public void removeUserAdvert(Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS + a.getKey() + "/");
        mFirebase.setValue(null);
    }

    public void removeUserSub(Anuncio a) {
        Firebase mFirebase = new Firebase(URL_ANUNCIOS).child(a.getKey()).child("solicitantes").child(currentUser.getKey());
        mFirebase.setValue(null);
        mFirebase = new Firebase(URL_SOLICITUDES).child(currentUser.getKey()).child(a.getKey());
        mFirebase.setValue(null);
        userSubRemoved = true;
    }

    public void detachFirebaseListeners() {
        mFirebase.removeEventListener(listener);
        listener = null;
        mFirebase = null;
    }
}