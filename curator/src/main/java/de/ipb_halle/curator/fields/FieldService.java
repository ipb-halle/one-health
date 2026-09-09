/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields;

import de.ipb_halle.curator.fields.integer.IntegerField;
import de.ipb_halle.curator.fields.integer.IntegerFieldConverter;
import de.ipb_halle.curator.fields.integer.IntegerFieldRepository;
import de.ipb_halle.curator.fields.text.TextField;
import de.ipb_halle.curator.fields.text.TextFieldConverter;
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
    private IntegerFieldConverter integerConverter;

    @Autowired
    private TextFieldRepository textRepository;

    @Autowired
    private TextFieldConverter textConverter;

    @Transactional(readOnly = true)
    public List<FieldDTO> loadFields(UUID elementId) {
        List<FieldDTO> results = new ArrayList<> ();
        List<TextField> textFields = textRepository.findTextFields(elementId);
        results.addAll(textConverter.createDTOs(textFields));

        List<IntegerField> integerFields = integerRepository.findIntegerFields(elementId);
        results.addAll(integerConverter.createDTOs(integerFields));

        return results;
    }
}
