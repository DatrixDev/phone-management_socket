package client.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.helper.JTableExporter;
import client.view.form.AddDecentralizeForm;
import client.view.form.EditDecentralizeForm;
import client.view.form.ViewDecentralizeForm;
import client.view.views.DecentralizeView;
import client.BUS.NhomQuyenBUS;
import server.DAO.NhomQuyenDAO;
import shared.models.ChiTietQuyen;
import shared.models.NhomQuyen;

import javax.swing.*;


public class DecentralizeController {
	private DecentralizeView view;
	private NhomQuyenBUS nhomQuyenBUS = new NhomQuyenBUS();
	private ArrayList<ChiTietQuyen> ctQuyen;


	public DecentralizeController(DecentralizeView view) {
		this.view = view;
		initController();
	}

	private void initController() {

		view.getTopPanel().getBtnAdd().addActionListener(e->openAddDecentralizeForm());
		view.getTopPanel().getBtnEdit().addActionListener(e->openEditEmployeeForm());
		view.getTopPanel().getBtnDetail().addActionListener(e->openViewEmployeeForm());
		view.getTopPanel().getBtnDelete().addActionListener(e -> deleteCustomer());
		view.getTopPanel().getBtnExport().addActionListener(e -> exportTable());
		view.getTopPanel().getTxtSearch().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				ArrayList<NhomQuyen> rs = nhomQuyenBUS.search(view.getTopPanel().getTxtSearch().getText());
				view.loadDataTable(rs);
			}
		});
		view.getTopPanel().getBtnReload().addActionListener(e -> reloadTable());

	}

	private void openAddDecentralizeForm() {
		AddDecentralizeForm form = new AddDecentralizeForm(nhomQuyenBUS, view);

		form.getBtnAdd().addActionListener(e->addDecentralize(form));
		form.getBtnCancel().addActionListener(e->form.dispose());

		form.setVisible(true);
	}

	private void addDecentralize(AddDecentralizeForm form) {
		ctQuyen = form.getListChiTietQuyen(NhomQuyenDAO.getInstance().getAutoIncrement());
		String instered = form.getTxtTennhomquyen().getText();
		if (instered.isEmpty()) {
			JOptionPane.showMessageDialog(view,  "Vui lòng nhập tên nhóm quyền!");
			return;
		}
		nhomQuyenBUS.add(form.getTxtTennhomquyen().getText(),ctQuyen);
		view.loadDataTable(nhomQuyenBUS.getAll());
		form.dispose();
	}

	private void openEditEmployeeForm() {
		int index = view.getRowSelected();
		if (index >= 0) {
			EditDecentralizeForm form = new EditDecentralizeForm(nhomQuyenBUS, view, view.listNhomQuyen.get(index));
			form.initUpdate();
			form.getBtnUpdate().addActionListener(e -> saveDecentralize(form));
			form.getBtnCancel().addActionListener(e -> form.dispose());

			form.setVisible(true);
		}
	}

	private void saveDecentralize(EditDecentralizeForm form) {
		ctQuyen = form.getListChiTietQuyen(form.getNhomQuyen().getManhomquyen());
		NhomQuyen nhomQuyen = new NhomQuyen(form.getNhomQuyen().getManhomquyen(), form.getTxtTennhomquyen().getText());
		nhomQuyenBUS.update(nhomQuyen, ctQuyen, form.getIndex());
		view.loadDataTable(nhomQuyenBUS.getAll());
		form.dispose();
	}


	private void deleteCustomer() {
		int index = view.getRowSelected();
		if (index >= 0) {
			int input = JOptionPane.showConfirmDialog(null,"Bạn có chắc chắn muốn xóa nhóm quyền!", "Xóa nhóm quyền",JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (input == 0) {
				nhomQuyenBUS.delete(view.listNhomQuyen.get(index));
				view.loadDataTable(nhomQuyenBUS.getAll());
			}
		}
	}


	private void openViewEmployeeForm() {
		int index = view.getRowSelected();
		if (index >= 0) {
			ViewDecentralizeForm form = new ViewDecentralizeForm(nhomQuyenBUS, view, view.listNhomQuyen.get(index));
			form.initView();

			form.getBtnCancel().addActionListener(e -> form.dispose());
		}
	}


	private void exportTable() {
		try {
			JTableExporter.exportJTableToExcel(view.getTable());
			JOptionPane.showMessageDialog(null, "Xuất file excel thành công");
		} catch (IOException ex) {
			Logger.getLogger(view.getName()).log(Level.SEVERE, null, ex);
			JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}


	private void reloadTable() {
		// Xóa nội dung ô tìm kiếm
		view.getTopPanel().getTxtSearch().setText("");
		view.loadDataTable(nhomQuyenBUS.getAll());
	}


}
