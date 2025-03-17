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

@WebServlet(value = "/cmdi-00/BenchmarkTest00092")
class BenchmarkTest00092 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00092", "FOO%3Decho+Injection")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/cmdi-00/BenchmarkTest00092.html")
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
                if (theCookie.getName().equals("BenchmarkTest00092")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        String bar
        var guess: String = "ABC"
        var switchTarget: Char = guess.charAt(2)

        // Simple case statement that assigns param to bar on conditions 'A', 'C', or 'D'
        switchTarget match {
            case 'A' =>
                bar = param
                break
            case 'B' =>
                bar = "bobs_your_uncle"
                break
            case 'C' =>
            case 'D' =>
                bar = param
                break
            case _ =>
                bar = "bobs_your_uncle"
                break
        }

        String cmd =
                org.owasp.benchmark.helpers.Utils.getInsecureOSCommandString(
                        this.getClass().getClassLoader())
        var args: Array[String] = {cmd}
        var argsEnv: Array[String] = {bar}

        var r: Runtime = Runtime.getRuntime()

        try {
            var p: Process = r.exec(args, argsEnv)
            org.owasp.benchmark.helpers.Utils.printOSCommandResults(p, response)
        } catch {
            case e: IOException => {
                System.out.println("Problem executing cmdi - TestCase")
                response.getWriter()
                .println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(e.getMessage()))
                return
            }
        }
    }
object BenchmarkTest00092 {
    val serialVersionUID: Long = 1L
}
}