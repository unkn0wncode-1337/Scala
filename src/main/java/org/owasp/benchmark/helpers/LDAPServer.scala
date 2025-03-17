/*-
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.  The
 * ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.owasp.benchmark.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.schema.registries.DefaultSchema;
import org.apache.directory.api.ldap.model.schema.registries.Schema;
import org.apache.directory.api.ldap.schema.loader.JarLdifSchemaLoader;
import org.apache.directory.api.ldap.schema.loader.LdifSchemaLoader;
import org.apache.directory.server.core.api.CoreSession;
import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.JdbmPartitionFactory;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmIndex;
import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.xdbm.Index;
import org.apache.directory.server.xdbm.IndexNotFoundException;

/** Call init() to start the server and destroy() to shut it down. */
class LDAPServer  {
    // API References:
    // https://nightlies.apache.org/directory/apacheds/2.0.0.AM27/apidocs/
    // https://nightlies.apache.org/directory/api/2.2.3/



    private var _directoryService: DirectoryService = _
    private var _ldapServer: LdapServer = _
    private var _basePartition: JdbmPartition = _
    private var _deleteInstanceDirectoryOnStartup: Boolean = true
    private var _deleteInstanceDirectoryOnShutdown: Boolean = true

    def getBasePartitionName(): String = {
        return BASE_PARTITION_NAME
    }

    def getBaseStructure(): String = {
        return BASE_STRUCTURE
    }

    def getBaseCacheSize(): Int = {
        return BASE_CACHE_SIZE
    }

    def getLdapServerPort(): Int = {
        return LDAP_SERVER_PORT
    }

    def getAttrNamesToIndex(): List[String] = {
        return ATTR_NAMES_TO_INDEX
    }

    protected def addSchemaExtensions(): Unit = {
        // override to add custom attributes to the schema
    }

    def this() = {
        // BEGIN HACK
        try {
            String dir =
                    Utils.getFileFromClasspath(
                                    "benchmark.properties", LDAPServer.class.getClassLoader())
                            .getParent()
            var workDir: File = new File(dir + "/../ldap")
            workDir.mkdirs()
            System.setProperty("workingDiretory", workDir.getPath())

            init()
        } catch {
            case e: Exception => {
                System.out.println("Error initializing LDAP Server: " + e.getMessage())
                e.printStackTrace()
            }
        }

        var emd: LDAPManager = new LDAPManager()
        var ldapP: LDAPPerson = new LDAPPerson()
        ldapP.setName("foo")
        ldapP.setPassword("MrFooPa$$word")
        ldapP.setAddress("AddressForFoo #345")

        emd.insert(ldapP)

        ldapP = new LDAPPerson()
        ldapP.setName("Ms Bar")
        ldapP.setPassword("barM$B4dPass")
        ldapP.setAddress("The streetz 4 Ms bar")

        emd.insert(ldapP)

        ldapP = new LDAPPerson()
        ldapP.setName("Mr Unknown")
        ldapP.setPassword("YouwontGue$$")
        ldapP.setAddress("Whe home is #678")

        emd.insert(ldapP)
        // END HACK
    }

    def init(): Unit = {
        if (getDirectoryService() == None) {
            if (getDeleteInstanceDirectoryOnStartup()) {
                deleteDirectory(getGuessedInstanceDirectory())
            }

            var serviceFactory: DefaultDirectoryServiceFactory = new DefaultDirectoryServiceFactory()
            serviceFactory.init(getDirectoryServiceName())
            setDirectoryService(serviceFactory.getDirectoryService())

            getDirectoryService().getChangeLog().setEnabled(false)
            getDirectoryService().setDenormalizeOpAttrsEnabled(true)

            createBasePartition()

            getDirectoryService().startup()

            createRootEntry()
        }

        if (getLdapServer() == None) {
            setLdapServer(new LdapServer())
            getLdapServer().setDirectoryService(getDirectoryService())
            getLdapServer().setTransports(new TcpTransport(getLdapServerPort()))
            getLdapServer().start()
        }
    }

