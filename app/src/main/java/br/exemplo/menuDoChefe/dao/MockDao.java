package br.exemplo.menuDoChefe.dao;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.exemplo.menuDoChefe.mockApi.ApiMock;
import br.exemplo.menuDoChefe.entity.Post;

public class MockDao {

    private ApiMock apiMock;

    public MockDao() {
        apiMock = new ApiMock();
    }

    public List<Post> getAllPostsFromApiMock() {
        String response = apiMock.getAllPosts();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, new TypeToken<ArrayList<Post>>() {}.getType());
    }

    public List<Bitmap> getAllImagesFromApiMockByPosts(List<Post> posts) throws IOException {
        List<Bitmap> images = new ArrayList<>();
        for (Post p: posts) {
            images.add(apiMock.carregarImagemByUrl(p.getUrlImagem()));
        }
        return images;
    }

}
