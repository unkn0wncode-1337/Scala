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

@WebServlet(value = "/weakrand-00/BenchmarkTest00080")
class BenchmarkTest00080 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00080", "whatever")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/weakrand-00/BenchmarkTest00080.html")
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
                if (theCookie.getName().equals("BenchmarkTest00080")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        var bar: String = org.springframework.web.util.HtmlUtils.htmlEscape(param)

        var rand: Float = new java.util.Random().nextFloat()
        var rememberMeKey: String = Float.toString(rand).substring(2); // Trim off the 0. at the front.

        var user: String = "Floyd"
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
                    if (cookie.getValue().equals(request.getSession().getAttribute(cookieName))) {
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
            rememberMe.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
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

        response.getWriter().println("Weak Randomness Test java.util.Random.nextFloat() executed")
    }
object BenchmarkTest00080 {
    val serialVersionUID: Long = 1L
}
}