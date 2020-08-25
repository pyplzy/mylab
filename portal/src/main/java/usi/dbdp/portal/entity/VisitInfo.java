package usi.dbdp.portal.entity;
/**
 * 阅读记录表
 * @author nie.zhengqian
 * 创建时间：2015年3月25日 下午4:56:19
 */

public class VisitInfo {
	
	private long readId;		//主键
	
	private long bulletinId;	//公告ID
	
	private long reader;		//阅读者
	
	private int readCnt;		//阅读次数

	public long getReadId() {
		return readId;
	}

	public void setReadId(long readId) {
		this.readId = readId;
	}

	public long getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(long bulletinId) {
		this.bulletinId = bulletinId;
	}

	public long getReader() {
		return reader;
	}

	public void setReader(long reader) {
		this.reader = reader;
	}

	public int getReadCnt() {
		return readCnt;
	}

	public void setReadCnt(int readCnt) {
		this.readCnt = readCnt;
	}
	
}
