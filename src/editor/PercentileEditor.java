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
import javax.swing.JTextPane;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import javax.swing.undo.UndoManager;

import database.ConnectionClass;
import database.UpdateMeatPercentile;

public class PercentileEditor {
	private JFrame frame__;
	private JTextPane editor__;
	private String updatedpercentile;
	private String pcode;

	enum BulletActionType {
		INSERT, REMOVE
	};

	enum NumbersActionType {
		INSERT, REMOVE
	};

	enum UndoActionType {
		UNDO, REDO
	};

	public String getUpdatedpercentile() {
		return updatedpercentile;
	}

	public JButton cutButton = null;
	public JButton copyButton = null;
	public JButton pasteButton = null;

	// editor class to enable the modification on text including extra features
	public JFrame createAndShowGUI(String pcode, String text, String desc) {
		this.pcode = pcode;

		updatedpercentile = text;
		frame__ = new JFrame();

		String cattitle = "";

		cattitle = "Meat Percentile Update";

		setFrameTitleWithExtn(cattitle + " - " + pcode);

		EditButtonActionListener editButtonActionListener = new EditButtonActionListener();

		editor__ = new JTextPane();
		JScrollPane editorScrollPane = new JScrollPane(editor__);

		editor__.setDocument(getNewDocument());
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

		JPanel toolBarPanel = new JPanel();
		toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.PAGE_AXIS));
		toolBarPanel.add(panel1);

		JButton updatebtn = new JButton("SI Update");
		updatebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String html = editor__.getText();

				ConnectionClass conn = new ConnectionClass();
				UpdateMeatPercentile updatestatement = new UpdateMeatPercentile(conn.getConn());
				updatestatement.UpdateMeatPercentileToDatabase(pcode, html, desc);

				JOptionPane.showMessageDialog(null, "Meat Percentile Has Been Updated Successfully.");
			}
		});

		JPanel sidepanel = new JPanel(new BorderLayout());
		sidepanel.add(updatebtn, BorderLayout.EAST);

		JPanel topbar = new JPanel(new BorderLayout());
		topbar.add(toolBarPanel, BorderLayout.CENTER);
		topbar.add(sidepanel, BorderLayout.EAST);

		frame__.add(topbar, BorderLayout.NORTH);
		frame__.add(editorScrollPane, BorderLayout.CENTER);
		frame__.setSize(450, 300);
		frame__.setLocation(150, 80);
		frame__.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame__.setVisible(true);

		editor__.requestFocusInWindow();

		return frame__;
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

	public String getPcode() {
		return pcode;
	}

	public JFrame getFrame__() {
		return frame__;
	}

}