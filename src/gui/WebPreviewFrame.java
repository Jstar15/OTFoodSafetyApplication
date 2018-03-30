package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.xml.sax.SAXException;

import com.google.common.io.Files;

public class WebPreviewFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	// displays.doc fiel as html
	public WebPreviewFrame(File filein) {
		super("Food Safety - OT SPEC - Preview");

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		setContentPane(container);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1200, 840);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));

		String page = null;
		try {
			page = parseToHTML(filein);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (TikaException e) {
			e.printStackTrace();
		}

		// quick fix for an image missing//only used for preview
		page = page.replace("embedded:image1.jpg",
				"http://www.owentaylor.co.uk/masterpages/OwenTaylor/images/logo.jpg");
		page = page.replace("embedded:image2.jpg",
				"http://www.owentaylor.co.uk/masterpages/OwenTaylor/images/logo.jpg");

		JEditorPane jep = new JEditorPane();
		jep.setEditable(false);
		jep.setContentType("text/html");
		jep.setText(page);

		JScrollPane editorScrollPane = new JScrollPane(jep);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		container.add(editorScrollPane);
	}

	// converts .doc file to html
	public static String parseToHTML(File filein)
			throws IOException, SAXException, TikaException, TransformerConfigurationException {
		byte[] file = Files.toByteArray(filein);
		AutoDetectParser tikaParser = new AutoDetectParser();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
		TransformerHandler handler = factory.newTransformerHandler();
		handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
		handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
		handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		handler.setResult(new StreamResult(out));
		ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);

		tikaParser.parse(new ByteArrayInputStream(file), handler1, new Metadata());
		return new String(out.toByteArray(), "UTF-8");
	}

}