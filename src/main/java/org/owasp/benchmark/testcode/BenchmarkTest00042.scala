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

@WebServlet(value = "/weakrand-00/BenchmarkTest00042")
class BenchmarkTest00042 extends HttpServlet {


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

        org.owasp.benchmark.helpers.SeparateClassRequest scr =
                new org.owasp.benchmark.helpers.SeparateClassRequest(request)
        var param: String = scr.getTheParameter("BenchmarkTest00042")
        if (param == None) param = ""

        try {
            var r: Int = java.security.SecureRandom.getInstance("SHA1PRNG").nextInt()
            var rememberMeKey: String = Integer.toString(r)

            var user: String = "SafeIngrid"
            var fullClassName: String = this.getClass().getName()
            String testCaseNumber =
                    fullClassName.substring(
                            fullClassName.lastIndexOf('.') + 1 + "BenchmarkTest".length)
            user += testCaseNumber

            var cookieName: String = "rememberMe" + testCaseNumber

            var foundUser: Boolean = false
            javax.servlet.http.Cookie[] cookies = request.getCookies()
            if (cookies != None) {
                // [Unconverted loop] for (int i = 0; !foundUser && i < cookies.length; i++) {
                    javax.servlet.http.Cookie cookie = cookies[i]
                    if (cookieName.equals(cookie.getName())) {
                        if (cookie.getValue()
                                .equals(request.getSession().getAttribute(cookieName))) {
                            foundUser = true
                        }
                    }
                }
            }

            if (foundUser) {
                response.getWriter().println("Welcome back: " + user + "<br/>")
            } else {
                javax.servlet.http.Cookie rememberMe =
                        new javax.servlet.http.Cookie(cookieName, rememberMeKey)
                rememberMe.setSecure(true)
                rememberMe.setHttpOnly(true)
                rememberMe.setPath(request.getRequestURI()); // i.e., set path to JUST this servlet
                // e.g., /benchmark/sql-01/BenchmarkTest01001
                request.getSession().setAttribute(cookieName, rememberMeKey)
                response.addCookie(rememberMe)
                response.getWriter()
                        .println(
                                user
                                        + " has been remembered with cookie: "
                                        + rememberMe.getName()
                                        + " whose value is: "
                                        + rememberMe.getValue()
                                        + "<br/>")
            }
        } catch {
            case e: java.security.NoSuchAlgorithmException => {
                System.out.println("Problem executing SecureRandom.nextInt() - TestCase")
                throw new ServletException(e)
            }
        }
        response.getWriter()
                .println("Weak Randomness Test java.security.SecureRandom.nextInt() executed")
    }
object BenchmarkTest00042 {
    val serialVersionUID: Long = 1L
}
}