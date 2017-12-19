package xls2xmls;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FileSelector extends JFrame implements ActionListener{

	JLabel label1, label2;
	
	private static String selectedFileName=null;
	private static String path=null;
	
		
	public FileSelector() {
		super(".");
		JButton button = new JButton("選択");
		button.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(button);
		label1 = new JLabel();
		label2 = new JLabel();
		JPanel labelPanel = new JPanel();
		labelPanel.add(label1, BorderLayout.NORTH);
		labelPanel.add(label2, BorderLayout.SOUTH);
		
		getContentPane().add(labelPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		
	}
	
	public void actionPerformed(ActionEvent e){
		JFileChooser fileChooser = new JFileChooser(".");
		//fileChooser.setMultiSelectionEnabled(true);
		int selected = fileChooser.showOpenDialog(this);
		//System.out.println(selected);
		if(selected == JFileChooser.APPROVE_OPTION){
			File selectedFile = fileChooser.getSelectedFile();
			selectedFileName = selectedFile.getName();
			path = selectedFile.getPath().replace(selectedFileName, "");
	    	label1.setText("File:" + selectedFileName);
	    	label2.setText("Directory:" + path);
	    	xls2xml.doXls2Xml(selectedFileName, path);
			this.dispose();
		}else if(selected == JFileChooser.CANCEL_OPTION){
			this.dispose();
			return ;
	    }else if (selected == JFileChooser.ERROR_OPTION){
	    	label1.setText("エラーです、開発者にお問い合わせください");
			this.dispose();
	    	return ;
	    }else{
	    	label1.setText("エラーです、開発者にお問い合わせください");
	    	this.dispose();
	    	return ;
	    }
		
	}

	public static String getSelectedFileName() {
	    return selectedFileName;
	}

	public static String getPath() {
	    return path;
	}
	
	
}
