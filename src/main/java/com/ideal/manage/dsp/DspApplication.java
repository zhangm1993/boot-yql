package com.ideal.manage.dsp;

import com.ideal.manage.dsp.repository.framework.FrameworkJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.ideal.manage.dsp.repository"},
	repositoryFactoryBeanClass = FrameworkJpaRepositoryFactoryBean.class)
@SpringBootApplication
@EnableCaching
public class DspApplication {

	public static void main(String[] args) {
		SpringApplication.run(DspApplication.class, args);
	}
}
