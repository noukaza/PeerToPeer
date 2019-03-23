package client.folder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;

public class Folder {

    private static final String PATH = Paths.get(".").toAbsolutePath().normalize().toString()+"/src/client/src";
    private File folder;

    public Folder() {
        this.folder = new File(PATH);
    }

    public ArrayList<client.folder.File> listFilesForFolder() {
        ArrayList<client.folder.File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            files.add(new client.folder.File(fileEntry.getName(), fileEntry.length()));
        }
        return files;
    }

    public Blob getFile(String file){
        File files = new File(PATH+file);

        return null;
    }

    public void ceateFile(String nameFile,String c) throws IOException {
        FileOutputStream fos = new FileOutputStream(PATH+"/"+nameFile);
        fos.write(c.getBytes());
        fos.flush();
        fos.close();
	
    }
    

}
