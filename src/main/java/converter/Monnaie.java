package converter;

import java.io.Serializable;
import java.util.List;

public class Monnaie implements Serializable {

    public static final String COUNTRY_NAME = "CtryNm";
    public static final String CURRENCY_COMPLETE_NAME = "CcyNm";
    public static final String CURRENCY = "Ccy";
    public static final String CURRENCY_CODE = "CcyNbr";

    private List<String> nomsPays;
    private String nomCompletMonnaie;
    private String codeMonnaie;
    private double tauxDeChange;

    public List<String> getNomsPays() {
        return nomsPays;
    }

    public void setNomsPays(List<String> nomsPays) {
        this.nomsPays = nomsPays;
    }

    public String getNomCompletMonnaie() {
        return nomCompletMonnaie;
    }

    public void setNomCompletMonnaie(String nomCompletMonnaie) {
        this.nomCompletMonnaie = nomCompletMonnaie;
    }

    public String getCodeMonnaie() {
        return codeMonnaie;
    }

    public void setCodeMonnaie(String codeMonnaie) {
        this.codeMonnaie = codeMonnaie;
    }

    public double getTauxDeChange() {
        return tauxDeChange;
    }

    public void setTauxDeChange(double tauxDeChange) {
        this.tauxDeChange = tauxDeChange;
    }
}
