import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;

public class Currency {

    private static Document doc;
    private static double currency;
    public static HashMap<String, String> hashMap;
    private static final String WEBLINK = "https://www.cbr-xml-daily.ru/daily_eng_utf8.xml";
    private static int count;

    public Currency() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = docBuilder.parse(new URL(WEBLINK).openStream());
        setHashMap();
    }

    public void setHashMap() {
        hashMap = new HashMap<>();
        count = doc.getElementsByTagName("CharCode").getLength();
        for (int i = 0; i < count; i++) {
            hashMap.put(doc.getElementsByTagName("Name").item(i).getTextContent().toLowerCase(Locale.ROOT),
                    doc.getElementsByTagName("CharCode").item(i).getTextContent());
        }
    }


    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setCurrency(String name) throws ParseException {
        currency = findCurrency(name);
    }

    private double findCurrency(String name) throws ParseException {
        double result = 0;
        for (int i = 0; i < count; i++) {
            if (doc.getElementsByTagName("CharCode").item(i).getTextContent().
                    equals(name)){
                result = NumberFormat.getNumberInstance(Locale.FRANCE).
                        parse(doc.getElementsByTagName("Value").item(i).getTextContent()).doubleValue() /
                        NumberFormat.getNumberInstance(Locale.FRANCE).
                        parse(doc.getElementsByTagName("Nominal").item(i).getTextContent()).doubleValue();
            }
        }
        return result;
    }

    public double getCurrency() {
        return currency;
    }

}
