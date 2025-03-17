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

@WebServlet(value = "/cmdi-00/BenchmarkTest00051")
class BenchmarkTest00051 extends HttpServlet {


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
        var param: String = scr.getTheValue("BenchmarkTest00051")

        var a1: String = ""
        var a2: String = ""
        var osName: String = System.getProperty("os.name")
        if (osName.indexOf("Windows") != -1) {
            a1 = "cmd.exe"
            a2 = "/c"
        } else {
            a1 = "sh"
            a2 = "-c"
        }
        var args: Array[String] = {a1, a2, "echo " + param}

        var pb: ProcessBuilder = new ProcessBuilder(args)

        try {
            var p: Process = pb.start()
            org.owasp.benchmark.helpers.Utils.printOSCommandResults(p, response)
        } catch {
            case e: IOException => {
                System.out.println(
                "Problem executing cmdi - java.lang.ProcessBuilder(java.lang.String[]) Test Case")
                throw new ServletException(e)
            }
        }
    }
object BenchmarkTest00051 {
    val serialVersionUID: Long = 1L
}
}