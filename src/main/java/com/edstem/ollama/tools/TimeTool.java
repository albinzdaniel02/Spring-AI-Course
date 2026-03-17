package com.edstem.ollama.tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TimeTool {

    @Tool(
            name = "getCurrentLocalTime",
            description = "Tool to get the current time in the user's timezone")
    String getCurrentLocalTime() {
        log.info("getCurrentLocalTime() has been called by the LLM");
        return LocalDateTime.now().toString();
    }

    @Tool(
            name = "getCurrentTimeOfTimeZone",
            description = "Tool to get the current time in the specified timezone")
    String getCurrentTimeOfTimeZone(
            @ToolParam(description = "Value to represent the requested timezone ID")
                    String timezone) {
        log.info("getCurrentTimeOfTimeZone() has been called by the LLM");
        String time = LocalDateTime.now(ZoneId.of(timezone)).toString();
        log.info(time);
        return time;
    }
}
