package it.noesis.erifornimento.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Fattura {

    private String targa;
    private Double benzina;
    private Double diesel;
    private Double gpl;
    private Double metano;
    private Cliente cliente;
    //0 = contanti, 1 cartas credito
    private Integer tipoPagamento = 0;

    public Integer getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(Integer tipoPagamento) {
        if (tipoPagamento != 0 && tipoPagamento != 1)
            tipoPagamento = 0;
        this.tipoPagamento = tipoPagamento;
    }

    public Fattura(){
        benzina = 0d;
        diesel = 0d;
        gpl = 0d;
        metano = 0d;
        targa = "";
    }

    private  double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public double calculateTotal(){
        return round(benzina + diesel + metano + gpl, 2);
    }

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public Double getBenzina() {
        return benzina;
    }

    public void setBenzina(Double benzina) {
        this.benzina = benzina;
    }

    public Double getDiesel() {
        return diesel;
    }

    public void setDiesel(Double diesel) {
        this.diesel = diesel;
    }

    public Double getGpl() {
        return gpl;
    }

    public void setGpl(Double gpl) {
        this.gpl = gpl;
    }

    public Double getMetano() {
        return metano;
    }

    public void setMetano(Double metano) {
        this.metano = metano;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
    @JsonIgnore
    public boolean isValid() {

        if (TextUtils.isEmpty(targa))
            return false;

        if (benzina <= 0d && metano <= 0d && gpl <= 0d && diesel <=0d)
            return false;


        if (cliente == null)
            return false;

        return cliente.isValid();


    }
}
