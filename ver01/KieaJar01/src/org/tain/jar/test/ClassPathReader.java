package org.tain.jar.test;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * URL: http://rick-hightower.blogspot.com/2013/10/classpath-resource-reader.html
 * 
 * 
 * Prototype resource reader.
 * This prototype is devoid of error checking.
 *
 * 
 * I have two prototype jar files that I have setup.
 * <pre> *             <dependency>
 *                  <groupid>invoke</groupid>
 *                  <artifactid>invoke</artifactid>
 *                  <version>1.0-SNAPSHOT</version>
 *              </dependency>
 *
 *              <dependency>
 *                   <groupid>node</groupid>
 *                   <artifactid>node</artifactid>
 *                   <version>1.0-SNAPSHOT</version>
 *              </dependency>
 * </pre>
 * 
 * The jar files each have a file under /org/node/ called resource.txt.
 * 
 * This is just a prototype of what a handler would look like with classpath://
 * I also have a resource.foo.txt in my local resources for this project.
 */
public class ClassPathReader {

    public static void main(String[] args) throws Exception {
    	 
        String namespace = "resource";
 
        //someResource is classpath.
        String someResource = args.length > 0 ? args[0] :
                //"classpath:///org/node/resource.txt";   It works with files
                "classpath:///org/node/";                 //It also works with directories
 
        URI someResourceURI = URI.create(someResource);
 
        System.out.println("URI of resource = " + someResourceURI);
 
        someResource = someResourceURI.getPath();
 
        System.out.println("PATH of resource =" + someResource);
 
        boolean isDir = !someResource.endsWith(".txt");
 
 
        // Classpath resource can never really start with a starting slash.
        //   Logically they do, but in reality you have to strip it.
        //   This is a known behavior of classpath resources.
        //   It works with a slash unless the resource is in a jar file.
        //   Bottom line, by stripping it, it always works.
 
        if (someResource.startsWith("/")) {
            someResource = someResource.substring(1);
        }
 
        // Use the ClassLoader to lookup all resources that have this name.
        // Look for all resources that match the location we are looking for.
        Enumeration<URL> resources =  null;
 
        // Check the context classloader first. Always use this if available.
        try {
            resources =    Thread.currentThread().getContextClassLoader().getResources(someResource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
        if (resources==null || !resources.hasMoreElements()) {
            resources = ClassPathReader.class.getClassLoader().getResources(someResource);
        }
 
        //Now iterate over the URLs of the resources from the classpath
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
 
 
            // if the resource is a file, it just means that we can use normal mechnism
            // to scan the directory.
  
            if (resource.getProtocol().equals("file")) {
                //if it is a file then we can handle it the normal way.
                handleFile(resource, namespace);
                continue;
            }
 
            System.out.println("Resource " + resource);
            //
            //  Split up the string that looks like this:
            //  jar:file:/Users/rick/.m2/repository/invoke/invoke/1.0-SNAPSHOT/invoke-1.0-SNAPSHOT.jar!/org/node/
            //  into
            //     this /Users/rick/.m2/repository/invoke/invoke/1.0-SNAPSHOT/invoke-1.0-SNAPSHOT.jar
            //   and this
            //          /org/node/
            
            String[] split = resource.toString().split(":");
            String[] split2 = split[2].split("!");
            String zipFileName = split2[0];
            String sresource = split2[1];
 
            System.out.printf("After split zip file name = %s," +
                    " \nresource in zip %s \n", zipFileName, sresource);
 
 
            // Open up the zip file. 
            ZipFile zipFile = new ZipFile(zipFileName);
 
 
            //  Iterate through the entries.  
            Enumeration entries = zipFile.entries();
 
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // If it is a directory, then skip it.
                if (entry.isDirectory()) {
                    continue;
                }
 
                String entryName = entry.getName();
                System.out.printf("zip entry name %s \n", entryName);
 
                // If it does not start with our someResource String
                // then it is not our resource so continue.
                if (!entryName.startsWith(someResource)) {
                    continue;
                }
 
 
                // the fileName part from the entry name.
                // where /foo/bar/foo/bee/bar.txt, bar.txt is the file
                //
                String fileName = entryName.substring(entryName.lastIndexOf("/") + 1);
                System.out.printf("fileName %s \n", fileName);
 
                // See if the file starts with our namespace and ends with our extension.
                if (fileName.startsWith(namespace) && fileName.endsWith(".txt")) {
 
                    // If you found the file, print out the contents of the file to System.out.
                    try (Reader reader = new InputStreamReader(zipFile.getInputStream(entry))) {
                        StringBuilder builder = new StringBuilder();
                        int ch = 0;
                        while ((ch = reader.read()) != -1) {
                            builder.append((char) ch);
 
                        }
                        System.out.printf("zip fileName = %s\n\n####\n contents of file %s\n###\n", entryName, builder);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
 
            }
 
        }
 
 
    }
 
    /** The file was on the file system not a zip file,
     * this is here for completeness for this example.
     * otherwise.
     * @param resource
     * @param namespace
     * @throws Exception
     */
    private static void handleFile(URL resource, String namespace) throws Exception {
        System.out.println("Handle this resource as a file " + resource);
        URI uri = resource.toURI();
        File file = new File(uri.getPath());
 
 
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                if (childFile.isDirectory()) {
                    continue;
                }
                String fileName = childFile.getName();
                if (fileName.startsWith(namespace) && fileName.endsWith("txt")) {
 
                    try (FileReader reader = new FileReader(childFile)) {
                        StringBuilder builder = new StringBuilder();
                        int ch = 0;
                        while ((ch = reader.read()) != -1) {
                            builder.append((char) ch);
 
                        }
                        System.out.printf("fileName = %s\n\n####\n contents of file %s\n###\n", childFile, builder);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
 
                }
 
            }
        } else {
            String fileName = file.getName();
            if (fileName.startsWith(namespace) && fileName.endsWith("txt")) {
 
                try (FileReader reader = new FileReader(file)) {
                    StringBuilder builder = new StringBuilder();
                    int ch = 0;
                    while ((ch = reader.read()) != -1) {
                        builder.append((char) ch);
 
                    }
                    System.out.printf("fileName = %s\n\n####\n contents of file %s\n###\n", fileName, builder);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
 
            }
 
        }
    }
 
}
