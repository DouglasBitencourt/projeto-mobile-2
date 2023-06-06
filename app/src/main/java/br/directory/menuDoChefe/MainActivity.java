package br.directory.menuDoChefe;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import br.directory.menuDoChefe.Controlador.BancoControl;
import br.directory.menuDoChefe.DAOO.MockDaoJSON;
import br.directory.menuDoChefe.Entidade.Metodo;

public class MainActivity extends AppCompatActivity {

    private static final String PATH_IMAGES = "/data/user/0/br.directory.menuDoChefe/app_imageDir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MockDaoJSON mock = new MockDaoJSON();
        BancoControl bancoControl = new BancoControl(getBaseContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout lL = findViewById(R.id.linearLayout);

        Executor executor = Executors.newFixedThreadPool(10);
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                List<Metodo> metodos;
                List<Bitmap> listImagens;
                boolean comInternet = ConectadoInternet();
                if (comInternet) {
                    metodos = mock.getAllPostsFromApiMock();
                    listImagens = mock.getAllImagesFromApiMockByPosts(metodos);
                    if(!bancoControl.ChecksBD()) {
                        SaveBDImagensLoc(bancoControl, metodos, listImagens);
                    }
                } else {

                    metodos = bancoControl.carregaPosts();
                    listImagens = bancoControl.UpploadImagens(metodos, PATH_IMAGES);
                }
                final List<Metodo> finalMetodos = metodos;
                final List<Bitmap> finalListImagens = listImagens;
                handler.post(() -> {
                    if (!comInternet) {
                        Toast.makeText(getApplicationContext(), "Sem conexão com a internet internet.", Toast.LENGTH_LONG).show();
                    }
                    UploadMetPosts(lL, finalMetodos, finalListImagens);
                    System.out.println("Reconectado.");
                });
            } catch (Exception e) {
                System.err.print("Erro: " + e.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void UploadMetPosts(LinearLayout lL, List<Metodo> finalMetodos, List<Bitmap> finalListImagens) {
        for (int i = 0; i < finalMetodos.size(); i++) {
            LinearLayout layoutInternoTotal = new LinearLayout(this);
            layoutInternoTotal.setBackgroundColor(Color.rgb(255, 0, 0));
            layoutInternoTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutInternoTotal.setOrientation(LinearLayout.VERTICAL);

            LinearLayout layoutInterno = new LinearLayout(this);
            layoutInterno.setBackgroundColor(Color.rgb(255, 0, 0));
            layoutInterno.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutInterno.setOrientation(LinearLayout.HORIZONTAL);
            lL.addView(layoutInternoTotal);

            ImageView imagem = new ImageView(this);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(500, 500);
            imagem.setLayoutParams(params);
            imagem.setImageBitmap(finalListImagens.get(i));
            imagem.setPadding(30,50,30, 0);

            TextView titulo = new TextView(lL.getContext());
            titulo.setText(finalMetodos.get(i).getTitulo());
            titulo.setTextSize(40f);
            titulo.setPadding(30,125,30, 10);

            TextView descricao = new TextView(lL.getContext());
            descricao.setText(finalMetodos.get(i).getDescricao());
            descricao.setTextSize(17f);
            descricao.setPadding(30,5,30, 30);

            TextView preco = new TextView(lL.getContext());
            String precoLabel = finalMetodos.get(i).getPreco() != null ? "R$"+ finalMetodos.get(i).getPreco().toString() : "Consulte o preco";
            preco.setText("Preco:" + precoLabel);
            preco.setTextSize(25f);
            preco.setTextColor(Color.rgb(255, 0, 0));
            preco.setPadding(35,10,35, 35);

            layoutInterno.addView(imagem);
            layoutInterno.addView(titulo);

            layoutInternoTotal.addView(layoutInterno);
            layoutInternoTotal.addView(descricao);
            layoutInternoTotal.addView(preco);
        }
    }

    private void SaveBDImagensLoc(BancoControl bancoControl, List<Metodo> metodos, List<Bitmap> imagens) {
        for (int i = 0; i < metodos.size(); i++) {
            String urlImage = metodos.get(i).getUrlImagem();
            String titulo = metodos.get(i).getTitulo();
            String descricao = metodos.get(i).getDescricao();
            String nomeArquivo = "imagem"+ metodos.get(i).getId();
            bancoControl.ImputImageLocal(imagens.get(i), nomeArquivo, this);
            bancoControl.inserir(titulo, descricao, urlImage);
        }
    }

    private boolean ConectadoInternet() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }



}