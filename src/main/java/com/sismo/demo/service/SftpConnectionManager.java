package com.sismo.demo.service;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;

import java.io.File;
import java.io.IOException;

public class SftpConnectionManager {
    private final String server;
    private final String user;
    private final String privateKey;
    private final String passphrase;

    public SftpConnectionManager(String server, String user, String privateKey, String passphrase) {
        this.server = server;
        this.user = user;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
    }

    public SSHClient connect() throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(server);

        File key = new File(privateKey);
        KeyProvider keys = (passphrase != null && !passphrase.isEmpty())
                ? ssh.loadKeys(key.getPath(), passphrase)
                : ssh.loadKeys(key.getPath());
        ssh.authPublickey(user, keys);

        return ssh;
    }
}
