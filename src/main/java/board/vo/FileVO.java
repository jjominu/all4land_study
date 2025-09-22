package board.vo;

public class FileVO {
	public class BoardFileVO {
		private int seq;
		private int fseq;
		private String originalFileName;
		private String filePath;
		private long fileSize;
		public int getSeq() {
			return seq;
		}
		public void setSeq(int seq) {
			this.seq = seq;
		}
		public int getFseq() {
			return fseq;
		}
		public void setFseq(int fseq) {
			this.fseq = fseq;
		}
		public String getOriginalFileName() {
			return originalFileName;
		}
		public void setOriginalFileName(String originalFileName) {
			this.originalFileName = originalFileName;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public long getFileSize() {
			return fileSize;
		}
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}
		
}
	}
