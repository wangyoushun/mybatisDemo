package com.six.mydb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 
 * @ClassName: FileTool
 * @Description: 提供一些文件操作工具类
 * @author iwantfly
 * @date 2017年7月8日 下午4:18:17
 *
 */
public class FileTool {

	public static final String ENCODE_GBK = "GBK";
	public static final String ENCODE_UTF8 = "UTF-8";

	/**
	 * 以列表的方式获取文件的所有行
	 *
	 * @param file
	 *            需要出来的文件
	 * @return 包含所有行的list
	 */
	public static List<String> lines(File file) {

		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 以列表的方式获取文件的所有行
	 *
	 * @param file
	 *            需要处理的文件
	 * @param encoding
	 *            指定读取文件的编码
	 * @return 包含所有行的list
	 */
	public static List<String> lines(File file, String encoding) {
		List<String> list = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))) {
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 以列表的方式获取文件的所有行  读大文件
	 *
	 * @param file
	 *            需要处理的文件
	 * @return 包含所有行的list
	 * 种方案将会遍历文件中的所有行——允许对每一行进行处理，而不保持对它的引用。总之没有把它们存放在内存中
	 */
	public static List<String> linesBigFile(File file) {
		List<String> list = new ArrayList<>();

		try {
			FileInputStream inputStream = null;
			Scanner sc = null;
			try {
				inputStream = new FileInputStream(file);
				sc = new Scanner(inputStream, "UTF-8");
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					list.add(line);
					// System.out.println(line);
				}
				// note that Scanner suppresses exceptions
				if (sc.ioException() != null) {
					throw sc.ioException();
				}
			} finally {
				if (inputStream != null) {
					inputStream.close();
				}
				if (sc != null) {
					sc.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 将字符串以追加的方式写入到文件中
	 */
	public static boolean writeAppend(File file, String str) {
		try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			if (fileLength > 0) {
				randomFile.writeBytes(System.getProperty("line.separator"));
			}
			randomFile.writeBytes(str);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将字符串以追加的方式写入到文件中
	 */
	public static boolean writeAppend(File file, List<String> strList) {
		try (RandomAccessFile randomFile = new RandomAccessFile(file, "rw")) {
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			if (fileLength > 0) {
				randomFile.writeBytes(System.getProperty("line.separator"));
			}

			for (String str : strList) {
				randomFile.write(str.getBytes("UTF-8"));
				randomFile.writeBytes(System.getProperty("line.separator"));
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param s
	 * @param t
	 */
	public static void fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 遍历文件夹
	public static void copyFile(File file, String destPath) {
		if (file.isDirectory()) {
			System.out.println("==mulu==" + file.getName());
			File file2 = new File(destPath);
			if (!file2.exists()) {
				file2.mkdirs();
				System.out.println("==create mulu==" + destPath);
			}
			File[] listFiles = file.listFiles();
			for (File f : listFiles) {
				copyFile(f, destPath + "/" + f.getName());
			}
		} else {
			File file2 = new File(destPath);
			if (file2.exists() && file2.getName().equals(file.getName())) {
				System.out.println(file.getName() + "====exist");
				return;
			}
			System.out.println("==file==" + file.getName() + "----" + file.getAbsolutePath());
			fileChannelCopy(file, new File(destPath));
		}
	}

	/**
	 * 
	 * @Title: jsonStrToJavaJsonStr @Description: 将json串转化为 java中的字符串 @param
	 *         file @return String 返回类型 @throws
	 */
	public static String jsonStrToJavaJsonStr(File file) {
		List<String> lines = lines(file, ENCODE_GBK);
		String jsonStr = "";
		for (String str : lines) {
			str = str.replace("\"", "\\\"");
			jsonStr += str.trim();
		}
		return jsonStr;
	}

	/**
	 * 删除目录，并判断执行删除操作后目录是否存在
	 * 
	 * @param folder
	 * @return
	 * @Date 2017年7月24日15:04:14
	 */
	public static boolean deleteFolder(File folder) {
		return deleteFolderContents(folder) && folder.delete();
	}

	/**
	 * 
	 * @param folder
	 * @return
	 */
	public static boolean deleteFolderContents(File folder) {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!file.delete()) {
					return false;
				}
			} else {
				if (!deleteFolder(file)) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		List<String> lines = lines(new File("D://tmp//sql.txt"));
		System.out.println(lines);
		System.out.println();
		List<String> linesBigFile = linesBigFile(new File("D://tmp//sql.txt"));
		System.out.println(linesBigFile);
	}
}
