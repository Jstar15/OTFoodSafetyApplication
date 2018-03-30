package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import backup.BackupDataToZip;

public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainpanel;

	// setup main gui
	public Gui() {
		super("Food Safety");

		mainpanel = new LabelManagerMenu();

		// setup main frame
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		setContentPane(container);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1100, 700);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 650));
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("resources/logo.png")));

		JSplitPane splitPaneH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPaneH.setRightComponent(mainpanel);
		splitPaneH.setLeftComponent(MenuPanel());

		container.add(TitlePanel(), BorderLayout.NORTH);
		container.add(splitPaneH, BorderLayout.CENTER);
	}

	// initiate main gui frame
	public static void main(String[] args) {
		Gui frame = new Gui();
		frame.setVisible(true);
	}

	// main title panel
	private JPanel TitlePanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBackground(Color.GRAY);
		panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 55));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel leftpanel = new JPanel();
		leftpanel.setLayout(new BorderLayout());
		leftpanel.setBackground(Color.GRAY);

		JLabel title = new JLabel("   Food Safety Manager");
		title.setFont(new Font("Serif", Font.BOLD, 22));
		title.setForeground(Color.WHITE);

		JLabel name = new JLabel("    2.1");
		name.setFont(new Font("Serif", Font.BOLD, 14));
		name.setForeground(Color.WHITE);
		name.setBorder(new EmptyBorder(10, 0, 0, 0));

		leftpanel.add(title, BorderLayout.WEST);
		leftpanel.add(name, BorderLayout.EAST);

		JButton jButton1 = new JButton(
				new ImageIcon(getClass().getClassLoader().getResource("resources/OwenTaylorLogo.png")));
		jButton1.setOpaque(true);
		jButton1.setBorderPainted(false);
		jButton1.setBorder(null);
		jButton1.setBackground(Color.gray);
		jButton1.setEnabled(false);

		JPanel btnpanel = new JPanel();
		btnpanel.setLayout(new GridLayout(1, 1));
		btnpanel.add(jButton1);

		panel.add(leftpanel, BorderLayout.WEST);
		panel.add(btnpanel, BorderLayout.EAST);

		return panel;
	}

	// main menu panel
	private JPanel MenuPanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);

		JButton btn = new JButton("LABEL MANAGER");
		btn.setBackground(Color.white);
		btn.setForeground(Color.black);
		btn.setOpaque(true);
		btn.setFocusable(false);
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainpanel.removeAll();
				mainpanel.setLayout(new BorderLayout());
				mainpanel.add(new LabelManagerMenu(), BorderLayout.CENTER);
				mainpanel.repaint();
				mainpanel.revalidate();
			}
		});

		JButton btn2 = new JButton("SETTINGS");
		btn2.setBackground(Color.white);
		btn2.setForeground(Color.DARK_GRAY);
		btn2.setOpaque(true);
		btn2.setFocusable(false);
		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainpanel.removeAll();
				mainpanel.add(new SettingsMenu());
				mainpanel.repaint();
				mainpanel.revalidate();
			}
		});

		JButton btn3 = new JButton("BACKUP DATA");
		btn3.setBackground(Color.white);
		btn3.setForeground(Color.black);
		btn3.setOpaque(true);
		btn3.setFocusable(false);
		btn3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new BackupDataToZip();
			}
		});

		JButton btn4 = new JButton("DATA CHECKER");
		btn4.setBackground(Color.white);
		btn4.setForeground(Color.black);
		btn4.setOpaque(true);
		btn4.setFocusable(false);
		btn4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainpanel.removeAll();
				mainpanel.setLayout(new BorderLayout());
				mainpanel.add(new DatabaseChecker(), BorderLayout.CENTER);
				mainpanel.repaint();
				mainpanel.revalidate();
			}
		});
		panel.add(btn);
		panel.add(btn4);
		panel.add(btn3);
		panel.add(btn2);

		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setForeground(Color.WHITE);
		container.setBackground(Color.DARK_GRAY);

		container.add(panel, BorderLayout.NORTH);

		return container;
	}
}