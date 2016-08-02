package com.burkert.urlparser;

import java.util.Map;
import lombok.Data;

@Data
public class ParseResult {

    private String title;
    private String version;
    private Map<Integer, Integer> headingsCountPerLevel;
    private Integer internalLinksCount;
    private Integer externalLinksCount;
    private Integer inaccessibleLinksCount;
    private boolean containsLogin;
}
