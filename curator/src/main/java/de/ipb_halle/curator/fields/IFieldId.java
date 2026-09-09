/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import java.util.UUID;

/**
 *
 * @author fblocal
 */
public interface IFieldId {

    public UUID getElementId();

    public int getFieldId();

    public int getOrder();
}
