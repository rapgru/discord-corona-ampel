package com.rapgru.ampel.discord;

import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {

    private String name;

    private List<String> roles = new ArrayList<>();

    public abstract void execute(Message message, String[] args);

    public void roleNotFound(Message message) {
        message.getChannel().sendMessage("You dont have permissions for this command.").submit();
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void addRole(String roleName) {
        roles.add(roleName);
    }

    public void addRoles(String... roleNames) {
        roles.addAll(Arrays.asList(roleNames));
    }
}
