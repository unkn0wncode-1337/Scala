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
 * @created 2017
 */
package org.owasp.benchmark.service.pojo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "person")
class Person  {

    private var id: Long = _
    private var address: String = _
    private var firstName: String = _
    private var lastName: String = _

    def this() = {

    def this(id: Long, firstName: String, lastName: String, address: String) = {
        this.id = id
        this.firstName = firstName
        this.address = address
    }

    def getId(): Long = {
        return id
    }

    def getAddress(): String = {
        return address
    }

    @XmlAttribute
    def setId(id: Long): Unit = {
        this.id = id
    }

    @XmlElement
    def setAddress(address: String): Unit = {
        this.address = address
    }

    def getFirstName(): String = {
        return firstName
    }

    @XmlElement
    def setFirstName(firstName: String): Unit = {
        this.firstName = firstName
    }

    def getLastName(): String = {
        return lastName
    }

    @XmlElement
    def setLastName(lastName: String): Unit = {
        this.lastName = lastName
    }

    @Override
    def toString(): String = {
        return "Person [id="
                + id
                + ", address="
                + address
                + ", firstName="
                + firstName
                + ", lastName="
                + lastName
                + "]"
    }
}
