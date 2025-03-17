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

@WebServlet(value = "/pathtraver-00/BenchmarkTest00065")
class BenchmarkTest00065 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00065", "FileName")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/pathtraver-00/BenchmarkTest00065.html")
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
                if (theCookie.getName().equals("BenchmarkTest00065")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        var bar: String = ""
        if (param != None) {
            bar =
    def String(): new = {
                            org.apache.commons.codec.binary.Base64.decodeBase64(
                                    org.apache.commons.codec.binary.Base64.encodeBase64(
                                            param.getBytes())))
        }

        var fileName: String = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR + bar
        java.io.InputStream is = None

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName)
            is = java.nio.file.Files.newInputStream(path, java.nio.file.StandardOpenOption.READ)
            var b: Array[byte] = new byte[1000]
            var size: Int = is.read(b)
            response.getWriter()
                    .println(
                            "The beginning of file: '"
                                    + org.owasp.esapi.ESAPI.encoder().encodeForHTML(fileName)
                                    + "' is:\n\n")
            response.getWriter()
                    .println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(new String(b, 0, size)))
            is.close()
        } catch {
            case e: Exception => {
                System.out.println("Couldn't open InputStream on file: '" + fileName + "'")
                response.getWriter()
                .println(
                "Problem getting InputStream: "
                + org.owasp
                .esapi
                .ESAPI
                .encoder()
                .encodeForHTML(e.getMessage()))
            }
        }
        finally {
            if (is != None) {
                try {
                    is.close()
                    is = None
                } catch {
                    case e: Exception => {
                        // we tried...
                    }
                }
            }
        }
    }
object BenchmarkTest00065 {
    val serialVersionUID: Long = 1L
}
}