package ru.steamengine.helpers.anttasks.common;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.taskdefs.Java;

/**
 * Created by Steam engine corp. in 17.09.2010 21:40:00
 *
 * @author Christopher Marlowe
 */
public class DefaultJavaBuildListener extends Java implements BuildListener {
    @Override
    public void buildStarted(BuildEvent event) {
    }

    @Override
    public void buildFinished(BuildEvent event) {
    }

    @Override
    public void targetStarted(BuildEvent event) {
    }

    @Override
    public void targetFinished(BuildEvent event) {
    }

    @Override
    public void taskStarted(BuildEvent event) {
    }

    @Override
    public void taskFinished(BuildEvent event) {
    }

    @Override
    public void messageLogged(BuildEvent event) {
    }
}
