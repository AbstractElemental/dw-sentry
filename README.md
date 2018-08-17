# dw-sentry
A dropwizard appender for Sentry.io


## Setup instructions

1. Once we add this to Maven Central, slap this in your pom file.
2. Create a file named `io.dropwizard.logging.AppenderFactory` in `src/main/resources/META-INF/services`.
Inside this file slap in the following line `com.abstractelemental.dropwizard.sentry.SentryAppenderFactory`.
3. Open up your Dropwizard configuration file and add in a new appender named `sentry`.


```yaml
appenders:
  - type: sentry
    threshold: ERROR
    dsn: https://user:pass@sentry.io/appid
    environment: production
    release: 1.0.0
    serverName: 10.0.0.1
```
