package local.smc.backup;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;


public class NGBBackupVerification {
    public static void showJobsByStatus(String reqStatus){
        String filepath = "C:\\Users\\PasinduBhagya\\Documents\\SMC-CLI\\Uploads\\EQ-2025-02-13.xlsx";

        try (FileInputStream file = new FileInputStream(new File(filepath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("+------------------------------------------+------------------------------------------+--------------------------------+-----------------+----------------------+----------------------+");
            System.out.printf("| %-40s | %-40s | %-30s | %-15s | %-20s | %-20s |\n",
                    "Company Name", "Computer Name", "Plugin Name", "Status", "Started", "Finished");
            System.out.println("+------------------------------------------+------------------------------------------+--------------------------------+-----------------+----------------------+----------------------+");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String domain_name = getCellValue(row, 1);
                String client = getCellValue(row, 5);
                String status = getCellValue(row, 8);
                String started = getCellValue(row, 17);
                String finished = getCellValue(row, 18);
                String plugin_name = getCellValue(row, 24);

                String companyName = domain_name.contains("/") ?
                        domain_name.substring(domain_name.lastIndexOf("/") + 1) : domain_name;
                switch (reqStatus) {
                    case "failed":
                        if (Objects.equals(status, "failed")) {
                            System.out.printf("| %-40s | %-40s | %-30s | %-15s | %-20s | %-20s |\n",
                                    companyName, client, plugin_name, status, started, finished);
                        }
                        break;
                    case "success":
                        if (Objects.equals(status, "success")) {
                            System.out.printf("| %-40s | %-40s | %-30s | %-15s | %-20s | %-20s |\n",
                                    companyName, client, plugin_name, status, started, finished);
                        }
                        break;
                    default:
                        System.out.printf("| %-40s | %-40s | %-30s | %-15s | %-20s | %-20s |\n",
                                companyName, client, plugin_name, status, started, finished);

                }
            }
            System.out.println("+------------------------------------------+------------------------------------------+--------------------------------+-----------------+----------------------+----------------------+");
        } catch (IOException e) {
            System.out.println("ERROR: Failed to read the Excel file - " + e.getMessage());
        }
    }

    private static String getCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy h:mm a");
                    yield dateFormat.format(cell.getDateCellValue());
                } else {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }

}
