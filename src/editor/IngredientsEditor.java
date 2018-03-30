package editor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import javax.swing.undo.UndoManager;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import XML.XMLReader;
import database.ConnectionClass;
import database.UpdateIngredients;

public class IngredientsEditor {
	private JFrame frame__;
	private JTextPane editor__;
	private String updatedingredients;
	private String catid;
	private String phaseid;

	enum BulletActionType {
		INSERT, REMOVE
	};

	enum NumbersActionType {
		INSERT, REMOVE
	};

	enum UndoActionType {
		UNDO, REDO
	};

	public String getUpdatedingredients() {
		return updatedingredients;
	}

	public JButton cutButton = null;
	public JButton copyButton = null;
	public JButton pasteButton = null;

	// editor class to enable the modification on text including extra features
	public JFrame createAndShowGUI(String catid, String phaseid, String text) {
		this.catid = catid;
		this.phaseid = phaseid;

		updatedingredients = text;
		frame__ = new JFrame();

		String cattitle = "";

		if (catid.equals("1020")) {
			cattitle = "Bakehouse - ";
		}
		if (catid.equals("1021")) {
			cattitle = "Manufacturing - ";
		}

		setFrameTitleWithExtn(cattitle + catid + " - " + phaseid);

		EditButtonActionListener editButtonActionListener = new EditButtonActionListener();

		editor__ = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor__);

		editor__.setDocument(getNewDocument());
		editor__.setContentType("text/html");
		editor__.setText(text);

		UndoManager manager = new UndoManager();
		editor__.getDocument().addUndoableEditListener(manager);

