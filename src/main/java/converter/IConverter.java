package converter;


import org.w3c.dom.stylesheets.LinkStyle;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface IConverter {

    public double euroToOtherCurrency(double amount, String currencyCode);
    public List<String> getAvailableCurrencies();
    public Map<Monnaie,Double> euroToOtherCurrencies(double amount);
}
