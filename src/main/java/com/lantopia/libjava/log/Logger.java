package com.lantopia.libjava.log;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.lantopia.libjava.util.LibJava.nonNullUnmodifiableList;
import static com.lantopia.libjava.util.LibJava.nonNullUnmodifiableMap;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 15/07/2014
 * <p/>
 * A smartly structured logging system that supports adding rich data to log messages, a fluent interface for composing
 * log messages, and a pluggable back end for logging outputs.  Ultimately it should act as a viable back end for other
 * logging systems as well.
 */
public abstract class Logger {
    private final Map<Thread, LinkedList<Context>> threadContextMap = new HashMap<>();

    /**
     * Produces a thread-specific 'logging context' object.  This attaches a string to a stack of 'contexts' that
     * will be emitted with any log getMessage produced on the thread they are attached to, between when they are created
     * and when they are released (with the {@link Context#close()} method).
     * <p/>
     * This object is suitable for use with a try-with-resources clause.
     * <p/>
     * Example:
     * <code>
     * public void parseXml() {
     * try ( Context c = logger.context("IN-XML-PARSER")) {
     * ...
     * if (!valid(o))
     * logger().getLevel(Warn).getMessage("Validation failed").property("object", o).withTrace().log();
     * }
     * }
     * </code>
     * <p/>
     * The log getMessage above will have IN-XML-PARSER in its context stack, along with any other contexts inserted
     * higher in the call stack for that thread; it will also store a stack getTrace pointing to the point of failure.
     */
    public Context context(final String name) {
        final Thread thread = Thread.currentThread();
        final Context out = new Context(this, thread, name);

        if (!threadContextMap.containsKey(thread)) threadContextMap.put(thread, new LinkedList<Context>());
        final LinkedList<Context> threadContextStack = threadContextMap.get(thread);
        threadContextStack.push(out);

        return out;
    }

    public LogBuilder level(@Nullable final Level v) { return new LogBuilder(this).level(v); }

    public LogBuilder category(@Nullable final String v) { return new LogBuilder(this).category(v); }

    public LogBuilder message(@Nullable final String v) { return new LogBuilder(this).message(v); }

    public LogBuilder error(@Nullable final Throwable v) { return new LogBuilder(this).error(v); }

    public LogBuilder withTrace() { return new LogBuilder(this).withTrace(); }

    public LogBuilder property(final String k, final Object v) { return new LogBuilder(this).property(k, v); }

    abstract void log(LogBuilder builder);

    void popContext(final Context context) {
        threadContextMap.get(context.getThread()).pop();
    }

    protected Map<Thread, LinkedList<Context>> getThreadContextMap() { return threadContextMap; }

    public enum Level {
        /**
         * Majorly verbose information, useful only to developers for a specific system area
         */
        Debug,
        /**
         * Something that is expected to happen and is of passing interest to developers
         */
        Info,
        /**
         * Something that shouldn't happen but is recoverable
         */
        Warn,
        /**
         * Something that shouldn't happen in normal operation, and prevents something important from happening
         */
        Error,
        /**
         * Something that shouldn't be able to happen at all
         */
        Bug
    }

    public static class LogBuilder {
        private final Logger logger;
        @Nullable private Level level = null;
        @Nullable private String category = null;
        @Nullable private String message = null;
        @Nullable private Throwable throwable = null;
        @Nullable private StackTraceElement[] trace = null;
        @Nullable private Map<String, Object> properties = null;

        LogBuilder(final Logger logger) { this.logger = logger; }

        // TODO: Consider: getProperties().property(k,v).property(k,v).end() concept?

        public LogBuilder level(@Nullable final Level v) {
            level = v;
            return this;
        }

        public LogBuilder category(@Nullable final String v) {
            category = v;
            return this;
        }

        public LogBuilder message(@Nullable final String v) {
            message = v;
            return this;
        }

        public LogBuilder error(@Nullable final Throwable v) {
            throwable = v;
            return this;
        }

        public LogBuilder withTrace() {
            trace = new Throwable().fillInStackTrace().getStackTrace();
            return this;
        }

        public LogBuilder property(final String k, final Object v) {
            if (properties == null) properties = new HashMap<>();
            properties.put(k, v);
            return this;
        }

        public LogBuilder clearProperties() {
            properties = null;
            return this;
        }

        public void log() { logger.log(this); }

        @Nullable public Level getLevel() { return level; }

        @Nullable public String getCategory() { return category; }

        @Nullable public String getMessage() { return message; }

        public Throwable getError() { return throwable; }

        public List<StackTraceElement> getTrace() { return nonNullUnmodifiableList(trace); }

        public Map<String, Object> getProperties() { return nonNullUnmodifiableMap(properties); }
    }

    /**
     * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
     * @version 0.1
     * @since 15/07/2014
     * <p/>
     * Resource representing a logging context.
     */
    public static class Context implements Closeable {
        private final Logger logger;
        private final Thread thread;
        private final String name;

        Context(final Logger logger, final Thread thread, final String name) {
            this.logger = logger;
            this.thread = thread;
            this.name = name;
        }

        public String getName() { return name; }

        public Thread getThread() { return thread; }

        @Override
        public void close() throws IOException {
            logger.popContext(this);
        }
    }
}
