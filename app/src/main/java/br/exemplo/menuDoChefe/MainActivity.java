package br.exemplo.menuDoChefe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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


import br.exemplo.menuDoChefe.controller.BancoController;
import br.exemplo.menuDoChefe.dao.MockDao;
import br.exemplo.menuDoChefe.entity.Post;

public class MainActivity extends AppCompatActivity {

    private static String PATH_IMAGES = "/data/user/0/br.exemplo.menuDoChefe/app_imageDir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MockDao mock = new MockDao();
        BancoController bancoController = new BancoController(getBaseContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout lL = findViewById(R.id.linearLayout);

        Executor executor = Executors.newFixedThreadPool(10);
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                List<Post> posts;
                List<Bitmap> listImagens;
                boolean comInternet = logadoNaInternet();
                if (comInternet) {
                    //CARREGA DO MOCK DAO
                    posts = mock.getAllPostsFromApiMock();
                    listImagens = mock.getAllImagesFromApiMockByPosts(posts);
                    if(!bancoController.verificaBancoPopulado()) {
                        salvarNoBancoEImagensLocalmente(bancoController, posts, listImagens);
                    }
                } else {
                    // PARA CAIR AQUI NÃO ESQUEÇA DE COLOCAR NO MODO AVIÃO
                    //CARREGA DA MEMÓRIA INTERNA
                    posts = bancoController.carregaPosts();
                    listImagens = bancoController.carregaImagens(posts, PATH_IMAGES);
                }
                //EXIGÊNCIA QUE A VARIÁVEL SEJA FINAL
                final List<Post> finalPosts = posts;
                final List<Bitmap> finalListImagens = listImagens;
                handler.post(() -> {
                    if (!comInternet) {
                        Toast.makeText(getApplicationContext(), "Sem conexão com a internet internet.", Toast.LENGTH_LONG).show();
                    }
                    carregarPosts(lL, finalPosts, finalListImagens);
                    System.out.println("Reconectado.");
                });
            } catch (Exception e) {
                System.err.print("Erro: " + e.getMessage());
            }
        });
    }

    private void carregarPosts(LinearLayout lL, List<Post> finalPosts, List<Bitmap> finalListImagens) {
        for (int i = 0; i < finalPosts.size(); i++) {
            //CRIACAO DE UM LAYOUT MAIOR PARA AJUSTAR AS INFORMAÇÕES MELHOR
            LinearLayout layoutInternoTotal = new LinearLayout(this);
            layoutInternoTotal.setBackgroundColor(Color.rgb(255, 0, 0));
            layoutInternoTotal.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layoutInternoTotal.setOrientation(LinearLayout.VERTICAL);

            //NESSE LAYOUT VAI A IMAGEM E O TÍTULO
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
            titulo.setText(finalPosts.get(i).getTitulo());
            titulo.setTextSize(40f);
            titulo.setPadding(30,125,30, 10);

            TextView descricao = new TextView(lL.getContext());
            descricao.setText(finalPosts.get(i).getDescricao());
            descricao.setTextSize(17f);
            descricao.setPadding(30,5,30, 30);

            TextView preco = new TextView(lL.getContext());
            String precoLabel = finalPosts.get(i).getPreco() != null ? "R$"+finalPosts.get(i).getPreco().toString() : "Consulte o preço!";
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

    private void salvarNoBancoEImagensLocalmente(BancoController bancoController, List<Post> posts, List<Bitmap> imagens) {
        for (int i = 0; i < posts.size(); i++) {
            String titulo = posts.get(i).getTitulo();
            String descricao = posts.get(i).getDescricao();
            String urlImage = posts.get(i).getUrlImagem();
            String nomeArquivo = "imagem"+posts.get(i).getId();
            bancoController.inserirImagemLocalmente(imagens.get(i), nomeArquivo, this);
            bancoController.inserir(titulo, descricao, urlImage);
        }
    }

    private boolean logadoNaInternet() {
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }



}