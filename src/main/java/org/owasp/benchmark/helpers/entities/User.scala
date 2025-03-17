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

class User  {
    private var userId: Int = _
    private var name: String = _
    private var password: String = _
    private var hobbyId: Int = _

    def getUserId(): Int = {
        return userId
    }

    def setUserId(userId: Int): Unit = {
        this.userId = userId
    }

    def getHobbyId(): Int = {
        return hobbyId
    }

    def setHobbyId(hobbyId: Int): Unit = {
        this.hobbyId = hobbyId
    }

    def getPassword(): String = {
        return password
    }

    def setPassword(password: String): Unit = {
        this.password = password
    }

    def getName(): String = {
        return name
    }

    def setName(name: String): Unit = {
        this.name = name
    }
}