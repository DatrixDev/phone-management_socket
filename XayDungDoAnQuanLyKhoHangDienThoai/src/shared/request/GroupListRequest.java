// File: shared/request/GroupListRequest.java
package shared.request;

import java.io.Serializable;

public class GroupListRequest implements Serializable {
    private final String username; // Tên người dùng mà chúng ta muốn lấy danh sách nhóm của họ

    public GroupListRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "GroupListRequest{username='" + username + "'}";
    }
}