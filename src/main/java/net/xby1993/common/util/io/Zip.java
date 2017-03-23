package net.xby1993.common.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Zip {
	static final int BUFFER = 2048;

	public static void zip(String fileName, String filePath) {
		try {
			ZipFile zipFile = new ZipFile(fileName);
			Enumeration emu = zipFile.entries();
			int i = 1;
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {

					new File(filePath + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile
						.getInputStream(entry));
				File file = new File(filePath + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
				i++;
				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ZipFiles(String file, String savepath) {
		try {
			File inFile = new File(file);
			 
			FileOutputStream fout = new FileOutputStream(savepath);
			 
			// 使用输出流检查
			CheckedOutputStream cs = new CheckedOutputStream(fout,new Adler32());
			 
			// 声明输出zip流
			ZipOutputStream zout = new ZipOutputStream(
					new BufferedOutputStream(cs));
			// 写一个注释
			zout.setComment("This is the comment");
			zips(zout, inFile, "", cs);
			zout.close();
			 
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	public void zips(ZipOutputStream out, File inFile, String root,
			CheckedOutputStream cs) throws Exception {
		if (inFile.isDirectory()) {
			File[] files = inFile.listFiles();
			out.putNextEntry(new ZipEntry(root + "/"));
			root = root.length() == 0 ? "" : root + "/";
			for (int i = 0; i < files.length; i++) {
				zips(out, files[i], root + files[i].getName(), cs);
			}
		} else {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(inFile));
			out.putNextEntry(new ZipEntry(root));
			int c;
			while ((c = in.read()) != -1)
				out.write(c);
			in.close();
			

		}
	}

}
