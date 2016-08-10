package ru.steamengine.helpers.anttasks.delay;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Java;

/**
 * Created by Steam engine corp. in 17.09.2010 23:18:19
 *
 * @author Christopher Marlowe
 */
public class Delay extends Java {

    private long period = 1000;
                                   
    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public void execute() throws BuildException {
        try {
            long l = System.currentTimeMillis() + getPeriod();
            while(l > System.currentTimeMillis()) {
                long r = l - System.currentTimeMillis();
                log(r + " ms left ");
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            throw new BuildException(e.toString(), e);
        }
    }
}
