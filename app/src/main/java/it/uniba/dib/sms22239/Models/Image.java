package it.uniba.dib.sms22239.Models;

public class Image {

    private String filename;
    private String nome;

    public Image() {}

    public Image(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return filename;
    }

    public void setUrl(String filename) {
        this.filename = filename;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
