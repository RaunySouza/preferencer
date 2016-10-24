package raunysouza.github.com.processor.method;

/**
 * @author raunysouza
 */
public class LongMethod implements SharedPreferencesMethod {

    @Override
    public String get() {
        return "getLong";
    }

    @Override
    public String put() {
        return "putLong";
    }

    @Override
    public String defaultValue() {
        return "0L";
    }
}
