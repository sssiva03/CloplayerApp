package com.cloplayer.sqlite;

import com.cloplayer.CloplayerService;
import com.cloplayer.tasks.BootstrapTask;
import com.cloplayer.tasks.DownloadTask;
import com.cloplayer.tasks.PlayTask;

public class Story {
	private long id;
	private String url;
	private String domain = "Unknown";
	private String headline = "Unknown";
	private String detail = "Unknown";
	private int itemCount;
	private int downloadProgress;
	private int playProgress;
	private int state;

	public static final int STATE_ADDED = 0;
	public static final int STATE_BOOTSTRAPPED = 1;
	public static final int STATE_DOWNLOADED = 2;
	public static final int STATE_PLAYED = 3;

	DownloadTask downloadTask;
	public PlayTask playTask;
	public BootstrapTask bootstrapTask;

	public void bootstrap() {
		if (bootstrapTask == null) {
			bootstrapTask = new BootstrapTask(this);
			bootstrapTask.execute();
		}
	}

	public void download() {
		if (downloadTask == null) {
			downloadTask = new DownloadTask(this);
			downloadTask.execute();
		}
	}

	public void play() {
		if (playTask == null) {
			playTask = new PlayTask(this);
			playTask.start();
		}
	}

	public void resume() {
		if (playTask == null) {
			playTask = new PlayTask(this, playProgress);
			playTask.start();
		}
	}

	public void stopPlaying() {
		if (playTask != null) {
			playTask.stop(true);
			playTask = null;
			CloplayerService.getInstance().sendIntMessageToUI(CloplayerService.MSG_UPDATE_STORY, (int) getId());
		}
	}

	public void pause() {
		if (playTask != null) {
			playTask.stop(false);
			playTask = null;
		}
	}

	public void play(int count) {
		playTask = new PlayTask(this, count);
		playTask.start();
	}

	public void scroll(int incCount) {
		pause();
		play(playProgress + incCount);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
		if (domain == null || domain == "")
			this.domain = "Unknown";
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
		if (headline == null || headline == "")
			this.headline = "Unknown";
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getDownloadProgress() {
		return downloadProgress;
	}

	public void setDownloadProgress(int downloadProgress) {
		this.downloadProgress = downloadProgress;
	}

	public int getPlayProgress() {
		return playProgress;
	}

	public void setPlayProgress(int playProgress) {
		this.playProgress = playProgress;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		/*
		 * return "" + "id: " + id + "url: " + url + "headline: " + headline +
		 * "ic: " + itemCount + "state: " + state;
		 */
		return headline + " @ " + domain;
	}

	public boolean isPlaying() {
		return playTask != null ? true : false;
	}
}
