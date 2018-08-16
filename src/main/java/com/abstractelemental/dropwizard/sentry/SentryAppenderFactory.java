package com.abstractelemental.dropwizard.sentry;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.dropwizard.logging.AbstractAppenderFactory;
import io.dropwizard.logging.async.AsyncAppenderFactory;
import io.dropwizard.logging.filter.LevelFilterFactory;
import io.dropwizard.logging.layout.LayoutFactory;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import io.sentry.logback.SentryAppender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@JsonTypeName("sentry")
public class SentryAppenderFactory extends AbstractAppenderFactory<ILoggingEvent> {

    private static final String APPENDER_NAME = "sentry";
    private static final String DEFAULT_ENVIRONMENT = "development";

    @NotNull
    @JsonProperty
    private String dsn = null;

    @NotNull
    @JsonProperty
    private String release = null;

    @JsonProperty
    private String environment = DEFAULT_ENVIRONMENT;

    @NotNull
    @JsonProperty
    private String serverName = null;

    @JsonProperty
    private List<String> stacktracePackages = Collections.emptyList();

    @Override
    public Appender<ILoggingEvent> build(LoggerContext context, String applicationName, LayoutFactory<ILoggingEvent> layoutFactory,
                                         LevelFilterFactory<ILoggingEvent> levelFilterFactory, AsyncAppenderFactory<ILoggingEvent> asyncAppenderFactory) {

        SentryClient client = SentryClientFactory.sentryClient(dsn);
        client.setEnvironment(environment);
        client.setRelease(release);
        client.setEnvironment(environment);
        client.setServerName(serverName);

        Sentry.setStoredClient(client);

        SentryAppender appender = new SentryAppender();
        appender.setName(APPENDER_NAME);
        appender.setContext(context);
        appender.addFilter(levelFilterFactory.build(threshold));
        appender.start();

        return wrapAsync(appender, asyncAppenderFactory, context);
    }
}
