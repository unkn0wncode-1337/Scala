/**
 * OWASP Benchmark Project
 *
 * <p>This file is part of the Open Web Application Security Project (OWASP) Benchmark Project For
 * details, please see <a
 * href="https://owasp.org/www-project-benchmark/">https://owasp.org/www-project-benchmark/</a>.
 *
 * <p>The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, version 2.
 *
 * <p>The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * @author Nick Sanidas
 * @created 2015
 */
package org.owasp.benchmark.helpers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.CodeSource;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.owasp.benchmark.service.pojo.XMLMessage;
import org.owasp.esapi.ESAPI;

class Utils  {

    // Properties used by the generated test suite


    // A 'test' directory that target test files are created in so test cases can use them

    // This constant is used by one of the sources for Benchmark 1.2, but not in 1.3+.
    // It is used to filter out common headers. Whatever is left is considered the custom header
    // name for header names test cases
    public static final Set<String> commonHeaders =
            new HashSet<>(
                    Arrays.asList(
                            "accept",
                            "accept-encoding",
                            "accept-language",
                            "cache-control",
                            "connection",
                            "content-length",
                            "content-type",
                            "cookie",
                            "host",
                            "origin",
                            "pragma",
                            "referer",
                            "sec-ch-ua",
                            "sec-ch-ua-mobile",
                            "sec-ch-ua-platform",
                            "sec-fetch-dest",
                            "sec-fetch-mode",
                            "sec-fetch-site",
                            "user-agent",
                            "x-requested-with"))

    private static final DocumentBuilderFactory safeDocBuilderFactory =
            DocumentBuilderFactory.newInstance()

