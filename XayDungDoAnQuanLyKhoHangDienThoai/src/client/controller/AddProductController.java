package client.controller;

import client.view.form.SanPham.AddProductForm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AddProductController {
    private AddProductForm form;

    public AddProductController(AddProductForm form) {
        this.form = form;
        initController();
    }

    private void initController() {
        form.getBtnSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProduct();
            }
        });

        form.getBtnCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	form.setVisible(false);
            }
        });
    }

    private void saveProduct() {
        System.out.println("Đã bấm Lưu");
    }
}
