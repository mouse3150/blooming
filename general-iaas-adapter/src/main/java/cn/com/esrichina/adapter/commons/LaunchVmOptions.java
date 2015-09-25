package cn.com.esrichina.adapter.commons;

public class LaunchVmOptions {
	private String vmName;
	private String description;
	private String userData;

	private ImageOptions imageOptions;
	private HardwareOptions hardwareOptions;
	private NetworkConfigOptions networkConfigOptions;
	private GuestCustomizationOptions guestCustomOptions;

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public ImageOptions getImageOptions() {
		return imageOptions;
	}

	public void setImageOptions(ImageOptions imageOptions) {
		this.imageOptions = imageOptions;
	}

	public HardwareOptions getHardwareOptions() {
		return hardwareOptions;
	}

	public void setHardwareOptions(HardwareOptions hardwareOptions) {
		this.hardwareOptions = hardwareOptions;
	}

	public NetworkConfigOptions getNetworkConfigOptions() {
		return networkConfigOptions;
	}

	public void setNetworkConfigOptions(NetworkConfigOptions networkConfigOptions) {
		this.networkConfigOptions = networkConfigOptions;
	}

	public GuestCustomizationOptions getGuestCustomOptions() {
		return guestCustomOptions;
	}

	public void setGuestCustomOptions(GuestCustomizationOptions guestCustomOptions) {
		this.guestCustomOptions = guestCustomOptions;
	}

}
