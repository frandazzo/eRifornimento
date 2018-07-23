package it.noesis.erifornimento.model;

public class Fattura {

    private String targa;
    private Double benzina;
    private Double diesel;
    private Double gpl;
    private Double metano;
    private Cliente cliente;

    public Fattura(){
        benzina = 0d;
        diesel = 0d;
        gpl = 0d;
        metano = 0d;
        targa = "";
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
}
