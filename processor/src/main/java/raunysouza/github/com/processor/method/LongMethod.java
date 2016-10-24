package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class LongMethod implements SharedPreferencesMethod {
    @Override
    public boolean accept(String referencedType) {
        return "long".equals(referencedType) || "java.lang.Long".equals(referencedType);
    }

    @Override
    public String getGetterMethodName() {
        return "getLong";
    }

    @Override
    public String getPutMethodName() {
        return "putLong";
    }

    @Override
    public Object getDefaultValue() {
        return 0L;
    }

    @Override
    public Object getDefaultValue(String value) {
        return Long.parseLong(value);
    }


}
