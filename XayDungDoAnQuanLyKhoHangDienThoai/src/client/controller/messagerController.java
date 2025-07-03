package client.controller;

import client.view.views.messager;
import network.SocketManager;
import shared.models.NhanVien;
import shared.request.*;
import shared.response.GroupUserListResponse;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class messagerController {
    private final messager view;
    private final String currentUsername;
    private final SocketManager sm = SocketManager.getInstance();
    private String hoten;

    public messagerController(messager view, String currentUsername, String hoten) throws Exception {
        this.view = view;
        this.currentUsername = currentUsername;
        this.hoten = hoten;

        sm.startListening();
        sm.setChatListener(this::onReceiveChatMessage);
        sm.setUserListListener(this::onUserListUpdate);
        sm.setGroupListListener(this::onGroupListUpdate);
        sm.addChatListener(this::onReceiveChatMessage);
        sm.addFileListener(this::onReceiveFileMessage);

        sm.setGroupUserListListener(res -> {
            if (res.getGroupName().equals(view.getCurrentRecipient())) {
                SwingUtilities.invokeLater(() -> view.setUsersInCurrentGroup(res.getMembers()));
            }
        });

        try {
            sm.send(new GroupListRequest(currentUsername)); // <--- New request here
            System.out.println("[CLIENT] Đã gửi yêu cầu lấy danh sách nhóm ban đầu cho: " + currentUsername);
        } catch (IOException e) {
            System.err.println("[CLIENT] Lỗi khi yêu cầu danh sách nhóm ban đầu: " + e.getMessage());
            e.printStackTrace();
        }





        view.getSendButton().addActionListener(e -> sendMessage());
        view.getMessageField().addActionListener(e -> sendMessage());
        view.getCreateGroupButton().addActionListener(e -> createGroupDialog());



        view.getUploadButton().addActionListener(e -> uploadFile());


        view.getOnlineUsersList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = view.getOnlineUsersList().getSelectedValue();
                if (selectedUser != null && !selectedUser.equals(currentUsername)) {
                    view.openConversationTab(selectedUser, false);
                    view.setCurrentRecipient(selectedUser);
                    view.setCurrentChatIsGroup(false);

                    try {
                        // ✅ Debug
                        System.out.println("[DEBUG] Gửi yêu cầu lấy lịch sử từ '" + currentUsername + "' tới '" + selectedUser + "', isGroup=false");
                        sm.send(new shared.request.HistoryRequest(currentUsername, selectedUser, false));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


        view.getGroupsList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedGroup = view.getGroupsList().getSelectedValue();
                if (selectedGroup != null) {
                    view.openConversationTab(selectedGroup, true);
                    view.setCurrentRecipient(selectedGroup);
                    try {
                        sm.send(new GroupUserListRequest(selectedGroup));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    view.setCurrentChatIsGroup(true);

                    try {
                        // ✅ Debug
                        System.out.println("[DEBUG] Gửi yêu cầu lấy lịch sử từ '" + currentUsername + "' tới '" + selectedGroup + "', isGroup=true");
                        sm.send(new shared.request.HistoryRequest(currentUsername, selectedGroup, true));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



        view.getUserNameLabel().setText(hoten);

        view.getConversationsTabbedPane().addChangeListener(e -> {
            int selectedIndex = view.getConversationsTabbedPane().getSelectedIndex();
            if (selectedIndex == -1) {
                System.out.println("[DEBUG] No tab selected.");
                return;
            }

            String tabTitle = view.getConversationsTabbedPane().getTitleAt(selectedIndex);

            ListModel<String> groupModel = view.getGroupsList().getModel();
            boolean isGroup = false;
            for (int i = 0; i < groupModel.getSize(); i++) {
                if (groupModel.getElementAt(i).equals(tabTitle)) {
                    isGroup = true;
                    break;
                }
            }

            view.setCurrentRecipient(tabTitle);
            view.setCurrentChatIsGroup(isGroup);

            System.out.println("[DEBUG] Tab changed. Recipient: " + tabTitle + ", isGroup: " + isGroup);
        });



    }

    private void onUserListUpdate(UserListUpdate update) {
        SwingUtilities.invokeLater(() -> {
            var filteredList = update.getOnlineUsers()
                    .stream()
                    .filter(username -> !username.equals(currentUsername))
                    .toArray(String[]::new);

            view.getOnlineUsersList().setListData(filteredList);
        });
    }
    private void onGroupListUpdate(GroupListUpdate update) {
        System.out.println("[CLIENT] Nhận GroupListUpdate: " + update.getGroupNames());
        SwingUtilities.invokeLater(() -> {
            view.getGroupsList().setListData(update.getGroupNames().toArray(new String[0]));
        });
    }

    private void sendMessage() {
        String text = view.getMessageField().getText().trim();
        String recipient = view.getCurrentRecipient();
        boolean isGroup = view.isCurrentChatIsGroup();

        if (text.isEmpty() || recipient == null || recipient.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Bạn chưa chọn người nhận hoặc nhập tin nhắn.");
            return;
        }
        if(text.equalsIgnoreCase("VKU")){
            text = "Viet Han";
        }

        ChatMessage msg = new ChatMessage(
                currentUsername,
                hoten,
                recipient,
                text,
                isGroup,
                isGroup ? recipient : null
        );

        System.out.println("Bạn đã gửi dòng tin nhắn "+msg);

        try {
            sm.send(msg);

            if (!view.getUserChatPanes().containsKey(recipient)) {
                view.openConversationTab(recipient, isGroup);
            }

            if (!isGroup) {
                view.displayMessage(currentUsername, text, true, recipient);
            }

            view.getMessageField().setText("");
            System.out.println("[CLIENT] Gửi tới: " + recipient + ", isGroup: " + isGroup);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Gửi tin thất bại: " + ex.getMessage());
        }

    }


    private void onReceiveChatMessage(ChatMessage chatMessage) {
        SwingUtilities.invokeLater(() -> {
            String conversationId = chatMessage.isGroup()
                    ? chatMessage.getRecipient()
                    : (chatMessage.getSender().equals(currentUsername)
                    ? chatMessage.getRecipient()
                    : chatMessage.getSender());

            view.openConversationTab(conversationId, chatMessage.isGroup());

            view.setCurrentChatPane(view.getUserChatPanes().get(conversationId));

            // Hiển thị tin nhắn
            view.displayMessage(
                    chatMessage.getSender(),
                    chatMessage.getMessage(),
                    chatMessage.getSender().equals(currentUsername),
                    conversationId
            );
            System.out.println("Client nhận được tin nhắn từ server: " +     chatMessage.getMessage());
            System.out.println("Gọi displayMessage với ID: " + conversationId);

        });
    }

    private void uploadFile() {
        String recipient = view.getCurrentRecipient();
        boolean isGroup = view.isCurrentChatIsGroup();

        if (recipient == null || recipient.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Chọn người hoặc nhóm để gửi file.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(view);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] data = fis.readAllBytes();

                sm.sendFile(currentUsername, recipient, file.getName(), data, isGroup);

                view.displayFile(currentUsername, file.getName(), data, true,
                        isGroup ? recipient : "", recipient);
            } catch (IOException e) {
                e.printStackTrace(); // ghi log ra console
                JOptionPane.showMessageDialog(view,
                        "Không thể gửi file. Vui lòng kiểm tra lại đường dẫn hoặc kết nối mạng.",
                        "Lỗi gửi file",
                        JOptionPane.ERROR_MESSAGE
                );

            }
        }
    }



    private void onReceiveFileMessage(FileMessage fileMsg) {
        SwingUtilities.invokeLater(() -> {
            String conversationId = fileMsg.isGroup()
                    ? fileMsg.getRecipient()
                    : (fileMsg.getSender().equals(currentUsername)
                    ? fileMsg.getRecipient()
                    : fileMsg.getSender());

            view.openConversationTab(conversationId, fileMsg.isGroup());
            view.displayFile(
                    fileMsg.getSender(),
                    fileMsg.getFileName(),
                    fileMsg.getData(),
                    fileMsg.getSender().equals(currentUsername),
                    fileMsg.isGroup() ? fileMsg.getRecipient() : "",
                    conversationId
            );
        });
    }

    private void createGroupDialog() {
        JTextField groupNameField = new JTextField();
        JList<String> userList = view.getOnlineUsersList();
        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(userList);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Tên nhóm:"));
        panel.add(groupNameField);
        panel.add(new JLabel("Chọn thành viên:"));
        panel.add(scrollPane);

        int result = JOptionPane.showConfirmDialog(view, panel, "Tạo nhóm mới", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String groupName = groupNameField.getText().trim();
            var selectedUsers = userList.getSelectedValuesList();

            if (groupName.isEmpty() || selectedUsers.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Tên nhóm và thành viên không được để trống.");
                return;
            }

            selectedUsers.add(currentUsername); // đảm bảo người tạo cũng là thành viên

            try {
                sm.send(new CreateGroupRequest(groupName, selectedUsers));
                JOptionPane.showMessageDialog(view, "Đã gửi yêu cầu tạo nhóm.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Không thể tạo nhóm: " + e.getMessage());
            }
        }
    }

}
