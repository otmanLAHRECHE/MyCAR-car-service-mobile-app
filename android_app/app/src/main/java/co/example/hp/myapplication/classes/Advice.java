package co.example.hp.myapplication.classes;

import java.io.Serializable;
import java.net.URL;

public class Advice implements Serializable {
    private String id;
    private String title;
    private String content;
    private URL imageUrl;

    public Advice(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
    }
}
