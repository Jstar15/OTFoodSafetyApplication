package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import XML.XMLReader;
import XML.XMLWriter;
import editor.SettingsGroupEditor;

public class SettingsMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField allergensbox;
	private JTextField nutritionbox;
	private JTextField databasebox;
	private JTextField packagebox;

	public SettingsMenu() {
		setLayout(new BorderLayout());

		JLabel databasel = new JLabel("Database Connection String:  ", SwingConstants.RIGHT);
		JLabel allergensdatabase = new JLabel(" Allergens Database:  ", SwingConstants.RIGHT);
		JLabel nutritiondatabase = new JLabel(" Nutrition Database:  ", SwingConstants.RIGHT);
		JLabel packagedatabase = new JLabel(" Packaging Database:  ", SwingConstants.RIGHT);

		JPanel labelcontainer = new JPanel();
		labelcontainer.setLayout(new GridLayout(5, 1));
		labelcontainer.add(new JPanel());
		labelcontainer.add(databasel);
		labelcontainer.add(allergensdatabase);
		labelcontainer.add(nutritiondatabase);
		labelcontainer.add(packagedatabase);

		databasebox = TextFieldPopup();

		allergensbox = new JTextField();
		allergensbox.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				allergensbox.setText(Chooser(allergensbox.getText()));
				allergensbox.setFocusable(false);
				allergensbox.setFocusable(true);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});

		nutritionbox = new JTextField();
		nutritionbox.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				nutritionbox.setText(Chooser(nutritionbox.getText()));
				nutritionbox.setFocusable(false);
				nutritionbox.setFocusable(true);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});

		packagebox = new JTextField();
		packagebox.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				packagebox.setText(Chooser(packagebox.getText()));
				packagebox.setFocusable(false);
				packagebox.setFocusable(true);
			}

			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		JPanel boxcontainer = new JPanel();
		boxcontainer.setLayout(new GridLayout(5, 1));
		boxcontainer.add(new JPanel());

		boxcontainer.add(databasebox);
		boxcontainer.add(allergensbox);
		boxcontainer.add(nutritionbox);
		boxcontainer.add(packagebox);

		JLabel title = new JLabel("Settings Menu");
		title.setForeground(Color.WHITE);

		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.DARK_GRAY);
		titlepanel.setForeground(Color.WHITE);

		titlepanel.add(title, BorderLayout.CENTER);

		JButton savebtn = new JButton("Save");
		savebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WriteSettings();
				JOptionPane.showMessageDialog(null, "XML File Has Been Updated Successfully.");
			}
		});

		JPanel savepanel = new JPanel();
		savepanel.setLayout(new GridLayout(2, 1));
		savepanel.add(new JPanel());
		savepanel.add(savebtn);

		JPanel maincontainer = new JPanel();
		maincontainer.setLayout(new GridLayout(1, 3));
		maincontainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		maincontainer.add(labelcontainer);
		maincontainer.add(boxcontainer);
		maincontainer.add(savepanel);

		JPanel maincontainer2 = new JPanel();
		maincontainer2.setLayout(new BorderLayout());
		maincontainer2.add(maincontainer, BorderLayout.NORTH);

		JButton a = new JButton("Allergen B-list");
		a.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Allergen Blacklist Manager", "Allergen Blacklist", 1);
			}
		});
		JButton b = new JButton("Coliac B-list");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Colicas Blacklist Manager", "Colicas Blacklist", 2);
			}
		});
		JButton c = new JButton("Vegeterian B-list");
		c.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Vegeterian Blacklist Manager",
						"Vegeterian Blacklist Specification", 3);
			}
		});
		JButton d = new JButton("Vegan B-list");
		d.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Vegan Blacklist Manager", "Vegan Blacklist", 4);
			}
		});

		JButton e = new JButton("GMO B-list");
		e.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Gmo Blacklist Manager", "Gmo Blacklist", 5);
			}
		});

		JButton f = new JButton("Msg B-list");
		f.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SettingsGroupEditor().createAndShowGUI("Msg Blacklist Manager", "Msg Blacklist", 7);
			}
		});

		JPanel settingsmenucontainer = new JPanel();
		settingsmenucontainer.setLayout(new GridLayout(1, 6));
		settingsmenucontainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		settingsmenucontainer.add(a);
		settingsmenucontainer.add(b);
		settingsmenucontainer.add(c);
		settingsmenucontainer.add(d);
		settingsmenucontainer.add(e);
		settingsmenucontainer.add(f);

		add(titlepanel, BorderLayout.NORTH);
		add(maincontainer2, BorderLayout.CENTER);
		add(settingsmenucontainer, BorderLayout.SOUTH);
		// get current settings and display
		ReadSettings();

	}

	private String Chooser(String currentdir) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile().toString();
		} else {
			return currentdir;
		}
	}

	private void ReadSettings() {
		XMLReader x = new XMLReader();
		databasebox.setText(x.getLocation());
		allergensbox.setText(x.getAller());
		nutritionbox.setText(x.getNutri());
		packagebox.setText(x.getPackspec());
	}

	private void WriteSettings() {
		try {
			new XMLWriter(databasebox.getText(), nutritionbox.getText(), allergensbox.getText(), packagebox.getText());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JTextField TextFieldPopup() {
		JTextField textField = new JTextField();

		JPopupMenu menu = new JPopupMenu();
		Action cut = new DefaultEditorKit.CutAction();
		cut.putValue(Action.NAME, "Cut");
		cut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control X"));
		menu.add(cut);

		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		copy.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));
		menu.add(copy);

		Action paste = new DefaultEditorKit.PasteAction();
		paste.putValue(Action.NAME, "Paste");
		paste.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
		menu.add(paste);

		textField.setComponentPopupMenu(menu);

		return textField;
	}

}
