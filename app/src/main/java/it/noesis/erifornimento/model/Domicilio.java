package it.noesis.erifornimento.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Domicilio {

    private String  prov = "";
    private String  cap = "";
    private String  com = "";
    private String  ind = "";
    private String  naz = "";


    @Override
    public String toString() {
        return String.format("%s, %s %s %s %s", ind,cap, com, prov, naz);
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

    public String getNaz() {
        return naz;
    }

    public void setNaz(String naz) {
        this.naz = naz;
    }
    @JsonIgnore
    public boolean isValid() {


        if (TextUtils.isEmpty(com))
            return false;
        if (TextUtils.isEmpty(cap))
            return false;
        if (TextUtils.isEmpty(ind))
            return false;

        return true;
    }
}
