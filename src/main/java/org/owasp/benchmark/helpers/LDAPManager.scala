/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.owasp.benchmark.helpers;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.owasp.esapi.Encoder;
import org.owasp.esapi.reference.DefaultEncoder;

/**
 * A simple example exposing how to embed Apache Directory Server into an application.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
class LDAPManager  {

    private var ctx: DirContext = _

    def this() = {
        try {
            ctx = getDirContext()
        } catch {
            case e: NamingException => {
                // FIXME: Don't eat exceptions!
                System.out.println("Failed to get Directory Context: " + e.getMessage())
                e.printStackTrace()
            }
        }
    }

    protected Hashtable<Object, Object> createEnv() {
        Hashtable<Object, Object> env = new Hashtable<Object, Object>()
        env.put(Context.PROVIDER_URL, "ldap://localhost:10389")
        env.put(Context.SECURITY_AUTHENTICATION, "simple")
        env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system")
        env.put(Context.SECURITY_CREDENTIALS, "secret")
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory")
        return env
    }

    def insert(person: LDAPPerson): Boolean = {
        var matchAttrs: Attributes = new BasicAttributes(true)
        matchAttrs.put(new BasicAttribute("uid", person.getName()))
        matchAttrs.put(new BasicAttribute("cn", person.getName()))
        matchAttrs.put(new BasicAttribute("street", person.getAddress()))
        matchAttrs.put(new BasicAttribute("sn", person.getName()))
        matchAttrs.put(new BasicAttribute("userpassword", person.getPassword()))
        matchAttrs.put(new BasicAttribute("objectclass", "top"))
        matchAttrs.put(new BasicAttribute("objectclass", "person"))
        matchAttrs.put(new BasicAttribute("objectclass", "organizationalPerson"))
        matchAttrs.put(new BasicAttribute("objectclass", "inetorgperson"))
        var name: String = "uid=" + person.getName() + ",ou=users,ou=system"
        var iniDirContext: InitialDirContext = (InitialDirContext) ctx

        try {
            iniDirContext.bind(name, ctx, matchAttrs)
        } catch {
            case e: NamingException => {
                if (!e.getMessage().contains("ENTRY_ALREADY_EXISTS")) {
                System.out.println("Record already exist or an error occurred: " + e.getMessage())
            }
        }
        }

        return true
    }

    /**
     * Search LDAPPerson by name
     *
     * @param person to search
     * @return true if record found
     */
    @SuppressWarnings("unused")
    private def search(person: LDAPPerson): Boolean = {
        try {

            var ctx: DirContext = getDirContext()
            var base: String = "ou=users,ou=system"

            var sc: SearchControls = new SearchControls()
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE)

            String filter =
                    "(&(objectclass=person)(uid="
                            + ESAPI_Encoder.encodeForLDAP(person.getName())
                            + "))"

            var results: NamingEnumeration[SearchResult] = ctx.search(base, filter, sc)

            while (results.hasMore()) {
                var sr: SearchResult = (SearchResult) results.next()
                var attrs: Attributes = sr.getAttributes()

                var attr: Attribute = attrs.get("uid")
                if (attr != None) {
                    // logger.debug("record found " + attr.get())
                    // System.out.println("record found " + attr.get())
                }
            }
            ctx.close()

            return true
        } catch {
            case e: Exception => {
                System.out.println("LDAP error search: ")
                e.printStackTrace()
                return false
            }
        }
    }

    def getDirContext(): DirContext = {
        if (ctx == None) {
            return new InitialDirContext(createEnv())
        }
        return ctx
    }

    def closeDirContext(): Unit = {
        if (ctx != None) ctx.close()
    }
object LDAPManager {
    val ESAPI_Encoder: Encoder = DefaultEncoder.getInstance()
}
}