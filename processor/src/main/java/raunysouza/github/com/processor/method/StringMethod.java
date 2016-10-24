package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class StringMethod implements SharedPreferencesMethod {
    @Override
    public boolean accept(String referencedType) {
        return "java.lang.String".equals(referencedType);
    }

    @Override
    public String getGetterMethodName() {
        return "getString";
    }

    @Override
    public String getPutMethodName() {
        return "putString";
    }

    @Override
    public Object getDefaultValue() {
        return "\"\"";
    }

    @Override
    public Object getDefaultValue(String value) {
        return "\"" + value + "\"";
    }
}
