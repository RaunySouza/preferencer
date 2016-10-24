package raunysouza.github.com.processor.model;

import com.google.common.base.CaseFormat;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;

/**
 * @author raunysouza
 */
public class SharedPreferenceClass {

    private boolean useDefault;
    private boolean isInterface;
    private TypeElement sourceElement;
    private List<Preference> preferences = new ArrayList<>();

    public String getName() {
        String className = sourceElement.getSimpleName().toString();
        if (isInterface) {
            if (className.startsWith("I")) {
                return className.substring(1);
            }
        } else {
            if (className.startsWith("Abstract")) {
                return className.substring(8);
            } else if (className.startsWith("Base")) {
                return className.substring(4);
            }
        }

        return className + "Preference";
    }

    public String getPreferenceName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, getName());
    }

    public boolean isUseDefault() {
        return useDefault;
    }

    public void setUseDefault(boolean useDefault) {
        this.useDefault = useDefault;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    public TypeElement getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(TypeElement sourceElement) {
        this.sourceElement = sourceElement;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

    public void addPreference(Preference preference) {
        this.preferences.add(preference);
    }
}
