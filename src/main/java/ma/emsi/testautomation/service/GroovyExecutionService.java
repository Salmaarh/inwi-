package ma.emsi.testautomation.service;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Service;
import groovy.lang.GroovyShell;

@Service
public class GroovyExecutionService {
    public String executeGroovyScript(String scriptContent) {
        CompilerConfiguration config = new CompilerConfiguration();
        GroovyShell shell = new GroovyShell(config);
        Object result = shell.evaluate(scriptContent);
        return result.toString();
    }
}

