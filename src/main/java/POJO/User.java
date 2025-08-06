package POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String avatar;


    public User(String email, String password, String name, String avatar) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
    }
    public User() {
    }
    public void updateUser(String email, String password, String name, String avatar) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.avatar = avatar;
        this.role="customer";
    }

    @Override
    public String toString() {
        return "User {" +
                "   email:'" + email + '\''+
                "   password: '"+ password + '\''+
                "   name: '" + name + '\''+
                "   avatar: '" + avatar + '\''+
                "}";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {this.role = role;}

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

}
