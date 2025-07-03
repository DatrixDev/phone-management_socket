package client.view.form.PhieuNhap;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import server.DAO.SanPhamDAO;
import shared.models.ChiTietPhieuNhapDTO;
import shared.models.SanPham;
import shared.models.cauHinhSanPham;


import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

public class ExportPDF {

    public static void ExportPDF(int maPhieu, String nhaCungCap, String nguoiThucHien, Timestamp thoiGianTao, List<ChiTietPhieuNhapDTO> chiTietList) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file PDF");
        fileChooser.setSelectedFile(new File("Chi_Tiet_Phieu_Nhhap_" + maPhieu + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            exportPhieuNhapToPDF(new File(filePath), maPhieu, nhaCungCap, nguoiThucHien, thoiGianTao, chiTietList);
        }
    }

    public static void exportPhieuNhapToPDF(File saveFile, int maPhieu, String nhaCungCap, String nguoiThucHien, Timestamp thoiGianTao, List<ChiTietPhieuNhapDTO> chiTietList) {
        try {
            // Load font từ resources
            InputStream is = ExportPDF.class.getResourceAsStream("/client/fonts/arial.ttf");
            System.out.println(is);

            if (is == null) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy font: \"/client/fonts/arial.ttf\"");
                return;
            }

            BaseFont baseFont = BaseFont.createFont(
                    "arial.ttf",
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED,
                    true,
                    is.readAllBytes(),
                    null
            );

            Font unicodeFont = new Font(baseFont, 12);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);
            Font titleFont = new Font(baseFont, 22, Font.BOLD);
            Font italicFont = new Font(baseFont, 12, Font.ITALIC);
            Font totalFont = new Font(baseFont, 14, Font.BOLD);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(saveFile));
            document.open();

            // Tiêu đề
            Paragraph title = new Paragraph("THÔNG TIN PHIẾU NHẬP", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            document.add(title);

            // Thông tin chung
            Paragraph para;

            para = new Paragraph("Mã phiếu: PN-" + maPhieu, unicodeFont);
            para.setSpacingAfter(5f);
            document.add(para);

            para = new Paragraph("Nhà cung cấp: " + nhaCungCap, unicodeFont);
            para.setSpacingAfter(5f);
            document.add(para);

            para = new Paragraph("Người thực hiện: " + nguoiThucHien, unicodeFont);
            para.setSpacingAfter(5f);
            document.add(para);

            para = new Paragraph("Thời gian nhập: " + thoiGianTao.toString(), unicodeFont);
            para.setSpacingAfter(10f);
            document.add(para);

            // Bảng chi tiết sản phẩm
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"Tên sản phẩm", "Phiên bản", "Đơn giá", "Số lượng", "Thành tiền"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            double tongTien = 0;
            DecimalFormat df = new DecimalFormat("#,###");

            for (ChiTietPhieuNhapDTO ct : chiTietList) {
                String tenSP = ct.getTenSP();
                int masp = ct.getMaSP();
                SanPham sanPham = SanPhamDAO.getSanPhamById(masp);
                cauHinhSanPham cauhinh = sanPham.getCauHinhs();

                String config = cauhinh.getRom() + " - " + cauhinh.getRam() + " - " + cauhinh.getMausac();
                double donGia = ct.getDonGia();
                int soLuong = ct.getSoLuong();
                double thanhTien = donGia * soLuong;
                tongTien += thanhTien;

                table.addCell(new Phrase(tenSP, unicodeFont));
                table.addCell(new Phrase(config, unicodeFont));
                table.addCell(new Phrase(df.format(donGia) + "đ", unicodeFont));
                table.addCell(new Phrase(String.valueOf(soLuong), unicodeFont));
                table.addCell(new Phrase(df.format(thanhTien) + "đ", unicodeFont));
            }

            document.add(table);

            // Tổng thành tiền
            Paragraph tong = new Paragraph("Tổng thành tiền: " + df.format(tongTien) + "đ", totalFont);
            tong.setAlignment(Element.ALIGN_RIGHT);
            tong.setSpacingBefore(10f);
            document.add(tong);

            // Chữ ký
            document.add(new Paragraph(" "));
            PdfPTable signTable = new PdfPTable(3);
            signTable.setWidthPercentage(100);
            signTable.setSpacingBefore(30f);

            signTable.addCell(getSignCell("Người lập phiếu", italicFont));
            signTable.addCell(getSignCell("Nhân viên nhận", italicFont));
            signTable.addCell(getSignCell("Nhà cung cấp", italicFont));

            document.add(signTable);
            document.close();

            JOptionPane.showMessageDialog(null, "Xuất PDF thành công:\n" );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF: " + e.getMessage());
        }
    }

    private static PdfPCell getSignCell(String text, Font font) {
        Phrase phrase = new Phrase("\n\n\n\n\n" + text + "\n(Ký và ghi rõ họ tên)", font);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }
}
