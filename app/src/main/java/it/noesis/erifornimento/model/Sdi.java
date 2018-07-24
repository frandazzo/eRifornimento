package it.noesis.erifornimento.model;

import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Sdi {

    private String pec;
    private String cod;

    public String getPec() {
        return pec;
    }

    public void setPec(String pec) {
        this.pec = pec;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
    @JsonIgnore
    public boolean isValid() {


        if (TextUtils.isEmpty(pec) && TextUtils.isEmpty(cod))
            return false;

        return true;
    }
}
