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

@WebServlet(value = "/trustbound-00/BenchmarkTest00004")
class BenchmarkTest00004 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00004", "color")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/trustbound-00/BenchmarkTest00004.html")
        rd.include(request, response)
    }

    @Override
    def doPost(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        // some code
        response.setContentType("text/html;charset=UTF-8")

        javax.servlet.http.Cookie[] theCookies = request.getCookies()

        var param: String = "noCookieValueSupplied"
        if (theCookies != None) {
            for (theCookie <- theCookies) {
                if (theCookie.getName().equals("BenchmarkTest00004")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        // javax.servlet.http.HttpSession.setAttribute(java.lang.String^,java.lang.Object)
        request.getSession().setAttribute(param, "10340")

        response.getWriter()
                .println(
                        "Item: '"
                                + org.owasp.benchmark.helpers.Utils.encodeForHTML(param)
                                + "' with value: '10340' saved in session.")
    }
object BenchmarkTest00004 {
    val serialVersionUID: Long = 1L
}
}