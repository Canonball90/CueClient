package cn.origin.cube.utils.client.jsonutils;

import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

final class MultiForm {
    private static final String NEWLINE = "\r\n";
    private final String boundary;
    private final PrintWriter writer;
    private final OutputStream outputStream;

    public MultiForm(final String boundary, final OutputStream outputStream) {
        this.boundary = boundary;
        this.outputStream = outputStream;
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
    }

    public void addFile(final String name, final File file) {
        this.writer.append("--").append(this.boundary).append("\r\n");
        this.writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
        this.writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
        this.writer.append("Content-Transfer-Encoding: binary").append("\r\n");
        this.writer.append("\r\n");
        this.writer.flush();
        try {
            final FileInputStream stream = new FileInputStream(file);
            final byte[] buffer = new byte[4096];
            int i;
            while ((i = stream.read(buffer)) != -1) {
                this.outputStream.write(buffer, 0, i);
            }
            this.outputStream.flush();
            stream.close();
            this.writer.append("\r\n");
            this.writer.flush();
        }
        catch (Exception ex) {}
    }

    public void end() {
        this.writer.append("\r\n").flush();
        this.writer.append("--").append(this.boundary).append("--").append("\r\n");
        this.writer.close();
    }
}
