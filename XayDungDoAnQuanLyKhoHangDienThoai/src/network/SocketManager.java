package network;

import shared.request.FileMessage;
import shared.response.DeleteMessageBroadcast;
import shared.response.GroupUserListResponse;
import shared.response.HistoryResponse;
import shared.response.LoginResponse;
import shared.models.SanPham;
import shared.request.ChatMessage;
import shared.request.GroupListUpdate;
import shared.request.UserListUpdate;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class SocketManager {
    private static SocketManager instance;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private final String host = "localhost";
    private final int port = 8888;

    private volatile boolean running = false; // Điều khiển vòng lặp lắng nghe

    // Single listeners (một callback)
    private Consumer<LoginResponse> loginListener;
    private Consumer<UserListUpdate> userListListener;
    private Consumer<GroupListUpdate> groupListListener;
    private Consumer<ChatMessage> chatListener;
    private Consumer<GroupUserListResponse> groupUserListListener;
    private Consumer<List<SanPham>> productListListener;

    // NEW: listeners cho lịch sử & xóa tin nhắn
    private Consumer<HistoryResponse> historyListener;
    private Consumer<DeleteMessageBroadcast> deleteListener;

    // Multi listeners (nhiều callback)
    private final List<Consumer<ChatMessage>> chatListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<FileMessage>> fileListeners = new CopyOnWriteArrayList<>();

    private SocketManager() throws Exception {
        this.connect();
    }

    private void connect() throws Exception {
        socket = new Socket(host, port);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
    }

    public static SocketManager getInstance() throws Exception {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public static void resetInstance() {
        if (instance != null) {
            instance.stopListening();  // Dừng thread trước khi đóng socket
            instance.close();
            instance = null;
        }
    }

    public void close() {
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Object request) throws IOException {
        if (oos == null) throw new IOException("Output stream is null.");
        synchronized (oos) {
            oos.reset();
            oos.writeObject(request);
            oos.flush();
        }
    }

    public Object receive() throws IOException, ClassNotFoundException {
        synchronized (ois) {
            return ois.readObject();
        }
    }

    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }

    public void startListening() {
        System.out.println("[SocketManager] Start listening thread...");
        running = true;

        new Thread(() -> {
            while (running && isConnected()) {
                try {
                    Object obj;
                    synchronized (ois) {
                        obj = ois.readObject();
                    }

                    // ======== ROUTING =========
                    if (obj instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof SanPham) {
                        if (productListListener != null) {
                            @SuppressWarnings("unchecked")
                            List<SanPham> products = (List<SanPham>) list;
                            SwingUtilities.invokeLater(() -> productListListener.accept(products));
                        }

                    } else if (obj instanceof LoginResponse loginRes) {
                        if (loginListener != null) {
                            SwingUtilities.invokeLater(() -> loginListener.accept(loginRes));
                        }

                    } else if (obj instanceof ChatMessage chatMsg) {
                        System.out.println("[SocketManager] Tin nhắn chat đến: " + chatMsg.getMessage());
                        // 1) Single listener (nếu dùng)
                        if (chatListener != null) {
                            SwingUtilities.invokeLater(() -> chatListener.accept(chatMsg));
                        }
                        // 2) Multi listeners (nếu có đăng ký)
                        if (!chatListeners.isEmpty()) {
                            SwingUtilities.invokeLater(() -> {
                                for (Consumer<ChatMessage> listener : chatListeners) {
                                    listener.accept(chatMsg);
                                }
                            });
                        }

                    } else if (obj instanceof UserListUpdate userListUpdate) {
                        System.out.println("[SocketManager] UserListUpdate received: " + userListUpdate.getOnlineUsers());
                        if (userListListener != null) {
                            SwingUtilities.invokeLater(() -> userListListener.accept(userListUpdate));
                        }

                    } else if (obj instanceof GroupListUpdate groupListUpdate) {
                        System.out.println("[SocketManager] Nhận GroupListUpdate: " + groupListUpdate.getGroupNames());
                        if (groupListListener != null) {
                            SwingUtilities.invokeLater(() -> groupListListener.accept(groupListUpdate));
                        }

                    } else if (obj instanceof GroupUserListResponse res) {
                        if (groupUserListListener != null) {
                            SwingUtilities.invokeLater(() -> groupUserListListener.accept(res));
                        }

                    } else if (obj instanceof FileMessage fileMsg) {
                        if (!fileListeners.isEmpty()) {
                            SwingUtilities.invokeLater(() -> {
                                for (Consumer<FileMessage> listener : fileListeners) {
                                    listener.accept(fileMsg);
                                }
                            });
                        }

                    } else if (obj instanceof DeleteMessageBroadcast del) {
                        if (deleteListener != null) {
                            SwingUtilities.invokeLater(() -> deleteListener.accept(del));
                        }

                    } else if (obj instanceof HistoryResponse historyResponse) {
                        if (historyListener != null) {
                            SwingUtilities.invokeLater(() -> historyListener.accept(historyResponse));
                        } else {
                            if (!chatListeners.isEmpty()) {
                                SwingUtilities.invokeLater(() -> {
                                    for (ChatMessage msg : historyResponse.getMessages()) {
                                        for (Consumer<ChatMessage> listener : chatListeners) {
                                            listener.accept(msg);
                                        }
                                    }
                                });
                            }
                        }

                    } else {
                        System.out.println("[SocketManager] Bỏ qua object không biết: " + obj.getClass().getName());
                    }

                } catch (EOFException | StreamCorruptedException eof) {
                    System.out.println("[SocketManager] EOF - Stream đã bị đóng.");
                    break;
                } catch (SocketException e) {
                    System.out.println("[SocketManager] Socket bị mất kết nối: " + e.getMessage());
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            }

            System.out.println("[SocketManager] Luồng lắng nghe đã dừng.");
        }, "SocketManager-Listener").start();
    }

    public void stopListening() {
        running = false;
        System.out.println("[SocketManager] Dừng lắng nghe server.");
    }

    // ======== Setters for listeners ========
    public void setLoginListener(Consumer<LoginResponse> listener) {
        this.loginListener = listener;
    }

    public void setUserListListener(Consumer<UserListUpdate> listener) {
        this.userListListener = listener;
    }

    public void setGroupListListener(Consumer<GroupListUpdate> listener) {
        this.groupListListener = listener;
    }

    public void setChatListener(Consumer<ChatMessage> listener) {
        this.chatListener = listener;
    }

    public void setProductListListener(Consumer<List<SanPham>> listener) {
        this.productListListener = listener;
    }

    public void setGroupUserListListener(Consumer<GroupUserListResponse> listener) {
        this.groupUserListListener = listener;
    }

    // NEW
    public void setHistoryListener(Consumer<HistoryResponse> listener) {
        this.historyListener = listener;
    }

    // NEW
    public void setDeleteListener(Consumer<DeleteMessageBroadcast> listener) {
        this.deleteListener = listener;
    }

    // ======== Multi-listener registration ========
    public void addChatListener(Consumer<ChatMessage> listener) {
        chatListeners.add(listener);
    }

    public void addFileListener(Consumer<FileMessage> listener) {
        fileListeners.add(listener);
    }

    // ======== Expose raw streams/socket (nếu cần) ========
    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutputStream() {
        return oos;
    }

    public ObjectInputStream getInputStream() {
        return ois;
    }

    // ======== Helpers ========
    public void setProcessingRequest(boolean processing) {
        // Nếu bạn dùng biến này, có thể xử lý riêng
    }

    public void sendFile(String sender, String recipient, String filename, byte[] data, boolean isGroup) throws IOException {
        FileMessage fileMessage = new FileMessage(sender, recipient, filename, data, isGroup);
        send(fileMessage);
    }
}
