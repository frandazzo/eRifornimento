package it.noesis.erifornimento.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Anagrafica {

    private  String example;

    private String naz = "";
    private String cf = "";
    private String piva = "";
    private String denom = "";
    private Domicilio domFisc = new Domicilio();
    public Domicilio getDomFisc() {
        return domFisc;
    }

    public void setDomFisc(Domicilio domFisc) {
        this.domFisc = domFisc;
    }



    public String getNaz() {
        return naz;
    }

    public void setNaz(String naz) {
        this.naz = naz;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getDenom() {
        return denom;
    }

    public void setDenom(String denom) {
        this.denom = denom;
    }

    @JsonIgnore
    public boolean isValid() {

        if (TextUtils.isEmpty(denom))
            return false;

        if (TextUtils.isEmpty(piva))
            return false;

        if (piva.length()!= 11)
            return false;

        if (domFisc == null)
            return false;


        return domFisc.isValid();



    }
}
