package com.rg.microservices.otpservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.redis.port:6397}")
    private Integer redisPort;


    @Value("${spring.redis.channel-name:emailSender}")
    private String redisChannelName;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);

        return new LettuceConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return template;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(redisChannelName);
    }

}
