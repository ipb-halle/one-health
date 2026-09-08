/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator;

import org.springframework.boot.SpringApplication;

public class TestCuratorApplication {

	public static void main(String[] args) {
		SpringApplication.from(CuratorApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
