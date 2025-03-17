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

class Certificate  {
    private var id: Int = _
    private var name: String = _

    def this() = {

    def this(name: String) = {
        this.name = name
    }

    def getId(): Int = {
        return id
    }

    def setId(id: Int): Unit = {
        this.id = id
    }

    def getName(): String = {
        return name
    }

    def setName(name: String): Unit = {
        this.name = name
    }

    @Override
    def equals(obj: Object): Boolean = {
        if (obj == None) return false
        if (!this.getClass().equals(obj.getClass())) return false

        var obj2: Certificate = (Certificate) obj
        if ((this.id == obj2.getId()) && (this.name.equals(obj2.getName()))) {
            return true
        }
        return false
    }

    @Override
    def hashCode(): Int = {
        var tmp: Int = 0
        tmp = (id + name).hashCode()
        return tmp
    }
}
