package ma.emsi.testautomation.controller

import ma.emsi.testautomation.service.GroovyExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groovy")
public class GroovyController {
    @Autowired
    private GroovyExecutionService groovyExecutionService;

    @PostMapping("/execute")
    public Object executeGroovy(@RequestBody String script) {
        return groovyExecutionService.executeGroovyScript(script);
    }
}
