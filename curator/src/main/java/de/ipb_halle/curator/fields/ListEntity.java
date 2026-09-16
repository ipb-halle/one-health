/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author fblocal
 */
public class ListEntity implements FieldEntity<List> {

    private List<FieldDTO> values;
    public ListEntity(List<FieldDTO> dtos) {
        values = dtos;
    }

    @Override
    public IFieldId getId() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public List<FieldDTO> getValue() {
        return values;
    }
}
