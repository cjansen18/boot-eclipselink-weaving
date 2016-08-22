/*
 * Copyright 2015-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.jpa.eclipselink;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Spring Boot application that uses EclipseLink as the JPA provider.
 *
 * @author Oliver Gierke
 * @author Jeremy Rickard
 */
@SpringBootApplication
public class Application extends JpaBaseConfiguration {

	/**
	 * @param dataSource
	 * @param properties
	 * @param jtaTransactionManagerProvider
	 */
	protected Application(DataSource dataSource, JpaProperties properties,
			ObjectProvider<JtaTransactionManager> jtaTransactionManagerProvider) {
		super(dataSource, properties, jtaTransactionManagerProvider);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#createJpaVendorAdapter()
	 */
	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		return vendorAdapter;
	}
	/* 
	 * (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration#getVendorProperties()
	 */
	@Override
	protected Map<String, Object> getVendorProperties() {
		// Turn off dynamic weaving to disable LTW lookup in static weaving mode
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("eclipselink.application-location", "C:\\sql");
		map.put("eclipselink.create-ddl-jdbc-file-name", "create.sql");
		map.put("eclipselink.weaving", detectWeavingMode());
		//map.put(PersistenceUnitProperties.WEAVING, detectWeavingMode());
		//can also use drop-and-create-tables - will output to eclipselink.application-location. create-or-extend-tables
		map.put("eclipselink.ddl-generation", "drop-and-create-tables");
		//both
		map.put("eclipselink.ddl-generation.output-mode", "both");
		map.put("eclipselink.logging.level", "fine");

		return map;
	}


	protected transient Logger logger = LoggerFactory.getLogger(getClass());
	private String detectWeavingMode() {
		boolean setting = InstrumentationLoadTimeWeaver.isInstrumentationAvailable();
		logger.info("===> EclipseLink instrumentation is set to:" + setting);

		//true: if spring-agent.jar is found on the classpath
		return InstrumentationLoadTimeWeaver.isInstrumentationAvailable() ? "true" : "static";
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	public static void main(String[] args) {

		CustomerRepository repository = SpringApplication.run(Application.class, args).getBean(CustomerRepository.class);
		repository.save(new Customer("Richard", "Feynman"));

		System.out.println(repository.findAll());
	}
}