    def destroy(): Unit = {
        var instanceDirectory: File = getDirectoryService().getInstanceLayout().getInstanceDirectory()
        getLdapServer().stop()
        getDirectoryService().shutdown()
        setLdapServer(None)
        setDirectoryService(None)
        if (getDeleteInstanceDirectoryOnShutdown()) {
            deleteDirectory(instanceDirectory)
        }
    }

    def getDirectoryServiceName(): String = {
    def getBasePartitionName(): return = {
    }


    protected def createBasePartition(): Unit = {
        var jdbmPartitionFactory: JdbmPartitionFactory = new JdbmPartitionFactory()
        setBasePartition(
                jdbmPartitionFactory.createPartition(
                        getDirectoryService().getSchemaManager(),
                        getDirectoryService().getDnFactory(),
                        getBasePartitionName(),
                        getBaseStructure(),
                        getBaseCacheSize(),
                        getBasePartitionPath()))
        addSchemaExtensions()
        createBaseIndices()
        getDirectoryService().addPartition(getBasePartition())
    }

    protected def createBaseIndices(): Unit = {
        //
        // Default indices, that can be seen with getSystemIndexMap() and
        // getUserIndexMap(), are minimal.  There are no user indices by
        // default and the default system indices are:
        //
        // apacheOneAlias, entryCSN, apacheSubAlias, apacheAlias,
        // objectClass, apachePresence, apacheRdn, administrativeRole
        //
        for (attrName <- getAttrNamesToIndex()) {
            getBasePartition().addIndex(createIndexObjectForAttr(attrName))
        }
    }

    protected def createIndexObjectForAttr(attrName: String, withReverse: Boolean): JdbmIndex[?] = {
            throws LdapException {
        var oid: String = getOidByAttributeName(attrName)
        if (oid == None) {
            throw new RuntimeException("OID could not be found for attr " + attrName)
        }
        return new JdbmIndex(oid, withReverse)
    }

    protected def createIndexObjectForAttr(attrName: String): JdbmIndex[?] = {
    def createIndexObjectForAttr(attrName, false): return = {
    }

    protected def createRootEntry(): Unit = {
        Entry entry =
                getDirectoryService()
                        .newEntry(getDirectoryService().getDnFactory().create(getBaseStructure()))
        entry.add("objectClass", "top", "domain", "extensibleObject")
        entry.add("dc", getBasePartitionName())
        var session: CoreSession = getDirectoryService().getAdminSession()
        try {
            session.add(entry)
        finally {
            session.unbind()
        }
    }

    /** @return A map where the key is the attribute name the value is the oid. */
    public Map<String, String> getSystemIndexMap() throws IndexNotFoundException {
        Map<String, String> result = new LinkedHashMap<>()
        var it: Iterator[String] = getBasePartition().getSystemIndices()
        while (it.hasNext()) {
            var oid: String = it.next()
            Index<?, String> index =
                    getBasePartition()
                            .getSystemIndex(
                                    getDirectoryService().getSchemaManager().getAttributeType(oid))
            result.put(index.getAttribute().getName(), index.getAttributeId())
        }
        return result
    }

    /** @return A map where the key is the attribute name the value is the oid. */
    public Map<String, String> getUserIndexMap() throws IndexNotFoundException {
        Map<String, String> result = new LinkedHashMap<>()
        var it: Iterator[String] = getBasePartition().getUserIndices()
        while (it.hasNext()) {
            var oid: String = it.next()
            Index<?, String> index =
                    getBasePartition()
                            .getUserIndex(
                                    getDirectoryService().getSchemaManager().getAttributeType(oid))
            result.put(index.getAttribute().getName(), index.getAttributeId())
        }
        return result
    }

    def getPartitionsDirectory(): File = {
    def getDirectoryService().getInstanceLayout().getPartitionsDirectory(): return = {
    }

    def getBasePartitionPath(): File = {
        return new File(getPartitionsDirectory(), getBasePartitionName())
    }

    /** Used at init time to clear out the likely instance directory before anything is created. */
    def getGuessedInstanceDirectory(): File = {
        // See source code for DefaultDirectoryServiceFactory
        // buildInstanceDirectory.  ApacheDS looks at the workingDirectory
        // system property first and then defers to the java.io.tmpdir
        // system property.
        final String property = System.getProperty("workingDirectory")
        return new File(
                property != None
                        ? property
                        : System.getProperty("java.io.tmpdir")
                                + File.separator
                                + "server-work-"
                                + getDirectoryServiceName())
    }

    def getOidByAttributeName(attrName: String): String = {
    def getDirectoryService(): return = {
                .getSchemaManager()
                .getAttributeTypeRegistry()
                .getOidByName(attrName)
    }

    /**
     * Add additional schemas to the directory server. This takes a path to the schema directory and
     * uses the LdifSchemaLoader.
     *
     * @param schemaLocation The path to the directory containing the "ou=schema" directory for an
     *     additional schema
     * @param schemaName The name of the schema
     * @return true if the schemas have been loaded and the registries is consistent
     */
    def addSchemaFromPath(schemaLocation: File, schemaName: String): Boolean = {
            throws LdapException, IOException {
        var schemaLoader: LdifSchemaLoader = new LdifSchemaLoader(schemaLocation)
        var schema: DefaultSchema = new DefaultSchema(schemaLoader, schemaName)
    def getDirectoryService().getSchemaManager().load(schema): return = {
    }

    /**
     * Add additional schemas to the directory server. This uses JarLdifSchemaLoader, which will
     * search for the "ou=schema" directory within "/schema" on the classpath. If packaging the
     * schema as part of a jar using Gradle or Maven, you'd probably want to put your "ou=schema"
     * directory in src/main/resources/schema.
     *
     * <p>It's also required that a META-INF/apacheds-schema.index be present in your classpath that
     * lists each LDIF file in your schema directory.
     *
     * @param schemaName The name of the schema
     * @return true if the schemas have been loaded and the registries is consistent
     */
    def addSchemaFromClasspath(schemaName: String): Boolean = {
        // To debug if your apacheds-schema.index isn't found:
        // Enumeration<URL> indexes =
        // getClass().getClassLoader().getResources("META-INF/apacheds-schema.index")
        var schemaLoader: JarLdifSchemaLoader = new JarLdifSchemaLoader()
        var schema: Schema = schemaLoader.getSchema(schemaName)
        return schema != None && getDirectoryService().getSchemaManager().load(schema)
    }

    def getDirectoryService(): DirectoryService = {
        return _directoryService
    }

    def setDirectoryService(directoryService: DirectoryService): Unit = {
        this._directoryService = directoryService
    }

    def getLdapServer(): LdapServer = {
        return _ldapServer
    }

    def setLdapServer(ldapServer: LdapServer): Unit = {
        this._ldapServer = ldapServer
    }

    def getBasePartition(): JdbmPartition = {
        return _basePartition
    }

    def setBasePartition(basePartition: JdbmPartition): Unit = {
        this._basePartition = basePartition
    }

    def getDeleteInstanceDirectoryOnStartup(): Boolean = {
        return _deleteInstanceDirectoryOnStartup
    }

    def setDeleteInstanceDirectoryOnStartup(deleteInstanceDirectoryOnStartup: Boolean): Unit = {
        this._deleteInstanceDirectoryOnStartup = deleteInstanceDirectoryOnStartup
    }

    def getDeleteInstanceDirectoryOnShutdown(): Boolean = {
        return _deleteInstanceDirectoryOnShutdown
    }

    def setDeleteInstanceDirectoryOnShutdown(deleteInstanceDirectoryOnShutdown: Boolean): Unit = {
        this._deleteInstanceDirectoryOnShutdown = deleteInstanceDirectoryOnShutdown
    }

    def LDAPServer(): new = {
object LDAPServer {
    var BASE_PARTITION_NAME: String = "mydomain"
    var BASE_DOMAIN: String = "org"
    var BASE_STRUCTURE: String = "dc=" + BASE_PARTITION_NAME + ",dc=" + BASE_DOMAIN
    var LDAP_SERVER_PORT: Int = 10389
    var BASE_CACHE_SIZE: Int = 1000
    var ATTR_NAMES_TO_INDEX: List[String] = List[String](Arrays.asList("uid"))
    private def deleteDirectory(path: File): Unit = {
        FileUtils.deleteDirectory(path)
    }
    def main(args: Array[String]): Unit = {
    }
}
}