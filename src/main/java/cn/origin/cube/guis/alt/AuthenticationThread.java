package cn.origin.cube.guis.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;
import java.net.Proxy;

public class AuthenticationThread implements Runnable {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Alt alt;
    private String status;

    public AuthenticationThread(Alt alt) {
        this.alt = alt;
    }

    private Session createSession(String username, String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            alt.setUsername(auth.getSelectedProfile().getName());
            alt.setUuid(auth.getSelectedProfile().getId());
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException e) {
            status = "\247cLogin failed.";
            return null;
        }
    }

    @Override
    public void run() {
        status = "\247e\247kLogging in...";
        Field session = null;
        for (Field field : Minecraft.class.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("session") || field.getType() == Session.class) {
                session = field;
            }
        }

        if (session == null)
            session = Minecraft.class.getDeclaredFields()[28];

        session.setAccessible(true);

        if (alt.getPassword().equals("")) {
            Session newSession = new Session(alt.getEmail(), "", "", "mojang");
            try {
                session.set(mc, newSession);
                status = "\247aLogged in as\247r " + newSession.getUsername();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Session newSession = createSession(alt.getEmail(), alt.getPassword());
            if (newSession != null) {
                try {
                    session.set(mc, newSession);
                    status = "\247aLogged in as\247r " + newSession.getUsername();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getStatus() {
        return status;
    }

}
