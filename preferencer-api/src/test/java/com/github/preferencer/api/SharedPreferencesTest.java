package com.github.preferencer.api;

import android.content.Context;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * @author raunysouza
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SharedPreferencesTest {

    @Test
    public void test() {
        Context context = RuntimeEnvironment.application;
        PreferenceMock preference = new PreferenceMock(context.getSharedPreferences("test", Context.MODE_PRIVATE));

        Assert.assertTrue(preference.name().get().isEmpty());
        Assert.assertEquals("default", preference.nameWithDefault().get());
        Assert.assertTrue(preference.people().get().isEmpty());
        Assert.assertEquals(4, preference.animals().get().size());

        preference.name().putIfAbsent("NewName");
        Assert.assertEquals("NewName", preference.name().get());

        preference.name().put("name");
        Assert.assertEquals("name", preference.name().get());

        preference.name().putIfAbsent("OtherName");
        Assert.assertEquals("name", preference.name().get());

        preference.name().remove();
        Assert.assertFalse(preference.name().exists());
    }

    @Test
    public void testBatchSave() {
        Context context = RuntimeEnvironment.application;
        PreferenceMock preference = new PreferenceMock(context.getSharedPreferences("test_batch", Context.MODE_PRIVATE));
        preference.edit()
                .name().put("name")
                .nameWithDefault().put("name")
                .name().remove()
                .apply();

        Assert.assertTrue(preference.name().exists());
        Assert.assertEquals("name", preference.nameWithDefault().get());
    }
}
