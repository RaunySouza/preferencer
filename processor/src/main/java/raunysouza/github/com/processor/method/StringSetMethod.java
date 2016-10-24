package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class StringSetMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getStringSet";
    }

    @Override
    public String put() {
        return "putStringSet";
    }

    @Override
    public String defaultValue() {
        return "null";
    }
}
