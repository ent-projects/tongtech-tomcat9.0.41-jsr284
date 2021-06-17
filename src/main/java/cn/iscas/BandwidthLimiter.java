/**
 * Copyright (2021, ) Institute of Software, Chinese Academy of Sciences
 */
package cn.iscas;

import org.apache.log4j.Logger;
/**
 * @author wuheng@iscas.ac.cn
 *
 */
public class BandwidthLimiter  {

	private static Logger logger = Logger.getLogger(BandwidthLimiter.class);
    //KB代表的字节数
    private static final Long KB = 1024L;
    //一个chunk的大小，单位byte。设置一个块的大小为1M
    private static final Long CHUNK_LENGTH = 1024 * 1024L;

    //已经发送/读取的字节数
    private int bytesWillBeSentOrReceive = 0;
    //上一次接收到字节流的时间戳——单位纳秒
    private long lastPieceSentOrReceiveTick = System.nanoTime();
    //允许的最大速率，默认为 1024KB/s
    private int maxRate = 1024;
    //在maxRate的速率下，通过chunk大小的字节流要多少时间（纳秒）
    private long timeCostPerChunk = (1000000000L * CHUNK_LENGTH) / (this.maxRate * KB);

    public BandwidthLimiter(int maxRate) {
        this.setMaxRate(maxRate);
    }

    //动态调整最大速率
    public void setMaxRate(int maxRate) {
        if (maxRate < 0) {
            throw new IllegalArgumentException("maxRate can not less than 0");
        }
        this.maxRate = maxRate;
        if (maxRate == 0) {
            this.timeCostPerChunk = 0;
        } else {
            this.timeCostPerChunk = (1000000000L * CHUNK_LENGTH) / (this.maxRate * KB);
        }
    }

    public synchronized void limitNextBytes() {
        this.limitNextBytes(1);
    }

    public synchronized void limitNextBytes(int len) {
        this.bytesWillBeSentOrReceive += len;

        while (this.bytesWillBeSentOrReceive > CHUNK_LENGTH) {
            long nowTick = System.nanoTime();
            long passTime = nowTick - this.lastPieceSentOrReceiveTick;
            long missedTime = this.timeCostPerChunk - passTime;
            if (missedTime > 0) {
                try {
                    Thread.sleep(missedTime / 1000000, (int) (missedTime % 1000000));
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            this.bytesWillBeSentOrReceive -= CHUNK_LENGTH;
            this.lastPieceSentOrReceiveTick = nowTick + (missedTime > 0 ? missedTime : 0);
        }
    }
}
