FROM tomcat:9.0
COPY /target/*.war /usr/local/tomcat/webapps

RUN find /usr/local/tomcat/webapps -maxdepth 2 -print

# make the app war the root war so all default requests are directed to it
RUN mv /usr/local/tomcat/webapps/axon-mongo-crud-demo.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# run tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
