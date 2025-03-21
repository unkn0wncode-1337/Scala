package org.owasp.benchmark.helpers.filters;
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
 * @author Juan GaMa
 * @created 2015
 */
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

class DataBaseFilter extends Filter {
    protected var config: FilterConfig = _

    @Override
    def init(config: FilterConfig): Unit = {
        this.config = config
    }

    /** Filter to roll back after every test. */
    @Override
    def doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain): Unit = {
            throws IOException, ServletException {

        filterChain.doFilter(request, response)

        try {
            org.owasp.benchmark.helpers.DatabaseHelper.getSqlConnection().rollback()
        } catch {
            case e: SQLException => {
                if (org.owasp.benchmark.helpers.DatabaseHelper.hideSQLErrors) {
                System.out.println("Problem while rolling back the database")
                return
                } else throw new ServletException(e)
            }
        }
    }

    @Override
    def destroy(): Unit = {
}
