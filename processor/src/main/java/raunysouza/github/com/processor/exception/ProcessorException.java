package raunysouza.github.com.processor.exception;

import javax.lang.model.element.Element;

/**
 * @author raunysouza
 */
public class ProcessorException extends Exception {

    private Element element;

    public ProcessorException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
