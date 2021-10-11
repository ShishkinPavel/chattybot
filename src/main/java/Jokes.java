import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Random;

public class Jokes {
    private final static Random random = new Random();
    private final static String path = "src/main/resources/jokes.xml";
    private static Document doc;

    public Jokes() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = docBuilder.parse(path);
    }

    public String getJoke() {
        int count = doc.getElementsByTagName("joke").getLength();
        return doc.getElementsByTagName("joke").item(random.nextInt(count)).getTextContent();
    }
}
