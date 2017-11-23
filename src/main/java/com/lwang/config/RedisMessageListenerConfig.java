package com.lwang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * RedisMessageListenerConfig.class
 *
 * @author lwang
 * @date 17-11-23.
 */
public class RedisMessageListenerConfig {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    MessageListener messageListener;

    @Bean
    public RedisMessageListenerContainer configRedisMessageListenerContainer(Executor executor){

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置Redis的连接工厂
        container.setConnectionFactory(stringRedisTemplate.getConnectionFactory());
        // 设置监听使用的线程池
        container.setTaskExecutor(executor);
        // 设置监听的Topic
        ChannelTopic channelTopic = new ChannelTopic("__keyevent@0__:expired");
        // 设置监听器
        container.addMessageListener(messageListener, channelTopic);
        return container;
    }


    // 配置线程池
    @Bean
    public Executor myTaskAsyncPool() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("myThreadPool");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


}
