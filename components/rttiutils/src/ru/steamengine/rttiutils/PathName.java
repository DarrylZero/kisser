package ru.steamengine.rttiutils;

/**
 * Created by Steam engine corp. in 29.06.2010 21:55:11
 *
 * @author Christopher Marlowe
 */
public class PathName {

    public enum Kind {

        jar,

        file

    }

    private final Kind kind;

    private final String path;

    private final String name;

    public PathName(Kind kind,
                    String path,
                    String name) {
        if (kind == null)
            throw new NullPointerException("kind is null");
        if (path == null)
            throw new NullPointerException("path is null");
        if (name == null)
            throw new NullPointerException("name is null");

        this.kind = kind;
        this.path = path;
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Kind getKind() {
        return kind;
    }

}
