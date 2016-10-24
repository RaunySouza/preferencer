package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class StringSetMethod implements SharedPreferencesMethod {
    @Override
    public boolean accept(String referencedType) {
        return "java.util.Set<java.lang.String>".equals(referencedType);
    }

    @Override
    public String getGetterMethodName() {
        return "getStringSet";
    }

    @Override
    public String getPutMethodName() {
        return "putStringSet";
    }

    @Override
    public Object getDefaultValue() {
        return "new java.util.HashSet<String>()";
    }

    @Override
    public Object getDefaultValue(String value) {
        return getDefaultValue();
    }
}
