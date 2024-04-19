package ipbhalle.de.ontologymanagerserver.api;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Register a custom editor for String type to decode URL-encoded strings
        binder.registerCustomEditor(String.class, new StringPropertyEditor());
    }

    private static class StringPropertyEditor extends java.beans.PropertyEditorSupport {
        @Override
        public void setAsText(String text) {
            try {
                // Decode the URL-encoded string using UTF-8 encoding
                setValue(UriUtils.decode(text, "UTF-8"));
            } catch (RuntimeException e) {
                // Handle decoding exception
                e.printStackTrace();
            }
        }
    }
}
