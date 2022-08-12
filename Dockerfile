FROM jboss/wildfly:12.0.0.Final

# Maintainer
MAINTAINER "miguelsr1" "miguelsr1@gmail.com"

#ENV DB_CONNECTION_URL jdbc:oracle:thin:@//192.168.20.50:1521/mined 
#ENV DB_USERNAME PAQUETES 
#ENV DB_PASSWORD mined2012
ENV JBOSS_HOME /opt/jboss/wildfly

# JBoss ports
EXPOSE 8080 9990 8009

#COPY ojdbc8.jar $JBOSS_HOME/modules/system/layers/base/com/oracle/main/
#COPY module.xml $JBOSS_HOME/modules/system/layers/base/com/oracle/main/
#COPY eclipse/eclipselink-2.7.4.jar $JBOSS_HOME/modules/system/layers/base/org/eclipse/persistence/main
#COPY eclipse/jipijapa-eclipselink-10.1.0.Final.jar $JBOSS_HOME/modules/system/layers/base/org/eclipse/persistence/main
#COPY eclipse/module.xml $JBOSS_HOME/modules/system/layers/base/org/eclipse/persistence/main



# Create user
RUN ${JBOSS_HOME}/bin/add-user.sh miguelsr1 admin123 --silent

USER root
RUN chmod +x ${JBOSS_HOME}/bin/jboss-cli.sh

#RUN /bin/sh -c '$JBOSS_HOME/bin/standalone.sh &' && \
#  sleep 10 && \ 
#    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="module add --name=com.oracle --resources=/opt/jboss/wildfly/modules/system/layers/base/com/oracle/main/ojdbc8.jar --dependencies=javax.api,javax.transaction.api" && \
#    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/subsystem=datasources/jdbc-driver=oracle:add(driver-name=oracle,driver-module-name=com.oracle,driver-xa-datasource-class-name=oracle.jdbc.xa.client.OracleXADataSource)" && \
#    $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/subsystem=datasources/data-source=PaqueteDS:add(jndi-name=\"java:/PaqueteDS\",connection-url=\"$DB_CONNECTION_URL\",driver-name=oracle,user-name=$DB_USERNAME,password=$DB_PASSWORD,valid-connection-checker-class-name=\"org.jboss.jca.adapters.jdbc.extensions.oracle.OracleValidConnectionChecker\",stale-connection-checker-class-name=\"org.jboss.jca.adapters.jdbc.extensions.oracle.OracleStaleConnectionChecker\",exception-sorter-class-name=\"org.jboss.jca.adapters.jdbc.extensions.oracle.OracleExceptionSorter\")" && \
#    $JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown


RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current
RUN chown -R jboss:jboss /opt/jboss/wildfly/

ADD target/app-1.0.war $JBOSS_HOME/standalone/deployments/
 
# Run
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-c", "standalone.xml"]