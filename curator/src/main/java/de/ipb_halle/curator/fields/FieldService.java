/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.fields.integer.IntegerFieldRepository;
import de.ipb_halle.curator.fields.text.TextFieldRepository;
import de.ipb_halle.curator.metadata.MetadataRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fblocal
 */
@Service
public class FieldService {

    @Autowired
    private IntegerFieldRepository integerRepository;

    @Autowired
    private TextFieldRepository textRepository;

    @Autowired
    private MetadataRegistry registry;

    @Transactional(readOnly = true)
    public List<Field> loadFields(UUID elementId) {
        List<Field> results = new ArrayList<> ();
        results.addAll(textRepository.findTextFields(elementId));
        results.addAll(integerRepository.findIntegerFields(elementId));
        return results;
    }
}
