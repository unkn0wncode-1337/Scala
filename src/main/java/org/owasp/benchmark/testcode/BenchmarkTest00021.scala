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

@WebServlet(value = "/ldapi-00/BenchmarkTest00021")
class BenchmarkTest00021 extends HttpServlet {


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

        var param: String = request.getParameter("BenchmarkTest00021")
        if (param == None) param = ""

        org.owasp.benchmark.helpers.LDAPManager ads = new org.owasp.benchmark.helpers.LDAPManager()
        try {
            response.setContentType("text/html;charset=UTF-8")
            javax.naming.directory.DirContext ctx = ads.getDirContext()
            var base: String = "ou=users,ou=system"
            javax.naming.directory.SearchControls sc = new javax.naming.directory.SearchControls()
            sc.setSearchScope(javax.naming.directory.SearchControls.SUBTREE_SCOPE)
            var filter: String = "(&(objectclass=person))(|(uid=" + param + ")(street={0}))"
            var filters: Array[Object] = new Object[] {"The streetz 4 Ms bar"}
            // System.out.println("Filter " + filter)
            var found: Boolean = false
            javax.naming.NamingEnumeration<javax.naming.directory.SearchResult> results =
                    ctx.search(base, filter, filters, sc)
            while (results.hasMore()) {
                javax.naming.directory.SearchResult sr =
                        (javax.naming.directory.SearchResult) results.next()
                javax.naming.directory.Attributes attrs = sr.getAttributes()

                javax.naming.directory.Attribute attr = attrs.get("uid")
                javax.naming.directory.Attribute attr2 = attrs.get("street")
                if (attr != None) {
                    response.getWriter()
                            .println(
                                    "LDAP query results:<br>"
                                            + "Record found with name "
                                            + attr.get()
                                            + "<br>"
                                            + "Address: "
                                            + attr2.get()
                                            + "<br>")
                    // System.out.println("record found " + attr.get())
                    found = true
                }
            }
            if (!found) {
                response.getWriter()
                        .println(
                                "LDAP query results: nothing found for query: "
                                        + org.owasp.esapi.ESAPI.encoder().encodeForHTML(filter))
            }
        } catch {
            case e: javax.naming.NamingException => {
                throw new ServletException(e)
            }
        }
        finally {
            try {
                ads.closeDirContext()
            } catch {
                case e: Exception => {
                    throw new ServletException(e)
                }
            }
        }
    }
object BenchmarkTest00021 {
    val serialVersionUID: Long = 1L
}
}