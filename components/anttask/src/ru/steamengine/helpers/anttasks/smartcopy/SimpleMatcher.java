package ru.steamengine.helpers.anttasks.smartcopy;

/**
 * Created by Steam engine corp. in 17.07.2010 22:36:14
 *
 * @author Christopher Marlowe
 */
public class SimpleMatcher implements ComplexMatcher {
                                             
    @Override
    public boolean matches(String content) {
        return true;
    }

}
