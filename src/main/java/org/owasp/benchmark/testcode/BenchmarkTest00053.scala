/**
 * OWASP Benchmark Project v1.2
 *
 * <p>This file is part of the Open Web Application Security Project (OWASP) Benchmark Project. For
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
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/crypto-00/BenchmarkTest00053")
class BenchmarkTest00053 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00053", "someSecret")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/crypto-00/BenchmarkTest00053.html")
        rd.include(request, response)
    }

    @Override
    def doPost(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")

        javax.servlet.http.Cookie[] theCookies = request.getCookies()

        var param: String = "noCookieValueSupplied"
        if (theCookies != None) {
            for (theCookie <- theCookies) {
                if (theCookie.getName().equals("BenchmarkTest00053")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        String bar

        // Simple ? condition that assigns constant to bar on true condition
        var num: Int = 106

        bar = (7 * 18) + num > 200 ? "This_should_always_happen" : param

        // Code based on example from:
        // http://examples.javacodegeeks.com/core-java/crypto/encrypt-decrypt-file-stream-with-des/
        // 8-byte initialization vector
        //	    byte[] iv = {
        //	    	(byte)0xB2, (byte)0x12, (byte)0xD5, (byte)0xB2,
        //	    	(byte)0x44, (byte)0x21, (byte)0xC3, (byte)0xC3033
        //	    }
        java.security.SecureRandom random = new java.security.SecureRandom()
        var iv: Array[byte] = random.generateSeed(8); // DES requires 8 byte keys

        try {
            javax.crypto.Cipher c =
                    javax.crypto.Cipher.getInstance(
                            "DES/CBC/PKCS5PADDING", java.security.Security.getProvider("SunJCE"))

            // Prepare the cipher to encrypt
            javax.crypto.SecretKey key = javax.crypto.KeyGenerator.getInstance("DES").generateKey()
            java.security.spec.AlgorithmParameterSpec paramSpec =
                    new javax.crypto.spec.IvParameterSpec(iv)
            c.init(javax.crypto.Cipher.ENCRYPT_MODE, key, paramSpec)

            // encrypt and store the results
            var input: Array[byte] = {(byte) '?'}
            var inputParam: Object = bar
            if (inputParam instanceof String) input = ((String) inputParam).getBytes()
            if (inputParam instanceof java.io.InputStream) {
                var strInput: Array[byte] = new byte[1000]
                var i: Int = ((java.io.InputStream) inputParam).read(strInput)
                if (i == -1) {
                    response.getWriter()
                            .println(
                                    "This input source requires a POST, not a GET. Incompatible UI for the InputStream source.")
                    return
                }
                input = java.util.Arrays.copyOf(strInput, i)
            }
            var result: Array[byte] = c.doFinal(input)

            java.io.File fileTarget =
                    new java.io.File(
                            new java.io.File(org.owasp.benchmark.helpers.Utils.TESTFILES_DIR),
                            "passwordFile.txt")
            java.io.FileWriter fw =
                    new java.io.FileWriter(fileTarget, true); // the true will append the new data
            fw.write(
                    "secret_value="
                            + org.owasp.esapi.ESAPI.encoder().encodeForBase64(result, true)
                            + "\n")
            fw.close()
            response.getWriter()
                    .println(
                            "Sensitive value: '"
                                    + org.owasp
                                            .esapi
                                            .ESAPI
                                            .encoder()
                                            .encodeForHTML(new String(input))
                                    + "' encrypted and stored<br/>")

        } catch {
            case e: java.security.NoSuchAlgorithmException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
            case e: javax.crypto.NoSuchPaddingException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
            case e: javax.crypto.IllegalBlockSizeException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
            case e: javax.crypto.BadPaddingException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
            case e: java.security.InvalidKeyException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
            case e: java.security.InvalidAlgorithmParameterException => {
                response.getWriter()
                .println(
                "Problem executing crypto - javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) Test Case")
                e.printStackTrace(response.getWriter())
                throw new ServletException(e)
            }
        }
        response.getWriter()
                .println(
                        "Crypto Test javax.crypto.Cipher.getInstance(java.lang.String,java.security.Provider) executed")
    }
object BenchmarkTest00053 {
    val serialVersionUID: Long = 1L
}
}