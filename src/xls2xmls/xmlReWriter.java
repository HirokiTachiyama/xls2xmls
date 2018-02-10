package xls2xmls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class xmlReWriter {

	public void xmlRewrite(String tmpFileName, String path){
		File tmpFile = new File(path + tmpFileName);
		File rewriteFile = new File(path + tmpFileName.replace("tmp", ""));
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmpFile));
			
			if(rewriteFile.createNewFile()){
				System.out.println(rewriteFile.getName()+"の作成に成功");
			} else {
				System.out.println(rewriteFile.getName()+"の作成に失敗");
			}
			FileWriter fw = new FileWriter(rewriteFile);
			String str;
			while((str = br.readLine()) != null){
				if(str.contains("/>")){
					String replaceStr, forward, back;
					forward = str.replace("/", "");
					if(str.contains("box type=")){
						back = "</box>";
					} else {
						int squareBracketIndex = str.indexOf("<");
						back = "</"+str.substring(squareBracketIndex + 1); //<の除去

						back = back.replace("/>", "") + ">"; // />と>とを取り替え
					}
					replaceStr = forward + back;
					fw.write(replaceStr+xls2xml.getCrlf());
				} else {
					fw.write(str+xls2xml.getCrlf());
				}
			}
			fw.close();
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
