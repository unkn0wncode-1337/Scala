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

@WebServlet(value = "/weakrand-00/BenchmarkTest00094")
class BenchmarkTest00094 extends HttpServlet {


    @Override
    def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit = {
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8")
        javax.servlet.http.Cookie userCookie =
                new javax.servlet.http.Cookie("BenchmarkTest00094", "whatever")
        userCookie.setMaxAge(60 * 3); // Store cookie for 3 minutes
        userCookie.setSecure(true)
        userCookie.setPath(request.getRequestURI())
        userCookie.setDomain(new java.net.URL(request.getRequestURL().toString()).getHost())
        response.addCookie(userCookie)
        javax.servlet.RequestDispatcher rd =
                request.getRequestDispatcher("/weakrand-00/BenchmarkTest00094.html")
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
                if (theCookie.getName().equals("BenchmarkTest00094")) {
                    param = java.net.URLDecoder.decode(theCookie.getValue(), "UTF-8")
                    break
                }
            }
        }

        var bar: String = "safe!"
        java.util.HashMap<String, Object> map52993 = new java.util.HashMap<String, Object>()
        map52993.put("keyA-52993", "a_Value"); // put some stuff in the collection
        map52993.put("keyB-52993", param); // put it in a collection
        map52993.put("keyC", "another_Value"); // put some stuff in the collection
        bar = (String) map52993.get("keyB-52993"); // get it back out
        bar = (String) map52993.get("keyA-52993"); // get safe value back out

        try {
            var rand: Double = java.security.SecureRandom.getInstance("SHA1PRNG").nextDouble()

            String rememberMeKey =
                    Double.toString(rand).substring(2); // Trim off the 0. at the front.

            var user: String = "SafeDonna"
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
                System.out.println("Problem executing SecureRandom.nextDouble() - TestCase")
                throw new ServletException(e)
            }
        }
        response.getWriter()
                .println("Weak Randomness Test java.security.SecureRandom.nextDouble() executed")
    }
object BenchmarkTest00094 {
    val serialVersionUID: Long = 1L
}
}