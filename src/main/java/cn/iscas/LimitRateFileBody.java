/**
 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package cn.iscas;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.Args;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author wuheng@iscas.ac.cn
 *
 */
public class LimitRateFileBody extends FileBody {

	private final File file;
    private final String filename;

    //限速的大小
    private int maxRate = 1024;

    public LimitRateFileBody(File file, int maxRate) {
        this(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
        this.maxRate = maxRate;
    }

    public LimitRateFileBody(File file) {
        this(file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
    }

    public LimitRateFileBody(File file, ContentType contentType, String filename) {
        super(file, contentType, filename);
        this.file = file;
        this.filename = filename;
    }

    public LimitRateFileBody(File file, ContentType contentType) {
        this(file, contentType, file != null ? file.getName() : null);
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        Args.notNull(out, "Output stream");
        FileInputStream in = new FileInputStream(this.file);
        LimitInputStream ls = new LimitInputStream(in, new BandwidthLimiter(this.maxRate));
        try {
            byte[] tmp = new byte[4096];

            int l;
            while ((l = ls.read(tmp)) != -1) {
                out.write(tmp, 0, l);
            }

            out.flush();
        } finally {
            in.close();
        }
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }
}
