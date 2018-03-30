package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.DefaultEditorKit;

import commons.RunCommand;
import database.ConnectionClass;
import database.QueryByDepartment;
import database.QueryIngredientsDetails;
import database.QuerySearchStr;
import database.UpdateNutritional;
import editor.IngredientsEditor;
import editor.PercentileEditor;
import excelgrabber.AllergensFileObject;
import excelgrabber.NutritionFileObject;
import otspec.OTSpecGenerator;

public class LabelManagerMenu extends JPanel {
	private static final long serialVersionUID = 1L;
	private Object[][] o = null;
	private Object[][] o2 = null;
	private JTable nutritable = null;
	private JTable maintable = null;
	private JTable mainmenutable = null;
	private JPanel maincontainer = null;
	private JPanel nutripanel = new JPanel();
	private JPanel searchbar;
	private JTextField searchbox;
	private JLabel lifespanlabelfz, lifespanlabeltr, lifespanlabelcr;
	private JLabel packaginglabel;
	private JEditorPane jep;
	private JRadioButton firstButton, secondButton;
	private JButton backbtn;
	private JButton searchtitlebtn;
	private QueryIngredientsDetails q;
	private AllergensFileObject allergenload;
	private NutritionFileObject nutritionload;
	private ArrayList<String> result, result2;
	private int row;
	private int searchstatus = 1;
	private int option;
	private String catidcurrent = "0";
	private String phaseidcurrent = "0";
	private int selectrow = 0;

