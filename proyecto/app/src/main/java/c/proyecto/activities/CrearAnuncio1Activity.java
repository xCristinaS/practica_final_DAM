package c.proyecto.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import c.proyecto.Constantes;
import c.proyecto.R;
import c.proyecto.api.ImgurAPI;
import c.proyecto.api.ImgurResponse;
import c.proyecto.models.Anuncio;
import c.proyecto.utils.Imagenes;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearAnuncio1Activity extends AppCompatActivity {

    private static final String INTENT_ANUNCIO = "intent anuncio1";
    private static final int RC_ABRIR_GALERIA = 233;
    private static final int RC_CAPTURAR_FOTO = 455;
    private Anuncio mAnuncio;
    private ImageView imgSiguiente;
    private ImageView imgPrincipal;
    private ImageView img1, img2, img3, img4, img5;
    private ImageView imgSeleccionada;
    private String mPathOriginal;
    private File[] imagenesAnuncio;


    public static void start(Context context, @Nullable Anuncio anuncio){
        Intent intent = new Intent(context, CrearAnuncio1Activity.class);
        intent.putExtra(INTENT_ANUNCIO, anuncio);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_anuncio1);
        mAnuncio = getIntent().getParcelableExtra(INTENT_ANUNCIO);
        imagenesAnuncio = new File[Constantes.NUMERO_IMAGENES_ANUNCIO];
        initViews();
    }

    private void initViews() {
        imgPrincipal = (ImageView) findViewById(R.id.imgPrincipal);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        showImageDialogList(imgPrincipal);
        showImageDialogList(img1);
        showImageDialogList(img2);
        showImageDialogList(img3);
        showImageDialogList(img4);
        showImageDialogList(img5);


        imgSiguiente = (ImageView) findViewById(R.id.imgSiguiente);
        imgSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearAnuncio2Activity.start(CrearAnuncio1Activity.this, mAnuncio, imagenesAnuncio[0], imagenesAnuncio[1], imagenesAnuncio[2], imagenesAnuncio[3], imagenesAnuncio[4], imagenesAnuncio[5]);
            }
        });
    }

    private void showImageDialogList(final ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CrearAnuncio1Activity.this);
                dialog.setTitle("Seleccione una de las opciones");
                dialog.setItems(R.array.chooseImageListItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Se guarda cual fue el ultimo ImageView seleccionado
                        imgSeleccionada = img;
                        switch (which) {
                            //Galería
                            case 0:
                                openGallery();
                                break;
                            //Cámara
                            case 1:
                                if(Imagenes.hayCamara(CrearAnuncio1Activity.this))
                                    takePhoto();
                                else
                                    Toast.makeText(CrearAnuncio1Activity.this, "Este dispositivo no dispone de cámara", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                dialog.create().show();
            }
        });
    }

    private void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, RC_ABRIR_GALERIA);
    }

    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Si hay alguna actividad que sepa realizar la acción.
        if (i.resolveActivity(getPackageManager()) != null)
            // Se envía el intent esperando respuesta.
            startActivityForResult(i, RC_CAPTURAR_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
            switch (requestCode){
                case RC_ABRIR_GALERIA:
                    // Se obtiene el path real a partir de la uri retornada por la galería.
                    Uri uriGaleria = data.getData();
                    mPathOriginal = Imagenes.getRealPathFromGallery(this, uriGaleria);
                    new HiloEscalador().execute(imgSeleccionada.getWidth(), imgSeleccionada.getHeight());
                    break;
                case RC_CAPTURAR_FOTO:
                    Bitmap miniatura = (Bitmap) data.getExtras().get("data");
                    imgSeleccionada.setImageBitmap(miniatura);
                    break;
            }

    }

    class HiloEscalador extends AsyncTask<Integer, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Integer... params) {
            return Imagenes.escalar(params[0], params[1], mPathOriginal);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imgSeleccionada.setImageBitmap(bitmap);
            guardarBitmapEnArray(bitmap, imgSeleccionada.getId());
        }
    }

    private void guardarBitmapEnArray(Bitmap bitmap, int idImageView){
        switch (idImageView){
            case R.id.imgPrincipal:
                imagenesAnuncio[0] = Imagenes.crearArchivoFoto(this, "foto_piso0.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[0]);
                break;
            case R.id.img1:
                imagenesAnuncio[1] = Imagenes.crearArchivoFoto(this, "foto_piso1.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[1]);
                break;
            case R.id.img2:
                imagenesAnuncio[2] = Imagenes.crearArchivoFoto(this, "foto_piso2.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[2]);
                break;
            case R.id.img3:
                imagenesAnuncio[3] = Imagenes.crearArchivoFoto(this, "foto_piso3.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[3]);
                break;
            case R.id.img4:
                imagenesAnuncio[4] = Imagenes.crearArchivoFoto(this, "foto_piso4.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[4]);
                break;
            case R.id.img5:
                imagenesAnuncio[5] = Imagenes.crearArchivoFoto(this, "foto_piso5.jpeg", false);
                Imagenes.guardarBitmapEnArchivo(bitmap, imagenesAnuncio[5]);
                break;
        }
    }
}
