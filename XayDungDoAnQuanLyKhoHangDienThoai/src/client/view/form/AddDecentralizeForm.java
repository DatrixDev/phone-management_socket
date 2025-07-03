package client.view.form;

import javax.swing.*;
import javax.swing.border.EmptyBorder;



import client.view.views.DecentralizeView;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import client.BUS.NhomQuyenBUS;
import server.DAO.DanhMucChucNangDAO;
import server.DAO.NhomQuyenDAO;
import shared.models.ChiTietQuyen;
import shared.models.DanhMucChucNang;
import shared.models.NhomQuyen;

import java.awt.*;
import java.util.ArrayList;

public class AddDecentralizeForm extends JDialog {
	private ArrayList<DanhMucChucNang> dmcn;
	private JButton btnAdd;
	private JButton btnCancel;
	private JPanel jpTop, jpLeft;
	private JTextField txtTennhomquyen;
	private int sizeDmCn, sizeHanhdong;
	private JPanel jpCen;
	private JCheckBox[][] listCheckBox;
	private JPanel jpBottom;
	private NhomQuyen nhomQuyen;
	private NhomQuyenDAO nhomQuyenDAO;
	private NhomQuyenBUS nhomquyenBUS;
	private DecentralizeView jpPhanQuyen;
	int index;
	private ArrayList<ChiTietQuyen> ctQuyen;
	String[] mahanhdong = {"view", "create", "update", "delete"};


	public void initComponents() {
		dmcn = DanhMucChucNangDAO.getInstance().selectAll();

		String[] hanhdong = {"Xem", "Tạo mới", "Cập nhật", "Xoá"};

		this.setTitle("Thêm nhóm quyền");
		this.setSize(new Dimension(1000, 580));
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout(0, 0));
		// Hiển thị tên nhóm quyền
		jpTop = new JPanel(new BorderLayout(20, 10));
		jpTop.setBorder(new EmptyBorder(20, 20, 20, 20));
		jpTop.setBackground(Color.WHITE);
		JLabel lblTennhomquyen = new JLabel("Tên nhóm quyền");
		txtTennhomquyen = new JTextField();
		txtTennhomquyen.setPreferredSize(new Dimension(150, 35));
		jpTop.add(lblTennhomquyen, BorderLayout.WEST);
		jpTop.add(txtTennhomquyen, BorderLayout.CENTER);

		// Hiển thị danh mục chức năng
		jpLeft = new JPanel(new GridLayout(dmcn.size() + 1, 1));
		jpLeft.setBackground(Color.WHITE);
		jpLeft.setBorder(new EmptyBorder(0, 20, 0, 14));
		JLabel dmcnl = new JLabel("Danh mục chức năng ");
		dmcnl.setFont(new Font(FlatRobotoFont.FAMILY, Font.BOLD, 15));
		jpLeft.add(dmcnl);
		for (DanhMucChucNang i : dmcn) {
			JLabel lblTenchucnang = new JLabel(i.getTenchucnang());
			jpLeft.add(lblTenchucnang);
		}
		// Hiển thị chức năng CRUD
		sizeDmCn = dmcn.size();
		sizeHanhdong = hanhdong.length;
		jpCen = new JPanel(new GridLayout(sizeDmCn + 1, sizeHanhdong));
		jpCen.setBackground(Color.WHITE);
		listCheckBox = new JCheckBox[sizeDmCn][sizeHanhdong];
		for (int i = 0; i < sizeHanhdong; i++) {
			JLabel lblhanhdong = new JLabel(hanhdong[i]);
			lblhanhdong.setHorizontalAlignment(SwingConstants.CENTER);
			jpCen.add(lblhanhdong);
		}
		for (int i = 0; i < sizeDmCn; i++) {
			for (int j = 0; j < sizeHanhdong; j++) {
				listCheckBox[i][j] = new JCheckBox();
				listCheckBox[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				jpCen.add(listCheckBox[i][j]);
			}
		}
		// Hiển thị nút thêm và xóa
		jpBottom = new JPanel(new FlowLayout());
		jpBottom.setBackground(Color.white);
		jpBottom.setBorder(new EmptyBorder(20, 0, 20, 0));
		btnAdd = new JButton("Thêm nhóm quyền");
		btnAdd.setBorderPainted(false);
		btnAdd.setFont(new Font("Arial", Font.BOLD, 13));
		btnAdd.setPreferredSize(new Dimension(150, 40));
		btnAdd.setBackground(new Color(51, 142, 193));
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btnCancel = new JButton("Hủy bỏ");
		btnCancel.setBorderPainted(false);
		btnCancel.setFont(new Font("Arial", Font.BOLD, 13));
		btnCancel.setPreferredSize(new Dimension(150, 40));
		btnCancel.setBackground(new Color(197, 79, 85));
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		jpBottom.add(btnAdd);
		jpBottom.add(btnCancel);

		// Thêm các Panel vào giao diện
		this.add(jpTop, BorderLayout.NORTH);
		this.add(jpLeft, BorderLayout.WEST);
		this.add(jpCen, BorderLayout.CENTER);
		this.add(jpBottom, BorderLayout.SOUTH);

		this.setVisible(true);
	}

	public AddDecentralizeForm(NhomQuyenBUS nhomQuyenBUS, DecentralizeView jpPhanQuyen) {
		this.nhomquyenBUS = nhomQuyenBUS;
		this.jpPhanQuyen = jpPhanQuyen;
		initComponents();
	}

	public ArrayList<ChiTietQuyen> getListChiTietQuyen(int manhomquyen) {
		ArrayList<ChiTietQuyen> result = new ArrayList<>();
		for (int i = 0; i < sizeDmCn; i++) {
			for (int j = 0; j < sizeHanhdong; j++) {
				if (listCheckBox[i][j].isSelected()) {
					result.add(new ChiTietQuyen(manhomquyen, dmcn.get(i).getMachucnang(), mahanhdong[j]));
				}
			}
		}
		return result;
	}

	public void initUpdate() {
		this.txtTennhomquyen.setText(nhomQuyen.getTennhomquyen());
		System.out.println(ctQuyen);
		for (ChiTietQuyen k : ctQuyen) {
			for (int i = 0; i < sizeDmCn; i++) {
				for (int j = 0; j < sizeHanhdong; j++) {
					if(k.getHanhdong().equals(mahanhdong[j]) && k.getMachucnang().equals(dmcn.get(i).getMachucnang())) {
						listCheckBox[i][j].setSelected(true);
					}
				}
			}
		}
	}
    
    public JButton getBtnAdd() {
		return btnAdd;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JTextField getTxtTennhomquyen() {
		return txtTennhomquyen;
	}

	//	public static void main(String[] args) {
//    	try {
//    		UIManager.setLookAndFeel(new FlatIntelliJLaf());
//			new AddDecentralizeForm().setVisible(true);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
}
