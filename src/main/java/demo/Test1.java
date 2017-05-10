package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Test1 {
	public static void main(String[] args) {
		String dir = "E:\\myproj\\xbynetwiki\\pages";
		File file = new File(dir);
		Collection<File> flist = FileUtils.listFiles(file, null, true);

		for (File f : flist) {
			try {
				List<String> strlist=IOUtils.readLines(new FileInputStream(f),"UTF-8");
				List<String> destlist=new ArrayList<String>(strlist.size());
				for(String str:strlist){
//					destlist.add(str+"   ");
					if(str.startsWith("```")){
						destlist.add(" ");
						destlist.add(str);
						destlist.add(" ");
					}else{
						destlist.add(str);
					}
				}
				IOUtils.writeLines(destlist, "\n", new FileOutputStream(f),"UTF-8");
				System.out.println("文件"+f.getAbsolutePath()+"处理完毕");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
