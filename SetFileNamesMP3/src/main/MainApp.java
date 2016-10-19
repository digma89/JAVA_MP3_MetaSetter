package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

import com.mpatric.mp3agic.BaseException;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v22Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JDesktopPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;



public class MainApp {

	private JFrame frame;
	private JTextField textField;
	
	//Create global labels to set after click events
	JLabel lbl_setMeta_sourceFolder = new JLabel("");
	JLabel lbl_setMeta_resultfolder = new JLabel("");
	private JTextField tf_setMeta_album;
	private JTextField tf_setMeta_year;
	private JTextField tf_setMeta_genre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainApp window = new MainApp();					
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 297);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "name_347042451980677");
		
		JPanel setMeta = new JPanel();
		tabbedPane.addTab("Set metaData", null, setMeta, null);
		setMeta.setLayout(null);		
		
		//BTN select source folder
		JButton btn_setMeta_sourceFolder = new JButton("Source folder");
		btn_setMeta_sourceFolder.setBounds(12, 12, 109, 31);
		setMeta.add(btn_setMeta_sourceFolder);
		btn_setMeta_sourceFolder.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				lbl_setMeta_sourceFolder.setText(fileChooser());
			}
		});
		
		
		lbl_setMeta_sourceFolder.setBounds(133, 16, 532, 21);
		setMeta.add(lbl_setMeta_sourceFolder);
		
		//BTN select result folder
		JButton btn_setMeta_resultFolder = new JButton("Result folder");		
		btn_setMeta_resultFolder.setBounds(12, 56, 109, 31);
		setMeta.add(btn_setMeta_resultFolder);
		btn_setMeta_resultFolder.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				lbl_setMeta_resultfolder.setText(fileChooser());		
			}
		});
		
		lbl_setMeta_resultfolder.setBounds(133, 63, 532, 21);
		setMeta.add(lbl_setMeta_resultfolder);
		
		//BTN RUN
		JButton btn_setMeta_run = new JButton("Run");
		btn_setMeta_run.setBounds(556, 176, 109, 31);
		setMeta.add(btn_setMeta_run);
		btn_setMeta_run.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				String sourceFolder = lbl_setMeta_sourceFolder.getText().trim() +"/";
				String resultFolder = lbl_setMeta_resultfolder.getText().trim() +"/";
				String album = "";
				String year = "";
				String genre = "";
				
				if(sourceFolder != "" || resultFolder != ""){
					if(!tf_setMeta_year.getText().trim().isEmpty()){
						if(isNumeric(tf_setMeta_year.getText().trim())){
							if(Integer.parseInt(tf_setMeta_year.getText().trim()) > 0 && Integer.parseInt(tf_setMeta_year.getText().trim()) < 9999){
								year = tf_setMeta_year.getText().trim();
							}else{
								JOptionPane.showMessageDialog(frame, "Please correct the year");
							}
						}else{
							JOptionPane.showMessageDialog(frame, "Please correct the year");
						}
					}else{
						year = "";
					}
					BackEnd backend  = new BackEnd();
					try {
						album = tf_setMeta_album.getText().trim();
						genre = tf_setMeta_genre.getText().trim();
						backend.getFiles(sourceFolder, resultFolder, album, year, genre);
					} catch (BaseException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage());
						e.printStackTrace();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, e.getMessage());
						e.printStackTrace();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(frame, e.getMessage());
						e.printStackTrace();
					}			
					
				}else{
					JOptionPane.showMessageDialog(frame, "Please select source and result folder");
				}				
			}
		});
		
		JLabel lbl_setMeta_album = new JLabel("Album name:");
		lbl_setMeta_album.setBounds(12, 98, 109, 21);
		setMeta.add(lbl_setMeta_album);
		
		tf_setMeta_album = new JTextField();
		tf_setMeta_album.setBounds(133, 97, 265, 22);
		setMeta.add(tf_setMeta_album);
		tf_setMeta_album.setColumns(10);
		
		JLabel lbl_setMeta_year = new JLabel("Year:");
		lbl_setMeta_year.setBounds(12, 133, 109, 21);
		setMeta.add(lbl_setMeta_year);
		
		tf_setMeta_year = new JTextField();
		tf_setMeta_year.setColumns(10);
		tf_setMeta_year.setBounds(133, 132, 265, 22);
		setMeta.add(tf_setMeta_year);
		
		JLabel lbl_setMeta_genre = new JLabel("Genre:");
		lbl_setMeta_genre.setBounds(12, 168, 109, 21);
		setMeta.add(lbl_setMeta_genre);
		
		tf_setMeta_genre = new JTextField();
		tf_setMeta_genre.setColumns(10);
		tf_setMeta_genre.setBounds(133, 167, 265, 22);
		setMeta.add(tf_setMeta_genre);
		
		JPanel setFileName = new JPanel();
		tabbedPane.addTab("Set file name", null, setFileName, null);
		setFileName.setLayout(null);
		
		JButton button = new JButton("New button");
		button.setBounds(200, 81, 219, 51);
		setFileName.add(button);	 
		
		textField = new JTextField();
		textField.setBounds(49, 144, 116, 22);
		setFileName.add(textField);
		textField.setColumns(10);
	}	   
	
	
	public String fileChooser(){
	    JFileChooser chooser;
	    chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("C:/"));
	    chooser.setDialogTitle("choosertitle");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showSaveDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
	      //System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
	      //System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
	      return chooser.getSelectedFile().toString();
	    }else {
	    	return "No selection";
	    }
	}
	
	public static boolean isNumeric(String str){  
	  try{  
	    double i = Integer.parseInt(str);  
	  }catch(NumberFormatException nfe){  
	    return false;  
	  }  
	  return true;  
	}
	
}
