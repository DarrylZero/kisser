/**
 * Created by Steam engine corp. in 24.10.2011 23:02:57
 *
 * @author Christopher Marlowe
 */


package ru.steamengine.rtti.defaultimplementors;

import org.junit.Test;
import ru.steamengine.rtti.basetypes.*;

public class ATest {

    @Test
    public void test1() {
        Pair<String, RegistryFactory>[] fPairs = RegFactoryPlant.getFactories();
        for (Pair<String, RegistryFactory> fPair : fPairs) {
            RegistryFactory factory = fPair.getObjTwo();
            RegistryFactory.RegistryPair pair = factory.getRegistryPair();
            Registry registry = pair.getRegistry();
            RegistryUser registryUser = pair.getRegistryUser();


            ClassPropRegistrySlot slot = registry.classProps(ATest.class);
            slot.regProp("items", new DefaultListedItem() {

            });



        }

    }

}
