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
 * PURPOSE. See the GNU General Public License for more details
 *
 * @author Dave Wichers
 * @created 2015
 */
package org.owasp.benchmark.helpers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

class SeparateClassRequest  {
    private var request: HttpServletRequest = _

    def this(request: HttpServletRequest) = {
        this.request = request
    }

    def getTheParameter(p: String): String = {
        return request.getParameter(p)
    }

    def getTheCookie(c: String): String = {
        var cookies: Array[Cookie] = request.getCookies()

        var value: String = ""

        if (cookies != None) {
            for (cookie <- cookies) {
                if (cookie.getName().equals(c)) {
                    value = cookie.getValue()
                    break
                }
            }
        }

        return value
    }

    // This method is a 'safe' source.
    def getTheValue(p: String): String = {
        return "bar"
    }
}