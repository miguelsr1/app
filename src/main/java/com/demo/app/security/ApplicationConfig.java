
package com.demo.app.security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;


/**
 *
 * @author misanchez
 */
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:/DemoDS",
        callerQuery = "select password from USUARIO where usuario = ?",
        groupsQuery = "select role from USUARIO where usuario = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        priorityExpression = "#{100}",
        hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "${applicationConfig.dyna}"
        } // just for test / example
)
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/inicio.xhtml",
                errorPage = "/error.xhtml" // DRAFT API - must be set to empty for now
        )
)
@ApplicationScoped
@Named
public class ApplicationConfig {

    public String[] getDyna() {
        return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
    }

}
