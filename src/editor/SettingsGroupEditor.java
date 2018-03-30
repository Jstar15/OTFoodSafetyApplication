package editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import XML.XMLReader;
import XML.XMLWriter;

public class SettingsGroupEditor {
	private JFrame frame__;

	enum BulletActionType {
		INSERT, REMOVE
	};

	enum NumbersActionType {
		INSERT, REMOVE
	};

	enum UndoActionType {
		UNDO, REDO
	};

	public JButton cutButton = null;
	public JButton copyButton = null;
	public JButton pasteButton = null;
	private Object[][] o;
	private JTable table = null;
	private JPanel listpanel;
	private String header = "";
	private int mode = 0;

	public JFrame createAndShowGUI(String title, String header, int mode) {

		this.mode = mode;
		this.header = header;

		frame__ = new JFrame();
		setFrameTitleWithExtn(title);

		JButton updatebtn = new JButton("Update " + header);
		updatebtn.setFocusable(false);
		updatebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rowcount = table.getRowCount();

				StringBuilder sb = new StringBuilder();
				for (int x = 0; x < rowcount; x++) {
					if (table.getValueAt(x, 0).toString().replaceAll("[^a-zA-Z0-9 |@]", "").trim().length() > 1) {
						sb.append(table.getValueAt(x, 0) + "@@split");
					}
				}

				WriteSettings(sb.toString(), mode);
			}
		});

		JPanel toppanel = new JPanel(new BorderLayout());
		toppanel.add(updatebtn, BorderLayout.EAST);

		listpanel = SearchTablePanel(ReadSettingsList());

		frame__.add(toppanel, BorderLayout.NORTH);
		frame__.add(listpanel, BorderLayout.CENTER);
		frame__.setSize(500, 500);
		frame__.setLocation(150, 80);
		frame__.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame__.setVisible(true);

		return frame__;
	}

	private String[][] ReadSettingsList() {
		XMLReader x = new XMLReader();
		String[][] settings = null;
		if (mode == 1) {
			settings = x.getAllergenblacklistarray();
		} else if (mode == 2) {
			settings = x.getColicacsblacklistarray();
		} else if (mode == 3) {
			settings = x.getVegeterianblacklistarray();
		} else if (mode == 4) {
			settings = x.getVeganblacklistarray();
		} else if (mode == 5) {
			settings = x.getGmoBlacklistarray();
		} else if (mode == 6) {
			settings = x.getPackspecarray();
		} else if (mode == 7) {
			settings = x.getMsgblacklistarray();
		}

		return settings;
	}

	private void WriteSettings(String data, int mode) {
		try {
			new XMLWriter(data, mode);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setFrameTitleWithExtn(String titleExtn) {
		frame__.setTitle(titleExtn);
	}

	private JPanel SearchTablePanel(String[][] tablelist) {

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table

		DefaultTableModel dm = new DefaultTableModel();

		o = tablelist;

		dm.setDataVector(o, new Object[] { header });

		table = new JTable(dm) {
			private static final long serialVersionUID = 1L;

			public Class<? extends Object> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return true;
			}
		};

		table.setRowHeight(30);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					final JPopupMenu menu = new JPopupMenu();

					// add empty item to table
					JMenuItem item = new JMenuItem();
					item.setText("Add Item");
					item.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							Thread t = new Thread() {
								public void run() {
									ArrayList<ArrayList<String>> arrayList = new ArrayList<ArrayList<String>>();
									int rowcount = table.getRowCount();

									for (int x = 0; x < rowcount; x++) {
										arrayList
												.add(new ArrayList<String>(Arrays.asList("" + table.getValueAt(x, 0))));
									}

									arrayList.add(new ArrayList<String>(Arrays.asList("")));

									// convert back to String[][] and update
									// table
									String[][] array = new String[arrayList.size()][];
									for (int i = 0; i < arrayList.size(); i++) {
										ArrayList<String> row = arrayList.get(i);
										array[i] = row.toArray(new String[row.size()]);
									}

									listpanel.removeAll();
									listpanel.add(SearchTablePanel(array), BorderLayout.CENTER);
									listpanel.repaint();
									listpanel.revalidate();

								}
							};

							t.start();
						}
					});

					JMenuItem item2 = new JMenuItem();
					item2.setText("RemoveItem");
					item2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							Thread t = new Thread() {
								public void run() {
									if (table.getSelectedRow() > 0) {
										((DefaultTableModel) table.getModel()).removeRow(table.getSelectedRow());
									}
								}
							};

							t.start();
						}
					});

					menu.add(item);
					menu.add(item2);
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(table));

		container.add(tp, BorderLayout.CENTER);

		return container;
	}

}