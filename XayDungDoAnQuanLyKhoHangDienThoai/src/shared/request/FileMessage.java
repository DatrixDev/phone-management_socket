package shared.request;

import java.io.Serializable;

public class FileMessage implements Serializable {
    private String sender;
    private String recipient;
    private String fileName;
    private byte[] data;
    private boolean isGroup;

    public FileMessage(String sender, String recipient, String fileName, byte[] data, boolean isGroup) {
        this.sender = sender;
        this.recipient = recipient;
        this.fileName = fileName;
        this.data = data;
        this.isGroup = isGroup;
    }

    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getFileName() { return fileName; }
    public byte[] getData() { return data; }
    public boolean isGroup() { return isGroup; }



    public void setData(byte[] data) {
        this.data = data;
    }

}
