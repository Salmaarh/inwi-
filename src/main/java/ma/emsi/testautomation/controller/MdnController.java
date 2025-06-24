package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.service.MdnService;
import ma.emsi.testautomation.service.WebServiceExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mdn")
@CrossOrigin("*")
public class MdnController {

    private final MdnService mdnService;
    private final Map<String, WebServiceExecutor> services;

    public MdnController(MdnService mdnService, Map<String, WebServiceExecutor> services) {
        this.mdnService = mdnService;
        this.services = services;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            List<String> mdns = mdnService.extractMdnsFromFile(file);
            return ResponseEntity.ok(mdns);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/test-create")
    public ResponseEntity<String> testCreate() {
        WebServiceExecutor executor = services.get("CreateSubscriber");
        String result = executor.execute(Map.of());
        return ResponseEntity.ok(result);
    }



}
