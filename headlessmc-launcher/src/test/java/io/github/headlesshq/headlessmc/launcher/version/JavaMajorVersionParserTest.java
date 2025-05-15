package io.github.headlesshq.headlessmc.launcher.version;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JavaMajorVersionParserTest {
    private final JavaMajorVersionParser parser = new JavaMajorVersionParser();

    @Test
    public void testParse() {
        assertNull(parser.parse(null));
        assertNull(parser.parse(new JsonArray()));
        JsonObject object = new JsonObject();
        assertNull(parser.parse(object));
        object.add("test", new JsonArray());
        assertNull(parser.parse(object));
        object.add("majorVersion", new JsonPrimitive(8));
        assertEquals(8, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive(17));
        assertEquals(17, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive(17.0));
        assertEquals(17, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive(8));
        assertEquals(8, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive("8"));
        assertEquals(8, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive("9.0"));
        assertEquals(9, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive("17.0"));
        assertEquals(17, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive("1.8"));
        assertEquals(8, parser.parse(object));
        object.add("majorVersion", new JsonPrimitive(1.8));
        assertEquals(8, parser.parse(object));
    }

}
