package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import XML.XMLReader;

//connects to database
public class ConnectionClass {
	private Connection conn = null;

	String classs = "net.sourceforge.jtds.jdbc.Driver";
	String init = "jdbc:jtds:sqlserver://";

	public ConnectionClass() {
		try {
			Class.forName(classs);
			DriverManager.setLoginTimeout(10);

			// get connection details from xml file
			XMLReader x = new XMLReader();
			String ConnURL = x.getLocation();

			conn = DriverManager.getConnection(ConnURL); // create connection

		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database: " + se.getMessage());
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database: " + e.getMessage());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot connect to database: " + e.getMessage());
		}
	}

	public Connection getConn() {
		return conn;
	}

	public void ConnectionClose() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
