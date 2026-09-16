package de.ipb_halle.curator;

/*
 * SPDX-License-Identifier: Apache-2.0
 * SPDX-FileCopyrightText: 2026 Leibniz-Institut f. Pflanzenbiochemie
 *
 * Curator
 * Curator provides an ETL pipeline to the One Health project.
 */


import de.ipb_halle.curator.fields.integer.IntegerFieldIoTest;
import de.ipb_halle.curator.fields.integer.IntegerFieldReader;
import de.ipb_halle.curator.fields.text.TextFieldIoTest;
import de.ipb_halle.curator.fields.text.TextFieldReader;
import de.ipb_halle.curator.onehealth.ElementIoTest;
import de.ipb_halle.curator.onehealth.ElementReader;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author fblocal
 */
@Component
public class TestDataCreator {
    @Autowired
    private ElementReader elementReader;

    @Autowired
    private IntegerFieldReader integerReader;

    @Autowired
    private TextFieldReader textReader;

    public void setupFull(DbTestHelper helper) throws IOException {
        helper.deleteElements();
        InputStream input = ElementReader.class.getResourceAsStream(ElementIoTest.ELEMENTS_CSV);
        elementReader.read(input);
        input = TextFieldReader.class.getResourceAsStream(TextFieldIoTest.TEXTFIELDS_CSV);
        textReader.read(input);
        input = IntegerFieldReader.class.getResourceAsStream(IntegerFieldIoTest.INTEGERFIELDS_CSV);
        integerReader.read(input);
    }

    public void setupElements(DbTestHelper helper) throws IOException {
        helper.deleteElements();
        InputStream input = ElementReader.class.getResourceAsStream(ElementIoTest.ELEMENTS_CSV);
        elementReader.read(input);
    }
}
