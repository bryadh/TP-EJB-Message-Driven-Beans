package converter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Stateful(name = "ConverterEJB")
@Remote
public class ConverterBean implements IConverter {

    public Map<String,Double> cubeList = new HashMap<String, Double>();

    public ConverterBean() {
    }

    @Override
    public double euroToOtherCurrency(double amount, String currencyCode) {

        Double ret = 0.0;

        SAXBuilder sxb = new SAXBuilder();
        try {
            URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            Document document = sxb.build(con.getInputStream());

            Element racine = document.getRootElement();

            Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002−08−01/eurofxref");

            Element elem = racine.getChildren().get(2);

            Element cube = elem.getChildren().get(0);

            List<Element> elements = cube.getChildren();

            for(Element e : elements ){

                if(e.getAttributeValue("currency").equals(currencyCode)){
                    ret = amount * Double.parseDouble(e.getAttributeValue("rate"));
                }

                // Remplissage du hashmap
                cubeList.put(e.getAttributeValue("currency"),Double.parseDouble(e.getAttributeValue("rate")));

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public List<String> getAvailableCurrencies(){

        SAXBuilder sxb = new SAXBuilder();
        try {
            URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            Document document = sxb.build(con.getInputStream());

            Element racine = document.getRootElement();

            Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002−08−01/eurofxref");

            Element elem = racine.getChildren().get(2);

            Element cube = elem.getChildren().get(0);

            List<Element> elements = cube.getChildren();

            for(Element e : elements ){

                // Remplissage du hashmap
                cubeList.put(e.getAttributeValue("currency"),Double.parseDouble(e.getAttributeValue("rate")));

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        List<String> returnList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : cubeList.entrySet()){
            returnList.add(entry.getKey());
        }

        return returnList;
    }

    public Map<Monnaie,Double> euroToOtherCurrencies(double amount){

        Map<Monnaie, Double> returnMap = new HashMap<Monnaie, Double>();
        List<String> currencies = getAvailableCurrencies();

        SAXBuilder sxb = new SAXBuilder();
        try {
            URL url = new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            Document document = sxb.build(con.getInputStream());

            Element racine = document.getRootElement();

            Namespace ns = Namespace.getNamespace("http://www.ecb.int/vocabulary/2002−08−01/eurofxref");

            Element elem = racine.getChildren().get(0);

            List<Element> elements = elem.getChildren();

            List<String> checkedCurrencies = new ArrayList<>();

            for(Element e : elements ){

                String currency = e.getChild(Monnaie.CURRENCY).getValue();

                // Verifier que la currency est bien dans la liste des currencies
                if(getAvailableCurrencies().contains(currency)){

                    // Verifier si l'on a déja vu la currency
                    if(!checkedCurrencies.contains(currency)){
                        // Ajouter a checkedCurrencies
                        checkedCurrencies.add(currency);

                        // Initialiser la liste des pays
                        List<String> countriesList = new ArrayList<>();

                        // Instancier un objet Monnaie
                        Monnaie monnaie = new Monnaie();

                        // Faire un autre parcour pour remplir la liste de countries
                        for(Element f : elements){
                            if(f.getChild(Monnaie.CURRENCY).getValue().equals(currency)){
                                countriesList.add(f.getChild(Monnaie.COUNTRY_NAME).getValue());
                            }
                        }

                        String completCurrency = e.getChild(Monnaie.CURRENCY_COMPLETE_NAME).getValue();

                        String code = e.getChild(Monnaie.CURRENCY_CODE).getValue();

                        Double rate = 0.0;

                        // Chercher le taux de change dans le Map
                        for(Map.Entry<String, Double> entry : cubeList.entrySet()){
                            if(entry.getKey().equals(currency)){
                                rate = entry.getValue();
                            }
                        }

                        // Utiliser les setter de l'objet Monnaie
                        monnaie.setCodeMonnaie(code);
                        monnaie.setNomCompletMonnaie(completCurrency);
                        monnaie.setNomsPays(countriesList);
                        monnaie.setTauxDeChange(rate);

                        // Faire la conversion
                        Double returnAmount = rate * amount;

                        // Mettre le resultat de la conversion et la monnaie dans le map
                        returnMap.put(monnaie, returnAmount);
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

        return returnMap;
    }
}
