package org.tain.jar.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.tain.utils.ClassUtils;

public class JarReadTestMain {

	private static final boolean flag;
	
	static {
		flag = true;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getFileLine());
		
		if (flag) test01(args);
		if (flag) test02(args);
		if (flag) test03(args);
	}
	
	///////////////////////////////////////////////////////////////////////////

	private static void test01(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getFileLine());
		
		if (!flag) {
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("/data/03.MidPattern.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (flag) System.out.println(">>>>> " + line);
			}
			inputStream.close();
		}
		
		if (!flag) {
			// files in MidPattern01.jar
			InputStream inputStream = JarReadTestMain.class.getResourceAsStream("/data/03.MidPattern.txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (flag) System.out.println(">>>>> " + line);
			}
			inputStream.close();
		}
		
		if (!flag) {
			//URL url = new URL("jar:file:C:\\hanwha\\GIT\\git\\KieaJar\\ver01\\KieaJar01\\lib\\MidPattern01.jar!/data/03.MidPattern.txt");
			//URL url = new URL("jar:file:/hanwha/GIT/git/KieaJar/ver01/KieaJar01/lib/MidPattern01.jar!/data/03.MidPattern.txt");
			URL url = new URL("jar:file:./lib/MidPattern01.jar!/data/03.MidPattern.txt");
			//URL url = new URL("jar:file:./lib/MidPattern01.jar!/lib/gson-2.7.jar!/META-INF/MANIFEST.MF");  // TODO KANG20181023: java.io.FileNotFoundException: JAR entry lib/gson-2.7.jar!/META-INF/MANIFEST.MF not found in .\lib\MidPattern01.jar
			InputStream inputStream = url.openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (flag) System.out.println(">>>>> " + line);
			}
			inputStream.close();
		}
		
		if (!flag) {
			URL jarUrl = new URL("jar:file:./lib/MidPattern01.jar!/data/03.MidPattern.txt");
			JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
			URL url = jarConnection.getJarFileURL();
			if (flag) System.out.println(">>>>> jarUrl: " + jarUrl.getFile());
			if (flag) System.out.println(">>>>> url: " + url.getFile());
		}
		
		if (!flag) {
			//URL url = new URL("jar:file:./lib/MidPattern01.jar!/data/03.MidPattern.txt");
			File file = new File(new URL("file:/hanwha/GIT/git/KieaJar/ver01/KieaJar01/lib/MidPattern01.jar").toURI());
			if (flag) System.out.println(">>>>> file: " + file.getPath());
		}
		
		if (!flag) {
			// try again....
			ClassLoader loader = new Object() {}.getClass().getClassLoader();
			URL url = loader.getResource("resource name");
			String[] filePath = null;
			String protocol = url.getProtocol();
			if (protocol.equals("jar")) {
				url = new URL(url.getPath());
				protocol = url.getProtocol();
			}
			if (protocol.equals("file")) {
				String[] pathArray = url.getPath().split("!");
				filePath = pathArray[0].split("/", 2);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "resource" })
	private static void test02(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getFileLine());
		
		if (!flag) {
			// JarFile jar = new JarFile("");
			//ZipFile file = new ZipFile("file:./lib/MidPattern01.jar");
			ZipFile file = new ZipFile("./lib/MidPattern01.jar");
			//ZipFile file = new ZipFile("file:./lib/MidPattern01.jar");
			if (file != null) {
				Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) file.entries();  // get entries from the zip file...
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					// use the entry to see if it's the file '1.txt'
					// Read from the byte using file.getInputStream(entry)
					if (flag) System.out.println(">>>>> entry: " + entry.getName());
				}
			}
		}
		
		if (!flag) {
			JarFile jarFile = new JarFile("./lib/MidPattern01.jar");
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (flag) System.out.println(">>>>> entry: " + entry.getName());
			}
		}
		
		if (!flag) {
			String path = "/resources/html/custom.css";
			BufferedReader buffer = new BufferedReader(new InputStreamReader(new Object() {}.getClass().getResourceAsStream(path)));
			String ret = buffer.lines().collect(Collectors.joining(System.getProperty("line.seperator")));
		}
	}
	
	private static void test03(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + ClassUtils.getFileLine());
		
		if (!flag) {
			//File file = new File(new URL("file:/hanwha/GIT/git/KieaJar/ver01/KieaJar01/lib/MidPattern01.jar").toURI());
			//File file = new File(new Object() {}.getClass().getResource("/data/03.MidPattern.txt").toURI());
			//File file = new File(JarReadTestMain.class.getResource("/data/03.MidPattern.txt").toURI());
			//File file = new File(JarReadTestMain.class.getResource("./lib/MidPattern01.jar").toURI());
			File file = new File(JarReadTestMain.class.getResource("file:./lib/MidPattern01.jar!/data/03.MidPattern.txt").toURI());
			if (file.isDirectory()) {
				System.out.println(">>>>> directory.");
			} else {
				System.out.println(">>>>> not directory");
			}
		}
		
		if (!flag) {
			ClassLoader classLoader = new Object() {}.getClass().getClassLoader();
			//File file = new File(classLoader.getResource("data/03.MidPattern.txt").getFile());
			//File file = new File(classLoader.getResource("03.MidPattern.txt").getFile());
			File file = new File(classLoader.getResource("data/").getFile());
			if (file.isDirectory()) {
				System.out.println(">>>>> is directory...");
			} else if (file.isFile()) {
				System.out.println(">>>>> is file...");
			} else {
				System.out.println(">>>>> not file...");
			}
		}
		
		if (flag) {
			//ClassPathResource resource = new ClassPathResource("filename.txt");
			
		}
	}
}