		editor__.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					final JPopupMenu menu = new JPopupMenu();
					JMenuItem item;
					item = new JMenuItem(new DefaultEditorKit.CopyAction());
					item.setText("Copy");
					item.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							copyButton.doClick();
						}
					});
					JMenuItem item2 = new JMenuItem();
					item2.setText("Cut");
					item2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							cutButton.doClick();
						}
					});

					JMenuItem item3 = new JMenuItem();
					item3.setText("Paste");
					item3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							pasteButton.doClick();
						}
					});

					menu.add(item);
					menu.add(item2);
					menu.add(item3);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		cutButton = new JButton(new CutAction());
		cutButton.setHideActionText(true);
		cutButton.setText("Cut");
		cutButton.addActionListener(editButtonActionListener);
		cutButton.setVisible(false);

		copyButton = new JButton(new CopyAction());
		copyButton.setHideActionText(true);
		copyButton.setText("Copy");
		copyButton.addActionListener(editButtonActionListener);
		copyButton.setVisible(false);

		pasteButton = new JButton(new PasteAction());
		pasteButton.setHideActionText(true);
		pasteButton.setText("Paste");
		pasteButton.addActionListener(editButtonActionListener);
		pasteButton.setVisible(false);

		JButton boldButton = new JButton(new BoldAction());
		boldButton.setHideActionText(true);
		boldButton.addActionListener(editButtonActionListener);
		boldButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resourceseditor/bold.png")));
		boldButton.setSize(50, 50);

		JButton italicButton = new JButton(new ItalicAction());
		italicButton.setHideActionText(true);
		italicButton.addActionListener(editButtonActionListener);
		italicButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resourceseditor/italic.png")));
		italicButton.setSize(50, 50);

		JButton underlineButton = new JButton(new UnderlineAction());
		underlineButton.setHideActionText(true);
		underlineButton.addActionListener(editButtonActionListener);
		underlineButton
				.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resourceseditor/underlineicon.png")));
		underlineButton.setSize(50, 50);

		JButton undoButton = new JButton(UndoManagerHelper.getUndoAction(manager));
		undoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resourceseditor/undo.png")));
		undoButton.setSize(50, 50);
		undoButton.setText("");

		JButton redoButton = new JButton(UndoManagerHelper.getRedoAction(manager));
		redoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resourceseditor/redo.png")));
		redoButton.setSize(50, 50);
		redoButton.setText("");

		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.add(cutButton);
		panel1.add(copyButton);
		panel1.add(pasteButton);

		panel1.add(undoButton);
		panel1.add(redoButton);

		panel1.add(new JSeparator(SwingConstants.VERTICAL));
		panel1.add(boldButton);
		panel1.add(italicButton);
		panel1.add(underlineButton);

		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
		toolBarPanel.add(panel1);

		JButton updatebtn = new JButton("Si Update");
		updatebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String html = editor__.getText();
				html = CleanHtml(html);
				String rtf = HtmlToRTF(html);

				ConnectionClass conn = new ConnectionClass();
				UpdateIngredients updatestatement = new UpdateIngredients(conn.getConn());
				updatestatement.UpdateIngredientsToDatabase(catid, phaseid, html, rtf);
				updatedingredients = html;
				JOptionPane.showMessageDialog(null, "Ingredients Has Been Updated Successfully.");
			}
		});

		JButton allerbtn = new JButton("Auto Highlight");
		allerbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// on click highlight-underline and capitalise all allergens
				// then update editor text
				String html = editor__.getText();

				html = html.replaceAll("<u>", "");
				html = html.replaceAll("<b>", "");
				html = html.replaceAll("<i>", "");

				html = html.replaceAll("<U>", "");
				html = html.replaceAll("<B>", "");
				html = html.replaceAll("<I>", "");

				html = html.replaceAll("</u>", "");
				html = html.replaceAll("</b>", "");
				html = html.replaceAll("</i>", "");

				html = html.replaceAll("</U>", "");
				html = html.replaceAll("</B>", "");
				html = html.replaceAll("</I>", "");

				html = html.replaceAll("&amp;", "");
				html = html.replaceAll("&amp;", "");

				for (String[] aller2 : ReadAllergenBlacklist()) {
					html = BoldAmdUnderLineHtml3(html, aller2[0]);

				}
				html = html.replaceAll("</U></U>", "</U>");
				html = html.replaceAll("</B></B>", "</B>");
				html = html.replaceAll("</I></I>", "</I>");

				html = html.replaceAll("<U><U>", "<U>");
				html = html.replaceAll("<B><B>", "<B>");
				html = html.replaceAll("<I><I>", "<I>");

				editor__.setContentType("text/html");
				editor__.setText(html);
			}
		});

		JPanel sidepanel = new JPanel(new BorderLayout());
		sidepanel.add(allerbtn, BorderLayout.WEST);
		sidepanel.add(updatebtn, BorderLayout.EAST);

		JPanel topbar = new JPanel(new BorderLayout());
		topbar.add(toolBarPanel, BorderLayout.CENTER);
		topbar.add(sidepanel, BorderLayout.EAST);

		frame__.add(topbar, BorderLayout.NORTH);
		frame__.add(editorScrollPane, BorderLayout.CENTER);
		frame__.setSize(900, 500);
		frame__.setLocation(150, 80);
		frame__.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame__.setVisible(true);

		editor__.requestFocusInWindow();

		return frame__;
	}

	// get allergens blacklist form xml file
	private String[][] ReadAllergenBlacklist() {
		XMLReader x = new XMLReader();
		String[][] settings = null;
		settings = x.getAllergenblacklistarray();
		return settings;
	}

	// replace all blacklisted words with formatted html tags
	private String BoldAmdUnderLineHtml3(String html, String target) {
		if (target.trim().length() > 0) {
			html = html.replaceAll(target.toLowerCase(), "<U><B>" + target.toUpperCase() + "</U></B>");
			html = html.replaceAll(target.toUpperCase(), "<U><B>" + target.toUpperCase() + "</U></B>");
			String output = target.substring(0, 1).toUpperCase() + target.substring(1).toLowerCase();
			html = html.replaceAll(output, "<U><B>" + target.toUpperCase() + "</U></B>");

		}
		return html;

	}

	// format html for saving output
	private String CleanHtml(String html) {

		html = html.replaceAll("</strong>", "");
		html = html.replaceAll("<strong>", "");

		Whitelist wl = Whitelist.simpleText();
		wl.addTags("U", "B", "I"); // add additional tags here as necessary
		html = Jsoup.clean(html, wl);

		html = html.replaceAll("&nbsp", "");
		html = html.replaceAll("<u>", "<U>");
		html = html.replaceAll("<b>", "<B>");
		html = html.replaceAll("<i>", "<I>");

		html = html.replaceAll("</u>", "</U>");
		html = html.replaceAll("</b>", "</B>");
		html = html.replaceAll("</i>", "</I>");
		html = html.trim();

		return html;
	}

	// convert html tags to rtf tags for saving output
	private String HtmlToRTF(String html) {
		html = html.replaceAll("<U>", "<span style=\"text-decoration:underline;\">");
		html = html.replaceAll("<B>", "<span style=\"font-weight:bold;\">");
		html = html.replaceAll("<I>", "<span style=\"text-weight:italic;\">");

		html = html.replaceAll("</U>", "</span>");
		html = html.replaceAll("</B>", "</span>");
		html = html.replaceAll("</I>", "</span>");

		return html;
	}

	private void setFrameTitleWithExtn(String titleExtn) {
		frame__.setTitle(titleExtn);
	}

	private StyledDocument getNewDocument() {

		StyledDocument doc = new DefaultStyledDocument();
		return doc;
	}

	private class EditButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			editor__.requestFocusInWindow();
		}
	}

	public String getCatid() {
		return catid;
	}

	public String getPhaseid() {
		return phaseid;
	}

	public JFrame getFrame__() {
		return frame__;
	}

}