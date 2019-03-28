
package com.grsu.guideapp.network.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Language implements Serializable
{

    @SerializedName("ru")
    @Expose
    private String ru;
    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("cn")
    @Expose
    private String cn;
    @SerializedName("pl")
    @Expose
    private String pl;
    @SerializedName("lt")
    @Expose
    private String lt;
    private final static long serialVersionUID = 2483469886703002654L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Language() {
    }

    /**
     * 
     * @param cn
     * @param pl
     * @param en
     * @param lt
     * @param ru
     */
    public Language(String ru, String en, String cn, String pl, String lt) {
        super();
        this.ru = ru;
        this.en = en;
        this.cn = cn;
        this.pl = pl;
        this.lt = lt;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

}
