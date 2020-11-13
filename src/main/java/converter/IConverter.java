package converter;

import javax.ejb.EJB;
import java.net.MalformedURLException;

@EJB
public interface IConverter {

    public double euroToOtherCurrency(double amount, String currencyCode);

}
