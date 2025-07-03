package client.view.form;

import shared.models.SanPham;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {

    public static void exportSanPhamListToExcel(List<SanPham> danhSachSanPham) {
        if (danhSachSanPham == null || danhSachSanPham.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Danh sách sản phẩm trống!");
            return;
        }
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.endsWith(".xlsx")) filePath += ".xlsx";

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách sản phẩm");

            String[] headers = {
                    "ID", "Tên SP", "Xuất xứ", "Chip", "Pin", "Màn hình",
                    "Cam sau", "Cam trước", "HĐH", "Phiên bản HĐH", "Bảo hành",
                    "Thương hiệu", "Kho","Hình ảnh", "Số lượng", "Màu sắc", "RAM", "ROM",
                    "Giá nhập", "Giá xuất",
            };

            // ======= TẠO STYLE CHO HEADER =========
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // ======= GHI TIÊU ĐỀ =========
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ======= GHI DỮ LIỆU =========
            int rowIndex = 1;
            for (SanPham sp : danhSachSanPham) {
                Object[] values = {
                        sp.getId(), sp.getTenSanPham(), sp.getXuatXu(), sp.getChip(), sp.getDungLuongPin(),
                        sp.getKichThuocMan(), sp.getCamSau(), sp.getCamTruoc(), sp.getHeDieuHanh(),
                        sp.getPhienBanHDH(), sp.getThoiGianBaoHanh(), sp.getThuongHieu(), sp.getKhuVucKho(),sp.getImage_path(),
                        sp.getSoLuong(),sp.getCauHinhs().getMausac(),sp.getCauHinhs().getRam(),sp.getCauHinhs().getRom(),sp.getCauHinhs().getGianhap(),sp.getCauHinhs().getGiaxuat()
                };

                Row valueRow = sheet.createRow(rowIndex++);
                for (int i = 0; i < values.length; i++) {
                    Cell cell = valueRow.createCell(i);
                    Object val = values[i];

                    if (val == null) {
                        cell.setCellValue("");
                    } else if (val instanceof Number) {
                        cell.setCellValue(((Number) val).doubleValue());
                    } else if (val instanceof Boolean) {
                        cell.setCellValue((Boolean) val);
                    }
                    else {
                        cell.setCellValue(val.toString());
                    }
                }
            }

            // ======= TỰ ĐỘNG CĂN CỘT =========
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // ======= GHI FILE =========
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
