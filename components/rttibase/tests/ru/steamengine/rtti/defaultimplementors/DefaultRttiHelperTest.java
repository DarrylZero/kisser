package ru.steamengine.rtti.defaultimplementors;

import org.junit.Assert;
import org.junit.Test;
import ru.steamengine.rtti.basetypes.Registered;
import ru.steamengine.rtti.basetypes.Registry;

public class DefaultRttiHelperTest {

    public static class A {
        private static void reg() {

        }
    }

    public static class B {

        @Registered
        private static void reg(Registry registry) {

        }
    }

    public static class C {

        @Registered
        private static void reg() {

        }
    }

    public static class D {
        public static class E {
            public static class F {
                public static class I {
                    public static class J {
                        public static class K {
                            public static class L {
                                @Registered
                                private static void reg() {
                                                                        
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static class DD {
        public static class E {
            public static class F {
                public static class I {
                    public static class J {
                        public static class K {
                            public static class L {
                                @Registered
                                private static void reg() {

                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Test
    public void test0() {
        Assert.assertFalse(DefaultRttiHelper.hasRegistrationMethods(A.class));
    }

    @Test
    public void test1() {
        Assert.assertTrue(DefaultRttiHelper.hasRegistrationMethods(B.class));
    }

    @Test
    public void test2() {
        Assert.assertFalse(DefaultRttiHelper.hasRegistrationMethods(C.class));
    }

    @Test
    public void test3() {
        Assert.assertFalse(DefaultRttiHelper.hasRegistrationMethods(this.getClass()));
    }

    @Test
    public void test4() {
        Assert.assertFalse(DefaultRttiHelper.hasRegistrationMethods(D.E.F.I.J.K.L.class));
    }

    @Test
    public void testCallRegistrationMethods() {
        synchronized (RegClass.lock) {
            RegClass.reset();
            DefaultRttiHelper.callRegistrationMethods(RegClass.class, null);
            RegClass.checkCorectcount();
        }
    }
}
