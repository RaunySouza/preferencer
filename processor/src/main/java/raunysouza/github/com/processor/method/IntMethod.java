package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class IntMethod implements SharedPreferencesMethod {

    @Override
    public boolean accept(String referencedType) {
        return "int".equals(referencedType) || "java.lang.Integer".equals(referencedType);
    }

    @Override
    public String getGetterMethodName() {
        return "getInt";
    }

    @Override
    public String getPutMethodName() {
        return "putInt";
    }

    @Override
    public Object getDefaultValue() {
        return 0;
    }

    @Override
    public Object getDefaultValue(String value) {
        return Integer.parseInt(value);
    }
}
