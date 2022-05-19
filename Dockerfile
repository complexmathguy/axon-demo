FROM tomcat:9.0
COPY /target/*.war /usr/local/tomcat/webapps
 
# make the app war the root war so all default requests are directed to it
RUN mv /usr/local/tomcat/webapps/axonmongocruddemo-0.0.1.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# run tomcat
CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
