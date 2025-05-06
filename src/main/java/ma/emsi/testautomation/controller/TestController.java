package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.model.TestEntity;
import ma.emsi.testautomation.model.TestReport;
import ma.emsi.testautomation.model.TestRequest;
import ma.emsi.testautomation.model.TestResult;
import ma.emsi.testautomation.repository.TestReportRepository;
import ma.emsi.testautomation.service.SoapTestExecutorService;
import ma.emsi.testautomation.service.TestService;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.Paragraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/tests")
@CrossOrigin(origins = "*")
public class TestController {

    private final TestService testService;
    private final TestReportRepository testReportRepository;

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    public TestController(TestService testService, TestReportRepository testReportRepository) {
        this.testService = testService;
        this.testReportRepository = testReportRepository;
    }

    // Endpoint : Exécution de test
    @PostMapping("/run")
    public ResponseEntity<?> runTest(@RequestBody TestRequest testRequest) {
        logger.info("Réception d'une requête pour exécuter un test : {}", testRequest.getTestName());
        try {
            TestResult result = testService.executeTest(testRequest);
            logger.info("Test exécuté avec succès : {}", result.getStatus());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Erreur lors de l'exécution du test : {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'exécution du test : " + e.getMessage());
        }
    }

    // Endpoint : Annulation
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelTest(@PathVariable Long id) {
        logger.info("Requête reçue pour annuler le test avec ID : {}", id);
        boolean success = testService.cancelTest(id);
        if (success) {
            logger.info("Test avec ID {} annulé avec succès.", id);
            return ResponseEntity.ok("Test annulé avec succès.");
        } else {
            logger.warn("Échec de l'annulation du test avec ID : {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Échec de l'annulation du test. Vérifiez si l'ID est valide.");
        }
    }

    // Endpoint : Filtrage par statut
    @GetMapping("/status")
    public ResponseEntity<List<TestEntity>> getTestsByStatus(@RequestParam(defaultValue = "ALL") String status) {
        logger.info("Requête pour récupérer les tests avec le statut : {}", status);
        List<TestEntity> tests = testService.getTestsByStatus(status);
        logger.info("Nombre de tests retournés : {}", tests.size());
        return ResponseEntity.ok(tests);
    }

    // Endpoint : Export Excel
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        ByteArrayInputStream stream = exportReportsToExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=rapports.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(stream.readAllBytes());
    }

    public ByteArrayInputStream exportReportsToExcel() {
        List<TestReport> reports = testReportRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Rapports");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Test Name", "Status", "Start Time", "End Time", "Error Message"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowIdx = 1;
            for (TestReport report : reports) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(report.getId());
                row.createCell(1).setCellValue(report.getTestName());
                row.createCell(2).setCellValue(report.getStatus());
                row.createCell(3).setCellValue(report.getStartTime() != null ? report.getStartTime().toString() : "");
                row.createCell(4).setCellValue(report.getEndTime() != null ? report.getEndTime().toString() : "");
                row.createCell(5).setCellValue(report.getErrorMessage() != null ? report.getErrorMessage() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'export Excel", e);
        }
    }

    //  Endpoint : Export CSV
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCSV() {
        ByteArrayInputStream stream = exportReportsToCSV();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=rapports.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_PLAIN)
                .body(stream.readAllBytes());
    }

    public ByteArrayInputStream exportReportsToCSV() {
        List<TestReport> reports = testReportRepository.findAll();
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("ID", "Test Name", "Status", "Start Time", "End Time", "Error Message");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(out), format)) {

            for (TestReport report : reports) {
                printer.printRecord(
                        report.getId(),
                        report.getTestName(),
                        report.getStatus(),
                        report.getStartTime(),
                        report.getEndTime(),
                        report.getErrorMessage()
                );
            }

            printer.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'export CSV", e);
        }
    }

    // Endpoint : Export PDF
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF() {
        ByteArrayInputStream stream = exportReportsToPDF();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=rapports.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(stream.readAllBytes());
    }

    public ByteArrayInputStream exportReportsToPDF() {
        List<TestReport> reports = testReportRepository.findAll();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            com.itextpdf.text.Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph title = new Paragraph("Rapports d'exécution des tests", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 2, 3, 3, 4});

            // En-têtes du tableau
            Stream.of("ID", "Nom du Test", "Statut", "Début", "Fin", "Erreur")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        com.itextpdf.text.Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(header);
                    });

            // Contenu du tableau
            for (TestReport report : reports) {
                table.addCell(String.valueOf(report.getId()));
                table.addCell(report.getTestName());
                table.addCell(report.getStatus());
                table.addCell(report.getStartTime() != null ? report.getStartTime().toString() : "");
                table.addCell(report.getEndTime() != null ? report.getEndTime().toString() : "");
                table.addCell(report.getErrorMessage() != null ? report.getErrorMessage() : "");
            }

            document.add(table);
            document.close();

        } catch (DocumentException ex) {
            throw new RuntimeException("Erreur lors de la génération du PDF", ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
    @RestController
    @RequestMapping("/api/soap-tests")
    public class SoapTestController {

        @Autowired
        private SoapTestExecutorService executor;

        @PostMapping("/run")
        public ResponseEntity<String> runSoapTests() {
            executor.executeSoapUiProject("CHF-soapui-project.xml");
            return ResponseEntity.ok("Tests SOAP exécutés.");
        }
    }
    @PostMapping("/upload-numeros")
    public ResponseEntity<String> uploadNumeros(@RequestParam("file") MultipartFile file) {
        try {
            List<String> numeros = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .collect(Collectors.toList());

            // Injecter dans ta base ou une liste temporaire
            // Ex: numeroService.saveAll(numeros);

            return ResponseEntity.ok("Numéros uploadés avec succès !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur pendant l’upload");
        }

    }

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }

}
