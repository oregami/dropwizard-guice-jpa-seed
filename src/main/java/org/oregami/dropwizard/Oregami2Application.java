package org.oregami.dropwizard;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Oregami2Application extends Application<Oregami2Configuration> {
    public static void main(String[] args) throws Exception {
        new Oregami2Application().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<Oregami2Configuration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(Oregami2Configuration configuration,
                    Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource(
                configuration.getTemplate(),
                configuration.getDefaultName()
            );
            environment.jersey().register(resource);
    }

}