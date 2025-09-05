package server.handler;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler implements Closeable {

    private static final int OUTBOX_CAPACITY = 1000;   // giới hạn queue
    private static final int RESET_EVERY = 500;        // reset cache sau mỗi N gói

    private final Socket socket;
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    private final String username;

    private final BlockingQueue<Object> outbox = new LinkedBlockingQueue<>(OUTBOX_CAPACITY);
    private final Thread writerThread;
    private final AtomicBoolean running = new AtomicBoolean(true);

    // đếm để reset() định kỳ
    private int writeCount = 0;

    public ClientHandler(Socket socket, String username,
                         ObjectInputStream reader, ObjectOutputStream writer) {
        this.socket = socket;
        this.username = username;
        this.reader = reader;
        this.writer = writer;

        this.writerThread = new Thread(this::drainOutbox, "writer-" + username);
        this.writerThread.setDaemon(true);
        this.writerThread.start();
    }

    public String getUsername() { return username; }
    public Socket getSocket() { return socket; }
    public ObjectOutputStream getWriter() { return writer; }   // giữ để tương thích
    public ObjectInputStream  getReader() { return reader; }
    public ObjectOutputStream getOos() { return writer; }      // alias
    public ObjectInputStream  getOis() { return reader; }

    public boolean isAlive() {
        return running.get() && !socket.isClosed();
    }

    /** Gửi (non-blocking). Trả về false nếu đã đóng. */
    public boolean send(Object obj) {
        if (!isAlive()) return false;
        return outbox.offer(obj); // không chặn; có thể rơi nếu đầy
    }

    /** Gửi có timeout, giúp không rơi gói nếu tạm thời nghẽn. */
    public boolean trySend(Object obj, long timeoutMs) {
        if (!isAlive()) return false;
        try {
            return outbox.offer(obj, timeoutMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private void drainOutbox() {
        try {
            while (running.get()) {
                Object obj = outbox.take(); // chờ gói
                // writer chỉ dùng ở thread này -> không cần synchronized,
                // nhưng giữ synchronized để phòng code khác gọi getWriter() rồi ghi tay.
                synchronized (writer) {
                    writer.writeObject(obj);
                    writer.flush();
                    // reset cache định kỳ để tránh RAM tăng do shared refs
                    if (++writeCount % RESET_EVERY == 0) {
                        writer.reset();
                    }
                }
            }
        } catch (InterruptedException ie) {
            // dừng
        } catch (IOException ioe) {
            System.err.println("[ClientHandler] Write error -> " + username + ": " + ioe.getMessage());
        } finally {
            try { close(); } catch (IOException ignored) {}
        }
    }

    @Override
    public void close() throws IOException {
        if (!running.getAndSet(false)) return;
        writerThread.interrupt();
        try { reader.close(); } catch (Exception ignored) {}
        try { writer.close(); } catch (Exception ignored) {}
        try { socket.close(); } catch (Exception ignored) {}
    }

    @Override
    public String toString() {
        return "ClientHandler{" + username + ", alive=" + isAlive() + "}";
    }
}
