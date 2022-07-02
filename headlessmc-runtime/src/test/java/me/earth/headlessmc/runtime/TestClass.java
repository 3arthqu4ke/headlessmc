package me.earth.headlessmc.runtime;

@SuppressWarnings("unused")
public class TestClass<T> {
    public static final String FIRST_CTR = "first ctr";
    public static final String STRING_CTR = "string ctr";
    public static final String NOARGS_CTR = "noargs ctr";
    public static final String TWO_ARGS_CTR = "two args ctr";

    public static final String METHOD_WO_ARGS = "method wo args";
    public static final String METHOD_W_ARGS = "method w args";

    private static final String STATIC_FINAL_FIELD = "test";
    static int STATIC_FIELD;
    protected final T final_field = null;
    public long field_with_100 = 100L;
    public String value;
    public final String ctr;
    public T secondVal;
    public String calledMethod;
    public String parameter;

    public TestClass(T constructorArg) {
        this.ctr = FIRST_CTR;
    }

    public TestClass(String arg) {
        this.ctr = STRING_CTR;
        this.value = arg;
    }

    public TestClass(String arg, T secondArg) {
        this.ctr = TWO_ARGS_CTR;
        this.secondVal = secondArg;
        this.value = arg;
    }

    public TestClass() {
        this.ctr = NOARGS_CTR;
    }

    public void setter(String value) {
        this.value = value;
    }

    public String method() {
        return "";
    }

    static String staticMethod(long arg, String... args) {
        return "";
    }

    public String methodWithSameName() {
        this.calledMethod = METHOD_WO_ARGS;
        return this.calledMethod;
    }

    public String methodWithSameName(String parameter) {
        this.parameter = parameter;
        this.calledMethod = METHOD_W_ARGS;
        return this.calledMethod;
    }

}