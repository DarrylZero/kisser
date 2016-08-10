import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.util.*;

interface BaseInterface<T> {}
interface FirstArg<T1,T2> extends BaseInterface<T1>{}
interface SecondArg<T1,T2> extends BaseInterface<T2>{}

class First implements FirstArg<Number, String> {}
class Second implements SecondArg<Number, String> {}


public class Example {
    public static void main(String[] av) {
        new Example().go();
    }

    void go() {
        test(First.class);
        test(Second.class);
    }

    void test(Class<?> c1) {


        ParameterizedType t2 = (ParameterizedType) c1.getGenericInterfaces()[0];
        System.out.println(c1 + " implements " + t2 );

        Class<?> c2 = (Class<?>)t2.getRawType();
        GenericDeclaration g2 = (GenericDeclaration) c2;


        System.out.println(t2 + "  params are " + Arrays.asList(g2.getTypeParameters()));

        System.out.println("So that means");
        for(int i = 0; i<t2.getActualTypeArguments().length; i++) {
                System.out.println("Parameter " + c2.getTypeParameters()[i] + " is " + t2.getActualTypeArguments()[i]);
        }

        ParameterizedType t3 =  (ParameterizedType) c2.getGenericInterfaces()[0];
        System.out.println(t2 + "  implements " + t3);

        System.out.println("and so that means we are talking about\n" + t3.getRawType().toString() + " <");
        for(int i = 0 ; i< t3.getActualTypeArguments().length; i++) {
                System.out.println("\t" + t3.getActualTypeArguments()[i] + " -> "
                + Arrays.asList(g2.getTypeParameters()).indexOf(t3.getActualTypeArguments()[i])
                + " -> " +
                t2.getActualTypeArguments()[Arrays.asList(g2.getTypeParameters()).indexOf(t3.getActualTypeArguments()[i])]
                );
        }

        System.out.println(">");
        System.out.println();
    }

}