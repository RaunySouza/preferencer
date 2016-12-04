package com.github.preferencer.processor.model;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * @author raunysouza
 */
public class SharedPreferenceType {

    private TypeElement sourceElement;
    private boolean useDefault;
    private List<Preference> preferences = new ArrayList<>();

    public SharedPreferenceType(TypeElement sourceElement, boolean useDefault) {
        this.sourceElement = sourceElement;
        this.useDefault = useDefault;
    }

    public TypeElement getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(TypeElement sourceElement) {
        this.sourceElement = sourceElement;
    }

    public boolean isUseDefault() {
        return useDefault;
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(Preference preference) {
        preferences.add(preference);
    }

    public String getName() {
        return sourceElement.getSimpleName().toString();
    }

}
