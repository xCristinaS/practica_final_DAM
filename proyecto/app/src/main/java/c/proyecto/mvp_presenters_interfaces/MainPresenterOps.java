package c.proyecto.mvp_presenters_interfaces;

import com.firebase.geofire.GeoLocation;

import java.util.ArrayList;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.Prestacion;
import c.proyecto.pojo.Usuario;
import c.proyecto.pojo.MessagePojo;

public interface MainPresenterOps {
    void removeUserAdvert(Anuncio a);
    void removeUserSub(Anuncio a);
    void initializeFirebaseListeners(Usuario usuario);
    void advertHasBeenObtained(Anuncio a);
    void adverHasBeenModified(Anuncio a);
    void subHasBeenObtained(Anuncio a);
    void subHasBeenModified(Anuncio a);
    void userAdvertHasBeenObtained(Anuncio a);
    void userAdvertHasBeenModified(Anuncio a);
    void removeSub(Anuncio a);
    void detachListeners();
    void requestUserMessages(Usuario user);
    void userMessageHasBeenObtained(MessagePojo m);
    void userHasBeenModified(Usuario user);
    void removeAdvert(Anuncio a);
    void sendAdvertHasBeenRemovedBroadcast();
    void filterRequest(String[] tipoVivienda, int minPrice, int maxPrice, int minSize, int maxSize, ArrayList<Prestacion> prestaciones, String provincia, String poblacion);
    void onFilterResponsed(ArrayList<Anuncio> filteredAdverts);
    void detachAdvertsListener();
    void attachAdvertsListeners();
    void getLocations(GeoLocation centerPosition, double radius);
    void detachGeoLocationListeners();
    void locationObtained(Anuncio a, GeoLocation location);
}
