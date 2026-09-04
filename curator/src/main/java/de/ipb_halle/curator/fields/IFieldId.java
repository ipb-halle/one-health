/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * JCrawler
 * JCrawler is a project to efficiently crawl large file systems.
 */
package de.ipb_halle.curator.fields;

import java.util.UUID;

/**
 *
 * @author fblocal
 */
public interface IFieldId {

    UUID getElementId();

    int getFieldId();

    int getOrder();

}
