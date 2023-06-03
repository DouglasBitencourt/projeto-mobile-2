package br.exemplo.menuDoChefe.mockApi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.exemplo.menuDoChefe.entity.Post;

public class ApiMock {

    private List<Post> posts = new ArrayList<>();

    public ApiMock() {
        carregarPosts();
    }

    private void carregarPosts() {
        posts.add(new Post(1L, "Prato: Salmão Grelhado com Molho de Limão", " Um suculento filé de salmão grelhado, servido com um molho de limão fresco e acompanhado de legumes salteados. Uma opção saudável e deliciosa para os amantes de peixe.", "https://i.im.ge/2023/06/04/hbhqqL.filezin.jpg", 35.90));
        posts.add(new Post(2L, "Prato: Risoto de Cogumelos Silvestres", "Um cremoso risoto preparado com uma variedade de cogumelos silvestres, finalizado com queijo parmesão e um toque de trufas. Uma explosão de sabores terrosos em cada garfada.", "https://i.im.ge/2023/06/04/hbhYsq.risoto-cogumelo.jpg", 28.50));
        posts.add(new Post(3L, "Prato: Filé Mignon ao Molho de Vinho Tinto\n", "Um suculento filé mignon grelhado, regado com um rico molho de vinho tinto e acompanhado de batatas gratinadas. Uma combinação clássica que agrada aos paladares mais exigentes.", "https://i.im.ge/2023/06/04/hbhczP.filezin.jpg", 42.00));
        posts.add(new Post(4L, "Prato: Lasanha à Bolonhesa", "Camadas de massa fresca intercaladas com um delicioso molho à bolonhesa, queijo derretido e temperos aromáticos. Uma lasanha clássica e reconfortante que traz o sabor da Itália para a mesa.", "https://i.im.ge/2023/06/04/hbhx4C.lasanha.webp", 22.00));
        posts.add(new Post(5L, "Prato: Salada Mediterrânea com Queijo Feta", " Uma refrescante salada composta por alface crocante, tomate cereja, pepino, azeitonas, cebola roxa e pedaços de queijo feta, regada com um molho de ervas. Uma opção leve e saudável para os dias quentes.", "https://i.im.ge/2023/06/04/hbhRjf.salada-mediterranea.jpg", 18.30));
        posts.add(new Post(6L, "Prato: Nhoque de Batata com Molho Pesto", " Nhoque de batata caseiro, servido com um irresistível molho pesto feito com manjericão fresco, pinhões, queijo parmesão e azeite de oliva. Uma explosão de sabores italianos em cada garfada.", "https://i.im.ge/2023/06/04/hbhbtp.nhoque-molho.webp", 25.90));
        posts.add(new Post(7L, "Prato: Frango à Parmegiana", " Peito de frango empanado e dourado, coberto com molho de tomate caseiro, queijo derretido e gratinado no forno. Acompanha uma porção de arroz branco e batatas fritas. Um clássico da culinária brasileira.", "https://i.im.ge/2023/06/04/hbh8O1.frango-parmediana.jpg", 29.00));
        posts.add(new Post(8L, "Prato: Tiramisu", "Uma tradicional sobremesa italiana feita com camadas de biscoitos embebidos em café, creme de mascarpone e cacau em pó. Uma combinação irresistível de sabores e texturas para encerrar a refeição com doçura.", "https://i.im.ge/2023/06/04/hbhWWm.tira.jpg", 14.00));
    }

    public String getAllPosts() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(posts);
    }

    public Bitmap carregarImagemByUrl(String urlImagem) throws IOException {
        URL url = new URL(urlImagem);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        InputStream is = con.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        byte[] content = os.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0, content.length);
        return bitmap;
    }
}
