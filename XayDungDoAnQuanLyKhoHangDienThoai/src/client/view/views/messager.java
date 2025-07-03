package client.view.views;

import client.controller.messagerController;
import client.view.components.RoundedButton;
import client.view.components.RoundedPanel;
import client.view.shared.BaseView;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import shared.models.NhanVien;
import shared.models.TaiKhoan;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class messager extends BaseView {
    public JTextPane currentChatPane;
    public HashMap<String, JTextPane> userChatPanes = new HashMap<>();
    private JList<String> onlineUsersList;
    private JList<String> groupsList;
    private JTextField messageField;
    private JList<String> listUsersInGroup;
    private JTabbedPane conversationsTabbedPane;
    private JButton sendButton;
    private JButton uploadButton;
    private JButton createGroupButton;
    private JLabel chatTitleLabel;
    private JPanel chatPanel;
    private JLabel userNameLabel;
    private String currentUsername;
    private String currentRecipient = "";
    private JPanel mainChatPanel;
    private boolean isCurrentChatGroup = false;
    private JPanel messagePanel;

    // Màu sắc
    private static final Color SIDEBAR_BG = new Color(230, 240, 255);
    private static final Color CHAT_BG = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(96, 138, 104);
    private static final Color SENT_MSG_COLOR = new Color(220, 248, 198);
    private static final Color RECEIVED_MSG_COLOR = new Color(240, 240, 240);
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color TEXT_MUTED = new Color(120, 120, 120);

    public messager(TaiKhoan tk, NhanVien nv) throws Exception {
        super();
        createChatLayout();
        getMainPanel().add(mainChatPanel, BorderLayout.CENTER);
        new messagerController(this, tk.getUsername(), nv.getHoten());
    }

    private void createChatLayout() {
        mainChatPanel = new JPanel(new BorderLayout());

        // ========== LEFT SIDEBAR ========== //
        JPanel leftPanel = new RoundedPanel(20,SIDEBAR_BG);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));

        // Panel thông tin user
        JPanel userInfoPanel = new RoundedPanel(15);
        userInfoPanel.setLayout(new BorderLayout());
        userInfoPanel.setBackground(new Color(220, 235, 245));
        userInfoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        userNameLabel = new JLabel("Đang tải...");
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userInfoPanel.add(userNameLabel, BorderLayout.CENTER);
        leftPanel.add(userInfoPanel, BorderLayout.NORTH);

        // Panel người dùng online
        JPanel onlineUsersPanel = new RoundedPanel(15);
        onlineUsersPanel.setLayout(new BorderLayout());
        onlineUsersPanel.setBorder(BorderFactory.createTitledBorder("Người dùng trực tuyến"));

        onlineUsersList = new JList<>();
        onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        onlineUsersList.setCellRenderer(new UserListCellRenderer());
        onlineUsersList.setBackground(SIDEBAR_BG);

        JScrollPane onlineUsersScrollPane = new JScrollPane(onlineUsersList);
        onlineUsersScrollPane.setBorder(null);
        onlineUsersPanel.add(onlineUsersScrollPane, BorderLayout.CENTER);
        leftPanel.add(onlineUsersPanel, BorderLayout.CENTER);

        // Panel nhóm
        JPanel groupsPanel = new RoundedPanel(15);
        groupsPanel.setLayout(new BorderLayout());
        groupsPanel.setBorder(BorderFactory.createTitledBorder("Nhóm trò chuyện"));

        groupsList = new JList<>();
        groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsList.setCellRenderer(new UserListCellRenderer());
        groupsList.setBackground(SIDEBAR_BG);

        JScrollPane groupsScrollPane = new JScrollPane(groupsList);
        groupsScrollPane.setBorder(null);
        groupsPanel.add(groupsScrollPane, BorderLayout.CENTER);

        createGroupButton = new RoundedButton("Tạo nhóm mới", 15);
        createGroupButton.setBackground(ACCENT_COLOR);
        createGroupButton.setForeground(Color.WHITE);
        createGroupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        groupsPanel.add(createGroupButton, BorderLayout.SOUTH);

        leftPanel.add(groupsPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new RoundedPanel(20);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(CHAT_BG);

        chatTitleLabel = new JLabel("Chọn người để bắt đầu trò chuyện");
        chatTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chatTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chatTitleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        centerPanel.add(chatTitleLabel, BorderLayout.NORTH);

        // This is the actual panel that holds the message bubbles.
        // It should be part of the JScrollPane.
        messagePanel = new RoundedPanel(10);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(CHAT_BG);

        JScrollPane chatScrollPane = new JScrollPane(messagePanel);
        chatScrollPane.setBorder(null);
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Tabbed pane chứa các hội thoại
        conversationsTabbedPane = new JTabbedPane();
        conversationsTabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // The initial tab should ideally point to a default chat, like "yourself_chat"
        // or be empty until a conversation is selected.
        // For now, let's connect it to the main messagePanel (which holds the current chat history)
        conversationsTabbedPane.addTab("Chat", chatScrollPane);

        // Panel nhập tin nhắn
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setBorder(new EmptyBorder(10, 5, 5, 10));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(10, 15, 10, 15)
        ));

        sendButton = new RoundedButton("Gửi", 15);
        sendButton.setBackground(ACCENT_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        uploadButton = new RoundedButton("📎", 15);
        uploadButton.setBackground(new Color(220, 220, 220));
        uploadButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(uploadButton, BorderLayout.WEST);
        buttonPanel.add(sendButton, BorderLayout.CENTER);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        centerPanel.add(conversationsTabbedPane, BorderLayout.CENTER);
        centerPanel.add(inputPanel, BorderLayout.SOUTH);


        // ========== RIGHT SIDEBAR ========== //
        JPanel rightPanel = new RoundedPanel(20);
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(200, 0));
        rightPanel.setBackground(SIDEBAR_BG);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel groupMembersPanel = new RoundedPanel(15);
        groupMembersPanel.setLayout(new BorderLayout());
        groupMembersPanel.setBorder(BorderFactory.createTitledBorder("Thành viên nhóm"));

        listUsersInGroup = new JList<>();
        listUsersInGroup.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listUsersInGroup.setCellRenderer(new UserListCellRenderer());
        listUsersInGroup.setBackground(SIDEBAR_BG);

        JScrollPane membersScrollPane = new JScrollPane(listUsersInGroup);
        membersScrollPane.setBorder(null);
        groupMembersPanel.add(membersScrollPane, BorderLayout.CENTER);

        rightPanel.add(groupMembersPanel, BorderLayout.CENTER);

        mainChatPanel.add(centerPanel, BorderLayout.CENTER);
        mainChatPanel.add(leftPanel, BorderLayout.WEST);
        mainChatPanel.add(rightPanel, BorderLayout.EAST);
        mainChatPanel.setBorder(BorderFactory.createEmptyBorder(7,7,7,7));

        currentChatPane = new JTextPane();
        currentChatPane.setEditable(false);
    }

    private class UserListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBorder(new EmptyBorder(8, 10, 8, 10));
            if (isSelected) {
                setBackground(ACCENT_COLOR);
                setForeground(Color.WHITE);
            } else {
                setBackground(SIDEBAR_BG);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    public void displayMessage(String sender, String message, boolean yourMessage, String conversationId) {
        SwingUtilities.invokeLater(() -> {

            // Create chat bubble
            JPanel bubble = new JPanel(new BorderLayout());
            bubble.setBackground(yourMessage ? SENT_MSG_COLOR : RECEIVED_MSG_COLOR);
            bubble.setBorder(new CompoundBorder(
                    new RoundBorder(12, yourMessage ? SENT_MSG_COLOR.darker() : RECEIVED_MSG_COLOR.darker()),
                    new EmptyBorder(8, 12, 8, 12)
            ));
            bubble.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

            JTextPane textPane = new JTextPane();
            textPane.setEditorKit(new WrapEditorKit());
            textPane.setText(message);
            textPane.setEditable(false);
            textPane.setOpaque(false);
            textPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            textPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            textPane.setForeground(TEXT_DARK);

            // Create header with sender name and time
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);

            JLabel senderLabel = new JLabel(yourMessage ? "Bạn" : sender);
            senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            senderLabel.setForeground(yourMessage ? new Color(0, 100, 0) : new Color(0, 0, 150));
            String time = new SimpleDateFormat("HH:mm").format(new Date());
            JLabel timeLabel = new JLabel(time);
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            timeLabel.setForeground(TEXT_MUTED);
            headerPanel.add(senderLabel, BorderLayout.WEST);
            headerPanel.add(timeLabel, BorderLayout.EAST);
            bubble.add(headerPanel, BorderLayout.NORTH);
            bubble.add(textPane, BorderLayout.CENTER);
            JPanel messageContainer = new JPanel(new FlowLayout(yourMessage ? FlowLayout.RIGHT : FlowLayout.LEFT, 8, 4));
            messageContainer.setBackground(CHAT_BG);
            messageContainer.add(bubble);
            messageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Add the message container to the messagePanel associated with the current conversation
            // You need to get the correct messagePanel from the conversationsTabbedPane
            Component selectedComponent = conversationsTabbedPane.getSelectedComponent();
            if (selectedComponent instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) selectedComponent).getViewport();
                Component view = viewport.getView();
                if (view instanceof JPanel) { // This should be your messagePanel
                    ((JPanel) view).add(messageContainer);
                    ((JPanel) view).revalidate();
                    ((JPanel) view).repaint();
                }
            }

            // Scroll to the bottom
            JScrollBar vertical = ((JScrollPane) conversationsTabbedPane.getSelectedComponent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    public void displayFile(String sender, String filename, byte[] fileData, boolean yourMessage, String groupName, String conversationId) {
        SwingUtilities.invokeLater(() -> {
            // Create chat bubble for file
            JPanel bubble = new JPanel(new BorderLayout());
            bubble.setBackground(yourMessage ? SENT_MSG_COLOR : RECEIVED_MSG_COLOR);
            bubble.setBorder(new CompoundBorder(
                    new RoundBorder(12, yourMessage ? SENT_MSG_COLOR.darker() : RECEIVED_MSG_COLOR.darker()),
                    new EmptyBorder(8, 12, 8, 12)
            ));
            bubble.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

            // Create header with sender name and time
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setOpaque(false);

            String senderText = yourMessage ? "Bạn" : (groupName.isEmpty() ? sender : "[" + groupName + "] " + sender);
            JLabel senderLabel = new JLabel(senderText);
            senderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            senderLabel.setForeground(yourMessage ? new Color(0, 100, 0) : new Color(0, 0, 150));

            String time = new SimpleDateFormat("HH:mm").format(new Date());
            JLabel timeLabel = new JLabel(time);
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            timeLabel.setForeground(TEXT_MUTED);

            headerPanel.add(senderLabel, BorderLayout.WEST);
            headerPanel.add(timeLabel, BorderLayout.EAST);

            JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            filePanel.setOpaque(false);

            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/documentation.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            JLabel iconLabel = new JLabel(scaledIcon);

            JLabel fileLabel = new JLabel(filename);
            fileLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            fileLabel.setForeground(new Color(0, 100, 200));
            fileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            fileLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    saveFile(filename, fileData);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    fileLabel.setForeground(new Color(0, 150, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    fileLabel.setForeground(new Color(0, 100, 200));
                }
            });

            filePanel.add(iconLabel);
            filePanel.add(fileLabel);

            bubble.add(headerPanel, BorderLayout.NORTH);
            bubble.add(filePanel, BorderLayout.CENTER);

            JPanel messageContainer = new JPanel(new FlowLayout(yourMessage ? FlowLayout.RIGHT : FlowLayout.LEFT, 8, 4));
            messageContainer.setBackground(CHAT_BG);
            messageContainer.add(bubble);
            messageContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Add the message container to the messagePanel associated with the current conversation
            Component selectedComponent = conversationsTabbedPane.getSelectedComponent();
            if (selectedComponent instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) selectedComponent).getViewport();
                Component view = viewport.getView();
                if (view instanceof JPanel) { // This should be your messagePanel
                    ((JPanel) view).add(messageContainer);
                    ((JPanel) view).revalidate();
                    ((JPanel) view).repaint();
                }
            }

            JScrollBar vertical = ((JScrollPane) conversationsTabbedPane.getSelectedComponent()).getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void saveFile(String filename, byte[] fileData) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(filename));
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(fileData);
                JOptionPane.showMessageDialog(this,
                        "File đã được lưu tại: " + file.getAbsolutePath(),
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Hỏi có muốn mở file không
                int open = JOptionPane.showConfirmDialog(this,
                        "Bạn có muốn mở file ngay bây giờ không?",
                        "Mở file",
                        JOptionPane.YES_NO_OPTION);

                if (open == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(file);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi lưu file: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Class RoundBorder cho bong bóng chat
    private class RoundBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius+1, radius+1, radius+1, radius+1);
        }
    }

    // Class WrapEditorKit để tự động xuống dòng
    private class WrapEditorKit extends StyledEditorKit {
        public ViewFactory getViewFactory() {
            return new WrapColumnFactory();
        }
    }

    private class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new LabelView(elem);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(elem) {
                            @Override
                            protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) {
                                if (r == null) {
                                    r = new SizeRequirements();
                                }
                                float pref = layoutPool.getPreferredSpan(axis);
                                float min = layoutPool.getMinimumSpan(axis);
                                r.minimum = (int) min;
                                r.preferred = Math.max(r.minimum, (int) pref);
                                r.maximum = Integer.MAX_VALUE;
                                r.alignment = 0.5f;
                                return r;
                            }
                        };
                    case AbstractDocument.SectionElementName:
                        return new BoxView(elem, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(elem);
                    case StyleConstants.IconElementName:
                        return new IconView(elem);
                    default:
                        return new LabelView(elem);
                }
            }
            return new LabelView(elem);
        }
    }

    public void setUsername(String username) {
        this.currentUsername = username;
        // The "yourself_chat" pane needs to be correctly managed if you're using it for self-messages.
        // If your architecture relies on different JTextPanes for different conversations,
        // then currentChatPane should be set when the conversation changes.
        // For now, this part might need adjustment based on how 'yourself_chat' is used.
        userNameLabel.setText(username);
    }

    public void openConversationTab(String conversationId, boolean isGroupChat) {
        int tabCount = conversationsTabbedPane.getTabCount();
        boolean tabExists = false;
        int tabIndex = 0;
        JScrollPane chatScrollPaneForTab = null;

        // Check if tab already exists
        for (int i = 0; i < tabCount; i++) {
            if (conversationsTabbedPane.getTitleAt(i).equals(conversationId)) {
                tabExists = true;
                tabIndex = i;
                // Get the existing JScrollPane and its messagePanel
                chatScrollPaneForTab = (JScrollPane) conversationsTabbedPane.getComponentAt(i);
                break;
            }
        }

        if (!tabExists) {
            // Create a new messagePanel and JScrollPane for this conversation
            JPanel newConversationMessagePanel = new JPanel();
            newConversationMessagePanel.setLayout(new BoxLayout(newConversationMessagePanel, BoxLayout.Y_AXIS));
            newConversationMessagePanel.setBackground(CHAT_BG);
            newConversationMessagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            chatScrollPaneForTab = new JScrollPane(newConversationMessagePanel);
            chatScrollPaneForTab.setBorder(null);
            chatScrollPaneForTab.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            chatScrollPaneForTab.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            // Add the new tab
            conversationsTabbedPane.addTab(conversationId, chatScrollPaneForTab);
            tabIndex = conversationsTabbedPane.indexOfTab(conversationId);
        }

        // Set the selected tab
        conversationsTabbedPane.setSelectedIndex(tabIndex);

        // Update the current recipient and current message panel reference
        this.currentRecipient = conversationId;
        // The `messagePanel` field in your class should now point to the messagePanel
        // of the currently selected tab.
        // Assuming your `displayMessage` and `displayFile` methods add to `this.messagePanel`,
        // you need to update `this.messagePanel` when the tab changes.
        Component selectedComponent = conversationsTabbedPane.getSelectedComponent();
        if (selectedComponent instanceof JScrollPane) {
            JViewport viewport = ((JScrollPane) selectedComponent).getViewport();
            this.messagePanel = (JPanel) viewport.getView();
        }

        this.isCurrentChatGroup = isGroupChat;
        chatTitleLabel.setText("Chat với: " + conversationId);
    }

    public void closeConversationTab(String conversationId) {
        int tabIndex = conversationsTabbedPane.indexOfTab(conversationId);
        if (tabIndex >= 0) {
            conversationsTabbedPane.remove(tabIndex);
            // userChatPanes.remove(conversationId); // This might not be needed if messagePanels are dynamically managed
        }
        if (conversationsTabbedPane.getTabCount() > 0) {
            conversationsTabbedPane.setSelectedIndex(0);
            this.currentRecipient = conversationsTabbedPane.getTitleAt(0);
            // Update messagePanel reference to the newly selected tab's messagePanel
            Component selectedComponent = conversationsTabbedPane.getSelectedComponent();
            if (selectedComponent instanceof JScrollPane) {
                JViewport viewport = ((JScrollPane) selectedComponent).getViewport();
                this.messagePanel = (JPanel) viewport.getView();
            }
        } else {
            this.currentRecipient = "";
            // If no tabs are open, you might want to clear messagePanel or revert to a default state
            this.messagePanel.removeAll();
            this.messagePanel.revalidate();
            this.messagePanel.repaint();
            chatTitleLabel.setText("Chọn người để bắt đầu trò chuyện");
        }
    }

    // MODIFIED METHOD:
    public void setCurrentRecipient(String recipient) {
        this.currentRecipient = recipient;
        chatTitleLabel.setText("Chat với: " + recipient);

        // Instead of removing all, we need to ensure the correct chat history
        // (the JPanel associated with this recipient's tab) is being displayed.
        // The `openConversationTab` method should handle switching the display.
        // This method should primarily update the label and potentially trigger
        // the tab switch if not already done.

        // If you are calling `setCurrentRecipient` directly to switch conversations
        // without going through `openConversationTab`, you need to ensure the tab
        // is selected and the `messagePanel` field points to the correct JPanel.
        openConversationTab(recipient, false); // Assuming individual chat for direct recipient setting
        // You might need to adjust 'false' based on context.
    }

    public void setCurrentChatPane(JTextPane currentChatPane) { this.currentChatPane = currentChatPane; }
    public HashMap<String, JTextPane> getUserChatPanes() { return userChatPanes; }
    public JList<String> getOnlineUsersList() { return onlineUsersList; }
    public boolean isCurrentChatIsGroup() { return isCurrentChatGroup; }
    public void setCurrentChatIsGroup(boolean isCurrentChatGroup) { this.isCurrentChatGroup = isCurrentChatGroup; }
    public void setUsersInCurrentGroup(List<String> members) { listUsersInGroup.setListData(members.toArray(new String[0])); }
    public JList<String> getGroupsList() { return groupsList; }
    public JTextField getMessageField() { return messageField; }
    public JTabbedPane getConversationsTabbedPane() { return conversationsTabbedPane; }
    public JButton getSendButton() { return sendButton; }
    public JButton getUploadButton() { return uploadButton; }
    public JButton getCreateGroupButton() { return createGroupButton; }
    public String getCurrentRecipient() { return currentRecipient; }
    public JLabel getUserNameLabel() { return userNameLabel; }
    public JPanel getContentPanel() { return mainPanel; }
}