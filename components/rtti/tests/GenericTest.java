import org.junit.Test;
import ru.steamengine.rtti.basetypes.Assert;
import ru.steamengine.rtti.basetypes.ListedItem;
import ru.steamengine.rtti.defaultimplementors.DefaultListedItem;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;

/**
 * Created by Steam engine corp. in 18.06.2011 2:31:15
 *
 * @author Christopher Marlowe
 */
public class GenericTest {

    private final ListedItem<Collection, String> stringListedItem = new ListedItem<Collection, String>() {

        @Override
        public String[] getItems(Collection collection) {
            return null;
        }

        @Override
        public void beginUpdate(Collection collection) {
        }

        @Override
        public void endUpdate(Collection collection) {
        }

        @Override
        public void clear(Collection collection) {
        }

        @Override
        public String newItem(Collection collection) {
            return null;
        }

        @Override
        public void setItemValue(Collection collection, String s, int index) {
        }

        @Override
        public Class<String> getItemClass() {
            return null;
        }
    };

    private final ListedItem<Collection, String> stringListedItem2 = new DefaultListedItem<Collection, String>() {

        @Override
        public String[] getItems(Collection collection) {
            return null;
        }

        @Override
        public void beginUpdate(Collection collection) {
        }

        @Override
        public void endUpdate(Collection collection) {
        }

        @Override
        public void clear(Collection collection) {
        }

        @Override
        public String newItem(Collection collection) {
            return null;
        }

        @Override
        public void setItemValue(Collection collection, String s, int index) {
        }

        @Override
        public Class<String> getItemClass() {
            return null;
        }
    };

    private class TestA<A, B, C, D> extends DefaultListedItem<A, B> {
    }


    @Test
    public void test() {


        Class<?> itemType = getListItemType(stringListedItem);
        if (itemType != String.class)
            throw new IllegalArgumentException();


    }

    @Test
    public void test2() {
        Assert.assertTrue(getListItemType(stringListedItem) == String.class);
        Assert.assertTrue(getItemType(ListedItem.class) == Object.class);
        Assert.assertTrue(getListItemType(stringListedItem2) == String.class);


        Assert.assertTrue(getItemType(new TestA<Long, String, Boolean, Integer>().getClass()) == String.class);


    }


    public static Class<?> getItemType(Class<? extends ListedItem> aClass) {

//        aClass.getTypeParameters()
        TypeVariable<? extends Class<? extends ListedItem>>[] typeVariables = aClass.getTypeParameters();

        for (Type type : aClass.getGenericInterfaces())
            if ((type instanceof ParameterizedType) && (((ParameterizedType) type).getRawType() == ListedItem.class)) {
                Type[] arguments = ((ParameterizedType) type).getActualTypeArguments();
                return (Class) arguments[1];
            }

        Type type = aClass.getGenericSuperclass();
        if (type instanceof ParameterizedType && (((ParameterizedType) type).getRawType() == ListedItem.class)) {
                        
        }





        return Object.class;
    }


    public static Class<?> getListItemType(ListedItem listedItem) {
        return getItemType(listedItem.getClass());
    }

}
