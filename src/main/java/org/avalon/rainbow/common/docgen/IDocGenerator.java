package org.avalon.rainbow.common.docgen;

import java.util.Map;

public interface IDocGenerator {

    Map<String, Object> getParameters();

    String getTemplate();

    byte[] generate() throws Exception;
}
