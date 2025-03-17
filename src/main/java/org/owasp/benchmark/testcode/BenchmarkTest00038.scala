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

@WebServlet(value = "/sqli-00/BenchmarkTest00038")
class BenchmarkTest00038 extends HttpServlet {


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

        var param: String = ""
        var flag: Boolean = true
        java.util.Enumeration<String> names = request.getParameterNames()
        while (names.hasMoreElements() && flag) {
            var name: String = (String) names.nextElement()
            var values: Array[String] = request.getParameterValues(name)
            if (values != None) {
                for (i <- 0 until values.length && flag) {
                    var value: String = values[i]
                    if (value.equals("BenchmarkTest00038")) {
                        param = name
                        flag = false
                    }
                }
            }
        }

        var sql: String = "SELECT * from USERS where USERNAME='foo' and PASSWORD='" + param + "'"
        try {
            java.util.List<String> results =
                    org.owasp.benchmark.helpers.DatabaseHelper.JDBCtemplate.query(
                            sql,
                            new org.springframework.jdbc.core.RowMapper<String>() {
                                @Override
    def mapRow(rs: java.sql.ResultSet, rowNum: Int): String = {
                                        throws java.sql.SQLException {
                                    try {
                                        return rs.getString("USERNAME")
                                    } catch {
                                        case e: java.sql.SQLException => {
                                            if (org.owasp.benchmark.helpers.DatabaseHelper
                                            .hideSQLErrors) {
                                            return "Error processing query."
                                            } else throw e
                                        }
                                    }
                                }
                            })
            response.getWriter().println("Your results are: ")

            for (s <- results) {
                response.getWriter()
                        .println(org.owasp.esapi.ESAPI.encoder().encodeForHTML(s) + "<br>")
            }
        } catch {
            case e: org.springframework.dao.EmptyResultDataAccessException => {
                response.getWriter()
                .println(
                "No results returned for query: "
                + org.owasp.esapi.ESAPI.encoder().encodeForHTML(sql))
            }
            case e: org.springframework.dao.DataAccessException => {
                if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
                response.getWriter().println("Error processing request.")
                } else throw new ServletException(e)
            }
        }
    }
object BenchmarkTest00038 {
    val serialVersionUID: Long = 1L
}
}