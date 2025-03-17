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
 * @author Juan Gama
 * @created 2015
 */
package org.owasp.benchmark.helpers.entities;

import java.util.Set;

class Employee  {
    private var id: Int = _
    private var firstName: String = _
    private var lastName: String = _
    private var salary: Int = _
    private var certificates: Set[Certificate] = _

    def this() = {

    def this(fname: String, lname: String, salary: Int) = {
        this.firstName = fname
        this.lastName = lname
        this.salary = salary
    }

    def getId(): Int = {
        return id
    }

    def setId(id: Int): Unit = {
        this.id = id
    }

    def getFirstName(): String = {
        return firstName
    }

    def setFirstName(first_name: String): Unit = {
        this.firstName = first_name
    }

    def getLastName(): String = {
        return lastName
    }

    def setLastName(last_name: String): Unit = {
        this.lastName = last_name
    }

    def getSalary(): Int = {
        return salary
    }

    def setSalary(salary: Int): Unit = {
        this.salary = salary
    }

    def getCertificates(): Set[Certificate] = {
        return certificates
    }

    def setCertificates(certificates: Set[Certificate]): Unit = {
        this.certificates = certificates
    }
}