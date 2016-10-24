package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class FloatMethod implements SharedPreferencesMethod {
    @Override
    public boolean accept(String referencedType) {
        return "float".equals(referencedType) || "java.lang.Float".equals(referencedType);
    }

    @Override
    public String getGetterMethodName() {
        return "getFloat";
    }

    @Override
    public String getPutMethodName() {
        return "putFloat";
    }

    @Override
    public Object getDefaultValue() {
        return 0.0;
    }

    @Override
    public Object getDefaultValue(String value) {
        return Float.parseFloat(value);
    }
}
