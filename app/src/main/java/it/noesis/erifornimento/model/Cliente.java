package it.noesis.erifornimento.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Cliente {

    private Anagrafica anag = new Anagrafica();
    private Sdi sdi = new Sdi();
    private Date dtGenQr;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String targa;

    public String getTarga() {
        return targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public Cliente(){

    }

    @JsonCreator
    public Cliente(
            @JsonProperty("anag") Anagrafica anag,
            @JsonProperty("SDI") Sdi sdi,
            @JsonProperty("dtGenQr") Date dtGenQr) {
        this.anag = anag;
        this.sdi = sdi;
        this.dtGenQr = dtGenQr;
    }

    public Anagrafica getAnag() {
        return anag;
    }

    public void setAnag(Anagrafica anag) {
        this.anag = anag;
    }

    public it.noesis.erifornimento.model.Sdi getSdi() {
        return sdi;
    }

    public void setSdi(it.noesis.erifornimento.model.Sdi Sdi) {
        this.sdi = Sdi;
    }

    public Date getDtGenQr() {
        return dtGenQr;
    }

    public void setDtGenQr(Date dtGenQr) {
        this.dtGenQr = dtGenQr;
    }

    @JsonIgnore
    public boolean isValid() {

        if (anag == null)
            return false;

        if (!anag.isValid())
            return false;

        if (sdi == null)
            return false;

        return sdi.isValid();


    }
}
