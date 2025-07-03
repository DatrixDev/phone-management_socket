package server;

import client.helper.BCrypt;
import server.DAO.TaiKhoanDAO;
import server.DAO.TinNhanDAO;
import server.handler.ProductHandler;
import server.handler.ClientHandler;
import shared.response.GroupUserListResponse;
import shared.response.HistoryResponse;
import shared.response.LoginResponse;
import shared.models.Group;
import shared.request.*;
import shared.request.ChatMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Server {
    private static final TaiKhoanDAO taiKhoanDao = TaiKhoanDAO.getInstance();
    private static final List<Group> groups = Collections.synchronizedList(new ArrayList<>());


    public static List<ClientHandler> clientHandlers = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("[Server] Running on port 8888...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] Client connected: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleClient(Socket clientSocket) {
        new Thread(() -> {
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            ClientHandler clientHandler = null;

            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());

                Object firstObj = ois.readObject();


                if (!(firstObj instanceof LoginRequest loginRequest)) {
                    System.err.println("[Server] Client không gửi LoginRequest, đóng kết nối.");
                    close(clientSocket, ois, oos);
                    return;
                }
                LoginResponse loginResponse = handleLogin(loginRequest);
                oos.writeObject(loginResponse);
                oos.flush();

                if (!loginResponse.isSuccess()) {
                    close(clientSocket, ois, oos);
                    return;
                }

                var account = loginResponse.getTaiKhoan();
                clientHandler = new ClientHandler(clientSocket, account.getUsername(), ois, oos);
                clientHandlers.add(clientHandler);

                System.out.println("[Server] Client logged in as " + clientHandler.getUsername());
                broadcastOnlineUsers();



                System.out.println("[Server] Đang có client: " + clientHandlers.stream()
                        .map(ClientHandler::getUsername)
                        .toList());


                while (true) {
                    Object obj = ois.readObject();

                    if (obj instanceof AddFullProductRequest req) {
                        ProductHandler.handleAddFullProduct(req, oos);
                    } else if (obj instanceof DeleteProductRequest req) {
                        ProductHandler.handleDeleteProduct(req, oos);
                    } else if (obj instanceof EditSanPhamRequest req) {
                        ProductHandler.handleEditSanPham(req, oos);
                    } else if (obj instanceof EditCauhinhRequest req) {
                        ProductHandler.handleEditCauHinh(req, oos);
                    } else if (obj instanceof String command && command.equals("LOGOUT")) {
                        System.out.println("[Server] " + clientHandler.getUsername() + " logged out");
                        break;
                    } else if (obj instanceof FileMessage fileMsg) {
                        if (fileMsg.isGroup()) {
                            Group group = groups.stream()
                                    .filter(g -> g.getName().equals(fileMsg.getRecipient()))
                                    .findFirst()
                                    .orElse(null);

                            if (group != null) {
                                for (ClientHandler handler : clientHandlers) {
                                    if (group.contains(handler.getUsername())) {
                                        handler.send(fileMsg);
                                    }
                                }
                            }
                        } else {
                            for (ClientHandler handler : clientHandlers) {
                                if (handler.getUsername().equals(fileMsg.getRecipient())) {
                                    handler.send(fileMsg);
                                    break;
                                }
                            }
                        }
                    } else if (obj instanceof HistoryRequest req) {
                        List<ChatMessage> history = TinNhanDAO.layTinNhan(
                                req.getFromUser(),
                                req.getToUser(),
                                req.isGroup()
                        );

                        clientHandler.send(new HistoryResponse(history));
                        System.out.println("[Server] Gửi HistoryResponse cho " + clientHandler.getUsername() +
                                " (" + history.size() + " tin nhắn)");

                    } else if (obj instanceof CreateGroupRequest req) {
                        Group group = new Group(req.getGroupName());

                        for (String member : req.getMembers()) {
                            group.addMember(member);
                        }

                        groups.add(group);

                        System.out.println("[Server] ✅ Nhóm '" + req.getGroupName() + "' được tạo với các thành viên: " + req.getMembers());

                        for (String memberUsername : req.getMembers()) {
                            sendUserGroupListUpdate(memberUsername); // Cập nhật danh sách nhóm cho từng thành viên
                        }
                    } else if (obj instanceof GroupUserListRequest req) {
                        Group group = groups.stream()
                                .filter(g -> g.getName().equals(req.getGroupName()))
                                .findFirst()
                                .orElse(null);

                        if (group != null) {
                            clientHandler.send(new GroupUserListResponse(group.getName(), group.getMembers()));
                        }
                    } else if (obj instanceof GroupListRequest req) {
                        System.out.println("[Server] Nhận GroupListRequest từ: " + req.getUsername());
                        List<String> userGroupNames = new ArrayList<>();
                        synchronized (groups) {
                            for (Group group : groups) {
                                if (group.contains(req.getUsername())) {
                                    userGroupNames.add(group.getName());
                                }
                            }
                        }
                        clientHandler.send(new GroupListUpdate(userGroupNames));
                        System.out.println("[Server] Đã gửi GroupListUpdate cho " + req.getUsername() + ": " + userGroupNames.size() + " nhóm.");
                    } else if (obj instanceof ChatMessage chatMessage) {
                        TinNhanDAO.luuTinNhan(chatMessage);

                        if (chatMessage.isGroup()) {
                            Group group = groups.stream()
                                    .filter(g -> g.getName().equals(chatMessage.getGroupName()))
                                    .findFirst()
                                    .orElse(null);

                            if (group != null) {
                                for (ClientHandler handler : clientHandlers) {
                                    if (group.contains(handler.getUsername())) {
                                        handler.send(chatMessage);
                                    }
                                }
                            } else {
                                clientHandler.send(new ChatMessage(
                                        "server",
                                        "Hệ thống",
                                        chatMessage.getSender(),
                                        "Nhóm không tồn tại.",
                                        false
                                ));
                            }
                        } else {
                            System.out.println("🧾 [DEBUG] Người gửi (senderUsername): " + chatMessage.getSender());
                            System.out.println("🎯 [DEBUG] Người nhận (recipient): " + chatMessage.getRecipient());

                            ClientHandler target = Server.clientHandlers.stream()
                                    .filter(h -> h.getUsername().equalsIgnoreCase(chatMessage.getRecipient())) // <- ignore case nếu cần                            System.out.println("📃 [DEBUG] Danh sách user online: " + Server.clientHandlers.stream().map(ClientHandler::getUsername).toList());

                                    .findFirst().orElse(null);

                            System.out.println("[DEBUG] recipientHandler: " + target);


                            if (target != null) {
                                target.send(chatMessage);
                            } else {
                                clientHandler.send(new ChatMessage(
                                        "server",
                                        "Hệ thống",
                                        clientHandler.getUsername(),
                                        "Người nhận không online.",
                                        false
                                ));

                            }
                        }
                    }

                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("[Server] Error: " + e.getMessage());
            } finally {
                if (clientHandler != null) {
                    clientHandlers.remove(clientHandler);
                    String username = clientHandler.getUsername();

                    synchronized (groups) {
                        for (Group group : groups) {
                            if (group.getMembers().remove(username)) {
                                broadcastGroupMembers(group.getName());

                            }
                        }
                    }
                }

                broadcastOnlineUsers();
                broadcastGroupList();
                close(clientSocket, ois, oos);
            }

        }).start();
    }

    public static void broadcastGroupList() {
        List<String> groupNames = groups.stream().map(Group::getName).toList();
        for (ClientHandler handler : clientHandlers) {
            handler.send(new GroupListUpdate(groupNames));
        }
    }

    public static void broadcastOnlineUsers() {
        List<String> users = clientHandlers.stream().map(ClientHandler::getUsername).toList();
        System.out.println("[Server] Broadcasting user list: " + users);
        for (ClientHandler handler : clientHandlers) {
            handler.send(new UserListUpdate(users));
        }
    }

    public static void sendUserGroupListUpdate(String username) {
        ClientHandler targetHandler = clientHandlers.stream()
                .filter(h -> h.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (targetHandler != null) {
            List<String> userGroupNames = new ArrayList<>();
            synchronized (groups) {
                for (Group group : groups) {
                    if (group.contains(username)) {
                        userGroupNames.add(group.getName());
                    }
                }
            }
            targetHandler.send(new GroupListUpdate(userGroupNames));
            System.out.println("[Server] Cập nhật danh sách nhóm cho " + username + ": " + userGroupNames);
        }
    }


    private static void close(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcastGroupMembers(String groupName) {
        Group group = groups.stream()
                .filter(g -> g.getName().equals(groupName))
                .findFirst()
                .orElse(null);

        if (group == null) return;

        GroupUserListResponse response = new GroupUserListResponse(groupName, group.getMembers());

        for (ClientHandler handler : clientHandlers) {
            if (group.contains(handler.getUsername())) {
                handler.send(response);
            }
        }
    }

    private static LoginResponse handleLogin(LoginRequest loginRequest) {
        return LoginResponse.fromLoginRequest(loginRequest, taiKhoanDao);
    }


}
