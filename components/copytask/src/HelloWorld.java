import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * The task of the tutorial.
 * Print�s a message or let the build fail.
 *
 * @author Jan Mat�rne
 * @since 2003-08-19
 */
public class HelloWorld extends Task {


    /**
     * Store nested 'message's.
     */
    private final List<Message> messages = new Vector<Message>();

    /**
     * The message to print. As attribute.
     */
    private String message;

    public void setMessage(String msg) {
        message = msg;
    }

    /**
     * Should the build fail? Defaults to <i>false</i>. As attribute.
     */
    boolean fail = false;

    public void setFail(boolean b) {
        fail = b;
    }

    /**
     * Support for nested text.
     */
    public void addText(String text) {
        message = text;
    }


    /**
     * Do the work.
     */
    public void execute() {
        // handle attribute 'fail'
        if (fail)
            throw new BuildException("Fail requested.");

        // handle attribute 'message' and nested text
        if (message != null)
            log(message);

        // handle nested elements
        for (Message msg : messages) 
            log(msg.getMsg());

    }


    /**
     * Factory method for creating nested 'message's.
     */
    public Message createMessage() {
        Message msg = new Message();
        messages.add(msg);
        return msg;
    }

    /**
     * A nested 'message'.
     */
    public class Message {
        // Bean constructor
        public Message() {
        }

        /**
         * Message to print.
         */
        private String msg;

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

}