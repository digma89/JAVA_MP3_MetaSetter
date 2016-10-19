package main;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
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
import javax.swing.JProgressBar;



public class MainApp {

	private JFrame frame;
	
	//Create global labels to set after click events
	JLabel lbl_setMeta_sourceFolder = new JLabel("");
	JLabel lbl_setMeta_resultfolder = new JLabel("");
	JLabel lbl_setFile_sourceFolder = new JLabel("");
	JLabel lbl_setFile_resultFolder = new JLabel("");
	
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
		frame.setBounds(100, 100, 700, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "name_347042451980677");
		
		JPanel setMeta = new JPanel();
		tabbedPane.addTab("Set metaData", null, setMeta, null);
		setMeta.setLayout(null);		
		
		//BTN select source folder in meta
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
		
		//BTN select result folder in meta
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
		
		//BTN RUN in meta
		JButton btn_setMeta_run = new JButton("Run");
		btn_setMeta_run.setBounds(556, 204, 109, 31);
		setMeta.add(btn_setMeta_run);
		btn_setMeta_run.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				String sourceFolder = lbl_setMeta_sourceFolder.getText().trim() +"/";
				String resultFolder = lbl_setMeta_resultfolder.getText().trim() +"/";
				String album = "";
				String year = "";
				String genre = "";
				
				if(sourceFolder != "" && resultFolder != ""){
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
						backend.setMetaData(sourceFolder, resultFolder, album, year, genre);
						JOptionPane.showMessageDialog(frame, "Complete");
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
		
		//BTN select source folder in file
		JButton btn_setFile_sourceFolder = new JButton("Source folder");
		btn_setFile_sourceFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lbl_setFile_sourceFolder.setText(fileChooser());
			}
		});
		btn_setFile_sourceFolder.setBounds(12, 13, 109, 31);
		setFileName.add(btn_setFile_sourceFolder);
		
		lbl_setFile_sourceFolder.setBounds(133, 17, 532, 21);
		setFileName.add(lbl_setFile_sourceFolder);
		
		//BTN select result folder in file
		JButton btn_setFile_ResultFolder = new JButton("Result folder");
		btn_setFile_ResultFolder.setBounds(12, 57, 109, 31);
		setFileName.add(btn_setFile_ResultFolder);
		btn_setFile_ResultFolder.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				lbl_setFile_resultFolder.setText(fileChooser());				
			}
		});
		
		lbl_setFile_resultFolder.setBounds(133, 64, 532, 21);
		setFileName.add(lbl_setFile_resultFolder);
		
		//BTN Run in file
		JButton btn_setFile_run = new JButton("Run");
		btn_setFile_run.setBounds(556, 204, 109, 31);
		setFileName.add(btn_setFile_run);
		btn_setFile_run.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				String sourceFolder = lbl_setFile_sourceFolder.getText().trim() +"/";
				String resultFolder = lbl_setFile_resultFolder.getText().trim() +"/";
				
				if(sourceFolder != "" && resultFolder != ""){					
					BackEnd backend  = new BackEnd();
					try {
						backend.setFileNames(sourceFolder, resultFolder);
						JOptionPane.showMessageDialog(frame, "Complete");
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
