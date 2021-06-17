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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author wuheng@iscas.ac.cn
 *
 */
public class LimitInputStream extends InputStream  {

	private InputStream inputStream;
    private BandwidthLimiter bandwidthLimiter;

    public LimitInputStream(InputStream inputStream, BandwidthLimiter bandwidthLimiter) {
        this.inputStream = inputStream;
        this.bandwidthLimiter = bandwidthLimiter;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes(len);
        }
        return inputStream.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        if (bandwidthLimiter != null && b.length > 0) {
            bandwidthLimiter.limitNextBytes(b.length);
        }
        return inputStream.read(b);
    }

    @Override
    public int read() throws IOException {
        if (bandwidthLimiter != null) {
            bandwidthLimiter.limitNextBytes();
        }
        return inputStream.read();
    }
}
