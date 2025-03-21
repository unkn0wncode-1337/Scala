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

@WebServlet(value = "/trustbound-00/BenchmarkTest00031")
class BenchmarkTest00031 extends HttpServlet {


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
            var values: Array[String] = map.get("BenchmarkTest00031")
            if (values != None) param = values[0]
        }

        // javax.servlet.http.HttpSession.putValue(java.lang.String,java.lang.Object^)
        request.getSession().putValue("userid", param)

        response.getWriter()
                .println(
                        "Item: 'userid' with value: '"
                                + org.owasp.benchmark.helpers.Utils.encodeForHTML(param)
                                + "' saved in session.")
    }
object BenchmarkTest00031 {
    val serialVersionUID: Long = 1L
}
}