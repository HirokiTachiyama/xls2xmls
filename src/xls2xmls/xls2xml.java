package xls2xmls;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class xls2xml {

	//private static ArrayList<String>[] xlsArrayLists;
	private static ArrayList<String> menuArrayList;
	private static String targetFileName = null;
	private static String workDir = null;
	private static String crlf=null;

	public static void doXls2Xml(String _targetFileName, String _workDir) {
		targetFileName = _targetFileName;
		workDir = _workDir;
		menuArrayList = new ArrayList();

		//改行コード取得
		//crlf = System.getProperty("line.separator");
		crlf = "\n";

		InputStream inputStream;
		Workbook workbook;
		try {
			inputStream = new FileInputStream(workDir + targetFileName);
			workbook = WorkbookFactory.create(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
			return;
		}
		Sheet sheet = workbook.getSheet("sheet1");

		for(int i=0; i<Integer.MAX_VALUE; i++){
			ArrayList<String> xlsArrayList = new ArrayList<>();
			Row row = sheet.getRow(i);
			if(row == null) { break; }
			for(int j=0; j<Integer.MAX_VALUE; j++){
				Cell cell = row.getCell(j);
				if(cell == null){ //空っぽなら抜ける
					break;
				} else if (i==0) { //最初のは項目用のArrayListに
					menuArrayList.add(cell.toString());
				} else { //２行目以降はデータ用のArrayListに
					//xlsArrayList.add(cell.toString()+" ");
					xlsArrayList.add(cell.toString());
				}
				System.out.print(cell.toString()+", ");
			}
			if(i!=0){
				makeXML(xlsArrayList);
				//xlsArrayLists[xlsArrayListsCounter++] = xlsArrayList;
			}
		}
	}

	public static String getCrlf(){
		return crlf;
	}

	private static void makeXML(ArrayList<String> dataArrayList){
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch(ParserConfigurationException e){
			e.printStackTrace();
		}
        Document document = documentBuilder.newDocument();
    	System.out.println("****"+menuArrayList.size());

        // XML文書の作成
        Element root = document.createElement(menuArrayList.get(0).replace("ID", ""));
        root.setAttribute("id", dataArrayList.get(0)); //最初のID
        document.appendChild(root);


        for(int i=1; i<menuArrayList.size(); i++){
        	Element element = null;

			if(menuArrayList.get(i).contains("'c'")){
				//System.out.println("'c' 発見");
				element = document.createElement("box");
				element.setAttribute("type", "c");
			} else if(menuArrayList.get(i).contains("'d'")){
				//System.out.println("'d' 発見");
				element = document.createElement("box");
				element.setAttribute("type", "d");
			} else if(menuArrayList.get(i).contains("'h'")){
				//System.out.println("'h' 発見");
				element = document.createElement("box");
				element.setAttribute("type", "h");
			} else if(menuArrayList.get(i).contains("'aca'")){
				//System.out.println("'acc' 発見");
				element = document.createElement("box");
				element.setAttribute("type", "aca");
			} else {
				element = document.createElement(menuArrayList.get(i).toString());
			}

			if(menuArrayList.get(i).equals("sequence")){
        		String sequence = dataArrayList.get(i);
        		for(int j=0; j<sequence.length(); j+=61){
        			//System.out.println(sequence.substring(0, j) + "\n" + sequence.substring(j));
        			sequence = sequence.substring(0, j) + crlf + sequence.substring(j);
        		}
        		sequence += crlf;
        		element.appendChild(document.createTextNode(sequence));
        		root.appendChild(element);
        	} else {
        		element.appendChild(document.createTextNode(dataArrayList.get(i)));
        		root.appendChild(element);
        	}
        }
        File file = new File(workDir+dataArrayList.get(0)+"tmp.xml");
		//write(file, document);
	}

	public static boolean write(File file, Document document) {
        // Transformerインスタンスの生成
        Transformer transformer;
        xmlReWriter xmlReWriter = new xmlReWriter();
        try {
             TransformerFactory transformerFactory = TransformerFactory.newInstance();
             transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
             e.printStackTrace();
             return false;
        }
        // Transformerの設定
        transformer.setOutputProperty("indent", "yes"); //改行指定
        transformer.setOutputProperty("omit-xml-declaration", "yes"); //XML宣言
        //transformer.setOutputProperty("encoding", "Shift_JIS"); // エンコーディング
        // XMLファイルの作成
        try {
             transformer.transform(new DOMSource(document), new StreamResult(file)); //tmpファイルの作成
             while(!file.exists()) {
                 System.out.println("waiting");
            	 System.out.print(".");
                 Thread.sleep(1000);
             }
             xmlReWriter.xmlRewrite(file.getName(), workDir);
             file.delete();
        } catch (TransformerException | InterruptedException e) {
             e.printStackTrace();
             return false;
        }
        return true;
   }
}
