/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */
package de.ipb_halle.curator.fields.text;

import de.ipb_halle.curator.fields.FieldDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class TextFieldConverter {

   public TextField createEntity(TextFieldDTO fieldDTO) {
        return fieldDTO.createEntity();
    }

    public FieldDTO createDTO(TextField field) {
        return TextFieldDTO.createDTO(field);
    }

    public List<FieldDTO> createDTOs(List<TextField> fields) {
        return fields.stream().map(f -> createDTO(f)).collect(Collectors.toList());
    }
}
