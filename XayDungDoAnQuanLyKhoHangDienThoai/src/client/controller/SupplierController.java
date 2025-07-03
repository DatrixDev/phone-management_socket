package client.controller;

import java.util.ArrayList;

import client.view.form.AddSupplierForm;
import client.view.form.EditSupplierForm;
import client.view.form.ViewSupplierForm;
import client.view.views.SupplierView;
import client.BUS.NhaCungCapBUS;
import server.DAO.NhaCungCapDAO;
import shared.models.NhaCungCap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

public class SupplierController {
	private SupplierView view;
	
	public SupplierController(SupplierView view) {
		this.view = view;
		initController();
	}

	private void initController() {
		
		view.getTopPanel().getBtnAdd().addActionListener(e->openAddSupplierForm());
		view.getTopPanel().getBtnEdit().addActionListener(e->openEditSupplierForm());
		view.getTopPanel().getBtnDelete().addActionListener(e -> deleteSupplier());
		view.getTopPanel().getBtnDetail().addActionListener(e->openViewSupplierForm());
		view.getTopPanel().getBtnExport().addActionListener(e->openExcelExport());
		view.getTopPanel().getTxtSearch().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				openSearch();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				openSearch();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				openSearch();
			}
		});
		view.getTopPanel().getBtnReload().addActionListener(e -> reloadTable());
	}

	private void openAddSupplierForm() {
		AddSupplierForm form = new AddSupplierForm();
		
		form.getBtnAdd().addActionListener(e->addSupplier(form));
		form.getBtnCancel().addActionListener(e->form.dispose());
		
		form.setVisible(true);
	}
	
	private void openEditSupplierForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhà cung cấp để sửa!");
			return;
		}

		int id = Integer.parseInt(view.getTable().getValueAt(selectedRow, 0).toString());
		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String diaChi = view.getTable().getValueAt(selectedRow, 2).toString();
		String email = view.getTable().getValueAt(selectedRow, 3).toString();
		String sdt = view.getTable().getValueAt(selectedRow, 4).toString();

		EditSupplierForm form = new EditSupplierForm();
		form.setSupplierData(id, ten, diaChi, email, sdt); // Sửa lại để truyền đúng ID

		form.getBtnSave().addActionListener(e -> saveSupplier(form));
		form.getBtnCancel().addActionListener(e -> form.dispose());

		form.setVisible(true);
	}

	private void deleteSupplier() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhà cung cấp để xóa!");
			return;
		}

		int option = JOptionPane.showConfirmDialog(
				view,
				"Bạn có chắc chắn muốn xóa nhà cung cấp này?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			String mancc = view.getTable().getValueAt(selectedRow, 0).toString();

			int deleted = NhaCungCapDAO.getInstance().delete(mancc);

			if (deleted > 0) {
				JOptionPane.showMessageDialog(view, "Xóa nhà cung cấp thành công!");
				NhaCungCapBUS bus = new NhaCungCapBUS();
				bus.reloadFromDB();
				view.loadDataTable(NhaCungCapDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(view, "Xóa nhà cung cấp thất bại!");
			}
		}
	}


	private void openViewSupplierForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhà cung cấp để xem!");
			return;
		}

		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String diaChi = view.getTable().getValueAt(selectedRow, 2).toString();
		String email = view.getTable().getValueAt(selectedRow, 3).toString();
		String sdt = view.getTable().getValueAt(selectedRow, 4).toString();

		ViewSupplierForm form = new ViewSupplierForm();
		form.setSupplierData(ten, diaChi, email, sdt);
		form.getBtnCancel().addActionListener(e -> form.dispose());
		form.setVisible(true);
	}
	
	private void addSupplier(AddSupplierForm form) {
		String ten = form.getTxtSupplierName().getText().trim();
		String email = form.getTxtSupplierEmail().getText().trim();
		String sdt = form.getTxtSupplierPhone().getText().trim();
		String diaChi = form.getTxtSupplierAddress().getText().trim();

		if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || email.isEmpty()) {
			JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
			return;
		}

		if (!sdt.matches("\\d+")) {
			JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
			return;
		}

		int id = NhaCungCapDAO.getInstance().getAutoIncrement();
		NhaCungCap ncc = new NhaCungCap(id, ten, diaChi, email, sdt);

		int inserted = NhaCungCapDAO.getInstance().insert(ncc);

		if (inserted > 0) {
			JOptionPane.showMessageDialog(form, "Thêm nhà cung cấp thành công!");
			form.dispose();
			NhaCungCapBUS bus = new NhaCungCapBUS();
			bus.reloadFromDB();
			view.loadDataTable(NhaCungCapDAO.getInstance().selectAll());
		} else {
			JOptionPane.showMessageDialog(form, "Thêm nhà cung cấp thất bại!");
		}
	}
	
	private void saveSupplier(EditSupplierForm form) {
		try {
			int id = form.getSupplierId(); // ✅ Lấy ID đúng từ form
			String ten = form.getTxtSupplierName().getText().trim();
			String sdt = form.getTxtSupplierPhone().getText().trim();
			String diaChi = form.getTxtSupplierAddress().getText().trim();
			String email = form.getTxtSupplierEmail().getText().trim();

			if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			if (!sdt.matches("\\d+")) {
				JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
				return;
			}

			NhaCungCap ncc = new NhaCungCap(id, ten, diaChi, email, sdt);

			int updated = NhaCungCapDAO.getInstance().update(ncc);

			if (updated > 0) {
				JOptionPane.showMessageDialog(form, "Cập nhật nhà cung cấp thành công!");
				form.dispose();
				NhaCungCapBUS bus = new NhaCungCapBUS();
				bus.reloadFromDB();
				view.loadDataTable(NhaCungCapDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(form, "Cập nhật thất bại!");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(form, "Đã xảy ra lỗi khi cập nhật nhà cung cấp!");
			ex.printStackTrace();
		}
	}

	private void openExcelExport() {
		try {
			ArrayList<NhaCungCap> suppliers = NhaCungCapDAO.getInstance().selectAll();

			if (suppliers.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Không có dữ liệu để xuất Excel!");
				return;
			}

			javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
			fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
			fileChooser.setSelectedFile(new java.io.File("DanhSachNhaCungCap.xlsx"));

			int userSelection = fileChooser.showSaveDialog(view);
			if (userSelection != javax.swing.JFileChooser.APPROVE_OPTION) return;

			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filePath.endsWith(".xlsx")) {
				filePath += ".xlsx";
			}

			// Tạo workbook Excel
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Danh sách nhà cung cấp");

			// Tạo header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Email", "Số điện thoại" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);

				CellStyle style = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				style.setFont(font);
				cell.setCellStyle(style);
			}

			// Ghi dữ liệu khách hàng
			int rowNum = 1;
			for (NhaCungCap ncc : suppliers) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(ncc.getMancc());
				row.createCell(1).setCellValue(ncc.getTenncc());
				row.createCell(2).setCellValue(ncc.getDiachi());
				row.createCell(3).setCellValue(ncc.getEmail());
				row.createCell(4).setCellValue(ncc.getSdt());
			}

			// Tự động điều chỉnh độ rộng cột
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Ghi file ra đĩa
			java.io.FileOutputStream fileOut = new java.io.FileOutputStream(filePath);
			workbook.write(fileOut);
			fileOut.close();
			workbook.close();

			JOptionPane.showMessageDialog(view, "Xuất Excel thành công: " + filePath);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, "Lỗi khi xuất Excel: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void openSearch() {
		String keyword = view.getTopPanel().getTxtSearch().getText().trim().toLowerCase();
		String type = view.getTopPanel().getCbxChoose().getSelectedItem().toString();
		NhaCungCapBUS bus = new NhaCungCapBUS();
		if (keyword.isEmpty()) {
			bus.reloadFromDB();
			view.loadDataTable(NhaCungCapDAO.getInstance().selectAll());
			return;
		}
		ArrayList<NhaCungCap> filtered = bus.search(keyword, type);
		view.loadDataTable(filtered);
	}

	private void reloadTable() {
		// Xóa nội dung ô tìm kiếm
		view.getTopPanel().getTxtSearch().setText("");

		// Tải lại dữ liệu khách hàng từ DB
		ArrayList<NhaCungCap> suppliers = NhaCungCapDAO.getInstance().selectAll();

		// Hiển thị dữ liệu lên bảng
		view.loadDataTable(suppliers);
	}
	
}
