package ma.emsi.testautomation.controller;

import ma.emsi.testautomation.model.TestReport;
import ma.emsi.testautomation.repository.TestReportRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private TestReportRepository testReportRepository;

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportToPDF() {
        List<TestReport> reports = testReportRepository.findAll();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Paragraph title = new Paragraph("Rapports d'exécution des tests", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 3, 2, 3, 3, 4});

            Stream.of("ID", "Nom du Test", "Statut", "Début", "Fin", "Erreur")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(header);
                    });

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

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=rapports.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(out.toByteArray());

        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }
    // EXPORT CSV
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportToCSV() {
        List<TestReport> reports = testReportRepository.findAll();
        StringBuilder csvBuilder = new StringBuilder();

        // En-tête
        csvBuilder.append("ID,Test Name,Status,Start Time,End Time,Error Message\n");

        // Données
        for (TestReport report : reports) {
            csvBuilder.append(report.getId()).append(",");
            csvBuilder.append(report.getTestName()).append(",");
            csvBuilder.append(report.getStatus()).append(",");
            csvBuilder.append(report.getStartTime() != null ? report.getStartTime() : "").append(",");
            csvBuilder.append(report.getEndTime() != null ? report.getEndTime() : "").append(",");
            csvBuilder.append(report.getErrorMessage() != null ? report.getErrorMessage().replace(",", " ") : "").append("\n");
        }

        byte[] csvBytes = csvBuilder.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rapports.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
    // EXPORT EXCEL
    @GetMapping("/export/excel")
    public ResponseEntity<byte[]> exportToExcel() {
        List<TestReport> reports = testReportRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Rapports");

            // Ligne d'en-tête
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Test Name", "Status", "Start Time", "End Time", "Error Message"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Données
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

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            HttpHeaders headersHttp = new HttpHeaders();
            headersHttp.setContentDisposition(ContentDisposition.attachment().filename("rapports.xlsx").build());
            headersHttp.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            return ResponseEntity.ok().headers(headersHttp).body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la génération du fichier Excel", e);
        }
    }

}
