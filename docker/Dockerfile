FROM	dockerfile/java:oracle-java8

RUN		apt-get update
RUN		apt-get install -y maven
RUN		git clone https://github.com/oregami/dropwizard-guice-jpa-seed.git
RUN		cd dropwizard-guice-jpa-seed && mvn clean install
#RUN		java -jar target/dropwizard-guice-jpa-seed-0.0.1-SNAPSHOT.jar server todo.yml &2>1



# Define working directory.
WORKDIR /data/dropwizard-guice-jpa-seed
# Define default command.
#CMD ["bash"]


#CMD ["java", "-jar", "target/dropwizard-guice-jpa-seed-0.0.1-SNAPSHOT.jar","server","todo.yml","&2>1"]
CMD ["java", "-jar", "target/dropwizard-guice-jpa-seed-0.0.1-SNAPSHOT.jar", "server", "todo.yml"]