package ru.steamengine.helpers.anttasks.smartcopy;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

import ru.steamengine.helpers.anttasks.common.ConstructionUtils;

/**
 * Created by Steam engine corp. in 09.07.2010 23:39:36
 *
 * @author Christopher Marlowe
 */
public class CopyItem extends DataType {

    private enum CopyItemType {

        undefined,

        word,

        regexp,

        complexMatcher
    }

    private CopyItemType type = CopyItemType.undefined;

    private String value;

    private Pattern regexp;

    private ComplexMatcher matcher;

    public CopyItemType getType() {
        return type;
    }

    public void setRegExp(String regExp) {
        if (regExp == null)
            throw new BuildException("regExp is null");
        this.regexp = Pattern.compile(regExp);
        this.value = null;
        this.matcher = null;
        this.type = CopyItemType.regexp;

    }

    public void setValue(String value) {
        if (value == null)
            throw new BuildException("value is null");
        this.regexp = null;
        this.value = value;
        this.matcher = null;
        this.type = CopyItemType.word;
    }

    public void setComplexmatcher(String complexmatcher) {
        if (complexmatcher == null)
            throw new BuildException("complexmatcher is null");
        this.regexp = null;
        this.value = null;
        this.matcher = getMatcher(complexmatcher);
        this.type = CopyItemType.complexMatcher;
    }

    private ComplexMatcher getMatcher(String complexmatcher) throws BuildException {
        try {
            Class<?> clazz = Class.forName(complexmatcher);
            log("clazz = " + clazz);
            if (!ComplexMatcher.class.isAssignableFrom(clazz))
                throw new BuildException("!ComplexMatcher.class.isAssignableFrom(clazz)");


            Constructor defaultConstructor = ConstructionUtils.getDefaultConstructor(clazz);

            if (defaultConstructor == null)
                throw new BuildException("!InstPlant.hasNoParamConstructor(clazz)");

            @SuppressWarnings({"UnnecessaryLocalVariable"})
            ComplexMatcher result = ConstructionUtils.newInstance(defaultConstructor);
            return result;
        } catch (ClassNotFoundException e) {
            throw new BuildException(e.getMessage(), e);
        }
    }

    public boolean matches(String content) {
        if (content == null)
            return false;

        switch (type) {
            case word:
                return content.indexOf(value) >= 0;

            case regexp:
                return regexp.matcher(content).matches();

            case complexMatcher:
                return matcher.matches(content);

            case undefined:
                return false;

            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "CopyItem{" +
                "type=" + type +
                ", value='" + value + '\'' +
                ", regexp=" + regexp +
                ", matcher=" + matcher +
                '}';
    }

}
