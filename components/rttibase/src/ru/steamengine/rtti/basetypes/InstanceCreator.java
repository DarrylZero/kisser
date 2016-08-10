package ru.steamengine.rtti.basetypes;

/**
 * Created by Steam engine corp 12.07.2009 21:35:45
 * <p/>
 * Public API
 *
 * @author Christopher Marlowe
 */
public interface InstanceCreator {
    /**
     * @return class string ident
     *         Value must contain lain symbols only, has no whitespaces, dots or start with number
     *         ident must be inique for application.
     *         one ident for one class
     *         <br>
     *         <br> <b> right idents: </b> 
     *         <br> 1. RoadToParadise
     *         <br> 2. LordPalace
     *         <br> 3. Leader
     *         <br> 4. Dealer2
     *         <br> 5. Dealer2Customer
     *         <br>
     *         <br>
     *         <br> <b> wrong idents:</b>
     *         <i>
     *         <br> 1. 2Morrow
     *         <br> 2. Life is bad
     *         <br> 3. &this@Is!wrong)identifier
     *         </i>
     */
    String classIdent();

    /**
     * @return registered class
     */
    Class instanceClass();

    /**
     * @return new class instance
     * @throws IllegalArgumentException if creation is not possible due to errors
     */
    Object newInstance() throws IllegalArgumentException;


    /**
     * @return new typed class instance
     * @throws IllegalArgumentException if creation is not possible due to errors
     */
    <T> T newTypedInstance() throws IllegalArgumentException;
}
