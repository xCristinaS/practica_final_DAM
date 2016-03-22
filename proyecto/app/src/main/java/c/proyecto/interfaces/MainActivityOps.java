package c.proyecto.interfaces;

import java.util.ArrayList;

import c.proyecto.models.Anuncio;

/**
 * Created by Cristina on 20/03/2016.
 */
public interface MainActivityOps {
    void advertHasBeenObtained(Anuncio a);
    void adverHasBeenModified(Anuncio a);
    void subHasBeenObtained(Anuncio a);
    void subHasBeenModified(Anuncio a);
    void userAdvertHasBeenObtained(Anuncio a);
    void userAdvertHasBeenModified(Anuncio a);
    void removeSub(Anuncio a);
}
