package unsw.blackout;

public class File {
    private String filename;
    private String content;

    /**
     * Constructor for a file.
     * @param filename
     * @param content
     */
    public File (String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    /**
     * getter for file name.
     * @return
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * setter for file name.
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * getter for file content.
     * @return
     */
    public String getContent() {
        return this.content;
    }

    /**
     * setter for file content.
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    } 
}
