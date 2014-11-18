package org.oregami.dropwizard;

import com.github.toastshaman.dropwizard.auth.jwt.JWTAuthProvider;
import com.github.toastshaman.dropwizard.auth.jwt.JsonWebTokenParser;
import com.github.toastshaman.dropwizard.auth.jwt.JsonWebTokenValidator;
import com.github.toastshaman.dropwizard.auth.jwt.hmac.HmacSHA512Verifier;
import com.github.toastshaman.dropwizard.auth.jwt.model.JsonWebToken;
import com.github.toastshaman.dropwizard.auth.jwt.parser.DefaultJsonWebTokenParser;
import com.github.toastshaman.dropwizard.auth.jwt.validator.ExpiryValidator;
import com.google.common.base.Optional;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.oregami.data.DatabaseFiller;
import org.oregami.resources.SecuredResource;
import org.oregami.resources.TaskResource;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;
import java.security.SecureRandom;
import java.util.EnumSet;
import java.util.Random;

public class ToDoApplication extends Application<ToDoConfiguration> {

	public static final String JPA_UNIT =
            "data";
	        // "dataMysql";

  public static byte[] authKey;

	private static final JpaPersistModule jpaPersistModule = new JpaPersistModule(JPA_UNIT);

	private GuiceBundle<ToDoConfiguration> guiceBundle;

	PersistService persistService = null;

	public static void main(String[] args) throws Exception {
		new ToDoApplication().run(args);
	}

	@Override
	public String getName() {
		return "hello-world";
	}

	@Override
	public void initialize(Bootstrap<ToDoConfiguration> bootstrap) {
		guiceBundle = GuiceBundle.<ToDoConfiguration> newBuilder()
				.addModule(new ToDoGuiceModule())
				.addModule(jpaPersistModule).enableAutoConfig("org.oregami")
				.setConfigClass(ToDoConfiguration.class).build();

		bootstrap.addBundle(guiceBundle);

//		persistService = guiceBundle.getInjector().getInstance(PersistService.class);
//      persistService.start();
		DatabaseFiller.getInstance().initData();

    initAuthKey();

	}

  private void initAuthKey() {
    Random random = new SecureRandom();
    ToDoApplication.authKey = new byte[64];
    random.nextBytes(ToDoApplication.authKey);
    System.out.println("AuthKey initialized!");
  }

  @Override
	public void run(ToDoConfiguration configuration, Environment environment) {

        environment.servlets().addFilter("persistFilter", guiceBundle.getInjector().getInstance(PersistFilter.class)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
	    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
	    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.EXPOSED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,Location");
	    filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
//
//	    filter.setInitParameter("allow", "GET,PUT,POST,DELETE,OPTIONS");
//	    filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months

      environment.jersey().register(guiceBundle.getInjector().getInstance(TaskResource.class));




    final JsonWebTokenParser tokenParser = new DefaultJsonWebTokenParser();
    final HmacSHA512Verifier tokenVerifier = new HmacSHA512Verifier(ToDoApplication.authKey);
    final JsonWebTokenValidator expiryValidator = new ExpiryValidator();
    environment.jersey().register(new JWTAuthProvider<User>(new Authenticator<JsonWebToken, User>() {
      @Override
      public Optional<User> authenticate(JsonWebToken token) throws AuthenticationException {
        // Provide your own implementation to lookup users based on the principal attribute in the
        // JWT Token. E.g.: lookup users from a database etc.
        // This method will be called once the token's signature has been verified

        // In case you want to verify different parts of the token you can do that here.
        // E.g.: Verifying that the provided token has not expired.
       /*
        try {

          expiryValidator.validate(token);
        } catch (TokenExpiredException e) {
          throw new AuthenticationException(e.getMessage(), e);
        }
        */

        if ("good-guy".equals(token.claim().getParameter("principal"))) {
          return Optional.of(new User("good-guy"));
        }

        return Optional.absent();
      }
    }, tokenParser, tokenVerifier, "realm"));

    environment.jersey().register(new SecuredResource(ToDoApplication.authKey));


    //environment.jersey().register(new SecureResource());



	}

	public static JpaPersistModule createJpaModule() {
		return jpaPersistModule;
	}

}
