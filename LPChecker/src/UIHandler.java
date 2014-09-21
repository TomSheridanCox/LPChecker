import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private	int DAYS_FOR_LP_LOSS = 28;
	private long matchTime;
	private LocalDate currDate = new LocalDate();
	private LocalDate matchDate;
	private int daysSinceMatch;
	private int daysTillLoss;
	//StartScreen Components
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

	public UIHandler(){
		searchBut.addActionListener(this);
		againBut.addActionListener(this);
		closeBut.addActionListener(this);
		mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mFrame.setResizable(false);
		mFrame.setSize(new Dimension(scrSize.width*1/6, scrSize.height*1/4));
		mPanel.setBackground(new Color(0xAAF6FA));
		mFrame.setIconImage(new ImageIcon("src/frameIcon.png").getImage());
		mFrame.setContentPane(mPanel);
		mFrame.setLocation(scrSize.width/2-mFrame.getSize().width/2, scrSize.height/2-mFrame.getSize().height/2);
		showStartScreen();
		mFrame.setVisible(true);
	}

	public void showStartScreen(){
		prepPanel();
		mPanel.add(summNameLab);
		mPanel.add(summNameBox);
		mPanel.add(summRegionLab);
		mPanel.add(summRegionBox);
		mPanel.add(searchBut);
	}

	public void showStatsScreen(){
		prepPanel();
		daysSinLab = new JLabel("Your last ranked match was " + daysSinceMatch + " days ago. You have:");
		daysLefLab = new JLabel(Integer.toString(daysTillLoss) + " days", SwingConstants.CENTER);
		daysSinLab.setFont(smallFont);
		daysLefLab.setFont(largeFont);
		botLab.setFont(smallFont);
		mPanel.add(daysSinLab);
		mPanel.add(daysLefLab);
		mPanel.add(botLab);
		mPanel.add(againBut);
		mPanel.add(closeBut);
	}
	
	private void prepPanel(){
		mPanel.setLayout(new GridLayout(5,1));
		mPanel.removeAll();
		mPanel.revalidate();
		mPanel.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == searchBut){
			String summRegion = summRegionBox.getSelectedItem().toString().toLowerCase();
			String summID = qH.getSummID(summNameBox.getText(), summRegion);
			if(!summID.equals("failed")){
				matchTime = qH.getRankedMatchTime(summID, summRegion);
				matchDate = new LocalDate(matchTime);
				daysSinceMatch = Days.daysBetween(matchDate, currDate).getDays();
				daysTillLoss = DAYS_FOR_LP_LOSS - daysSinceMatch;
				System.out.println("I worked with " + daysTillLoss);
				showStatsScreen();
			}else{
				JOptionPane.showMessageDialog(mPanel, "Cannot find Summoner. Check entered name and region.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}else if(e.getSource() == againBut){
			showStartScreen();
		}else{
			System.exit(1);
		}
	}
}
