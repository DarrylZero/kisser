/**
 * Created by Steam engine corp. in 08.10.2011 22:10:11
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.defaultimplementors;

import org.junit.Test;
import ru.steamengine.rtti.basetypes.*;

public class RegFactoryPlantTest {

    public class A {
        private String name;
    }

    public class B extends A {
    }

    public class C extends B {
    }


    @Test
    public void testRegNames() {
        Pair<String, RegistryFactory>[] factories = RegFactoryPlant.getFactories();
        for (Pair<String, RegistryFactory> pair : factories) {
            RegistryFactory f = pair.getObjTwo();
            RegistryFactory.RegistryPair registryPair = f.getRegistryPair();


            Registry registry = registryPair.getRegistry();
            RegistryUser registryUser = registryPair.getRegistryUser();

            registry.regNameProcessor(A.class, new ObjectNameProcessor<A>() {
                @Override
                public String getName(A a) {
                    return a.name;
                }

                @Override
                public void setName(A a, String name) {
                    a.name = name;
                }
            });


            ObjectNameProcessor aNameProcessorProcessor = registryUser.getNameProcessor(A.class);
            Assert.assertTrue(aNameProcessorProcessor != null);

            ObjectNameProcessor bNameProcessorProcessor = registryUser.getNameProcessor(B.class);
            Assert.assertTrue(aNameProcessorProcessor == bNameProcessorProcessor);

            ObjectNameProcessor ÒNameProcessorProcessor = registryUser.getNameProcessor(C.class);
            Assert.assertTrue(bNameProcessorProcessor == ÒNameProcessorProcessor);

            Assert.assertTrue(aNameProcessorProcessor == ÒNameProcessorProcessor);

            A a = new A();
            aNameProcessorProcessor.setName(a, "objectName");
            String value = aNameProcessorProcessor.getName(a);
            Assert.assertTrue(value != null);
            Assert.assertTrue("objectName".equals(value));


        }


    }

    @Test
    public void testGetObjectName() {
        // Add your code here
    }

}
