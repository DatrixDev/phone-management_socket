package client.controller;

import client.helper.JTableExporter;
import client.view.form.EditAccountForm;
import client.view.form.SelectAccountForm;
import client.view.form.ViewAccountForm;
import client.view.views.AccountView;
import server.DAO.NhomQuyenDAO;
import server.DAO.TaiKhoanDAO;
import shared.models.NhanVien;
import shared.models.NhomQuyen;
import shared.models.TaiKhoan;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;




public class AccountController {
	private AccountView view;
	private NhanVien nhanVien;
	private SelectAccountForm selectAccountForm;
	private ArrayList<NhomQuyen> listNq = NhomQuyenDAO.getInstance().selectAll();
	private ArrayList<TaiKhoan> listTK = TaiKhoanDAO.getInstance().selectAll();

	public AccountController(AccountView view) {
		this.view = view;
		initController();
	}

	private void initController() {
		view.getTopPanel().getBtnAdd().addActionListener(e -> openSelectAccountForm());
		view.getTopPanel().getBtnEdit().addActionListener(e->openEditAccountForm());
		view.getTopPanel().getBtnDelete().addActionListener(e -> deleteAccount());
		view.getTopPanel().getBtnDetail().addActionListener(e->openViewEmployeeForm());
		view.getTopPanel().getBtnExport().addActionListener(e->exportTable());
		view.getTopPanel().getCbxChoose().addItemListener(view);
		view.getTopPanel().getTxtSearch().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String type = (String) view.getTopPanel().getCbxChoose().getSelectedItem();
				String txt = view.getTopPanel().getTxtSearch().getText();
				view.listTk = view.taiKhoanBus.search(txt, type);
				view.loadTableData(view.listTk);
			}
		});
		view.getTopPanel().getBtnReload().addActionListener(e -> reloadTable());
	}

	private void openSelectAccountForm() {
		SelectAccountForm form = new SelectAccountForm(view);

		form.setVisible(true);
	}

	private void openEditAccountForm() {
		int index = view.getRowSelected();
		if (index != -1) {
			EditAccountForm form = new EditAccountForm(view, view.listTk.get(index));
			form.getPanelContent().remove(form.getLblPass());
			form.getPanelContent().remove(form.getTxtAccountPass());
//			form.getTxtAccountPass().setEnabled(false);

			form.setVisible(true);
		}
	}

	private void deleteAccount() {
		int index = view.getRowSelected();
		if (index != -1) {
			int input = JOptionPane.showConfirmDialog(null,
					"Bạn có chắc chắn muốn xóa tài khoản :)!", "Xóa tài khoản",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (input == 0) {
				TaiKhoanDAO.getInstance().delete(view.listTk.get(index).getManv() + "");
				view.loadTableData(view.taiKhoanBus.getTaiKhoanAll());
			}
		}
	}

	private void openViewEmployeeForm() {
		int index = view.getRowSelected();
		if (index != -1) {
			ViewAccountForm form = new ViewAccountForm(view, view.listTk.get(index));
			form.getPanelContent().remove(form.getLblPass());
			form.getPanelContent().remove(form.getTxtAccountPass());
			form.getTxtAccountName().setEnabled(false);
			form.getTxtAccountRights().setEnabled(false);
			form.getTxtAccountStatus().setEnabled(false);
//			form.getTxtAccountPass().setEnabled(false);

			form.setVisible(true);
		}
	}


	private void exportTable() {
		try {
			JTableExporter.exportJTableToExcel(view.getTable());
		} catch (IOException ex) {
			Logger.getLogger(TaiKhoan.class.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void reloadTable() {
		// Xóa nội dung ô tìm kiếm
		view.getTopPanel().getTxtSearch().setText("");
		view.loadTableData(view.taiKhoanBus.getTaiKhoanAll());
	}


}
