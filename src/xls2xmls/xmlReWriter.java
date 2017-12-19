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
			String str = new String();
			while((str = br.readLine()) != null){
				if(str.contains("/>")){
					//System.out.print(str + " -> ");
					String replaceStr, forward, back;
					forward = str.replace("/", "");
					if(str.contains("type")){
						back = "</box>";
					} else {
						back = "</"+str.substring(1).replace("/>", "") + ">";
					}
					replaceStr = forward + back;
					//System.out.println(replaceStr);
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
