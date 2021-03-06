package c.proyecto.mvp_presenters_interfaces;

import c.proyecto.pojo.Anuncio;
import c.proyecto.pojo.MessagePojo;
import c.proyecto.pojo.Usuario;

public interface ConversationPresenterOps {
    void userConversationRequested(MessagePojo mensaje);
    void userConversationRequested(Anuncio anuncio, Usuario usuarioAnunciante);
    void messageHasBeenObtained(MessagePojo m);
    void sendMessage(MessagePojo mensaje, String keyReceptor, boolean isFirstMessageSended);
    void detachFirebaseListeners();
    void removeMessage(MessagePojo m);
    void getAdvertFromTitle(String tituloAnuncio);
    void advertObtained(Anuncio a);
    void allMessagesObtained();
}
