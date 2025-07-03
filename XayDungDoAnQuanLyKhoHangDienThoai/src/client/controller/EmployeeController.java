package client.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;


import client.view.form.AddEmployeeForm;
import client.view.form.EditEmployeeForm;
import client.view.form.ViewEmployeeForm;
import client.view.form.phanquyen.formPhanquyen;
import client.view.views.EmployeeView;
import client.view.views.StatisticView;
import client.BUS.NhanVienBUS;
import server.DAO.NhanVienDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import shared.models.TaiKhoan;


import javax.swing.*;


public class EmployeeController {
	private EmployeeView view;
	private JTable table;
	public StatisticView thongKe = new StatisticView();

	public EmployeeController(EmployeeView view) {
		this.view = view;
		initController();
	}

	private void initController() {

		view.getTopPanel().getBtnAdd().addActionListener(e->openAddEmployeeForm());
		view.getTopPanel().getBtnEdit().addActionListener(e->openEditEmployeeForm());
		view.getTopPanel().getBtnDelete().addActionListener(e -> deleteEmployee());
		view.getTopPanel().getBtnDetail().addActionListener(e->openViewEmployeeForm());
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

	private void openAddEmployeeForm() {
		AddEmployeeForm form = new AddEmployeeForm();

		form.getBtnAdd().addActionListener(e->addEmployee(form));
		form.getBtnCancel().addActionListener(e->form.dispose());

		form.setVisible(true);
	}

	private void openEditEmployeeForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để sửa!");
			return;
		}

		int id = Integer.parseInt(view.getTable().getValueAt(selectedRow, 0).toString());
		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String gioiTinhStr = view.getTable().getValueAt(selectedRow, 2).toString();
		int gioiTinh = gioiTinhStr.equalsIgnoreCase("Nam") ? 1 : 0;
		Object objNgaySinh = view.getTable().getValueAt(selectedRow, 3);
		Date ngaySinh = null;

		if (objNgaySinh instanceof java.sql.Date) {
			ngaySinh = new Date(((java.sql.Date) objNgaySinh).getTime()); // convert về java.util.Date
		} else if (objNgaySinh instanceof Date) {
			ngaySinh = (Date) objNgaySinh;
		}
		String sdt = view.getTable().getValueAt(selectedRow, 4).toString();
		String email = view.getTable().getValueAt(selectedRow, 5).toString();

		EditEmployeeForm form = new EditEmployeeForm();
		form.setEmployeeData(id, ten, gioiTinh, ngaySinh, sdt, email); // Sửa lại để truyền đúng ID

		form.getBtnSave().addActionListener(e -> saveEmployee(form));
		form.getBtnCancel().addActionListener(e -> form.dispose());

