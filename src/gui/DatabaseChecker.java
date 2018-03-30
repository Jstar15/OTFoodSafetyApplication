package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.io.FileUtils;

import commons.IngrMenuObject;
import database.ConnectionClass;
import database.QueryAllProducts;
import excelgrabber.AllergensFileObject;
import excelgrabber.NutritionFileObject;
import excelgrabber.PackagingFileObject;

public class DatabaseChecker extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel maincontainer = null;
	private AllergensFileObject allergenload;
	private NutritionFileObject nutritionload;
	private PackagingFileObject packload;
	private JTable table = null;
	private JLabel title = new JLabel(" Database Checker (MISSING)");

	public DatabaseChecker() {
		maincontainer = new JPanel(new GridLayout(4, 5));
		setLayout(new BorderLayout());

		allergenload = new AllergensFileObject();
		nutritionload = new NutritionFileObject();
		// packload = new PackagingFileObject();

		JButton allerbtn = new JButton("CHECK ALLERGENS");
		allerbtn.setBackground(Color.gray);
		allerbtn.setForeground(Color.WHITE);
		allerbtn.setOpaque(true);
		allerbtn.setFocusable(false);
		allerbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ArrayList<String>> outputarray = new ArrayList<ArrayList<String>>();

				// load entire database array
				ConnectionClass conn = new ConnectionClass();
				QueryAllProducts q = new QueryAllProducts(conn.getConn());
				try {
					q.QueryAllCodes();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				ArrayList<IngrMenuObject> qresults = q.getQueryArray();

				for (IngrMenuObject i : qresults) {
					ArrayList<String> result = allergenload.SearchAller(i.getPcode());
					if (result == null) {
						ArrayList<String> strarr1 = new ArrayList<String>();
						strarr1.add(i.getPcode());
						strarr1.add(i.getDesc());
						strarr1.add(i.getCatid());
						outputarray.add(strarr1);
					}
				}

				title.setText(" Database Checker (Missing Allergen Information)");

				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SelectionTablePanel(outputarray), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
			}
		});

		JButton nutribtn = new JButton("CHECK NUTRITION");
		nutribtn.setBackground(Color.gray);
		nutribtn.setForeground(Color.WHITE);
		nutribtn.setOpaque(true);
		nutribtn.setFocusable(false);
		nutribtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<ArrayList<String>> outputarray = new ArrayList<ArrayList<String>>();

				// load entire database array
				ConnectionClass conn = new ConnectionClass();
				QueryAllProducts q = new QueryAllProducts(conn.getConn());
				try {
					q.QueryAllCodes();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				ArrayList<IngrMenuObject> qresults = q.getQueryArray();
				for (IngrMenuObject i : qresults) {
					ArrayList<String> result = nutritionload.SearchNutri(i.getPcode());
					if (result == null) {
						ArrayList<String> strarr1 = new ArrayList<String>();
						strarr1.add(i.getPcode());
						strarr1.add(i.getDesc());
						strarr1.add(i.getCatid());
						outputarray.add(strarr1);
					}
				}

				title.setText(" Database Checker (Missing Nutitional Information)");

				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(SelectionTablePanel(outputarray), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
			}
		});

		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(new JPanel());
		maincontainer.add(allerbtn);
		maincontainer.add(new JPanel());
		maincontainer.add(nutribtn);
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

		add(maincontainer, BorderLayout.CENTER);
	}

	private JPanel SelectionTablePanel(ArrayList<ArrayList<String>> queryarray) {

		Object[][] xmlobject = new Object[queryarray.size()][3];

		for (int j = 0; j < queryarray.size(); j++) {
			xmlobject[j][2] = queryarray.get(j).get(2);
			xmlobject[j][1] = queryarray.get(j).get(1);
			xmlobject[j][0] = queryarray.get(j).get(0);
		}

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());

		// headers for the table
		DefaultTableModel dm = new DefaultTableModel();

		dm.setDataVector(xmlobject, new Object[] { "Pcode", "Description", "Label" });

		// setup main selection table
		table = new JTable(dm) {
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

		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				table.repaint();
			}
		});

		table.setRowHeight(30);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoCreateRowSorter(true);

		JPanel tp = new JPanel();
		tp.setLayout(new BorderLayout());
		tp.add(new JScrollPane(table));

		JButton backbtn = new JButton("Back");
		backbtn.setBackground(Color.white);
		backbtn.setForeground(Color.DARK_GRAY);
		backbtn.setOpaque(true);
		backbtn.setFocusable(false);
		backbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				maincontainer.removeAll();
				maincontainer.setLayout(new BorderLayout());
				maincontainer.add(new DatabaseChecker(), BorderLayout.CENTER);
				maincontainer.repaint();
				maincontainer.revalidate();
			}
		});

		JButton exportbtn = new JButton("Export");
		exportbtn.setBackground(Color.white);
		exportbtn.setForeground(Color.DARK_GRAY);
		exportbtn.setOpaque(true);
		exportbtn.setFocusable(false);
		exportbtn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {

				StringBuilder sb = new StringBuilder();
				sb.append("Pcode, Description, Label" + "\n");

				for (ArrayList<String> arr : queryarray) {
					sb.append(arr.get(0) + "," + arr.get(1) + "," + arr.get(2) + "\n");
				}

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");

				int userSelection = fileChooser.showSaveDialog(null);

				String csvFile = "";
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					File fileToSave = fileChooser.getSelectedFile();
					csvFile = fileToSave.getAbsolutePath() + ".csv";
					try {
						FileUtils.writeStringToFile(new File(csvFile), sb.toString());
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					JOptionPane.showMessageDialog(null, "CSV file has been generated successfully.");
				}
			}
		});

		JPanel btnpanel = new JPanel();
		btnpanel.setLayout(new BorderLayout());
		btnpanel.setBackground(Color.DARK_GRAY);
		btnpanel.setForeground(Color.WHITE);
		btnpanel.add(exportbtn, BorderLayout.CENTER);
		btnpanel.add(backbtn, BorderLayout.EAST);

		title.setForeground(Color.WHITE);
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.DARK_GRAY);
		titlepanel.setForeground(Color.WHITE);
		titlepanel.add(title, BorderLayout.WEST);
		titlepanel.add(btnpanel, BorderLayout.EAST);

		container.add(titlepanel, BorderLayout.NORTH);
		container.add(tp, BorderLayout.CENTER);
		return container;
	}
}
