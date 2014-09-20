import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIHandler {

	private JFrame mFrame = new JFrame("LPChecker");
	private JPanel mPanel = new JPanel();
	private Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JLabel summNameLab = new JLabel("Enter Summoner Name:");
	private JTextField summNameBox = new JTextField();
	private JLabel summRegionLab = new JLabel("Choose Region:");
	private String[] regions = {"NA", "EUW", "EUNE", "KR", "OCE", "RU", "BR", "LAN", "LAS", "TR", "PBE"};
	private JComboBox<String> summRegionBox = new JComboBox<String>(regions);
	private JButton searchBut = new JButton("How long till I lose LP?");
	
	public UIHandler(){
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setResizable(false);
		mFrame.setSize(new Dimension(scrSize.width*1/6, scrSize.height*1/4));
		mPanel.setLayout(new GridLayout(5,1));
		mPanel.setBackground(new Color(0xAAF6FA));
		mFrame.setIconImage(new ImageIcon("src/frameIcon.png").getImage());
		mFrame.setContentPane(mPanel);
		mFrame.setLocation(scrSize.width/2-mFrame.getSize().width/2, scrSize.height/2-mFrame.getSize().height/2);
		showStartScreen();
		mFrame.setVisible(true);
	}
	
	public void showStartScreen(){
		mPanel.removeAll();
		mPanel.add(summNameLab);
		mPanel.add(summNameBox);
		mPanel.add(summRegionLab);
		mPanel.add(summRegionBox);
		mPanel.add(searchBut);
	}
}
