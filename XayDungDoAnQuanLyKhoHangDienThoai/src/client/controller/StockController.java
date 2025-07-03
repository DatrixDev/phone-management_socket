package client.controller;

import client.view.form.KhuVucKho.AddStockForm;
import client.view.form.KhuVucKho.EditStockForm;
import client.view.views.StockView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StockController {

    private StockView stockView;

    public StockController(StockView stockView) {
        this.stockView = stockView;
        initController();

        // Đăng ký sự kiện MouseListener cho bảng
        this.stockView.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleRowSelection();
            }
        });
    }

    private void handleRowSelection() {
        int selectedRow = stockView.getTable().getSelectedRow();
        if (selectedRow != -1) {
            String area = stockView.getTable().getValueAt(selectedRow, 1).toString();
            stockView.updateProductList(area);
        }
    }

    private void initController() {
        stockView.getBtnAdd().addActionListener(e -> openAddStockForm());
        stockView.getBtnEdit().addActionListener(e -> openEditStockForm());

    }

    private void openAddStockForm() {
        AddStockForm form = new AddStockForm();
        form.getBtnCancel().addActionListener(e->form.dispose());

        form.setVisible(true);

    }

    private void openEditStockForm(){
        EditStockForm form = new EditStockForm();
        form.getBtnCancel().addActionListener(e->form.dispose());

        form.setVisible(true);
    }
}