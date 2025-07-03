package client.view.form.phieuXuat;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import server.DAO.SanPhamDAO;
import shared.models.ChiTietPhieuXuatDTO;
import shared.models.SanPham;
import shared.models.cauHinhSanPham;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.List;

public class ExportPDFPhieuXuat {

    public static void ExportPDFPhieuXuat(int maPhieu, String khachHang, String nguoiThucHien, Timestamp thoiGianTao, List<ChiTietPhieuXuatDTO> chiTietList) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file PDF");
        fileChooser.setSelectedFile(new File("Chi_Tiet_Phieu_Xuat_" + maPhieu + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            exportPhieuxuatToPDF(new File(filePath), maPhieu, khachHang, nguoiThucHien, thoiGianTao, chiTietList);
        }
    }

    public static void exportPhieuxuatToPDF(File saveFile, int maPhieu, String khachHang, String nguoiThucHien, Timestamp thoiGianTao, List<ChiTietPhieuXuatDTO> chiTietList) {
        try {
            // Load font từ resources
            InputStream is = ExportPDFPhieuXuat.class.getResourceAsStream("/client/fonts/arial.ttf");
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
            Paragraph title = new Paragraph("THÔNG TIN PHIẾU XUẤT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(15f);
            document.add(title);

            // Thông tin chung
            Paragraph para;

            para = new Paragraph("Mã phiếu nhập: PN-" + maPhieu, unicodeFont);
            para.setSpacingAfter(5f);
            document.add(para);

            para = new Paragraph("Khách hàng: " + khachHang, unicodeFont);
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

            for (ChiTietPhieuXuatDTO ct : chiTietList) {
                String tenSP = ct.getTenSP();
                int masp = ct.getMaSP();
                SanPham sanPham = SanPhamDAO.getSanPhamById(masp);
                cauHinhSanPham cauhinh = sanPham.getCauHinhs();

                String config = cauhinh.getRom() + " - " + cauhinh.getRam() + " - " + cauhinh.getMausac();
                double donGia = ct.getGiaxuat();
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
            signTable.addCell(getSignCell("Nhân giao", italicFont));
            signTable.addCell(getSignCell("Khách hàng", italicFont));

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
