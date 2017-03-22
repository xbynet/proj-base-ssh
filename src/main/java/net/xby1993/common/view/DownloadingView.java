package net.xby1993.common.view;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class DownloadingView implements View {
	private final File dfile;
	private final String contentType;

	public DownloadingView(File dfile, String contentType) {
		this.dfile = dfile;
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = dfile.getName();
		// 避免下载时文件名乱码
		fileName = new String(fileName.getBytes(), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		response.setContentType("application/octet-stream");
		response.setContentLength((int) dfile.length());
		// 得到输入流
		FileInputStream in = new FileInputStream(dfile);
		BufferedInputStream bufIn = new BufferedInputStream(in);
		ServletOutputStream out = response.getOutputStream();
		// 下面是一个普通的流的复制 。。。忽略 .这样可以防止内存问题
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = bufIn.read(bs)) != -1) {
			out.write(bs);
		}
		// 最后是流的关闭。
		out.flush();
		bufIn.close();
		in.close();
	}
}