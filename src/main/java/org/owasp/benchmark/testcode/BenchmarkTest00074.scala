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

@WebServlet(value = "/hash-00/BenchmarkTest00074")
class BenchmarkTest00074 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00074", "someSecret")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/hash-00/BenchmarkTest00074.html")
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
                if (theCookie.getName().equals("BenchmarkTest00074")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        var bar: String = "safe!"
        java.util.HashMap<String, Object> map98790 = new java.util.HashMap<String, Object>()
        map98790.put("keyA-98790", "a-Value"); // put some stuff in the collection
        map98790.put("keyB-98790", param); // put it in a collection
        map98790.put("keyC", "another-Value"); // put some stuff in the collection
        bar = (String) map98790.get("keyB-98790"); // get it back out

        try {
            java.util.Properties benchmarkprops = new java.util.Properties()
            benchmarkprops.load(
                    this.getClass().getClassLoader().getResourceAsStream("benchmark.properties"))
            var algorithm: String = benchmarkprops.getProperty("hashAlg1", "SHA512")
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm)
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
            md.update(input)

            var result: Array[byte] = md.digest()
            java.io.File fileTarget =
                    new java.io.File(
                            new java.io.File(org.owasp.benchmark.helpers.Utils.TESTFILES_DIR),
                            "passwordFile.txt")
            java.io.FileWriter fw =
                    new java.io.FileWriter(fileTarget, true); // the true will append the new data
            fw.write(
                    "hash_value="
                            + org.owasp.esapi.ESAPI.encoder().encodeForBase64(result, true)
                            + "\n")
            fw.close()
            response.getWriter()
                    .println(
                            "Sensitive value '"
                                    + org.owasp
                                            .esapi
                                            .ESAPI
                                            .encoder()
                                            .encodeForHTML(new String(input))
                                    + "' hashed and stored<br/>")

        } catch {
            case e: java.security.NoSuchAlgorithmException => {
                System.out.println("Problem executing hash - TestCase")
                throw new ServletException(e)
            }
        }

        response.getWriter()
                .println(
                        "Hash Test java.security.MessageDigest.getInstance(java.lang.String) executed")
    }
object BenchmarkTest00074 {
    val serialVersionUID: Long = 1L
}
}