package it.noesis.erifornimento.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Cliente {

    private Anagrafica anag;
    private Sdi sdi;
    private Date dtGenQr;


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
}
