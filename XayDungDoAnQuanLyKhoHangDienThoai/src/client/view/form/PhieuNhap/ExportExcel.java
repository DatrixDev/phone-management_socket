package client.view.form.PhieuNhap;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shared.models.phieuNhap;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExportExcel {
    public static void ExportExcel(List<phieuNhap> dsPhieuNhap) {
        if (dsPhieuNhap == null || dsPhieuNhap.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách phiếu nhập trống!");
            return;
        }

        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HHmmss").format(new Date());

            fileChooser.setSelectedFile(new File("PhieuNhap_" + timeStamp + ".xlsx"));
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách phiếu nhập");

            String[] headers = {"STT","Mã phiếu nhập", "Nhà cunng cấp", "Nhân viên nhập","Thời gian","Tổng tiền"};

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

            // ==== Ghi dữ liệu ====
            int rowIndex = 1;
            int stt = 0;
            // Tạo CellStyle cho thời gian
            CellStyle dateStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            short dateFormat = createHelper.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss");
            dateStyle.setDataFormat(dateFormat);

            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat("#,##0.00"));
            currencyStyle.setAlignment(HorizontalAlignment.RIGHT);


            for (phieuNhap pn : dsPhieuNhap) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(++stt);

                Cell cellMaPhieu = row.createCell(1);
                cellMaPhieu.setCellValue(pn.getMaphieu());
                cellMaPhieu.setCellStyle(centerStyle);

                row.createCell(2).setCellValue(pn.getTennhacungcap());
                row.createCell(3).setCellValue(pn.getTennhanvien());

                Cell cellTime = row.createCell(4);
                cellTime.setCellValue(new java.util.Date(pn.getThoigiantao().getTime()));
                cellTime.setCellStyle(dateStyle);

                Cell cellTongTien = row.createCell(5);
                cellTongTien.setCellValue(pn.getTongTien());
                cellTongTien.setCellStyle(currencyStyle); // ✅ định dạng tiền
            }

            // ==== Tự động căn cột ====
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ==== Ghi file ====
            FileOutputStream out = new FileOutputStream(filePath);
            workbook.write(out);
            out.close();
            workbook.close();

            JOptionPane.showMessageDialog(null, "Xuất Excel thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi ghi file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi xuất Excel: " + e.getMessage());
        }
    }
}
