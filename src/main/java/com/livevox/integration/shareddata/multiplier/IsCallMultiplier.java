/*
 * Copyright (c) 2020. Luis Felipe Sosa Alvarez. All rights reserved to LiveVox.
 * Use this subject to license terms.
 *
 * IS-CALL-MULTIPLIER
 */

package com.livevox.integration.shareddata.multiplier;

import com.livevox.integration.shareddata.multiplier.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * The type Integration call multiplier application.
 *
 * @autor Luis Felipe Sosa Alvarez lsosa@livevox.com
 */
@SpringBootApplication(exclude ={JmsAutoConfiguration.class, ActiveMQAutoConfiguration.class})
@Import({AppConfig.class})
public class IsCallMultiplier {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(IsCallMultiplier.class, args);
	}
}
