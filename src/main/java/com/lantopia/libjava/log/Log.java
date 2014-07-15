package com.lantopia.libjava.log;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 15/07/2014
 *
 * Want a structured, context-sensitive logging notion that emits messages in a table-based format where each message is structured like a JSON message.
 * - sooner or later we always want to process the logfile... let's make it easy from day one
 */
public abstract class Log {
    private Log(final Log context, final String name) {

    }
}
