import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class UIHandler implements ActionListener{

	private QueryHandler qH = new QueryHandler();
	private JFrame mFrame = new JFrame("LPChecker");
	private JPanel mPanel = new JPanel();
	private Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	private final int DAYS_FOR_LP_LOSS = 28;
	private LocalDate currDate = new LocalDate();
	private int daysSinceMatch;
	private int daysTillLoss;
	//StartScreen Components
	private JLabel summReqs1 = new JLabel("Requirements:");
	private JLabel summReqs2 = new JLabel("Must be level 30");
	private JLabel summReqs3 = new JLabel("Must have played Ranked Solo Queue since late July");	
	private JLabel summNameLab = new JLabel("Enter Summoner Name:");
	private JTextField summNameBox = new JTextField();
	private JLabel summRegionLab = new JLabel("Choose Region:");
	private String[] regions = {"NA", "EUW", "EUNE", "KR", "OCE", "RU", "BR", "LAN", "LAS", "TR", "PBE"};
	private JComboBox<String> summRegionBox = new JComboBox<String>(regions);
	private JButton searchBut = new JButton("How long till I lose LP?");
	//StatsScreen Components
	private JLabel daysSinLab;
	private JLabel daysLefLab;
	private JLabel botLab = new JLabel("before LP loss occurs!");
	private Font largeFont = new Font("Verdana", Font.BOLD, 40);
	private Font smallFont = new Font("Verdana", Font.BOLD, 10);
	private JButton againBut = new JButton("Search Again");
	private JButton closeBut = new JButton("Close");
	
	/**
	 * Prepares the size, icon, location, and other properties of the frame.
	 * Sets the background and content of the panel (using showStartScreen()).
	 */
	public UIHandler(){
		searchBut.addActionListener(this);
		againBut.addActionListener(this);
		closeBut.addActionListener(this);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setResizable(false);
		mFrame.setSize(new Dimension(scrSize.width*1/6, scrSize.height*1/4));
		mPanel.setBackground(new Color(0xAAF6FA));
		mFrame.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("frameIcon.png")).getImage());
		mFrame.setContentPane(mPanel);
		mFrame.setLocation(scrSize.width/2-mFrame.getSize().width/2, scrSize.height/2-mFrame.getSize().height/2);
		showStartScreen();
		mFrame.setVisible(true);
	}

	/**
	 * Displays the opening screen where users enter a summoner name and region.
	 * Sets the font and colour of the labels, and the layout of the panel.
	 */
	public void showStartScreen(){
		mPanel.setLayout(new GridLayout(8,1));
		prepPanel();
		summReqs1.setFont(smallFont);
		summReqs2.setFont(smallFont);
		summReqs3.setFont(smallFont);
		summNameLab.setFont(smallFont);
		summRegionLab.setFont(smallFont);
		summReqs1.setForeground(Color.red);
		summReqs2.setForeground(Color.red);
		summReqs3.setForeground(Color.red);
		mPanel.add(summReqs1);
		mPanel.add(summReqs2);
		mPanel.add(summReqs3);
		mPanel.add(summNameLab);
		mPanel.add(summNameBox);
		mPanel.add(summRegionLab);
		mPanel.add(summRegionBox);
		mPanel.add(searchBut);
	}

	/**
	 * Displays the  screen where users are shown how many days they have left till LP decay occurs.
	 * Sets the font and colour of the labels, and the layout of the panel.
	 */
	public void showStatsScreen(){
		mPanel.setLayout(new GridLayout(5,1));
		prepPanel();
		daysSinLab = new JLabel("<html>Your last ranked match was <font color='red'>" + daysSinceMatch + " days ago</font>. You have:</html>");
		daysLefLab = new JLabel("<html><font color='red'>"+Integer.toString(daysTillLoss) + " days</font></html>", SwingConstants.CENTER);
		daysSinLab.setFont(smallFont);
		daysLefLab.setFont(largeFont);
		botLab.setFont(smallFont);
		mPanel.add(daysSinLab);
		mPanel.add(daysLefLab);
		mPanel.add(botLab);
		mPanel.add(againBut);
		mPanel.add(closeBut);
	}
	
	/**
	 * Clears the panel of all components.
	 */
	private void prepPanel(){
		mPanel.removeAll();
		mPanel.revalidate();
		mPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		long matchTime;
		LocalDate matchDate;
		if(e.getSource() == searchBut){
			String summRegion = summRegionBox.getSelectedItem().toString().toLowerCase();
			String summID = qH.getSummID(summNameBox.getText(), summRegion);
			if(!summID.equals("failed")){
				matchTime = qH.getRankedMatchTime(summID, summRegion);
				matchDate = new LocalDate(matchTime);
				daysSinceMatch = Days.daysBetween(matchDate, currDate).getDays();
				daysTillLoss = DAYS_FOR_LP_LOSS - daysSinceMatch;
				showStatsScreen();
			}else{
				JOptionPane.showMessageDialog(mPanel, "Error - cannot find summoner.\nEnsure correct Summoner Name and Region chosen.\nEnsure listed requirements are fulfilled.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}else if(e.getSource() == againBut){
			showStartScreen();
		}else{
			System.exit(1);
		}
	}
}
