package org.jgille.mumon.application.bootstrao;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MuMonApplication extends Application<MuMonConfiguration> {

    public void initialize(Bootstrap<MuMonConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
    }

    @Override
    public void run(MuMonConfiguration configuration, Environment environment) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        new MuMonApplication().run(args);
    }
}
