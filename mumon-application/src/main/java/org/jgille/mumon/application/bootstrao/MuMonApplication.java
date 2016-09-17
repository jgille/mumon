package org.jgille.mumon.application.bootstrao;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class MuMonApplication extends Application<Configuration> {
    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        new MuMonApplication().run(args);
    }
}