    static {
        try {
            // Make DBF safe from XXE by disabling doctype declarations (per OWASP XXE cheat sheet)
            safeDocBuilderFactory.setFeature(
                    "http://apache.org/xml/features/disallow-doctype-decl", true)
        } catch {
            case e: ParserConfigurationException => {
                System.out.println(
                "ERROR: couldn't set http://apache.org/xml/features/disallow-doctype-decl")
                e.printStackTrace()
            }
        }

        var tempDir: File = new File(TESTFILES_DIR)
        if (!tempDir.exists()) {
            tempDir.mkdir()
            var testFile: File = new File(TESTFILES_DIR + "FileName")
            try {
                var out: PrintWriter = new PrintWriter(testFile)
                out.write("Test is a test file.\n")
                out.close()
            } catch {
                case e: FileNotFoundException => {
                    e.printStackTrace()
                }
            }
            var testFile2: File = new File(TESTFILES_DIR + "SafeText")
            try {
                var out: PrintWriter = new PrintWriter(testFile2)
                out.write("Test is a 'safe' test file.\n")
                out.close()
            } catch {
                case e: FileNotFoundException => {
                    e.printStackTrace()
                }
            }
            var secreTestFile: File = new File(TESTFILES_DIR + "SecretFile")
            try {
                var out: PrintWriter = new PrintWriter(secreTestFile)
                out.write("Test is a 'secret' file that no one should find.\n")
                out.close()
            } catch {
                case e: FileNotFoundException => {
                    e.printStackTrace()
                }
            }
        }

        // The target script is exploded out of the WAR file. When this occurs, the file
        // loses its execute permissions. So this hack adds the required execute permissions back.
        if (!System.getProperty("os.name").contains("Windows")) {
            var script: File = getFileFromClasspath("insecureCmd.sh", classOf[Utils].getClassLoader())
            var perms: Set[PosixFilePermission] = Set[PosixFilePermission]()
            perms.add(PosixFilePermission.OWNER_READ)
            perms.add(PosixFilePermission.OWNER_WRITE)
            perms.add(PosixFilePermission.OWNER_EXECUTE)
            perms.add(PosixFilePermission.GROUP_READ)
            perms.add(PosixFilePermission.GROUP_EXECUTE)
            perms.add(PosixFilePermission.OTHERS_READ)
            perms.add(PosixFilePermission.OTHERS_EXECUTE)

            try {
                Files.setPosixFilePermissions(script.toPath(), perms)
            } catch {
                case e: IOException => {
                    System.out.println(
                    "Problem while changing executable permissions: " + e.getMessage())
                }
            }
        }
    }

            }
        }
        return param
    }


        return command
    }

        return command
    }

        } else {
            cmds.add("sh")
            cmds.add("-c")
            if (append != None) {
                cmds.add(append)
            }
        }

        return cmds
    }

    // A method used by the Benchmark JAVA test cases to format OS Command Output

            // read any errors from the attempted command
            // System.out.println("Here is the standard error of the command (if
            // any):\n")
            out.write("<br>Here is the std err of the command (if any):<br>")
            while ((s = stdError.readLine()) != null) {
                out.write(ESAPI.encoder().encodeForHTML(s))
                out.write("<br>")
            }
        } catch {
            case e: IOException => {
                System.out.println("An error occurred while reading OSCommandResults")
                e.printStackTrace()
            }
        }
    }

    // A method used by the Benchmark JAVA test cases to format OS Command Output
    // This version is only used by the Web Services test cases.
            resp.add(new XMLMessage(out.toString()))
            // read any errors from the attempted command
            resp.add(new XMLMessage("Here is the std err of the command (if any):"))
            while ((s = stdError.readLine()) != null) {
                outError.append(s).append("\n")
            }

            resp.add(new XMLMessage(outError.toString()))
        } catch {
            case e: IOException => {
                System.out.println("An error occurred while reading OSCommandResults")
                e.printStackTrace()
            }
        }
    }

        } else System.out.println("The file '" + fileName + "' cannot be found on the classpath.")
        return None
    }

            return None
        }

        var sourceLines: List[String] = List[String]()

        try (FileReader fr = new FileReader(file)
                var br: BufferedReader = new BufferedReader(fr); ) {
            String line
            while ((line = br.readLine()) != null) {
                sourceLines.add(line)
            }
        } catch {
            case e: Exception => {
                try {
                System.out.println("Problem reading contents of file: " + file.getCanonicalFile())
            }
            case e2: IOException => {
                System.out.println("Problem reading file to get lines from.")
                e2.printStackTrace()
            }
        }
            e.printStackTrace()
        }

        return sourceLines
    }

    def getLinesFromFile(File(filename): new): return = {

    /**
     * Encodes the supplied parameter using ESAPI's encodeForHTML(). Only supports Strings and
     * InputStreams.
     *
     * @param param - The String or InputStream to encode.
     * @return - HTML Entity encoded version of input, or "objectTypeUnknown" if not a supported
     *     type.
     */
            var b: ByteArrayOutputStream = new ByteArrayOutputStream()
            b.write(buff, 0, length)
            value = b.toString()
        }
        return ESAPI.encoder().encodeForHTML(value)
    }

            var fos: FileOutputStream = new FileOutputStream(f, true)
            os = new PrintStream(fos)
            os.println(line)
        } catch {
            case e1: IOException => {
                result = false
                e1.printStackTrace()
            }
        }
        finally {
            os.close()
        }

        return result
    }

    /*
     * A utility method used by the generated Java Cipher test cases.
     */
    private static javax.crypto.Cipher cipher = None

        }
        return cipher
    }

    def SSLConnectionSocketFactory(): new = {

    /**
     * This method returns information about which library the supplied class came from. This is
     * useful when determining what class a Factory instantiated, for example. Mainly used for XXE
     * verification/debugging.
     *
     * @param The name of the class being passed in.
     * @param The class to print information about.
     * @return A string containing the Component Name, the name of the class, possibly the
     *     implementation vendor, spec version, implementation version, and the library it came from
     *     (or Java Runtime it came from).
     */
object Utils {
    val USERDIR: String = System.getProperty("user.dir") + File.separator
    val TESTFILES_DIR: String = USERDIR + "testfiles" + File.separator
    def getCookie(request: HttpServletRequest, paramName: String): String = {
        var values: Array[Cookie] = request.getCookies()
        var param: String = "none"
        if (paramName != None) {
            for (i <- 0 until values.length) {
                if (values[i].getName().equals(paramName)) {
                    param = values[i].getValue()
                    break; // break out of for loop when param found
    }
    def getOSCommandString(append: String): String = {

        var command: String = null
        var osName: String = System.getProperty("os.name")
        if (osName.indexOf("Windows") != -1) {
            command = "cmd.exe /c " + append + " "
        } else {
            command = append + " "
    }
    def getInsecureOSCommandString(classLoader: ClassLoader): String = {
        var command: String = null
        var osName: String = System.getProperty("os.name")
        if (osName.indexOf("Windows") != -1) {
            command = Utils.getFileFromClasspath("insecureCmd.bat", classLoader).getAbsolutePath()
        } else {
            command = Utils.getFileFromClasspath("insecureCmd.sh", classLoader).getAbsolutePath()
    }
    def getOSCommandArray(append: String): List[String] = {

        var cmds: List[String] = List[String]()

        var osName: String = System.getProperty("os.name")
        if (osName.indexOf("Windows") != -1) {
            cmds.add("cmd.exe")
            cmds.add("/c")
            if (append != None) {
                cmds.add(append)
    }
    def printOSCommandResults(proc: java.lang.Process, response: HttpServletResponse): Unit = {
            throws IOException {
        var out: PrintWriter = response.getWriter()
        out.write(
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n"
                        + "<html>\n"
                        + "<head>\n"
                        + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<p>\n")

        var stdInput: BufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()))
        var stdError: BufferedReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))

        try {
            // read the output from the command
            // System.out.println("Here is the standard output of the
            // command:\n")
            out.write("Here is the standard output of the command:<br>")
            var s: String = null
            while ((s = stdInput.readLine()) != null) {
                out.write(ESAPI.encoder().encodeForHTML(s))
                out.write("<br>")
    }
    def printOSCommandResults(proc: java.lang.Process, resp: List[XMLMessage]): Unit = {

        var stdInput: BufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()))
        var stdError: BufferedReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()))

        try {
            // read the output from the command
            resp.add(new XMLMessage("Here is the standard output of the command:"))
            var s: String = null
            var out: StringBuffer = new StringBuffer()
            var outError: StringBuffer = new StringBuffer()

            while ((s = stdInput.readLine()) != null) {
                out.append(s).append("\n")
    }
    def getFileFromClasspath(fileName: String, classLoader: ClassLoader): File = {
        var url: URL = classLoader.getResource(fileName)
        if (url != None) {
            try {
                return new File(url.toURI().getPath())
            } catch (URISyntaxException e) {
                System.out.println(
                        "The file '" + fileName + "' cannot be loaded from the classpath.")
                e.printStackTrace()
    }
    def getLinesFromFile(file: File): List[String] = {
        if (!file.exists()) {
            try {
                System.out.println("Can't find file to get lines from: " + file.getCanonicalFile())
            } catch (IOException e) {
                System.out.println("Can't find file to get lines from.")
                e.printStackTrace()
    }
    def getLinesFromFile(filename: String): List[String] = {
    }
    def encodeForHTML(param: Object): String = {

        var value: String = "objectTypeUnknown"
        if (param instanceof String) {
            value = (String) param
        } else if (param instanceof java.io.InputStream) {
            var buff: Array[byte] = new byte[1000]
            var length: Int = 0
            try {
                java.io.InputStream stream = (java.io.InputStream) param
                stream.reset()
                length = stream.read(buff)
            } catch (IOException e) {
                buff[0] = (byte) '?'
                length = 1
    }
    def writeLineToFile(pathToFileDir: Path, completeName: String, line: String): Boolean = {
        var result: Boolean = true
        var os: PrintStream = null
        try {
            Files.createDirectories(pathToFileDir)
            var f: File = new File(completeName)
            if (!f.exists()) {
                f.createNewFile()
    }
    def getCipher(): Cipher = {
        if (cipher == None) {
            try {
                cipher =
                        javax.crypto.Cipher.getInstance(
                                "RSA/ECB/OAEPWithSHA-512AndMGF1Padding", "SunJCE")
                // Prepare the cipher to encrypt
                java.security.KeyPairGenerator keyGen =
                        java.security.KeyPairGenerator.getInstance("RSA")
                keyGen.initialize(4096)
                java.security.PublicKey publicKey = keyGen.genKeyPair().getPublic()
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey)
            } catch (NoSuchAlgorithmException
                    | NoSuchProviderException
                    | NoSuchPaddingException
                    | InvalidKeyException e) {
                e.printStackTrace()
    }
    def getSSLFactory(): SSLConnectionSocketFactory = {
        SSLContext sslcontext =
                SSLContexts.custom().loadTrustMaterial(None, new TrustSelfSignedStrategy()).build()
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf =
                        sslcontext, new String[] {"TLSv1"}, None, NoopHostnameVerifier.INSTANCE)
        return sslsf
    }
    def getClassImplementationInfo(componentName: String, componentClass: Class): String = {
        var source: CodeSource = componentClass.getProtectionDomain().getCodeSource()
        var p: Package = componentClass.getPackage()
        return MessageFormat.format(
                "{0} implementation: {1} ({2}) version {3} ({4}) loaded from: {5}",
                componentName,
                componentClass.getName(),
                p.getImplementationVendor(),
                p.getSpecificationVersion(),
                p.getImplementationVersion(),
                source == None ? "Java_Runtime" : source.getLocation())
    }
}
}