package it.uniba.dib.sms22239;

import java.util.Date;

public class Oggetto_Spesa {
    private String nome;
    private double prezzo;
    private int quantita;
    private Date dataAcquisto;

    public Oggetto_Spesa() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(Date dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    public void writeNewOggetto (Oggetto_Spesa os, String nome, double prezzo, int quantita, Date dataAcquisto)
    {
        os.nome = nome;
        os.prezzo = prezzo;
        os.quantita = quantita;
        os.dataAcquisto = dataAcquisto;



    }
}
