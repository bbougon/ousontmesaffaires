package fr.bbougon.ousontmesaffaires.test.utils;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class TestAppender extends AppenderBase<ILoggingEvent> {

    public static boolean hasMessageInLevel(final Level level, final String message) {
        List<ILoggingEvent> events = TestAppender.events.get(level);
        return events.stream().anyMatch(event-> message.equals(event.getFormattedMessage()));
    }

    private static Map<Level, List<ILoggingEvent>> events = Maps.newHashMap();

    @Override
    protected void append(ILoggingEvent e) {
        if(events.get(e.getLevel()) != null) {
            events.get(e.getLevel()).add(e);
        } else {
            events.put(e.getLevel(), Lists.newArrayList(e));
        }
    }

}
