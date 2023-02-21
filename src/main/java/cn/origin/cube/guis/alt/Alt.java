package cn.origin.cube.guis.alt;

import java.util.UUID;

public class Alt {

    private String username;
    private UUID uuid;
    private final String email;
    private final String password;

    public Alt(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Alt(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
