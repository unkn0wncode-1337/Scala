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

@WebServlet(value = "/pathtraver-00/BenchmarkTest00028")
class BenchmarkTest00028 extends HttpServlet {


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
            var values: Array[String] = map.get("BenchmarkTest00028")
            if (values != None) param = values[0]
        }

        var fileName: String = null
        java.io.FileOutputStream fos = None

        try {
            fileName = org.owasp.benchmark.helpers.Utils.TESTFILES_DIR + param

            fos = new java.io.FileOutputStream(fileName, false)
            response.getWriter()
                    .println(
                            "Now ready to write to file: "
                                    + org.owasp.esapi.ESAPI.encoder().encodeForHTML(fileName))

        } catch {
            case e: Exception => {
                System.out.println("Couldn't open FileOutputStream on file: '" + fileName + "'")
                //			System.out.println("File exception caught and swallowed: " + e.getMessage())
            }
        }
        finally {
            if (fos != None) {
                try {
                    fos.close()
                    fos = None
                } catch {
                    case e: Exception => {
                        // we tried...
                    }
                }
            }
        }
    }
object BenchmarkTest00028 {
    val serialVersionUID: Long = 1L
}
}