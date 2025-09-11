package com.kadioglumf.authservice.core.config;

import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskPoolConfiguration implements AsyncConfigurer {

  private static final int CORE_POOL_SIZE = 50;
  private static final int MAX_POOL_SIZE = 100;
  private static final int QUEUE_CAPACITY = 50;

  @Override
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
    threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
    threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
    threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    threadPoolTaskExecutor.setThreadNamePrefix("Auth-Service-Executor-");
    threadPoolTaskExecutor.initialize();
    return new ContextAwareExecutor(threadPoolTaskExecutor);
  }

  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return (throwable, method, objects) ->
        System.err.println("Async exception in method: " + method.getName() + ", " + throwable);
  }

  static class ContextAwareExecutor implements Executor {
    private final Executor delegate;

    public ContextAwareExecutor(Executor delegate) {
      this.delegate = delegate;
    }

    @Override
    public void execute(Runnable task) {
      Locale locale = LocaleContextHolder.getLocale();

      delegate.execute(
          () -> {
            try {
              LocaleContextHolder.setLocale(locale);
              task.run();
            } finally {
              LocaleContextHolder.resetLocaleContext();
            }
          });
    }
  }
}
