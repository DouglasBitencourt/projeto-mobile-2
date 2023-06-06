package br.directory.menuDoChefe.DAOO;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.directory.menuDoChefe.JSONMOCK.ApiMock;
import br.directory.menuDoChefe.Entidade.Metodo;

public class MockDaoJSON {

    private ApiMock apiMock;

    public MockDaoJSON() {
        apiMock = new ApiMock();
    }

    public List<Metodo> getAllPostsFromApiMock() {
        String response = apiMock.getAllPosts();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, new TypeToken<ArrayList<Metodo>>() {}.getType());
    }

    public List<Bitmap> getAllImagesFromApiMockByPosts(List<Metodo> metodos) throws IOException {
        List<Bitmap> images = new ArrayList<>();
        for (Metodo p: metodos) {
            images.add(apiMock.carregarImagemByUrl(p.getUrlImagem()));
        }
        return images;
    }

}
