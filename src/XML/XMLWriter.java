package XML;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLWriter {

	public XMLWriter(String location, String nutri, String aller, String packspec)
			throws ParserConfigurationException, SAXException, IOException {

		try {
			File stocks = new File("devices.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("node");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					// create tmp list and generate table object
					setValue("location", element, location);
					setValue("nutrilocation", element, nutri);
					setValue("allergenslocation", element, aller);
					setValue("packspeclocation", element, packspec);
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("devices.xml"));
			transformer.transform(source, result);

			JOptionPane.showMessageDialog(null, "File Saved Successfully");

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error Saving File!");
		}
	}

	public XMLWriter(String data, int mode) throws ParserConfigurationException, SAXException, IOException {
		System.out.println(data);
		try {
			File stocks = new File("devices.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(stocks);
			doc.getDocumentElement().normalize();

			NodeList nodes = doc.getElementsByTagName("node");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					// create tmp list and generate table object
					if (mode == 1) {
						setValue("allergenblacklist", element,
								data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 2) {
						setValue("colicacsblacklist", element,
								data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 3) {
						setValue("vegeterianblacklist", element,
								data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 4) {
						setValue("veganblacklist", element,
								data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 5) {
						setValue("gmoblacklist", element, data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 6) {
						setValue("packagingspecs", element,
								data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					} else if (mode == 7) {
						setValue("msgblacklist", element, data.toLowerCase().replaceAll("[^a-zA-Z0-9 |@]", "").trim());
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("devices.xml"));
			transformer.transform(source, result);

			JOptionPane.showMessageDialog(null, "File Saved Successfully");

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error Saving File!");
		}
	}

	private void setValue(String tag, Element element, String value) {
		try {
			NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
			Node node = (Node) nodes.item(0);
			node.setTextContent(value);
		} catch (NullPointerException e) {

		}

	}

}
