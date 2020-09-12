package com.lwq.hr.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:		 lewe
 * @date:		 2017年2月24日 下午07:03:28
 * @description: TODO(文件操作工具类)
 */
public class FileUtil {
	
	/** 日志记录器 */
	private static Logger logger = Logger.getLogger(FileUtil.class);
    /**	最大读取数
     * 	update by xiaoc.liu
     * */
	public static final long URL_MAX_INPUT_STREAM_LENGTH = 0xffff;//65535
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下午6:03:14
	 * @description: TODO(删除指定文件)
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			Boolean flag = file.getAbsoluteFile().delete();
			if (!flag) {
				System.gc();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("删除文件出错", e);
				}
				file.getAbsoluteFile().delete();
			}
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月17日 上午10:59:07
	 * @description: TODO（删除指定路径文件）
	 */
	/*public static void deleteFile(String path) {
		if (BaseUtil.isBlank(path)) {
			return;
		}
		File file = new File(BaseUtil.pathFilter(path));
		deleteFile(file);
	}*/
	
	/**
	 * 复制文件
	 * 
	 * @author:	lewe
	 * @date:	2018年4月26日 上午11:33:15
	 * 
	 * @param	fromFile	源文件
	 * @param	toFile		目标文件
	 */
	public static void copyFile(File fromFile, File toFile){
		FileInputStream fis = null;
		FileOutputStream fos =	null;
		try {
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			byte[] b = new byte[1024];
			int n = 0;
			while ((n = fis.read(b)) != -1) {
				fos.write(b, 0, n);
			}
		} catch (FileNotFoundException e) {
			logger.error("copyFile异常",e);
		} catch (IOException e) {
			logger.error("copyFile异常2",e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				logger.error("copyFile异常3",e);
			}
		}
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下去5:55:33
	 * @description: TODO(获取指定路径的文件分隔符)
	 */
	public static String getSeparator(String path) {
		if (BaseUtil.isBlank(path)) {
			return "";
		}
		String separator = "";
		if (path.charAt(0) == '/') { // 服务器为Aix、Linux等
			separator = "/";
		} else { // 服务器为Windows
			//separator = "\\";
			
			// 分隔符"/"应该是各系统都支持的，此处暂时统一使用
			separator = "/";
		}
		return separator;
	}
	
	/**
	 * @author:		 wwx
	 * @date:		 2018年4月17日 下午6:15:37
	 * @description: TODO(获取指定路径的文件存储目录)
	 */
	public static String getCatalog(String path) {
		if (BaseUtil.isBlank(path)) {
			return "";
		}
		String separator = "";
		if (path.charAt(0) == '/') { // 服务器为Aix、Linux等
			separator = "/";
		} else { // 服务器为Windows
			//separator = "\\";
			
			// 分隔符"/"应该是各系统都支持的，此处暂时统一使用
			separator = "/";
		}
		path = path.substring(0, path.lastIndexOf(separator));
		return path;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月6日 下去5:55:33
	 * @description: TODO(校验指定目录，不存在的目录会自动创建)
	 */
	/*public static boolean checkDirectory(String path) {
		if (BaseUtil.isBlank(path)) {
			return false;
		}
		File dir = new File(BaseUtil.pathFilter(path));
		if (!dir.exists()) {
			return dir.mkdirs();
		}
		return true;
	}*/
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年4月19日 上午10:19:41
	 * @description: TODO（保存文件到本地目录）
	 */
	/*public static String saveLocalFile(MultipartFile mpFile, String filePath, String fileName) throws Exception {
		// 保存上传文件到本地临时目录
    	String localFileName = filePath + FileUtil.getSeparator(filePath) + fileName;
    	File localFile = new File(BaseUtil.pathFilter(localFileName));
    	mpFile.transferTo(localFile);
    	return localFileName;
	}*/
	
	/**
	 * 读取文件二进制内容
	 * 
	 * @author:	lewe
	 * @date:	2018年4月12日 下午2:36:12
	 * 
	 * @param	fileName	文件名称（带路径）
	 * 
	 * @return	字节数组，文件不存在时返回 null
	 */
	public static byte[] readBytes(final String fileName) throws Exception {
		byte[] result = null;
		File file = new File(fileName);
		if (file.exists()) {
			result = new byte[(int) file.length()];
			int offset = 0, numRead = 0;
			InputStream is = null;
			try {
				is = new FileInputStream(file);
//				while (offset < result.length  && (numRead = is.read(result, offset, result.length - offset)) >= 0) {
//					offset += numRead;
//				}
				while (offset < result.length && result.length<URL_MAX_INPUT_STREAM_LENGTH && (numRead = is.read(result, offset, result.length - offset)) >= 0) {
					offset += numRead;
				}
			} catch (Exception e) {
				logger.error("readBytes异常",e);
			} finally {
				if (is != null) {
					is.close();
				}
			}
		} else {
			// 文件不存在，返回 null
		}
		return result;
	}
	
	/**
	 * 保存文件二进制内容
	 * 
	 * @author:	lewe
	 * @date:	2018年4月12日 下午2:39:32
	 * 
	 * @param	content		文件内容，字节数组
	 * @param	fileName	保存文件名称（带路径）
	 */
	public static void saveBytes(final byte[] content, final String fileName) throws Exception {
		File file = new File(fileName);
		BufferedOutputStream fos = null;
		FileOutputStream f = null;
		try {
			f = new FileOutputStream(file);
			fos = new BufferedOutputStream(f);
			fos.write(content, 0, content.length);
		} catch (Exception e) {
			logger.error("saveBytes异常",e);
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
				if (f != null) {
					f.close();
				}
			} catch (Exception e) {
				logger.error("saveBytes异常2",e);
			}
		}
	}
	
	/**
	 * 读取文件内容，转换为BASE64编码字符串
	 * 
	 * @author:	lewe
	 * @date:	2018年4月12日 下午2:43:05
	 * 
	 * @param	fileName	文件名称（带路径）
	 * 
	 * @return	BASE64字符串，文件不存在时返回空字符串
	 */
	public static String readBase64String(final String fileName) throws Exception {
		byte[] bytes = readBytes(fileName);
		if (bytes == null) {
			return "";
		}
		return Base64.encodeBase64String(bytes);
	}
	
	/**
	 * 转换BASE64编码字符串为字节数组，保存到指定文件
	 * 
	 * @author:	lewe
	 * @date:	2018年4月12日 下午2:48:28
	 * 
	 * @param	base64Str	文件内容，BASE64编码字符串
	 * @param	fileName	保存文件名称（带路径）
	 */
	public static void saveBase64String(final String base64Str, final String fileName) throws Exception {
		byte[] bytes = Base64.decodeBase64(base64Str);
		saveBytes(bytes, fileName);
	}
	
	/**
	 * 保存文件url为本地文件
	 * 
	 * @author:	lewe
	 * @date:	2018年4月16日 上午10:30:47
	 * 
	 * @param	fileUrl		文件url
	 * @param	fileName	保存文件名称（带路径）
	 */
	/*public static void saveFileFromUrl(String fileUrl, String fileName) throws Exception {
		if (BaseUtil.isBlank(fileUrl) || BaseUtil.isBlank(fileName)) {
			logger.warn("文件url或文件名为空，保存文件失败！");
			return;
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			// 创建url，打开连接
			URL url = new URL(fileUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(HttpUtil.CONNECT_TIME_OUT);
			// 获取输入流，读取字节数据，输出到文件
			is = conn.getInputStream();
			File sf = new File(BaseUtil.pathFilter(fileName));
			os = new FileOutputStream(sf);
			int len;
			byte[] bs = new byte[1024];
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			logger.error("saveFileFromUrl异常2",e);
		} finally {
			if (os != null) {
				// 关闭所有链接
				os.flush();
				os.close();
			}
			if (is != null) {
				// 关闭所有链接
				is.close();
			}
		}
	}*/
}
