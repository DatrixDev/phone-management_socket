package client.controller;

import javax.swing.JOptionPane;

import client.view.form.KhachHang.AddCustomerForm;
import client.view.form.KhachHang.EditCustomerForm;
import client.view.form.ViewCustomerForm;
import client.view.views.CustomerView;
import client.view.views.StatisticView;
import client.BUS.KhachHangBUS;
import server.DAO.KhachHangDAO;
import shared.models.KhachHang;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;

public class CustomerController {
	private CustomerView view;
	public KhachHangBUS khBUS = new KhachHangBUS();
	public StatisticView thongKe = new StatisticView();
	public CustomerController(CustomerView view) {
		this.view = view;
		initController();
	}

	private void initController() {
		view.getTopPanel().getBtnAdd().addActionListener(e -> openAddCustomerForm());
		view.getTopPanel().getBtnEdit().addActionListener(e -> openEditCustomerForm());
		view.getTopPanel().getBtnDelete().addActionListener(e -> deleteCustomer());
		view.getTopPanel().getBtnDetail().addActionListener(e -> openViewCustomerForm());
		view.getTopPanel().getBtnExport().addActionListener(e -> openExcelExport());
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

	private void openAddCustomerForm() {
		AddCustomerForm form = new AddCustomerForm();

		form.getBtnAdd().addActionListener(e -> addCustomer(form));
		form.getBtnCancel().addActionListener(e -> form.dispose());

		form.setVisible(true);
	}

	private void openEditCustomerForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để sửa!");
			return;
		}

		int id = Integer.parseInt(view.getTable().getValueAt(selectedRow, 0).toString());
		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String diaChi = view.getTable().getValueAt(selectedRow, 2).toString();
		String sdt = view.getTable().getValueAt(selectedRow, 3).toString();

		EditCustomerForm form = new EditCustomerForm();
		form.setCustomerData(id, ten, sdt, diaChi); // Sửa lại để truyền đúng ID

		form.getBtnSave().addActionListener(e -> saveCustomer(form));
		form.getBtnCancel().addActionListener(e -> form.dispose());

		form.setVisible(true);
	}

	private void deleteCustomer() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để xóa!");
			return;
		}

		int option = JOptionPane.showConfirmDialog(
				view,
				"Bạn có chắc chắn muốn xóa khách hàng này?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			String makh = view.getTable().getValueAt(selectedRow, 0).toString();

			int deleted = KhachHangDAO.getInstance().delete(makh);

			if (deleted > 0) {
				JOptionPane.showMessageDialog(view, "Xóa khách hàng thành công!");
				KhachHangBUS bus = new KhachHangBUS();
				bus.reloadFromDB();
				view.loadDataTable(KhachHangDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(view, "Xóa khách hàng thất bại!");
			}
		}
	}


	private void openViewCustomerForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một khách hàng để xem!");
			return;
		}

		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String diaChi = view.getTable().getValueAt(selectedRow, 2).toString();
		String sdt = view.getTable().getValueAt(selectedRow, 3).toString();

		ViewCustomerForm form = new ViewCustomerForm();
		form.setCustomerData(ten, sdt, diaChi);
		form.getBtnCancel().addActionListener(e -> form.dispose());
		form.setVisible(true);
	}

	private void addCustomer(AddCustomerForm form) {
		String ten = form.getTxtCustomerName().getText().trim();
		String sdt = form.getTxtCustomerPhone().getText().trim();
		String diaChi = form.getTxtCustomerAddress().getText().trim();

		if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
			JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
			return;
		}

		if (!sdt.matches("\\d+")) {
			JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
			return;
		}

		int id = KhachHangDAO.getInstance().getAutoIncrement();
		KhachHang kh = new KhachHang(id, ten, sdt, diaChi, new java.sql.Date(System.currentTimeMillis()));

		int inserted = KhachHangDAO.getInstance().insert(kh);

		if (inserted > 0) {
			JOptionPane.showMessageDialog(form, "Thêm khách hàng thành công!");
			form.dispose();
			KhachHangBUS bus = new KhachHangBUS();
			bus.reloadFromDB();
			view.loadDataTable(KhachHangDAO.getInstance().selectAll());
		} else {
			JOptionPane.showMessageDialog(form, "Thêm khách hàng thất bại!");
		}
	}

	private void openExcelExport() {
		try {
			ArrayList<KhachHang> customers = KhachHangDAO.getInstance().selectAll();

			if (customers.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Không có dữ liệu để xuất Excel!");
				return;
			}

			javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
			fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
			fileChooser.setSelectedFile(new java.io.File("DanhSachKhachHang.xlsx"));

			int userSelection = fileChooser.showSaveDialog(view);
			if (userSelection != javax.swing.JFileChooser.APPROVE_OPTION) return;

			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filePath.endsWith(".xlsx")) {
				filePath += ".xlsx";
			}

			// Tạo workbook Excel
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Danh sách khách hàng");

			// Tạo header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Mã KH", "Tên khách hàng", "Số điện thoại", "Địa chỉ", "Ngày tham gia" };
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
			for (KhachHang kh : customers) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(kh.getId());
				row.createCell(1).setCellValue(kh.getTenKhachHang());
				row.createCell(2).setCellValue(kh.getSoDienThoai());
				row.createCell(3).setCellValue(kh.getDiaChi());
				row.createCell(4).setCellValue(kh.getNgayThamGia().toString());
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


	private void saveCustomer(EditCustomerForm form) {
		try {
			int id = form.getCustomerId(); // ✅ Lấy ID đúng từ form
			String ten = form.getTxtCustomerName().getText().trim();
			String sdt = form.getTxtCustomerPhone().getText().trim();
			String diaChi = form.getTxtCustomerAddress().getText().trim();

			if (ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()) {
				JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			if (!sdt.matches("\\d+")) {
				JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
				return;
			}

			KhachHang kh = new KhachHang(id, ten, sdt, diaChi, null);

			int updated = KhachHangDAO.getInstance().update(kh);

			if (updated > 0) {
				JOptionPane.showMessageDialog(form, "Cập nhật khách hàng thành công!");
				form.dispose();
				KhachHangBUS bus = new KhachHangBUS();
				bus.reloadFromDB();
				view.loadDataTable(KhachHangDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(form, "Cập nhật thất bại!");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(form, "Đã xảy ra lỗi khi cập nhật khách hàng!");
			ex.printStackTrace();
		}
	}

	private void openSearch() {
		String keyword = view.getTopPanel().getTxtSearch().getText().trim().toLowerCase();
		String type = view.getTopPanel().getCbxChoose().getSelectedItem().toString();
		KhachHangBUS bus = new KhachHangBUS();
		if (keyword.isEmpty()) {
			bus.reloadFromDB();
			view.loadDataTable(KhachHangDAO.getInstance().selectAll());
			return;
		}
		ArrayList<KhachHang> filtered = bus.search(keyword, type);
		view.loadDataTable(filtered);
	}



	private void reloadTable() {
		// Xóa nội dung ô tìm kiếm
		view.getTopPanel().getTxtSearch().setText("");

		// Tải lại dữ liệu khách hàng từ DB
		ArrayList<KhachHang> customers = KhachHangDAO.getInstance().selectAll();

		// Hiển thị dữ liệu lên bảng
		view.loadDataTable(customers);
	}



}
