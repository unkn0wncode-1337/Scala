/**
 * OWASP Benchmark v1.2
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
 * @author Dave Wichers
 * @created 2015
 */
package org.owasp.benchmark.testcode;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/hash-00/BenchmarkTest00029")
class BenchmarkTest00029 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        doPost(request, response)
    }

    @Override
    def doPost(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8")

        java.util.Map<String, String[]> map = request.getParameterMap()
        var param: String = ""
        if (!map.isEmpty()) {
            var values: Array[String] = map.get("BenchmarkTest00029")
            if (values != None) param = values[0]
        }

        try {
            java.util.Properties benchmarkprops = new java.util.Properties()
            benchmarkprops.load(
                    this.getClass().getClassLoader().getResourceAsStream("benchmark.properties"))
            var algorithm: String = benchmarkprops.getProperty("hashAlg1", "SHA512")
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm)
            var input: Array[byte] = {(byte) '?'}
            var inputParam: Object = param
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
object BenchmarkTest00029 {
    val serialVersionUID: Long = 1L
}
}