package peer.util;



import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import peer.Client;
import peer.logger.IClientLogger;
import peer.peer.Peer;

public class Deserialisation {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    private ByteBuffer byteBuffer;
    private IClientLogger iClientLogger;
    private Client client;

    public Deserialisation(Client client) {
        this.byteBuffer = client.getByteBuffer();
        this.iClientLogger = client.getiClientLogger();
        this.client = client;
    }

    public void start() throws IOException {
        this.byteBuffer.flip();
        int id = this.byteBuffer.get();
        switch (id) {
            case 1:
                this.message(id);
                this.client.commandSendPort(2222);
                break;
            case 3:
                this.iClientLogger.command(id);
                this.client.commandGetList();
                this.client.commandSendListPeer();
                break;
            case 4:
                this.listPairs(id);
                break;
            case 5:
                this.iClientLogger.command(id);
                this.client.commandSendFileList();
                this.client.commandGetFileList();
                break;
            case 6:
                this.listFile(id);
                break;
            case 7:
            	getFile(id);
            	this.iClientLogger.command(id);
                this.client.commandGetFile("test2.c", 1757, 0, 1757); 
                break;
            case 8:
                file(id);
                break;
            default:
                this.iClientLogger.error(id);
        }
        this.byteBuffer.clear();

    }

    private void message(int id) {
        this.iClientLogger.connected(id, this.getString());
    }

    private void listPairs(int id) {
        int paire = this.getInt();
        this.iClientLogger.listLength(paire);
        for (int i = 0; i < paire; i++) {
            var port = this.getInt();
            String address = this.getString();
            this.client.getPeers().add(new Peer(port, address));
            this.iClientLogger.listPeer(id, port, address);
        }
        byteBuffer.clear();
    }

    private void listFile(int id) {
        int nbFile = this.getInt();
        this.iClientLogger.fileLength(nbFile);
        for (int i = 0; i < nbFile; i++) {
            this.iClientLogger.listFile(id, this.getString(), this.getLong());
        }
    }

    private void getFile(int id) {
        String fileName = getString();
        long sizeFile = getLong();
        long pointer = getLong();
        int fragment = getInt();
        this.iClientLogger.file(id, fileName, sizeFile, pointer, fragment);
        this.byteBuffer.clear();
    }
   
    private void file(int id) throws IOException{
        String fileName = getString();
        long sizeFile = getLong();
        long pointer = getLong();
        int fragment = getInt();
        this.iClientLogger.file(id, fileName, sizeFile, pointer, fragment);

        int limit = this.byteBuffer.limit();
        this.byteBuffer.limit(this.byteBuffer.position() + fragment);
        String message = CHARSET.decode(this.byteBuffer).toString();
        this.byteBuffer.limit(limit);
        //System.out.println(message);
        this.client.getFolder().ceateFile(fileName, message);
        this.byteBuffer.clear();

    }


    private int getInt() {
        return this.byteBuffer.getInt();
    }

    private String getString() {
        int stringSize = this.getInt();
        int limit = this.byteBuffer.limit();
        this.byteBuffer.limit(this.byteBuffer.position() + stringSize);
        String message = CHARSET.decode(this.byteBuffer).toString();
        this.byteBuffer.limit(limit);
        return message;
    }

    private long getLong() {
        return this.byteBuffer.getLong();
    }

}