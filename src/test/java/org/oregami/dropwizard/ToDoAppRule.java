package org.oregami.dropwizard;

import io.dropwizard.testing.junit.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.oregami.rest.RestTestHelper;
import org.oregami.util.StartHelper;

/**
 * Created by sebastian on 06.04.15.
 */
public class ToDoAppRule extends DropwizardAppRule {

    public ToDoAppRule(Class applicationClass, String configPath, ConfigOverride... configOverrides) {
        super(applicationClass, configPath, configOverrides);
        StartHelper.init(configPath);
        RestTestHelper.initRestAssured();
    }


}
