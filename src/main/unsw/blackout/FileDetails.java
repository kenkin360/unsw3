package unsw.blackout;

public class FileDetails extends File{

    private int fileSize;
    private boolean hasTransferCompleted;

    /**
     * Constructor for a file information.
     * @param filename
     * @param content
     */
    public FileDetails(String filename, String content, int fileSize, boolean hasTransferCompleted) {
        super(filename, content);
        //TODO Auto-generated constructor stub
        this.fileSize = fileSize;
        this.hasTransferCompleted = hasTransferCompleted;
    }

    /**
     * getter for file size 
     * @return
     */
    public int getFileSize() {
        return this.fileSize;
    }

    /**
     * setter for file size
     * @param fileSize
     */
    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * getter for has the file completely been transferred
     * @return
     */
    public boolean getHasTransferCompleted() {
        return this.hasTransferCompleted;
    }

    /**
     * setter for has the file completely been transferred
     * @param hasTransferCompleted
     */
    public void setHasTransferCompleted(boolean hasTransferCompleted) {
        this.hasTransferCompleted = hasTransferCompleted;
    }
}
