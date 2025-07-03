package client.view.form.phieuXuat;

import client.view.shared.Toast;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shared.models.phieuXuat;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportExcelPhieuXuat {
    public static void ExportExcelPhieuXuat(JFrame parent, List<phieuXuat> dsPhieuXuat){
        if (dsPhieuXuat == null || dsPhieuXuat.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách phiếu xuất trống!");
            return;
        }

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");

            String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());

            fileChooser.setSelectedFile(new File("PhieuXuat_" + timeStamp + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách phiếu xuất");

            String[] headers = {"STT","Mã phiếu xuất", "Khách hàng", "Nhân viên nhập","Thời gian","Tổng tiền"};

            // ==== Style Header ====
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // ==== Ghi Header ====
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ==== Style các ô ====
            CreationHelper createHelper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            currencyStyle.setAlignment(HorizontalAlignment.RIGHT);

            // ==== Ghi dữ liệu ====
            int rowIndex = 1;
            int stt = 0;
            for (phieuXuat px : dsPhieuXuat) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(++stt);

                Cell cellMaPhieu = row.createCell(1);
                cellMaPhieu.setCellValue(px.getMaphieuXuat());
                cellMaPhieu.setCellStyle(centerStyle);

                row.createCell(2).setCellValue(px.getTenKhachHang());
                row.createCell(3).setCellValue(px.getTennhanvien());

                Cell cellTime = row.createCell(4);
                cellTime.setCellValue(new java.util.Date(px.getThoigiantao().getTime()));
                cellTime.setCellStyle(dateStyle);

                Cell cellTongTien = row.createCell(5);
                cellTongTien.setCellValue(px.getTongTien());
                cellTongTien.setCellStyle(currencyStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ==== Ghi file ====
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            workbook.close();

            ImageIcon successIcon = new ImageIcon(ExportExcelPhieuXuat.class.getResource("/images/success.png"));
            new Toast(parent, "Success", "Xuất Excel thành công!", 1300, successIcon);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi ghi file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi xuất Excel: " + e.getMessage());
        }
    }
}
