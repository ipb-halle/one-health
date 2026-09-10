/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author fblocal
 */
public class FieldDtoIterator implements Iterator<FieldDTO> {

    private FieldDTO nextDTO;

    public FieldDtoIterator(FieldDTO starter) {
        nextDTO = starter;
    }

    @Override
    public boolean hasNext() {
        return nextDTO != null;
    }

    @Override
    public FieldDTO next() {
        if (hasNext()) {
            FieldDTO currentDTO = nextDTO;
            nextDTO = currentDTO.getLinkedFieldDTO();
            return nextDTO;
        }
        throw new NoSuchElementException();
    }

}
