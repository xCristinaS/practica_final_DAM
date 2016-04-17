package c.proyecto.mvp_presenters;

import android.app.Activity;

import java.lang.ref.WeakReference;

import c.proyecto.activities.EditProfileActivity;
import c.proyecto.mvp_models.UsersFirebaseManager;
import c.proyecto.mvp_presenters_interfaces.EditProfilePresenterOps;
import c.proyecto.interfaces.MyPresenter;
import c.proyecto.pojo.Usuario;

public class EditProfilePresenter implements EditProfilePresenterOps, MyPresenter {

    private static WeakReference<EditProfileActivity> activity;
    private static EditProfilePresenter presentador;
    private UsersFirebaseManager usersManager;

    private EditProfilePresenter(Activity activity){
        this.activity = new WeakReference<>((EditProfileActivity)activity);
    }

    public static EditProfilePresenter getPresentador(Activity a){
        if (presentador == null)
            presentador = new EditProfilePresenter(a);
        else
            activity = new WeakReference<>((EditProfileActivity)a);
        return presentador;
    }

    public void setUsersManager(UsersFirebaseManager usersManager) {
        this.usersManager = usersManager;
    }

    @Override
    public void updateUserProfile(Usuario u) {
        usersManager.updateUserProfile(u);
    }

}
