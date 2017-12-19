package xls2xmls;

public class Main {

	public static void main(String[] args) {

		FileSelector fs = new FileSelector();

		//fs.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fs.setBounds(60, 60, 640, 480);
		fs.setTitle("excelファイルを選択");
		fs.setVisible(true);

	}

}
