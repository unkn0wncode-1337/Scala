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
 * @author Nick Sanidas
 * @created 2015
 */
package org.owasp.benchmark.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class PropertiesManager  {
    private var file: Option[File] = None

    // This loads the default benchmark.properties file
    def this() = {
        file = Utils.getFileFromClasspath("benchmark.properties", this.getClass().getClassLoader())
    }

    // This can be used to load an alternate properties file specified by the fileName
    def this(fileName: String) = {
        file = Utils.getFileFromClasspath(fileName, this.getClass().getClassLoader())
    }

    // This can be used to load an alternate properties file specified by the path and fileName
    def this(path: String, fileName: String) = {
        file = new File(path + File.separator + fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch {
                case e: IOException => {
                    System.out.println(
                    "Problem creating new empty properties file: " + file.getAbsolutePath())
                }
            }
        }
    }

    def displayProperties(): Unit = {
        var props: Properties = loadProperties()

        System.out.println(props.keySet())
        System.out.println(props.values())
    }

    def getProperty(key: String, defaultValue: String): String = {
        var props: Properties = loadProperties()
        return props.getProperty(key, defaultValue)
    }

    def getProperty(key: String, defaultValue: Int): Int = {
        var props: Properties = loadProperties()
        return Integer.parseInt(props.getProperty(key, Integer.toString(defaultValue)))
    }

    def saveProperty(key: String, value: String): Unit = {
        var props: Properties = loadProperties()
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.setProperty(key, value)
            props.store(out, None)
        } catch {
            case e: IOException => {
                System.out.println("There was a problem saving a property in the properties file")
                e.printStackTrace()
            }
        }
    }

    def removeProperty(key: String): Unit = {
        var props: Properties = loadProperties()
        try (FileOutputStream out = new FileOutputStream(file)) {
            props.remove(key)
            props.store(out, None)
        } catch {
            case e: IOException => {
                System.out.println("There was a problem removing a property from the properties file")
                e.printStackTrace()
            }
        }
    }

    private def loadProperties(): Properties = {
        var props: Properties = new Properties()
        try (InputStream is = new FileInputStream(file)) {
            props.load(is)
        } catch {
            case e: IOException => {
                System.out.println("Error loading properties file")
                e.printStackTrace()
            }
        }
        return props
    }
}