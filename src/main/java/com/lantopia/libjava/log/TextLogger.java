package com.lantopia.libjava.log;

import com.google.common.base.Joiner;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.io.Writer;
import java.util.EnumMap;

/**
 * @author Mark McKenna &lt;mark.denis.mckenna@gmail.com&gt;
 * @version 0.1
 * @since 15/07/2014
 * <p/>
 * Logs to an output stream.  Format is like:
 * >0001 | WRN | yyyy-mm-ddThh:mm:ss,mmm | getCategory | threadName | context1;context2;context3 | getMessage | {json-data-payload}
 * <p/>
 * Columns are more or less fixed-width up to getCategory, after which all bets are off.  If traces, exception and property are included,
 * they'll be in the JSON blob.  The caret at the beginning is to facilitate pasting log messages into email, tickets and
 * so on.
 */
public class TextLogger extends Logger {
    private static final EnumMap<Level, String> LevelNames;

    static {
        LevelNames = new EnumMap<>(Level.class);
        LevelNames.put(Level.Debug, "DBG");
        LevelNames.put(Level.Info, "INF");
        LevelNames.put(Level.Warn, "WRN");
        LevelNames.put(Level.Error, "ERR");
        LevelNames.put(Level.Bug, "BUG");
    }

    private final Writer writer;
    private final int sequenceNumber;

    private TextLogger(final Writer writer) {
        this.writer = writer;
        this.sequenceNumber = 0;
    }

    public static TextLogger make(final Writer writer) { return new TextLogger(writer); }

    @SuppressWarnings({"UseOfSystemOutOrSystemErr", "CallToPrintStackTrace"}) @Override
    void log(final LogBuilder builder) {
        try {
            final Thread cur = Thread.currentThread();
            writer.append(String.format(">%06d | %s | %s | %s | %s | %s | %s | %s",
                    sequenceNumber,
                    LevelNames.get(builder.getLevel()),
                    DateTimeFormat.fullDateTime().print(DateTime.now()),
                    builder.getCategory(),
                    cur.getName(),
                    Joiner.on(';').skipNulls().join(getThreadContextMap().get(cur)),
                    builder.getMessage(),
                    Joiner.on(',').useForNull("null").withKeyValueSeparator(":").join(builder.getProperties())
            ));
        } catch (final IOException e) {
            System.err.println("*** FAILED TO WRITE LOG MESSAGE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