	public LabelManagerMenu() {
		maincontainer = new JPanel(new GridLayout(4, 5));
		setLayout(new BorderLayout());

		// load allergens from excel file
		allergenload = new AllergensFileObject();
		nutritionload = new NutritionFileObject();
		// packagingload = new PackagingFileObject();

		JButton manbtn = new JButton("MANUFACTURING");
		manbtn.setBackground(Color.gray);
		manbtn.setForeground(Color.WHITE);
		manbtn.setOpaque(true);
		manbtn.setFocusable(false);
		manbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SelectionTablePanel(1), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
				RemoveSearchBar();
			}
		});

		JButton bakebtn = new JButton("BAKEHOUSE");
		bakebtn.setBackground(Color.gray);
		bakebtn.setForeground(Color.WHITE);
		bakebtn.setOpaque(true);
		bakebtn.setFocusable(false);
		bakebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SelectionTablePanel(2), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();

				RemoveSearchBar();
			}
		});

		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(manbtn);
		maincontainer.add(new JPanel());
		maincontainer.add(bakebtn);
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		searchbar = SearchBar();

		add(searchbar, BorderLayout.NORTH);
		add(maincontainer, BorderLayout.CENTER);
	}

	private JPanel SearchBar() {
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		JPanel searchpanel = new JPanel();
		searchpanel.setLayout(new BorderLayout());

		searchtitlebtn = new JButton("Search");
		searchtitlebtn.setOpaque(true);
		searchtitlebtn.setFocusable(false);
		searchtitlebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SearchTablePanel(), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();

			}
		});

		searchbox = new JTextField(20);
		searchbox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				searchtitlebtn.doClick();
			}
		});

		JPanel searchbarpanel = new JPanel();
		searchbarpanel.setLayout(new BorderLayout());

		// Create the buttons.
		firstButton = new JRadioButton("Code");
		firstButton.setSelected(true);
		firstButton.setFocusable(false);
		firstButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstButton.setSelected(true);
				secondButton.setSelected(false);
				searchstatus = 1;
			}
		});
		secondButton = new JRadioButton("Name");
		secondButton.setFocusable(false);
		secondButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				firstButton.setSelected(false);
				secondButton.setSelected(true);
				searchstatus = 2;
			}
		});

		JPanel group = new JPanel();
		group.add(firstButton);
		group.add(secondButton);

		searchpanel.add(searchtitlebtn, BorderLayout.EAST);
		searchpanel.add(searchbox, BorderLayout.CENTER);

		searchbarpanel.add(searchpanel, BorderLayout.CENTER);

		container.add(searchbarpanel, BorderLayout.CENTER);
		container.add(group, BorderLayout.WEST);

		container.setBorder(BorderFactory.createEmptyBorder(5, 150, 5, 150));

		return container;
	}

	private void RemoveSearchBar() {
		searchbar.removeAll();
		searchbar.setVisible(false);
		searchbar.repaint();
		searchbar.revalidate();
	}

	private JPanel SelectionTablePanel(int option) {

		this.option = option;
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table
		DefaultTableModel dm = new DefaultTableModel();

		ConnectionClass conn = new ConnectionClass();
		QueryByDepartment q = new QueryByDepartment(conn.getConn());
		q.QueryNutritionMenu(option);

		o = q.getXmlobject();
		dm.setDataVector(o, new Object[] { "ID", "catid", "Description" });

		// setup main selection table
		mainmenutable = new JTable(dm) {
			private static final long serialVersionUID = 1L;

			// Returning the Class of each column will allow different
			// renderers to be used based on Class
			public Class<? extends Object> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		mainmenutable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainmenutable.repaint();
			}
		});

		mainmenutable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				catidcurrent = (String) o[row][1];
				phaseidcurrent = (String) o[row][0];
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(MainIngredientsInfoPanel(catidcurrent, phaseidcurrent), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
				mainmenutable.setRowSelectionInterval(0, 0);
			}
		});

		mainmenutable.setRowHeight(30);
		mainmenutable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(mainmenutable));

		container.add(tp, BorderLayout.CENTER);
		return container;
	}

	private JPanel MainIngredientsInfoPanel(String catid, String phaseid) {
		catidcurrent = catid;
		phaseidcurrent = phaseid;

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table
		DefaultTableModel dm = new DefaultTableModel();

		ConnectionClass conn = new ConnectionClass();
		q = new QueryIngredientsDetails(conn.getConn());
		q.QueryIngredientsData(catid, phaseid);

		o = q.getXmlobject();

		dm.setDataVector(o, new Object[] { "ProductID", "Description", "Label", "Meat Percentile(%)" });

		jep = new JEditorPane();
		jep.setEditable(false);
		jep.setContentType("text/html");
		jep.setText("<html>" + "</html>");
		jep.addMouseListener(new MouseAdapter() {
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {
					final JPopupMenu menu = new JPopupMenu();
					JMenuItem item;
					item = new JMenuItem(new DefaultEditorKit.CopyAction());
					item.setText("Copy");
					item.setEnabled(jep.getSelectionStart() != jep.getSelectionEnd());

					JMenuItem item2 = new JMenuItem();
					item2.setText("Edit");
					item2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ev) {
							Thread t = new Thread() {
								public void run() {
									IngredientsEditor e = new IngredientsEditor();
									e.createAndShowGUI(catid, phaseid, q.getTempobj().get(0).getIngredients());

									JFrame parentframe = (JFrame) SwingUtilities.getRoot(getParent());
									parentframe.setEnabled(false);
									e.getFrame__().addWindowListener(new WindowAdapter() {
										@Override
										public void windowClosed(WindowEvent event) {
											// update ingredients field from
											// database in case of any changes
											// in editor
											parentframe.setEnabled(true);
											parentframe.toFront();

											q.getTempobj().get(0).setIngredients(e.getUpdatedingredients());
											jep.setText(e.getUpdatedingredients());
											jep.repaint();
										}
									});
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

		try {

			jep.setText("<html>" + q.getTempobj().get(0).getIngredients() + "</html>");

		} catch (IndexOutOfBoundsException e) {
			// ignore
		}

		maintable = new JTable(dm) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		maintable.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				maintable.repaint();
			}
		});

		maintable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable source = (JTable) e.getSource();
				row = source.rowAtPoint(e.getPoint());

				// String packinfo =
				// packagingload.SearchPack(q.getTempobj().get(row).getPcode());
				// if (packinfo.length() > 0) {
				// packaginglabel.setText(" Pack: " + packinfo + " ");
				// packaginglabel.setVisible(true);
				// packaginglabel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1,
				// Color.BLACK));
				// q.getTempobj().get(row).setPackaginginfo(packinfo);
				// }

				// set lifespans
				lifespanlabeltr.setText("  " + q.getTempobj().get(row).getLifespantr() + "  ");
				lifespanlabelcr.setText("  " + q.getTempobj().get(row).getLifespancr() + "  ");

				JTabbedPane nutritionpane = new JTabbedPane();
				nutritionpane.setBackground(Color.white);
				nutritionpane.setForeground(Color.DARK_GRAY);
				nutritionpane.setOpaque(true);
				nutritionpane.setFocusable(false);
				nutritionpane.addTab("<html><p style='padding-top:3px;'>Excel</p></html>",
						NutitionalPanel(q.getTempobj().get(row).getNutrition(), q.getTempobj().get(row).getPcode(),
								q.getTempobj().get(row).getDescription(), 2));
				nutritionpane.addTab("<html><p style='padding-top:3px;'>SI</p></html>",
						NutitionalPanel(q.getTempobj().get(row).getNutrition(), q.getTempobj().get(row).getPcode(),
								q.getTempobj().get(row).getDescription(), 1));

				// nutiional info
				nutripanel.removeAll();
				nutripanel.setLayout(new BorderLayout());
				nutripanel.add(nutritionpane, BorderLayout.CENTER);

				JLabel storagelabela = new JLabel("No storage info found.");
				JLabel storagelabelb = new JLabel("");

				try {
					String[] arr = q.getTempobj().get(row).getStorage().split("<br />");

					String a = "  " + arr[0] + "  ";
					String b = "  " + arr[1] + "  ";
					storagelabela.setText(a.replace("<B>", "").replace("</B>", ""));
					storagelabelb.setText(b.replace("<B>", "").replace("</B>", ""));
				} catch (Exception eee) {

				}

				JPanel storagepanel = new JPanel();
				storagepanel.setLayout(new BorderLayout());
				storagepanel.setBorder(new LineBorder(Color.black, 1));
				storagepanel.add(storagelabela, BorderLayout.EAST);
				storagepanel.add(storagelabelb, BorderLayout.WEST);

				JPanel allerNstoragepanel = new JPanel();
				allerNstoragepanel.setLayout(new BorderLayout());

				allerNstoragepanel.add(storagepanel, BorderLayout.SOUTH);

				// set allergens info
				result = allergenload.SearchAller(q.getTempobj().get(row).getPcode());
				if (result == null) {
					JLabel msg = new JLabel(
							"No Allergen Info Found For Product: " + q.getTempobj().get(row).getPcode() + "   ");
					JPanel msgpanelss = new JPanel(new BorderLayout());
					msgpanelss.add(msg, BorderLayout.EAST);

					allerNstoragepanel.add(msgpanelss, BorderLayout.CENTER);

				} else {
					// allergens panel
					JLabel image = new JLabel("");

					image.setIcon(new ImageIcon(getClass().getClassLoader().getResource("resources/allert.png")));
					image.setHorizontalTextPosition(JLabel.CENTER);
					String s = "<html><br><br><br><br><br><br><br><br><b> " + CheckInputAller(result.get(2))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(3))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(4))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(5))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(6))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(7))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(8))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(9))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(10))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(11))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(12))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(13))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(14))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(15))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(16))
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + CheckInputAller(result.get(17)) + "</b></html>";

					image.setText(s);
					image.revalidate();

					allerNstoragepanel.add(image, BorderLayout.CENTER);
				}

				nutripanel.add(allerNstoragepanel, BorderLayout.SOUTH);
				nutripanel.repaint();
				nutripanel.revalidate();
			}
		});

		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem generateItem = new JMenuItem("GENERATE OT-SPEC");
		generateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OTSpecGenerator ot = new OTSpecGenerator(q.getTempobj().get(row), result,
						getSpecNumber(phaseid, catid));
				System.out.println(q.getTempobj().get(row).getNutrition());
				System.out.println(q.getTempobj().get(row).getNutritionexcel());
				ot.SaveToFile();
				int reply = JOptionPane.showConfirmDialog(null, "Do you want to open the file now?",
						"Saved Successfully", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					new RunCommand("start " + ot.getDestfilePath());
				}
			}
		});

		JMenuItem previewItem = new JMenuItem("PREVIEW OT-SPEC");
		previewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OTSpecGenerator ot = new OTSpecGenerator(q.getTempobj().get(row), result,
						getSpecNumber(phaseid, catid));
				WebPreviewFrame frame2 = new WebPreviewFrame(ot.getFileTemp());
				frame2.setVisible(true);
			}
		});

		JMenuItem modpercentileItem = new JMenuItem("UPDATE PERCENTILE");
		modpercentileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// open meat percentile update panel
				Thread t = new Thread() {
					public void run() {
						PercentileEditor e = new PercentileEditor();
						e.createAndShowGUI(q.getTempobj().get(row).getPcode(),
								q.getTempobj().get(row).getMeatpercentile(), q.getTempobj().get(row).getDescription());

						JFrame parentframe = (JFrame) SwingUtilities.getRoot(getParent());
						parentframe.setEnabled(false);
						e.getFrame__().addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent event) {
								// update ingredients field from
								// database in case of any changes
								// in editor
								parentframe.setEnabled(true);
								parentframe.toFront();

								int selectrow = maintable.getSelectedRow();

								// refresh panel to show changes
								maincontainer.removeAll();
								maincontainer.setLayout(new BorderLayout());
								maincontainer.add(MainIngredientsInfoPanel(catidcurrent, phaseidcurrent),
										BorderLayout.CENTER);
								maincontainer.repaint();
								maincontainer.revalidate();

								maintable.setRowSelectionInterval(2, selectrow);
							}
						});
					}
				};

				t.start();
			}
		});

		popupMenu.add(generateItem);
		popupMenu.add(previewItem);
		popupMenu.add(modpercentileItem);

		maintable.setComponentPopupMenu(popupMenu);
		maintable.setRowHeight(30);
		maintable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		maintable.getColumnModel().getColumn(0).setMaxWidth(130);
		maintable.getColumnModel().getColumn(0).setMinWidth(135);

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(maintable));

		nutripanel.setLayout(new BorderLayout());

		JScrollPane scrollPane = new JScrollPane(jep); // put ingredient desc in
														// scroll bar
		lifespanlabelfz = new JLabel("");
		lifespanlabeltr = new JLabel("");
		lifespanlabelcr = new JLabel("");
		lifespanlabelcr.setHorizontalAlignment(SwingConstants.CENTER);

		packaginglabel = new JLabel("Pack:  ");

		JPanel packagingpanel = new JPanel();
		packaginglabel.setVisible(false);

		packagingpanel.setLayout(new BorderLayout());

		packagingpanel.add(packaginglabel, BorderLayout.CENTER);

		JPanel lifespanpanel = new JPanel();
		lifespanpanel.setLayout(new BorderLayout());
		lifespanpanel.setBorder(new LineBorder(Color.black, 1));
		lifespanpanel.add(lifespanlabelfz, BorderLayout.EAST);
		lifespanpanel.add(lifespanlabeltr, BorderLayout.WEST);
		lifespanpanel.add(lifespanlabelcr, BorderLayout.CENTER);

		JPanel ingrpaneldatabottom = new JPanel();
		ingrpaneldatabottom.setLayout(new BorderLayout());

		ingrpaneldatabottom.setMinimumSize(new Dimension(0, 200));
		ingrpaneldatabottom.add(packagingpanel, BorderLayout.NORTH);
		ingrpaneldatabottom.add(lifespanpanel, BorderLayout.SOUTH);

		JPanel ingrpanel = new JPanel();
		ingrpanel.setLayout(new BorderLayout());

		ingrpanel.add(scrollPane, BorderLayout.CENTER);
		ingrpanel.add(ingrpaneldatabottom, BorderLayout.SOUTH);

		JPanel bottomdetailspanel = new JPanel();
		bottomdetailspanel.setLayout(new BorderLayout());
		bottomdetailspanel.add(ingrpanel, BorderLayout.CENTER);
		bottomdetailspanel.add(nutripanel, BorderLayout.EAST);

		backbtn = new JButton("Back");
		backbtn.setBackground(Color.white);
		backbtn.setForeground(Color.DARK_GRAY);
		backbtn.setOpaque(true);
		backbtn.setFocusable(false);
		backbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SelectionTablePanel(option), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
			}
		});

		String cattitle = "";

		if (catid.equals("1020")) {
			cattitle = " - Bakehouse";
		}
		if (catid.equals("1021")) {
			cattitle = " - Manufacturing";
		}

		JLabel title = new JLabel(" " + phaseid + cattitle);
		title.setForeground(Color.WHITE);
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.DARK_GRAY);
		titlepanel.setForeground(Color.WHITE);
		titlepanel.add(title, BorderLayout.WEST);
		titlepanel.add(backbtn, BorderLayout.EAST);

		JPanel toppanel = new JPanel();
		toppanel.setLayout(new BorderLayout());
		toppanel.add(titlepanel, BorderLayout.NORTH);
		toppanel.add(tp, BorderLayout.CENTER);

		JSplitPane splitPaneH = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPaneH.setTopComponent(toppanel);
		splitPaneH.setBottomComponent(bottomdetailspanel);
		container.add(splitPaneH, BorderLayout.CENTER);

		return container;
	}

	protected String CheckInputAller(String string) {
		if (string.equals("")) {
			return "&nbsp;&nbsp;";
		}
		return string;
	}

	public JPanel NutitionalPanel(String nutrition, String pcode, String desc, int mode) {
		o2 = new Object[9][2];
		o2[0][0] = "Energy (KJ)";
		o2[1][0] = "Energy (Kcal)";
		o2[2][0] = "Fat";
		o2[3][0] = "Of which saturates";
		o2[4][0] = "Carbohydrates";
		o2[5][0] = "Of Which Sugars";
		o2[6][0] = "Fibre";
		o2[7][0] = "Protein";
		o2[8][0] = "Salt";

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table
		DefaultTableModel dm = new DefaultTableModel();

		// nutrition = "10g/<br />5kj<br />5g<br />5g<br />5g<br />5g<br />5g<br
		// />5g<br />5g";
		String nutritionexcel = "";
		result2 = nutritionload.SearchNutri(q.getTempobj().get(row).getPcode());
		if (result2 != null) {
			int lazycount = 1;
			for (String sa : result2) {
				if (lazycount == 3) {
					nutritionexcel = nutritionexcel + sa + " / ";
				} else if (lazycount > 2) {
					nutritionexcel = nutritionexcel + "<br />" + sa;
				}
				lazycount++;
			}
		}

		try {
			// set nutritional data to data objects
			q.getTempobj().get(row).setNutrition(nutrition);
			q.getTempobj().get(row).setNutritionexcel(nutritionexcel);
			if (mode == 1) {
				// get data from SI database
				String[] splitnutri = nutrition.split("<br />");
				o2[0][1] = splitnutri[0];
				o2[1][1] = splitnutri[1];
				o2[2][1] = splitnutri[2];
				o2[3][1] = splitnutri[3];
				o2[4][1] = splitnutri[4];
				o2[5][1] = splitnutri[5];
				o2[6][1] = splitnutri[6];
				o2[7][1] = splitnutri[7];
				o2[8][1] = splitnutri[8];
				o2[8][1] = String.format("%.2f", o2[8][1] + "") + "g";

			} else if (mode == 2) {
				// get data from excel file
				String[] splitnutri = nutritionexcel.split("<br />");
				o2[0][1] = splitnutri[0];
				o2[1][1] = splitnutri[1];
				o2[2][1] = splitnutri[2];
				o2[3][1] = splitnutri[3];
				o2[4][1] = splitnutri[4];
				o2[5][1] = splitnutri[5];
				o2[6][1] = splitnutri[6];
				o2[7][1] = splitnutri[7];
				o2[8][1] = splitnutri[8];
				o2[8][1] = String.format("%.2f", o2[8][1] + "") + "g";
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}

		dm.setDataVector(o2, new Object[] { "Nutritional Information Typical Value per 100g", pcode });

		nutritable = new JTable(dm) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				if (column == 1 && mode == 1) {
					return true;
				} else {
					return false;
				}
			}
		};

		nutritable.setRowHeight(22);
		nutritable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nutritable.getColumnModel().getColumn(0).setMinWidth(150);
		if (mode == 1) {
			// set table color to show comes froom file
			JTableHeader anHeader = nutritable.getTableHeader();
			anHeader.setForeground(Color.white);
			anHeader.setBackground(Color.black);

			nutritable.setTableHeader(anHeader);
		}

		JButton nutribtnupdate = new JButton("Update");
		nutribtnupdate.setBackground(Color.white);
		nutribtnupdate.setForeground(Color.DARK_GRAY);
		nutribtnupdate.setOpaque(true);
		nutribtnupdate.setFocusable(false);
		nutribtnupdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nutitionalupdatestr = "";
				for (int count = 0; count < nutritable.getRowCount(); count++) {
					if (nutritable.getValueAt(count, 1) == null) {
						nutitionalupdatestr = nutitionalupdatestr + "<br />";
					} else {
						nutitionalupdatestr = nutitionalupdatestr + nutritable.getValueAt(count, 1).toString()
								+ "<br />";
					}
				}

				selectrow = maintable.getSelectedRow();
				System.out.println("cat: " + catidcurrent);
				System.out.println("phrase: " + phaseidcurrent);
				System.out.println("row: " + selectrow);
				System.out.println("pcode: " + nutitionalupdatestr);
				System.out.println("str: " + pcode);

				if (selectrow >= 0 && pcode.trim().length() > 0) {
					ConnectionClass conn = new ConnectionClass();
					UpdateNutritional updatestatement = new UpdateNutritional(conn.getConn());
					updatestatement.UpdateNutritionalToDatabase(pcode, nutitionalupdatestr, desc);

					maincontainer.removeAll();
					maincontainer.setLayout(new BorderLayout());
					maincontainer.add(MainIngredientsInfoPanel(catidcurrent, phaseidcurrent), BorderLayout.CENTER);
					maincontainer.repaint();
					maincontainer.revalidate();

					maintable.setRowSelectionInterval(selectrow, selectrow);
					JOptionPane.showMessageDialog(null,
							"Nutitional data has been updated to SI Successfully For Product: " + pcode + ".");
				} else {
					JOptionPane.showMessageDialog(null,
							"Error: If this happens again please contact your Administator." + pcode + ".");
				}
			}
		});

		JButton nutribtncopyover = new JButton("Copy Over");
		nutribtncopyover.setBackground(Color.white);
		nutribtncopyover.setForeground(Color.DARK_GRAY);
		nutribtncopyover.setOpaque(true);
		nutribtncopyover.setFocusable(false);
		nutribtncopyover.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String[] splitnutri = q.getTempobj().get(row).getNutritionexcel().split("<br />");

					o2[0][1] = splitnutri[0];
					o2[1][1] = splitnutri[1];
					o2[2][1] = splitnutri[2];
					o2[3][1] = splitnutri[3];
					o2[4][1] = splitnutri[4];
					o2[5][1] = splitnutri[5];
					o2[6][1] = splitnutri[6];
					o2[7][1] = splitnutri[7];
					o2[8][1] = splitnutri[8];

					dm.setDataVector(o2, new Object[] { "Nutritional Information Typical Value per 100g", pcode });
					nutritable.repaint();

					// copy to clipboard should sepearate out of this fuction////////////////
					String myString = splitnutri[0];
					myString = myString + "\n";
					myString = myString + splitnutri[1];
					myString = myString + "\n";
					myString = myString + splitnutri[2];
					myString = myString + "\n";
					myString = myString + splitnutri[3];
					myString = myString + "\n";
					myString = myString + splitnutri[4];
					myString = myString + "\n";
					myString = myString + splitnutri[5];
					myString = myString + "\n";
					myString = myString + splitnutri[6];
					myString = myString + "\n";
					myString = myString + splitnutri[7];
					myString = myString + "\n";
					myString = myString + splitnutri[8];

					StringSelection stringSelection = new StringSelection(myString);
					Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
					clpbrd.setContents(stringSelection, null);

					JOptionPane.showMessageDialog(null, "Copied Nutitional Info From Excel To SI.");
				} catch (Exception e2) { //
					System.out.println("Error Message: copying over invalid nutrition fields.");
				}
			}
		});

		JPanel nutribtnpanel = new JPanel();
		nutribtnpanel.setLayout(new BorderLayout());
		nutribtnpanel.add(nutribtncopyover, BorderLayout.WEST);
		nutribtnpanel.add(nutribtnupdate, BorderLayout.EAST);

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(nutritable), BorderLayout.CENTER);

		// save features only available for mode 1 SI

		if (mode == 1) {
			tp.add(nutribtnpanel, BorderLayout.SOUTH);
		}

		container.add(tp, BorderLayout.CENTER);

		return container;
	}

	private JPanel SearchTablePanel() {
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table
		DefaultTableModel dm = new DefaultTableModel();

		ConnectionClass conn = new ConnectionClass();
		QuerySearchStr q = new QuerySearchStr(conn.getConn());
		try {
			q.QueryNutritionMenu(searchbox.getText(), searchstatus);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		o = q.getXmlobject();
		dm.setDataVector(o, new Object[] { "Pcode", "Description", "CatId", "PhaseId" });

		nutritable = new JTable(dm) {
			private static final long serialVersionUID = 1L;

			public Class<? extends Object> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		nutritable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				String c = (String) o[row][2];
				String p = (String) o[row][3];

				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(MainIngredientsInfoPanel(c, p), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();

				backbtn.setVisible(false);
			}
		});

		nutritable.setRowHeight(30);
		nutritable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(nutritable));

		container.add(tp, BorderLayout.CENTER);

		return container;
	}

	private String getSpecNumber(String phrase, String cat) {
		if (cat.equals("1021")) {
			return "MAN" + phrase;
		} else if (cat.equals("1020")) {
			return "BAKE" + phrase;
		}
		return "";

	}

}