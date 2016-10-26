package com.github.preferencer.processor.exception;

import javax.lang.model.element.Element;

/**
 * @author raunysouza
 */
public class ProcessingException extends Exception {

    private Element element;

    public ProcessingException(String message, Element element) {
        super(message);
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
