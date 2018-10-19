package org.tain.jar.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JarReadTestMain {

	private static final boolean flag;
	
	static {
		flag = true;
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws Exception {
		
		if (flag) test01(args);
		if (flag) test02(args);
	}
	
	private static void test01(String[] args) throws Exception {
		InputStream inputStream = JarReadTestMain.class.getResourceAsStream("/data/03.MidPattern.txt");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			if (flag) System.out.println(">>>>> " + line);
		}
		inputStream.close();
	}
	
	private static void test02(String[] args) throws Exception {
		//ClassPathResource resource = new ClassPathResource("03.MidPattern.txt");
	}
}
