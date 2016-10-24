package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class FloatMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getFloat";
    }

    @Override
    public String put() {
        return "putFloat";
    }

    @Override
    public String defaultValue() {
        return "0.0f";
    }
}
