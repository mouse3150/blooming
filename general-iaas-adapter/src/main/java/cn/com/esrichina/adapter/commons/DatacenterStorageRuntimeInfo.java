package cn.com.esrichina.adapter.commons;

public class DatacenterStorageRuntimeInfo {
	/**
	 * 存储名称
	 */
	private String storageName;
	/**
	 * 存储ID
	 */
	private String storageId;
	/**
	 * 存储配额总数
	 */
	private Storage total;
	
	/**
	 * 已用数
	 */
	private Storage used;
	
	/**
	 * 是否限制
	 */
	private boolean isLimited;

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageId() {
		return storageId;
	}

	public void setStorageId(String storageId) {
		this.storageId = storageId;
	}

	public Storage getTotal() {
		return total;
	}

	public void setTotal(Storage total) {
		this.total = total;
	}

	public Storage getUsed() {
		return used;
	}

	public void setUsed(Storage used) {
		this.used = used;
	}

	public boolean isLimited() {
		return isLimited;
	}

	public void setLimited(boolean isLimited) {
		this.isLimited = isLimited;
	}
}
