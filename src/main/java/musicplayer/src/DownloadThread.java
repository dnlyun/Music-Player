/**
 * [DownloadThread.java]
 * Allows program to download songs without freezing the GUI
 *
 * @author Daniel Yun
 * @version %I%, %G%
 */
package musicplayer.src;

public class DownloadThread implements Runnable {

    private String videoId;

    DownloadThread(String videoId){ this.videoId = videoId; }

    @Override
    public void run() {
        AppManageDownload download = new AppManageDownload();
        download.download(this.videoId);
    }
}
