package it.uniba.dib.sms22239.Models;

import java.time.LocalDateTime;

public class Appuntamento {
    private int id_veterinario;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String data;
    private int id_appuntamento;

    public Appuntamento(int id_veterinario, LocalDateTime inizio, LocalDateTime fine, String data, int id_appuntamento) {
        this.id_veterinario = id_veterinario;
        this.inizio = inizio;
        this.fine = fine;
        this.data = data;
        this.id_appuntamento = id_appuntamento;
    }


    }