		form.setVisible(true);
	}

	private void deleteEmployee() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để xóa!");
			return;
		}

		int option = JOptionPane.showConfirmDialog(
				view,
				"Bạn có chắc chắn muốn xóa nhân viên này?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION
		);

		if (option == JOptionPane.YES_OPTION) {
			String manv = view.getTable().getValueAt(selectedRow, 0).toString();

			int deleted = NhanVienDAO.getInstance().delete(manv);

			if (deleted > 0) {
				JOptionPane.showMessageDialog(view, "Xóa nhân viên thành công!");
				NhanVienBUS bus = new NhanVienBUS();
				bus.reloadFromDB();
				view.loadDataTable(NhanVienDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(view, "Xóa nhân viên thất bại!");
			}
		}
	}


	private void openViewEmployeeForm() {
		int selectedRow = view.getTable().getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(view, "Vui lòng chọn một nhân viên để xem!");
			return;
		}

		String ten = view.getTable().getValueAt(selectedRow, 1).toString();
		String gioiTinhStr = view.getTable().getValueAt(selectedRow, 2).toString();
		int gioiTinh = gioiTinhStr.equalsIgnoreCase("Nam") ? 1 : 0;
		Object objNgaySinh = view.getTable().getValueAt(selectedRow, 3);
		Date ngaySinh = null;

		if (objNgaySinh instanceof java.sql.Date) {
			ngaySinh = new Date(((java.sql.Date) objNgaySinh).getTime()); // convert về java.util.Date
		} else if (objNgaySinh instanceof Date) {
			ngaySinh = (Date) objNgaySinh;
		}
		String email = view.getTable().getValueAt(selectedRow, 5).toString();
		String sdt = view.getTable().getValueAt(selectedRow, 4).toString();

		ViewEmployeeForm form = new ViewEmployeeForm();
		form.setEmployeeData(ten, gioiTinh, ngaySinh, sdt, email);
		form.getBtnCancel().addActionListener(e -> form.dispose());
		form.setVisible(true);
	}

	private void addEmployee(AddEmployeeForm form) {
		int id = NhanVienDAO.getInstance().getAutoIncrement();
		String ten = form.getTxtEmployeeName().getText().trim();
		int txt_gender = -1;
		if (form.getRBtnMale().isSelected()) {
			System.out.println("Nam");
			txt_gender = 1;
		} else if (form.getRBtnFemale().isSelected()) {
			System.out.println("Nữ");
			txt_gender = 0;
		}
		String email = form.getTxtEmployeeEmail().getText().trim();
		String sdt = form.getTxtEmployeePhone().getText().trim();
		Date date = form.getDateChooser().getDate();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());



		if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
			JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
			return;
		}

		if (!sdt.matches("\\d+")) {
			JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
			return;
		}

		NhanVien nv = new NhanVien(id, ten, txt_gender , sqlDate,  sdt, 1, email);

		int inserted = NhanVienDAO.getInstance().insert(nv);

		if (inserted > 0) {
			JOptionPane.showMessageDialog(form, "Thêm nhân viên thành công!");
			form.dispose();
			NhanVienBUS bus = new NhanVienBUS();
			bus.reloadFromDB();
			view.loadDataTable(NhanVienDAO.getInstance().selectAll());
		} else {
			JOptionPane.showMessageDialog(form, "Thêm nhân viên thất bại!");
		}
	}

	private void saveEmployee(EditEmployeeForm form) {
		try {
			int id = form.getEmployeeId(); // ✅ Lấy ID đúng từ form
			String ten = form.getTxtEmployeeName().getText().trim();
			int txt_gender = -1;
			if (form.getRBtnMale().isSelected()) {
				System.out.println("Nam");
				txt_gender = 1;
			} else if (form.getRBtnFemale().isSelected()) {
				System.out.println("Nữ");
				txt_gender = 0;
			}
			String email = form.getTxtEmployeeEmail().getText().trim();
			String sdt = form.getTxtEmployeePhone().getText().trim();
			Date date = form.getDateChooser().getDate();
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());

			if (ten.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
				JOptionPane.showMessageDialog(form, "Vui lòng nhập đầy đủ thông tin!");
				return;
			}

			if (!sdt.matches("\\d+")) {
				JOptionPane.showMessageDialog(form, "Số điện thoại không hợp lệ!");
				return;
			}

			NhanVien nv = new NhanVien(id, ten, txt_gender, sqlDate, sdt, 1, email);

			int updated = NhanVienDAO.getInstance().update(nv);

			if (updated > 0) {
				JOptionPane.showMessageDialog(form, "Cập nhật nhân viên thành công!");
				form.dispose();
				NhanVienBUS bus = new NhanVienBUS();
				bus.reloadFromDB();
				view.loadDataTable(NhanVienDAO.getInstance().selectAll());
			} else {
				JOptionPane.showMessageDialog(form, "Cập nhật thất bại!");
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(form, "Đã xảy ra lỗi khi cập nhật nhân viên!");
			ex.printStackTrace();
		}
	}


	private void openExcelExport() {
		try {
			ArrayList<NhanVien> employee = NhanVienDAO.getInstance().selectAll();

			if (employee.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Không có dữ liệu để xuất Excel!");
				return;
			}

			javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
			fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
			fileChooser.setSelectedFile(new java.io.File("DanhSachNhanVien.xlsx"));

			int userSelection = fileChooser.showSaveDialog(view);
			if (userSelection != javax.swing.JFileChooser.APPROVE_OPTION) return;

			String filePath = fileChooser.getSelectedFile().getAbsolutePath();
			if (!filePath.endsWith(".xlsx")) {
				filePath += ".xlsx";
			}

			// Tạo workbook Excel
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Danh sách nhân viên");

			// Tạo header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email" };
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
			for (NhanVien nv : employee) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(nv.getManv());
				row.createCell(1).setCellValue(nv.getHoten());
				String gioiTinhStr = (nv.getGioitinh() == 1) ? "Nam" : "Nữ";
				row.createCell(2).setCellValue(gioiTinhStr);
				row.createCell(3).setCellValue(nv.getNgaysinh());
				row.createCell(4).setCellValue(nv.getSdt());
				row.createCell(5).setCellValue(nv.getEmail());
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
	public void setupRightClickMenu() {
		table = view.getTable();

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					int row = table.rowAtPoint(e.getPoint());
					if (row == -1) return;

					table.setRowSelectionInterval(row, row);

					JPopupMenu popupMenu = new JPopupMenu();
					JMenuItem pq = new JMenuItem("Phân quyền");
					popupMenu.add(pq);

					pq.addActionListener(evt -> {
						formPhanquyen form = new formPhanquyen();

						form.getBtnConfirm().addActionListener(e1 -> {
							int newMaNhomQuyen = -1;

							if (form.getRbHome().isSelected()) {
								newMaNhomQuyen = 1;
							} else if (form.getRbProduct().isSelected()) {
								newMaNhomQuyen = 2;
							} else if (form.getRbProductInfo().isSelected()) {
								newMaNhomQuyen = 3;
							} else if (form.getRbImport().isSelected()) {
								newMaNhomQuyen = 4;
							} else if (form.getRbExport().isSelected()) {
								newMaNhomQuyen = 5;
							}

							if (newMaNhomQuyen == -1) {
								JOptionPane.showMessageDialog(form, "Vui lòng chọn một quyền!");
								return;
							}

							int selectedRow = table.getSelectedRow();
							if (selectedRow == -1) return;
							int manv = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());



							boolean updated = TaiKhoanDAO.getInstance().updateQuyen(manv,newMaNhomQuyen);
							if (updated) {
								JOptionPane.showMessageDialog(form, "Cập nhật quyền thành công!");
								form.dispose();



								view.getSidebarMenu().anButtonTheoQuyen(newMaNhomQuyen);

								String tenQuyen = view.getSidebarMenu().anButtonTheoQuyen(newMaNhomQuyen);
								System.out.println("[DEBUG] Tên quyền sau xử lý: " + tenQuyen);

								view.getSidebarMenu().getLblNhomQuyen().setText(tenQuyen);
							} else {
								JOptionPane.showMessageDialog(form, "Cập nhật thất bại!");
							}
						});

						form.setVisible(true);
					});

					popupMenu.show(table, e.getX() + 10, e.getY() + 10);
				}
			}
		});
	}


	private void openSearch() {
		String keyword = view.getTopPanel().getTxtSearch().getText().trim().toLowerCase();
		String type = view.getTopPanel().getCbxChoose().getSelectedItem().toString();
		NhanVienBUS bus = new NhanVienBUS();

		if (keyword.isEmpty()) {
			bus.reloadFromDB();
			view.loadDataTable(NhanVienDAO.getInstance().selectAll());
			return;
		}

		ArrayList<NhanVien> filtered = bus.search(keyword, type);
		view.loadDataTable(filtered);
	}


	private void reloadTable() {
		// Xóa nội dung ô tìm kiếm
		view.getTopPanel().getTxtSearch().setText("");

		// Tải lại dữ liệu khách hàng từ DB
		ArrayList<NhanVien> employee = NhanVienDAO.getInstance().selectAll();

		// Hiển thị dữ liệu lên bảng
		view.loadDataTable(employee);
	}


}